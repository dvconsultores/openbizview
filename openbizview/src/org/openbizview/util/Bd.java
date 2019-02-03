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
import java.util.*;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


/**
 * Crea Usuarios y clave de Base de Datos para todos los programas
 */
@ManagedBean
@RequestScoped
public class Bd  {

	
    //Declaracion de variables para manejo de mensajes multi idioma y pais
    protected static String productName; //Manejador de base de datos  
    private static String lenguaje = "es";
    private static String pais = "VEN";
    private static String valorCookie;
    private static String Message = "";
    private static Locale  localidad = new Locale(lenguaje, pais);
    //Para trabajar con quartz properties, por alguna razón no funciona con external context
    static FacesContext ctx = FacesContext.getCurrentInstance();
    public static final String JNDI = ctx.getExternalContext().getInitParameter("JNDI_BD"); //"jdbc/orabiz"; //Nombre del JNDI
	static final String JNDIMAIL = ctx.getExternalContext().getInitParameter("JNDI_MAIL"); //"jdbc/orabiz"; //Nombre del JNDI
    static final String MAIL_ACCOUNT = ctx.getExternalContext().getInitParameter("MAIL_ACCOUNT");//Chequea actualizaciones
    
    //Variables para uso internos de servlet
    private static final String PARAMINFOA = "dirUploadFiles"; //Uploads
    private static final String PARAMINFOB = "dirUploadRep"; //Uploads

    /**
	 * @return the paraminfob
	 */
	public static String getPARAMINFOB() {
		return PARAMINFOB;
	}
/**
	 * @return the paraminfoa
	 */
	public static String getPARAMINFOA() {
		return PARAMINFOA;
	}
	
    
    /**
     * Obtiene la fecha según el tipo de base de datos
     * @throws IOException
     */
    public String getFecha(String productName){  
    	String fechabd = null;
    	switch ( productName ) {
        case "Oracle":
        	fechabd = "sysdate";
        break;
        case "PostgreSQL":
        	fechabd = "now()";
             break;
        }
        return fechabd;
    }
    
 
    /**
     * Recursos de lenguaje. Archivos Properties
     **/
    public static String getMessage(String mensaje) {
    	if(getBundle()){
    		lenguaje = "es";
    		pais = "VEN";
    	} else {
    		lenguaje = "en";
    		pais = "US";        		
    	}
    	//System.out.println(localidad);
    	localidad = new Locale(lenguaje, pais);
    	ResourceBundle recursos =  ResourceBundle.getBundle("org.openbizview.i18n.messages",localidad);
        Message = recursos.getString(mensaje);
        return Message;
    }
    
    
    /**
     * Toma el valor de las cookie
     */
	public static void getCookie(String name) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        Cookie[] cookies = request.getCookies();  
        if (cookies != null) {      	
        	for (Cookie cookie : cookies) {
        		if (cookie.getName().equals(name)) {
        			valorCookie = cookie.getValue();
        		}
        	}
        }
      }
	
	
      
	/**
	 * Retorna true o false dependiendo del idioma del equipo.
	 * Toma por defecto el que trae la máquina
	 * @return
	 */
	public static Boolean getBundle(){
    	getCookie("obv_language");   	
    	//Si el valor asignado del cookie es null entonces carga el idioma del browser
    	FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
    	Locale currentLocale = request.getLocale();
    	if(valorCookie==null){
        	valorCookie = currentLocale.getLanguage().substring(0, 2);
        }
    	//System.out.println(valorCookie.substring(0, 2));
    	//Continúa su validación
    	if(valorCookie.substring(0, 2).equals("es")){
    		return true;
    	} else {
    		return false;
    	}
    }
	
	

	
}




