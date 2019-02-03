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

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author Andres Dominguez 08/02/2009
 */
/**
 * datos de cualquier tabla pasada por parametro
 *  */
public class PntGenerica implements Serializable {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//Variables serán utilizadas para capturar mensajes de errores de Oracle

	
	
	/////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////NUEVO METODO, PUEDE USAR EL QUE SE LE DE MAS COMODO//
	/////////////////////////////ESTE USA LISTAS EL ANTERIOR ARREGLOS///////////////////////
	////////////////////////////////////////////////////////////////////////////////////////

	//Variables para select
	List<List<String>> data = new ArrayList<List<String>>();
	/**
	* @return the data
	*/
	public List<List<String>> getData() {
		return data;
	}
	
	/**
	* @param data the data to set
	*/
	public void setData(List<List<String>> data) {
		this.data = data;
	}
   
       
       

		/**
		 * Leer datos de cualquier tabla pasando el query por parametro
		 * Nueva versión Agosto 2016
		 * Se reemplaza la clase anterior (PntGenerica) utlizando como métodos
		 * listas por ser mas eficientes y remplazando a la smatrices
		 * @param La cadena de consulta
		 * @param La conexión JNDI
		 * @return void
		 **/
	    public void selectGenerica(String strCadena, String pool) {
	    	
	    	PreparedStatement pstmt = null;
	    	Connection con;
			try {
				//Pool de conexión a base de datos
				Context initContext = new InitialContext();
				DataSource ds = (DataSource) initContext.lookup(pool);
		 		con = ds.getConnection();		
		 		
		 		ResultSet r;
		 		
		 		pstmt = con.prepareStatement(strCadena);
		 		pstmt.setInt(1, 1);
		 		
		 		//System.out.println(strCadena);
		 		r =  pstmt.executeQuery();
		 		
		 		//Resulset metadata para conocer la cantidad de columnas
		 		ResultSetMetaData rsmd = r.getMetaData();
		 		
		 		//Se define la cantidad de columnas
		 		int col = rsmd.getColumnCount();
		 		
		 		for (int i = 0; i < col; i++){
		 			//Se agrega la lista para defrinir la cantidad de columnas
	 				data.add(new ArrayList<String>());
	 			}
		
		 		while (r.next()){
		 			//Dependiendo la cantidad de columnas y filas se define
		 			//la lista get(i) para columnas "add" para el número de filas
		 			for (int i = 0; i < col; i++){
		 			data.get(i).add(r.getString(i+1));
		 			}                                 
		 		}
		 	 
		 		//Cierra las conecciones
		        pstmt.close();
		        con.close();
		        r.close();
		 		
			} catch (NamingException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}     
	        
	    }
	    
	    
	    /* Leer datos de cualquier tabla pasando el query sin parametro
		 * Nueva versión Agosto 2016
		 * Se reemplaza la clase anterior (PntGenerica) utlizando como métodos
		 * listas por ser mas eficientes y remplazando a la smatrices
		 * @param La cadena de consulta
		 * @param La conexión JNDI
		 * @return void
		 **/
	    public void selectGenericaNP(String strCadena, String pool) {
	    	
	    	PreparedStatement pstmt = null;
	    	Connection con;
			try {
				//Pool de conexión a base de datos
				Context initContext = new InitialContext();
				DataSource ds = (DataSource) initContext.lookup(pool);
		 		con = ds.getConnection();		
		 		
		 		ResultSet r;
		 		
		 		pstmt = con.prepareStatement(strCadena);
		 		//pstmt.setInt(1, 1);
		 		
		 		//System.out.println(strCadena);
		 		r =  pstmt.executeQuery();
		 		
		 		//Resulset metadata para conocer la cantidad de columnas
		 		ResultSetMetaData rsmd = r.getMetaData();
		 		
		 		//Se define la cantidad de columnas
		 		int col = rsmd.getColumnCount();
		 		
		 		for (int i = 0; i < col; i++){
		 			//Se agrega la lista para defrinir la cantidad de columnas
	 				data.add(new ArrayList<String>());
	 			}
		
		 		while (r.next()){
		 			//Dependiendo la cantidad de columnas y filas se define
		 			//la lista get(i) para columnas "add" para el número de filas
		 			for (int i = 0; i < col; i++){
		 			data.get(i).add(r.getString(i+1));
		 			}                                 
		 		}
		 	 
		 		//Cierra las conecciones
		        pstmt.close();
		        con.close();
		        r.close();
		 		
			} catch (NamingException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}     
	        
	    }
	    
	    
	    /**
		 * Leer datos de cualquier tabla pasando el query por parametro
		 * Nueva versión Agosto 2016
		 * Se reemplaza la clase anterior (PntGenerica) utlizando como métodos
		 * listas por ser mas eficientes y remplazando a la smatrices
		 * @param La cadena de consulta para oracle
		 * @param La cadena de consulta para postgres
		 * @param La cadena de consulta para sqlserver
		 * @param La conexión JNDI
		 * @return void
		 **/
	    public void selectGenericaMDB(String strOra, String strPg, String pool) {
	    	
	    	PreparedStatement pstmt = null;
	    	Connection con;
			try {
				//Pool de conexión a base de datos
				Context initContext = new InitialContext();
				DataSource ds = (DataSource) initContext.lookup(pool);
		 		con = ds.getConnection();	
		 		//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
        		DatabaseMetaData databaseMetaData = con.getMetaData();
        		Bd.productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
		 		
		 		ResultSet r;
		 			 		
		 		
		 		switch ( Bd.productName ) {
                case "Oracle":
                	pstmt = con.prepareStatement(strOra);
                     break;
                case "PostgreSQL":
                	pstmt = con.prepareStatement(strPg);
                     break;  
                }
		 		pstmt.setInt(1, 1);
		 		
		 		//System.out.println(strCadena);
		 		r =  pstmt.executeQuery();
		 		
		 		//Resulset metadata para conocer la cantidad de columnas
		 		ResultSetMetaData rsmd = r.getMetaData();
		 		
		 		//Se define la cantidad de columnas
		 		int col = rsmd.getColumnCount();
		 		
		 		for (int i = 0; i < col; i++){
		 			//Se agrega la lista para defrinir la cantidad de columnas
	 				data.add(new ArrayList<String>());
	 			}
		
		 		while (r.next()){
		 			//Dependiendo la cantidad de columnas y filas se define
		 			//la lista get(i) para columnas "add" para el número de filas
		 			for (int i = 0; i < col; i++){
		 			data.get(i).add(r.getString(i+1));
		 			}                                 
		 		}
		 	 
		 		//Cierra las conecciones
		        pstmt.close();
		        con.close();
		        r.close();
		 		
			} catch (NamingException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}     
	        
	    }
	    
	    
	    
	    /**
		 * Leer datos de cualquier tabla pasando el query sin parametro
		 * Nueva versión Agosto 2016
		 * Se reemplaza la clase anterior (PntGenerica) utlizando como métodos
		 * listas por ser mas eficientes y remplazando a la smatrices
		 * @param La cadena de consulta para oracle
		 * @param La cadena de consulta para postgres
		 * @param La cadena de consulta para sqlserver
		 * @param La conexión JNDI
		 * @return void
		 **/
	    public void selectGenericaMDBNP(String strOra, String strPg, String pool) {
	    	
	    	PreparedStatement pstmt = null;
	    	Connection con;
			try {
				//Pool de conexión a base de datos
				Context initContext = new InitialContext();
				DataSource ds = (DataSource) initContext.lookup(pool);
		 		con = ds.getConnection();	
		 		//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
        		DatabaseMetaData databaseMetaData = con.getMetaData();
        		Bd.productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
		 		
		 		ResultSet r;
		 			 		
		 		
		 		switch ( Bd.productName ) {
                case "Oracle":
                	pstmt = con.prepareStatement(strOra);
                     break;
                case "PostgreSQL":
                	pstmt = con.prepareStatement(strPg);
                     break;  
                }
		 		//pstmt.setInt(1, 1);
		 		
		 		//System.out.println(strCadena);
		 		r =  pstmt.executeQuery();
		 		
		 		//Resulset metadata para conocer la cantidad de columnas
		 		ResultSetMetaData rsmd = r.getMetaData();
		 		
		 		//Se define la cantidad de columnas
		 		int col = rsmd.getColumnCount();
		 		
		 		for (int i = 0; i < col; i++){
		 			//Se agrega la lista para defrinir la cantidad de columnas
	 				data.add(new ArrayList<String>());
	 			}
		
		 		while (r.next()){
		 			//Dependiendo la cantidad de columnas y filas se define
		 			//la lista get(i) para columnas "add" para el número de filas
		 			for (int i = 0; i < col; i++){
		 			data.get(i).add(r.getString(i+1));
		 			}                                 
		 		}
		 	 
		 		//Cierra las conecciones
		        pstmt.close();
		        con.close();
		        r.close();
		 		
			} catch (NamingException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}     
	        
	    }
	    
	 

}
