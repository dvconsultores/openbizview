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

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
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


@ManagedBean
@SessionScoped
public class Maillista  extends Bd implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private LazyDataModel<Maillista> lazyModel;  
	
	
	/**
	 * @return the lazyModel
	 */
	public LazyDataModel<Maillista> getLazyModel() {
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
		streamMenu.parallel().forEach(x -> {
			if(segu.listMenu.get(x).opcMenu.equals("M23") && segu.listMenu.get(x).vistaMenu.equals("1")) {
				new LoginBean().logout();
			}
		});
		
		lazyModel  = new LazyDataModel<Maillista>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 7217573531435419432L;
			
            @Override
			public List<Maillista> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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

	private String idgrupo = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idgrupo");
	private String idmail = "";
	private String mail = "";
	private int validarOperacion = 0;//Param guardar para validar si guarda o actualiza
	private Object filterValue = "";
	private List<Maillista> list = new ArrayList<Maillista>();
	private String instancia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instancia"); //Usuario logeado
	SeguridadMenuBean segu = new SeguridadMenuBean();
	private String login = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario"); //Usuario logeado
	
     

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
	 * @param lazyModel the lazyModel to set
	 */
	public void setLazyModel(LazyDataModel<Maillista> lazyModel) {
		this.lazyModel = lazyModel;
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
	 * @return the idmail
	 */
	public String getIdmail() {
		return idmail;
	}
	/**
	 * @param idmail the idmail to set
	 */
	public void setIdmail(String idmail) {
		this.idmail = idmail;
	}
	/**
	 * @return the mail
	 */
	public String getMail() {
		return mail;
	}
	/**
	 * @param mail the mail to set
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}
 
	

    /**
	 * @return the list
	 */
	public List<Maillista> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<Maillista> list) {
		this.list = list;
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Variables seran utilizadas para capturar mensajes de errores de Oracle y parametros de metodos
FacesMessage msj = null; 
PntGenerica consulta = new PntGenerica();
private int rows; //Registros de tabla


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
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

		//Coneccion a base de datos
		//Pool de conecciones JNDIFARM
		Connection con;
		PreparedStatement pstmt = null;
		ResultSet r;
	
	/**
     * Inserta.
     **/
    private void insert()  {
        try {
        	 Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);

     		con = ds.getConnection();
    		
     		String[] vecidgrupo = idgrupo.split("\\ - ", -1);

            String query = "INSERT INTO MAILLISTA VALUES (?,?,?,?)";
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(vecidgrupo[0]));
            pstmt.setString(2, idmail.toUpperCase());
            pstmt.setString(3, mail.toLowerCase());
            pstmt.setInt(4, Integer.parseInt(instancia));

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
    
    /**
     * Borra mailconfig
     * <p>
     * @throws NamingException 
     **/  
    public void delete() {  

    	HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
    	String[] chkbox = request.getParameterValues("toDelete");
    	
    	if (chkbox==null){
    		msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("del"), "");
    		FacesContext.getCurrentInstance().addMessage(null, msj); 
    	} else {
        try {

        	 Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);

     		con = ds.getConnection();
    		
        	
        	String param = "'" + StringUtils.join(chkbox, "','") + "'";

        	String query = "DELETE from MAILlista WHERE IDGRUPO||IDMAIL in (" + param + ") and instancia = '" + instancia + "'";
            pstmt = con.prepareStatement(query);
            //System.out.println(query);
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
    	
    }
    
    /**
     * Actualiza mailconfig
     **/
    private void update()  {
        try {

        	 Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);
     		con = ds.getConnection();
     		
     		//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
       		DatabaseMetaData databaseMetaData = con.getMetaData();
       		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
    		
       		
       		String query = "";
       	
        	
        	switch ( productName ) {
            case "Oracle":
            	query = "UPDATE MAILLISTA";
         	    query += " SET mail = ?";
         	    query += " where IDGRUPO = ? and IDMAIL = ? and instancia = '" + instancia + "'";
                 break;
            case "PostgreSQL":
            	query = "UPDATE MAILLISTA";
         	    query += " SET mail = ?";
         	    query += " where cast(IDGRUPO as text) = ? and cast(IDMAIL as text) = ? and instancia = '" + instancia + "'";
                 break;
            }



            //System.out.println(query);
            	   pstmt = con.prepareStatement(query);
                   pstmt.setString(1, mail.toLowerCase());
                   pstmt.setString(2, idgrupo);                  
                   pstmt.setString(3, idmail);

                //Avisando
                pstmt.executeUpdate();
                if(pstmt.getUpdateCount()==0){
                	msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("msnNoUpdate"), "");
                } else {
                msj = new FacesMessage(FacesMessage.SEVERITY_INFO,  getMessage("msnUpdate"), "");
                }
                mail = "";

            pstmt.close();
            con.close();
              

        } catch (Exception e) {
        	e.printStackTrace();
            msj = new FacesMessage(FacesMessage.SEVERITY_FATAL,  e.getMessage(), "");
        }
        FacesContext.getCurrentInstance().addMessage(null, msj); 
    }
    
    
    /**
     * Genera las operaciones de guardar o modificar
     * @throws NamingException 
     * @throws SQLException 
     **/ 
    public void guardar() {   	
    	if(validarOperacion==0){
    		insert();
    	} else {
    		update();
    	}
    }
    
    /**
     * Leer Datos de mailconfig
     * @throws NamingException 
     * @throws IOException 
     **/ 	
  	public void select(int first, int pageSize, String sortField, Object filterValue) {
     try {	

    	 Context initContext = new InitialContext();     
    	 DataSource ds = (DataSource) initContext.lookup(JNDI);
    	 con = ds.getConnection();
    	 
    		//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
   		DatabaseMetaData databaseMetaData = con.getMetaData();
   		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
   		
   		String query = "";
   		
   		if(idgrupo==null){
   			idgrupo = " - ";
 		}
 		if(idgrupo.equals("")){
 			idgrupo = " - ";
 		}
   		
    	String[] vecidgrupo = idgrupo.split("\\ - ", -1);
    	
    	switch ( productName ) {
        case "Oracle":
        	   query += "  select * from ";
        	   query += " ( select query.*, rownum as rn from";
        	   query += " (SELECT trim(A.IDGRUPO), trim(B.IDMAIL), trim(B.MAIL)";
        	   query += " FROM MAILGRUPOS A, MAILLISTA B";
        	   query += " WHERE A.IDGRUPO=B.IDGRUPO";
        	   query += " and A.instancia=B.instancia";
        	   query += " and a.idgrupo||b.idmail||upper(b.mail) like '%" + ((String) filterValue).toUpperCase() + "%'";
        	   if(idgrupo != " - ") { 
        	   query += " and  a.idgrupo = '" + vecidgrupo[0] + "'";	   
        	   }
        	   query += " and  b.idmail like '" + idmail + "%'";
        	   query += " AND   b.instancia = '" + instancia + "'";
	  		   query += " order by " + sortField + ") query";
	  		   query += " ) where rownum <= ?";
	           query += " and rn > (?)";
             break;
        case "PostgreSQL":
      	       query += " SELECT A.IDGRUPO, trim(B.IDMAIL), trim(B.MAIL)";
    	       query += " FROM MAILGRUPOS A, MAILLISTA B";
    	       query += " WHERE A.IDGRUPO=B.IDGRUPO";
    	       query += " and A.instancia=B.instancia";
    	       query += " and cast(a.idgrupo as text)||b.idmail||upper(b.mail) like '%" + ((String) filterValue).toUpperCase() + "%'";
    	       if(idgrupo != " - ") {
               query += " and  cast(a.idgrupo as text) = '" + vecidgrupo[0] + "'";	   
               }
    	       query += " and  b.idmail like '" + idmail + "%'";
    	       query += " AND   b.instancia = '" + instancia + "'";
	  		   query += " order by " + sortField ;
	  		   query += " LIMIT ?";
	           query += " OFFSET ?";
             break;
          }

        
        pstmt = con.prepareStatement(query);
        pstmt.setInt(1, pageSize);
        pstmt.setInt(2, first);
       // System.out.println(query);

         r =  pstmt.executeQuery();
        
        
        while (r.next()){
        	Maillista select = new Maillista();
            select.setIdgrupo(r.getString(1));
            select.setIdmail(r.getString(2));
            select.setMail(r.getString(3));
        	//Agrega la lista
        	list.add(select);
        }
        
        //Cierra las conecciones
        pstmt.close();
        con.close();
        r.close();
        
     } catch (SQLException | NamingException e){
         e.printStackTrace();    
     }
  	}
    
  	
  	/**
     * Leer registros en la tabla
     **/ 	
  	public void counter(Object filterValue )  {
  		
  		if(idgrupo==null){
   			idgrupo = " - ";
 		}
 		if(idgrupo.equals("")){
 			idgrupo = " - ";
 		}
   		
    	String[] vecidgrupo = idgrupo.split("\\ - ", -1);
    	

 	   String query = " SELECT trim(A.IDGRUPO), trim(B.IDMAIL), trim(B.MAIL)";
 	   query += " FROM MAILGRUPOS A, MAILLISTA B";
 	   query += " WHERE A.IDGRUPO=B.IDGRUPO";
 	   query += " and A.instancia=B.instancia";
 	   query += " and a.idgrupo||b.idmail||upper(b.mail) like '%" + ((String) filterValue).toUpperCase() + "%'";
 	   if(idgrupo != " - ") {
   	   query += " and  a.idgrupo = '" + vecidgrupo[0] + "'";	   
   	   }
 	   query += " and  b.idmail like '" + idmail + "%'";
 	   query += " AND   b.instancia = '" + instancia + "' order by ?";
        
        
       String  querypg = " SELECT A.IDGRUPO, trim(B.IDMAIL), trim(B.MAIL)";
       querypg += " FROM MAILGRUPOS A, MAILLISTA B";
       querypg += " WHERE A.IDGRUPO=B.IDGRUPO";
       querypg += " and A.instancia=B.instancia";
       querypg += " and cast(a.idgrupo as text)||b.idmail||upper(b.mail) like '%" + ((String) filterValue).toUpperCase() + "%'";
       if(idgrupo != " - ") {
       querypg += " and  cast(a.idgrupo as text) = '" + vecidgrupo[0] + "'";	   
       }
       querypg += " and  b.idmail like '" + idmail + "%'";
       querypg += " AND   b.instancia = '" + instancia + "'";
       querypg += " order by ?" ;
       
       consulta.selectGenericaMDB(query, querypg, JNDI);
       rows = consulta.getData().get(0).size();  

  	}
  	
  	public void reset(){
  		idgrupo = "";
  	}
    
	/**
     * Limpia los valores
     **/
	public void limpiarValores(){
		idmail = "";
		mail = "";
		validarOperacion=0;
	}
	

}
