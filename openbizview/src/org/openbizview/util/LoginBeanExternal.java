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

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.quartz.SchedulerException;

@ManagedBean
@ViewScoped
public class LoginBeanExternal extends Bd {
	
	@PostConstruct
	public void init() {
		System.out.println("inicio");
	}

    FacesMessage msj = null;
    HttpSession sesionOk;
    HttpServletRequest rq = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    private String key = "";
    //
    String url = rq.getRequestURL().toString();
	String baseURL = url.substring(0, url.length() - rq.getRequestURI().length()) + rq.getContextPath() + "/";
    
    
	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}


	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}




	/**
	 * Leer Datos de Usuarios
	 * <p>
	 * Se conecta a la base de datos y valida el usuario y la contraseña.
	 * Adicionalmente valida si el usuario existe ne la base de datos.
	 * **/
	public void loginExternal()   {
		//System.out.println("key: " + key);
		
		String[] vecKey = key.split("\\|)", -1);
		String vlquery = "Select " + vecKey[4] + " from " + vecKey[5] + " where " + vecKey[4] + " = '" + vecKey[0] + "' and " + vecKey[6] + " = '" + vecKey[7] + "' order by ?";
		//System.out.println(vlquery);
		
		PntGenerica consulta = new PntGenerica();
		//Consulta contra base de datos externa
		consulta.selectGenerica(vlquery, JNDI_EXTERNAL);

		int rows = 0;

		rows = consulta.getData().get(0).size();
		//System.out.println("Método");
		
		if (rows == 0) {
			RequestContext.getCurrentInstance().execute("PF('alerta').show()");
		}
		
		//Se asignan a dos variables String ya que retorna un arreglo y debe convertirse a String
		// y se convierte en mayúscula
		if(rows>0){
		String vLusuario = vecKey[0].toUpperCase();
		//System.out.println("Rows: " + rows);
		//System.out.println("Clave: " + vLclave);
		
		//Valida que usuario y claves sean los mismos, realiza el login y crea la variable de session
		if(vecKey[0].equals(vLusuario)){
			//System.out.println("Usuario y contraseña correctos");

			//Creando la variable de session	
			sesionOk = ( HttpSession ) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
			sesionOk.setAttribute("usuario", vecKey[0].toUpperCase());
			//sesionOk.setMaxInactiveInterval(getSession());
			String sessionId = sesionOk.getId();
			//FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario", usuario);
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("login", vecKey[0].toUpperCase());
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("rol", vecKey[2].toString());
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("desuser", vecKey[1]);
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("session", sessionId);
			//Instancia
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("instancia", vecKey[3]);
			//Se genera opción de logout
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("logoutEstandart", "1");
			
			//Genera las opciones de seguridad y guarda en lista de sesión
			SeguridadMenuBean opcseg = new SeguridadMenuBean();
			opcseg.opcbot(); //Acceso a botones
			opcseg.opcmnu(); //Acceso a menú
			
			try {
				new Programacion().recuperarTriggers("0");
			} catch (SchedulerException | NamingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect(baseURL + "ct/openbizview.xhtml");
	
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		} 
		
		}//Fin if valida que sema mayor a cero (0)
	}


}
