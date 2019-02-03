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

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import org.quartz.SchedulerException;

@ManagedBean
@ViewScoped
public class LoginBean {

	private String usuario;
	private String clave;
	FacesMessage msj = null;
	HttpSession sesionOk;
	HttpServletRequest rq = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	StringMD md = new StringMD(); // Objeto encriptador
	String logged = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
	String loggedUsr = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("desuser");
	//
	String url = rq.getRequestURL().toString();
	String baseURL = url.substring(0, url.length() - rq.getRequestURI().length()) + rq.getContextPath() + "/";

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(final String usuario) {
		this.usuario = usuario.toUpperCase();
	}

	public String getClave() {
		return clave;
	}

	public void setClave(final String clave) {
		this.clave = clave;
	}


	/**
	 * Leer Datos de Usuarios
	 * <p>
	 * Se conecta a la base de datos y valida el usuario y la contraseña.
	 * Adicionalmente valida si el usuario existe ne la base de datos.
	 **/
	public void login() {
		int rows = 0;
		if (sesionOk != null) {
			sesionOk.invalidate();
			FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		}
		// System.out.println("Método");
		PntGenerica consulta = new PntGenerica();
		String query = "SELECT trim(coduser), trim(cluser), trim(B_CODROL), trim(desuser), trim(mail), instancia";
		query += " FROM BVT002";
		query += " where coduser = '" + usuario.toUpperCase() + "' order by ?";

		consulta.selectGenerica(query, Bd.JNDI);
		rows = consulta.getData().get(0).size();

		if (rows == 0) {
			msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, Bd.getMessage("noreg"), "");
			FacesContext.getCurrentInstance().addMessage(null, msj);
			usuario = "";
		}

		// Se asignan a dos variables String ya que retorna un arreglo y debe
		// convertirse a String
		// y se convierte en mayúscula
		if (rows > 0) {
			String vLusuario = consulta.getData().get(0).get(0);
			String vLclave = consulta.getData().get(1).get(0);
			// System.out.println("Usuario: " + vLusuario);
			// System.out.println("Clave: " + vLclave);

			// Valida que usuario y claves sean los mismos, realiza el login y crea la
			// variable de session
			if (usuario.equals(vLusuario) && !md.getStringMessageDigest(clave, StringMD.SHA256).equals(vLclave)) {
				// System.out.println(getMessage("logCl"));
				msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, Bd.getMessage("logCl"), "");
				FacesContext.getCurrentInstance().addMessage(null, msj);

			} else if (usuario.equals(vLusuario) && md.getStringMessageDigest(clave, StringMD.SHA256).equals(vLclave)) {
				// System.out.println("Usuario y contraseña correctos");

				// Creando la variable de session
				sesionOk = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
				sesionOk.setAttribute("usuario", usuario);

				// FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario",
				// usuario);
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("login",
						usuario.toUpperCase());
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("rol",
						consulta.getData().get(2).get(0));
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("desuser",
						consulta.getData().get(3).get(0));
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("instancia",
						consulta.getData().get(5).get(0));

				// Genera las opciones de seguridad y guarda en lista de sesión
				SeguridadMenuBean opcseg = new SeguridadMenuBean();
				opcseg.opcbot(); // Acceso a botones
				opcseg.opcmnu(); // Acceso a menú

				// Recuperar trigger
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

		} // Fin if valida que sema mayor a cero (0)
	}


	/**
	 * Invalida la session y sale de la aplicación
	 * 
	 * @return String
	 */
	public void logout() {
		// Invalida la session
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		if (sesionOk != null) {
			sesionOk.invalidate();
		}
		try {
		 FacesContext.getCurrentInstance().getExternalContext().redirect(baseURL + "/login.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
		e.printStackTrace();
		}
	}

	/**
	 * Retorna el nombre de la sesión activa. Si la sesión es null, entonces llama
	 * al método logout que la invalida redirecciona al logout, retornando blanco.
	 * 
	 * @return String
	 */
	public String getLogged() { // System.out.println("Usuario Logeado:" + logged);
		if (!rq.isRequestedSessionIdValid() || logged == null) {
			// rq.getCurrentInstance().execute("PF('yourdialogid').show()");
			// RequestContext.getCurrentInstance().execute("PF('idleDialog').show()");
			logout();
			return null;
		} else {
			return logged.toLowerCase();
		}
	}

	/**
	 * Retorna el nombre de la sesión activa. Si la sesión es null, entonces llama
	 * al método logout que la invalida redirecciona al logout, retornando blanco.
	 * 
	 * @return String
	 */
	public String getLoggedUsr() throws IOException { // System.out.println("Usuario Logeado:" + login);
		if (!rq.isRequestedSessionIdValid() || logged == null) {
			// rq.getCurrentInstance().execute("PF('yourdialogid').show()");
			// RequestContext.getCurrentInstance().execute("PF('idleDialog').show()");
			logout();
			return null;
		} else {
			return loggedUsr.toUpperCase();
		}
	}

}
