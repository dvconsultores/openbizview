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
import java.util.Properties;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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
	public class Bvt002 extends Bd implements Serializable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		
		private LazyDataModel<Bvt002> lazyModel;  
		
		
		
		
	    /**
		 * @return the lazyModel
		 */
		public LazyDataModel<Bvt002> getLazyModel() {
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
			if(segu.listMenu.get(x).opcMenu.equals("M12") && segu.listMenu.get(x).vistaMenu.equals("1")) {
				new LoginBean().logout();
			}
		});

		lazyModel  = new LazyDataModel<Bvt002>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 7217573531435419432L;
			
            @Override
			public List<Bvt002> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
            	//Filtro
            	if (filters != null) {
               	 for(Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
               		 String filterProperty = it.next(); // table column name = field name
                     filterValue = filters.get(filterProperty);
               	 }
                }
            		//Consulta
					select(first, pageSize,sortField, filterValue);
					//
					
					//Counter
					counter(filterValue);
					//Contador lazy
					lazyModel.setRowCount(rows);  //Necesario para crear la paginación
         
				return list;  
            } 
            
		};
		if(vusuario==null){
			vusuario = "";
		}
	     }
	}
	
	private String coduser = "";
	private String desuser = "";
	private String cluser = "";
	private String cluser1 = "";
	private String b_codrol = "";
	private String desrol = "";
	private String cluserold = "";
	private String mail = "";
	private int columns;
	private String[][] arr;
	private Object filterValue = "";
	private List<Bvt002> list = new ArrayList<Bvt002>();
	private List<Bvt002> filter = new ArrayList<Bvt002>();
	@SuppressWarnings("unchecked")
	List<Bvt002> listRoles = (List<Bvt002>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("listRoles");
	@SuppressWarnings("unchecked")
	List<Bvt002> listRolesAdicionales = (List<Bvt002>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("listRolesAdicionales");
	SeguridadMenuBean segu = new SeguridadMenuBean();

	
	private int validarOperacion = 0;
	private String instancia = "";
	private String instancia_insert = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instancia"); //Usuario logeado
	private String rolfilter = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("rolfilter"); //Usuario logeado
	
	//Cambio de password
	StringMD md = new StringMD();
	private String randomKey;
	
	
	//Roles adicionales
	private String vcodrol;
	private String vcodroladic;
	private String vdesrol;
	private String isRol;
	PassStore passStore = new PassStore();


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
	 * @return the cluser1
	 */
	public String getCluser1() {
		return cluser1;
	}

	/**
	 * @param cluser1 the cluser1 to set
	 */
	public void setCluser1(String cluser1) {
		this.cluser1 = cluser1;
	}

		/**
	 * @return the coduser
	 */
	public String getCoduser() {
		return coduser;
	}
	
	/**
	 * @param coduser the coduser to set
	 */
	public void setCoduser(String coduser) {
		this.coduser = coduser;
	}
	
	/**
	 * @return the desuser
	 */
	public String getDesuser() {
		return desuser;
	}
	
	/**
	 * @param desuser the desuser to set
	 */
	public void setDesuser(String desuser) {
		this.desuser = desuser;
	}
	
	/**
	 * @return the cluser
	 */
	public String getCluser() {
		return cluser;
	}
	
	/**
	 * @param cluser the cluser to set
	 */
	public void setCluser(String cluser) {
		this.cluser = cluser;
	}
	
	/**
	 * @return the b_codrol
	 */
	public String getb_codrol() {
		return b_codrol;
	}
	
	/**
	 * @param b_codrol the b_codrol to set
	 */
	public void setb_codrol(String b_codrol) {
		this.b_codrol = b_codrol;
	}
	
	
	
	/**
	 * @return the cluserold
	 */
	public String getCluserold() {
		return cluserold;
	}
	
	/**
	 * @param cluserold the cluserold to set
	 */
	public void setCluserold(String cluserold) {
		this.cluserold = cluserold;
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
	 * @return the vcodrol
	 */
	public String getVcodrol() {
		return vcodrol;
	}



	/**
	 * @param vcodrol the vcodrol to set
	 */
	public void setVcodrol(String vcodrol) {
		this.vcodrol = vcodrol;
	}



	/**
	 * @return the vdesrol
	 */
	public String getVdesrol() {
		return vdesrol;
	}



	/**
	 * @param vdesrol the vdesrol to set
	 */
	public void setVdesrol(String vdesrol) {
		this.vdesrol = vdesrol;
	}



	/**
	 * @return the isRol
	 */
	public String getIsRol() {
		return isRol;
	}



	/**
	 * @param isRol the isRol to set
	 */
	public void setIsRol(String isRol) {
		this.isRol = isRol;
	}
	
	

	/**
	 * @return the listRoles
	 */
	public List<Bvt002> getListRoles() {
		return listRoles;
	}


	/**
	 * @param listRoles the listRoles to set
	 */
	public void setListRoles(List<Bvt002> listRoles) {
		this.listRoles = listRoles;
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listRoles", listRoles);
	}
	
	
	
	

	/**
	 * @return the listRolesAdicionales
	 */
	public List<Bvt002> getListRolesAdicionales() {
		return listRolesAdicionales;
	}



	/**
	 * @param listRolesAdicionales the listRolesAdicionales to set
	 */
	public void setListRolesAdicionales(List<Bvt002> listRolesAdicionales) {
		this.listRolesAdicionales = listRolesAdicionales;
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listRolesAdicionales", listRolesAdicionales);
	}



	/**
	 * @return the vusuario
	 */
	public String getVusuario() {
		return vusuario;
	}



	/**
	 * @param vusuario the vusuario to set
	 */
	public void setVusuario(String vusuario) {
		this.vusuario = vusuario;
	}
	
	
	

	/**
	 * @return the filter
	 */
	public List<Bvt002> getFilter() {
		return filter;
	}



	/**
	 * @param filter the filter to set
	 */
	public void setFilter(List<Bvt002> filter) {
		this.filter = filter;
	}
	
	
	

	/**
	 * @return the rolfilter
	 */
	public String getRolfilter() {
		return rolfilter;
	}



	/**
	 * @param rolfilter the rolfilter to set
	 */
	public void setRolfilter(String rolfilter) {
		this.rolfilter = rolfilter;
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("rolfilter", rolfilter);
	}
	
	

	/**
	 * @return the vcodroladic
	 */
	public String getVcodroladic() {
		return vcodroladic;
	}



	/**
	 * @param vcodroladic the vcodroladic to set
	 */
	public void setVcodroladic(String vcodroladic) {
		this.vcodroladic = vcodroladic;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Variables seran utilizadas para capturar mensajes de errores de Oracle y parametros de metodos
	FacesMessage msj = null;
	PntGenerica consulta = new PntGenerica();
	boolean vGacc; //Validador de opciones del menó
	private int rows; //Registros de tabla
	private String login = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario"); //Usuario logeado
	private String vusuario = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("bcoduser"); //Usuario logeado
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	//Coneccion a base de datos
	//Pool de conecciones JNDI
	Connection con;
	PreparedStatement pstmt = null;
	ResultSet r;
	
	
	 /**
     * Inserta rol al crear un usuario con una instancia diferente.
     **/
    private void insertRol(String pinstancia, String pcodrol) {   	
       		
        try {
        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);
            con = ds.getConnection();
            
            String query = "insert into bvt003 select codrol, desrol, usrcre, feccre, usract, fecact," + pinstancia + " from bvt003 where codrol = '" + pcodrol + "'";
            pstmt = con.prepareStatement(query);
            //System.out.println(query);
            try {
                //Avisando
            	pstmt.executeUpdate();
             } catch (SQLException e)  {
            	e.printStackTrace();
            }            
            pstmt.close();
            con.close();
        } catch (Exception e) {
        	e.printStackTrace();
        }	
    }


	/**
     * Inserta Usuarios.
     **/
    private void insert()  {
    	String vlquery = "select coduser from bvt002 where coduser = '" + coduser.toUpperCase() + "' order by ?";
    	PntGenerica select = new PntGenerica();
    	select.selectGenerica(vlquery, JNDI);
    	int rows = select.getData().get(0).size();
    	if(rows>0){
    		msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("html49"), "");
    	} else {
    	//Valida que los campos no sean nulos
    		String[] veccodrol = b_codrol.split("\\ - ", -1);
    		String[] vecinst = instancia.split("\\ - ", -1);
    		
    		if(!vecinst[0].equals(instancia_insert)){
    			insertRol(vecinst[0], veccodrol[0]);
    		}
    		
        try {
        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);
            con = ds.getConnection();
            
            //Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
     		DatabaseMetaData databaseMetaData = con.getMetaData();
     		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
            
            //String query = "INSERT INTO Bvt002 VALUES (?,?,?,?,?,'" + getFecha() + "',?,'" + getFecha() + "',?,?)";
            //Solo proagro
            String query = "INSERT INTO Bvt002 VALUES (?,?,?,?,?," + getFecha(productName) + ",?," + getFecha(productName) + ",?,?)";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, coduser.toUpperCase().trim());
            pstmt.setString(2, desuser.toUpperCase());
            pstmt.setString(3, md.getStringMessageDigest(cluser, StringMD.SHA256));
            pstmt.setString(4, veccodrol[0].toUpperCase());
            pstmt.setString(5, login);
            pstmt.setString(6, login);
            pstmt.setString(7, mail.toLowerCase());
            pstmt.setInt(8, Integer.parseInt(vecinst[0]));

            ////System.out.println(query);

                //Avisando
            	pstmt.executeUpdate();
            	msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnInsert"), "");
           

            pstmt.close();
            con.close();
            
            //Insertando en passStore
            passStore.insertPassStore(coduser, md.getStringMessageDigest(cluser, StringMD.SHA256), vecinst[0]);
            
            limpiarValores();
        } catch (Exception e) {
        	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
        	//e.printStackTrace();
        }	
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
	       	
	        	String param = "'" + StringUtils.join(chkbox, "','") + "'";
	        	//Eliminando de PassStore
	            passStore.deletePassStore(param);
	            
	        	Context initContext = new InitialContext();     
	     		DataSource ds = (DataSource) initContext.lookup(JNDI);

	     		con = ds.getConnection();		
	     		
	        	
	        	String query = "DELETE from Bvt002 WHERE coduser in (" + param + ")";
	        		        	
	            pstmt = con.prepareStatement(query);
	            ////System.out.println(query);

	                pstmt.executeUpdate();
	                msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnDelete"), "");

	                
	            pstmt.close();
	            con.close();
	            
	            
	            limpiarValores();	
	        } catch (Exception e) {
	        	e.printStackTrace();
                msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
	        }
    	}
    	FacesContext.getCurrentInstance().addMessage(null, msj); 
    }
    
  

     /**
     * Actualiza Usuarios
     **/
    public void update()  {
    
   		String[] veccodrol = b_codrol.split("\\ - ", -1);
        try {

        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);

     		con = ds.getConnection();	
     		
     		if(instancia==""){
     			instancia = "0 - ";
     		}
     		String[] vecinst = instancia.split("\\ - ", -1);
     		
            String query = "UPDATE Bvt002";
             query += " SET DESUSER = '" + desuser.toUpperCase() + "'";
             query += " , B_CODROL = '" + veccodrol[0].toUpperCase() + "'";
             query += " , MAIL = '" + mail.toLowerCase() + "'";
             query += " , instancia = '" + vecinst[0] + "'";
             query += " WHERE CODUSER = '" + coduser.toUpperCase() + "'";
             
            //System.out.println(query);
            pstmt = con.prepareStatement(query);
                //Avisando
            	pstmt.executeUpdate();
                if(pstmt.getUpdateCount()==0){
                	msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("msnNoUpdate"), "");
                } else {
                	msj = new FacesMessage(FacesMessage.SEVERITY_INFO,  getMessage("msnUpdate"), "");
                }
                desuser = "";
           		cluser = "";
           		b_codrol = "";
           		cluserold = "";
                mail = "";	
                instancia = "";

            pstmt.close();
            con.close();

        } catch (Exception e) {
        	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            e.printStackTrace();       
	}
	FacesContext.getCurrentInstance().addMessage(null, msj); 
   }
    
    
    public void guardar() throws NamingException, SQLException{   	
    	if(validarOperacion==0){
    		insert();
    	} else {
    		update();
    	}
    }
    
    /**
     * Actualiza Usuarios
     **/
//    public void updatea()  {
//        try {
//        	Context initContext = new InitialContext();     
//     		DataSource ds = (DataSource) initContext.lookup(JNDI);
//
//     		con = ds.getConnection();	
//            //Class.forName(getDriver());
//            //con = DriverManager.getConnection(
//            //        getUrl(), getUsuario(), getClave());
//            String query = "UPDATE Bvt002";
//             query += " SET CLUSER = ?";
//             query += " WHERE CODUSER = ?";
//            pstmt = con.prepareStatement(query);
//            pstmt.setString(1, md.getStringMessageDigest(cluser, StringMD.SHA256));
//            pstmt.setString(2, login.toUpperCase());
//
//            	if(!cluser.equals(cluser1)){
//            		 msj = new FacesMessage(FacesMessage.SEVERITY_ERROR,  getMessage("bvt002Cl1Msj"), "");
//            	} else {
//                //Avisando
//                pstmt.executeUpdate();
//                msj = new FacesMessage(FacesMessage.SEVERITY_INFO,  getMessage("bvt002up"), "");
//               
//            	}
//
//            pstmt.close();
//            con.close();
//        } catch (Exception e) {
//        	e.printStackTrace();
//            msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
//        }
//    	FacesContext.getCurrentInstance().addMessage(null, msj); 
//    }
    
    /**
     * Leer Datos
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
        	   query += " (SELECT trim(A.CODUSER) , trim(A.DESUSER), trim(A.CLUSER), trim(A.B_CODROL), trim(B.DESROL), trim(a.mail), a.instancia||' - '||trim(c.descripcion)";
        	   query += " FROM Bvt002 A, BVT003 B, INSTANCIAS C " ;
        	   query += " WHERE A.B_CODROL=B.CODROL" ;
        	   query += " and A.instancia=b.instancia" ;
        	   query += " and A.instancia=c.instancia" ;
        	   query += " AND A.CODUSER like '" + coduser.toUpperCase() + "%'";
        	   query += " AND A.instancia like '" + instancia_insert + "%'";
        	   query += " AND A.CODUSER||A.DESUSER like '%" + ((String) filterValue).toUpperCase() + "%'";
	  		   query += " order by " + sortField + ") query";
	  		   query += " ) where rownum <= ?";
	           query += " and rn > (?)";
             break;
        case "PostgreSQL":
			   query += " SELECT trim(A.CODUSER) , trim(A.DESUSER), trim(A.CLUSER), trim(A.B_CODROL), trim(B.DESROL), trim(a.mail), a.instancia||' - '||trim(c.descripcion)";
			   query += " FROM Bvt002 A inner JOIN INSTANCIAS C ON a.INSTANCIA=c.INSTANCIA, BVT003 B" ;
			   query += " WHERE A.B_CODROL=B.CODROL " ;
			   query += " and A.INSTANCIA=B.INSTANCIA " ;
			   query += " AND cast(A.instancia as text) like '" + instancia_insert + "%'";
			   query += " AND A.CODUSER||A.DESUSER like '%" + ((String) filterValue).toUpperCase() + "%'";
			   query += " AND A.CODUSER like '" + coduser.toUpperCase() + "%'";
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
        Bvt002 select = new Bvt002();
     	select.setCoduser(r.getString(1));
     	select.setDesuser(r.getString(2));
        select.setCluser(r.getString(3));
        select.setb_codrol(r.getString(4) + " - " + r.getString(5));
        select.setDesrol(r.getString(5));
        select.setMail(r.getString(6));
        select.setInstancia(r.getString(7));
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

 	    String query = " SELECT trim(A.CODUSER) , trim(A.DESUSER), trim(A.CLUSER), trim(A.B_CODROL), trim(B.DESROL), trim(a.mail), a.instancia||' - '||trim(c.descripcion)";
 	    query += " FROM Bvt002 A, BVT003 B, INSTANCIAS C " ;
 	    query += " WHERE A.B_CODROL=B.CODROL" ;
 	    query += " and A.instancia=b.instancia" ;
 	    query += " and A.instancia=c.instancia" ;
 	    query += " AND A.CODUSER like '" + coduser.toUpperCase() + "%'";
 	    query += " AND A.instancia like '" + instancia_insert + "%'";
 	    query += " AND A.CODUSER||A.DESUSER like '%" + ((String) filterValue).toUpperCase() + "%' order by ?";
        
        
        String querypg = " SELECT trim(A.CODUSER) , trim(A.DESUSER), trim(A.CLUSER), trim(A.B_CODROL), trim(B.DESROL), trim(a.mail), a.instancia||' - '||trim(c.descripcion)";
        querypg += " FROM Bvt002 A inner JOIN INSTANCIAS C ON a.INSTANCIA=c.INSTANCIA, BVT003 B" ;
        querypg += " WHERE A.B_CODROL=B.CODROL " ;
        querypg += " and A.INSTANCIA=B.INSTANCIA " ;
        querypg += " AND cast(A.instancia as text) like '" + instancia_insert + "%'";
        querypg += " AND A.CODUSER||A.DESUSER like '%" + ((String) filterValue).toUpperCase() + "%'";
        querypg += " AND A.CODUSER like '" + coduser.toUpperCase() + "%' order by ?";
        
        consulta.selectGenericaMDB(query, querypg, JNDI);
        rows = consulta.getData().get(0).size();

  	}

        
    /**
     * Leer datos de Usuarios
     *<p> Parómetros del Mótodo: String coduser, String desuser.
      * * Fila desde y hasta para paginación, orden de la consulta.
     **/
	@SuppressWarnings("null")
	public void  selectBvt002a(String usuario, String orden, String pool) {

        try {
        	Context initContext = new InitialContext();
            DataSource ds = (DataSource) initContext.lookup(JNDI);
             
            Statement stmt = null;
            ResultSet rs;
            Connection con = ds.getConnection();
            
            
            String query = "SELECT CODUSER , DESUSER" +
           " FROM Bvt002" +
           " WHERE CODUSER = '" + usuario.toUpperCase() + "'" +
           " ORDER BY " + orden ;
            //System.out.println(query);
            try{
            rs = stmt.executeQuery(query);
            rows = 1;
 		    rs.last();
 		    rows = rs.getRow();
            //System.out.println(rows);

            ResultSetMetaData rsmd = rs.getMetaData();
        	columns = rsmd.getColumnCount();
 		    //System.out.println(columns);
        	arr = new String[rows][columns];

            int i = 0;
 		    rs.beforeFirst();
            while (rs.next()){
                for (int j = 0; j < columns; j++)
 				arr [i][j] = rs.getString(j+1);
 				i++;
               }
                    } catch (SQLException e) {
                    e.printStackTrace();
                }
            stmt.close();
            con.close();
            r.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     
     /**
      * Leer Datos de Usuarios
      
     public void selectLogin(String user, String pool) {       
         try {
        	 Context initContext = new InitialContext();
             DataSource ds = (DataSource) initContext.lookup(JNDI);
             
             Statement stmt;
             ResultSet rs;
             Connection con = ds.getConnection();
           //Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
      		DatabaseMetaData databaseMetaData = con.getMetaData();
      		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
             //Class.forName(getDriver());
             //con = DriverManager.getConnection(
             //        getUrl(), getUsuario(), getClave());
             stmt = con.createStatement(
                     ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY);
             String query = "";
             
             //System.out.println( productName );
             
             switch ( productName ) {
             case "Oracle":
            	 query = "SELECT trim(coduser), trim(cluser), trim(B_CODROL), trim(desuser), trim(mail), instancia";
                 query += " FROM BVT002";
                 query += " where coduser = '" + user.toUpperCase() + "'";
                  break;
             case "PostgreSQL":
            	 query = "SELECT trim(coduser), trim(cluser), trim(B_CODROL), trim(desuser), trim(mail), instancia";
                 query += " FROM BVT002";
                 query += " where coduser = '" + user.toUpperCase() + "'";
                  break;         		   
       		}
       		
             
            // System.out.println(query);
             try {
                 rs = stmt.executeQuery(query);
                 rows = 1;
                 rs.last();
                 rows = rs.getRow();
                 //System.out.println(rows);

                 ResultSetMetaData rsmd = rs.getMetaData();
                 columns = rsmd.getColumnCount();
                 //System.out.println(columns);
                 arr = new String[rows][columns];

                 int i = 0;
                 rs.beforeFirst();
                 while (rs.next()) {
                     for (int j = 0; j < columns; j++) {
                         arr[i][j] = rs.getString(j + 1);
                     }
                     i++;
                 }
             } catch (SQLException e) {
             }
             stmt.close();
             con.close();
             r.close();

         } catch (Exception e) {
             e.printStackTrace();
         }
     }**/

     /**
      * @return Retorna el arreglo
      **/
     public String[][] getArray(){
     	return arr;
     }

     /**
      * @return Retorna número de filas
      **/
     public int getRows(){
     	return rows;
     }
     /**
      * @return Retorna número de columnas
      **/
     public int getColumns(){
     	return columns;
     }

   	private void limpiarValores() {
 		// TODO Auto-generated method stub
   		coduser = "";
   		desuser = "";
   		cluser = "";
   		b_codrol = "";
   		cluserold = "";
   		mail = "";
   		instancia = "";
 	}
   	
   	
   	/**
     * Envía notificación a usuario al cambiar la contraseña
    **/
    private void ChangePassNotification2(String mail, String clave) {
    	try {
    
    		Properties props = new Properties();
    		props.put("mail.smtp.auth", mailsmtpauth);
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.port", "587");
            
            final String username = "gestorambientes@gmail.com";
            final String password = "Caracas2020";
            
            Session session = Session.getInstance(props,
            	      new javax.mail.Authenticator() {
            	      protected PasswordAuthentication getPasswordAuthentication() {
            	        return new PasswordAuthentication(username, password);
            	      }
            	      });
            
            
    			// Crear el mensaje a enviar
    			MimeMessage mm = new MimeMessage(session);
    			mm.setFrom(new InternetAddress(MAIL_ACCOUNT));
    			
    			
    			// Establecer las direcciones a las que será enviado
    			// el mensaje (test2@gmail.com y test3@gmail.com en copia
    			// oculta)
    			// mm.setFrom(new
    			// InternetAddress("opennomina@dvconsultores.com"));
    			mm.addRecipient(Message.RecipientType.TO,
    					new InternetAddress(mail));
	        
    			// Establecer el contenido del mensaje
    			mm.setSubject(getMessage("mailUserUserChgPass"));
    			mm.setText(getMessage("mailNewUserMsj2"));
    			
    			// use this is if you want to send html based message
                mm.setContent(
                		HEADER +  //Header del correo
    	                CONTENT_OPEN + //Abre contenido del correo
                		getMessage("mailNewUserMsj6") 
    	                + " " + coduser.toUpperCase() + " / " + clave + "<br/><br/> " 
                		+ getMessage("mailNewUserMsj2")
                		+ CONTENT_CLOSE + //Cierra contenido del correo
    	                FOOTER //Pié de página   	                
                		, "text/html; charset=utf-8");

                mm.saveChanges();
    			// Enviar el correo electrónico
    			Transport.send(mm);
    			//System.out.println("Correo enviado exitosamente");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
     }
   	
   	
   	//Recuperación de contraseñas
   	/**
     * Recuperación de contraseña por parte del usuario
     * @throws NamingException 
   	 * @throws ClassNotFoundException 
     * @throws IOException 
     **/ 
  	public void reqChgpass() {
  			try {
  	        	Context initContext = new InitialContext();     
  	        	DataSource ds = (DataSource) initContext.lookup(JNDI);
  	        	con = ds.getConnection();
  	        
  	        randomKey = "BIZ"+md.randomKey();
  	        	
  			String query = "UPDATE bvt002";
  			       query+= " set cluser = '" + md.getStringMessageDigest(randomKey.toUpperCase(), StringMD.SHA256) + "'";
  			       query+= " where coduser = '" +  coduser.toUpperCase() + "'";
  			pstmt = con.prepareStatement(query); 

  			int rows = 0;
  			
  		//System.out.println("Método");
  			PntGenerica consulta = new PntGenerica();
  			String query1 = "SELECT trim(coduser), trim(cluser), trim(B_CODROL), trim(desuser), trim(mail), instancia";
  	        query1 += " FROM BVT002";
  	        query1 += " where coduser = '" + coduser.toUpperCase() + "' order by ?";
  	        
  	        consulta.selectGenerica(query1, JNDI);
  	        rows = consulta.getData().get(0).size();

  			//System.out.println("Mail: " + vltabla[0][4].toLowerCase());
        	//System.out.println("RandomKey: " + randomKey);
            //Valida que exista el usuario
  			if (rows == 0) {
  				msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("noreg"), "");

  			} else {
  			pstmt.executeUpdate();

            	msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("chgpass7"), "");
            	ChangePassNotification2(consulta.getData().get(4).get(0).toLowerCase(), randomKey);
            	
            cluser= "";
  			}
  			
  			pstmt.close();
            con.close();
           // r.close();
            
  			} catch (SQLException | NamingException e) {
                e.printStackTrace();
                msj = new FacesMessage(FacesMessage.SEVERITY_FATAL,  e.getMessage(), "");
            }
      
  		FacesContext.getCurrentInstance().addMessage(null, msj); 
  	}


  	
  	/**
  	 * Retorna si el usuario tiene asignado algún rolList<Bvt002> listRoles = new ArrayList<Bvt002>(); adicional
  	 */
  	public String isRol(String pcodrol){
  		String valor = "fa fa-circle fa-2x text-danger";
  		Bvt002 result = null;
  		//carga lista de roles
  		if(listRolesAdicionales==null) {
  			listRolesAdicionales = new ArrayList<Bvt002>();
  		}
  		
  		result = listRolesAdicionales.stream().parallel()           //Recorre lista             
                .filter(x -> pcodrol.equals(x.getVcodroladic()))       //Compara con parametro
                .findAny()                                      //Retorna valor o null si no consigue
                .orElse(null);	
  		
  		if(result!=null){
  			valor = "fa fa-circle fa-2x text-success";
  		}
  		return valor;
  	}
  	

  	
  	/**
     * Selección de roles
     **/ 	
  	public void selectRoles() {
  		if(listRoles == null) {
  			listRoles = new ArrayList<Bvt002>();
  		}
  		
  		if(rolfilter == null) {
  			rolfilter = " - ";
  		}
  		
  		String[] veccodrol = rolfilter.split("\\ - ", -1);
  		if(listRoles.size()==0) {
  	    //Para mostrar en lista los roles
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
        	   query += " select codrol, desrol ";
         	   query += " FROM bvt003" ;
        	   query += " where instancia = '" + instancia_insert + "' and codrol like '" + veccodrol[0] + "%' order by ?";
             break;
        case "PostgreSQL":
	           query += " select codrol, desrol";
	      	   query += " FROM bvt003" ;
	      	   query += " where instancia = '" + instancia_insert + "' and codrol like '" + veccodrol[0] + "%' order by ?";
             break;    		
        }

        
        pstmt = con.prepareStatement(query);
        pstmt.setInt(1, 1);
        //System.out.println(query);
  		
        r =  pstmt.executeQuery();
        
        while (r.next()){
        Bvt002 select = new Bvt002();
     	select.setVcodrol(r.getString(1));
     	select.setVdesrol(r.getString(2));
        //Agrega la lista
        listRoles.add(select);
        }
        setListRoles(listRoles);
        
        //Cierra las conecciones
        pstmt.close();
        con.close();
        r.close();
        rolfilter = "";
        
		} catch (NamingException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
  	  }	
  	}
  	
  	
  	/**
     * Selección de roles
     **/ 	
  	public void selectRolesAdicionales() {
  		if(listRolesAdicionales == null) {
  			listRolesAdicionales = new ArrayList<Bvt002>();
  		}

  		if(listRolesAdicionales.size()==0) {
  	    //Para mostrar en lista los roles
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
        	   query += " select b_codrol from bvt008 where coduser = '" + vusuario.trim().toUpperCase() + "' and instancia = '" + instancia_insert + "' order by ?";
             break;
        case "PostgreSQL":
	           query += " select b_codrol from bvt008 where coduser = '" + vusuario.trim().toUpperCase() + "' and instancia = '" + instancia_insert + "' order by ?";
             break;    		
        }

        
        pstmt = con.prepareStatement(query);
        pstmt.setInt(1, 1);
        //System.out.println(query);
  		
        r =  pstmt.executeQuery();
        
        while (r.next()){
        Bvt002 select = new Bvt002();
     	select.setVcodroladic(r.getString(1));
     	
        //Agrega la lista
        listRolesAdicionales.add(select);
        }
        setListRolesAdicionales(listRolesAdicionales);
        
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
 	  * Filtrado de table
 	  */
 	  public void updatetbfilter() {
 		 listRoles.clear(); 		  
 		 listRoles = new ArrayList<Bvt002>();
 		 selectRoles();
 	  }
 	  
 	  
 	 /**
  	  * Filtrado de roles adicionales
  	  */
  	  public void updatetbfilter1(String vcoduser) {
  		 //setBcoduser(vcoduser); 
  	
  		 listRolesAdicionales = new ArrayList<Bvt002>();
  		 selectRolesAdicionales();
  	  }


  	
  	/**
     * Inserta roles adicionales.
     * <p>
     * Parámetros del Mátodo: String rol
     **/
    public void insert(String prol)  {
    	//System.out.println("INSTANCIA........................:" + instancia_insert);
    	if(vusuario=="") {
    		msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("htmlvalidate"), "");
    		FacesContext.getCurrentInstance().addMessage(null, msj); 
    	} else {
    	if(veriInsert(vusuario, prol.split(" - ")[0])) {
        try {
        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);
            con = ds.getConnection();
            
            //Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
     		DatabaseMetaData databaseMetaData = con.getMetaData();
     		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección

            String query = "INSERT INTO Bvt008 VALUES (?,?,?," + getFecha(productName) + ",?)";
            //System.out.println(query);
            //System.out.println("instancia: " + instancia_insert);
            //System.out.println("usuario: " + vusuario);
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, vusuario.trim().toUpperCase());
            pstmt.setString(2, prol.toUpperCase());
            pstmt.setString(3, login);
            pstmt.setInt(4, Integer.parseInt(instancia_insert));
                //Avisando
            	pstmt.executeUpdate();
            	msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnInsert"), "");
            
            pstmt.close();
            con.close();
            
            listRolesAdicionales.clear(); 		  
    		listRolesAdicionales = new ArrayList<Bvt002>();
    		selectRolesAdicionales();
            
        } catch (Exception e) {
        	 msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
        	e.printStackTrace();
        }	       
        FacesContext.getCurrentInstance().addMessage(null, msj); 
    	}
    	}
    }
    
    
    /**
     * Elimina roles adicionales.
     * <p>
     * Parámetros del Mátodo: String rol
     **/
    public void delete(String prol)  { 
    	if(veriDelete(vusuario, prol.split(" - ")[0])) {
	        try {
	       	
	        	Context initContext = new InitialContext();     
	     		DataSource ds = (DataSource) initContext.lookup(JNDI);

	     		con = ds.getConnection();		

	        	String query = "DELETE from Bvt008 WHERE b_codrol = '" + prol + "' and instancia = '" + instancia_insert + "' and coduser = '" + vusuario.trim().toUpperCase() + "'";
	        		        	
	            pstmt = con.prepareStatement(query);
	            ////System.out.println(query);
	                //Avisando
	                pstmt.executeUpdate();
	                msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnDelete"), "");

	                limpiarValores();	
	
	            pstmt.close();
	            con.close();
	            
	            listRolesAdicionales.clear(); 		  
	    		listRolesAdicionales = new ArrayList<Bvt002>();
	    		selectRolesAdicionales();
	
	        } catch (Exception e) {
	            e.printStackTrace();
	            msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
	        
    	}
    	FacesContext.getCurrentInstance().addMessage(null, msj); 
    	}
    }
    
    
    /**
	 * Verificar si ya existe el reporte y retornar el mensaje para inserción
	 */
	private Boolean veriInsert(String pcoduser, String pcodrol) {
		Boolean retorno = true;
		String query = "select b_codrol from bvt008 where coduser = '" + pcoduser + "' and b_codrol = '" + pcodrol + "' order by ?";
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
	private Boolean veriDelete(String pcoduser, String pcodrol) {
		Boolean retorno = true;
		String query = "select b_codrol from bvt008 where coduser = '" + pcoduser + "' and b_codrol = '" + pcodrol + "' order by ?";
		PntGenerica consulta = new PntGenerica();
		consulta.selectGenerica(query, JNDI);
		int row = consulta.getData().get(0).size();
		if(row == 0) {
			retorno = false;
		}
		return retorno;
	}
    
    /**
     * Limpiar panel filtro
     */
    public void reset() {
     rolfilter = "";
     setRolfilter("");
     updatetbfilter();
    }
    
    
	////////////////////////////////////////VARIABLES GENERALES PARA HEAD-CONTENT-FOOTER DE CORREOS///////////////
	final String HEADER = "<div style='background: #3498db; font-size: 15px'><b><center>" +  getMessage("htmlinfo") + "</center></b></div>";
	final String CONTENT_OPEN = "<div style='margin-top: 2px; border: 1px solid #F2F2F2; border-top-color:#ffdf64; border-top-width: 2px; padding-bottom: 4px; padding-left: 4px;'><br/>";
    final String CONTENT_CLOSE = "</div></center>";
    final String FOOTER = "<div style='background: #161616;color: #777;height:20px; padding-top: 8px; margin-top: 1px; font-size: 10px'>Copyright DvConsultores 2016 | Todos los derechos reservados</div>";

}
