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



	// Constructor	
	public Bd()  {
    }
	

	
    //Declaracion de variables para manejo de mensajes multi idioma y pais
    private String lenguaje = "es";
    private String pais = "VEN";
    private Locale  localidad = new Locale(lenguaje, pais);
    //ResourceBundle recursos =  ResourceBundle.getBundle("org.openbizview.util.MessagesBundle",localidad);
    private String Message = "";
    protected String productName; //Manejador de base de datos
    private String valorCookie;
    @SuppressWarnings("unused")
	private Locale OsLang = Locale.getDefault();
    
    
 
	//Declaracion de variables y manejo de mensajes
    String userLang = System.getProperty("user.language");//Identifica el lenguaje el OS
    String userCountry = System.getProperty("user.country");//Identifica el pais el OS
    protected Locale locale = new Locale(userLang, userCountry);//Identifica idioma y pais, por defecto le colocamos ven
    java.util.Date fecact = new java.util.Date();
    //Para trabajar con quartz properties, por alguna razón no funciona con external context
    static FacesContext ctx = FacesContext.getCurrentInstance();
    public static final String JNDI = ctx.getExternalContext().getInitParameter("JNDI_BD"); //"jdbc/orabiz"; //Nombre del JNDI
	static final String JNDIMAIL = ctx.getExternalContext().getInitParameter("JNDI_MAIL"); //"jdbc/orabiz"; //Nombre del JNDI
    protected static final String BIRT_VIEWER_WORKING_FOLDER = ctx.getExternalContext().getInitParameter("BIRT_VIEWER_WORKING_FOLDER");//Url logout
    protected static final String BIRT_VIEWER_LOG_DIR = ctx.getExternalContext().getInitParameter("BIRT_VIEWER_LOG_DIR");//Url logout
    protected static final String OPENBIZVIEW_BD_LANG = ctx.getExternalContext().getInitParameter("OPENBIZVIEW_BD_LANG");//Localización
    static final String MAIL_ACCOUNT = ctx.getExternalContext().getInitParameter("MAIL_ACCOUNT");//Chequea actualizaciones
    java.text.SimpleDateFormat sdfecha = new java.text.SimpleDateFormat("dd/MM/yyyy", locale );
    String fecha = sdfecha.format(fecact); //Fecha formateada para insertar en tablas
    
    //Variables para uso internos de servlet
    private static final String PARAMINFOA = "dirUploadFiles"; //Uploads
    private static final String PARAMINFOB = "dirUploadRep"; //Uploads
    
   
    
	
    /**
	 * @return the openbizviewBdLang
	 */
	public static String getOpenbizviewBdLang() {
		return OPENBIZVIEW_BD_LANG;
	}
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
	 * @return the openbizviewLocale
	 */
	public static String getOpenbizviewLocale() {
		return OPENBIZVIEW_BD_LANG;
	}
	
	/**
     * Obtiene la fecha del dia formateada, ya que se va a utilizar en todas la tablas
     * se crea el metodo.
     * @throws IOException 
     */
    public String getFecha(){
    	java.text.SimpleDateFormat sdfecha_es = new java.text.SimpleDateFormat("dd/MM/yyyy", locale );
    	java.text.SimpleDateFormat sdfecha_en = new java.text.SimpleDateFormat("dd/MMM/yyyy", locale );
    	if(OPENBIZVIEW_BD_LANG=="es"){
    		fecha = sdfecha_es.format(fecact);
    	} else {
    		fecha = sdfecha_en.format(fecact);
    	}
        return fecha;
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
     * Obtiene la fecha del dia formateada con hora y minutos, ya que se va a utilizar en todas la tablas
     * se crea el metodo.
     * @throws IOException 
     */
    public String getFechaHora(){
    	java.text.SimpleDateFormat sdfecha_es = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", locale );
    	java.text.SimpleDateFormat sdfecha_en = new java.text.SimpleDateFormat("dd/MMM/yyyy HH:mm", locale );
    	if(OPENBIZVIEW_BD_LANG.equals("es")){
    		fecha = sdfecha_es.format(fecact);
    	} else {
    		fecha = sdfecha_en.format(fecact);
    	}
        return fecha;
    }
      

	
	/**
     * Formatea la fecha leyendo el formato desde xml de configuración
     * @param La fecha a cargar 
     * @throws IOException 
     */
    public String getFechaFormat(Date pfecha) throws IOException {
    	FacesContext ctx = FacesContext.getCurrentInstance();
    	String ff =
                ctx.getExternalContext().getInitParameter("FORMAT_DATE");
    	java.text.SimpleDateFormat sdfecha = new java.text.SimpleDateFormat(ff, locale );    	 
        return sdfecha.format(pfecha);
    }
    
    

 
    /**
     * Recursos de lenguaje. Archivos Properties
     **/
    public String getMessage(String mensaje) {
    	if(getBundle()){
    		lenguaje = "es";
    		pais = "VEN";
    	} else {
    		lenguaje = "en";
    		pais = "US";        		
    	}
    	////System.out.println(localidad);
    	localidad = new Locale(lenguaje, pais);
    	ResourceBundle recursos =  ResourceBundle.getBundle("org.openbizview.util.MessagesBundle",localidad);
        Message = recursos.getString(mensaje);
        return Message;
    }
    
    
    /**
     * Toma el valor de las cookie
     */
	public void getCookie(String name) {
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
	public Boolean getBundle(){
    	getCookie("obv_language");   	
    	//Si el valor asignado del cookie es null entonces carga el idioma del browser
    	FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
    	Locale currentLocale = request.getLocale();
    	if(valorCookie==null){
        	valorCookie = currentLocale.getLanguage().substring(0, 2);
        }
    	////System.out.println(valorCookie.substring(0, 2));
    	//Continúa su validación
    	if(valorCookie.substring(0, 2).equals("es")){
    		return true;
    	} else {
    		return false;
    	}
    }
	
	
	/**
	 * Retorna el tipo de fa (icono) para español
	 * Toma por defecto el que trae la máquina
	 * @return
	 */
	public String getFaEs(){
    	getCookie("obv_language");   	
    	//Si el valor asignado del cookie es null entonces carga el idioma del browser
    	FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
    	Locale currentLocale = request.getLocale();
    	if(valorCookie==null){
        	valorCookie = currentLocale.getLanguage();
        }
    	////System.out.println(valorCookie);
    	//Continúa su validación
    	if(valorCookie.substring(0, 2).equals("es")){
    		return "fa fa-check";
    	} else {
    		return "";
    	}
    }
	
	
	
	/**
	 * Retorna el tipo de fa (icono) para inglés
	 * Toma por defecto el que trae la máquina
	 * @return
	 */
	public String getFaEn(){
    	getCookie("obv_language");   	
    	//Si el valor asignado del cookie es null entonces carga el idioma del browser
    	FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
    	Locale currentLocale = request.getLocale();
    	if(valorCookie==null){
        	valorCookie = currentLocale.getLanguage();
        }
    	////System.out.println(valorCookie);
    	//Continúa su validación
    	if(valorCookie.substring(0, 2).equals("es")){
    		return "";
    	} else {
    		return "fa fa-check";
    	}
    }


	
	
	

	
}




