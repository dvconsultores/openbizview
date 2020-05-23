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

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Andres
 */
@ManagedBean
@ViewScoped
public class Bvtcat3 extends Bd implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private LazyDataModel<Bvtcat3> lazyModel;  
	
	/**
	 * @return the lazyModel
	 */
	public LazyDataModel<Bvtcat3> getLazyModel() {
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
			if(segu.listMenu.get(x).opcMenu.equals("M04") && segu.listMenu.get(x).vistaMenu.equals("1")) {
				new LoginBean().logout();
			}
		});

		
		lazyModel  = new LazyDataModel<Bvtcat3>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 7217573531435419432L;
			
            @Override
			public List<Bvtcat3> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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
	

	private String b_codcat1 = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cat1"); 
	private String codcatdescat1 = "";
	private String b_codcat2 = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cat2"); 
	private String codcatdescat2 = "";
	private String codcat3 = "";
	private String descat3 = "";
	private Object filterValue = "";
	private List<Bvtcat3> list = new ArrayList<Bvtcat3>();
	private int validarOperacion = 0;
	private String instancia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instancia"); //Usuario logeado
	SeguridadMenuBean segu = new SeguridadMenuBean();

	/**
	 * @return the codcat3
	 */
	public String getCodcat3() {
		return codcat3;
	}

	/**
	 * @param codcat3 the codcat3 to set
	 */
	public void setCodcat3(String codcat3) {
		this.codcat3 = codcat3;
	}

	/**
	 * @return the descat4
	 */
	public String getDescat3() {
		return descat3;
	}

	/**
	 * @param descat4 the descat4 to set
	 */
	public void setDescat3(String descat3) {
		this.descat3 = descat3;
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

	/**
	 * @param rows the rows to set
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}
	
	

		/**
	 * @return the b_codcat1
	 */
	public String getB_codcat1() {
		return b_codcat1;
	}

	/**
	 * @param b_codcat1 the b_codcat1 to set
	 */
	public void setB_codcat1(String b_codcat1) {
		this.b_codcat1 = b_codcat1;
	}

	/**
	 * @return the b_codcat2
	 */
	public String getB_codcat2() {
		return b_codcat2;
	}

	/**
	 * @param b_codcat2 the b_codcat2 to set
	 */
	public void setB_codcat2(String b_codcat2) {
		this.b_codcat2 = b_codcat2;
	}
	
	

		/**
	 * @return the codcatdescat1
	 */
	public String getCodcatdescat1() {
		return codcatdescat1;
	}

	/**
	 * @param codcatdescat1 the codcatdescat1 to set
	 */
	public void setCodcatdescat1(String codcatdescat1) {
		this.codcatdescat1 = codcatdescat1;
	}

	/**
	 * @return the codcatdescat2
	 */
	public String getCodcatdescat2() {
		return codcatdescat2;
	}

	/**
	 * @param codcatdescat2 the codcatdescat2 to set
	 */
	public void setCodcatdescat2(String codcatdescat2) {
		this.codcatdescat2 = codcatdescat2;
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
     * Inserta categoria3.
     **/
    public void insert()  {
    	//Valida que los campos no sean nulos
   
    		String[] veccodcat1 = b_codcat1.split("\\ - ", -1);
    		String[] veccodcat2 = b_codcat2.split("\\ - ", -1);
        try {
        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);
            con = ds.getConnection();
            
            //Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
     		DatabaseMetaData databaseMetaData = con.getMetaData();
     		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
            
            String query = "INSERT INTO Bvtcat3 VALUES (?,?,?,?,?," + getFecha(productName) + ",?," + getFecha(productName) + ",?)";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, veccodcat1[0].toUpperCase());
            pstmt.setString(2, veccodcat2[0].toUpperCase());
            pstmt.setString(3, codcat3.toUpperCase());
            pstmt.setString(4, descat3.toUpperCase());
            pstmt.setString(5, login);
            pstmt.setString(6, login);
            pstmt.setInt(7, Integer.parseInt(instancia));

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
	
	        	String query = "DELETE from Bvtcat3 WHERE b_codcat1||b_codcat2||codcat3 in (" + param + ") and instancia = '" + instancia + "'";
	        		        	
	            pstmt = con.prepareStatement(query);
	            ////System.out.println(query);
	
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
     * Actualiza categoria3
     **/
    public void update() {
   
    	String[] veccodcat1 = b_codcat1.split("\\ - ", -1);
		String[] veccodcat2 = b_codcat2.split("\\ - ", -1);
        try {
        	Context initContext = new InitialContext();     
        	DataSource ds = (DataSource) initContext.lookup(JNDI);

        	con = ds.getConnection();		
        	
        	//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
     		DatabaseMetaData databaseMetaData = con.getMetaData();
     		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
        		
            String query = "UPDATE Bvtcat3";
             query += " SET descat3 = ?, usract = ?, fecact=" + getFecha(productName) + "";
             query += " WHERE b_codcat1 = ? and b_codcat2 = ? and codcat3 = ? and instancia = '" + instancia + "'";
            ////System.out.println(query);
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, descat3.toUpperCase());
            pstmt.setString(2, login);
            pstmt.setString(3, veccodcat1[0].toUpperCase());
            pstmt.setString(4, veccodcat2[0].toUpperCase());
            pstmt.setString(5, codcat3.toUpperCase());

                pstmt.executeUpdate();
                if(pstmt.getUpdateCount()==0){
                	msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("msnNoUpdate"), "");
                } else {
                	msj = new FacesMessage(FacesMessage.SEVERITY_INFO,  getMessage("msnUpdate"), "");
                }
                descat3 = "";
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
    		////System.out.println("Insertar");
    		insert();
    	} else {
    		////System.out.println("Guardar");
    		update();
    	}
    }
  
   
   /**
    * Leer Datos de categoria2
    **/ 	
 	public void select(int first, int pageSize, String sortField, Object filterValue) {
 		
 		Context initContext;
		try {
			initContext = new InitialContext();
		   
 		DataSource ds = (DataSource) initContext.lookup(JNDI);
 		con = ds.getConnection();	
 		//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
 		DatabaseMetaData databaseMetaData = con.getMetaData();
 		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
 		
 		if(b_codcat1 == null){
			b_codcat1 = " - ";
		}
		if(b_codcat1 == ""){
			b_codcat1 = " - ";
		}
		
		if(b_codcat2 == null){
			b_codcat2 = " - ";
		}
		if(b_codcat2 == ""){
			b_codcat2 = " - ";
		}
		
 		String[] veccodcat1 = b_codcat1.split("\\ - ", -1);
		String[] veccodcat2 = b_codcat2.split("\\ - ", -1);
 		
		String query = "";

  		switch ( productName ) {
        case "Oracle":
        	   query += "  select * from ";
        	   query += " ( select query.*, rownum as rn from";
        	   query += " (SELECT trim(A.codcat3), trim(A.descat3), trim(A.B_CODCAT1), trim(B.DESCAT1), trim(A.B_CODCAT2), trim(C.DESCAT2) ";
        	   query +=  " FROM BVTCAT3 A, BVTCAT1 B, BVTCAT2 C";
               query += " WHERE A.B_CODCAT1=B.CODCAT1";
               query += " AND A.B_CODCAT1=C.B_CODCAT1";
               query += " AND A.B_CODCAT2=C.CODCAT2";
               query += " and A.instancia=B.instancia";
               query += " and A.instancia=c.instancia";
               if(!veccodcat1[0].equals("")){
               query += " and  A.b_codcat1 = '" + veccodcat1[0].toUpperCase() + "'";
               }
               if(!veccodcat2[0].equals("")){
               query += " and  A.b_codcat2 = '" + veccodcat2[0].toUpperCase() + "'";
               }
               query += " and  A.codcat3 ||a.descat3 like  '%" + ((String) filterValue).toUpperCase() + "%'";
               query += " AND   a.instancia = '" + instancia + "'";
               query += " order by a." + sortField + ") query";
               query += " ) where rownum <= ?";
	           query += " and rn > (?)";
             break;
        case "PostgreSQL":
        	   query += " SELECT trim(A.codcat3), trim(A.descat3), trim(A.B_CODCAT1), trim(B.DESCAT1), trim(A.B_CODCAT2), trim(C.DESCAT2) ";
        	   query +=  " FROM BVTCAT3 A, BVTCAT1 B, BVTCAT2 C";
               query += " WHERE A.B_CODCAT1=B.CODCAT1";
               query += " AND A.B_CODCAT1=C.B_CODCAT1";
               query += " AND A.B_CODCAT2=C.CODCAT2";
               query += " and A.instancia=B.instancia";
               query += " and A.instancia=c.instancia";
               if(!veccodcat1[0].equals("")){
               query += " and  A.b_codcat1 = '" + veccodcat1[0].toUpperCase() + "'";
               }
               if(!veccodcat2[0].equals("")){
               query += " and  A.b_codcat2 = '" + veccodcat2[0].toUpperCase() + "'";
               }
               query += " AND   a.instancia = '" + instancia + "'";
               query += " and  A.codcat3 ||a.descat3 like  '%" + ((String) filterValue).toUpperCase() + "%'";
               query += " order by a." + sortField ;
               query += " LIMIT ?";
	           query += " OFFSET ?";
             break;
         }
       
       pstmt = con.prepareStatement(query);
       pstmt.setInt(1, pageSize);
       pstmt.setInt(2, first);
  		
        r =  pstmt.executeQuery();

       
       while (r.next()){
    	Bvtcat3 select = new Bvtcat3();
    	select.setCodcat3(r.getString(1));
    	select.setDescat3(r.getString(2));
        select.setB_codcat1(r.getString(3));
        select.setCodcatdescat1(r.getString(3) + " - " + r.getString(4));
        select.setB_codcat2(r.getString(5));
        select.setCodcatdescat2(r.getString(5) + " - " + r.getString(6));
       	
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

 		
 		if(b_codcat1 == null){
			b_codcat1 = " - ";
		}
		if(b_codcat1 == ""){
			b_codcat1 = " - ";
		}
		
		if(b_codcat2 == null){
			b_codcat2 = " - ";
		}
		if(b_codcat2 == ""){
			b_codcat2 = " - ";
		}
		
 		String[] veccodcat1 = b_codcat1.split("\\ - ", -1);
		String[] veccodcat2 = b_codcat2.split("\\ - ", -1);
		

 	    String query = " SELECT trim(A.codcat3), trim(A.descat3), trim(A.B_CODCAT1), trim(B.DESCAT1), trim(A.B_CODCAT2), trim(C.DESCAT2) ";
 	    query +=  " FROM BVTCAT3 A, BVTCAT1 B, BVTCAT2 C";
        query += " WHERE A.B_CODCAT1=B.CODCAT1";
        query += " AND A.B_CODCAT1=C.B_CODCAT1";
        query += " AND A.B_CODCAT2=C.CODCAT2";
        query += " and A.instancia=B.instancia";
        query += " and A.instancia=c.instancia";
        if(!veccodcat1[0].equals("")){
        query += " and  A.b_codcat1 = '" + veccodcat1[0].toUpperCase() + "'";
        }
        if(!veccodcat2[0].equals("")){
        query += " and  A.b_codcat2 = '" + veccodcat2[0].toUpperCase() + "'";
        }
        query += " and  A.codcat3 ||a.descat3 like  '%" + ((String) filterValue).toUpperCase() + "%'";
        query += " AND   a.instancia = '" + instancia + "' order by ?";

  		
        
        String querypg = " SELECT trim(A.codcat3), trim(A.descat3), trim(A.B_CODCAT1), trim(B.DESCAT1), trim(A.B_CODCAT2), trim(C.DESCAT2) ";
        querypg +=  " FROM BVTCAT3 A, BVTCAT1 B, BVTCAT2 C";
        querypg += " WHERE A.B_CODCAT1=B.CODCAT1";
        querypg += " AND A.B_CODCAT1=C.B_CODCAT1";
        querypg += " AND A.B_CODCAT2=C.CODCAT2";
        querypg += " and A.instancia=B.instancia";
        querypg += " and A.instancia=c.instancia";
        if(!veccodcat1[0].equals("")){
        	querypg += " and  A.b_codcat1 = '" + veccodcat1[0].toUpperCase() + "'";
        }
        if(!veccodcat2[0].equals("")){
        querypg += " and  A.b_codcat2 = '" + veccodcat2[0].toUpperCase() + "'";
        }
        querypg += " AND   a.instancia = '" + instancia + "'";
        querypg += " and  A.codcat3 ||a.descat3 like  '%" + ((String) filterValue).toUpperCase() + "%' order by ?";

  		
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
  		codcat3 = "";
  		descat3 = "";
  		validarOperacion = 0;
	}
  	
  	public void reset() {
  		b_codcat1 = null;  
  		b_codcat2 = null;
    }


}
