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


import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;


	/**
	 *
	 * @author Andres
	 */
	@ManagedBean
	@ViewScoped
	public class Bvt007 extends Bd implements Serializable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		
		private LazyDataModel<Bvt007> lazyModel;  
		
		
		/**
		 * @return the lazyModel
		 */
		public LazyDataModel<Bvt007> getLazyModel() {
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
			if(segu.listMenu.get(x).opcMenu.equals("M15") && segu.listMenu.get(x).vistaMenu.equals("1")) {
				RequestContext.getCurrentInstance().execute("PF('idleDialogNP').show()");
			}
		});
		
		lazyModel  = new LazyDataModel<Bvt007>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 7217573531435419432L;
			
            @Override
			public List<Bvt007> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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
		//selectReportes();
	     }
	}
	
	private String instancia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instancia"); //Usuario logeado
	private String b_codrol = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("segrol");
	private String b_codrep = "";
	private String b_codrepacc = "";
	private String desrol = "";
	private String desrep;
	private List<Bvt007> list = new ArrayList<Bvt007>();
	@SuppressWarnings("unchecked")
	private List<Bvt007> listaccesoreportes = (List<Bvt007>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("listaccesoreportes");
	private Object filterValue = "";
	SeguridadMenuBean segu = new SeguridadMenuBean();
	private int rows;
	private String exito = "exito";

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
	 * @return the b_codrep
	 */
	public String getB_codrep() {
		return b_codrep;
	}

	/**
	 * @param b_codrep the b_codrep to set
	 */
	public void setB_codrep(String b_codrep) {
		this.b_codrep = b_codrep;
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
	 * @return the desrep
	 */
	public String getDesrep() {
		return desrep;
	}

	/**
	 * @param desrep the desrep to set
	 */
	public void setDesrep(String desrep) {
		this.desrep = desrep;
	}

 
	/**
	 * @return the listaccesoreportes
	 */
	public List<Bvt007> getListaccesoreportes() {
		return listaccesoreportes;
	}

	/**
	 * @param listaccesoreportes the listaccesoreportes to set
	 */
	public void setListaccesoreportes(List<Bvt007> listaccesoreportes) {
		this.listaccesoreportes = listaccesoreportes;
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listaccesoreportes", listaccesoreportes);
	}
	
	

		/**
	 * @return the b_codrepacc
	 */
	public String getB_codrepacc() {
		return b_codrepacc;
	}

	/**
	 * @param b_codrepacc the b_codrepacc to set
	 */
	public void setB_codrepacc(String b_codrepacc) {
		this.b_codrepacc = b_codrepacc;
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
		FacesMessage msj = null;
		private String login = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario"); //Usuario logeado
		PntGenerica consulta = new PntGenerica();
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
		//Coneccion a base de datos
		//Pool de conecciones JNDI
			Connection con;
			PreparedStatement pstmt = null;
			ResultSet r;

	/**
     * Inserta acceso reportes.
     **/
    private void insert(String pcodrep) {
    	//Valida que los campos no sean nulos
       
      	String[] veccodrol = b_codrol.split("\\ - ", -1);
      	if(veriInsert(pcodrep, veccodrol[0].toUpperCase())) {
        try {
        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);
            con = ds.getConnection();
            
            //Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
     		DatabaseMetaData databaseMetaData = con.getMetaData();
     		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
            
            String query = "INSERT INTO Bvt007 VALUES (?,?,?," + getFecha(productName) + ",?," + getFecha(productName) + ",?)";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, veccodrol[0].toUpperCase());
            pstmt.setString(2, pcodrep.toUpperCase());
            pstmt.setString(3, login);
            pstmt.setString(4, login);
            pstmt.setInt(5, Integer.parseInt(instancia));
            ////System.out.println(query);

            	pstmt.executeUpdate();
            	//msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnInsert"), "");
            	
            pstmt.close();
            con.close();
            
        } catch (Exception e) {
        	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
        	FacesContext.getCurrentInstance().addMessage(null, msj);
        	
        }	  
      	}
    }

    
    /**
     * Elimina el acceso a reportes.
     * <p>
     * Parámetros del Mátodo: String rol
     **/
    public void delete(String pcodrep)  { 
    	String[] veccodrol = b_codrol.split("\\ - ", -1);
    	if(veriDelete(pcodrep, veccodrol[0].toUpperCase())) {
	        try {
	        	
	        	Context initContext = new InitialContext();     
	     		DataSource ds = (DataSource) initContext.lookup(JNDI);

	     		con = ds.getConnection();		

	        	String query = "DELETE from Bvt007 WHERE b_codrol = '" + veccodrol[0].toUpperCase() + "' and instancia = '" + instancia + "' and b_codrep ='" + pcodrep + "'";
	        		        	
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
    	}
    	}
    }
    
    
    
    
    /**
     * Leer Datos de categoria2
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
        	   query += " (SELECT  trim(c.codrep), trim(c.desrep)";
        	   query += " FROM  bvt001 c";;
        	   query += " where c.codrep||c.desrep like '%" + ((String) filterValue).toUpperCase() + "%'";
        	   query += " AND   c.instancia = '" + instancia + "'";
	  		   query += " order by c.codrep) query";
	  		   query += " ) where rownum <= ?";
	           query += " and rn > (?)";
             break;
        case "PostgreSQL":
        	   query += " SELECT trim(c.codrep), trim(c.desrep)";
        	   query += " FROM bvt001 c";;
        	   query += " where c.codrep||c.desrep like '%" + ((String) filterValue).toUpperCase() + "%'";
        	   query += " AND  c.instancia = '" + instancia + "'";
	  		   query += " order by c.codrep" ;
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
        	Bvt007 select = new Bvt007();
     	    select.setB_codrep(r.getString(1));
     	    select.setDesrep(r.getString(2));
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

  		
 	   String query = " SELECT trim(c.codrep), trim(c.desrep)";
 	   query += " FROM bvt001 c";
 	   query += " where c.codrep||c.desrep like '%" + ((String) filterValue).toUpperCase() + "%'";
 	   query += " AND   c.instancia = '" + instancia + "' order by ?";

        
        
       String querypg = " SELECT  trim(c.codrep), trim(c.desrep)";
       querypg += " FROM bvt001 c";
       querypg += " where c.codrep||c.desrep like '%" + ((String) filterValue).toUpperCase() + "%'";
       querypg += " AND  c.instancia = '" + instancia + "' order by ?";
    
        consulta.selectGenericaMDB(query, querypg, JNDI);
        rows = consulta.getData().get(0).size();   

  	}
  	
  	
  	/**
     * Leer Datos de acceso a reportes
     **/ 	
  	public void selectReportes()  {
  		
  		if(listaccesoreportes == null) {
  			listaccesoreportes = new ArrayList<Bvt007>();
  		}
  		
  		if(listaccesoreportes.size()==0 && b_codrol != null) {
  		
  		Context initContext;
		try {
			initContext = new InitialContext();
		   
 		DataSource ds = (DataSource) initContext.lookup(JNDI);
 		con = ds.getConnection();	
 		
 		//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
 		DatabaseMetaData databaseMetaData = con.getMetaData();
 		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
 		
 		
 		String query = "";
 		//System.out.println("b_codrol = "+b_codrol);

 		String[] veccodrol = b_codrol.split("\\ - ", -1);  	

  		switch ( productName ) {
        case "Oracle":
        	   query += " SELECT trim(b_codrep)";
        	   query += " FROM Bvt007";
        	   query += " where b_codrol = '" + veccodrol[0] + "'";
        	   query += " AND   instancia = '" + instancia + "'";
	  		   query += " order by ?";
             break;
        case "PostgreSQL":
        	   query += " SELECT trim(b_codrep)";
     	       query += " FROM Bvt007";
     	       query += " where b_codrol = '" + veccodrol[0] + "'";
     	       query += " AND   instancia = '" + instancia + "'";
	  		   query += " order by ?";
             break;
  		}
 		
        
        pstmt = con.prepareStatement(query);
        pstmt.setInt(1, 1);
        //System.out.println(query);
  		
        r =  pstmt.executeQuery();

        while (r.next()){
        	Bvt007 select = new Bvt007();
     	    select.setB_codrepacc(r.getString(1));
        	//Agrega la lista
        	listaccesoreportes.add(select);
        }
        setListaccesoreportes(listaccesoreportes);
        //Cierra las conecciones
        pstmt.close();
        con.close();
        
        
		} catch (NamingException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
  		}
  	}
  	
  	/*
     * Actualizar acceso a roles
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
    	} else 	if(paramAcc!=null && paramDacc!=null){
    		msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("bvtmenuUp1"), "");
    		FacesContext.getCurrentInstance().addMessage(null, msj);
    		exito = "error";
    	} else 	if(paramAcc!=null && paramDacc==null && paramAcc.length>0){
             //Recorrido del calculo
    		IntStream.range(0, paramAcc.length).forEach(i -> {
    		insert(paramAcc[i]);
    	});
    		//listaccesoreportes.clear();
            updatetbfilter(veccodrol[0].toUpperCase());
    		
    	} else if (paramAcc==null && paramDacc!=null && paramDacc.length>0){
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
	  * Filtrado de table
	  */
	  public void updatetbfilter(String pcodrol) {
		 setRol(pcodrol); 
		  
		 listaccesoreportes = new ArrayList<Bvt007>();
		 selectReportes();
	  }
	   
	  
	  /**
	  	 * Retorna si el usuario tiene asignado algún reporte
	  	 */
	 public String isRep(String pcodrep){
	 	String valor = "fa fa-circle fa-2x text-danger";
	 	Bvt007 result = null;
	 	//carga lista de roles
	 	if(listaccesoreportes==null) {
	 		listaccesoreportes = new ArrayList<Bvt007>();
	 	}
	  		
	 	result = listaccesoreportes.stream().parallel()           //Recorre lista             
	             .filter(x -> pcodrep.equals(x.getB_codrepacc()))       //Compara con parametro
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
	private Boolean veriInsert(String pcodrep, String pcodrol) {
		Boolean retorno = true;
		String query = "select b_codrep from bvt007 where b_codrep = '" + pcodrep + "' and b_codrol = '" + pcodrol + "' and instancia = '"+ instancia + "' order by ?";
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
	private Boolean veriDelete(String pcodrep, String pcodrol) {
		Boolean retorno = true;
		String query = "select b_codrep from bvt007 where b_codrep = '" + pcodrep + "' and b_codrol = '" + pcodrol + "' and instancia = '"+ instancia + "' order by ?";
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
