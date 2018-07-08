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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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

import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

	/**
	 *
	 * @author Andres
	 */
	@ManagedBean
	@ViewScoped
	public class Instanciasusr extends Bd implements Serializable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		
		private LazyDataModel<Instanciasusr> lazyModel;  
		
		
	
	/**
		 * @return the lazyModel
		 */
		public LazyDataModel<Instanciasusr> getLazyModel() {
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
			if(segu.listMenu.get(x).opcMenu.equals("M11") && segu.listMenu.get(x).vistaMenu.equals("1")) {
				RequestContext.getCurrentInstance().execute("PF('idleDialogNP').show()");
			}
		});
		
		lazyModel  = new LazyDataModel<Instanciasusr>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 7217573531435419432L;
			
            @Override
			public List<Instanciasusr> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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

		  selectInstanciasUsr();
	     }
	}

	
	private String coduser = "";
	private String instancias = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("bcoduser");
	private String descripcion = "";
	private Object filterValue = "";
	private List<Instanciasusr> list = new ArrayList<Instanciasusr>();
	private Map<String,String> listUsr = new HashMap<String, String>();   //Lista de compañías disponibles para acceso a seguridad 
    private List<String> selectedUsr;   //Listado de compañias seleccionadas
    private Map<String, String> sorted;
    private String exito = "exito";
    SeguridadMenuBean segu = new SeguridadMenuBean();
	IntStream streamMenu = IntStream.range(0,segu.listMenu.size());
	private String instancia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instancia"); //Usuario logeado
	


	/**
	 * @return the coduser
	 */
	public String getCoduser() {
		return coduser;
	}


	/**
	 * @param coduser the coduser to set
	 */
	public void setCoduser(String coduser) {
		this.coduser = coduser;
	}


	/**
	 * @return the instancias
	 */
	public String getInstancias() {
		return instancias;
	}


	/**
	 * @param instancias the instancias to set
	 */
	public void setInstancias(String instancias) {
		this.instancias = instancias;
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
	public List<Instanciasusr> getList() {
		return list;
	}


	/**
	 * @param list the list to set
	 */
	public void setList(List<Instanciasusr> list) {
		this.list = list;
	}


	/**
	 * @param rows the rows to set
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}
	
	

	/**
	 * @return the listUsr
	 */
	public Map<String, String> getListUsr() {
		return listUsr;
	}


	/**
	 * @param listUsr the listUsr to set
	 */
	public void setListUsr(Map<String, String> listUsr) {
		this.listUsr = listUsr;
	}


	/**
	 * @return the selectedUsr
	 */
	public List<String> getSelectedUsr() {
		return selectedUsr;
	}


	/**
	 * @param selectedUsr the selectedUsr to set
	 */
	public void setSelectedUsr(List<String> selectedUsr) {
		this.selectedUsr = selectedUsr;
	}


	/**
	 * @return the sorted
	 */
	public Map<String, String> getSorted() {
		return sorted;
	}

	/**
	 * @param sorted the sorted to set
	 */
	public void setSorted(Map<String, String> sorted) {
		this.sorted = sorted;
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
    private void insert(String pusuarios)  {
    		String[] veccat1 = pusuarios.split("\\ - ", -1);
        try {
        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);
            con = ds.getConnection();
            
            //Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
     		DatabaseMetaData databaseMetaData = con.getMetaData();
     		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
            
            String query = "INSERT INTO instancias_usr VALUES (?,?,?," + getFecha(productName) + ",?," + getFecha(productName) + ")";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, veccat1[0].toUpperCase());
            pstmt.setInt(2, Integer.parseInt(instancias.split(" - ")[0]));
            pstmt.setString(3, login);
            pstmt.setString(4, login);
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
    
    
    /**
     * Genera las operaciones de guardar o modificar
     * @throws NamingException 
     * @throws SQLException 
     * @throws IOException 
     * @throws ClassNotFoundException 
     **/ 
    public void guardar() {  
        if (selectedUsr.size()<=0){
    		msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("InstanciasUsrIngUsr"), "");
    		FacesContext.getCurrentInstance().addMessage(null, msj);
    	} else {  	
    	   for (int i = 0; i< selectedUsr.size(); i++){
    		  //System.out.println("Selected :" + selectedUsr.get(i));
    		insert(selectedUsr.get(i));           
    	   }
   		limpiarValores();   
        if(exito=="exito"){
        	msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnInsert"), "");
        	FacesContext.getCurrentInstance().addMessage(null, msj);
        }
    	}
    }
    
    public void delete()  {  
    	HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
    	String[] chkbox = request.getParameterValues("toDelete");
    	
    	if (chkbox==null){
    		msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("del"), "");
    	} else {
	        try {
	       	
	        	Context initContext = new InitialContext();     
	     		DataSource ds = (DataSource) initContext.lookup(JNDI);

	     		con = ds.getConnection();		
	        	
	        	String param = "'" + StringUtils.join(chkbox, "','") + "'";
	
	        	String query = "DELETE from instancias_usr WHERE coduser||instancia in (" + param + ")";
	        		        	
	            pstmt = con.prepareStatement(query);
	            ////System.out.println(query);

	                //Avisando
	                pstmt.executeUpdate();
	                msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnDelete"), "");
	                limpiarValores();	
	
	            pstmt.close();
	            con.close();
	
	        } catch (Exception e) {
	        	e.printStackTrace();
                msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
	        }
    	}
    	FacesContext.getCurrentInstance().addMessage(null, msj); 
    }
    
    
    /**
     * Leer Datos de categoria2
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
 		if(instancias==null){
 			instancias = " - ";
 		}
 		if(instancias==""){
 			instancias = " - ";
 		}
 		String[] veccodrol = instancias.split("\\ - ", -1);

  		switch ( productName ) {
        case "Oracle":
        	   query += "  select * from ";
        	   query += " ( select query.*, rownum as rn from";
        	   query += " (SELECT trim(a.coduser), trim(a.instancia), trim(b.descripcion)";
        	   query += " FROM instancias_usr a, instancias b";
  		       query += " where a.instancia=b.instancia";
  		       query += " and   a.instancia like '" + veccodrol[0] + "%'";
        	   query += " AND   a.coduser||a.instancia||b.descripcion like '%" + ((String) filterValue).toUpperCase() + "%'";
	  		   query += " order by " + sortField + ") query";
	  		   query += " ) where rownum <= ?";
	           query += " and rn > (?)";
             break;
        case "PostgreSQL":
      	       query += " SELECT trim(a.coduser), a.instancia, trim(b.descripcion)";
    	       query += " FROM instancias_usr a, instancias b";
		       query += " where a.instancia=b.instancia";
		       query += " and   cast(a.instancia as text) like '" + veccodrol[0] + "%'";
    	       query += " AND   a.coduser||cast(a.instancia as text)||b.descripcion like '%" + ((String) filterValue).toUpperCase() + "%'";
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
        	Instanciasusr select = new Instanciasusr();
     	    select.setCoduser(r.getString(1));
     	    select.setInstancias(r.getString(2));
            select.setDescripcion(r.getString(3));
        	//Agrega la lista
        	list.add(select);
        }
        //Cierra las conecciones
        pstmt.close();
        con.close();
        
		} catch (NamingException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
  	}
  	
  	
  	/**
     * Leer registros en la tabla
     **/ 	
  	public void counter(Object filterValue ) {

  		if(instancias==null){
 			instancias = " - ";
 		}
 		if(instancias==""){
 			instancias = " - ";
 		}
 		String[] veccodrol = instancias.split("\\ - ", -1);
 		
 	   String query = " SELECT trim(a.coduser), trim(a.instancia), trim(b.descripcion)";
 	   query += " FROM instancias_usr a, instancias b";
	   query += " where a.instancia=b.instancia";
	   query += " and   a.instancia like '" + veccodrol[0] + "%'";
 	   query += " AND   a.coduser||a.instancia||b.descripcion like '%" + ((String) filterValue).toUpperCase() + "%'";

        
        
        String querypg = " SELECT trim(a.coduser), a.instancia, trim(b.descripcion)";
        querypg += " FROM instancias_usr a, instancias b";
        querypg += " where a.instancia=b.instancia";
        querypg += " and   cast(a.instancia as text) like '" + veccodrol[0] + "%'";
        querypg += " AND   a.coduser||cast(a.instancia as text)||b.descripcion like '%" + ((String) filterValue).toUpperCase() + "%'";
        querypg += " order by ?" ;

        consulta.selectGenericaMDB(query, querypg, JNDI);
        rows = consulta.getData().get(0).size();    

  	}

  	
  	
  	/**
     * Leer Datos de nominas para asignar a menucheck
     * @throws NamingException 
	 * @throws SQLException 
     * @throws IOException 
     **/ 	
  	private void selectInstanciasUsr() {
  		
  		Context initContext;
		try {
			initContext = new InitialContext();
		   
    	DataSource ds = (DataSource) initContext.lookup(JNDI);
        con = ds.getConnection();
  		   		
    	//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
  		DatabaseMetaData databaseMetaData = con.getMetaData();
  		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
    	      		
  		String query = "";

  		switch ( productName ) {
  		case "Oracle":
	    	query = "Select trim(coduser), trim(coduser)||' - '||trim(desuser)";
	        query += " from bvt002";
	        query += " order by coduser";
	        break;
  		case "PostgreSQL":
  			query = "Select trim(coduser), trim(coduser)||' - '||trim(desuser)";
	        query += " from bvt002";
	        query += " order by coduser";
	        break;
	        }


        //System.out.println(query);

        
        pstmt = con.prepareStatement(query);
        ////System.out.println(query);
  		
        r =  pstmt.executeQuery();
        
  		
        
        while (r.next()){
        	String cat1 = new String(r.getString(1));
        	String descat1 = new String(r.getString(2));
        	
            listUsr.put(descat1, cat1);
            sorted = sortByValues(listUsr);
            
        }
        //Cierra las conecciones
        pstmt.close();
        con.close();
        
		} catch (NamingException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
  	}
  	
  	
  	/*
     * Java method to sort Map in Java by value e.g. HashMap or Hashtable
     * throw NullPointerException if Map contains null values
     * It also sort values even if they are duplicates
     */
    @SuppressWarnings("rawtypes")
	public static <K extends Comparable,V extends Comparable> Map<K,V> sortByValues(Map<K,V> map){
        List<Map.Entry<K,V>> entries = new LinkedList<Map.Entry<K,V>>(map.entrySet());
     
        Collections.sort(entries, new Comparator<Map.Entry<K,V>>() {

            @SuppressWarnings("unchecked")
			@Override
            public int compare(Entry<K, V> o1, Entry<K, V> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
     
        //LinkedHashMap will keep the keys in the order they are inserted
        //which is currently sorted on natural ordering
        Map<K,V> sortedMap = new LinkedHashMap<K,V>();
     
        for(Map.Entry<K,V> entry: entries){
            sortedMap.put(entry.getKey(), entry.getValue());
        }
     
        return sortedMap;
    }

   
   /**
  	 * @return the rows
  	 */
  	public int getRows() {
  		return rows;
  	}

  	private void limpiarValores() {
 		// TODO Auto-generated method stub
  		instancias = "";
 	}

  	public void reset() {
  		instancias = null;     
    }

}
