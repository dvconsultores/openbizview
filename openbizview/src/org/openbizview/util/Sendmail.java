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


import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


@ManagedBean
@ViewScoped
public class Sendmail extends Bd {


	public Sendmail() {

	}

	PntGenerica consulta = new PntGenerica();
	MimeMessage mm;
	FacesMessage msj = null;

	/**
	 * Método que envía por correo reporte generado por Birt Report en PDF
	 * @throws NamingException 
	 * **/
	public void mail(String trigger, String ruta, String file, String asunto, String contenido, String formato) {
	   
		   String vlquery = "select trim(a.mail)";
	         vlquery += " from maillista a, t_programacion b" ;
	         vlquery += " where a.idgrupo=b.idgrupo  ";
	         vlquery += " and a.instancia=b.instancia  ";
	         vlquery += " and disparador='" + trigger.toUpperCase() + "' order by ?";
	         //System.out.println(vlquery);
	         consulta.selectGenerica(vlquery, JNDI);
	     		  		
	     	int rows = consulta.getData().get(0).size();

	     	 //System.out.println(rows);
	     	if (rows>0){//Si la consulta es mayor a cero devuelve registros envía el correo  
	     	//Recorre la lista de correos	
	     	for(int i=0;i<rows;i++){	
	     		//System.out.println(vlmail[i][0]);
	     		//mail(trigger, ruta, file, asunto, contenido, formato, vlmail[i][0]);
	     		mail(trigger, ruta, file, asunto, contenido, formato, consulta.getData().get(0).get(i));
	     		
	       	}
	     	//msj = new FacesMessage(FacesMessage.SEVERITY_INFO, "FIN DEL BLOQUE", "");
			//FacesContext.getCurrentInstance().addMessage(null, msj);
	     	//System.out.println("Correo enviado exitosamente");
	 	 	}
		        
		    
	}
	
	protected void mail(String trigger, String ruta, String file, String asunto, String contenido, String formato, String to){
		try {
			// //System.out.println("Registros :" + rows);
			Context initContext = new InitialContext();
			Session session = (Session) initContext.lookup(Bd.JNDIMAIL);
			
			MimeMessage mm = new MimeMessage(session);
			mm.setFrom(new InternetAddress(MAIL_ACCOUNT));

			// Crear el mensaje a enviar

			// Establecer las direcciones a las que será enviado
			// el mensaje (test2@gmail.com y test3@gmail.com en copia
			// oculta)
			// mm.setFrom(new
			// InternetAddress("opennomina@dvconsultores.com"));
			mm.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			// Establecer el contenido del mensaje
			mm.setSubject(asunto);
			// mm.setText(getMessage("mailContent"));

			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();

			// Fill the message
			messageBodyPart.setContent(
					contenido 
	                , "text/html; charset=utf-8");

			// Create a multipar message
			Multipart multipart = new MimeMultipart();

			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			// Part two is attachment
			messageBodyPart = new MimeBodyPart();
			String filename = ruta + File.separator + file + "." + formato;
			javax.activation.DataSource source = new FileDataSource(filename);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(file + "." + formato);
			multipart.addBodyPart(messageBodyPart);

			// Send the complete message parts
			mm.setContent(multipart);
			
			mm.saveChanges();

			// Enviar el correo electrónico
			Transport.send(mm);
			//System.out.println("Correo enviado exitosamente a :" + to + ". Reporte:" + file);

		} catch (MessagingException|NamingException e) {
			throw new RuntimeException(e);

		}
	}
	
	public void mailthread(String trigger, String ruta, String file, String asunto, String contenido, String formato) {
		ExecutorService ex = Executors.newFixedThreadPool(50);   //Número de hilos a usar para el insert
		MailThread th = new MailThread(trigger, ruta, file, asunto, contenido, formato); //Insert Generic
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
			FacesContext.getCurrentInstance().addMessage(null, msj);
			ex.shutdown();
			e.printStackTrace();
		}
        ex.execute(th);
        ex.shutdown();
	}

}
