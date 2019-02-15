package org.openbizview.util;

import java.io.Serializable;

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


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;



@ManagedBean(name = "smnubean")
@ViewScoped
public class SeguridadMenuBean extends Bd implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	public SeguridadMenuBean() {

	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////OPCIONES DE SEGURIDAD DEL MENU
	// BASICO/////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	PntGenerica consulta = new PntGenerica();
	private String login = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario"); //Usuario logeado
	private String rol = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("rol"); //Usuario logeado
	
	HttpServletRequest rq = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	@SuppressWarnings("unchecked")
	public
	List<SeguridadMenuBean> listMenu = (List<SeguridadMenuBean>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("listMenu"); 
	@SuppressWarnings("unchecked")
	List<SeguridadMenuBean> listBoton = (List<SeguridadMenuBean>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("listBoton"); 
	String rendered = "true";
	
	public String vistaMenu = "true", opcMenu;
	String vistaBoton = "true", opcBoton;
	//
	Connection con;
	PreparedStatement pstmt = null;
    ResultSet r;
    int pos = 0;
    
	
	
	/**
	 * @return the listMenu
	 */
	public List<SeguridadMenuBean> getListMenu() {
		return listMenu;
	}

	/**
	 * @param listMenu the listMenu to set
	 */
	public void setListMenu(List<SeguridadMenuBean> listMenu) {
		this.listMenu = listMenu;
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listMenu", listMenu);
	}

	/**
	 * @return the listBoton
	 */
	public List<SeguridadMenuBean> getListBoton() {
		return listBoton;
	}

	/**
	 * @param listBoton the listBoton to set
	 */
	public void setListBoton(List<SeguridadMenuBean> listBoton) {
		this.listBoton = listBoton;
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listBoton", listBoton);
	}


	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}
	
	

	/**
	 * @return the vistaMenu
	 */
	public String getVistaMenu() {
		return vistaMenu;
	}

	/**
	 * @param vistaMenu the vistaMenu to set
	 */
	public void setVistaMenu(String vistaMenu) {
		this.vistaMenu = vistaMenu;
	}

	/**
	 * @return the vistaBoton
	 */
	public String getVistaBoton() {
		return vistaBoton;
	}

	/**
	 * @param vistaBoton the vistaBoton to set
	 */
	public void setVistaBoton(String vistaBoton) {
		this.vistaBoton = vistaBoton;
	}
	
	

	/**
	 * @return the opcMenu
	 */
	public String getOpcMenu() {
		return opcMenu;
	}

	/**
	 * @param opcMenu the opcMenu to set
	 */
	public void setOpcMenu(String opcMenu) {
		this.opcMenu = opcMenu;
	}

	/**
	 * @return the opcBoton
	 */
	public String getOpcBoton() {
		return opcBoton;
	}

	/**
	 * @param opcBoton the opcBoton to set
	 */
	public void setOpcBoton(String opcBoton) {
		this.opcBoton = opcBoton;
	}

	/**
	 * Lee las opciones para el menú de acceso se carga una sola vez en session
	 * Con este método evitamos estar leyendo constantemete desde la base de datos
	 * NUeva opción versión  de Openbizview
	 */
	public void opcmnu() {
		if(listMenu==null) { listMenu = new ArrayList<>();}
		if(listMenu.size()==0) {
		Context initContext;
		try {
			initContext = new InitialContext();
		   
 		DataSource ds = (DataSource) initContext.lookup(JNDI);
 		con = ds.getConnection();
 		
		String vlquery = "select trim(codvis), trim(codopc) ";
               vlquery += " from bvtmenu";
			   vlquery += " where b_codrol  = ? order by codopc";
		
	    pstmt = con.prepareStatement(vlquery);
		pstmt.setString(1, rol);	
		
		r =  pstmt.executeQuery();
		
		while (r.next()){
        	SeguridadMenuBean select = new SeguridadMenuBean();
     		select.setVistaMenu(r.getString(1));
     		select.setOpcMenu(r.getString(2));
        	//Agrega la lista
        	listMenu.add(select);
        }
		setListMenu(listMenu);
		
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
	 * Lee las opciones para el menú de acceso se carga una sola vez en session
	 * Con este método evitamos estar leyendo constantemete desde la base de datos
	 * NUeva opción versión  de Openbizview
	 */
	protected void opcbot() {
		if(listBoton==null) { listBoton = new ArrayList<>();}
		Context initContext;
		try {
			initContext = new InitialContext();
		   
 		DataSource ds = (DataSource) initContext.lookup(JNDI);
 		con = ds.getConnection();
 		
 		String vlquery = "select trim(codvis), trim(codopc) ";
        vlquery += " from bvt005";
		vlquery += " where b_codrol = ? order by codopc";
		
	    pstmt = con.prepareStatement(vlquery);
		pstmt.setString(1, rol);	
		
		r =  pstmt.executeQuery();
		
		while (r.next()){
        	SeguridadMenuBean select = new SeguridadMenuBean();
     		select.setVistaBoton(r.getString(1));
     		select.setOpcBoton(r.getString(2));
        	//Agrega la lista
        	listBoton.add(select);
        }
		setListBoton(listBoton);
		
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
	 * Lee las opciones y retorna la posición
	 * 
	 * @return int
	 */
	public int opcmnu(String nummenu) {
		IntStream streamMenu = IntStream.range(0,listMenu.size());		
		streamMenu.parallel().forEach(x -> {
			 if(listMenu.get(x).opcMenu.equals(nummenu)) {
				 pos = x;
			 }
			});
		streamMenu.close();
		return pos;	 
	}

	/**
	 * Lee las opciones del menú para acceso totones
	 * 
	 * @return true o false
	 */
	public String opcbot(String opc)  {
		IntStream streamBoton = IntStream.range(0,listBoton.size());
		//Recorrer opciones de seguridad y salir si no tiene acceso
		//Recorre todo el menú de la lista de sesión y por opción verifica
		streamBoton.parallel().forEach(x -> {
		 if(listBoton.get(x).opcBoton.equals(opc) && listBoton.get(x).vistaBoton.equals("1")) {
			 rendered = "false";
		 }
		});
		streamBoton.close();
		return rendered;
	}

		

}
