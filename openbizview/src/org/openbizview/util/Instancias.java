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

import org.apache.commons.lang3.StringUtils;
import org.openbizview.util.PntGenerica;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

	/**
	 *
	 * @author Andres
	 */
	@ManagedBean
	@ViewScoped
	public class Instancias extends Bd implements Serializable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		
		private LazyDataModel<Instancias> lazyModel;  
		
		
		/**
		 * @return the lazyModel
		 */
		public LazyDataModel<Instancias> getLazyModel() {
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
			
			lazyModel  = new LazyDataModel<Instancias>(){
				/**
				 * 
				 */
				private static final long serialVersionUID = 7217573531435419432L;
				
	            @Override
				public List<Instancias> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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
		
	
	private String instancia = "";
	private String descripcion = "";
	private Object filterValue = "";
	private List<Instancias> list = new ArrayList<Instancias>();
	private int validarOperacion = 0;
	SeguridadMenuBean segu = new SeguridadMenuBean();
	

	/**
	 * @return the instancia
	 */
	public String getInstancia() {
		return instancia;
	}

	/**
	 * @param instancia the instancia to set
	 */
	public void setInstancia(String instancia) {
		this.instancia = instancia;
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
	 * @return the validarOperacion
	 */
	public int getValidarOperacion() {
		return validarOperacion;
	}
	/**
	 * @param validarOperacion the validarOperacion to set
	 */
	public void setValidarOperacion(int validarOperacion) {
		this.validarOperacion = validarOperacion;
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
     * Inserta categoria1.
     **/
    public void insert()  {   	
       		
        try {
        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);
            con = ds.getConnection();
            
            //Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
     		DatabaseMetaData databaseMetaData = con.getMetaData();
     		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
            
                 
            String query = "INSERT INTO instancias VALUES (?,?,?," + getFecha(productName) + ",?," + getFecha(productName) + ")";
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(instancia));
            pstmt.setString(2, descripcion.toUpperCase());
            pstmt.setString(3, login);
            pstmt.setString(4, login);
                //Avisando
            	pstmt.executeUpdate();
            	msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnInsert"), "");
                limpiarValores();                
            
            pstmt.close();
            con.close();
        } catch (Exception e) {
        	 e.printStackTrace();
             msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
        }	
        FacesContext.getCurrentInstance().addMessage(null, msj);
    }
    
    
    /*
     * Borrar instancias
     */
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
	
	        	String query = "DELETE from instancias WHERE instancia in (" + param + ")";
	        		        	
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
     * Actualiza instancia
     **/
    public void update() {
         try {
        	 Context initContext = new InitialContext();     
      		DataSource ds = (DataSource) initContext.lookup(JNDI);

      		con = ds.getConnection();	
      		
      		//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
     		DatabaseMetaData databaseMetaData = con.getMetaData();
     		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección	
      		
            String query = "UPDATE instancias";
             query += " SET descripcion = ?, usract = ?, fecact=" + getFecha(productName) + "";
             query += " WHERE instancia = ?";
           // //System.out.println(query);
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, descripcion.toUpperCase());
            pstmt.setString(2, login);
            pstmt.setInt(3, Integer.parseInt(instancia));
            // Antes de ejecutar valida si existe el registro en la base de Datos.
                //Avisando
                pstmt.executeUpdate();
                if(pstmt.getUpdateCount()==0){
                	msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("msnNoUpdate"), "");
                } else {
                	msj = new FacesMessage(FacesMessage.SEVERITY_INFO,  getMessage("msnUpdate"), "");
                }
                descripcion = "";
            	validarOperacion = 0;

            pstmt.close();
            con.close();
        } catch (Exception e) {
        	msj = new FacesMessage(FacesMessage.SEVERITY_INFO, e.getMessage(), "");
        	e.printStackTrace();
        }
        FacesContext.getCurrentInstance().addMessage(null, msj);
    }
    
    
    public void guardar() {   	
    	if(validarOperacion==0){
    		insert();
    	} else {
    		update();
    	}
    }
   
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

  		switch ( productName ) {
        case "Oracle":
        	   query += "  select * from ";
        	   query += " ( select query.*, rownum as rn from";
        	   query += " (SELECT trim(instancia), trim(descripcion)";
        	   query += " FROM instancias";
        	   query += " WHERE instancia||descripcion like '%" + ((String) filterValue).toUpperCase() + "%'";
        	   query += " and instancia like '%" + instancia + "%'";
	  		   query += " order by " + sortField + ") query";
	  		   query += " ) where rownum <= ?";
	           query += " and rn > (?)";
             break;
        case "PostgreSQL":
        	   query += " SELECT instancia, trim(descripcion) ";
        	   query += " FROM instancias";
        	   query += " WHERE cast(instancia as text)||descripcion like '%" + ((String) filterValue).toUpperCase() + "%'";
        	   query += " and cast(instancia as text) like '%" + instancia + "%'";
	  		   query += " order by " + sortField ;
	  		   query += " LIMIT ?";
	           query += " OFFSET ?";
             break;
        }

  		
       pstmt = con.prepareStatement(query);
       pstmt.setInt(1, pageSize);
       pstmt.setInt(2, first);
 		
       r =  pstmt.executeQuery();
       
       while (r.next()){
    	Instancias select = new Instancias();
    	select.setInstancia(r.getString(1));
    	select.setDescripcion(r.getString(2));
       	
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
     * @throws NamingException 
     * @throws IOException 
     **/ 	
  	public void counter(Object filterValue ) {

  	   String query = " SELECT trim(instancia), trim(descripcion)";
  	   query += " FROM instancias";
  	   query += " WHERE instancia||descripcion like '%" + ((String) filterValue).toUpperCase() + "%'";
  	   query += " and instancia like '%" + instancia + "%' order by ?";

         
         
       String querypg = " SELECT instancia, trim(descripcion) ";
       querypg += " FROM instancias";
       querypg += " WHERE cast(instancia as text)||descripcion like '%" + ((String) filterValue).toUpperCase() + "%'";
       querypg += " and cast(instancia as text) like '%" + instancia + "%' order by ?";
       
       consulta.selectGenericaMDB(query, querypg, JNDI);
       rows = consulta.getData().get(0).size();   

  	}
  	
   
   /**
  	 * @return the rows
  	 */
  	public int getRows() {
  		return rows;
  	}

  	private void limpiarValores() {
		// TODO Auto-generated method stub
  		instancia = "";
  		descripcion = "";
  		validarOperacion = 0;
	}

  	/*
	 * Listar instancias al momento del login
	 * si el usuario no tiene alguna instancia predefinida
	 * muestra el modal para seleccionar la instancia,
	 * lee de las instancias asociadas al usuario
	 */
     public List<String> selectInstancias()   {
    	List<String> values = new ArrayList<String>();
  		Context initContext;
		try {
			initContext = new InitialContext();
		  
 		DataSource ds = (DataSource) initContext.lookup(JNDI);
 		con = ds.getConnection();
 		
 		
 		String query = "select a.instancia||' - '||b.descripcion";
	       query += " from instancias_usr a, instancias b";
	       query += " where a.instancia = b.instancia";
  		  		
  		pstmt = con.prepareStatement(query);
       // System.out.println(query);
  		
        r =  pstmt.executeQuery();
        		
        while (r.next()){
 
        values.add(r.getString(1));

        }
        //Cierra las conecciones
        pstmt.close();
        con.close();
		} catch (NamingException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
        
        return values;
    }

}
