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

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

	/**
	 *
	 * @author Andres
	 */
	@ManagedBean
	@ViewScoped
	public class Bvt003 extends Bd implements Serializable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		
	private LazyDataModel<Bvt003> lazyModel;  
	
	/**
	 * @return the lazyModel
	 */
	public LazyDataModel<Bvt003> getLazyModel() {
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
			if(segu.listMenu.get(x).opcMenu.equals("M12") && segu.listMenu.get(x).vistaMenu.equals("1")) {
				new LoginBean().logout();
			}
		});

		
		lazyModel  = new LazyDataModel<Bvt003>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 7217573531435419432L;
			
            @Override
			public List<Bvt003> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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
	     }
	}
	
	private String codrol = "";
	private String desrol = "";
	private Object filterValue = "";
	private List<Bvt003> list = new ArrayList<Bvt003>();
	private int validarOperacion = 0;
	private int rows;
	private String instancia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instancia"); //Usuario logeado
	SeguridadMenuBean segu = new SeguridadMenuBean();

	
	     /**
	 * @return the codrol
	 */
	public String getCodrol() {
		return codrol;
	}
	
	/**
	 * @param codrol the codrol to set
	 */
	public void setCodrol(String codrol) {
		this.codrol = codrol;
	}
	
	/**
	 * @return the desrol
	 */
	public String getDesrol() {
		return desrol;
	}
	
	/**
	 * @param desrol the desrol to set
	 */
	public void setDesrol(String desrol) {
		this.desrol = desrol;
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
	PntGenerica consulta = new PntGenerica();
	boolean vGacc; //Validador de opciones del menú
	private String login = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario"); //Usuario logeado
	FacesMessage msj =  null;
	
	
	//Coneccion a base de datos
	//Pool de conecciones JNDI
	//Coneccion a base de datos
	//Pool de conecciones JNDIFARM
	Connection con;
	PreparedStatement pstmt = null;
	ResultSet r;

	/**
     * Inserta roles.
     **/
    public void insert()  {
    	//Valida que los campos no sean nulos
        try {
        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);

     		con = ds.getConnection();
     		
     		//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
     		DatabaseMetaData databaseMetaData = con.getMetaData();
     		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección		
     		
            String query = "INSERT INTO Bvt003 VALUES (?,?,?," + getFecha(productName) + ",?," + getFecha(productName) + ",?)";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, codrol.toUpperCase());
            pstmt.setString(2, desrol.toUpperCase());
            pstmt.setString(3, login);
            pstmt.setString(4, login);
            pstmt.setInt(5, Integer.parseInt(instancia));
            ////System.out.println(query);

                //Avisando
                pstmt.executeUpdate();
                msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnInsert"), "");               
                limpiarValores();

            pstmt.close();
            con.close();
            
        } catch (Exception e) {
        	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
        	e.printStackTrace();
        }
    	
        FacesContext.getCurrentInstance().addMessage(null, msj);
    }
    
    /**
     * Borra Paises
     **/
    public void delete() {  
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
     		
     		String query = "DELETE from Bvt003 WHERE codrol in (" + param + ") and instancia = '" + instancia + "'";
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
			}
			FacesContext.getCurrentInstance().addMessage(null, msj); 
		}
    
    
    

    /**
     * Actualiza roles
     **/
    public void update() {
        try {
        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);

     		con = ds.getConnection();
     		
     		//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
     		DatabaseMetaData databaseMetaData = con.getMetaData();
     		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
     		
            String query = "UPDATE Bvt003";
             query += " SET desrol = ?, usract = ?, fecact='" + getFecha(productName) + "'";
             query += " WHERE codrol = ? and instancia = '" + instancia + "'";
            ////System.out.println(query);
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, desrol.toUpperCase());
            pstmt.setString(2, login);
            pstmt.setString(3, codrol.toUpperCase());

                //Avisando
                pstmt.executeUpdate();
                if(pstmt.getUpdateCount()==0){
                	msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("msnNoUpdate"), "");
                } else {
                	msj = new FacesMessage(FacesMessage.SEVERITY_INFO,  getMessage("msnUpdate"), "");
                }
                desrol = "";
            	validarOperacion = 0;

            pstmt.close();
            con.close();
        } catch (Exception e) {
        	msj = new FacesMessage(FacesMessage.SEVERITY_INFO, e.getMessage(), "");
        	e.printStackTrace();
        }
        FacesContext.getCurrentInstance().addMessage(null, msj);
    }
    
    public void guardar() throws NamingException, SQLException, ClassNotFoundException{   	
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
        	   query += " (SELECT trim(CODROL), trim(DESROL)";
        	   query += " FROM BVT003";
        	   query += " WHERE codrol||desrol like '%" + ((String) filterValue).toUpperCase() + "%'";
        	   query += " and codrol like '%" + codrol.toUpperCase() + "%'";
        	   query += " AND   instancia = '" + instancia + "'";
	  		   query += " order by " + sortField + ") query";
	  		   query += " ) where rownum <= ?";
	           query += " and rn > (?)";
             break;
        case "PostgreSQL":
        	   query += " SELECT trim(codrol), trim(desrol) ";
        	   query += " FROM BVT003";
        	   query += " WHERE codrol||desrol like '%" + ((String) filterValue).toUpperCase() + "%'";
        	   query += " and codrol like '%" + codrol.toUpperCase() + "%'";
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
     	Bvt003 select = new Bvt003();
     	select.setCodrol(r.getString(1));
        select.setDesrol(r.getString(2));	
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
  	public void counter(Object filterValue ) {

 	   String query = " SELECT trim(CODROL), trim(DESROL)";
 	   query += " FROM BVT003";
 	   query += " WHERE codrol||desrol like '%" + ((String) filterValue).toUpperCase() + "%'";
 	   query += " and codrol like '%" + codrol.toUpperCase() + "%'";
 	   query += " AND   instancia = '" + instancia + "' order by ?";
        
        
       String querypg = " SELECT trim(codrol), trim(desrol) ";
       querypg += " FROM BVT003";
       querypg += " WHERE codrol||desrol like '%" + ((String) filterValue).toUpperCase() + "%'";
       querypg += " and codrol like '%" + codrol.toUpperCase() + "%'";
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
    	codrol = "";
    	desrol = "";
    	validarOperacion = 0;
	}

  	


}
