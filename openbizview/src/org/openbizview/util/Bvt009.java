/*
o *  Copyright (C) 2011 - 2016  DVCONSULTORES

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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


import org.jdom2.Attribute;
import org.jdom2.Comment;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;


import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
	/**
	 *
	 * @author Andres
	 */

	@ManagedBean
	@ViewScoped
	public class Bvt009 extends Bd implements Serializable {
	
	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private LazyDataModel<Bvt009> lazyModel;  
		
		
		/**
		 * @return the lazyModel
		 */
		public LazyDataModel<Bvt009> getLazyModel() {
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

		
		lazyModel  = new LazyDataModel<Bvt009>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 7217573531435419432L;
			
            @Override
			public List<Bvt009> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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
	
	//Declaración de variables, getters y setters
	
    private String nombre = "";
    private String descripcion = "";
    private String url = "";
    private String driver = "";
    private String usuario = "";
    private String clave = "";
    private String xml = "";
	private String instancia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instancia"); //Usuario logeado
	private int rows;
	private Object filterValue = "";
	private List<Bvt009> list = new ArrayList<Bvt009>();
	private String vinstancia = ""; //Istancia para el log
	SeguridadMenuBean segu = new SeguridadMenuBean();
	ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
	
	


	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}
	
	
	


	/**
	 * @return the rol
	 */
	public String getRol() {
		return rol;
	}


	/**
	 * @return the list
	 */
	public List<Bvt009> getList() {
		return list;
	}
	
	
	/**
	 * @param list the list to set
	 */
	public void setList(List<Bvt009> list) {
		this.list = list;
	}
	
	
	
	
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
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
	 * @return the driver
	 */
	public String getDriver() {
		return driver;
	}

	/**
	 * @param driver the driver to set
	 */
	public void setDriver(String driver) {
		this.driver = driver;
	}

	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the clave
	 */
	public String getClave() {
		return clave;
	}

	/**
	 * @param clave the clave to set
	 */
	public void setClave(String clave) {
		this.clave = clave;
	}

	/**
	 * @return the xml
	 */
	public String getXml() {
		return xml;
	}

	/**
	 * @param xml the xml to set
	 */
	public void setXml(String xml) {
		this.xml = xml;
	}

	/**
	 * @return the vinstancia
	 */
	public String getVinstancia() {
		return vinstancia;
	}

	/**
	 * @param vinstancia the vinstancia to set
	 */
	public void setVinstancia(String vinstancia) {
		this.vinstancia = vinstancia;
	}

	

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

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Variables seran utilizadas para capturar mensajes de errores de Oracle y parametros de metodos
	PntGenerica consulta = new PntGenerica();
	boolean vGacc; //Validador de opciones del menú
	private String login = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario"); //Usuario logeado
	private String rol = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("rol"); //Usuario logeado
	FacesMessage msj =  null;
	
	
	
	
	//Coneccion a base de datos
	//Pool de conecciones JNDI
	//Coneccion a base de datos
	//Pool de conecciones JNDIFARM
	Connection con;
	PreparedStatement pstmt = null;
	PreparedStatement pstmt1 = null;
	ResultSet r;

	
	private int validarOperacion=0;
	
	
	public int getValidarOperacion() {
		return validarOperacion;
	}
	
	
	public void setValidarOperacion(int validarOperacion) {
		this.validarOperacion = validarOperacion;
	}


	/**
     * Inserta reportes.
     * <p>
     **/
    private void insert() {
    	//Valida que los campos no sean nulos
        try {
        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);
     		//System.out.println("Grupo: " + veccodgrup[0].toUpperCase());
     		con = ds.getConnection();		
           
       		//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
     		DatabaseMetaData databaseMetaData = con.getMetaData();
     		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
     		
            String query = "INSERT INTO Bvt009 VALUES (?,?,?,?,?,?,?,?," + getFecha(productName) + ",?," + getFecha(productName) + ",?)";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, nombre.trim());
            pstmt.setString(2, descripcion);
            pstmt.setString(3, url);
            pstmt.setString(4, driver);
            pstmt.setString(5, usuario);
            pstmt.setString(6, clave);
            pstmt.setString(7, xml.replace(".xml", ""));
            pstmt.setString(8, login);
            pstmt.setString(9, login);
            pstmt.setInt(10, Integer.parseInt(instancia));
            ////System.out.println(query);
       
                //Avisando
                pstmt.executeUpdate();
                msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnInsert"), "");
                FacesContext.getCurrentInstance().addMessage(null, msj);
            
            
            pstmt.close();
            con.close();
            new Bvt010().insertFromBvt009(nombre.trim(), rol);
            limpiarValores();
            //Genera xml de configuración
            writePivotConfig();

    	} catch (Exception e){
    		e.printStackTrace();
    		msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
        	FacesContext.getCurrentInstance().addMessage(null, msj);
    		
    	}
        
    }
    
    /**
	
    
    
 
    /**
     * Eliminar reportes   
     */
    public void delete()  {  
	        try {
	       	
	        	Context initContext = new InitialContext();     
	     		DataSource ds = (DataSource) initContext.lookup(JNDI);

	     		con = ds.getConnection();		
	        	
	
	        	String query1 = "DELETE from Bvt010 WHERE NOMBRE_CUBO = '" + nombre + "' and instancia = '" + instancia + "'";
	        	String query2 = "DELETE from Bvt009 WHERE NOMBRE = '" + nombre + "' and instancia = '" + instancia + "'";
	        		        	
	            pstmt = con.prepareStatement(query1);
	            pstmt1 = con.prepareStatement(query2);

	            //System.out.println(query);
	
	           
	                //Avisando
	                pstmt.executeUpdate();
	                pstmt1.executeUpdate();
	                msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnDelete"), "");
	               	
	
	            pstmt.close();
	            pstmt1.close();
	            con.close();
	            
	            //
	            //Eliminar del directorio el archivo físico
	            File file = new File(extContext.getRealPath("/WEB-INF/pivot.config") + File.separator + xml + ".xml");
				if(file.delete()){
	    			System.out.println(file.getName() + " is deleted!");
	    		}else{
	    			System.out.println("Delete operation is failed.");
	    		}
				 //Genera xml de configuración
	            writePivotConfig();
	            
				 limpiarValores();
	        } catch (Exception e) {
	            e.printStackTrace();
	            msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
	        }
	      
    	
    	FacesContext.getCurrentInstance().addMessage(null, msj); 
    }
    
    
    
    
    
    
    private void limpiarValores() {
		// TODO Auto-generated method stub
    	nombre = "";
    	descripcion = "";
    	driver = "";
    	url = "";
    	usuario = "";
    	clave = "";
    	xml = "";
    	validarOperacion = 0; 
	}

        
    /**
     * Actualiza reportes
     **/
    public void update()  {
        try {
        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);

     		con = ds.getConnection();		
     		
     		//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
     		DatabaseMetaData databaseMetaData = con.getMetaData();
     		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
     		
            //Class.forName(getDriver());
            //con = DriverManager.getConnection(
            //      getUrl(), getUsuario(), getClave());
            String query = "UPDATE Bvt009";
             query += " SET DESCRIPCION = ?, URL= ?, DRIVER = ?, USUARIO = ?, CLAVE = ?, XML = ?,  usract = ?, fecact= " + getFecha(productName) + "";
             query += " WHERE NOMBRE = ? and instancia = '" + instancia + "'";
            ////System.out.println(query);
             pstmt = con.prepareStatement(query);
             pstmt.setString(1, descripcion);
             pstmt.setString(2, url);
             pstmt.setString(3, driver);
             pstmt.setString(4, usuario);
             pstmt.setString(5, clave);
             pstmt.setString(6, xml.replace(".xml", ""));
             pstmt.setString(7, login);
             pstmt.setString(8, nombre);
            // Antes de ejecutar valida si existe el registro en la base de Datos.
    
                //Avisando
                pstmt.executeUpdate();
                if(pstmt.getUpdateCount()==0){
                	msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("msnNoUpdate"), "");
                } else {
                	msj = new FacesMessage(FacesMessage.SEVERITY_INFO,  getMessage("msnUpdate"), "");
                }
                descripcion = "";
            	driver = "";
            	url = "";
            	usuario = "";
            	clave = "";
            	xml = "";
            	validarOperacion = 0; 

            pstmt.close();
            con.close();
            
           //Genera xml de configuración
            writePivotConfig();
            //
            
        } catch (Exception e) {
        	msj = new FacesMessage(FacesMessage.SEVERITY_INFO, e.getMessage(), "");
        	e.printStackTrace();
        }
        FacesContext.getCurrentInstance().addMessage(null, msj);
    }
    
    /**
     * Leer Datos de reportes
     **/ 	
  	public void select(int first, int pageSize, String sortField, Object filterValue)   {
  		
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
        	   query += " (SELECT trim(A.NOMBRE), trim(A.DESCRIPCION), trim(A.URL), trim(A.DRIVER), trim(A.USUARIO), TRIM(A.CLAVE), TRIM(A.XML), a.instancia";
		       query += " FROM BVT009 A";
		       query += " WHERE A.NOMBRE||A.DESCRIPCION LIKE trim('%" + ((String) filterValue) +  "%') ";
		       query += " AND   A.instancia = '" + instancia + "'";
		       query += " AND   A.nombre like '" + nombre + "%'";
	  		   query += " order by " + sortField + ") query";
	  		   query += " ) where rownum <= ?";
	           query += " and rn > (?)";
             break;
        case "PostgreSQL":
        	   query += " SELECT trim(A.NOMBRE), trim(A.DESCRIPCION), trim(A.URL), trim(A.DRIVER), trim(A.USUARIO), TRIM(A.CLAVE), TRIM(A.XML), a.instancia";
		       query += " FROM BVT009 A ";
		       query += " WHERE A.NOMBRE||A.DESCRIPCION LIKE trim('%" + ((String) filterValue) +  "%') ";
		       query += " AND   A.instancia = '" + instancia + "'";
		       query += " AND   A.nombre like '" + nombre + "%'";
	  		   query += " order by " + sortField;
	  		   query += " LIMIT ?";
	           query += " OFFSET ?";
             break;     		   
  		}
  		//System.out.println(query);
  		pstmt = con.prepareStatement(query);
  		pstmt.setInt(1, pageSize);
        pstmt.setInt(2, first);
       
  		
        r =  pstmt.executeQuery();
        		
        while (r.next()){
        Bvt009 select = new Bvt009();
     	select.setNombre(r.getString(1));
     	select.setDescripcion(r.getString(2));
     	select.setUrl(r.getString(3));
     	select.setDriver(r.getString(4));
     	select.setUsuario(r.getString(5));
     	select.setClave(r.getString(6));
     	select.setXml(r.getString(7));
     	select.setInstancia(r.getString(8));
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
			msj = new FacesMessage(FacesMessage.SEVERITY_INFO, e.getMessage(), "");
		}   
    }
  	
  	/**
     * Leer registros en la tabla para paginación
     **/ 	
  	public void counter(Object filterValue )  {

  		String query = " SELECT trim(A.NOMBRE), trim(A.DESCRIPCION), trim(A.URL), trim(A.DRIVER), trim(A.USUARIO), TRIM(A.CLAVE), TRIM(A.XML), a.instancia";
	    query += " FROM BVT009 A";
	    query += " WHERE A.NOMBRE||A.DESCRIPCION LIKE trim('%" + ((String) filterValue) +  "%') ";
	    query += " AND   A.nombre like '" + nombre + "%'";
	    query += " AND   A.instancia = '" + instancia + "' order by ?";

	    String querypg = " SELECT trim(A.NOMBRE), trim(A.DESCRIPCION), trim(A.URL), trim(A.DRIVER), trim(A.USUARIO), TRIM(A.CLAVE), TRIM(A.XML), a.instancia";
	    querypg += " FROM BVT009 A ";
	    querypg += " WHERE A.NOMBRE||A.DESCRIPCION LIKE trim('%" + ((String) filterValue) +  "%') ";
	    querypg += " AND   A.nombre like '" + nombre + "%'";
	    querypg += " AND   A.instancia = '" + instancia + "' order by ?";
	    //System.out.println(query);
  		consulta.selectGenericaMDB(query, querypg, JNDI);
        rows = consulta.getData().get(0).size();

  	}
  	
    
  	/**
  	 * Operaciones insertar o modificas
  	 */
    public void guardar() {   	
    	if(validarOperacion==0){
    		insert();
    	} else {
    		update();
    	}
    }
      	
 
   
   /**
  	 * @return the rows
  	 */
  	public int getRows() {
  		return rows;
  	}
  	
  	///Escribiendo archivo de configuración///////////////////
  	
  	/**
  	 * Escribe pivot4j-config y reconfigura cada que vez se define
  	 * un cubo y una conección lo genera, el servicio del app server
  	 * debe ser reiniciado posteriormente para que el pivot
  	 * pueda tomar la conección nueva y el cubo xml nuevo
  	 */
  	private void writePivotConfig() {
  		//Recorrido por la tabla de definición de cubos bvt009
  		//Para generar Xml configuración Pivot
  		PntGenerica consulta = new PntGenerica();
  		String query = "Select trim(nombre), trim(descripcion), trim(url), trim(driver), trim(usuario), trim(clave), trim(xml) from bvt009 where instancia = '" + instancia + "' order by ?";
  		int rows;
  		
  		//Hacer la consulta
  		consulta.selectGenerica(query, JNDI);
  		rows = consulta.getData().get(0).size();
  		if(rows>0) { //Procesar siempre que la consulta sea mayor a cero
  		try {
  		//	File file = new File(extContext.getRealPath("/WEB-INF") + File.separator + "pivot4j-config.xml");
  		//	file.delete();
  			
        	// create the jdom
  			Document doc = new Document();
  			// create root element
  			Element config = new Element("config");
  			doc.setRootElement(config);
  	 
  			// add a comment below config
  			Comment comment = new Comment("Data source definitions.");
  			config.addContent(comment);
  	 
  			// add child datasources
  			Element datasources = new Element("datasources");
  			
  		    // add a comment below datasources
  			Comment comment1 = new Comment("You can register any kind of data source which is supported by Olap4J.\n" + 
  					"			For a reference of available drivers and connection parameters, please \n" + 
  					"			visit the Olap4J home page :\n" + 
  					"\n" + 
  					"			http://www.olap4j.org/");
  			datasources.addContent(comment1);
  			
  			//Comienza recorrido
  			IntStream stream = IntStream.range(0, rows);
  			
  			stream.forEach(x -> {
  			//Datasource
  			Element datasource = new Element("datasource");
  			datasource.addContent(new Element("name").setText(consulta.getData().get(0).get(x)));
  			datasource.addContent(new Element("description").setText(consulta.getData().get(1).get(x)));
  			
  			//Conection info
  			Element connection_info = new Element("connection-info");
  			connection_info.addContent(new Element("url").setText("jdbc:mondrian:"));
  			connection_info.addContent(new Element("driverClass").setText("mondrian.olap4j.MondrianOlap4jDriver"));
  			
  		    // add attribute
  			Attribute catalog =      new Attribute("name", "Catalog");
  			Attribute Jdbc =         new Attribute("name", "Jdbc");
  			Attribute JdbcDrivers =  new Attribute("name", "JdbcDrivers");
  			Attribute JdbcUser =     new Attribute("name", "JdbcUser");
  			Attribute JdbcPassword = new Attribute("name", "JdbcPassword");
  			
  			//Properties
  			Element properties = new Element("properties");
  			properties.addContent(new Element("property").setText("file:"+extContext.getRealPath("/WEB-INF/pivot.config")+File.separator+consulta.getData().get(6).get(x)+".xml").setAttribute(catalog));
  			properties.addContent(new Element("property").setText(consulta.getData().get(2).get(x)).setAttribute(Jdbc));
  			properties.addContent(new Element("property").setText(consulta.getData().get(3).get(x)).setAttribute(JdbcDrivers));
  			properties.addContent(new Element("property").setText(consulta.getData().get(4).get(x)).setAttribute(JdbcUser));
  			properties.addContent(new Element("property").setText(consulta.getData().get(5).get(x)).setAttribute(JdbcPassword));
  			
  			  			
  			connection_info.addContent(properties);
  			
  			datasource.addContent(connection_info);
  			
  			datasources.addContent(datasource);

  			
  			});
  			
  			config.addContent(datasources);
		
		//outputter
		XMLOutputter xmlOutput = new XMLOutputter();
		
		// display nice nice
		xmlOutput.setFormat(Format.getPrettyFormat());
		xmlOutput.output(doc, new FileWriter(extContext.getRealPath("/WEB-INF/pivot.config") + File.separator + "pivot4j-config.xml"));
		
  		} catch (IOException io) {
  		  io.printStackTrace();
  		}
  		//Liberar lista
  		consulta.getData().clear();
  		
  		} else {//Fin validación mayor a cero
  			//Borrar archivo de configuración
  			try{
  			File file = new File(extContext.getRealPath("/WEB-INF/pivot.config") + File.separator + "pivot4j-config.xml");
  			file.delete();
  			}catch(Exception e){
  				e.printStackTrace();
  	    	}
  		}
  	}
  	
  

 
}
