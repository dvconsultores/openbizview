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


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.naming.NamingException;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;



//La clase que tiene la tarea debe de implementar de la clase Job de Quartz
//ya que el programador de la tarea va a buscar una clase que implemente de ella
//y buscara el metodo execute para ejecutar la tarea
@ManagedBean
@ViewScoped
public class TareaInvocar extends Bd implements Job {

	PntGenerica consulta = new PntGenerica();
	
	//Coneccion a base de datos
	//Pool de conecciones JNDIFARM
	Connection con;
	PreparedStatement pstmt = null;
	ResultSet r;



	java.util.Date fechadia = new java.util.Date();

 /**
  * Método que se ejecuta según la tarea programada, según tres opciones:
  * 1- Envío de reporte y corre
  * 2- Envío de reporte a una ruta específica sin envío de corre
  * 3- Envío de reporte a una lista de personas en partícular (Ejemplo: recibos de pago o notificaciones particulares)	
  * Nueva opción creada para versión 5 26/10/2014
  */
  //Metodo que se ejecutara cada cierto tiempo que lo programemos despues
  public void execute(JobExecutionContext jec) throws JobExecutionException {
	// System.out.println("Fuego.......!!!!");
	
    //Aca pueden poner la tarea o el job que desean automatizar
    //Por ejemplo enviar correo, revisar ciertos datos, etc
    //SimpleDateFormat formato = new SimpleDateFormat("HH:mm");
   
    //System.out.println( "Tarea invocada a la hora: " + formato.format(new Date()));
    JobKey key = jec.getJobDetail().getKey(); //Trae el key de la tarea
    String job = key.toString().replace("unico.", "");//Reemplaza el nombre unico por nada, para que traiga solo el disparador
    //System.out.println(job);
    //Momento en que se ejecuta el reporte
    java.sql.Date sqlDate = new java.sql.Date(fechadia.getTime());  	
    //Selecciona las tareas por hora de ejecución y nombre del disparadr
   	String vlqueryORA = "select trim(codrep), trim(rutarep), trim(rutatemp), trim(opctareas), trim(ruta_salida), trim(formato), trim(disparador), case when trim(paramnames) is null then '0' else trim(paramnames) end, case when trim(paramvalues) is null then '0' else trim(paramvalues) end, trim(asunto), trim(contenido), 'R'||ROUND(dbms_random.value(1,9999)) from t_programacion where job=trim('" + job + "') order by job";
   	String vlqueryPG = "select trim(codrep), trim(rutarep), trim(rutatemp), trim(opctareas), trim(ruta_salida), trim(formato), trim(disparador),  trim(paramnames), trim(paramvalues), trim(asunto), trim(contenido), 'R'||round(random()*9999) from t_programacion where job=trim('" + job + "')  order by job";
   	String vlquerySQL = "select codrep, rutarep, rutatemp, opctareas, ruta_salida, formato from t_programacion where  job = '" + job + "' order by job";
   	//System.out.println(vlqueryORA);
   	
   	try {  	
    // System.out.println(vlqueryORA);
  	consulta.selectPntGenericaMDB(vlqueryORA, vlqueryPG, vlquerySQL, JNDI);
  	int rowsrep = consulta.getRows(); //Toma la cantidad de líneas
  	String[][] vltablarep = consulta.getArray();

	
	//Envía reporte
    //Selecciona nombre del reporte y id del grupo según hora programada
   	
  	if (rowsrep>0){//Si la consulta es mayor a cero devuelve registros envía el correo y genera reportes 
		 for (int nj = 0; nj < rowsrep; nj++){//Número de tareas a generar,pase de parámetros en el reporte
		 if(!vltablarep[nj][3].equals("2")){//Si no es igual a 2, es para envío de correo
		//Generar reporte	 
		 new RunReport().outReporteRecibo(vltablarep[nj][0].toString(), vltablarep[nj][5].toString(), vltablarep[nj][1].toString(), vltablarep[nj][2].toString(), vltablarep[nj][0].toString()+"_"+vltablarep[nj][11], sqlDate, vltablarep[nj][6].toString(), vltablarep[nj][7], vltablarep[nj][8]);	 
		 //Se envía por correo
		 //System.out.println("Mandé correo");
		 new Sendmail().mailthread(vltablarep[nj][6], vltablarep[nj][2], vltablarep[nj][0].toString()+"_"+vltablarep[nj][11], vltablarep[nj][9], vltablarep[nj][10], vltablarep[nj][5]);					
		 //Borra archivos
		 
		 //
		 } else { //Se envia a URL particular, no se evía por correo
			 //System.out.println("Invocando");
		 new RunReport().outReporteRecibo(vltablarep[nj][0].toString(), vltablarep[nj][5].toString(), vltablarep[nj][1].toString(), vltablarep[nj][4].toString(), vltablarep[nj][0].toString()+"_"+vltablarep[nj][11], sqlDate, vltablarep[nj][6].toString(), vltablarep[nj][7], vltablarep[nj][8]);	 
		 }
		 } //Termina el recorrido
		//Después del envío lo borra
  	}	

	} catch (NamingException e) {
		e.printStackTrace();
	}
	
  } 

}
