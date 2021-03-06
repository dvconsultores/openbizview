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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;



/**
	 *
	 * @author Andres
	 */

	@ManagedBean
	@ViewScoped
	public class Bvt001 extends Bd implements Serializable {
	
	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		

	
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
			if(segu.listMenu.get(x).opcMenu.equals("R01") && segu.listMenu.get(x).vistaMenu.equals("1") && segu.opcbot("1").equals("false")) {
				new LoginBean().logout();
			}
		});
		
		

		select();
		listarGrupos();		
		
	     }
	}
	
	public String add() {
		return "pagenav";
	}
	
	//Declaración de variables, getters y setters
	
	private String codrep = "", count = "";
	private String desrep = "";
	private String comrep = "";
	private String anio = "";
	private String vgrupo;
	private String vgrupodesgrupo;
	private String codgrup = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("codgrup"); 
	private String vlRol = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("rol"); //Usuario logeado
	private String instancia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instancia"); //Usuario logeado
	private List<Bvt001> list = new ArrayList<Bvt001>();
	private List<SelectItem> listgrupos = new ArrayList<SelectItem>();
	private String vinstancia = ""; //Istancia para el log
	SeguridadMenuBean segu = new SeguridadMenuBean();

	
	/**
	 * @return the vlRol
	 */
	public String getVlRol() {
		return vlRol;
	}
	
	


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
	public List<Bvt001> getList() {
		return list;
	}
	
	
	/**
	 * @param list the list to set
	 */
	public void setList(List<Bvt001> list) {
		this.list = list;
	}
	
	
	
	
	/**
	 * @return the listgrupos
	 */
	public List<SelectItem> getListgrupos() {
		return listgrupos;
	}




	/**
	 * @param listgrupos the listgrupos to set
	 */
	public void setListgrupos(List<SelectItem> listgrupos) {
		this.listgrupos = listgrupos;
	}




	public String getCodrep() {
		return codrep;
	}
	
	
	public void setCodrep(String codrep) {
		this.codrep = codrep;
	}
	
	
	public String getDesrep() {
		return desrep;
	}
	
	
	public void setDesrep(String desrep) {
		this.desrep = desrep;
	}
	
	
	public String getComrep() {
		return comrep;
	}
	
	
	public void setComrep(String comrep) {
		this.comrep = comrep;
	}
	
	
	public String getAnio() {
		return anio;
	}
	
	
	public void setAnio(String anio) {
		this.anio = anio;
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
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("codgrup", codgrup);
	}
	
	
	
	
	/**
	 * @return the vgrupo
	 */
	public String getVgrupo() {
		return vgrupo;
	}


	/**
	 * @param vgrupo the vgrupo to set
	 */
	public void setVgrupo(String vgrupo) {
		this.vgrupo = vgrupo;
	}


	/**
	 * @return the vgrupodesgrupo
	 */
	public String getVgrupodesgrupo() {
		return vgrupodesgrupo;
	}


	/**
	 * @param vgrupodesgrupo the vgrupodesgrupo to set
	 */
	public void setVgrupodesgrupo(String vgrupodesgrupo) {
		this.vgrupodesgrupo = vgrupodesgrupo;
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
	 * @return the count
	 */
	public String getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(String count) {
		this.count = count;
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
	PreparedStatement pstmt2 = null;
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

     		//System.out.println("Grupo: " + veccodgrup[0]);
     		con = ds.getConnection();		
           
       		//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
     		DatabaseMetaData databaseMetaData = con.getMetaData();
     		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
     		
            String query = "INSERT INTO Bvt001 VALUES (?,?,?,?," + getFecha(productName) + ",?," + getFecha(productName) + ",?,?)";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, codrep.toUpperCase());
            pstmt.setString(2, desrep);
            pstmt.setString(3, comrep);
            pstmt.setString(4, login);
            pstmt.setString(5, login);
            pstmt.setString(6, codgrup);
            pstmt.setInt(7, Integer.parseInt(instancia));
            ////System.out.println(query);
       
                //Avisando
                pstmt.executeUpdate();
                msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnInsert"), "");
                FacesContext.getCurrentInstance().addMessage(null, msj);
                desrep = "";
                //codgrup = "";
                comrep = "";
                //Acceso automático a reportes
                insertAccesoReporte();
            
            pstmt.close();
            con.close();
            buscar();
    	} catch (Exception e){
    		e.printStackTrace();
    		msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
        	FacesContext.getCurrentInstance().addMessage(null, msj);
    		
    	} finally {
			try {
				if (con == null) {
					con.close();
				}
				if (pstmt == null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    }
    
    
    /**
     * Inserta el acceso al reporte según el usuario logueado reportes.
     **/
    private void insertAccesoReporte()  {
    	//Valida que los campos no sean nulos
        try {
        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);
     		//System.out.println("Grupo: " + veccodgrup[0]);
     		con = ds.getConnection();		
     		
     		//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
     		DatabaseMetaData databaseMetaData = con.getMetaData();
     		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección

            String query = "INSERT INTO Bvt007 VALUES (?,?,?," + getFecha(productName) + ",?," + getFecha(productName) + ",?)";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, vlRol);
            pstmt.setString(2, codrep.toUpperCase());
            pstmt.setString(3, login);
            pstmt.setString(4, login);
            pstmt.setInt(5, Integer.parseInt(instancia));
            ////System.out.println(query);
           
                //Avisando
                pstmt.executeUpdate();
                codrep = "";
           
            pstmt.close();
            con.close();
    	} catch (Exception e){
    		e.printStackTrace();  
    	} finally {
			try {
				if (con == null) {
					con.close();
				}
				if (pstmt == null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    }
    
 
    /**
     * Eliminar reportes   
     */
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
	
	        	String query1 = "DELETE from Bvt007 WHERE b_codrep in (" + param + ") and instancia = '" + instancia + "'";
	        	String query2 = "DELETE from Bvt006 WHERE b_codrep in (" + param + ") and instancia = '" + instancia + "' and b_coduser = '" + login + "'";
	        	String query3 = "DELETE from Bvt001 WHERE codrep in (" + param + ") and instancia = '" + instancia + "'";
	        		        	
	            pstmt = con.prepareStatement(query1);
	            pstmt1 = con.prepareStatement(query2);
	            pstmt2 = con.prepareStatement(query3);
	            //System.out.println(query);
	
	           
	                //Avisando
	                pstmt.executeUpdate();
	                pstmt1.executeUpdate();
	                pstmt2.executeUpdate();
	                msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnDelete"), "");
	                limpiarValores();	
	
	            pstmt.close();
	            pstmt1.close();
	            pstmt2.close();
	            con.close();
	
	            buscar();
	        } catch (Exception e) {
	            e.printStackTrace();
	            msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
	        } finally {
				try {
					if (con == null) {
						con.close();
					}
					if (pstmt == null) {
						pstmt.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
    	}
    	FacesContext.getCurrentInstance().addMessage(null, msj); 
    }
    
    
    
    private void limpiarValores() {
		// TODO Auto-generated method stub
    	codrep = "";
    	desrep = "";
    	comrep = "";
    	anio = "";
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
            String query = "UPDATE Bvt001";
             query += " SET desrep = ?, comrep= ?, usract = ?, fecact=" + getFecha(productName) + ", codgrup = '" + codgrup + "'";
             query += " WHERE codrep = ? and instancia = '" + instancia + "'";
            ////System.out.println(query);
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, desrep);
            pstmt.setString(2, comrep);
            pstmt.setString(3, login);
            pstmt.setString(4, codrep.toUpperCase());
            // Antes de ejecutar valida si existe el registro en la base de Datos.
    
                //Avisando
                pstmt.executeUpdate();
                if(pstmt.getUpdateCount()==0){
                	msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("msnNoUpdate"), "");
                } else {
                	msj = new FacesMessage(FacesMessage.SEVERITY_INFO,  getMessage("msnUpdate"), "");
                }
                validarOperacion = 0;
                desrep = "";
            	comrep = "";
            	codgrup = "";

            pstmt.close();
            con.close();
            
            buscar();
        } catch (Exception e) {
        	msj = new FacesMessage(FacesMessage.SEVERITY_INFO, e.getMessage(), "");
        } finally {
			try {
				if (con == null) {
					con.close();
				}
				if (pstmt == null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
        FacesContext.getCurrentInstance().addMessage(null, msj);
    }
    
    /**
     * Leer Datos de reportes
     **/ 	
  	public void select()   {
  		
  		Context initContext;
		try {
			initContext = new InitialContext();
		  
 		DataSource ds = (DataSource) initContext.lookup(JNDI);
 		con = ds.getConnection();
 		
 		//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
 		DatabaseMetaData databaseMetaData = con.getMetaData();
 		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
 		
 		if(codgrup==null) {
 			codgrup = "";
 		}


  		String query = "";

  		switch ( productName ) {
        case "Oracle":
        	   query += " SELECT trim(A.CODREP), trim(A.DESREP), trim(A.COMREP), trim(A.CODGRUP), trim(B.DESGRUP), a.instancia";
		       query += " FROM BVT001 A, BVT001A B";
		       query += " WHERE A.CODGRUP=B.CODGRUP";
		       query += " AND   A.CODREP  IN (SELECT B_CODREP FROM BVT007 WHERE B_CODROL IN (SELECT B_CODROL FROM BVT002 WHERE CODUSER = '" + login + "' and instancia ='" + instancia + "' UNION ALL SELECT B_CODrol FROM BVT008 WHERE CODUSER = '" + login + "' and instancia = '" +  instancia + "'))";
		       query += " AND   A.instancia = '" + instancia + "'";
		       query += " AND   A.codrep like '" + codrep + "%'";
	  		   if(!codgrup.equals("")){
		  	   query += " AND   A.CODGRUP  = trim('" + codgrup +  "')";
		  	 	}
	  		   query += " order by ?";

             break;
        case "PostgreSQL":
        	   query += " SELECT trim(A.CODREP), trim(A.DESREP), trim(A.COMREP), trim(A.CODGRUP), trim(B.DESGRUP), a.instancia ";
        	   query += " FROM BVT001 A, BVT001A B";
		       query += " WHERE A.CODGRUP=B.CODGRUP";
		       query += " AND   A.CODREP  IN (SELECT B_CODREP FROM BVT007 WHERE B_CODROL IN (SELECT B_CODROL FROM BVT002 WHERE CODUSER = '" + login + "' and instancia ='" + instancia + "' UNION ALL SELECT B_CODrol FROM BVT008 WHERE CODUSER = '" + login + "' and instancia = '" +  instancia + "'))";
		       query += " AND   A.instancia = '" + instancia + "'";
		       query += " AND   A.codrep like '" + codrep + "%'";
	  		   if(!codgrup.equals("")){
		  	   query += " AND   A.CODGRUP  = trim('" + codgrup +  "')";
		  	 	}
	  		   query += " order by  ?" ;
             break;     		   
  		}
  		//System.out.println(query);
  		pstmt = con.prepareStatement(query);
  		pstmt.setInt(1, 1);
       
  		
        r =  pstmt.executeQuery();
        		
        while (r.next()){
        Bvt001 select = new Bvt001();
     	select.setCodrep(r.getString(1));
     	select.setDesrep(r.getString(2));
     	select.setComrep(r.getString(3));
     	select.setVgrupo(r.getString(4));
        select.setVgrupodesgrupo(r.getString(5));	
        select.setVinstancia(r.getString(6));
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
		} finally {
			try {
				if (con == null) {
					con.close();
				}
				if (pstmt == null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}   
		
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
  	 * Resetea variable de sesión
  	 */
    public void reset(){
    	codgrup = "";
    	setCodgrup(codgrup);
    }
    
    
    /**
     * Metodo para obtener los grupos
  	 */ 
  	 public void listarGrupos()  { 
        Context initContext;
		try {
			initContext = new InitialContext();
		  
 		DataSource ds = (DataSource) initContext.lookup(JNDI);
 		con = ds.getConnection();
 		
 		//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
 		DatabaseMetaData databaseMetaData = con.getMetaData();
 		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección

 		if(codgrup==null) {
 			codgrup = "";
 		}

  		String query = "";

  		switch ( productName ) {
        case "Oracle":
        	   query += " Select codgrup, desgrup from bvt001a where  instancia = '" + instancia + "' order by codgrup, ?";
             break;
        case "PostgreSQL":
        	   query += " Select codgrup, desgrup from bvt001a where  instancia = '" + instancia + "' order by codgrup, ?";
             break;     		   
  		}
  		//System.out.println(query);
  		pstmt = con.prepareStatement(query);
  		pstmt.setInt(1, 1);
       
  		
        r =  pstmt.executeQuery();
        		
        while (r.next()){
        SelectItem select = new SelectItem();		
        select.setLabel(r.getString(2));
        select.setValue(r.getString(1));
        	//Agrega la lista
        listgrupos.add(select);
        }
        //Cierra las conecciones
        pstmt.close();
        con.close();
        r.close();
        
		} catch (NamingException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msj = new FacesMessage(FacesMessage.SEVERITY_INFO, e.getMessage(), "");
		} finally {
			try {
				if (con == null) {
					con.close();
				}
				if (pstmt == null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}   
		
		
		}
   
  	/**
      * Metodo para limpiar la variable listempresa y cargarla con la informacion del metodo select
   	 */ 	
   	public void buscar() {

   		list = new ArrayList<>();
   		list.clear();
   		select();
   	}
   	
  	
 
}
