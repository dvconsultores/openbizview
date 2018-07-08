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


import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author Andres
 */
	@ManagedBean
	@ViewScoped
	public class Chgpwg extends Bd implements Serializable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		


	

	private String cluser = "";
	private String cluser1 = "";
	FacesMessage msj = null;
	//Cambio de password
	StringMD md = new StringMD();
	private String login = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario"); //Usuario logeado
	private String coduser = "";	
	PntGenerica consulta = new PntGenerica();
	private String instancia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instancia");
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
	 * @return the msj
	 */
	public FacesMessage getMsj() {
		return msj;
	}

	/**
	 * @param msj the msj to set
	 */
	public void setMsj(FacesMessage msj) {
		this.msj = msj;
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
	

	
	//Coneccion a base de datos
	//Pool de conecciones JNDI
	Connection con;
	PreparedStatement pstmt = null;
	ResultSet r;
	
	
    
    /**
     * Actualiza Usuarios
     **/
    public void updatea() throws  NamingException {
        try {
        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);
     		consulta.getData().clear();

     		con = ds.getConnection();	
            //Class.forName(getDriver());
            //con = DriverManager.getConnection(
            //        getUrl(), getUsuario(), getClave());
            String query = "UPDATE Bvt002";
             query += " SET CLUSER = ?";
             query += " WHERE CODUSER = ?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, md.getStringMessageDigest(cluser, StringMD.SHA256));
            pstmt.setString(2, login.toUpperCase());
            
            String passStore = "Select count(*) from PASS_STORE";
            	   passStore += " where coduser = '" + login.toUpperCase() + "'";
            	   passStore += " and cluser = '" + md.getStringMessageDigest(cluser, StringMD.SHA256) + "' order by ?";
            	consulta.selectGenerica(passStore, JNDI);   
                //System.out.println(consulta.getData().get(0).get(0));
            	if(!cluser.equals(cluser1)){
            		 msj = new FacesMessage(FacesMessage.SEVERITY_ERROR,  getMessage("bvt002Cl1Msj"), "");
            	} else if (Integer.parseInt(consulta.getData().get(0).get(0)) > 0){
            		 msj = new FacesMessage(FacesMessage.SEVERITY_ERROR,  getMessage("html61"), "");
            	} else {
                //Avisando
                pstmt.executeUpdate();
              //Insertando en passStore
                new PassStore().insertPassStore(login, md.getStringMessageDigest(cluser, StringMD.SHA256), instancia);
                msj = new FacesMessage(FacesMessage.SEVERITY_INFO,  getMessage("bvt002up"), "");
                
            	}

            pstmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    	FacesContext.getCurrentInstance().addMessage(null, msj); 
    }
    
   
   	

   	
  
  
    

}
