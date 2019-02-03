package org.openbizview.etl;

import java.io.File;
import java.io.Serializable;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.commons.lang3.StringUtils;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;

@ManagedBean
@ViewScoped
public class Etl implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Etl() {
		// TODO Auto-generated constructor stub
	}
	
	public void runTransformation(String etl, String ubicacionetl, String paramnames, String paramvalues) {
		//System.out.println("Iniciando ETL");
		  try {
			KettleEnvironment.init();
		    //EnvUtil.environmentInit();
		    TransMeta transMeta = new TransMeta(ubicacionetl + File.separator + etl + ".ktr");
		    Trans trans = new Trans(transMeta);
		    
		    //Arreglo para longitud, se recibe como parámetro de longitud paramvalues
		    //Con la libreria String utils se define el separador y con ello se conoce la longitud de los parametros
		    //Por ejemplo recibe como parámetro P1|P2|P3, con separador '|', al recorrerlo devuelve longitud tres
		    //Se va pasando al reporte cada parametro
		    //DVConsultores Andrés Dominguez 08/12/2017
		    String[] arraySplitSrtings = StringUtils.splitByWholeSeparator(paramvalues, "|");
		    
		    //Si la longitud de los valores de parámetros es diferente de cero
		    //Procede a leer y pasar parámetros
		    if(arraySplitSrtings.length>0 ){
		      String[] arr1 = paramnames.split("\\|", -1);//Toma la lista de parametros de la tabla y hace un split, trabaja el arreglo y lo recorre
		      String[] arr2 = paramvalues.split("\\|", -1);//Toma la lista de parametros de la tabla y hace un split, trabaja el arreglo y lo recorre
		    	  for(int i = 0; i < arraySplitSrtings.length; i++){
		    		  trans.setParameterValue(arr1[i], arr2[i]);
		    	  } 
		    }//Valida longitud 
		    
		    trans.execute(null); // You can pass arguments instead of null.
		    trans.waitUntilFinished();
		
		    
		    if ( trans.getErrors() > 0 )
		    {
		      throw new RuntimeException( "There were errors during transformation execution." );
		    }
		  }
		 
		  catch ( KettleException e ) {
		    // TODO Put your exception-handling code here.
		    System.out.println(e);
		  }
		  //System.out.println("Fin ETL");
		  
		}
	
	public static void main (String [] arg) {
		Etl etl = new Etl();
		etl.runTransformation("Prueba1", "/home/andres/Etl", "|", "|");
	}

}
