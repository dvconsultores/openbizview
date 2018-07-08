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
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.primefaces.context.RequestContext;


/*
 * Compartir archivos.
 */
@ManagedBean
@ViewScoped
public class SharePivot extends Bd implements Serializable {
	
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userShare = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userShare");
	@SuppressWarnings("unchecked")
	private List<SharePivot> list = (List<SharePivot>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("list");
	private String cubos;
	private String cubosPath;
	//Lista tipo archivo para recorrer y buscar en directiorios
	List<File> filesInFolder = null;
	FacesMessage msj =  null;
	//Ruta donde se ubican los pivots por usuarios
	private	String path = System.getProperty("user.home") + File.separator
					+ ".pivot4j" + File.separator + "repository" + File.separator + new LoginBean().getLogged().toUpperCase();
	private String exito = "exito";
	private String login = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario"); //Usuario logeado
	private String instancia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instancia"); //Usuario logeado
	SeguridadMenuBean segu = new SeguridadMenuBean();
	private int rows;
	
	@PostConstruct
	public void init() {
		if(login==null) {
	    	 RequestContext.getCurrentInstance().execute("PF('idleDialog').show()");
	     } else {
	    	//Recorrer opciones de seguridad y salir si no tiene acceso
	 		//Recorre todo el menú de la lista de sesión y por opción verifica
	    	IntStream streamMenu = IntStream.range(0,segu.listMenu.size());
	 		streamMenu.forEach(x -> {
	 			if(segu.listMenu.get(x).opcMenu.equals("R04") && segu.listMenu.get(x).vistaMenu.equals("1")) {
	 				RequestContext.getCurrentInstance().execute("PF('idleDialogNP').show()");
	 			}
	 		});
		getPesonalizedPivots();
	     }
	}
	
	
	/**
	 * @return the userShare
	 */
	public String getUserShare() {
		return userShare;
	}




	/**
	 * @param userShare the userShare to set
	 */
	public void setUserShare(String userShare) {
		this.userShare = userShare;
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userShare", userShare);

	}
	


	/**
	 * @return the list
	 */
	public List<SharePivot> getList() {
		return list;
	}




	/**
	 * @param list the list to set
	 */
	public void setList(List<SharePivot> list) {
		this.list = list;
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("list", list);
	}




	/**
	 * @return the cubos
	 */
	public String getCubos() {
		return cubos;
	}




	/**
	 * @param cubos the cubos to set
	 */
	public void setCubos(String cubos) {
		this.cubos = cubos;
	}
	
	



    /**
	 * @return the cubosPath
	 */
	public String getCubosPath() {
		return cubosPath;
	}


	/**
	 * @param cubosPath the cubosPath to set
	 */
	public void setCubosPath(String cubosPath) {
		this.cubosPath = cubosPath;
	}
	
	
	
	/**
	 * @return the rows
	 */
	public int getRows() {
		return rows;
	}


	/**
	 * @param rows the rows to set
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}


	/**
     * Carga lista de cubos personalizados busca en
     * directorio archivos físicos
     */
	public void getPesonalizedPivots()  {
		if(list==null) {
			list = new ArrayList<SharePivot>();
		}
		File directory = new File(path);
		//System.out.println(list.size());
		if(list.size()==0) {
		//Si no existe el directorio lo crea	
		if (! directory.exists()){
			directory.mkdirs();
		        // If you require it to make the entire directory path including parents,
		        // use directory.mkdirs(); here instead.
		}
		try {
			//Files walk de java8 para recorrer directiorios y leer archivos
			filesInFolder = Files.walk(Paths.get(path))
					.parallel()
			        .filter(Files::isRegularFile)
			        .map(Path::toFile)
			        .collect(Collectors.toList());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		//Cargar los acrhivos en otra lista
		int count = 0;
		while (count < filesInFolder.size()) {
		   SharePivot select = new SharePivot();
		   select.setCubos(filesInFolder.get(count).getName());
		   select.setCubosPath(filesInFolder.get(count).getPath());
	       list.add(select);
	       setList(list);
	       count++;
		 }
		rows = list.size();
		}
	}
	
	
	/**
	 * Compartir cubos de un uaurio a otro
	 * Copia el direcitoro de la carpeta origen
	 * a destino renombrando el archvio para identificar quien lo comprate,
	 * ejemplo: de: admin/reporte1.
	 * Al compartir el cubo crea un directorio public para el usuario origen
	 * sino existe lo genera, igualmente crea un directorio public para el usuario
	 * destino, verifica que: si no existe el directorio usuario en el repositorio,
	 * lo crea, si no existe la carpeta public la crea
	 */
	private void sharePivot(String cube, int pos) {
		//Seleccionando ruta de origen
		//Ruta donde se ubican los pivots por usuarios
		String pathshare = System.getProperty("user.home") + File.separator
			+ ".pivot4j" + File.separator + "repository" + File.separator + userShare;
		
		File directory = new File(path + File.separator + "Public");
		//Verificar sino existe el directorio lo crea
		if (! directory.exists()){
	        directory.mkdirs();
	        // If you require it to make the entire directory path including parents,
	        // use directory.mkdirs(); here instead.
	    }
		//Copia archivo seleccionado a la carpeta public
		File source = new File(list.get(pos).cubosPath);//Tomar la posición de la lista para moverlo a public
		File dest = new File(directory.toString() + File.separator + cube);
		
		try {
			if(!dest.exists()) {
		    FileUtils.moveFile(source, dest);
			}
		
		//Procede a copiar archivo en directorio destino
		//Verificar sino existe el directorio lo crea
		File directoryDest = new File(pathshare + File.separator + "Public");
		if (! directoryDest.exists()){
			directoryDest.mkdirs();
		  // If you require it to make the entire directory path including parents,
		  // use directory.mkdirs(); here instead.
		}
		
		//7Verificar que no exista
		
		//Copia archivo seleccionado a la carpeta public del usuario destino
		source = new File(directory.toString() + File.separator + cube);
		String elcubo = getMessage("pivotDe") +  login + File.separator + cube;
		dest = new File(directoryDest.toString() + File.separator +  elcubo);
		if(dest.exists()) {
			//Realiza nada y devuelve mensaje
			msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("pivotExist"), "");
        	FacesContext.getCurrentInstance().addMessage(null, msj);
        	exito = "error";
		} else {		
		 FileUtils.copyFile(source, dest);
		} 

		} catch (IOException e) {
		    e.printStackTrace();
		    msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
        	FacesContext.getCurrentInstance().addMessage(null, msj);
        	exito = "error";
		}
	}
	
	
	/**
	 * Descompartir el cubo
	 */
	private void unsharePivot(String cube) {
		//Ruta donde se ubican los pivots por usuarios
		Path dirPath_1 = Paths.get(System.getProperty("user.home") + File.separator
				 + ".pivot4j" + File.separator + "repository"  + File.separator + userShare + File.separator + "Public" + File.separator + getMessage("pivotDe") +  login + File.separator + cube);		
		try {
	        Files.deleteIfExists(dirPath_1);
	        if(Files.deleteIfExists(dirPath_1)) {
	          exito = "error";	
	        }
	        //Verificar si el directorio está vacío
	        File file = new File(System.getProperty("user.home") + File.separator
					 + ".pivot4j" + File.separator + "repository"  + File.separator + userShare + File.separator + "Public" + File.separator + getMessage("pivotDe") +  login);
	        if(file.isDirectory()){
	    		if(file.list().length>0){
	    			System.out.println("Directory is not empty!");
	    		}else{
	    			file.delete();
	    			System.out.println("Directory is empty!");
	    		}
	    	}else{
	    		System.out.println("This is not a directory");
	    	}  
		} catch (IOException ioException) {
	    	  msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("pivotsharedDeleteError"), "");
	          FacesContext.getCurrentInstance().addMessage(null, msj);
	          exito = "error";
	        ioException.printStackTrace();
	      }
	}
	
	 /**
  	 * Retorna si el usuario está compatiendo cubos
  	 */
	 public String isPivot(String ppivot){
		if(userShare==null) {
			userShare = "";
		}
	 	String valor = "fa fa-circle fa-2x text-danger";
	 	String pathshare = System.getProperty("user.home") + File.separator
				 + ".pivot4j" + File.separator + "repository"  + File.separator + userShare + File.separator + "Public" + File.separator + getMessage("pivotDe") +  login;
	 	File directoryDest = new File(pathshare + File.separator + ppivot);
	 		
	 	if(directoryDest.exists()){
	 		valor = "fa fa-circle fa-2x text-success";
	 	}
	 	return valor;
	  	}
	 
	 
	 /**
	  * Filtrado de table
	  */
	  public void updatetbfilter(String pusershare) {
		 setUserShare(pusershare);
		 list.clear(); 
		 list = new ArrayList<SharePivot>();
		 getPesonalizedPivots();
	  }
	 
	 
		/*
	     * Compartir cubos
	     * */
	    public void guardar()  { 

	    	HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
	    	String[] paramAcc = request.getParameterValues("toAcc");
	    	String[] paramDacc = request.getParameterValues("toDacc");
	    	if(paramAcc==null && paramDacc==null){
	    		msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("html59"), "");
	    		FacesContext.getCurrentInstance().addMessage(null, msj);
	    		exito = "error";
	    	} else 	if(paramAcc!=null && paramDacc!=null){
	    		msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("bvtmenuUp1"), "");
	    		FacesContext.getCurrentInstance().addMessage(null, msj);
	    		exito = "error";
	    	} else 	if(paramAcc!=null && paramDacc==null && paramAcc.length>0){
	             //Recorrido del calculo
	    		IntStream.range(0, paramAcc.length).forEach(i -> {
	    		sharePivot(paramAcc[i].split("\\|", -1)[0], Integer.parseInt(paramAcc[i].split("\\|", -1)[1]));
	    	});
	    		//listaccesoreportes.clear();
	            updatetbfilter(userShare.toUpperCase());
	            if (exito.equals("exito") ) {
					 msj = new FacesMessage(FacesMessage.SEVERITY_INFO,  getMessage("pivotshared"), "");
					 FacesContext.getCurrentInstance().addMessage(null, msj);
				}
	    		
	    	} else if (paramAcc==null && paramDacc!=null && paramDacc.length>0){
	         //Recorrido del calculo
	    	IntStream.range(0, paramDacc.length).forEach(i -> {
	    		unsharePivot(paramDacc[i].split("\\|", -1)[0]);
	    	});
	    	if (exito.equals("exito") ) {
				 msj = new FacesMessage(FacesMessage.SEVERITY_INFO,  getMessage("pivotsharedDelete"), "");
				 FacesContext.getCurrentInstance().addMessage(null, msj);
			}
	    	//listaccesoreportes.clear();
	        updatetbfilter(userShare.toUpperCase());
	    	}

	    }
	    
	    public void reset() {
	  		userShare = null;
	  		updatetbfilter(null);
	    }
	    
	    
	    /**
		 * Lista Usuario.
		 * 
		 * @throws NamingException
		 * @return List String
		 * @throws IOException
		 **/

		public List<String> completeUserPivot(String query)  {

			String queryora = "Select coduser" + " from bvt002 "
					+ " where coduser||desuser like '%"
					+ query.toUpperCase() + "%' and instancia = '" + instancia
					+ "' and coduser not in ('" + login + "') order by desuser, ?";
			String querypg = "Select coduser" + " from bvt002 "
					+ " where coduser||desuser  like '%"
					+ query.toUpperCase() + "%' and instancia = '" + instancia
					+ "' and coduser not in ('" + login + "') order by desuser, ?";

            PntGenerica consulta = new PntGenerica();
            List<String> results = new ArrayList<String>();
            
			consulta.selectGenericaMDB(queryora, querypg, JNDI);

			IntStream.range(0, consulta.getData().get(0).size())
		     .forEach(i -> 
		       results.add(consulta.getData().get(0).get(i))
		       );			
		    return results;
		}
}
