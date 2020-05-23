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

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

	/**
	 *
	 * @author Andres
	 */
	@ManagedBean
	@ViewScoped
	public class Bvtcat1 extends Bd implements Serializable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		
		private LazyDataModel<Bvtcat1> lazyModel;  
		
		
		/**
		 * @return the lazyModel
		 */
		public LazyDataModel<Bvtcat1> getLazyModel() {
			return lazyModel;
		}	
	
		@PostConstruct
		public void init() {
			if(login==null) {
		    	 new LoginBean().logout();
		     } else {	
			if (instancia == null){instancia = "999990817832122222";}
			IntStream streamMenu = IntStream.range(0,segu.listMenu.size());
			//Recorrer opciones de seguridad y salir si no tiene acceso
			//Recorre todo el menú de la lista de sesión y por opción verifica
			streamMenu.forEach(x -> {
				if(segu.listMenu.get(x).opcMenu.equals("M02") && segu.listMenu.get(x).vistaMenu.equals("1")) {
					new LoginBean().logout();
				}
			});
		
			
			lazyModel  = new LazyDataModel<Bvtcat1>(){
				/**
				 * 
				 */
				private static final long serialVersionUID = 7217573531435419432L;
				
	            @Override
				public List<Bvtcat1> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
	            	//Filtro
	            	if (filters != null) {
	               	 for(Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
	               		 String filterProperty = it.next(); // table column name = field name
	                     filterValue = filters.get(filterProperty);
	               	 }
	                }

						select(first, pageSize,sortField, filterValue);
						//Counter
						counter(filterValue);
						//Contador lazy
						lazyModel.setRowCount(rows);  //Necesario para crear la paginación
            
					return list;  
	            } 
			};
		     } //Fin validación login null
		}
		
	
	private String codcat1 = "";
	private String descat1 = "";
	private Object filterValue = "";
	private List<Bvtcat1> list = new ArrayList<Bvtcat1>();
	private int validarOperacion = 0;
	private String instancia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instancia"); //Usuario logeado
	SeguridadMenuBean segu = new SeguridadMenuBean();

	
	
	/**
	 * @return the codcat1
	 */
	public String getcodcat1() {
		return codcat1;
	}
	/**
	 * @param codcat1 the codcat to set
	 */
	public void setcodcat1(String codcat1) {
		this.codcat1 = codcat1;
	}
	/**
	 * @return the descat1
	 */
	public String getDescat1() {
		return descat1;
	}
	/**
	 * @param descat1 the descat1 to set
	 */
	public void setDescat1(String descat1) {
		this.descat1 = descat1;
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
            
                 
            String query = "INSERT INTO Bvtcat1 VALUES (?,?,?," + getFecha(productName) + ",?," + getFecha(productName) + ",?)";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, codcat1.toUpperCase());
            pstmt.setString(2, descat1.toUpperCase());
            pstmt.setString(3, login);
            pstmt.setString(4, login);            
            pstmt.setInt(5, Integer.parseInt(instancia));

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
    
    
    public void delete()   {  
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
	
	        	String query = "DELETE from Bvtcat1 WHERE codcat1 in (" + param + ") and instancia = '" + instancia + "'";
	        		        	
	            pstmt = con.prepareStatement(query);

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
     * Actualiza categoria1
     **/
    public void update() {
         try {
        	 Context initContext = new InitialContext();     
      		DataSource ds = (DataSource) initContext.lookup(JNDI);

      		con = ds.getConnection();
      		
      	    //Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
     		DatabaseMetaData databaseMetaData = con.getMetaData();
     		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
      		
            String query = "UPDATE Bvtcat1";
             query += " SET descat1 = ?, usract = ?, fecact=" + getFecha(productName) + "";
             query += " WHERE codcat1 = ? and instancia = '" + instancia + "'";
           // //System.out.println(query);
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, descat1.toUpperCase());
            pstmt.setString(2, login);
            pstmt.setString(3, codcat1.toUpperCase());
            // Antes de ejecutar valida si existe el registro en la base de Datos.

                pstmt.executeUpdate();
                if(pstmt.getUpdateCount()==0){
                	msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("msnNoUpdate"), "");
                } else {
                	msj = new FacesMessage(FacesMessage.SEVERITY_INFO,  getMessage("msnUpdate"), "");
                }
                descat1 = "";
            	validarOperacion = 0;

            pstmt.close();
            con.close();
        } catch (Exception e) {
        	msj = new FacesMessage(FacesMessage.SEVERITY_INFO, e.getMessage(), "");
        	e.printStackTrace();
        }
        FacesContext.getCurrentInstance().addMessage(null, msj);
    }
    
    
    public void guardar() throws NamingException, SQLException{   	
    	if(validarOperacion==0){
    		insert();
    	} else {
    		update();
    	}
    }
   
   /**
    * Leer Datos de paises
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
        	   query += " (SELECT trim(codcat1), trim(descat1)";
        	   query += " FROM BVTcat1";
        	   query += " WHERE codcat1||descat1 like '%" + ((String) filterValue).toUpperCase() + "%'";
        	   query += " and codcat1 like '%" + codcat1.toUpperCase() + "%'";
        	   query += " AND   instancia = '" + instancia + "'";
	  		   query += " order by " + sortField + ") query";
	  		   query += " ) where rownum <= ?";
	           query += " and rn > (?)";
             break;
        case "PostgreSQL":
        	   query += " SELECT trim(codcat1), trim(descat1) ";
        	   query += " FROM BVTcat1";
        	   query += " WHERE codcat1||descat1 like '%" + ((String) filterValue).toUpperCase() + "%'";
        	   query += " and codcat1 like '%" + codcat1.toUpperCase() + "%'";
        	   query += " AND   instancia = '" + instancia + "'";
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
    	Bvtcat1 select = new Bvtcat1();
    	select.setcodcat1(r.getString(1));
    	select.setDescat1(r.getString(2));
       	
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
     **/ 	
  	public void counter(Object filterValue )  {

 	   String query = " SELECT trim(codcat1), trim(descat1)";
 	   query += " FROM BVTcat1";
 	   query += " WHERE codcat1||descat1 like '%" + ((String) filterValue).toUpperCase() + "%'";
 	   query += " and codcat1 like '%" + codcat1.toUpperCase() + "%'";
 	   query += " AND   instancia = '" + instancia + "' order by ?";
        
        
       String querypg = " SELECT trim(codcat1), trim(descat1) ";
       querypg += " FROM BVTcat1";
       querypg += " WHERE codcat1||descat1 like '%" + ((String) filterValue).toUpperCase() + "%'";
       querypg += " and codcat1 like '%" + codcat1.toUpperCase() + "%'";
       querypg += " AND   instancia = '" + instancia + "' order by ?";
       
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
  		codcat1 = "";
  		descat1 = "";
  		validarOperacion = 0;
	}
  	
  	
  	/**
	 * Asigna valores a las variables
	 */
	public void editar(String codcat1, String descat1){
		this.codcat1 = codcat1;
		this.descat1 = descat1;
		validarOperacion = 1;
	}



}
