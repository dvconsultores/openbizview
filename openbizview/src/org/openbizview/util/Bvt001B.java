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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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

import org.apache.commons.io.FileUtils;
import org.openbizview.dashboard.EditXhtml;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
	/**
	 *
	 * @author Andres
	 */

	@ManagedBean
	@ViewScoped
	public class Bvt001B extends Bd implements Serializable {
	
	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private LazyDataModel<Bvt001B> lazyModel;  
		
		
		/**
		 * @return the lazyModel
		 */
		public LazyDataModel<Bvt001B> getLazyModel() {
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
			if(segu.listMenu.get(x).opcMenu.equals("R06") && segu.listMenu.get(x).vistaMenu.equals("1")) {
				RequestContext.getCurrentInstance().execute("PF('idleDialogNP').show()");
			}
		});

		
		lazyModel  = new LazyDataModel<Bvt001B>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 7217573531435419432L;
			
            @Override
			public List<Bvt001B> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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
	
    private String codgrup = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("codgrup"), desgrup = "";
    private String context = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("context");
    private String descripcion = "";
    private String jar = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("jar");
    private String indexName = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("indexName");
	private String instancia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instancia"); //Usuario logeado
	private int rows;
	// Primitives
	private static final int BUFFER_SIZE = 6124;
	private Object filterValue = "";
	private List<Bvt001B> list = new ArrayList<Bvt001B>();
	SeguridadMenuBean segu = new SeguridadMenuBean();
	ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
	private String opciones = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("opciones");
	
	


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
	public List<Bvt001B> getList() {
		return list;
	}
	
	
	/**
	 * @param list the list to set
	 */
	public void setList(List<Bvt001B> list) {
		this.list = list;
	}
	
	

	/**
	 * @return the context
	 */
	public String getContext() {
		return context;
	}

	/**
	 * @param context the context to set
	 */
	public void setContext(String context) {
		this.context = context;
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("context", context);
	}

	/**
	 * @return the codgrup
	 */
	public String getCodgrup() {
		return codgrup;
	}

	/**
	 * @param codgrup the codgrup to set
	 */
	public void setCodgrup(String codgrup) {
		this.codgrup = codgrup;
	}

	/**
	 * @return the jar
	 */
	public String getJar() {
		return jar;
	}

	/**
	 * @param jar the jar to set
	 */
	public void setJar(String jar) {
		this.jar = jar;
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("jar", jar);
	}

	/**
	 * @return the indexName
	 */
	public String getIndexName() {
		return indexName;
	}

	/**
	 * @param indexName the indexName to set
	 */
	public void setIndexName(String indexName) {
		this.indexName = indexName;
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("indexName", indexName);
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

	
	/**
	 * @return the desgrup
	 */
	public String getDesgrup() {
		return desgrup;
	}

	/**
	 * @param desgrup the desgrup to set
	 */
	public void setDesgrup(String desgrup) {
		this.desgrup = desgrup;
	}
	
	

	/**
	 * @return the opciones
	 */
	public String getOpciones() {
		return opciones;
	}

	/**
	 * @param opciones the opciones to set
	 */
	public void setOpciones(String opciones) {
		this.opciones = opciones;
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("opciones", opciones);
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
     		
            String query = "INSERT INTO Bvt001B VALUES (?,?,?,?,?,?," + getFecha(productName) + ",?," + getFecha(productName) + ",?)";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, codgrup.split(" - ")[0]);
            pstmt.setString(2, context);
            pstmt.setString(3, descripcion);
            pstmt.setString(4, jar.replace(".jar", ""));   
            pstmt.setInt(5, Integer.parseInt(instancia));
            pstmt.setString(6, login);
            pstmt.setString(7, login);
            pstmt.setString(8, indexName);
            ////System.out.println(query);
       
                //Avisando
                pstmt.executeUpdate();
                msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnInsert"), "");
                FacesContext.getCurrentInstance().addMessage(null, msj);
            
            
            pstmt.close();
            con.close();
            new Bvt001C().insertFromBvt001B(context.trim(), rol);
           
            
            //Crear directorio de proyecto
            File rutadb = new File(extContext.getRealPath("/db") + File.separator +  context);
            if (! rutadb.exists()){
            	rutadb.mkdirs();
    		        // If you require it to make the entire directory path including parents,
    		        // use directory.mkdirs(); here instead.
    		}
            //Escribir sobre el index para agregar código de verificación
            //
            limpiarValores();

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

	        	String query1 = "DELETE from Bvt001C WHERE CONTEXT = '" + context + "' and instancia = '" + instancia + "'";
	        	String query2 = "DELETE from Bvt001B WHERE CONTEXT = '" + context + "' and instancia = '" + instancia + "'";
	        		        	
	        	pstmt = con.prepareStatement(query1);
	            pstmt1 = con.prepareStatement(query2);

	            //System.out.println(query);
	            //Eliminar directorios
	            File directorio = new File(extContext.getRealPath("/db") + File.separator + context);
	            	try {
						FileUtils.deleteDirectory(directorio);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            	
	            //Eliminar jar file		            
	            	File file = new File(extContext.getRealPath("/WEB-INF/lib") + File.separator + jar + ".jar");
					if(file.delete()){
		    			System.out.println(file.getName() + " is deleted!");
		    		}else{
		    			System.out.println("Delete operation is failed.");
		    		}
	           
	                //Avisando
	                pstmt.executeUpdate();
	                pstmt1.executeUpdate();
	                msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnDelete"), "");
	                limpiarValores();	
	
	            pstmt.close();
	            pstmt1.close();
	            con.close();

	     
	        } catch (Exception e) {
	            e.printStackTrace();
	            msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
	        }
	      
    	FacesContext.getCurrentInstance().addMessage(null, msj); 
    }
    
    
    
    
    
    
    private void limpiarValores() {
		// TODO Auto-generated method stub
    	codgrup = "";
        context = "";
        descripcion = "";
        jar = "";
        indexName = "";
    	validarOperacion = 0; 
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
            String query = "UPDATE Bvt001B";
             query += " SET CODGRUP = ?, DESCRIPCION= ?, JAR = ?, INDEX_NAME = ?,  usract = ?, fecact= " + getFecha(productName) + "";
             query += " WHERE context = ? and instancia = '" + instancia + "'";
            ////System.out.println(query);
             pstmt = con.prepareStatement(query);
             
             pstmt.setString(1, codgrup.split(" - ")[0]);
             pstmt.setString(2, descripcion);
             pstmt.setString(3, jar.replace(".jar", ""));  
             pstmt.setString(4, indexName);
             pstmt.setString(5, login);
             pstmt.setString(6, context);
             
       
            // Antes de ejecutar valida si existe el registro en la base de Datos.
    
                //Avisando
                pstmt.executeUpdate();
                if(pstmt.getUpdateCount()==0){
                	msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("msnNoUpdate"), "");
                } else {
                	msj = new FacesMessage(FacesMessage.SEVERITY_INFO,  getMessage("msnUpdate"), "");
                }
                codgrup = "";
                context = "";
                descripcion = "";
                jar = "";
                indexName = "";
            	validarOperacion = 0; 

            pstmt.close();
            con.close();
            
           //Genera xml de configuración
       
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
 		
 		if(codgrup==null){
 			codgrup = " - ";
 		}
 		if(context==null){
 			context = "";
 		}
 		if(codgrup==""){
 			codgrup = " - ";
 		}
 		
 		String[] veccodgrup = codgrup.split("\\ - ", -1);


  		String query = "";

  		switch ( productName ) {
        case "Oracle":
        	   query += "  select * from ";
        	   query += " ( select query.*, rownum as rn from";
        	   query += " (SELECT trim(A.CODGRUP), trim(A.CONTEXT), trim(A.DESCRIPCION), trim(A.JAR), A.INSTANCIA, TRIM(B.DESGRUP), trim(index_name)";
		       query += " FROM Bvt001B A, BVT001A B";
		       query += " WHERE A.CODGRUP = B.CODGRUP ";
		       query += " AND   A.INSTANCIA = B.INSTANCIA ";
		       query += " AND   A.CONTEXT||A.DESCRIPCION LIKE trim('%" + ((String) filterValue) +  "%') ";
		       query += " AND   A.instancia = '" + instancia + "'";
		       query += " AND   A.CONTEXT like '" + context + "%'";
		       if(!veccodgrup[0].equals("")){
			  	   query += " AND   A.CODGRUP  = trim('" + veccodgrup[0].toUpperCase() +  "')";
			   }
	  		   query += " order by " + sortField + ") query";
	  		   query += " ) where rownum <= ?";
	           query += " and rn > (?)";
             break;
        case "PostgreSQL":
        	   query += " SELECT trim(A.CODGRUP), trim(A.CONTEXT), trim(A.DESCRIPCION), trim(A.JAR), A.INSTANCIA, TRIM(B.DESGRUP), trim(index_name)";
		       query += " FROM Bvt001B A, BVT001A B ";
		       query += " WHERE A.CODGRUP = B.CODGRUP ";
		       query += " AND   A.INSTANCIA = B.INSTANCIA ";
		       query += " AND   A.CONTEXT||A.DESCRIPCION LIKE trim('%" + ((String) filterValue) +  "%') ";
		       query += " AND   A.instancia = '" + instancia + "'";
		       query += " AND   A.CONTEXT like '" + context + "%'";
		       if(!veccodgrup[0].equals("")){
			  	   query += " AND   A.CODGRUP  = trim('" + veccodgrup[0].toUpperCase() +  "')";
			   }
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
        Bvt001B select = new Bvt001B();
            select.setCodgrup(r.getString(1));
            select.setContext(r.getString(2));
            select.setDescripcion(r.getString(3));
            select.setJar(r.getString(4));
            select.setInstancia(r.getString(5));
            select.setDesgrup(r.getString(6));
            select.setIndexName(r.getString(7));
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
  		
  		if(codgrup==null){
 			codgrup = " - ";
 		}
  		if(context==null){
 			context = "";
 		}
 		if(codgrup==""){
 			codgrup = " - ";
 		}
  		
  		String[] veccodgrup = codgrup.split("\\ - ", -1);

  		String query = " SELECT trim(A.CODGRUP), trim(A.CONTEXT), trim(A.DESCRIPCION), trim(A.JAR), A.INSTANCIA, TRIM(B.DESGRUP)";
	           query += " FROM Bvt001B A, BVT001A B";
	           query += " WHERE A.CODGRUP = B.CODGRUP ";
	           query += " AND   A.INSTANCIA = B.INSTANCIA ";
	           query += " AND   A.CONTEXT||A.DESCRIPCION LIKE trim('%" + ((String) filterValue) +  "%') ";
	           query += " AND   A.instancia = '" + instancia + "'";
	           if(!veccodgrup[0].equals("")){
			  	   query += " AND   A.CODGRUP  = trim('" + veccodgrup[0].toUpperCase() +  "')";
			   }
	           query += " AND   A.CONTEXT like '" + context + "%' order by ?";

	   String  querypg = " SELECT trim(A.CODGRUP), trim(A.CONTEXT), trim(A.DESCRIPCION), trim(A.JAR), A.INSTANCIA, TRIM(B.DESGRUP)";
			   querypg += " FROM Bvt001B A, BVT001A B ";
			   querypg += " WHERE A.CODGRUP = B.CODGRUP ";
			   querypg += " AND   A.INSTANCIA = B.INSTANCIA ";
			   querypg += " AND   A.CONTEXT||A.DESCRIPCION LIKE trim('%" + ((String) filterValue) +  "%') ";
			   querypg += " AND   A.instancia = '" + instancia + "'";
			   if(!veccodgrup[0].equals("")){
				   querypg += " AND   A.CODGRUP  = trim('" + veccodgrup[0].toUpperCase() +  "')";
			   }
			   querypg += " AND   A.CONTEXT like '" + context + "%' order by ?";

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
  	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////UPLOAD DE ARCHIVOS ///////////////////////////////////////////////// 
  	/**
  	 * Subir arhcivo comprimido
  	 * @param event
  	 */
  	public void handleZip(FileUploadEvent event) {
  	 EditXhtml editxhtml = new EditXhtml();
  	 if(opciones==null || opciones == "") {
  		msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("dashboardselect"), "");
		FacesContext.getCurrentInstance().addMessage(null, msj);
  	 } else {
		switch (opciones) {
		case "Zip":
			
		// Subir archivo .zip
				try {

					handleDashboardUploadZip(event);
				
				    //Buscar archivo y descomprimir 	
					ZipInputStream zipIn = new ZipInputStream(new FileInputStream(extContext.getRealPath("/db")  
							                             + File.separator + context + File.separator + event.getFile().getFileName()));
					
					ZipEntry entry = zipIn.getNextEntry();
			        // iterates over entries in the zip file
			        while (entry != null) {
			            String filePath = extContext.getRealPath("/db")  
	                             + File.separator + context + File.separator + File.separator + entry.getName();
			            if (!entry.isDirectory()) {
			                // if the entry is a file, extracts it
			                extractFile(zipIn, filePath);
			            } else {
			                // if the entry is a directory, make the directory
			                File dir = new File(filePath);
			                dir.mkdir();
			            }
			            zipIn.closeEntry();
			            entry = zipIn.getNextEntry();
			        }
			        zipIn.close();

					//Borrar archivo comprimido
					File file = new File(extContext.getRealPath("/db")  
                            + File.separator + context + File.separator + event.getFile().getFileName());
					if(file.delete()){
		    			System.out.println(file.getName() + " is deleted!");
		    		}else{
		    			System.out.println("Delete operation is failed.");
		    		}
					
					//Una vez descomprimido procede a localizar el archivo .jar y moverlo
					File source = new File(extContext.getRealPath("/db")  
                            + File.separator + context + File.separator + jar + ".jar");

					File dest = new File(extContext.getRealPath("/WEB-INF/lib") + File.separator + jar + ".jar");
					if(!dest.exists()) {
						//Realiza nada y devuelve mensaje
						FileUtils.moveFile(source, dest);
						source.delete();
					} 
					//Crear directorio de proyecto
		            File rutadb = new File(extContext.getRealPath("/db") + File.separator +  context);
					editxhtml.editFile(rutadb + File.separator + indexName);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				
				break;
		   case "Jar":
			   handleDashboardUploadJar(event);
				default:
					break;
		}
  	 }
	}  
  	

  	 /**
     * Extracts a zip entry (file entry)
     * @param zipIn
     * @param filePath
     * @throws IOException
     */
    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

    /**
     * Subir archivo .zip a proyecto
     * @param event
     */
  	private void handleDashboardUploadZip(FileUploadEvent event) {
  		File ruta = new File(
				extContext.getRealPath("/db")  + File.separator + context + File.separator + event.getFile().getFileName());
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(ruta);
			byte[] buffer = new byte[BUFFER_SIZE];

			int bulk;
			InputStream inputStream = event.getFile().getInputstream();
			while (true) {
				bulk = inputStream.read(buffer);
				if (bulk < 0) {
					break;
				}
				fileOutputStream.write(buffer, 0, bulk);
				fileOutputStream.flush();
			}
			msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("autos02Uploaded"), "");
			FacesContext.getCurrentInstance().addMessage(null, msj);
			fileOutputStream.close();
			inputStream.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
			FacesContext.getCurrentInstance().addMessage(null, msj);

		}
      	
	}
  	

    /*
     * Subir archivo .jar
     */
	private void handleDashboardUploadJar(FileUploadEvent event) {
		// System.out.println("Thread is running");
		File ruta = new File(extContext.getRealPath("/WEB-INF/lib") + File.separator + event.getFile().getFileName());
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(ruta);
			byte[] buffer = new byte[BUFFER_SIZE];

			int bulk;
			InputStream inputStream = event.getFile().getInputstream();
			while (true) {
				bulk = inputStream.read(buffer);
				if (bulk < 0) {
					break;
				}
				fileOutputStream.write(buffer, 0, bulk);
				fileOutputStream.flush();
			}
			msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("autos02Uploaded"), "");
			FacesContext.getCurrentInstance().addMessage(null, msj);
			fileOutputStream.close();
			inputStream.close();

		} catch (IOException e) {
			e.printStackTrace();
			msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
			FacesContext.getCurrentInstance().addMessage(null, msj);

		}
	  
	}
	
	public void reset() {
  		context = null;    
  		jar = null;
  		indexName = null;
    }

	
	}
