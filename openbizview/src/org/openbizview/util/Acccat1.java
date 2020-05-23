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


import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

	/**
	 *
	 * @author Andres
	 */
	@ManagedBean
	@ViewScoped
	public class Acccat1 extends Bd implements Serializable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		
		private LazyDataModel<Acccat1> lazyModel;  
		
		
	
	/**
		 * @return the lazyModel
		 */
		public LazyDataModel<Acccat1> getLazyModel() {
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
			if(segu.listMenu.get(x).opcMenu.equals("M17") && segu.listMenu.get(x).vistaMenu.equals("1")) {
				new LoginBean().logout();
			}
		});

		lazyModel  = new LazyDataModel<Acccat1>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 7217573531435419432L;
			
            @Override
			public List<Acccat1> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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
		//selectAcccat1();
      } //Fin valida nulo
	}

	
	private String b_codrol = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("segrol");
	private String desrol = "";
	private String b_codcat1 = "";
	private String b_codcat1acc = "";
	private String descat1 = "";
	private Object filterValue = "";
	private List<Acccat1> list = new ArrayList<Acccat1>();
 
	@SuppressWarnings("unchecked")
	private List<Acccat1> listaccesoacccat1 = (List<Acccat1>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("listaccesoacccat1");
    private String exito = "exito";
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
	public List<Acccat1> getList() {
		return list;
	}


	/**
	 * @param list the list to set
	 */
	public void setList(List<Acccat1> list) {
		this.list = list;
	}


	/**
	 * @param rows the rows to set
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}
	

	/**
	 * @return the listaccesoacccat1
	 */
	public List<Acccat1> getListaccesoacccat1() {
		return listaccesoacccat1;
	}


	/**
	 * @param listaccesoacccat1 the listaccesoacccat1 to set
	 */
	public void setListaccesoacccat1(List<Acccat1> listaccesoacccat1) {
		this.listaccesoacccat1 = listaccesoacccat1;
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listaccesoacccat1", listaccesoacccat1);
	}
	
	


	/**
	 * @return the b_codcat1acc
	 */
	public String getB_codcat1acc() {
		return b_codcat1acc;
	}


	/**
	 * @param b_codcat1acc the b_codcat1acc to set
	 */
	public void setB_codcat1acc(String b_codcat1acc) {
		this.b_codcat1acc = b_codcat1acc;
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
    private void insert(String pcat1)  {
    		String[] veccodrol = b_codrol.split("\\ - ", -1);
    	if(veriInsert(pcat1, veccodrol[0].toUpperCase())) {	
        try {
        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);
            con = ds.getConnection();
            
            //Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
     		DatabaseMetaData databaseMetaData = con.getMetaData();
     		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
            
            String query = "INSERT INTO acccat1 VALUES (?,?,?," + getFecha(productName) + ",?," + getFecha(productName) + ",?)";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, veccodrol[0].toUpperCase());
            pstmt.setString(2, pcat1.toUpperCase());
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
		  
		 listaccesoacccat1 = new ArrayList<Acccat1>();
		 selectAcccat1();
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
     * Elimina el acceso a reportes.
     * <p>
     * Parámetros del Mátodo: String rol
     **/
    public void delete(String pcodcat1)  { 
    	String[] veccodrol = b_codrol.split("\\ - ", -1);
    	if(veriDelete(pcodcat1, veccodrol[0].toUpperCase())) {
	        try {
	        	
	        	Context initContext = new InitialContext();     
	     		DataSource ds = (DataSource) initContext.lookup(JNDI);

	     		con = ds.getConnection();		

	        	String query = "DELETE from acccat1 WHERE b_codrol = '" + veccodrol[0].toUpperCase() + "' and instancia = '" + instancia + "' and b_codcat1 ='" + pcodcat1 + "'";
	        		        	
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


 		switch ( productName ) {
        case "Oracle":
        	   query += "  select * from ";
        	   query += " ( select query.*, rownum as rn from";
        	   query += " (SELECT trim(codcat1), trim(descat1)";
        	   query += " FROM BVTcat1";
        	   query += " WHERE codcat1||descat1 like '%" + ((String) filterValue).toUpperCase() + "%'";
        	   query += " and codcat1 like '%" + b_codcat1.toUpperCase() + "%'";
        	   query += " AND   instancia = '" + instancia + "'";
	  		   query += " order by " + sortField + ") query";
	  		   query += " ) where rownum <= ?";
	           query += " and rn > (?)";
             break;
        case "PostgreSQL":
        	   query += " SELECT trim(codcat1), trim(descat1) ";
        	   query += " FROM BVTcat1";
        	   query += " WHERE codcat1||descat1 like '%" + ((String) filterValue).toUpperCase() + "%'";
        	   query += " and codcat1 like '%" + b_codcat1.toUpperCase() + "%'";
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
        	Acccat1 select = new Acccat1();
     	    select.setB_codcat1(r.getString(1));
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
     * @throws NamingException 
     * @throws IOException 
     **/ 	
  	public void counter(Object filterValue )  {


 		String query = " SELECT trim(codcat1), trim(descat1)";
  	    query += " FROM BVTcat1";
  	    query += " WHERE codcat1||descat1 like '%" + ((String) filterValue).toUpperCase() + "%'";
  	    query += " and codcat1 like '%" + b_codcat1.toUpperCase() + "%'";
  	    query += " AND   instancia = '" + instancia + "' order by ?";
         
         
        String querypg = " SELECT trim(codcat1), trim(descat1) ";
        querypg += " FROM BVTcat1";
        querypg += " WHERE codcat1||descat1 like '%" + ((String) filterValue).toUpperCase() + "%'";
        querypg += " and codcat1 like '%" + b_codcat1.toUpperCase() + "%'";
        querypg += " AND   instancia = '" + instancia + "' order by ?";


 		
        consulta.selectGenericaMDB(query, querypg, JNDI);
        rows = consulta.getData().get(0).size();
  	}

  	
  	
  	/**
     * Leer Datos de nominas para asignar a menucheck
     **/ 	
  	private void selectAcccat1()  {
  		if(listaccesoacccat1 == null) {
  			listaccesoacccat1 = new ArrayList<Acccat1>();
  		}
  		
  		if(listaccesoacccat1.size()==0 && b_codrol != null) {
  			
  	
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
	    	query = "Select trim(b_codcat1)";
	        query += " from ACCCAT1";
	        query += " where b_codrol = '" + veccodrol[0] + "'";
	        query += " and   instancia = '" + instancia + "'";
	        query += " order by b_codcat1";
	        break;
  		case "PostgreSQL":
	    	query = "Select trim(b_codcat1)";
	        query += " from ACCCAT1";
	        query += " where b_codrol = '" + veccodrol[0] + "'";
	        query += " and   instancia = '" + instancia + "'";
	        query += " order by b_codcat1";
	        break;
	        }


        ////System.out.println(query);

        
        pstmt = con.prepareStatement(query);
        ////System.out.println(query);
  		
        r =  pstmt.executeQuery();
        
  		
        
        while (r.next()){
        	Acccat1 select = new Acccat1();
     	    select.setB_codcat1acc(r.getString(1));
        	//Agrega la lista
        	listaccesoacccat1.add(select);
        }
        setListaccesoacccat1(listaccesoacccat1);
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
   public String isAcccat1(String pcodcat1){
 	String valor = "fa fa-circle fa-2x text-danger";
 	Acccat1 result = null;
 	//carga lista de roles
 	if(listaccesoacccat1==null) {
 		listaccesoacccat1 = new ArrayList<Acccat1>();
 	}
  		
 	result = listaccesoacccat1.stream().parallel()           //Recorre lista             
             .filter(x -> pcodcat1.equals(x.getB_codcat1acc()))       //Compara con parametro
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
	private Boolean veriInsert(String pcodcat1, String pcodrol) {
		Boolean retorno = true;
		String query = "select b_codcat1 from acccat1 where b_codcat1 = '" + pcodcat1 + "' and b_codrol = '" + pcodrol + "' and instancia = '"+ instancia + "' order by ?";
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
	private Boolean veriDelete(String pcodcat1, String pcodrol) {
		Boolean retorno = true;
		String query = "select b_codcat1 from acccat1 where b_codcat1 = '" + pcodcat1 + "' and b_codrol = '" + pcodrol + "' and instancia = '"+ instancia + "' order by ?";
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
