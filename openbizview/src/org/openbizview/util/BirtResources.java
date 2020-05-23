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

import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author Andres
 */
public class BirtResources  {
//Declaración de variables para manejo de mensajes multi idioma y país
	private String lenguaje = "es";
    private String pais = "VEN";
    private Locale  localidad = new Locale(lenguaje, pais);
    ResourceBundle BirtResources =  ResourceBundle.getBundle("org.openbizview.util.MessagesBirt",localidad);
    private String Message = "";
    

    

	/**
     * Recursos de lenguaje. Archivos Properties. Integración con birt
     **/
    public  String getMessage(String mensaje, String valorCookie) {
    	if(valorCookie.substring(0, 2).equals("es")){
    		lenguaje = "es";
    		pais = "VEN";
    	} else {
    		lenguaje = "en";
    		pais = "US";        		
    	}
    	//System.out.println(localidad);
    	localidad = new Locale(lenguaje, pais);
    	BirtResources =  ResourceBundle.getBundle("org.openbizview.util.MessagesBirt",localidad);
        Message = BirtResources.getString(mensaje);
        return Message;
    }

    public  String getHtmlMessage(String mensaje, String valorCookie) {
    	if(valorCookie.substring(0, 2).equals("es")){
    		lenguaje = "es";
    		pais = "VEN";
    	} else {
    		lenguaje = "en";
    		pais = "US";        		
    	}
    	//System.out.println(localidad);
    	localidad = new Locale(lenguaje, pais);
    	BirtResources =  ResourceBundle.getBundle("org.openbizview.util.MessagesBirt",localidad);
        Message = BirtResources.getString(mensaje);
        return Message;
    }

     public  String getJavaScriptMessage(String mensaje, String valorCookie) {
    	 if(valorCookie.substring(0, 2).equals("es")){
     		lenguaje = "es";
     		pais = "VEN";
     	} else {
     		lenguaje = "en";
     		pais = "US";        		
     	}
     	//System.out.println(localidad);
     	localidad = new Locale(lenguaje, pais);
     	BirtResources =  ResourceBundle.getBundle("org.openbizview.util.MessagesBirt",localidad);
         Message = BirtResources.getString(mensaje);
        Message = BirtResources.getString(mensaje);
        return Message;
    }

}
