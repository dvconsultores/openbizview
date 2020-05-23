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

/*
 *  Esta clase controla todos los archivos de subida y procesamiento de txt al servidos
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.IntStream;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;


import org.primefaces.event.FileUploadEvent;

@ManagedBean
@ViewScoped
public class FileUploadController extends Bd {
	

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
			if(segu.listMenu.get(x).opcMenu.equals("M16") && segu.listMenu.get(x).vistaMenu.equals("1")) {
				new LoginBean().logout();
			}
		});
	     }
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Variables seran utilizadas para capturar mensajes de errores de Oracle y
	// parametros de metodos
	SeguridadMenuBean segu = new SeguridadMenuBean();
	FacesMessage msj = null;
	private String login = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario"); //Usuario logeado
	private String instancia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instancia"); //Usuario logeado

     ////
	// Primitives
	private static final int BUFFER_SIZE = 6124;
	//private static final String RUTA_REPORTE = File.separator + "reportes";
	FileUploadEvent event;
	ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext(); //Toma ruta real de la aplicación

	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////UPLOAD DE ARCHIVOS /////////////////////////////////////////////////  


	public void handleReportUpload(FileUploadEvent event)  {
		     //System.out.println("Thread is running");
			 File ruta = new File(extContext.getRealPath(BIRT_VIEWER_WORKING_FOLDER) + File.separator +  event.getFile().getFileName());
				try {
					FileOutputStream fileOutputStream = new FileOutputStream(ruta);
					byte[] buffer = new byte[BUFFER_SIZE];

					int bulk;
					InputStream inputStream = event.getFile().getInputstream();
					while (true) {
						bulk = inputStream.read(buffer);
						if (bulk < 0) {
							break;
						}
						fileOutputStream.write(buffer, 0, bulk);
						fileOutputStream.flush();						
					}
					msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("autos02Uploaded"), "");
					FacesContext.getCurrentInstance().addMessage(null, msj);	
					fileOutputStream.close();
					inputStream.close();
	        				
				} catch (IOException  e) {
					e.printStackTrace();
					msj = new FacesMessage(FacesMessage.SEVERITY_FATAL,  e.getMessage(), "");
					FacesContext.getCurrentInstance().addMessage(null, msj);

				}			
	}
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////UPLOAD DE ARCHIVOS /////////////////////////////////////////////////  

		public void handlePivotUpload(FileUploadEvent event) {
		
			// System.out.println("Thread is running");
			File ruta = new File(extContext.getRealPath("/WEB-INF/pivot.config") + File.separator + event.getFile().getFileName());
			try {
				FileOutputStream fileOutputStream = new FileOutputStream(ruta);
				byte[] buffer = new byte[BUFFER_SIZE];
	
				int bulk;
				InputStream inputStream = event.getFile().getInputstream();
				while (true) {
					bulk = inputStream.read(buffer);
					if (bulk < 0) {
						break;
					}
					fileOutputStream.write(buffer, 0, bulk);
					fileOutputStream.flush();
				}
				msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("autos02Uploaded"), "");
				FacesContext.getCurrentInstance().addMessage(null, msj);
				fileOutputStream.close();
				inputStream.close();
	
			} catch (IOException e) {
				e.printStackTrace();
				msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
				FacesContext.getCurrentInstance().addMessage(null, msj);
	
			}
		}
	



}
