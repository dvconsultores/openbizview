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

import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

	/**
	 *
	 * @author Andres
	 */
	@ManagedBean
	@ViewScoped
	public class Bvt001C extends Bd implements Serializable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		
		private LazyDataModel<Bvt001C> lazyModel;  
		
		
	
	/**
		 * @return the lazyModel
		 */
		public LazyDataModel<Bvt001C> getLazyModel() {
			return lazyModel;
		}


	@PostConstruct	
	public void init() {
     if(login==null) {
    	 RequestContext.getCurrentInstance().execute("PF('idleDialog').show()");
     } else {
		if (instancia == null){instancia = "999990817832122222";}
		
		IntStream streamMenu = IntStream.range(0,segu.listMenu.size());
		
		//Recorrer opciones de seguridad y salir si no tiene acceso
		//Recorre todo el menú de la lista de sesión y por opción verifica
		streamMenu.forEach(x -> {
			if(segu.listMenu.get(x).opcMenu.equals("R07") && segu.listMenu.get(x).vistaMenu.equals("1")) {
				RequestContext.getCurrentInstance().execute("PF('idleDialogNP').show()");
			}
		});

		lazyModel  = new LazyDataModel<Bvt001C>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 7217573531435419432L;
			
            @Override
			public List<Bvt001C> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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
	  //
		selectDashboardAcces();
		//selectBvt010();
      } //Fin valida nulo
	}

	
	private String b_codrol = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("segrol");
	private String context = "", descripcion = "", indexName = "";
	private String contexttacc = "";
	private Object filterValue = "";
	private List<Bvt001C> list = new ArrayList<Bvt001C>();
	private String codgrup = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("codgrup"); 
 
	@SuppressWarnings("unchecked")
	private List<Bvt001C> listaccesoBvt001C = (List<Bvt001C>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("listaccesoBvt001C");
    private String exito = "exito";
    private String rol = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("rol"); //Usuario logeado
	private String instancia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instancia"); //Usuario logeado
	SeguridadMenuBean segu = new SeguridadMenuBean();
	
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
	 * @param rows the rows to set
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}
	

	


	/**
	 * @return the context
	 */
	public String getContext() {
		return context;
	}


	/**
	 * @param context the context to set
	 */
	public void setContext(String context) {
		this.context = context;
	}


	/**
	 * @return the contexttacc
	 */
	public String getContexttacc() {
		return contexttacc;
	}


	/**
	 * @param contexttacc the contexttacc to set
	 */
	public void setContexttacc(String contexttacc) {
		this.contexttacc = contexttacc;
	}


	/**
	 * @return the listaccesoBvt001C
	 */
	public List<Bvt001C> getListaccesoBvt001C() {
		return listaccesoBvt001C;
	}


	/**
	 * @param listaccesoBvt001C the listaccesoBvt001C to set
	 */
	public void setListaccesoBvt001C(List<Bvt001C> listaccesoBvt001C) {
		this.listaccesoBvt001C = listaccesoBvt001C;
	}
	
	


	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}


	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	
	

	/**
	 * @return the indexName
	 */
	public String getIndexName() {
		return indexName;
	}


	/**
	 * @param indexName the indexName to set
	 */
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}


	/**
	 * @return the codgrup
	 */
	public String getCodgrup() {
		return codgrup;
	}


	/**
	 * @param codgrup the codgrup to set
	 */
	public void setCodgrup(String codgrup) {
		this.codgrup = codgrup;
	}


	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Variables seran utilizadas para capturar mensajes de errores de Oracle y parametros de metodos
	FacesMessage msj = null;
	PntGenerica consulta = new PntGenerica();
	boolean vGacc; //Validador de opciones del menú
	private int rows; //Registros de tabla
	private String login = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario"); //Usuario logeado
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	//Coneccion a base de datos
	//Pool de conecciones JNDI
	Connection con;
	PreparedStatement pstmt = null;
	ResultSet r;

	/**
     * Inserta acceso categoria1.
     * <p>
     * <b>Parametros del Metodo:<b> String rol, String cat1 unidos como un solo string.<br>
     * String pool, String login.<br><br>
     **/
    protected void insert(String pcontext)  {
    		String[] veccodrol = b_codrol.split("\\ - ", -1);
    	if(veriInsert(pcontext, veccodrol[0].toUpperCase())) {	
        try {
        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);
            con = ds.getConnection();
            
          //Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
     		DatabaseMetaData databaseMetaData = con.getMetaData();
     		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
            
            String query = "INSERT INTO Bvt001C VALUES (?,?,?," + getFecha(productName) + ",?," + getFecha(productName) + ",?)";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, veccodrol[0].toUpperCase());
            pstmt.setString(2, pcontext.trim());
            pstmt.setString(3, login);
            pstmt.setString(4, login);
            pstmt.setInt(5, Integer.parseInt(instancia));
            ////System.out.println(query);
                //Avisando
            	pstmt.executeUpdate();
            
            pstmt.close();
            con.close();
        } catch (Exception e) {
        	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
        	FacesContext.getCurrentInstance().addMessage(null, msj);
        	exito = "error";
        }
      }
    }
    
    
    /**
     * Inserta 
     * <p>
     **/
    protected void insertFromBvt001B(String pcontext, String prol)  {
    		String[] veccodrol = rol.split("\\ - ", -1);
    	if(veriInsert(pcontext, veccodrol[0].toUpperCase())) {	
        try {
        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);
            con = ds.getConnection();
            
            //Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
     		DatabaseMetaData databaseMetaData = con.getMetaData();
     		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
            
            String query = "INSERT INTO Bvt001C VALUES (?,?,?," + getFecha(productName) + ",?," + getFecha(productName) + ",?)";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, veccodrol[0].toUpperCase());
            pstmt.setString(2, pcontext.trim());
            pstmt.setString(3, login);
            pstmt.setString(4, login);
            pstmt.setInt(5, Integer.parseInt(instancia));
            ////System.out.println(query);
                //Avisando
            	pstmt.executeUpdate();
            
            pstmt.close();
            con.close();
        } catch (Exception e) {
        	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
        	FacesContext.getCurrentInstance().addMessage(null, msj);
        	exito = "error";
        }
      }
    }
    
    
    /**
	  * Filtrado de table
	  */
	  public void updatetbfilter(String pcodrol) {
		 setRol(pcodrol); 
		  
		 listaccesoBvt001C = new ArrayList<Bvt001C>();
		 selectDashboardAcces();
	  }
    
    /*
     * Actualizar acceso a dashboards
     * */
    public void guardar()  { 
    	String[] veccodrol = b_codrol.split("\\ - ", -1);
    	HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
    	String[] paramAcc = request.getParameterValues("toAcc");
    	String[] paramDacc = request.getParameterValues("toDacc");
    	if(paramAcc==null && paramDacc==null){
    		msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("html59"), "");
    		FacesContext.getCurrentInstance().addMessage(null, msj);
    		exito = "error";
    	} else  if(paramAcc!=null && paramDacc!=null){
    		msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("bvtmenuUp1"), "");
    		FacesContext.getCurrentInstance().addMessage(null, msj);
    		exito = "error";
    	} else 	if(paramAcc!=null && paramDacc==null){
             //Recorrido del calculo
    		IntStream.range(0, paramAcc.length).forEach(i -> {
    		insert(paramAcc[i]);
    	});
    		//listaccesoreportes.clear();
            updatetbfilter(veccodrol[0].toUpperCase());
    		
    	} else if (paramAcc==null && paramDacc!=null){
         //Recorrido del calculo
    	IntStream.range(0, paramDacc.length).forEach(i -> {
    		delete(paramDacc[i]);
    	});
    	//listaccesoreportes.clear();
        updatetbfilter(veccodrol[0].toUpperCase());
    	}
		if (exito.equals("exito") ) {
			 msj = new FacesMessage(FacesMessage.SEVERITY_INFO,  getMessage("msnUpdate"), "");
			 FacesContext.getCurrentInstance().addMessage(null, msj);
		}
    	
    }
    
    /**
     * Elimina el acceso a dashboard.
     * <p>
     * Parámetros del Mátodo: String rol
     **/
    public void delete(String pcubo)  { 
    	String[] veccodrol = b_codrol.split("\\ - ", -1);
    	if(veriDelete(pcubo, veccodrol[0].toUpperCase())) {
	        try {
	        	
	        	Context initContext = new InitialContext();     
	     		DataSource ds = (DataSource) initContext.lookup(JNDI);

	     		con = ds.getConnection();		

	        	String query = "DELETE from Bvt001C WHERE b_codrol = '" + veccodrol[0].toUpperCase() + "' and instancia = '" + instancia + "' and CONTEXT ='" + pcubo + "'";
	        		        	
	            pstmt = con.prepareStatement(query);
	            ////System.out.println(query);
	                //Avisando
	                pstmt.executeUpdate();
	               // msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnDelete"), "");

	            pstmt.close();
	            con.close();
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	            msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
	            FacesContext.getCurrentInstance().addMessage(null, msj); 
	            exito = "error";
    	}
    	}
    }
    
    
    
    
    
    /**
     * Leer Datos de bvt001b
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
 		
 		
 		if(codgrup==null){
 			codgrup = " - ";
 		}
 		if(codgrup==""){
 			codgrup = " - ";
 		}
 		
 		String[] veccodgrup = codgrup.split("\\ - ", -1);
 		
 		String query = "";


 		switch ( productName ) {
        case "Oracle":
        	   query += "  select * from ";
        	   query += " ( select query.*, rownum as rn from";
        	   query += " (SELECT trim(a.context), trim(b.descripcion), trim(b.index_name)";
        	   query += " FROM BVT001C a, bvt001B b";
        	   query += " where a.context = b.context";
        	   query += " and a.CONTEXT||b.descripcion like '%" + ((String) filterValue) + "%'";
        	   query += " AND   a.instancia = '" + instancia + "' and a.b_codrol = '" + rol + "'";
        	   if(!veccodgrup[0].equals("")){
    		   query += " AND   B.CODGRUP  = trim('" + veccodgrup[0].toUpperCase() +  "')";
    		   }
	  		   query += " order by " + sortField + ") query";
	  		   query += " ) where rownum <= ?";
	           query += " and rn > (?)";
             break;
        case "PostgreSQL":
        	   query += " SELECT trim(a.CONTEXT), trim(b.descripcion), trim(b.index_name)";
        	   query += " FROM BVT001C a, bvt001B b";
        	   query += " where a.context = b.context";
        	   query += " and a.CONTEXT||b.descripcion like '%" + ((String) filterValue) + "%'";
        	   query += " AND   a.instancia = '" + instancia + "' and b_codrol = '" + rol + "'";
        	   if(!veccodgrup[0].equals("")){
    		   query += " AND   B.CODGRUP  = trim('" + veccodgrup[0].toUpperCase() +  "')";
    		   }
	  		   query += " order by " + sortField ;
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
        	Bvt001C select = new Bvt001C();
     	    select.setContext(r.getString(1));
            select.setDescripcion(r.getString(2));
            select.setIndexName(r.getString(3));
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
  		
  		if(codgrup==null){
 			codgrup = " - ";
 		}
  		if(codgrup==""){
 			codgrup = " - ";
 		}
 		
 		String[] veccodgrup = codgrup.split("\\ - ", -1);


  	   String query = " SELECT trim(a.context), trim(b.descripcion)";
  	   query += " FROM BVT001C a, bvt001B b";
  	   query += " where a.context = b.context";
  	   query += " and a.CONTEXT||b.descripcion like '%" + ((String) filterValue) + "%'";
  	   if(!veccodgrup[0].equals("")){
	   query += " AND   b.CODGRUP  = trim('" + veccodgrup[0].toUpperCase() +  "')";
	   }
  	   query += " AND   a.instancia = '" + instancia + "' and a.b_codrol = '" + rol + "' order by ?";
         
         
  	   String querypg = " SELECT trim(a.CONTEXT), trim(b.descripcion)";
  	   querypg += " FROM BVT001C a, bvt001B b";
  	   querypg += " where a.context = b.context";
  	   querypg += " and a.CONTEXT||b.descripcion like '%" + ((String) filterValue) + "%'";
  	   if(!veccodgrup[0].equals("")){
	   querypg += " AND   B.CODGRUP  = trim('" + veccodgrup[0].toUpperCase() +  "')";
  	    }
  	   querypg += " AND   a.instancia = '" + instancia + "' and b_codrol = '" + rol + "' order by ?";


 		
        consulta.selectGenericaMDB(query, querypg, JNDI);
        rows = consulta.getData().get(0).size();
  	}

  	
  	
  	/**
     * Leer Datos de la tabla de acceso 
     **/ 	
  	private void selectDashboardAcces()  {
  		if(listaccesoBvt001C == null) {
  			listaccesoBvt001C = new ArrayList<Bvt001C>();
  		}
  		
  		if(listaccesoBvt001C.size()==0 && b_codrol != null) {
  			
  	
  		try {
  		Context initContext = new InitialContext();     
    	DataSource ds;
		
			ds = (DataSource) initContext.lookup(JNDI);
		
        con = ds.getConnection();
  		   		
        String[] veccodrol = b_codrol.split("\\ - ", -1);
        
    	//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
  		DatabaseMetaData databaseMetaData = con.getMetaData();
  		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
    	      		
  		String query = "";

  		switch ( productName ) {
  		case "Oracle":
	    	query = "Select TRIM(CONTEXT)";
	        query += " from Bvt001C";
	        query += " where b_codrol = '" + veccodrol[0] + "'";
	        query += " and   instancia = '" + instancia + "'";
	        query += " order by CONTEXT";
	        break;
  		case "PostgreSQL":
	    	query = "Select TRIM(CONTEXT)";
	        query += " from Bvt001C";
	        query += " where b_codrol = '" + veccodrol[0] + "'";
	        query += " and   instancia = '" + instancia + "'";
	        query += " order by CONTEXT";
	        break;
	        }


        ////System.out.println(query);

        
        pstmt = con.prepareStatement(query);
        ////System.out.println(query);
  		
        r =  pstmt.executeQuery();
        
  		
        
        while (r.next()){
        	Bvt001C select = new Bvt001C();
     	    select.setContexttacc(r.getString(1));
        	//Agrega la lista
        	listaccesoBvt001C.add(select);
        }
        setListaccesoBvt001C(listaccesoBvt001C);
        //Cierra las conecciones
        pstmt.close();
        con.close();
        r.close();

        
		} catch (NamingException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  	 }
  	}
   
   /**
  	 * @return the rows
  	 */
  	public int getRows() {
  		return rows;
  	}
  	
  	 /**
  	 * Retorna si el usuario tiene asignado algún reporte
  	 */
   public String isBvt001C(String pcubo){
 	String valor = "fa fa-circle fa-2x text-danger";
 	Bvt001C result = null;
 	//carga lista de roles
 	if(listaccesoBvt001C==null) {
 		listaccesoBvt001C = new ArrayList<Bvt001C>();
 	}
  		
 	result = listaccesoBvt001C.stream().parallel()           //Recorre lista             
             .filter(x -> pcubo.equals(x.getContexttacc()))       //Compara con parametro
             .findAny()                                      //Retorna valor o null si no consigue
             .orElse(null);	
 		
 	if(result!=null){
 		valor = "fa fa-circle fa-2x text-success";
 	}
 	return valor;
  	}

  	/**
	 * Verificar si ya existe el reporte y retornar el mensaje para inserción
	 */
	private Boolean veriInsert(String pcubo, String pcodrol) {
		Boolean retorno = true;
		String query = "select CONTEXT from Bvt001c where CONTEXT = '" + pcubo + "' and b_codrol = '" + pcodrol + "' and instancia = '"+ instancia + "' order by ?";
		PntGenerica consulta = new PntGenerica();
		consulta.selectGenerica(query, JNDI);
		int row = consulta.getData().get(0).size();
		if(row >0) {
			retorno = false;
		}
		return retorno;
	}
	
	
	/**
	 * Verificar si ya existe el reporte y retornar el mensaje para borrado
	 */
	private Boolean veriDelete(String pcubo, String pcodrol) {
		Boolean retorno = true;
		String query = "select CONTEXT from Bvt001C where CONTEXT = '" + pcubo + "' and b_codrol = '" + pcodrol + "' and instancia = '"+ instancia + "' order by ?";
		PntGenerica consulta = new PntGenerica();
		consulta.selectGenerica(query, JNDI);
		int row = consulta.getData().get(0).size();
		if(row == 0) {
			retorno = false;
		}
		return retorno;
	}

  	public void reset() {
  		b_codrol = null;   
  		updatetbfilter(null);
    }
  	
 

}
