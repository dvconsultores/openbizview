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

package org.openbizview.util;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;


import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;


	/**
	 *
	 * @author Andres
	 */
	@ManagedBean
	@ViewScoped
	public class Bvtmenu extends Bd implements Serializable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
      private LazyDataModel<Bvtmenu> lazyModel;  
		
		
		/**
		 * @return the lazyModel
		 */
		public LazyDataModel<Bvtmenu> getLazyModel() {
			return lazyModel;
		}	
	
	
	@PostConstruct	
	public void init(){		
		if(login==null) {
	    	 new LoginBean().logout();
	     } else {	
		if (instancia == null){instancia = "999990817832122222";}
		IntStream streamMenu = IntStream.range(0,segu.listMenu.size());
		
		//Recorrer opciones de seguridad y salir si no tiene acceso
		//Recorre todo el menú de la lista de sesión y por opción verifica
		streamMenu.forEach(x -> {
			if(segu.listMenu.get(x).opcMenu.equals("M13") && segu.listMenu.get(x).vistaMenu.equals("1")) {
				new LoginBean().logout();
			}
		});
				
		lazyModel  = new LazyDataModel<Bvtmenu>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 7217573531435419432L;
			
            @Override
			public List<Bvtmenu> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
            	//Filtro
            	if (filters != null) {
               	 for(Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
               		 String filterProperty = it.next(); // table column name = field name
                     filterValue = filters.get(filterProperty);
               	 }
                }

            		//Consulta
					select(first, pageSize,sortField, filterValue);
					//Counter
					counter(filterValue);
					//Contador lazy
					lazyModel.setRowCount(rows);  //Necesario para crear la paginación
           
				return list;  
            }             
		};
		
		
	     }
	}
	
	
	private String codopc = "";
	private String desopc = "";
	private String codvis = "";
	private String b_codrol = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("segrol");

	private List<Bvtmenu> list = new ArrayList<Bvtmenu>();
	String exito = "exito";
	private String instancia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instancia"); //Usuario logeado
	SeguridadMenuBean segu = new SeguridadMenuBean();
	private Object filterValue = "";
	
	
	/**
	 * @return the codopc
	 */
	public String getCodopc() {
		return codopc;
	}
	
	/**
	 * @param codopc the codopc to set
	 */
	public void setCodopc(String codopc) {
		this.codopc = codopc;
	}
	
	/**
	 * @return the desopc
	 */
	public String getDesopc() {
		return desopc;
	}
	
	/**
	 * @param desopc the desopc to set
	 */
	public void setDesopc(String desopc) {
		this.desopc = desopc;
	}
	
	/**
	 * @return the codvis
	 */
	public String getCodvis() {
		return codvis;
	}
	
	/**
	 * @param codvis the codvis to set
	 */
	public void setCodvis(String codvis) {
		this.codvis = codvis;
	}

	
    

	/**
	 * @return the b_codrol
	 */
	public String getB_codrol() {
		return b_codrol;
	}

	/**
	 * @param b_codrol the b_codrol to set
	 */
	public void setB_codrol(String b_codrol) {
		this.b_codrol = b_codrol;
	}

	
    

	/**
	 * @return the list
	 */
	public List<Bvtmenu> getList() {
		return list;
	}


	/**
	 * @param list the list to set
	 */
	public void setList(List<Bvtmenu> list) {
		this.list = list;
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


	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Variables seran utilizadas para capturar mensajes de errores de Oracle y parametros de metodos
	PntGenerica consulta = new PntGenerica();
	boolean vGacc; //Validador de opciones del menú
	private String login = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario"); //Usuario logeado
	FacesMessage msj =  null;
	private int rows;

	
	
	//Coneccion a base de datos
	//Pool de conecciones JNDI
	//Coneccion a base de datos
	//Pool de conecciones JNDIFARM
	Connection con;
	PreparedStatement pstmt = null;
	ResultSet r;

	/**
     * Inserta acceso menu.
     * <p>
     * <b>Parametros del Metodo:<b> String b_codrol, String codopc, String desopc, String codvis.<br>
     * String pool, String login.<br><br>
     **/
    public void insertBvTMENU(String rol, String strValores, String pool, String login)  {
       String[] vecValores = strValores.split("\\|", -1);
       String[] veccodrol = rol.split("\\ - ", -1);
        try {
        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);

     		con = ds.getConnection();
     		
     		//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
     		DatabaseMetaData databaseMetaData = con.getMetaData();
     		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección		
     		
            String query = "INSERT INTO Bvtmenu VALUES (?,?,?,?,?," + getFecha(productName) + ",?," + getFecha(productName) + ",?)";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, veccodrol[0]);
            pstmt.setString(2, vecValores[0].toUpperCase());
            pstmt.setString(3, vecValores[1]);
            pstmt.setString(4, vecValores[2].toUpperCase());
            pstmt.setString(5, login);
            pstmt.setString(6, login);
            pstmt.setInt(7, Integer.parseInt(instancia));
            ////System.out.println(query);
            try {
                //Avisando
                pstmt.executeUpdate();
                      
             } catch (SQLException e)  {
            	 exito = "error";
            	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            	FacesContext.getCurrentInstance().addMessage(null, msj);
            }
            pstmt.close();
            con.close();
            
        } catch (Exception e) {
        }
    	        
    }
    
    /**
    * Inserta acceso botones.
    * <p>
    * <b>Parametros del Metodo:<b> String b_codrol, String codopc, String desopc, String codvis.<br>
    * String pool, String login.<br><br>
     * @throws SQLException 
     * @throws ClassNotFoundException 
     * @throws IOException 
    **/
   public void insertBvTMENUOPC(String pcodrol) {
	   String[] veccodrol = pcodrol.split("\\ - ", -1);
	   consulta.selectGenerica("select * from bvtmenu where b_codrol = '" + veccodrol[0].toUpperCase() + "' order by ?" , JNDI);
	   int registros = consulta.getData().get(0).size();
	   ////System.out.println("Registros: " + registros);
	   
	   if (registros==28){
		  // msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("bvtmenumsj"), ""); 
		  // FacesContext.getCurrentInstance().addMessage(null, msj);
		   exito="error";
	   } else {
	   //Menú estandart
	   //Básicos
	   insertBvTMENU(veccodrol[0], "M01|"+getMessage("bas").toUpperCase()+"|0", JNDI, login);
	   insertBvTMENU(veccodrol[0], "M02|"+getMessage("bas1")+"|0", JNDI, login);
	   insertBvTMENU(veccodrol[0], "M03|"+getMessage("bas2")+"|0", JNDI, login);
	   insertBvTMENU(veccodrol[0], "M04|"+getMessage("bas3")+"|0", JNDI, login);
	   insertBvTMENU(veccodrol[0], "M05|"+getMessage("bas4")+"|0", JNDI, login);
	   //Administración
       insertBvTMENU(veccodrol[0], "M06|"+getMessage("adm").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "M07|"+getMessage("adm1")+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "M08|"+getMessage("adm2")+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "M09|"+getMessage("adm3")+"|0", JNDI, login);
       //Seguridad
       insertBvTMENU(veccodrol[0], "M10|"+getMessage("seg").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "M11|"+getMessage("instancias")+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "M12|"+getMessage("seg01")+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "M13|"+getMessage("seg02")+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "M14|"+getMessage("seg03")+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "M15|"+getMessage("seg08")+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "M16|"+getMessage("seg09")+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "M17|"+getMessage("seg04")+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "M18|"+getMessage("seg05")+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "M19|"+getMessage("seg06")+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "M20|"+getMessage("seg07")+"|0", JNDI, login);
       //Tareas
       insertBvTMENU(veccodrol[0], "M21|"+getMessage("mail").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "M22|"+getMessage("mail02")+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "M23|"+getMessage("mail03")+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "M24|"+getMessage("mail04")+"|0", JNDI, login); 
       insertBvTMENU(veccodrol[0], "M25|"+getMessage("mail05")+"|0", JNDI, login);
       //Reportes
       insertBvTMENU(veccodrol[0], "R01|"+getMessage("rep").toUpperCase()+"|0", JNDI, login);  
       //Pivot
       insertBvTMENU(veccodrol[0], "R02|"+getMessage("cube").toUpperCase()+"|0", JNDI, login); 
       insertBvTMENU(veccodrol[0], "R03|"+getMessage("cube1")+"|0", JNDI, login); 
       insertBvTMENU(veccodrol[0], "R04|"+getMessage("cube2")+"|0", JNDI, login);
       //Dashboard
       insertBvTMENU(veccodrol[0], "R05|"+getMessage("dashboard").toUpperCase()+"|0", JNDI, login); 
       insertBvTMENU(veccodrol[0], "R06|"+getMessage("dashboard1")+"|0", JNDI, login); 
       insertBvTMENU(veccodrol[0], "R07|"+getMessage("dashboard2")+"|0", JNDI, login);
	   }
	   if(exito.equals("exito")){
	   msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnInsert"), "");  
	   FacesContext.getCurrentInstance().addMessage(null, msj);
	   }
	   consulta.getData().clear();
   }
	   
   
   /**
    * Borra Paises
    * <p>
    * Parametros del metodo: String codpai. Pool de conecciones
    *
   public void delete() throws  NamingException {  
	        try {
   		Context initContext = new InitialContext();     
    		DataSource ds = (DataSource) initContext.lookup(JNDI);

    		con = ds.getConnection();
    		
    		String[] veccodrol = b_codrol.split("\\ - ", -1);
    		
    		String query = "DELETE from bvtmenu WHERE b_codrol ='" + veccodrol[0].toUpperCase() + "' and instancia = '" + instancia + "'";
           pstmt = con.prepareStatement(query);
           ////System.out.println(query);
           //Antes de insertar verifica si el rol del usuario tiene permisos para insertar
          
               pstmt.executeUpdate();
               msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnDelete"), "");
               limpiarValores();

           pstmt.close();
           con.close();

       } catch (Exception e) {
    	   e.printStackTrace();
           msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
       }
			
			FacesContext.getCurrentInstance().addMessage(null, msj); 
		}
	*/
   

   
    /**
     * Actualiza bvtmenu
     * <p> Parámetros del método: String codvis, String p_codrol, String codopc
     **/
    private void updateBvtmenu(String param, String codvis)  {
        String[] veccodrol = b_codrol.split("\\ - ", -1);
        ////System.out.println(cadStr);
        try {
        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);

     		con = ds.getConnection();
     		
            String query = "UPDATE BVTMENU SET CODVIS = '" + codvis + "'"
                    + " WHERE B_CODROL = ? AND CODOPC IN ('" + param + "') and instancia = '" + instancia + "'";
            ////System.out.println(query);
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, veccodrol[0].toUpperCase());

                //Avisando
                pstmt.executeUpdate();
                                
            pstmt.close();
            con.close();
            
   		 
        } catch (Exception e) {
        	msj = new FacesMessage(FacesMessage.SEVERITY_INFO, e.getMessage(), "");
        	FacesContext.getCurrentInstance().addMessage(null, msj);
        	exito = "error";
        }
        
    }
    
	 /**
	 * Retorna si el usuario tiene asignado algún reporte
	 */
	 public String isMenu(String pcodvis){
	 	String valor = "fa fa-circle fa-2x text-danger";	
	 	if(pcodvis.equals("0")){
	 		valor = "fa fa-circle fa-2x text-success";
	 	}
	 	return valor;
	  	}
    
    /*
     * Actualizar vista menu
     * */
    public void guardar()  { 

    	HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
    	String[] paramAcc = request.getParameterValues("toAcc");
    	String[] paramDacc = request.getParameterValues("toDacc");
    	if(paramAcc!=null && paramDacc!=null){
    		msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("bvtmenuUp1"), "");
    		FacesContext.getCurrentInstance().addMessage(null, msj);
    		exito = "error";
    	} else 	if(paramAcc!=null && paramDacc==null && paramAcc.length>0){
             //Recorrido del calculo
    		IntStream.range(0, paramAcc.length).forEach(i -> {
    		updateBvtmenu(paramAcc[i], "0");
    	});
    		
    	} else if (paramAcc==null && paramDacc!=null && paramDacc.length>0){
         //Recorrido del calculo
    	IntStream.range(0, paramDacc.length).forEach(i -> {
    		updateBvtmenu(paramDacc[i], "1");
    	});
    	}
    	segu.listMenu.clear();
  		segu.opcmnu();
		if (exito.equals("exito") ) {
			 msj = new FacesMessage(FacesMessage.SEVERITY_INFO,  getMessage("msnUpdate"), "");
			 FacesContext.getCurrentInstance().addMessage(null, msj);
		}

    }
    
    /*
    public void guardar() throws NamingException, SQLException, ClassNotFoundException{   	
    	HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
    	String[] paramAcc = request.getParameterValues("toAcc");
    	String[] paramDacc = request.getParameterValues("toDacc");
    	////System.out.println(paramAcc);
    	////System.out.println(paramDacc);
    	if(paramAcc==null && paramDacc==null){
    		////System.out.println("Inserta");
    		insertBvTMENUOPC();
    	} else {
    		////System.out.println("Update");
    		update();
    	}
    }*/
    
    /*

    
    /**
     * Leer Datos de paises
     * @throws NamingException 
     * @throws IOException 
     **/ 	
  	public void select(int first, int pageSize, String sortField, Object filterValue)  {
  		
  		Context initContext;
		try {
			initContext = new InitialContext();
		    
 		DataSource ds = (DataSource) initContext.lookup(JNDI);
 		con = ds.getConnection();
 		//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
 		DatabaseMetaData databaseMetaData = con.getMetaData();
 		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
 		
 		
 		String query = "";
 		
 		if(b_codrol==null){
 			b_codrol = " - ";
 		}
 		if(b_codrol.equals("")){
 			b_codrol = " - ";
 		}
 		
 		String[] veccodrol = b_codrol.split("\\ - ", -1);
 		
  		switch ( productName ) {
        case "Oracle":
        	   query += "  select * from ";
        	   query += " ( select query.*, rownum as rn from";
        	   query += " (SELECT  trim(codopc), trim(desopc), codvis, trim(b_codrol)";
        	   query += " FROM BVTMENU";
        	   query += " WHERE b_codrol||codopc||desopc like '%" + ((String) filterValue).toUpperCase() + "%'";
        	   query += " and b_codrol = trim('" + veccodrol[0].toUpperCase() +  "')";
        	   query += " AND   instancia = '" + instancia + "'";
	  		   query += " order by " + sortField + ") query";
	  		   query += " ) where rownum <= ?";
	           query += " and rn > (?)";
             break;
        case "PostgreSQL":
        	   query += " SELECT  trim(codopc), trim(desopc), codvis, trim(b_codrol) ";
        	   query += " FROM BVTMENU";
        	   query += " WHERE b_codrol||codopc||desopc like '%" + ((String) filterValue).toUpperCase() + "%'";
        	   query += " and b_codrol = trim('" + veccodrol[0].toUpperCase() +  "')";
        	   query += " AND   instancia = '" + instancia + "'";
	  		   query += " order by " + sortField + "";
	  		   query += " LIMIT ?";
	           query += " OFFSET ?";
             break;
          }
 		
        
        pstmt = con.prepareStatement(query);
        pstmt.setInt(1, pageSize);
        pstmt.setInt(2, first);
        //System.out.println(query);
  		
        r =  pstmt.executeQuery();
        
        while (r.next()){
        	Bvtmenu select = new Bvtmenu();
     		select.setCodopc(r.getString(1));
     		select.setDesopc(r.getString(2));
     		select.setCodvis(r.getString(3));
     		select.setB_codrol(r.getString(4));
        	//Agrega la lista
        	list.add(select);
        }
        //Cierra las conecciones
        pstmt.close();
        con.close();
        r.close();
        
        
		} catch (NamingException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
  	}
  	
  	
  	/**
     * Leer registros en la tabla
     * @throws NamingException 
     * @throws IOException 
     **/ 	
  	public void counter(Object filterValue )  {
  		
  		if(b_codrol==null){
 			b_codrol = " - ";
 		}
 		if(b_codrol.equals("")){
 			b_codrol = " - ";
 		}
 		
 		String[] veccodrol = b_codrol.split("\\ - ", -1);	
		
 	   String query = " SELECT  trim(codopc), trim(desopc), decode(codvis,'0','ACCESO','SIN ACCESO'), trim(b_codrol)";
 	   query += " FROM BVTMENU";
 	   query += " WHERE b_codrol||codopc||desopc like '%" + ((String) filterValue).toUpperCase() + "%'";
 	   query += " and b_codrol = trim('" + veccodrol[0].toUpperCase() +  "')";
 	   query += " AND   instancia = '" + instancia + "'";
	   query += " order by ?";

        
        
	   String querypg = " SELECT  trim(codopc), trim(desopc), case when codvis = '0' then 'ACCESO' else 'SIN ACCESO' end, trim(b_codrol) ";
	   querypg += " FROM BVTMENU";
	   querypg += " WHERE b_codrol||codopc||desopc like '%" + ((String) filterValue).toUpperCase() + "%'";
	   querypg += " and b_codrol = trim('" + veccodrol[0].toUpperCase() +  "')";
	   querypg += " AND   instancia = '" + instancia + "'";
	   querypg += " order by ?";

       consulta.selectGenericaMDB(query, querypg, JNDI);
       rows = consulta.getData().get(0).size();   
       
       
       
  	}
  	
    /**
     * Carga inicial del rol si no es existente en tabla
     */
  	public void inicial(String pcodrol) {
  		String[] veccodrol = pcodrol.split("\\ - ", -1);
  		insertBvTMENUOPC(veccodrol[0].toUpperCase());
  		setRol(pcodrol);
  		
//  		segu.listMenu.clear();
//  		segu.opcmnu();
  	}
	
	
	public void reset() {
  		b_codrol = null; 
    }



}
