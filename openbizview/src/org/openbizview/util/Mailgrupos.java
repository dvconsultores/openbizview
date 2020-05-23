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
public class Mailgrupos  extends Bd implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private LazyDataModel<Mailgrupos> lazyModel;  
	
	
	/**
	 * @return the lazyModel
	 */
	public LazyDataModel<Mailgrupos> getLazyModel() {
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
			if(segu.listMenu.get(x).opcMenu.equals("M22") && segu.listMenu.get(x).vistaMenu.equals("1")) {
				new LoginBean().logout();
			}
		});
		
		lazyModel  = new LazyDataModel<Mailgrupos>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 7217573531435419432L;
			
            @Override
			public List<Mailgrupos> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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

	private String idgrupo = "";
	private String desgrupo = "";
	private int validarOperacion = 0;//Param guardar para validar si guarda o actualiza
	private Object filterValue = "";
	private List<Mailgrupos> list = new ArrayList<Mailgrupos>();
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
	public void setLazyModel(LazyDataModel<Mailgrupos> lazyModel) {
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
	 * @return the desgrupo
	 */
	public String getDesgrupo() {
		return desgrupo;
	}

	/**
	 * @param desgrupo the desgrupo to set
	 */
	public void setDesgrupo(String desgrupo) {
		this.desgrupo = desgrupo;
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
	 * @return the list
	 */
	public List<Mailgrupos> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<Mailgrupos> list) {
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
     * Inserta Configuración.
     **/
    private void insert()  {
        try {
        	 Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);

     		con = ds.getConnection();
    		

            String query = "INSERT INTO MAILGRUPOS VALUES (?,?,?)";
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(idgrupo));
            pstmt.setString(2, desgrupo.toUpperCase());
            pstmt.setInt(3, Integer.parseInt(instancia));

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
     * Borra
     **/  
    public void delete()   {  

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

        	String query = "DELETE from MAILGRUPOS WHERE IDGRUPO in (" + param + ") and instancia = '" + instancia + "'";
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
    		

            String query = "UPDATE MAILGRUPOS";
            	   query += " SET DESGRUPO = ?";
            	   query += " where IDGRUPO = ? and instancia = '" + instancia + "'";


            //System.out.println(query);
            	   pstmt = con.prepareStatement(query);
                   pstmt.setString(1, desgrupo.toUpperCase());
                   pstmt.setInt(2, Integer.parseInt(idgrupo));

                //Avisando
                pstmt.executeUpdate();
                if(pstmt.getUpdateCount()==0){
                	msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("msnNoUpdate"), "");
                } else {
                msj = new FacesMessage(FacesMessage.SEVERITY_INFO,  getMessage("msnUpdate"), "");
                }
                desgrupo = "";
                list.clear();

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
  		
  		switch ( productName ) {
        case "Oracle":
        	   query += "  select * from ";
        	   query += " ( select query.*, rownum as rn from";
        	   query += " (SELECT trim(IDGRUPO), trim(DESGRUPO)";
        	   query += " FROM MAILGRUPOS";
        	   query += " WHERE idgrupo||desgrupo like '%" + ((String) filterValue).toUpperCase() + "%'";
        	   query += " and  idgrupo like '" + idgrupo + "%'";
        	   query += " AND   instancia = '" + instancia + "'";
	  		   query += " order by " + sortField + ") query";
	  		   query += " ) where rownum <= ?";
	           query += " and rn > (?)";
             break;
        case "PostgreSQL":
       	       query += " SELECT IDGRUPO, trim(DESGRUPO)";
    	       query += " FROM MAILGRUPOS";
    	       query += " WHERE idgrupo||desgrupo like '%" + ((String) filterValue).toUpperCase() + "%'";
    	       query += " and  cast(idgrupo as text) like '" + idgrupo + "%'";
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
        	Mailgrupos select = new Mailgrupos();
            select.setIdgrupo(r.getString(1));
            select.setDesgrupo(r.getString(2));
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

 	   String query = " SELECT trim(IDGRUPO), trim(DESGRUPO)";
 	   query += " FROM MAILGRUPOS";
 	   query += " WHERE idgrupo||desgrupo like '%" + ((String) filterValue).toUpperCase() + "%'";
 	   query += " and  idgrupo like '" + idgrupo + "%'";
 	   query += " AND   instancia = '" + instancia + "' order by ?";

        
        String querypg = " SELECT IDGRUPO, trim(DESGRUPO)";
        querypg += " FROM MAILGRUPOS";
        querypg += " WHERE idgrupo||desgrupo like '%" + ((String) filterValue).toUpperCase() + "%'";
        querypg += " and  cast(idgrupo as text) like '" + idgrupo + "%'";
        querypg += " AND   instancia = '" + instancia + "'";
        querypg += " order by ?";

        consulta.selectGenericaMDB(query, querypg, JNDI);
        rows = consulta.getData().get(0).size();  
  	}
    
    
	/**
     * Limpia los valores
     **/
	public void limpiarValores(){
		idgrupo = "";
		desgrupo = "";
		validarOperacion=0;
	}
}
