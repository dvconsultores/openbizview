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
	public class Bvt010 extends Bd implements Serializable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		
		private LazyDataModel<Bvt010> lazyModel;  
		
		
	
	/**
		 * @return the lazyModel
		 */
		public LazyDataModel<Bvt010> getLazyModel() {
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
			if(segu.listMenu.get(x).opcMenu.equals("R03") && segu.listMenu.get(x).vistaMenu.equals("1")) {
				new LoginBean().logout();
			}
		});

		lazyModel  = new LazyDataModel<Bvt010>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 7217573531435419432L;
			
            @Override
			public List<Bvt010> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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
		selectPivotAcces();
		//selectBvt010();
      } //Fin valida nulo
	}

	
	private String b_codrol = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("segrol");
	private String nombre_cubo = "";
	private String nombre_cuboacc = "";
	private Object filterValue = "";
	private List<Bvt010> list = new ArrayList<Bvt010>();
 
	@SuppressWarnings("unchecked")
	private List<Bvt010> listaccesoBvt010 = (List<Bvt010>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("listaccesoBvt010");
    private String exito = "exito";
    private String rol = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("rol"); //Usuario logeado
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
	 * @return the nombre_cubo
	 */
	public String getNombre_cubo() {
		return nombre_cubo;
	}


	/**
	 * @param nombre_cubo the nombre_cubo to set
	 */
	public void setNombre_cubo(String nombre_cubo) {
		this.nombre_cubo = nombre_cubo;
	}


	/**
	 * @return the nombre_cuboacc
	 */
	public String getNombre_cuboacc() {
		return nombre_cuboacc;
	}


	/**
	 * @param nombre_cuboacc the nombre_cuboacc to set
	 */
	public void setNombre_cuboacc(String nombre_cuboacc) {
		this.nombre_cuboacc = nombre_cuboacc;
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
	public List<Bvt010> getList() {
		return list;
	}


	/**
	 * @param list the list to set
	 */
	public void setList(List<Bvt010> list) {
		this.list = list;
	}


	/**
	 * @param rows the rows to set
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}
	

	/**
	 * @return the listaccesoBvt010
	 */
	public List<Bvt010> getListaccesoBvt010() {
		return listaccesoBvt010;
	}


	/**
	 * @param listaccesoBvt010 the listaccesoBvt010 to set
	 */
	public void setListaccesoBvt010(List<Bvt010> listaccesoBvt010) {
		this.listaccesoBvt010 = listaccesoBvt010;
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listaccesoBvt010", listaccesoBvt010);
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
    protected void insert(String pcubo)  {
    		String[] veccodrol = b_codrol.split("\\ - ", -1);
    	if(veriInsert(pcubo, veccodrol[0].toUpperCase())) {	
        try {
        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);
            con = ds.getConnection();
            
            //Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
     		DatabaseMetaData databaseMetaData = con.getMetaData();
     		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
            
            String query = "INSERT INTO Bvt010 VALUES (?,?,?," + getFecha(productName) + ",?," + getFecha(productName) + ",?)";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, veccodrol[0].toUpperCase());
            pstmt.setString(2, pcubo.trim());
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
     * Inserta acceso categoria1.
     * <p>
     * <b>Parametros del Metodo:<b> String rol, String cat1 unidos como un solo string.<br>
     * String pool, String login.<br><br>
     **/
    protected void insertFromBvt009(String pcubo, String prol)  {
    		String[] veccodrol = rol.split("\\ - ", -1);
    	if(veriInsert(pcubo, veccodrol[0].toUpperCase())) {	
        try {
        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);
            con = ds.getConnection();
            
            //Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
     		DatabaseMetaData databaseMetaData = con.getMetaData();
     		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
            
            String query = "INSERT INTO Bvt010 VALUES (?,?,?," + getFecha(productName) + ",?," + getFecha(productName) + ",?)";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, veccodrol[0].toUpperCase());
            pstmt.setString(2, pcubo.trim());
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
		  
		 listaccesoBvt010 = new ArrayList<Bvt010>();
		 selectBvt010();
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
    	} else  if(paramAcc!=null && paramDacc!=null){
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
    public void delete(String pcubo)  { 
    	String[] veccodrol = b_codrol.split("\\ - ", -1);
    	if(veriDelete(pcubo, veccodrol[0].toUpperCase())) {
	        try {
	        	
	        	Context initContext = new InitialContext();     
	     		DataSource ds = (DataSource) initContext.lookup(JNDI);

	     		con = ds.getConnection();		

	        	String query = "DELETE from Bvt010 WHERE b_codrol = '" + veccodrol[0].toUpperCase() + "' and instancia = '" + instancia + "' and NOMBRE_CUBO ='" + pcubo + "'";
	        		        	
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
        	   query += " (SELECT trim(NOMBRE)";
        	   query += " FROM BVT009";
        	   query += " WHERE NOMBRE like '%" + ((String) filterValue) + "%'";
        	   query += " AND   instancia = '" + instancia + "'";
	  		   query += " order by " + sortField + ") query";
	  		   query += " ) where rownum <= ?";
	           query += " and rn > (?)";
             break;
        case "PostgreSQL":
        	   query += " SELECT trim(NOMBRE) ";
        	   query += " FROM BVT009";
        	   query += " WHERE NOMBRE like '%" + ((String) filterValue) + "%'";
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
        	Bvt010 select = new Bvt010();
     	    select.setNombre_cubo(r.getString(1));

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


 		String query = " SELECT trim(NOMBRE)";
  	    query += " FROM BVT009";
  	    query += " WHERE NOMBRE like '%" + ((String) filterValue) + "%'";
  	    query += " AND   instancia = '" + instancia + "' order by ?";
         
         
        String querypg = " SELECT trim(NOMBRE) ";
        querypg += " FROM BVT009";
        querypg += " WHERE NOMBRE like '%" + ((String) filterValue) + "%'";
        querypg += " AND   instancia = '" + instancia + "' order by ?";


 		
        consulta.selectGenericaMDB(query, querypg, JNDI);
        rows = consulta.getData().get(0).size();
  	}

  	
  	
  	/**
     * Leer Datos de nominas para asignar a menucheck
     **/ 	
  	private void selectBvt010()  {
  		if(listaccesoBvt010 == null) {
  			listaccesoBvt010 = new ArrayList<Bvt010>();
  		}
  		
  		if(listaccesoBvt010.size()==0 && b_codrol != null) {
  			
  	
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
	    	query = "Select trim(NOMBRE_CUBO)";
	        query += " from Bvt010";
	        query += " where b_codrol = '" + veccodrol[0] + "'";
	        query += " and   instancia = '" + instancia + "'";
	        query += " order by NOMBRE_CUBO";
	        break;
  		case "PostgreSQL":
	    	query = "Select trim(NOMBRE_CUBO)";
	    	query += " from Bvt010";
	        query += " where b_codrol = '" + veccodrol[0] + "'";
	        query += " and   instancia = '" + instancia + "'";
	        query += " order by NOMBRE_CUBO";
	        break;
	        }


        ////System.out.println(query);

        
        pstmt = con.prepareStatement(query);
        ////System.out.println(query);
  		
        r =  pstmt.executeQuery();
        
  		
        
        while (r.next()){
        	Bvt010 select = new Bvt010();
     	    select.setNombre_cuboacc(r.getString(1));
        	//Agrega la lista
        	listaccesoBvt010.add(select);
        }
        setListaccesoBvt010(listaccesoBvt010);
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
   public String isBvt010(String pcubo){
 	String valor = "fa fa-circle fa-2x text-danger";
 	Bvt010 result = null;
 	//carga lista de roles
 	if(listaccesoBvt010==null) {
 		listaccesoBvt010 = new ArrayList<Bvt010>();
 	}
  		
 	result = listaccesoBvt010.stream().parallel()           //Recorre lista             
             .filter(x -> pcubo.equals(x.getNombre_cuboacc()))       //Compara con parametro
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
	private Boolean veriInsert(String pcubo, String pcodrol) {
		Boolean retorno = true;
		String query = "select NOMBRE_CUBO from Bvt010 where NOMBRE_CUBO = '" + pcubo + "' and b_codrol = '" + pcodrol + "' and instancia = '"+ instancia + "' order by ?";
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
	private Boolean veriDelete(String pcubo, String pcodrol) {
		Boolean retorno = true;
		String query = "select NOMBRE_CUBO from Bvt010 where NOMBRE_CUBO = '" + pcubo + "' and b_codrol = '" + pcodrol + "' and instancia = '"+ instancia + "' order by ?";
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
  	
  	////////////////////////////////////////////////////////////////////////////////////////////////////
  	////////////////////////////////////////Catalog list for security pivot////////////////////////////
  	/////////////////////////////Used in Catalog.xhtml////////////////////////////////////////////////
  	///////////////////////////////////////////////////////////////////////////////////////////////////
  	
  	
  	private String label, value, description;
  	
  	
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}


	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}


	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}


	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	
	


	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}


	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}


	/**
 	 * Leer acceso a cubos bvt010
 	 *
     **/ 	
  	public List<Bvt010> selectPivotAcces()  {
        List<Bvt010> lista = new ArrayList<Bvt010>();
  		try {
  		Context initContext = new InitialContext();     
  		DataSource ds = (DataSource) initContext.lookup(JNDI);
 		con = ds.getConnection();
		
    	      		
  		String query = "";

	    	query = "Select trim(NOMBRE_CUBO),TRIM(NOMBRE_CUBO)";
	        query += " from bvt010";
	        query += " where b_codrol = ? and instancia = ?";
	        query += " order by 1";
        
        pstmt = con.prepareStatement(query);
        pstmt.setString(1, rol);
        pstmt.setInt(2, Integer.parseInt(instancia));
        ////System.out.println(query);
  		
        r =  pstmt.executeQuery();
        
        while (r.next()){
        	Bvt010 select = new Bvt010();
        	select.setLabel(r.getString(2));
        	select.setValue(r.getString(1));
        	lista.add(select);
        }
        //Cierra las conecciones
        pstmt.close();
        con.close();
        } catch (SQLException | NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  return lista;
  	}

}
