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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

public class Xml implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Xml(){
		
	}
	
	   
    /**
     * Lee Archivo desde un XML. Indica el formato date.
     * <p>
     * Parámetros del Método: Ruta del XML.
     **/
    public  String XmlDate(File ruta) throws  IOException{
    	
    	String indicator = "";
        FileReader fr = new FileReader(ruta + File.separator + "configFormat.xml");
        BufferedReader bf = new BufferedReader(fr);

     //A partir de aquí trabajamos con el JDOM para lectura del xml
     try {
        @SuppressWarnings("deprecation")
		SAXBuilder builder=new SAXBuilder(true);
        //usar el parser Xerces y no queremos
        //que valide el documento
        //Abre el documento, el método build que lee de un InputStreamReader
        Document doc=builder.build(bf);
        //construyo el arbol en memoria desde el fichero
        // que se lo pasaré por parametro.
        Element raiz=doc.getRootElement();
        //cojo el elemento raiz
        //todos los hijos que tengan como nombre plantilla
        @SuppressWarnings("rawtypes")
		List load=raiz.getChildren("load");
        @SuppressWarnings("rawtypes")
		Iterator i = load.iterator();
        //
        while (i.hasNext()){
            Element e= (Element)i.next();
            //primer hijo que tenga como nombre club
            Element act =e.getChild("load-character-encoding");
           // System.out.println( "Esta activa (0 - Activa, 1 - Inactiva): " + act.getText() );
            //Convierto a String el elemento
            indicator = act.getText();
            //Convierto en integer el parse para retornar int

        }
        // Dejamos de mano del lector el sacar el nombre
        //de los arbitros, animate!!
     }catch (Exception e){
        e.printStackTrace();
     }
     return indicator;
}
    
    
    /**
     * Lee Archivo desde un XML. Indica número de threads a usar.
     * <p>
     * Parámetros del Método: Ruta del XML.
     **/
    public  String XmlThread(File ruta) throws  IOException{
    	
    	String indicator = "";
        FileReader fr = new FileReader(ruta + File.separator + "configFormat.xml");
        BufferedReader bf = new BufferedReader(fr);

     //A partir de aquí trabajamos con el JDOM para lectura del xml
     try {
        @SuppressWarnings("deprecation")
		SAXBuilder builder=new SAXBuilder(false);
        //usar el parser Xerces y no queremos
        //que valide el documento
        //Abre el documento, el método build que lee de un InputStreamReader
        Document doc=builder.build(bf);
        //construyo el arbol en memoria desde el fichero
        // que se lo pasaré por parametro.
        Element raiz=doc.getRootElement();
        //cojo el elemento raiz
        //todos los hijos que tengan como nombre plantilla
        @SuppressWarnings("rawtypes")
		List load=raiz.getChildren("thread");
        @SuppressWarnings("rawtypes")
		Iterator i = load.iterator();
        //
        while (i.hasNext()){
            Element e= (Element)i.next();
            //primer hijo que tenga como nombre club
            Element act =e.getChild("load-thread-pool");
            //System.out.println( "Numero de threads: " + act.getText() );
            //Convierto a String el elemento
            indicator = act.getText();
            //Convierto en integer el parse para retornar int

        }
        // Dejamos de mano del lector el sacar el nombre
        //de los arbitros, animate!!
     }catch (Exception e){
        e.printStackTrace();
     }
     return indicator;
}
    
    /**
     * Lee Archivo desde un XML. Indica nombre del jndi.
     * <p>
     * Parámetros del Método: Ruta del XML.
     **/
    public  String XmlJndi(File ruta) throws  IOException{
    	
    	String indicator = "";
        FileReader fr = new FileReader(ruta + File.separator + "configFormat.xml");
        BufferedReader bf = new BufferedReader(fr);

     //A partir de aquí trabajamos con el JDOM para lectura del xml
     try {
        @SuppressWarnings("deprecation")
		SAXBuilder builder=new SAXBuilder(false);
        //usar el parser Xerces y no queremos
        //que valide el documento
        //Abre el documento, el método build que lee de un InputStreamReader
        Document doc=builder.build(bf);
        //construyo el arbol en memoria desde el fichero
        // que se lo pasaré por parametro.
        Element raiz=doc.getRootElement();
        //cojo el elemento raiz
        //todos los hijos que tengan como nombre plantilla
        @SuppressWarnings("rawtypes")
		List load=raiz.getChildren("load-jndi");
        @SuppressWarnings("rawtypes")
		Iterator i = load.iterator();
        //
        while (i.hasNext()){
            Element e= (Element)i.next();
            //primer hijo que tenga como nombre club
            Element act =e.getChild("load-jndi-bd");
            //System.out.println( "Numero de threads: " + act.getText() );
            //Convierto a String el elemento
            indicator = act.getText();
            //Convierto en integer el parse para retornar int

        }
        // Dejamos de mano del lector el sacar el nombre
        //de los arbitros, animate!!
     }catch (Exception e){
        e.printStackTrace();
     }
     return indicator;
}
    
    /**
     * Lee Archivo desde un XML. Indica del jndi mail.
     * <p>
     * Parámetros del Método: Ruta del XML.
     **/
    public  String XmlJndiMail(File ruta) throws  IOException{
    	
    	String indicator = "";
        FileReader fr = new FileReader(ruta + File.separator + "configFormat.xml");
        BufferedReader bf = new BufferedReader(fr);

     //A partir de aquí trabajamos con el JDOM para lectura del xml
     try {
        @SuppressWarnings("deprecation")
		SAXBuilder builder=new SAXBuilder(false);
        //usar el parser Xerces y no queremos
        //que valide el documento
        //Abre el documento, el método build que lee de un InputStreamReader
        Document doc=builder.build(bf);
        //construyo el arbol en memoria desde el fichero
        // que se lo pasaré por parametro.
        Element raiz=doc.getRootElement();
        //cojo el elemento raiz
        //todos los hijos que tengan como nombre plantilla
        @SuppressWarnings("rawtypes")
		List load=raiz.getChildren("load-jndi");
        @SuppressWarnings("rawtypes")
		Iterator i = load.iterator();
        //
        while (i.hasNext()){
            Element e= (Element)i.next();
            //primer hijo que tenga como nombre club
            Element act =e.getChild("load-jndi-mail");
            //System.out.println( "Numero de threads: " + act.getText() );
            //Convierto a String el elemento
            indicator = act.getText();
            //Convierto en integer el parse para retornar int

        }
        // Dejamos de mano del lector el sacar el nombre
        //de los arbitros, animate!!
     }catch (Exception e){
        e.printStackTrace();
     }
     return indicator;
}
    
    
    /**
     * Lee Archivo desde un XML. Indica del jndi mail recibos.
     * <p>
     * Parámetros del Método: Ruta del XML.
     **/
    public  String XmlJndiMailRecibos(File ruta) throws  IOException{
    	
    	String indicator = "";
        FileReader fr = new FileReader(ruta + File.separator + "configFormat.xml");
        BufferedReader bf = new BufferedReader(fr);

     //A partir de aquí trabajamos con el JDOM para lectura del xml
     try {
        @SuppressWarnings("deprecation")
		SAXBuilder builder=new SAXBuilder(false);
        //usar el parser Xerces y no queremos
        //que valide el documento
        //Abre el documento, el método build que lee de un InputStreamReader
        Document doc=builder.build(bf);
        //construyo el arbol en memoria desde el fichero
        // que se lo pasaré por parametro.
        Element raiz=doc.getRootElement();
        //cojo el elemento raiz
        //todos los hijos que tengan como nombre plantilla
        @SuppressWarnings("rawtypes")
		List load=raiz.getChildren("load-jndi");
        @SuppressWarnings("rawtypes")
		Iterator i = load.iterator();
        //
        while (i.hasNext()){
            Element e= (Element)i.next();
            //primer hijo que tenga como nombre club
            Element act =e.getChild("load-jndi-mail-recibos");
            //System.out.println( "Numero de threads: " + act.getText() );
            //Convierto a String el elemento
            indicator = act.getText();
            //Convierto en integer el parse para retornar int

        }
        // Dejamos de mano del lector el sacar el nombre
        //de los arbitros, animate!!
     }catch (Exception e){
        e.printStackTrace();
     }
     return indicator;
}
    
     
    /**
     * Lee Archivo desde un XML. Indica url de envio para verificación de cuenta de correo.
     * <p>
     * Parámetros del Método: Ruta del XML.
     **/
    public  String XmlUrl(File ruta) throws  IOException{
    	
    	String indicator = "";
        FileReader fr = new FileReader(ruta + File.separator + "configFormat.xml");
        BufferedReader bf = new BufferedReader(fr);

     //A partir de aquí trabajamos con el JDOM para lectura del xml
     try {
        @SuppressWarnings("deprecation")
		SAXBuilder builder=new SAXBuilder(false);
        //usar el parser Xerces y no queremos
        //que valide el documento
        //Abre el documento, el método build que lee de un InputStreamReader
        Document doc=builder.build(bf);
        //construyo el arbol en memoria desde el fichero
        // que se lo pasaré por parametro.
        Element raiz=doc.getRootElement();
        //cojo el elemento raiz
        //todos los hijos que tengan como nombre plantilla
        @SuppressWarnings("rawtypes")
		List load=raiz.getChildren("load-url");
        @SuppressWarnings("rawtypes")
		Iterator i = load.iterator();
        //
        while (i.hasNext()){
            Element e= (Element)i.next();
            //primer hijo que tenga como nombre club
            Element act =e.getChild("load-url-verificacion");
            //System.out.println( "Numero de threads: " + act.getText() );
            //Convierto a String el elemento
            indicator = act.getText();
            //Convierto en integer el parse para retornar int

        }
        // Dejamos de mano del lector el sacar el nombre
        //de los arbitros, animate!!
     }catch (Exception e){
        e.printStackTrace();
     }
     return indicator;
}
    
}
