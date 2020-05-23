package org.openbizview.util;

import java.util.stream.IntStream;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;



@ManagedBean
@ViewScoped
public class AccesPivot extends LoginBean {
	
	
	private String login = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario"); //Usuario logeado
	private String instancia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instancia"); //Usuario logeado
	////
	SeguridadMenuBean segu = new SeguridadMenuBean();
	
	public void init() {
		//Si no tiene acceso al módulo no puede ingresar
		if(login==null) {
	    	 new LoginBean().logout();
	     } else {	
		if (instancia == null){instancia = "999990817832122222";}
		IntStream streamMenu = IntStream.range(0,segu.listMenu.size());
		
		//Recorrer opciones de seguridad y salir si no tiene acceso
		//Recorre todo el menú de la lista de sesión y por opción verifica
		streamMenu.forEach(x -> {
			if(segu.listMenu.get(x).opcMenu.equals("R04") && segu.listMenu.get(x).vistaMenu.equals("1")) {
				logout();
			}
		});
	     }
	}

}
