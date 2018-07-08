/*
 *  Copyright (C) 2011 - 2016  DVCONSULTORES

    Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
	    http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
 */

package org.openbizview.etl;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IParameterDefn;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.ReportEngine;
import org.openbizview.util.Bd;
import org.openbizview.util.SeguridadMenuBean;
import org.openbizview.util.TareaInvocar;
import org.openbizview.util.PntGenerica;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.CronScheduleBuilder.*;
import static org.quartz.CalendarIntervalScheduleBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;
import static org.quartz.TriggerKey.*;
import static org.quartz.JobKey.*;





@ManagedBean
@ViewScoped
public class ProgramacionEtl extends Bd implements Serializable {
	
	/**
	 * 
	 */
	 
	private static final long serialVersionUID = 1L;
	
	
	private LazyDataModel<ProgramacionEtl> lazyModel;  
	
	
	/**
	 * @return the lazyModel
	 */
	public LazyDataModel<ProgramacionEtl> getLazyModel() {
		return lazyModel;
	}	
	
	@PostConstruct
	public void init(){
		selectDiasSemana();
	}

	
	//Constructor
	public ProgramacionEtl() throws SchedulerException {
		if(login==null) {
	    	 RequestContext.getCurrentInstance().execute("PF('idleDialog').show()");
	     } else {	
		if (instancia == null){instancia = "999990817832122222";}
		IntStream streamMenu = IntStream.range(0,segu.listMenu.size());
		
		//Recorrer opciones de seguridad y salir si no tiene acceso
		//Recorre todo el menú de la lista de sesión y por opción verifica
		streamMenu.forEach(x -> {
			
			if(segu.listMenu.get(x).opcMenu.equals("M25") && segu.listMenu.get(x).vistaMenu.equals("1")) {
				RequestContext.getCurrentInstance().execute("PF('idleDialogNP').show()");
			}
		});
		//System.out.println("Estatus TRIGGER1: " + schd.checkExists(triggerKey("TRIGGER1", "unico")));
		//System.out.println("Estatus TRIGGER2: " + schd.checkExists(triggerKey("TRIGGER2", "unico")));
		//System.out.println("Estatus TRIGGER3: " + schd.checkExists(triggerKey("TRIGGER3", "unico")));	

		String[] vecreporte = reporte.split("\\ - ", -1);
		inputs = new String[paramsNumber(vecreporte[0])];
		lazyModel  = new LazyDataModel<ProgramacionEtl>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 7217573531435419432L;
			
            @Override
			public List<ProgramacionEtl> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
            	//Filtro
            	if (filters != null) {
               	 for(Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
               		 String filterProperty = it.next(); // table column name = field name
                     filterValue = filters.get(filterProperty);
               	 }
                }
            	try { 
            		//Consulta
					select(first, pageSize,sortField, filterValue);
					//Counter
					counter(filterValue);
					//Contador lazy
					lazyModel.setRowCount(rows);  //Necesario para crear la paginación
				} catch (SQLException | NamingException e) {	
					e.printStackTrace();
				}             
				return list;  
            } 
            
		};
		
	     }
	}
	
	public void reset() {
		mailreporteFiltro = null;
		mailfrecuenciaFiltro = null;
		mailgrupoFiltro = null;
    }
	
	//public void init() throws SchedulerException, NamingException{
	//	recuperarTriggers();
	//}
	private String session = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("session"); //Session
	private String instancia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instancia"); //Usuario logeado
	private String tarea = ""; //Nombre de la tarea
    private String vltrigger = ""; //Nombre del disparador
    private String diasemana = ""; //Día de la semana
    private String frecuencia = ""; //Fercuencia de repetición
    private String dias = ""; // Días de repetición del reporte
    private String diames = "1"; //Día del mes personalizado
    private String horarepeticion = "0";
    private String asunto = "";
    private String contenido = "";
    private Date diainicio = new Date();
    private Object filterValue = "";
	private List<ProgramacionEtl> list = new ArrayList<ProgramacionEtl>();
    private int rows;
    FacesMessage msj = null; 
    PntGenerica consulta = new PntGenerica();
    private String idgrupo = "";
    private String reporte = "";
    ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext(); //Toma ruta real de la aplicación
	SchedulerFactory schdFact = new StdSchedulerFactory();
    Scheduler schd = schdFact.getScheduler();
    java.util.Date fechadia = new java.util.Date();
    private String login = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario"); //Usuario logeado
	
    
    SeguridadMenuBean segu = new SeguridadMenuBean();
    
  //Programación lista tabla
  	private String vdisparador;
  	private String vgrupo;
  	private String vdiasem;
  	private String vfrecuencia;
  	private String vfrecuenciades;
  	private String vasunto;
  	private String vcontenido;
  	private String vcodrep;
  	private String vjob;
  	private String vidgrupo;
  	private String viddesidgrupo;
  	private String vdiames;
    private String vdiainicio;
    private String vactivadetalletb;
    private String vparamvalues;
    private String vintervalo;
    
    //Para detener las tareas
    private String activa;
    private String clase;
    private String statusIncon;
    
    
    //Variables para tomar los nombres de parámetros y pasarlos a la tabla
    private String[][] vlTabla;
    String arregloParametros; //Toma el valor del arreglo de los parámetros del reporte  
    String arregloNombreParametros; //Toma el valor del arreglo de los parámetros del reporte  
    
    //Opciones de envío
    private String opctareas = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("opc"); 
    private String ruta_salida = "URL"; //Directorio de envío
    
    //Formatos
    private String formato;
    private String vformato;
    
    //Filtros de búsqueda
    private String mailreporteFiltro = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("mailreporte"); 
    private String mailfrecuenciaFiltro = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("mailfrecuencia");
    private String mailgrupoFiltro = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("mailgrupo");


	/**
	 * @return the statusIncon
	 */
	public String getStatusIncon() {
		return statusIncon;
	}

	/**
	 * @param statusIncon the statusIncon to set
	 */
	public void setStatusIncon(String statusIncon) {
		this.statusIncon = statusIncon;
	}

	/**
	 * @return the mailreporteFiltro
	 */
	public String getMailreporteFiltro() {
		return mailreporteFiltro;
	}

	/**
	 * @param mailreporteFiltro the mailreporteFiltro to set
	 */
	public void setMailreporteFiltro(String mailreporteFiltro) {
		this.mailreporteFiltro = mailreporteFiltro;
	}

	/**
	 * @return the mailfrecuenciaFiltro
	 */
	public String getMailfrecuenciaFiltro() {
		return mailfrecuenciaFiltro;
	}

	/**
	 * @param mailfrecuenciaFiltro the mailfrecuenciaFiltro to set
	 */
	public void setMailfrecuenciaFiltro(String mailfrecuenciaFiltro) {
		this.mailfrecuenciaFiltro = mailfrecuenciaFiltro;
	}

	/**
	 * @return the mailgrupoFiltro
	 */
	public String getMailgrupoFiltro() {
		return mailgrupoFiltro;
	}

	/**
	 * @param mailgrupoFiltro the mailgrupoFiltro to set
	 */
	public void setMailgrupoFiltro(String mailgrupoFiltro) {
		this.mailgrupoFiltro = mailgrupoFiltro;
	}

	/**
	 * @return the formato
	 */
	public String getFormato() {
		return formato;
	}

	/**
	 * @param formato the formato to set
	 */
	public void setFormato(String formato) {
		this.formato = formato;
	}

	/**
	 * @return the vformato
	 */
	public String getVformato() {
		return vformato;
	}

	/**
	 * @param vformato the vformato to set
	 */
	public void setVformato(String vformato) {
		this.vformato = vformato;
	}

	/**
	 * @return the ruta_salida
	 */
	public String getRuta_salida() {
		return ruta_salida;
	}

	/**
	 * @param ruta_salida the ruta_salida to set
	 */
	public void setRuta_salida(String ruta_salida) {
		this.ruta_salida = ruta_salida;
	}

	/**
	 * @return the opctareas
	 */
	public String getOpctareas() {
		return opctareas;
	}

	/**
	 * @param opctareas the opctareas to set
	 */
	public void setOpctareas(String opctareas) {
		this.opctareas = opctareas;
	}

	/**
	 * @return the vactivadetalletb
	 */
	public String getVactivadetalletb() {
		return vactivadetalletb;
	}

	/**
	 * @param vactivadetalletb the vactivadetalletb to set
	 */
	public void setVactivadetalletb(String vactivadetalletb) {
		this.vactivadetalletb = vactivadetalletb;
	}

	/**
	 * @return the activa
	 */
	public String getActiva() {
		return activa;
	}

	/**
	 * @param activa the activa to set
	 */
	public void setActiva(String activa) {
		this.activa = activa;
	}

	
	
	/**
	 * @return the clase
	 */
	public String getClase() {
		return clase;
	}

	/**
	 * @param clase the clase to set
	 */
	public void setClase(String clase) {
		this.clase = clase;
	}

	/**
	 * @return the vdisparador
	 */
	public String getVdisparador() {
		return vdisparador;
	}

	/**
	 * @param vdisparador the vdisparador to set
	 */
	public void setVdisparador(String vdisparador) {
		this.vdisparador = vdisparador;
	}

	/**
	 * @return the vgrupo
	 */
	public String getVgrupo() {
		return vgrupo;
	}

	/**
	 * @param vgrupo the vgrupo to set
	 */
	public void setVgrupo(String vgrupo) {
		this.vgrupo = vgrupo;
	}

	/**
	 * @return the vdiasem
	 */
	public String getVdiasem() {
		return vdiasem;
	}

	/**
	 * @param vdiasem the vdiasem to set
	 */
	public void setVdiasem(String vdiasem) {
		this.vdiasem = vdiasem;
	}


	/**
	 * @return the vfrecuencia
	 */
	public String getVfrecuencia() {
		return vfrecuencia;
	}

	/**
	 * @param vfrecuencia the vfrecuencia to set
	 */
	public void setVfrecuencia(String vfrecuencia) {
		this.vfrecuencia = vfrecuencia;
	}

	/**
	 * @return the vfrecuenciades
	 */
	public String getVfrecuenciades() {
		return vfrecuenciades;
	}

	/**
	 * @param vfrecuenciades the vfrecuenciades to set
	 */
	public void setVfrecuenciades(String vfrecuenciades) {
		this.vfrecuenciades = vfrecuenciades;
	}

	/**
	 * @return the vasunto
	 */
	public String getVasunto() {
		return vasunto;
	}

	/**
	 * @param vasunto the vasunto to set
	 */
	public void setVasunto(String vasunto) {
		this.vasunto = vasunto;
	}

	/**
	 * @return the vcontenido
	 */
	public String getVcontenido() {
		return vcontenido;
	}

	/**
	 * @param vcontenido the vcontenido to set
	 */
	public void setVcontenido(String vcontenido) {
		this.vcontenido = vcontenido;
	}

	/**
	 * @return the vcodrep
	 */
	public String getVcodrep() {
		return vcodrep;
	}

	/**
	 * @param vcodrep the vcodrep to set
	 */
	public void setVcodrep(String vcodrep) {
		this.vcodrep = vcodrep;
	}

	/**
	 * @return the vjob
	 */
	public String getVjob() {
		return vjob;
	}

	/**
	 * @param vjob the vjob to set
	 */
	public void setVjob(String vjob) {
		this.vjob = vjob;
	}

	/**
	 * @return the vidgrupo
	 */
	public String getVidgrupo() {
		return vidgrupo;
	}

	/**
	 * @param vidgrupo the vidgrupo to set
	 */
	public void setVidgrupo(String vidgrupo) {
		this.vidgrupo = vidgrupo;
	}

	/**
	 * @return the viddesidgrupo
	 */
	public String getViddesidgrupo() {
		return viddesidgrupo;
	}

	/**
	 * @param viddesidgrupo the viddesidgrupo to set
	 */
	public void setViddesidgrupo(String viddesidgrupo) {
		this.viddesidgrupo = viddesidgrupo;
	}

	/**
	 * @return the vdiames
	 */
	public String getVdiames() {
		return vdiames;
	}

	/**
	 * @param vdiames the vdiames to set
	 */
	public void setVdiames(String vdiames) {
		this.vdiames = vdiames;
	}

	/**
	 * @return the vdiainicio
	 */
	public String getVdiainicio() {
		return vdiainicio;
	}

	/**
	 * @param vdiainicio the vdiainicio to set
	 */
	public void setVdiainicio(String vdiainicio) {
		this.vdiainicio = vdiainicio;
	}

	/**
	 * @return the filterValue
	 */
	public Object getFilterValue() {
		return filterValue;
	}

	/**
	 * @param filterValue the filterValue to set
	 */
	public void setFilterValue(Object filterValue) {
		this.filterValue = filterValue;
	}

	

	/**
	 * @return the list
	 */
	public List<ProgramacionEtl> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<ProgramacionEtl> list) {
		this.list = list;
	}

	/**
	 * @param lazyModel the lazyModel to set
	 */
	public void setLazyModel(LazyDataModel<ProgramacionEtl> lazyModel) {
		this.lazyModel = lazyModel;
	}

	/**
	 * @return the idgrupo
	 */
	public String getIdgrupo() {
		return idgrupo;
	}

	/**
	 * @param idgrupo the idgrupo to set
	 */
	public void setIdgrupo(String idgrupo) {
		this.idgrupo = idgrupo;
	}

	

	/**
	 * @return the tarea
	 */
	public String getTarea() {
		return tarea;
	}

	/**
	 * @param tarea the tarea to set
	 */
	public void setTarea(String tarea) {
		this.tarea = tarea;
	}
	
	


	/**
	 * @return the vltrigger
	 */
	public String getVltrigger() {
		return vltrigger;
	}

	/**
	 * @param vltrigger the vltrigger to set
	 */
	public void setVltrigger(String vltrigger) {
		this.vltrigger = vltrigger;
	}
	

	/**
	 * @return the diasemana
	 */
	public String getDiasemana() {
		return diasemana;
	}

	/**
	 * @param diasemana the diasemana to set
	 */
	public void setDiasemana(String diasemana) {
		this.diasemana = diasemana;
	}

	
	/**
	 * @return the frecuencia
	 */
	public String getFrecuencia() {
		return frecuencia;
	}

	/**
	 * @param frecuencia the frecuencia to set
	 */
	public void setFrecuencia(String frecuencia) {
		this.frecuencia = frecuencia;
	}

    
	
	
	
	/**
	 * @return the dias
	 */
	public String getDias() {
		return dias;
	}


	/**
	 * @param dias the dias to set
	 */
	public void setDias(String dias) {
		this.dias = dias;
	}


	/**
	 * @return the rows
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * @param rows the rows to set
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}
	
	
	

	/**
	 * @return the reporte
	 */
	public String getReporte() {
		return reporte;
	}

	/**
	 * @param reporte the reporte to set
	 */
	public void setReporte(String reporte) {
		this.reporte = reporte;
	}
	
	

	/**
	 * @return the asunto
	 */
	public String getAsunto() {
		return asunto;
	}

	/**
	 * @param asunto the asunto to set
	 */
	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	/**
	 * @return the contenido
	 */
	public String getContenido() {
		return contenido;
	}

	/**
	 * @param contenido the contenido to set
	 */
	public void setContenido(String contenido) {
		this.contenido = contenido;
	}
	
	   
    
    /**
	 * @return the diames
	 */
	public String getDiames() {
		return diames;
	}


	/**
	 * @param diames the diames to set
	 */
	public void setDiames(String diames) {
		this.diames = diames;
	}

	

	/**
	 * @return the diainicio
	 */
	public Date getDiainicio() {
		return diainicio;
	}


	/**
	 * @param diainicio the diainicio to set
	 */
	public void setDiainicio(Date diainicio) {
		this.diainicio = diainicio;
	}
	
	


	/**
	 * @return the horarepeticion
	 */
	public String getHorarepeticion() {
		return horarepeticion;
	}

	/**
	 * @param horarepeticion the horarepeticion to set
	 */
	public void setHorarepeticion(String horarepeticion) {
		this.horarepeticion = horarepeticion;
	}
	
	
    

	/**
	 * @return the vlTabla
	 */
	public String[][] getVlTabla() {
		return vlTabla;
	}

	/**
	 * @param vlTabla the vlTabla to set
	 */
	public void setVlTabla(String[][] vlTabla) {
		this.vlTabla = vlTabla;
	}
	
	

	/**
	 * @return the vparamvalues
	 */
	public String getVparamvalues() {
		return vparamvalues;
	}

	/**
	 * @param vparamvalues the vparamvalues to set
	 */
	public void setVparamvalues(String vparamvalues) {
		this.vparamvalues = vparamvalues;
	}
	
	
	/**
	 * @return the vintervalo
	 */
	public String getVintervalo() {
		return vintervalo;
	}

	/**
	 * @param vintervalo the vintervalo to set
	 */
	public void setVintervalo(String vintervalo) {
		this.vintervalo = vintervalo;
	}

	/**
	* Metodo que da la informacion mas detallada sobre el horario, como hora de inicio de la tarea y cada
    * cuanto tiempo se ejecute la tarea
    */
    public void iniciarTarea() throws NamingException {
    	//
 		String[] vecreporte = reporte.split("\\ - ", -1);
 		arregloParametros =  StringUtils.join(inputs, "|").toUpperCase();
 		
 		//Creamos la instancia calendario para separar en dia, mes y año la fecha seleccionada
	   	 Calendar cal = Calendar.getInstance();
	   	 cal.setTime(diainicio);
	   	 int hora = cal.get(Calendar.HOUR_OF_DAY);
	   	 int minutos = cal.get(Calendar.MINUTE); //Para iniciar meses comenzando desde enero = 0
 		
    	 if(frecuencia.equals("0")){    		 
    	   iniciarTareaDiaria(tarea.toUpperCase(), vltrigger.toUpperCase(), arregloParametros, "0", arregloParamNames(vecreporte[0]), hora, minutos);
    	 } else if (frecuencia.equals("1")){
    	   iniciarTareaSemanal(tarea.toUpperCase(), vltrigger.toUpperCase(), dias, arregloParametros, "0", arregloParamNames(vecreporte[0]), hora, minutos);
    	 } else if (frecuencia.equals("2")){
    	   iniciarTareaDiaMes(tarea.toUpperCase(), vltrigger.toUpperCase(), diames, arregloParametros, "0", arregloParamNames(vecreporte[0]), hora, minutos);	
    	 } else if (frecuencia.equals("3")){
    	   iniciarTareaIntervaloMinutos(tarea.toUpperCase(), vltrigger.toUpperCase(), horarepeticion, arregloParametros, "0", arregloParamNames(vecreporte[0]));	
    	 } else if (frecuencia.equals("4")){
    	   iniciarTareaIntervaloMensual(tarea.toUpperCase(), vltrigger.toUpperCase(),  arregloParametros, "0", arregloParamNames(vecreporte[0]));	 
    	 } else {
    		////System.out.println("Iniciando tarea diaria");
             //Definir la instancia del job
    		String pparam; 
    		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        	String[] chkbox = request.getParameterValues("toDelete");
         	if (chkbox==null){
         		pparam = "";
         	} else {
         		pparam = StringUtils.join(chkbox, ",").replace(" ", "");
         	}
    	   iniciarTareaRepeticion(tarea.toUpperCase(), vltrigger.toUpperCase(), arregloParametros, "0", arregloParamNames(vecreporte[0]), pparam, hora, minutos);
      	 }
    //	}
    }
    
    
    
    /**Programa tarea diaria
     * @throws NamingException */
    private void iniciarTareaDiaria(String ptarea, String ptrigger, String paramvalues, String isrecupera, String paramnames, int phora, int pminutos)  {
    	////System.out.println("Iniciando tarea diaria");
        //Definir la instancia del job
    	JobDetail job = newJob(TareaInvocar.class)
    			.withIdentity(ptarea.toUpperCase(), "unico")
    			.build();
    	//Define el Trigger y la frecuencia en que se va a ejecutar
    	Trigger trigger = newTrigger()
    		    .withIdentity(ptrigger.toUpperCase(), "unico")
    		    .startAt(diainicio) //Año de inicio,Get a Date object that represents the given time, on the  given date.
    		    .withSchedule(dailyAtHourAndMinute(phora, pminutos))
    		    .build();
  		
    	try {
    		//Inicia el cronograma			
			schd.scheduleJob(job, trigger);
			schd.start();
			msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("mailtareamsgexito") + " " + ptrigger.toUpperCase(), "");
			if(isrecupera=="0"){
			insert("0", "0", paramvalues, "0", paramnames);
			}
			deleteTemps(session, "bvtparams_temp");
			deleteTemps(session, "bvtparams_number_temp");
			limpiar();
    	} catch (SchedulerException | NamingException e) {
			e.printStackTrace();
			msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "");
		}
    	FacesContext.getCurrentInstance().addMessage(null, msj);
    }	
    
    
  
    
    /**Programa tarea semanal
     * @throws NamingException */
    private void iniciarTareaSemanal(String ptarea, String ptrigger, String pdias, String paramvalues, String isrecupera, String paramnames, int phora, int pminutos)  { 	 
	   	 
        //Definir la instancia del job
    	JobDetail job = newJob(TareaInvocar.class)
    			.withIdentity(ptarea.toUpperCase(), "unico")
    			.build();
    	//Define el Trigger y la frecuencia en que se va a ejecutar
    	Trigger trigger = newTrigger()
    		    .withIdentity(ptrigger.toUpperCase(), "unico")
    		    .startAt(diainicio) //Año de inicio,Get a Date object that represents the given time, on the  given date.
    		    .withSchedule(weeklyOnDayAndHourAndMinute(Integer.parseInt(pdias), phora, pminutos))
    		    .build();
  		
    	try {
    		//Inicia el cronograma
			schd.scheduleJob(job, trigger);
			schd.start();
			msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("mailtareamsgexitosemanal") + " " + ptrigger.toUpperCase(), "");
			if(isrecupera=="0"){
			insert(dias, "0", paramvalues, "0", paramnames);
			}
			deleteTemps(session, "bvtparams_temp");
			deleteTemps(session, "bvtparams_number_temp");
			limpiar();
    	} catch (SchedulerException | NamingException e) {
			e.printStackTrace();
			msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "");
		}
    	FacesContext.getCurrentInstance().addMessage(null, msj);
    }	
    
    
    /**Programa tarea diaria usando un cron, un día del mes especifico con repeticion
     * @throws NamingException */
    private void iniciarTareaDiaMes(String ptarea, String ptrigger, String pdiames, String paramvalues, String isrecupera, String paramnames, int phora, int pminutos)  {
	   	int diames =  Integer.parseInt(pdiames);
        //Definir la instancia del job
    	JobDetail job = newJob(TareaInvocar.class)
    			.withIdentity(ptarea.toUpperCase(), "unico")
    			.build();
    	//Define el Trigger y la frecuencia en que se va a ejecutar
    	CronTrigger trigger = newTrigger()
    		    .withIdentity(ptrigger.toUpperCase(), "unico")
    		    .startAt(diainicio) //Año de inicio,Get a Date object that represents the given time, on the  given date.
    		    .withSchedule(monthlyOnDayAndHourAndMinute(diames, phora, pminutos))
    		    .build();
  		
    	try {
    		//Inicia el cronograma
			schd.start();
			schd.scheduleJob(job, trigger);
			msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("mailtareamsgexitodiaria") + " " + ptrigger.toUpperCase(), "");
			if(isrecupera=="0"){
			insert(dias, pdiames, paramvalues, "0", paramnames);
			}
			deleteTemps(session, "bvtparams_temp");
			deleteTemps(session, "bvtparams_number_temp");
			limpiar();
    	} catch (SchedulerException | NamingException e) {
			e.printStackTrace();
			msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "");
		}
    	FacesContext.getCurrentInstance().addMessage(null, msj);
    }	
    

    /**Programa tarea por intervalo mensual puede ser , Mensual, Bimensual o trimestral
     * @throws NamingException */
    private void iniciarTareaIntervaloMensual(String ptarea, String ptrigger, String paramvalues, String isrecupera, String paramnames)  {
    	 
        //Definir la instancia del job
    	JobDetail job = newJob(TareaInvocar.class)
    			.withIdentity(ptarea.toUpperCase(), "unico")
    			.build();
    	//Define el Trigger y la frecuencia en que se va a ejecutar
    	Trigger trigger = newTrigger()
    		    .withIdentity(ptrigger.toUpperCase(), "unico")
    		     .startAt(diainicio) //Año de inicio,Get a Date object that represents the given time, on the  given date.
    		    .withSchedule(calendarIntervalSchedule().withIntervalInMonths(1)) // interval is set in calendar months
    		    .build();
  		
    	try {
    		//Inicia el cronograma
			schd.scheduleJob(job, trigger);
			schd.start();
			msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("mailtareamsgexitoMesIntervalo") + " " + ptrigger.toUpperCase(), "");
			if(isrecupera=="0"){
			insert(dias, "0", paramvalues, "0", paramnames);
			}
			deleteTemps(session, "bvtparams_temp");
			deleteTemps(session, "bvtparams_number_temp");
			limpiar();
    	} catch (SchedulerException | NamingException e) {
			e.printStackTrace();
			msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "");
		}
    	FacesContext.getCurrentInstance().addMessage(null, msj);
    	
    }	
    
    
    /**Programa tarea por intervalo por hora puede ser , Mensual, Bimensual o trimestral
     * @throws NamingException */
    private void iniciarTareaIntervaloMinutos(String ptarea, String ptrigger,  String phorarepeticion, String paramvalues, String isrecupera, String paramnames)  {
  	    int multiplo = 60;
    	int minutos = Integer.parseInt(phorarepeticion) * multiplo;
  	    //System.out.println(minutos);
        //Definir la instancia del job
    	JobDetail job = newJob(TareaInvocar.class)
    			.withIdentity(ptarea.toUpperCase(), "unico")
    			.build();
    	//Define el Trigger y la frecuencia en que se va a ejecutar
    	Trigger trigger = newTrigger()
    		    .withIdentity(ptrigger.toUpperCase(), "unico")
    		    .startAt(diainicio)
    		    .withSchedule(simpleSchedule().withIntervalInSeconds(minutos).repeatForever())
    		    .build();
  		
    	try {
    		//Inicia el cronograma
			schd.scheduleJob(job, trigger);
			schd.start();
			msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("mailtareamsgexitoMinutosIntervalo") + " " + ptrigger.toUpperCase(), "");
			if(isrecupera=="0"){
			insert("0", "0", paramvalues, phorarepeticion, paramnames);
			}
			deleteTemps(session, "bvtparams_temp");
			deleteTemps(session, "bvtparams_number_temp");
			limpiar();
    	} catch (SchedulerException | NamingException e) {
			e.printStackTrace();
			msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "");
		}
    	FacesContext.getCurrentInstance().addMessage(null, msj);
    	
    }	
    
    /**Programa tarea repeticion
     * @throws NamingException */
    private void iniciarTareaRepeticion(String ptarea, String ptrigger, String paramvalues, String isrecupera, String paramnames, String pparam, int phora, int pminutos){
    	
    	HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
    	String[] chkbox = request.getParameterValues("toDelete");
    	  	 
    	
    	////System.out.println("Iniciando tarea diaria");
        //Definir la instancia del job
    	if (chkbox==null){
    		msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("mailtareaAlert"), "");
    	} else {
    	String param = StringUtils.join(chkbox, ",").replace(" ", "");
    	JobDetail job = newJob(TareaInvocar.class)
    			.withIdentity(ptarea.toUpperCase(), "unico")
    			.build();
    	CronTrigger trigger;
    	//Define el Trigger y la frecuencia en que se va a ejecutar
    	if(isrecupera=="0"){
    	     trigger = newTrigger()
    		    .withIdentity(ptrigger.toUpperCase(), "unico")
    		    .startAt(diainicio) //Año de inicio,Get a Date object that represents the given time, on the  given date.
    		    .withSchedule(cronSchedule("0 " + pminutos + " " + phora + " "+ " ? * " + param + " *"))
    		    .build();
    	} else {
    		 trigger = newTrigger()
        		    .withIdentity(ptrigger.toUpperCase(), "unico")
        		    .startAt(diainicio) //Año de inicio,Get a Date object that represents the given time, on the  given date.
        		    .withSchedule(cronSchedule("0 " + pminutos + " " + phora + " "+ " ? * " + pparam + " *"))
        		    .build();	
    	}
  		//System.out.println("0 " + pminutos + " " + phora + " "+ " ? * " + pparam + " *");
    	try {
    		//Inicia el cronograma
			schd.scheduleJob(job, trigger);
			schd.start();
			msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("mailtareamsgexito") + " " + ptrigger.toUpperCase(), "");
			if(isrecupera=="0"){
			insert("0", "0", paramvalues, "0", paramnames);
			}
			deleteTemps(session, "bvtparams_temp");
			deleteTemps(session, "bvtparams_number_temp");
			//limpiar();
    	} catch (SchedulerException | NamingException e) {
			e.printStackTrace();
			msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "");
		}
    	}
    	FacesContext.getCurrentInstance().addMessage(null, msj);
    }	
    
    /*
    /**Programa tarea repeticion por hora
     * @throws NamingException 
    private void iniciarTareaRepeticionHora(String ptarea, String ptrigger, String phora, String pminuto, String phorarepeticion, String paramvalues, String isrecupera, String paramnames, String pparam){
    	
    	//Creamos la instancia calendario para separar en dia, mes y año la fecha seleccionada
	   	 Calendar cal = Calendar.getInstance();
	   	 cal.setTime(diainicio);
	   	 int anio = cal.get(Calendar.YEAR);
	   	 int vmes = cal.get(Calendar.MONTH); //Para iniciar meses comenzando desde enero = 0
	   	 int dia = cal.get(Calendar.DAY_OF_MONTH);
	   	 
    	HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
    	String[] chkbox = request.getParameterValues("toDelete");
    	
    	//System.out.println(param);
    	
    	////System.out.println("Iniciando tarea diaria");
        //Definir la instancia del job
    	if (chkbox==null){
    		msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("mailtareaAlert"), "");
    	} else {
    	String param = StringUtils.join(chkbox, ",").replace(" ", "");
    	JobDetail job = newJob(TareaInvocar.class)
    			.withIdentity(ptarea.toUpperCase(), "unico")
    			.build();
    	CronTrigger trigger;
    	//Define el Trigger y la frecuencia en que se va a ejecutar
    	if(isrecupera=="0"){
    	      trigger = newTrigger()
    		    .withIdentity(ptrigger.toUpperCase(), "unico")
    		    .startAt(dateOf(Integer.parseInt(phora), //Hora de inicio
    		    	       Integer.parseInt(pminuto), //Minutos de inicio
    		    	       00, //Segundos de inicio
    		    	       dia, //Dia de inicio
    		    	       vmes, //Mes de Inicio
    		    	       anio)) //Año de inicio,Get a Date object that represents the given time, on the  given date.
    		    .withSchedule(cronSchedule("0 0/"+Integer.parseInt(phorarepeticion)*60 +"  0 "+ " ? * " + param + ""))
    		    .build();
    	} else {
    		  trigger = newTrigger()
        		    .withIdentity(ptrigger.toUpperCase(), "unico")
        		    .startNow()
        		    .withSchedule(cronSchedule("0 0/"+Integer.parseInt(phorarepeticion)*60 +"  0 "+ " ? * " + param + ""))
        		    .build();	
    	}
  		//System.out.println("0 " + pminuto + " " + phora + " "+ " * * " + param + "");
    	try {
    		//Inicia el cronograma
			schd.start();
			schd.scheduleJob(job, trigger);
			msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("mailtareamsgexito") + " " + ptrigger.toUpperCase(), "");
			if(isrecupera=="0"){
			insert("0", "0", paramvalues, "0", paramnames);
			}
			deleteTemps(session, "bvtparams_temp");
			deleteTemps(session, "bvtparams_number_temp");
			limpiar();
    	} catch (SchedulerException | NamingException e) {
			e.printStackTrace();
			msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "");
		}
    	}
    	FacesContext.getCurrentInstance().addMessage(null, msj);
    }*/
    
    /**Borra la tarea
     * @throws NamingException */ 
   public void detenerTarea() throws NamingException  {
	   try {
		//schd.unscheduleJob(triggerKey(vltrigger.toUpperCase(), "unico")); 
		schd.deleteJob(jobKey(vltrigger.toUpperCase(), "unico"));//Reimplementación, anteriormente solo la quitaba de la tarea no la borraba
		                                         //Nuevo cambio 20/09/2014
		delete();
		msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("mailtareaborrada"), "");
		FacesContext.getCurrentInstance().addMessage(null, msj);
	} catch (SchedulerException e) {
		e.printStackTrace();
		msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "");
    	FacesContext.getCurrentInstance().addMessage(null, msj);
	} 	
   }
   
   /**Detiene la tarea
    * @throws NamingException */ 
  public void pararTarea() throws NamingException  {
	   try {
		   schd.deleteJob(jobKey(vltrigger.toUpperCase(), "unico"));
		updateJobStatusBd(vltrigger.toUpperCase(), "1");
		msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("mailtareadetenida"), "");
		FacesContext.getCurrentInstance().addMessage(null, msj);
	} catch (SchedulerException e) {
		e.printStackTrace();
		msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "");
   	FacesContext.getCurrentInstance().addMessage(null, msj);
	} 	
  }
  
	  /**Reinicia la tarea
	   * @throws NamingException */ 
	 public void reiniciarTarea() throws NamingException  { 

	 		String[] vecreporte = reporte.split("\\ - ", -1);
	 		arregloParametros =  StringUtils.join(inputs, "|").toUpperCase();
	 		
	 		//Actualiza el estatus
			updateJobStatusBd(vltrigger.toUpperCase(), "0");	
			
			//Creamos la instancia calendario para separar en dia, mes y año la fecha seleccionada
		   	 Calendar cal = Calendar.getInstance();
		   	 cal.setTime(diainicio);
		   	 int hora = cal.get(Calendar.HOUR_OF_DAY);
		   	 int minutos = cal.get(Calendar.MINUTE); //Para iniciar meses comenzando desde enero = 0
	 		
			if(frecuencia.equals("0")){    		 
		    	   iniciarTareaDiaria(tarea.toUpperCase(), vltrigger.toUpperCase(),  arregloParametros, "1", arregloParamNames(vecreporte[0]), hora, minutos);
		    	 } else if (frecuencia.equals("1")){
		    	   iniciarTareaSemanal(tarea.toUpperCase(), vltrigger.toUpperCase(), dias,  arregloParametros, "1", arregloParamNames(vecreporte[0]), hora, minutos);
		    	 } else if (frecuencia.equals("2")){
		    	   iniciarTareaDiaMes(tarea.toUpperCase(), vltrigger.toUpperCase(), diames, arregloParametros, "1", arregloParamNames(vecreporte[0]), hora, minutos);	
		    	 } else if (frecuencia.equals("3")){
		    	   iniciarTareaIntervaloMinutos(tarea.toUpperCase(), vltrigger.toUpperCase(),  horarepeticion, arregloParametros, "1", arregloParamNames(vecreporte[0]));	
		    	 } else if (frecuencia.equals("4")){
		    	   iniciarTareaIntervaloMensual(tarea.toUpperCase(), vltrigger.toUpperCase(),   arregloParametros, "1", arregloParamNames(vecreporte[0]));	 
		    	 } else {
		    	   iniciarTareaRepeticion(tarea.toUpperCase(), vltrigger.toUpperCase(),  arregloParametros, "1", arregloParamNames(vecreporte[0]), "0", hora, minutos);
		      	 }
		  
	 }
   
   /**Detiene la tarea para recargarla
    * Usada en el método iniar tarea
    * @throws NamingException */ 
   /*
  private void detenerTarea_Recargar() throws NamingException  {
	   try {
		//schd.unscheduleJob(triggerKey(vltrigger.toUpperCase(), "unico")); 
		schd.deleteJob(jobKey(vltrigger.toUpperCase(), "unico"));//Reimplementación, anteriormente solo la quitaba de la tarea no la borraba
		                                         //Nuevo cambio 20/09/2014
		delete();
		//msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("mailtareaborrada"), "");
		//FacesContext.getCurrentInstance().addMessage(null, msj);
	} catch (SchedulerException e) {
		e.printStackTrace();
		//msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "");
   	    //FacesContext.getCurrentInstance().addMessage(null, msj);
	} 	
  }*/
   
   
   /**Si el servidor se reinicia o la tarea no está activa se inicia desde los 
    * registros de la base de datos
 * @throws SchedulerException
 * @throws NamingException 
 * @throws IOException **/
   protected void recuperarTriggers(String estatus) throws SchedulerException, NamingException{
	   //System.out.println("Recuperando Tareas");
	   consulta.selectPntGenerica("select job, disparador, grupo, diasem,  frecuencia, diames, PARAMVALUES, paramnames, intervalo, dias_semana, to_char(diainicio,'HH24'), to_char(diainicio,'mi') from t_programacion where activa = '" + estatus + "' order by disparador", JNDI);
	   int rows = consulta.getRows();
	   String[][] vltabla = consulta.getArray();
	   
	   if(rows>0){//Si la consulta es mayor a cero comienza a recorrerla para verificar si la tarea existe en ram, si no la reactiva
		          //desde la base de datos
		   for(int i=0; i<rows; i++){//Recorre los triggers almacenados y verifica por cada uno
			   if(schd.checkExists(triggerKey(vltabla[i][1].toUpperCase(), "unico"))==false){//Si la tarea no existe la crea
				   //Opción para generar la tarea diaria
				   if(vltabla[i][4].toString().equals("0")){
					   //Tarea diaria
					   iniciarTareaDiaria(vltabla[i][0].toUpperCase(), vltabla[i][1].toUpperCase(), vltabla[i][6],"1", vltabla[i][7], Integer.parseInt(vltabla[i][10]), Integer.parseInt(vltabla[i][11]));				    	
				   } else if(vltabla[i][4].toString().equals("1")){//Fin valida que sea diaria, de lo contrario es semanal
					   //Tarea semanal
					   iniciarTareaSemanal(vltabla[i][0].toUpperCase(), vltabla[i][1].toUpperCase(), vltabla[i][3], vltabla[i][6],"1", vltabla[i][7], Integer.parseInt(vltabla[i][10]), Integer.parseInt(vltabla[i][11]));					   
				   } else if(vltabla[i][4].toString().equals("2")){
					   //Tarea dia mes
					   iniciarTareaDiaMes(vltabla[i][0].toUpperCase(), vltabla[i][1].toUpperCase(), vltabla[i][5], vltabla[i][6],"1", vltabla[i][7], Integer.parseInt(vltabla[i][10]), Integer.parseInt(vltabla[i][11]));
				   } else if(vltabla[i][4].toString().equals("3")){
					   //Tarea dia hora
					   //System.out.println("Recuperando Tareas minutos");
					   iniciarTareaIntervaloMinutos(vltabla[i][0].toUpperCase(), vltabla[i][1].toUpperCase(), vltabla[i][8], vltabla[i][6], "1", vltabla[i][7]);	   
				   } else if (vltabla[i][4].toString().equals("4")){
			    	   //Intervalos mensuales
					   iniciarTareaIntervaloMensual(vltabla[i][0].toUpperCase(), vltabla[i][1].toUpperCase(),  vltabla[i][6],"1", vltabla[i][7]);
			       } else {
			    	   //Reptición
			    	   iniciarTareaRepeticion(vltabla[i][0].toUpperCase(), vltabla[i][1].toUpperCase(), vltabla[i][6], "1", vltabla[i][7], vltabla[i][10], Integer.parseInt(vltabla[i][10]), Integer.parseInt(vltabla[i][11]));
			       }
			   }//Fin de chequeo de tarea en ram
		   }//Fin del recorrido
	   }
   }
   
     
    public void limpiar(){
    	reporte = "";
    	tarea = ""; //Nombre de la tarea
        vltrigger = ""; //Nombre del disparador
        diasemana = ""; //Día de la semana
        frecuencia = "0"; //Fercuencia de repetición
        dias = "2"; // Días de repetición del reporte
        idgrupo = "";
        asunto = "";
        contenido = "";
        diames = "1";
        diainicio = new Date();
        inputs = null;
        //ruta_salida = "URL";
    }
  //Coneccion a base de datos
  	//Pool de conecciones JNDIFARM
  	Connection con;
  	PreparedStatement pstmt = null;
  	ResultSet r;

    
    /**
     * Inserta Configuración.
     * Parametros del Metodo: String codpai, String despai. Pool de conecciones y login
  	 * @throws NamingException 
     **/
    private void delete() throws NamingException {

        try {
        	 Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);
     		
     		con = ds.getConnection();
     		
     		//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
	       	 DatabaseMetaData databaseMetaData = con.getMetaData();
	       	 String productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
     		
     		String vlfecha;
     		java.text.SimpleDateFormat sdfecha_es = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", locale );
        	java.text.SimpleDateFormat sdfecha_en = new java.text.SimpleDateFormat("dd/MMM/yyyy HH:mm", locale );
        	String to_date;
        	if(OPENBIZVIEW_BD_LANG.equals("es")){
        		vlfecha = sdfecha_es.format(diainicio);
        		to_date = "to_date('" + vlfecha + "', 'dd/mm/yyyy hh24:mi')";
        	} else {
        		vlfecha = sdfecha_en.format(diainicio);
        		to_date = "to_date('" + vlfecha + "', 'dd/mon/yyyy hh24:mi')";
        	}
        	
            String query = "";
           	
           	switch ( productName ) {
            case "Oracle":
            	 query = "DELETE  from T_PROGRAMACION WHERE diainicio = " + to_date + " and disparador = '" + vltrigger.toUpperCase() + "' and instancia = '" + instancia + "'";
                 break;
            case "PostgreSQL":
            	query = "DELETE  from T_PROGRAMACION WHERE diainicio = '" + diainicio + "' and disparador = '" + vltrigger.toUpperCase() + "' and instancia = '" + instancia + "'";
                 break;
            }
     		
            //System.out.println(query);
            pstmt = con.prepareStatement(query);
          
            try {
                pstmt.executeUpdate();
                limpiar();
            } catch (SQLException e)  {
                 e.printStackTrace();
            }
            
            pstmt.close();
            con.close();           
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Inserta Configuración.
     * 
  	 * @throws NamingException 
     **/
    private void insert(String pdias, String pdiames, String paramvalues, String intervalo, String paramnames)  {
        try {
        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);
     		con = ds.getConnection();
     		
     		//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
        	 DatabaseMetaData databaseMetaData = con.getMetaData();
        	 String productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
     		
     		String[] vecidgrupo = idgrupo.split("\\ - ", -1);
     		
     		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        	String[] chkbox = request.getParameterValues("toDelete");
        	String param = "";
        	//System.out.println(param);
        	
        	////System.out.println("Iniciando tarea diaria");
            //Definir la instancia del job
        	if (chkbox==null){
        		param = "";
        	} else {
        	    param = StringUtils.join(chkbox, ",").replace(" ", "");
        	}
        	//System.out.println(param);
     		String[] vecreporte = reporte.split("\\ - ", -1);
     		String vlfecha;
     		java.text.SimpleDateFormat sdfecha_es = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", locale );
        	java.text.SimpleDateFormat sdfecha_en = new java.text.SimpleDateFormat("dd/MMM/yyyy HH:mm", locale );
        	String to_date;
        	if(OPENBIZVIEW_BD_LANG.equals("es")){
        		vlfecha = sdfecha_es.format(diainicio);
        		to_date = "to_date('" + vlfecha + "', 'dd/mm/yyyy hh24:mi')";
        	} else {
        		vlfecha = sdfecha_en.format(diainicio);
        		to_date = "to_date('" + vlfecha + "', 'dd/mon/yyyy hh24:mi')";
        	}
     		
           	String query = "";
           	
           	switch ( productName ) {
            case "Oracle":
            	query = "INSERT INTO T_PROGRAMACION VALUES (?,?,?,?,?,?,?,?,?,?,?,?," + to_date + ",?,?,?,?,?,?,?,?,?)";
                 break;
            case "PostgreSQL":
            	query = "INSERT INTO T_PROGRAMACION VALUES (?,?,?,?,?,?,?,?,?,?,?,?,'" + diainicio + "',?,?,?,?,?,?,?,?,?)";
                 break;
            }


     		//System.out.println(query);
     		pstmt = con.prepareStatement(query);
            pstmt.setString(1, vltrigger.toUpperCase());
            pstmt.setString(2, "unico");
            pstmt.setInt(3, Integer.parseInt(pdias));       
            pstmt.setString(4, frecuencia);
            pstmt.setString(5, asunto);
            pstmt.setString(6, contenido);
            pstmt.setString(7, vecreporte[0].toUpperCase());
            pstmt.setString(8, extContext.getRealPath(BIRT_VIEWER_WORKING_FOLDER));
            pstmt.setString(9, extContext.getRealPath(BIRT_VIEWER_LOG_DIR)); 
            pstmt.setInt(10, Integer.parseInt(vecidgrupo[0]));
            pstmt.setString(11, tarea.toUpperCase());
            pstmt.setString(12, pdiames);
            pstmt.setString(13, "0");
            pstmt.setString(14, paramvalues);
            pstmt.setInt(15, Integer.parseInt(intervalo));
            pstmt.setString(16, paramnames);
            pstmt.setString(17, ruta_salida);
            pstmt.setString(18, opctareas);
            pstmt.setString(19, formato);
            pstmt.setInt(20, Integer.parseInt(instancia));
            pstmt.setString(21, param);
            
            try {
                pstmt.executeUpdate();
                limpiar();
            } catch (SQLException e)  {
                 //e.printStackTrace();
                 msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "");
       	    	 FacesContext.getCurrentInstance().addMessage(null, msj);
            }
            
            pstmt.close();
            con.close();           
        } catch (Exception e) {
           //e.printStackTrace();
        }
    }
    
    
    
    /**
     * Leer Datos de mailconfig
     * @throws NamingException 
     * @throws IOException 
     **/ 	
  	public void select(int first, int pageSize, String sortField, Object filterValue) throws SQLException, NamingException {
     try {	

    	 Context initContext = new InitialContext();     
         DataSource ds = (DataSource) initContext.lookup(JNDI);
    	 con = ds.getConnection();
    		
    	//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
     	 DatabaseMetaData databaseMetaData = con.getMetaData();
     	 String productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección	
   		
     	String query = "";
     	
     	if(opctareas==null){
     		opctareas = "1";
     	}
     	
     	if(mailreporteFiltro==null){
     		mailreporteFiltro = " - ";
     	}
     	
     	if(mailfrecuenciaFiltro==null){
     		mailfrecuenciaFiltro = " - ";
     	}
     	
        if(mailgrupoFiltro==null){
        	mailgrupoFiltro = " - ";
        }
        
        String[] vecrepf = mailreporteFiltro.split("\\ - ", -1);
 		String[] veccfrecf = mailfrecuenciaFiltro.split("\\ - ", -1);
 		String[] vecgrupf = mailgrupoFiltro.split("\\ - ", -1);
     	
     	switch ( productName ) {
        case "Oracle":
        	query += "  select * from ";
     	    query += " ( select query.*, rownum as rn from";
        	query += " ( select trim(a.job), trim(a.disparador),  case a.frecuencia when '0' then '" + getMessage("mailtareaDiario") + "' when  '1' then '" + getMessage("mailtareaSemanal") + "' when '2' then'" + getMessage("mailtareaPersonalizada") + "' when '3' then '" + getMessage("mailtareaHoraRep") + "' when '4' then '" + getMessage("mailimes1") + "' when '5' then '" + getMessage("maillidiasSelect") + "'  else '" + getMessage("maillidiasSelect1") + "' end , a.diasem";
      		query += " , trim(a.etl), trim(a.frecuencia), trim(a.diames), trim(to_char(a.diainicio, 'dd/mm/yyyy HH24:mi')), decode(trim(activa),'0','chkActiva','chkInactiva'), trim(activa), decode(trim(activa),'0','"+getMessage("mailtarea0")+"','"+ getMessage("mailtarea1")+"')";
      		query += " , case when activa = '0' then 'fa fa-circle fa-2x text-success' else 'fa fa-circle fa-2x text-danger' end, trim(paramvalues), intervalo";
      		query += " from t_etl a";
            query += " where  a.job||a.etl like '%" + ((String) filterValue).toUpperCase() + "%'";
            query += " and a.etl like '" + vecrepf[0] + "%'";
            query += " and a.frecuencia like '" + veccfrecf[0] + "%'";
            query += " and a.idgrupo like '" + vecgrupf[0] + "%'";
            query += " AND   a.instancia = '" + instancia + "'";
            query += " order by " + sortField.replace("v", "") + ") query";
	        query += " ) where rownum <= " + pageSize ;
	        query += " and rn > (" + first + ")";
             break;
        case "PostgreSQL":
        	query = "select trim(a.job), trim(a.disparador),   case a.frecuencia when '0' then '" + getMessage("mailtareaDiario") + "' when  '1' then '" + getMessage("mailtareaSemanal") + "' when '2' then'" + getMessage("mailtareaPersonalizada") + "' when '3' then '" + getMessage("mailtareaHoraRep") + "' when '4' then '" + getMessage("mailimes1") + "' when '5' then '" + getMessage("maillidiasSelect") + "'  else '" + getMessage("maillidiasSelect1") + "' end , a.diasem";
      		query += " , trim(a.codrep),  trim(cast(a.idgrupo as text)), trim(a.asunto), trim(a.contenido), trim(b.desgrupo) , trim(a.frecuencia), trim(a.diames), trim(to_char(a.diainicio, 'dd/mm/yyyy hh24:mi')),  case when trim(activa) = '0' then 'chkActiva' else 'chkInactiva' end, trim(activa), case when trim(activa) = '0' then '"+ getMessage("mailtarea0")+"' else '"+ getMessage("mailtarea1")+"' end, trim(a.formato)";
      		query += " , case when activa = '0' then 'fa fa-circle fa-2x text-success' else 'fa fa-circle fa-2x text-danger' end, trim(paramvalues), intervalo";
      		query += " from t_etl a";
      		query += " where  a.job||a.etl like '%" + ((String) filterValue).toUpperCase() + "%'";
            query += " and a.etl like '" + vecrepf[0] + "%'";
            query += " and a.frecuencia like '" + veccfrecf[0] + "%'";
            query += " and a.idgrupo like '" + vecgrupf[0] + "%'";
            query += " AND   a.instancia = '" + instancia + "'";
            query += " order by " + sortField.replace("v", "") ;
	        query += " LIMIT " + pageSize;
	        query += " OFFSET " + first;
             break;
        }


        
        pstmt = con.prepareStatement(query);
        //System.out.println(query);

         r =  pstmt.executeQuery();
        
        
        while (r.next()){
        	ProgramacionEtl select = new ProgramacionEtl();
            select.setVjob(r.getString(1));
            select.setVdisparador(r.getString(2));
            select.setVfrecuenciades(r.getString(3));
            select.setVdiasem(r.getString(4));
            select.setVcodrep(r.getString(5));
            select.setVidgrupo(r.getString(6));
            select.setVasunto(r.getString(7));
            select.setVcontenido(r.getString(8));
            select.setViddesidgrupo(r.getString(9));
            select.setVfrecuencia(r.getString(10));
            select.setVdiames(r.getString(11));
            select.setVdiainicio(r.getString(12));
            select.setClase(r.getString(13));
            select.setActiva(r.getString(14));
            select.setVactivadetalletb(r.getString(15));
            select.setVformato(r.getString(16).toUpperCase());
            select.setStatusIncon(r.getString(17));
            select.setVparamvalues(r.getString(18));
            select.setVintervalo(r.getString(19));
        	//Agrega la lista
        	list.add(select);
        	rows = list.size();
        	
        }
     } catch (SQLException | SchedulerException e){
         e.printStackTrace();    
     }
        //Cierra las conecciones
        pstmt.close();
        con.close();
        r.close();

  	}
  	
  	/**
     * Leer registros en la tabla para paginación
     **/ 	
  	public void counter(Object filterValue )  {
  	      		
  		if(opctareas==null){
     		opctareas = "1";
     	}
     	
     	if(mailreporteFiltro==null){
     		mailreporteFiltro = " - ";
     	}
     	
     	if(mailfrecuenciaFiltro==null){
     		mailfrecuenciaFiltro = " - ";
     	}
     	
        if(mailgrupoFiltro==null){
        	mailgrupoFiltro = " - ";
        }
        
        String[] vecrepf = mailreporteFiltro.split("\\ - ", -1);
 		String[] veccfrecf = mailfrecuenciaFiltro.split("\\ - ", -1);
 		String[] vecgrupf = mailgrupoFiltro.split("\\ - ", -1);


 		String query = "  select trim(a.job)";
 		query += " from t_etl a";
        query += " where  a.job||a.etl like '%" + ((String) filterValue).toUpperCase() + "%'";
        query += " and a.etl like '" + vecrepf[0] + "%'";
        query += " and a.frecuencia like '" + veccfrecf[0] + "%'";
        query += " and a.idgrupo like '" + vecgrupf[0] + "%'";
        query += " AND   a.instancia = '" + instancia + "'";
        query += " order by ?";

        String querypg = "select trim(a.job)";
        querypg += " from t_etl a";
        querypg += " where  a.job||a.etl like '%" + ((String) filterValue).toUpperCase() + "%'";
        querypg += " and a.etl like '" + vecrepf[0] + "%'";
        querypg += " and a.frecuencia like '" + veccfrecf[0] + "%'";
        querypg += " and a.idgrupo like '" + vecgrupf[0] + "%'";
        querypg += " AND   a.instancia = '" + instancia + "'";
        querypg += " order by ?" ;
    
  		consulta.selectGenericaMDB(query, querypg, JNDI);
        rows = consulta.getData().get(0).size();

  	}
  	
  	
  	  	
  	
  	
  	/**En caso que se requiera envia correos manualmente al grupo
  	 * @throws IOException **/
  	
  	public void enviarMailmanual() throws IOException{

  	    java.sql.Date sqlDate = new java.sql.Date(fechadia.getTime());
  	    //Para reportes
  	    int rowsrep;
  	   	String[][] vltablarep;
  
  		try {
  		String vlqueryRep = "select trim(codrep), trim(rutarep), trim(rutatemp), trim(disparador), trim(formato), to_char(diainicio,'HH24:mi'), case when trim(paramnames) is null then '0' else trim(paramnames) end, case when trim(paramvalues) is null then '0' else trim(paramvalues) end, trim(asunto), trim(contenido), trim(dias_semana)";
  	    vlqueryRep += " from t_programacion" ;
  	    vlqueryRep += " where disparador='" + vltrigger.toUpperCase() + "'";
  	    //System.out.println(vlqueryRep);	
  	    
  		consulta.selectPntGenerica(vlqueryRep, JNDI);
  		
  		////System.out.println("select nombrereporte, idgrupo, trim(rutareporte), trim(rutatemp) from mailtarea where hora='" + formato.format(new Date()) + "'");
  		   
  		rowsrep = consulta.getRows();
  		vltablarep = consulta.getArray();

  		//Imprime reporte
  		if (rowsrep>0){//Si la consulta es mayor a cero devuelve registros envía el correo
  			
  		 //new RunReport().outReporteRecibo(vltablarep[0][0].toString(), vltablarep[0][4].toString(), vltablarep[0][1].toString(), vltablarep[0][2].toString(), vltablarep[0][0].toString(), sqlDate, vltablarep[0][3].toString(),  vltablarep[0][6].toString(), vltablarep[0][7].toString());
 		
  			for(int i=0;i<rowsrep;i++){
  		//	 new Sendmail().mailthread(vltrigger.toUpperCase(), vltablarep[i][2], vltablarep[i][0], vltablarep[i][8], vltablarep[i][9], vltablarep[i][4]);
  			}
  		} 
  		msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("mailtareaEnvío1"), "");
  		FacesContext.getCurrentInstance().addMessage(null, msj);
  		} catch (NamingException e) {
  			e.printStackTrace();
  		}
  	}
  	
  


  	
  	/**Cambia estatus en la base de datos a la tarea como inactiva
     * @throws NamingException */
  	private void updateJobStatusBd(String ptrigger, String estatus){
  		 try {
        	Context initContext = new InitialContext();     
      		DataSource ds = (DataSource) initContext.lookup(JNDI);

      		con = ds.getConnection();		
      		
             String query = "UPDATE t_programacion";
             query += " SET activa = '" + estatus + "'";
             query += " WHERE disparador = '" + ptrigger.toUpperCase() + "'";
             query += " AND   instancia = '" + instancia + "'";
           //System.out.println(query);
            pstmt = con.prepareStatement(query);
            // Antes de ejecutar valida si existe el registro en la base de Datos.
                pstmt.executeUpdate();
                //Cierra conecciones
                pstmt.close();
                con.close(); 	
            } catch (SQLException | NamingException e) {
            	e.printStackTrace();
            } 			 
  	}
  	
  	
  	/**Actualiza job existente
  	 * Elimina y vuelve a crear
     * @throws NamingException */
  	public void updateExistingJob() throws NamingException{
  	//Vuelve a crear la tarea
 		if(ruta_salida==null){
 			ruta_salida = "";
 		}
 		String[] vecreporte = reporte.split("\\ - ", -1);
 		String[] vecidgrupo = idgrupo.split("\\ - ", -1);
 		
  		//Elimina lo existente
  		try {
  			schd.unscheduleJob(triggerKey(vltrigger.toUpperCase(), "unico")); 
  			schd.deleteJob(jobKey(vltrigger.toUpperCase(), "unico"));//Reimplementación, anteriormente solo la quitaba de la tarea no la borraba
  			                                         //Nuevo cambio 20/09/2014
  			//System.out.println("Detiene a: " + vltrigger.toUpperCase());
  			//Modifica los valores
  			Context initContext = new InitialContext();     
      		DataSource ds = (DataSource) initContext.lookup(JNDI);
      		
      		con = ds.getConnection();	
      		
      	   //Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
       	   DatabaseMetaData databaseMetaData = con.getMetaData();
       	   String productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
      		
      		
      		arregloParametros =  StringUtils.join(inputs, "|").toUpperCase();
      		
      		String vlfecha;
     		java.text.SimpleDateFormat sdfecha_es = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", locale );
        	java.text.SimpleDateFormat sdfecha_en = new java.text.SimpleDateFormat("dd/MMM/yyyy HH:mm", locale );
        	String to_date;
        	if(OPENBIZVIEW_BD_LANG.equals("es")){
        		vlfecha = sdfecha_es.format(diainicio);
        		to_date = "to_date('" + vlfecha + "', 'dd/mm/yyyy hh24:mi')";
        	} else {
        		vlfecha = sdfecha_en.format(diainicio);
        		to_date = "to_date('" + vlfecha + "', 'dd/mon/yyyy hh24:mi')";
        	}
     		
           	String query = "";
           	
           	switch ( productName ) {
            case "Oracle":
            	query = "UPDATE t_programacion";
                query += " SET diasem = '" + dias + "'";
                query += " , frecuencia = '" + frecuencia + "'";
                query += " , asunto = '" + asunto + "'";
                query += " , contenido = '" + contenido + "'";
                query += " , codrep = '" + vecreporte[0].toUpperCase() + "'";
                query += " , idgrupo = '" + Integer.parseInt(vecidgrupo[0]) + "'";
                query += " , diames = '" + diames + "'";
                query += " , diainicio = " + to_date ;
                query += " , paramvalues = '" + arregloParametros + "'";
                query += " , intervalo = '" + horarepeticion + "'";
                query += " , paramnames = '" + arregloParamNames(vecreporte[0]) + "'";
                query += " , ruta_salida = '" + ruta_salida + "'";
                query += " , opctareas = '" + opctareas + "'";
                query += " , formato = '" + formato + "'";
                query += " WHERE disparador = '" + vltrigger.toUpperCase() + "'";
                query += " AND   instancia = '" + instancia + "'";
                 break;
            case "PostgreSQL":
            	query = "UPDATE t_programacion";
                query += " SET diasem = '" + dias + "'";
                query += " , frecuencia = '" + frecuencia + "'";
                query += " , asunto = '" + asunto + "'";
                query += " , contenido = '" + contenido + "'";
                query += " , codrep = '" + vecreporte[0].toUpperCase() + "'";
                query += " , idgrupo = '" + Integer.parseInt(vecidgrupo[0]) + "'";
                query += " , diames = '" + diames + "'";
                query += " , diainicio = '" + diainicio + "'";
                query += " , paramvalues = '" + arregloParametros + "'";
                query += " , intervalo = '" + horarepeticion + "'";
                query += " , paramnames = '" + arregloParamNames(vecreporte[0]) + "'";
                query += " , ruta_salida = '" + ruta_salida + "'";
                query += " , opctareas = '" + opctareas + "'";
                query += " , formato = '" + formato + "'";
                query += " WHERE disparador = '" + vltrigger.toUpperCase() + "'";
                query += " AND   instancia = '" + instancia + "'";
                 break;
            }
             
          // System.out.println(query);
            pstmt = con.prepareStatement(query);
            // Antes de ejecutar valida si existe el registro en la base de Datos.
            pstmt.executeUpdate();
            //Cierra conecciones
  
             pstmt.close();
             con.close(); 	

  		} catch (SchedulerException | SQLException e) {
  			e.printStackTrace();
  			msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "");
  	    	FacesContext.getCurrentInstance().addMessage(null, msj);
  		} 		
  		
  	//Creamos la instancia calendario para separar en dia, mes y año la fecha seleccionada
	   	 Calendar cal = Calendar.getInstance();
	   	 cal.setTime(diainicio);
	   	 int hora = cal.get(Calendar.HOUR_OF_DAY);
	   	 int minutos = cal.get(Calendar.MINUTE); //Para iniciar meses comenzando desde enero = 0
	
  		 //Incia tarea
  		if(frecuencia.equals("0")){    		 
     	   iniciarTareaDiaria(tarea.toUpperCase(), vltrigger.toUpperCase(), arregloParametros, "1", arregloParamNames(vecreporte[0]), hora, minutos);
     	 } else if (frecuencia.equals("1")){
     	   iniciarTareaSemanal(tarea.toUpperCase(), vltrigger.toUpperCase(), dias, arregloParametros, "1", arregloParamNames(vecreporte[0]), hora, minutos);
     	 } else if (frecuencia.equals("2")){
     	   iniciarTareaDiaMes(tarea.toUpperCase(), vltrigger.toUpperCase(), diames, arregloParametros, "1", arregloParamNames(vecreporte[0]), hora, minutos);	
     	 } else if (frecuencia.equals("3")){
     	   iniciarTareaIntervaloMinutos(tarea.toUpperCase(), vltrigger.toUpperCase(), horarepeticion, arregloParametros, "1", arregloParamNames(vecreporte[0]));	
     	 } else if (frecuencia.equals("4")){
     	   iniciarTareaIntervaloMensual(tarea.toUpperCase(), vltrigger.toUpperCase(), arregloParametros, "1", arregloParamNames(vecreporte[0]));	 
     	 } else {
     	   iniciarTareaRepeticion(tarea.toUpperCase(), vltrigger.toUpperCase(),  arregloParametros, "1", arregloParamNames(vecreporte[0]), "0", hora, minutos);
       	 } 
  		//System.out.println("fin");
  	}
  	
  	
  	
  		
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////PARAMETROS DEL REPORTE///////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private List<String> listR = new ArrayList<String>();//Guarda el nombre que se va a mostrar en la pantalla
	private List<String> listPN = new ArrayList<String>();//Guarda el nombre del parámetro
	private List<Integer> listPT = new ArrayList<Integer>();//Guarda el tipo del parámetro
	private List<Boolean> listPH= new ArrayList<Boolean>();//Guarda si el parametro es hidden
	private List<Boolean> listPR= new ArrayList<Boolean>();//Guarda si el parametro es requerido
	private String[] inputs;
	private int rowsParam;
	

	/**
	 * @return the inputs
	 */
	public String[] getInputs() {
		return inputs;
	}

	/**
	 * @param inputs the inputs to set
	 */
	public void setInputs(String[] inputs) {
		this.inputs = inputs;
	}

	/**
	 * @return the listR
	 */
	public List<String> getListR() {
		return listR;
	}

	/**
	 * @param listR the listR to set
	 */
	public void setListR(List<String> listR) {
		this.listR = listR;
	}

	/**
	 * Retorna la ruta real del reporte 
	 * y se lo asigna al método ReadParamsBirtReport
	 * de la clase RunReport, el mismo leerá los parámetros
	 * del reporte parra programar la tarea
	 */
	 public String getRutaRepReal(){
		 //String ruta = extContext.getRealPath(RUTA_REPORTE) + File.separator + reporte.split(" - ")[0].toUpperCase() + ".rptdesign";
		 String ruta = extContext.getRealPath(BIRT_VIEWER_WORKING_FOLDER) + File.separator + reporte.split(" - ")[0].toUpperCase() + ".rptdesign";
		 //System.out.println(ruta);
		 return ruta;
	 }
  		
	 
	 /**
		 * Lee parámetros del los reportes
		 * Usa el API del birt report, Iterando los parámetros
		 * y guardando los valores en un ArrayList
		 * @param: Ruta, la ruta física de ubicación del reporte
		 */
		public void ReadParamsBirtReport(String preporte) {
			//Borra todo lo que contenga la session
			try {
				deleteTemps(session, "bvtparams_temp");
				deleteTemps(session, "bvtparams_number_temp");
			} catch (NamingException e1) {

				e1.printStackTrace();
			}
			
			//Lectura de parámetros de reportes, BIRT ENGINE
			//
			EngineConfig config = new EngineConfig( );
			ReportEngine engine = new ReportEngine( config );
			//Open a report design 
			IReportRunnable design = null;
			try {
				design = engine.openReportDesign(preporte);
			} catch (EngineException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 	
			IGetParameterDefinitionTask task = engine.createGetParameterDefinitionTask( design );
			@SuppressWarnings("rawtypes")
			Collection params = task.getParameterDefns( true );

			@SuppressWarnings("rawtypes")
			Iterator iter = params.iterator( ); 
			//Iterate over all parameters
			while ( iter.hasNext( ) )
			{			
			IParameterDefn param = (IParameterDefn) iter.next( );		
	        	  listPN.add(param.getName());//Guarda el nombre del parámetro
	        	  listPT.add(param.getDataType());//Guarda el tipo de parámetro
                  listPH.add(param.isHidden());//Indica si es hidden
                  listPR.add(param.isRequired());
				{
					if(param.getPromptText()==null){
						listR.add(param.getName());
					} else {
						listR.add(param.getPromptText());
					}
					rowsParam = params.size();
					
				}
			}
			//Una vez obtenido los parámetros los guardamos en una tabla temporal
			//Esto permite hace un mejor manejo en el for each loop al momento de insertar los valores
			//Recorre la lista y va insertando, blanquea la tabla antes de cargarla
			//
			for(int i = 0; i < rowsParam; i++){	
				//System.out.println(listPH.get(i));
		         try {
		        	//Inserta parámetros 
					deleteParam(reporte.split(" - ")[0].toUpperCase(), listPN.get(i), listPT.get(i), session);//Borra temporal
					insertParam(reporte.split(" - ")[0].toUpperCase(), listPN.get(i), listPT.get(i), session, listR.get(i), listPH.get(i).toString(), listPR.get(i).toString());//CArga temporal
					//Inserta el número de parámetros
					deleteParamNumber(reporte.split(" - ")[0].toUpperCase(), session);
					insertParamNumber(reporte.split(" - ")[0].toUpperCase(), session, rowsParam);
				} catch (NamingException e) {

					e.printStackTrace();
				}
		    }			
		   task.close();
		   
		   //Lectura de datos en la tabla posteriormente recorre el arreglo para impimir en pantalla
		   //
		   String[] vecreporte = reporte.split("\\ - ", -1);
		   String vlquery = "select promptext from bvtparams_temp where sessionid = '" + session + "' and codrep = '" + vecreporte[0].toUpperCase() + "' and paramhidden = 'false' and paramrequired = 'true'";
		   try {
			consulta.selectPntGenerica(vlquery, JNDI);
			rowsParam = consulta.getRows();
			
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		   inputs = new String[rowsParam];//Asigna el tamaño a los inputs
		   //Si hay registros en la temporal recorre la tabla 
		   if(rowsParam>0){//Si hay registros muestra los parámetros				     
			     vlTabla = consulta.getArray();
				 RequestContext.getCurrentInstance().execute("PF('dlg2').show()");
	        }
		}
		
		
		/**
	     * Inserta Temporal de parametros.
	     * <p>
	     **/
	    private void insertParam(String codrep, String paramName, Integer paramType, String sessionId, String promptext, String phidden, String prequired) throws  NamingException {
	        try {
	        	Context initContext = new InitialContext();     
	     		DataSource ds = (DataSource) initContext.lookup(JNDI);
	            con = ds.getConnection();
	            
	            String query = "INSERT INTO bvtparams_temp VALUES (?,?,?,?,?,?,?)";
	            pstmt = con.prepareStatement(query);
	            pstmt.setString(1, codrep.toUpperCase());
	            pstmt.setString(2, paramName);
	            pstmt.setInt(3, paramType);
	            pstmt.setString(4, sessionId);
	            pstmt.setString(5, promptext.toUpperCase());
	            pstmt.setString(6, phidden);
	            pstmt.setString(7, prequired);
	            ////System.out.println(query);
	            try {
	                //Avisando
	            	pstmt.executeUpdate();
	             } catch (SQLException e)  {
	            	e.printStackTrace();
	            }
	            
	            pstmt.close();
	            con.close();
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }	
	    }
	    
	    /**
	     * Borra Temporal de parametros.
	     * <p>
	     **/
	    private void deleteParam(String codrep, String paramName, Integer paramType, String sessionId) throws  NamingException {
	        try {
	        	Context initContext = new InitialContext();     
	     		DataSource ds = (DataSource) initContext.lookup(JNDI);
	            con = ds.getConnection();
	            
	            String query = "delete from bvtparams_temp where codrep = ? and paramname = ? and paramtype = ? and sessionid = ?";
	            pstmt = con.prepareStatement(query);
	            pstmt.setString(1, codrep.toUpperCase());
	            pstmt.setString(2, paramName.toUpperCase());
	            pstmt.setInt(3, paramType);
	            pstmt.setString(4, sessionId);

	            ////System.out.println(query);
	            try {
	                //Avisando
	            	pstmt.executeUpdate();
	             } catch (SQLException e)  {
	            	e.printStackTrace();
	            }	            
	            pstmt.close();
	            con.close();
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }	
	    }
	    
	    /**
	     * Inserta Temporal de parametros.
	     * <p>
	     **/
	    private void insertParamNumber(String codrep, String sessionId, Integer paramnumber) throws  NamingException {
	        try {
	        	Context initContext = new InitialContext();     
	     		DataSource ds = (DataSource) initContext.lookup(JNDI);
	            con = ds.getConnection();
	            
	            String query = "INSERT INTO bvtparams_number_temp VALUES (?,?,?)";
	            pstmt = con.prepareStatement(query);
	            pstmt.setString(1, codrep.toUpperCase());
	            pstmt.setString(2, sessionId);
	            pstmt.setInt(3, paramnumber);

	            ////System.out.println(query);
	            try {
	                //Avisando
	            	pstmt.executeUpdate();
	             } catch (SQLException e)  {
	            	e.printStackTrace();
	            }
	            
	            pstmt.close();
	            con.close();
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }	
	    }
	    
	    /**
	     * Borra Temporal de parametros.
	     * <p>
	     **/
	    private void deleteParamNumber(String codrep,  String sessionId) throws  NamingException {
	        try {
	        	Context initContext = new InitialContext();     
	     		DataSource ds = (DataSource) initContext.lookup(JNDI);
	            con = ds.getConnection();
	            
	            String query = "delete from bvtparams_number_temp where codrep = ? and sessionid = ?";
	            pstmt = con.prepareStatement(query);
	            pstmt.setString(1, codrep.toUpperCase());
	            pstmt.setString(2, sessionId);

	            ////System.out.println(query);
	            try {
	                //Avisando
	            	pstmt.executeUpdate();
	             } catch (SQLException e)  {
	            	e.printStackTrace();
	            }	            
	            pstmt.close();
	            con.close();
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }	
	    }
	    
	    
	    /**
	     * Borra Temporal de parametros.
	     * <p>
	     **/
	    private void deleteTemps(String sessionId, String tabla) throws  NamingException {
	        try {
	        	Context initContext = new InitialContext();     
	     		DataSource ds = (DataSource) initContext.lookup(JNDI);
	            con = ds.getConnection();
	            
	            String query = "delete from " + tabla + " where sessionid = '" + sessionId + "'";
	            pstmt = con.prepareStatement(query);
	            //System.out.println(sessionId);
	            try {
	                //Avisando
	            	pstmt.executeUpdate();
	             } catch (SQLException e)  {
	            	e.printStackTrace();
	            }	            
	            pstmt.close();
	            con.close();
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }	
	    }
	    
	    
	    /**
	     * Retorna el número de parámetros
	     * <p>
	     **/
	    public Integer paramsNumber(String reporte){
	    	Integer param = 0;
	    	int vlrows = 0; 
	    	String vlquery = "select paramnumber from bvtparams_number_temp where sessionid = '" + session + "'";
	    	try {
				consulta.selectPntGenerica(vlquery, JNDI);
				vlrows = consulta.getRows();
				if(vlrows>0){
					String [][] vltabla = consulta.getArray();
					param = Integer.parseInt(vltabla[0][0]);
				}
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return param;
	    }
	    
	    
	    /**
	     * Retorna el nombre de parámetros
	     * de la tabla temporal que almacena los parámetros
	     * Convierte las columnas en filas separadas por (|)
	     * <p>
	     **/
	    public String  arregloParamNames(String reporte){
	    	String paramNames = "";
	    	int vlrows = 0; 
	    	String vlqueryOra = "select codrep, listagg (paramname, '|') WITHIN GROUP (ORDER BY paramname) arr FROM bvtparams_temp  where SESSIONID = '" + session + "' and codrep = '" + reporte + "' GROUP BY codrep";
	    	String vlqueryPg = "select codrep, string_agg(paramname,'|' order by paramname)  arr from bvtparams_temp where SESSIONID = '" + session + "' and codrep = '" + reporte + "' GROUP BY codrep";
	    	String vlquerySqlSrv = "";
	    	try {
				consulta.selectPntGenericaMDB(vlqueryOra, vlqueryPg, vlquerySqlSrv, JNDI);
				vlrows = consulta.getRows();
				if(vlrows>0){
					String [][] vltabla = consulta.getArray();
					paramNames = vltabla[0][1];
				}
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return paramNames;
	    }
	    
	    
	    
	    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	    ////////////////////////////////////OPCIONES PARA MOSTRAR LOS DÍAS DE LA SEMANA////////////////////////////////
	    ///////////////////////SE HACE UN MANEJO DE ESTA FORMA PARA PODER TRASPONER DE FILAS A COLUMNAS////////////////
	    ///////////////////////APACHE STRING UTILS/////////////////////////////////////////////////////////////////////
	    ///////////////////////ABRIL 2017 DVCONSULTORES AD//////////////////////////////////////////////////////////////
	    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	    
	    /**
	     * Abre opciones de dias dependiendo del valor
	     * @throws NamingException 
	     * @throws SQLException 
	     */
	    public void showDays() {
	    	//System.out.println("frecuencia: " + frecuencia);
	    	if(frecuencia.equals("5")){
	    		RequestContext.getCurrentInstance().execute("PF('dlg4').show()");
	    	}
	    }
	    
	    /**
	     * Abre opciones de dias dependiendo del valor
	     * @throws NamingException 
	     * @throws SQLException 
	     */
	    public void showUrl() {
	    	//System.out.println("frecuencia: " + frecuencia);
	    	if(opctareas.equals("2")){
	    		RequestContext.getCurrentInstance().execute("PF('dlg3').show()");
	    	}
	    }
	    
	    
	    
	  //Dias de la semana
		private List<ProgramacionEtl> list1 = new ArrayList<ProgramacionEtl>();
	    private String vdiasSemana;
	    private String vdiasSemanaDesc;


		/**
		 * @return the list1
		 */
		public List<ProgramacionEtl> getList1() {
			return list1;
		}


		/**
		 * @param list1 the list1 to set
		 */
		public void setList1(List<ProgramacionEtl> list1) {
			this.list1 = list1;
		}



		/**
		 * @return the vdiasSemana
		 */
		public String getVdiasSemana() {
			return vdiasSemana;
		}


		/**
		 * @param vdiasSemana the vdiasSemana to set
		 */
		public void setVdiasSemana(String vdiasSemana) {
			this.vdiasSemana = vdiasSemana;
		}

			      	
	  	
	  	 /**
		 * @return the vdiasSemanaDesc
		 */
		public String getVdiasSemanaDesc() {
			return vdiasSemanaDesc;
		}

		/**
		 * @param vdiasSemanaDesc the vdiasSemanaDesc to set
		 */
		public void setVdiasSemanaDesc(String vdiasSemanaDesc) {
			this.vdiasSemanaDesc = vdiasSemanaDesc;
		}

		/**
	     * Leer Datos dias de la semana
		 * @throws SQLException 
	     * @throws NamingException 
	     * @throws IOException 
	     **/ 	
	  	public void selectDiasSemana()  {
	     try {	

	    	 Context initContext = new InitialContext();     
	         DataSource ds = (DataSource) initContext.lookup(JNDI);
	    	 con = ds.getConnection();
	    		
	    	//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
	     	 DatabaseMetaData databaseMetaData = con.getMetaData();
	     	 String productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección	
	   		
	     	String query = "";
	     	
	        	
	     	switch ( productName ) {
	        case "Oracle":
	        	query += "  SELECT ";
	        	query += " case rownum when 1 then 'MON'";
	        	query += " when 2 then 'TUE'";
	        	query += " when 3 then 'WED'";
	      		query += " when 4 then 'THU'";
	      		query += " when 5 then 'FRI'";
	      		query += " when 6 then 'SAT'";
	      		query += " when 7 then 'SUN' end days";
	     	    query += " , case rownum when 1 then 'Lunes'";
	        	query += " when 2 then 'Martes'";
	        	query += " when 3 then 'Miércoles'";
	      		query += " when 4 then 'Jueves'";
	      		query += " when 5 then 'Viernes'";
	      		query += " when 6 then 'Sábado'";
	      		query += " when 7 then 'Domingo' end dias";
	      		query += " FROM dual CONNECT BY LEVEL <= 7";
	             break;
	        case "PostgreSQL":
	        	query = "WITH RECURSIVE t(n) AS (";
	      		query += " SELECT 1";
	      		query += " UNION ALL";
	      		query += " SELECT n+1 FROM t";
	            query += " )";
	            query += " SELECT case n when 1 then 'MON' when 2 then 'TUE' when 3 then 'WED' when 4 then 'THU' when 5 then 'FRI' when 6 then 'SAT' else 'SUN' end , case n when 1 then 'Lunes' when 2 then 'Martes' when 3 then 'Miercoles' when 4 then 'Jueves' when 5 then 'Viernes' when 6 then 'Sábado' else 'Domingo' end ";
	            query += " FROM t  LIMIT 7";
	            break;
	        }

	        pstmt = con.prepareStatement(query);
	        //System.out.println(query);

	         r =  pstmt.executeQuery();
	              
	        while (r.next()){
	        	ProgramacionEtl select = new ProgramacionEtl();
	            select.setVdiasSemana(r.getString(1));
	            select.setVdiasSemanaDesc(r.getString(2));
	        	//Agrega la lista
	        	list1.add(select);
	        }
	        
	        //Cierra las conecciones
	        pstmt.close();
	        con.close();
	        r.close();
	        
	     } catch (SQLException | SchedulerException | NamingException e){
	         e.printStackTrace();    
	     }

	  	}
}

