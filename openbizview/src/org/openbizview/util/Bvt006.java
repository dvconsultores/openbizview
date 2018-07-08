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
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

	/**
	 *
	 * @author Andres
	 */
	@ManagedBean
	@ViewScoped
	public class Bvt006 extends Bd implements Serializable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		
		private LazyDataModel<Bvt006> lazyModel;  
		
		/**
		 * @return the lazyModel
		 */
		public LazyDataModel<Bvt006> getLazyModel() {
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
			if(segu.listMenu.get(x).opcMenu.equals("M16") && segu.listMenu.get(x).vistaMenu.equals("1")) {
				RequestContext.getCurrentInstance().execute("PF('idleDialogNP').show()");
			}
		});

		lazyModel  = new LazyDataModel<Bvt006>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 7217573531435419432L;
			
            @Override
			public List<Bvt006> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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
	
	
	private String bcodrep = "";
	private String bdesrep = "";
	private String fecacc = "";
	private String bcoduser = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("bcoduser");
	private Object filterValue = "";
	private List<Bvt006> list = new ArrayList<Bvt006>();
	private String instancia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instancia"); //Usuario logeado
	SeguridadMenuBean segu = new SeguridadMenuBean();
	
		/**
	 * @return the bcodrep
	 */
	public String getBcodrep() {
		return bcodrep;
	}
	
	
	/**
	 * @param bcodrep the bcodrep to set
	 */
	public void setBcodrep(String bcodrep) {
		this.bcodrep = bcodrep;
	}
	
	
	/**
	 * @return the bdesrep
	 */
	public String getBdesrep() {
		return bdesrep;
	}
	
	
	/**
	 * @param bdesrep the bdesrep to set
	 */
	public void setBdesrep(String bdesrep) {
		this.bdesrep = bdesrep;
	}
	
	
	/**
	 * @return the bcoduser
	 */
	public String getBcoduser() {
		return bcoduser;
	}
	
	
	/**
	 * @param bcoduser the bcoduser to set
	 */
	public void setBcoduser(String bcoduser) {
		this.bcoduser = bcoduser;
	}
	
		
		
	/**
	 * @return the list
	 */
	public List<Bvt006> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<Bvt006> list) {
		this.list = list;
	}

	/**
	 * @return the fecacc
	 */
	public String getFecacc() {
		return fecacc;
	}

	/**
	 * @param fecacc the fecacc to set
	 */
	public void setFecacc(String fecacc) {
		this.fecacc = fecacc;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Variables seran utilizadas para capturar mensajes de errores de Oracle y parametros de metodos
	FacesMessage msj = null;
	PntGenerica consulta = new PntGenerica();
	private int rows; //Registros de tabla
	private String login = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario"); //Usuario logeado
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    //Coneccion a base de datos
    //Pool de conecciones JNDI
	Connection con;
	PreparedStatement pstmt = null;
	ResultSet r;



	
	
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
	
	        	String query = "DELETE from  Bvt006 WHERE b_codrep||b_coduser||to_char(fecacc,'dd/mm/yyyy hh:mi:ss') in (" + param + ") and instancia = '" + instancia + "'";
	        		        	
	            pstmt = con.prepareStatement(query);
	            //System.out.println(query);

	                pstmt.executeUpdate();
	                msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnDelete"), "");
	
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
     * Leer Datos de auditoria
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
 		
 		if(bcoduser==null){
			bcoduser = " - ";
		}
		if(bcoduser==""){
			bcoduser = " - ";
		}
		
		String[] vlcoduser = bcoduser.split("\\ - ", -1); 

  		switch ( productName ) {
        case "Oracle":
        	   query += "  select * from ";
        	   query += " ( select query.*, rownum as rn from";
        	   query += " (SELECT trim(B_CODREP), trim(B_DESREP), trim(B_CODUSER), to_char(FECACC, 'dd/mm/yyyy hh:mi:ss')";
        	   query += " FROM bvt006";
        	   query += " WHERE B_CODUSER LIKE '" + vlcoduser[0] + "%'";
        	   query += " and b_codrep||b_desrep like '%" + ((String) filterValue).toUpperCase() + "%'";
        	   query += " AND   instancia = '" + instancia + "'";
	  		   query += " order by  B_CODUSER, FECACC desc) query";
	  		   query += " ) where rownum <= ?";
	           query += " and rn > (?)";
             break;
        case "PostgreSQL":
        	   query += " SELECT trim(B_CODREP), trim(B_DESREP), trim(B_CODUSER), to_char(FECACC, 'dd/mm/yyyy hh:mi:ss') ";
        	   query += " FROM BVT006";
        	   query += " WHERE B_CODUSER LIKE '" + vlcoduser[0] + "%'";
        	   query += " and b_codrep||b_desrep like '%" + ((String) filterValue).toUpperCase() + "%'";
        	   query += " AND   instancia = '" + instancia + "'";
        	   query += " order by  B_CODUSER, FECACC desc";
        	   query += " LIMIT ?";
	           query += " OFFSET ?";
             break;
          }

 		 		

        
        pstmt = con.prepareStatement(query);
        pstmt.setInt(1, pageSize);
        pstmt.setInt(2, first);
        ////System.out.println(query);
  		
        r =  pstmt.executeQuery();

        
        while (r.next()){
        Bvt006 select = new Bvt006();
        select.setBcodrep(r.getString(1));
        select.setBdesrep(r.getString(2));
        select.setBcoduser(r.getString(3));
        select.setFecacc(r.getString(4));
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

 		
 		if(bcoduser==null){
			bcoduser = " - ";
		}
		if(bcoduser==""){
			bcoduser = " - ";
		}
		
		String[] vlcoduser = bcoduser.split("\\ - ", -1); 
		

 	   String query = " SELECT trim(B_CODREP), trim(B_DESREP), trim(B_CODUSER), to_char(FECACC, 'dd/mm/yyyy hh:mi:ss')";
 	   query += " FROM bvt006";
 	   query += " WHERE B_CODUSER LIKE '" + vlcoduser[0] + "%'";
 	   query += " and b_codrep||b_desrep like '%" + ((String) filterValue).toUpperCase() + "%'";
 	   query += " AND   instancia = '" + instancia + "' order by ?";
        
        
       String querypg = " SELECT trim(B_CODREP), trim(B_DESREP), trim(B_CODUSER), to_char(FECACC, 'dd/mm/yyyy hh:mi:ss') ";
       querypg += " FROM BVT006";
       querypg += " WHERE B_CODUSER LIKE '" + vlcoduser[0] + "%'";
       querypg += " and b_codrep||b_desrep like '%" + ((String) filterValue).toUpperCase() + "%'";
       querypg += " AND   instancia = '" + instancia + "' order by ?";
  		
        consulta.selectGenericaMDB(query, querypg, JNDI);
        rows = consulta.getData().get(0).size();       

  	}

  	public void reset() {
  		bcoduser = null;     
    }
 	
	
	/**
	 * @return Retorna número de filas
	 **/
	public int getRows(){
		return rows;
	}


}
