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

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;

public class FileUploadControllerThread extends Thread {
	// Constructor
	public FileUploadControllerThread(FileUploadEvent event) {
      this.event = event;
	}
	
	private volatile boolean bExit = false;
	  
    public void setStop(boolean bExit){
        this.bExit = bExit;
    }
  

	// Primitives
	private static final int BUFFER_SIZE = 6124;
	private static final String RUTA_REPORTE = File.separator + "reportes";
	FileUploadEvent event;
	ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext(); //Toma ruta real de la aplicación
	private String retorno;
	private Boolean exito = false;

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Variables seran utilizadas para capturar mensajes de errores de Oracle y
	// parametros de metodos
	FacesMessage msj = null;
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////UPLOAD DE ARCHIVOS PARA AUTOSERVICIO/////////////////////////////////////////////////  

	    
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////UPLOAD DE ARCHIVOS PARA AUTOSERVICIO//////////////////////////////////////////////////

	public void run()  {		
		 while(!bExit){
	     //System.out.println("Thread is running");
		 File ruta = new File(extContext.getRealPath(RUTA_REPORTE)+ File.separator +  event.getFile().getFileName());
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
				fileOutputStream.close();
				inputStream.close();
        				
			} catch (IOException  e) {
				e.printStackTrace();
				retorno = e.getMessage();
				exito = true;
			}
		 }
	}
	
	
	
	public String getRetornoThread(){
		return retorno;
	}

	/**
	 * @return the exito
	 */
	public Boolean getExito() {
		return exito;
	}

	/**
	 * @param exito the exito to set
	 */
	public void setExito(Boolean exito) {
		this.exito = exito;
	}

    
	
	


}
