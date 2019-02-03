package org.openbizview.util;

import java.io.File;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean
@ViewScoped
public class Faces {
	
	private String imagen;

	/**
	 * Busca una imagen el el directorio para validar si existe
	 **/
	private boolean findfiles(String imagen,String size){
	   ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
	   File file=new File(extContext.getRealPath(IMAGE_DIR)+ File.separator +  imagen + "_" + size + ".png");
	   //System.out.println(file);
	   boolean exists = file.exists();
		 if (!exists) {
		  // It returns false if File or directory does not exist
			 return false;
		 }else{
		  // It returns true if File or directory exists
			 return true;
		 }
	}
	
	
	/**
	 * Muestra la imagen, retorna la imagen del usuario sino existe, muestra la estandart
	 **/
	public String imagen360400(String pimagen) {  
			if(findfiles(pimagen, "360400")){
			imagen = "../imagenp/" + pimagen + "_360400.png";
			} else {
			imagen = "../imagenp/pending.png";
			}
		
		//System.out.println(imagen);
	    return imagen;
	}
}
