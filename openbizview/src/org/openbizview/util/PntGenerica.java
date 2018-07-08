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
public class PntGenerica extends Bd implements Serializable {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//Variables serán utilizadas para capturar mensajes de errores de Oracle
	
	//Variables para select
	private int columns;
	private String[][] arr;
	private int rows;
	
	
	
	 /**
		 * Leer datos de cualquier tabla pasada por parametro
		 **/

	    public void  selectPntGenerica(String strCadena, String pool) throws  NamingException {
	        //Pool de conecciones JNDI
	        Context initContext = new InitialContext();
	        DataSource ds = (DataSource) initContext.lookup(pool);
	        try {
	            Statement stmt;
	            ResultSet rs = null;
	            Connection con = ds.getConnection();

	            //Class.forName(getDriver());
	            //con = DriverManager.getConnection(
	            //        getUrl(), getUsuario(), getClave());
	            stmt = con.createStatement(
	               		ResultSet.TYPE_SCROLL_INSENSITIVE,
	                    ResultSet.CONCUR_READ_ONLY);

				////System.out.println(strCadena);
	            try{
	            rs = stmt.executeQuery(strCadena);
	            rows = 1;
			    rs.last();
			    rows = rs.getRow();
	            ////System.out.println(rows);

	            ResultSetMetaData rsmd = rs.getMetaData();
	        	columns = rsmd.getColumnCount();
			    ////System.out.println(columns);
	        	arr = new String[rows][columns];

	            int i = 0;
			    rs.beforeFirst();
	            while (rs.next()){
	                for (int j = 0; j < columns; j++)
					arr [i][j] = rs.getString(j+1);
					i++;
	               }
	                } catch (SQLException e) {
	                }
	            stmt.close();
	            con.close(); 
	            rs.close();

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    
	    /**
	   	 * Leer datos de cualquier tabla pasada por parametro
	   	 * @param strCadena query para oracle
	   	 * @param strCadena1 query para postgres
	   	 * @param strCadena2 query para sqlserver
	   	 **/

	       public void  selectPntGenericaMDB(String strCadena, String strCadena1, String strCadena2, String pool) throws  NamingException {
	           //Pool de conecciones JNDI
	           Context initContext = new InitialContext();
	           DataSource ds = (DataSource) initContext.lookup(pool);
	           try {
	               Statement stmt;
	               ResultSet rs = null;
	               Connection con = ds.getConnection();
	             //Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
	        		DatabaseMetaData databaseMetaData = con.getMetaData();
	        		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
	               //Class.forName(getDriver());
	               //con = DriverManager.getConnection(
	               //        getUrl(), getUsuario(), getClave());
	               stmt = con.createStatement(
	                  		ResultSet.TYPE_SCROLL_INSENSITIVE,
	                       ResultSet.CONCUR_READ_ONLY);

	   			 //System.out.println(strCadena);
	               try{
	               	switch ( productName ) {
	                   case "Oracle":
	                   	rs = stmt.executeQuery(strCadena);
	                        break;
	                   case "PostgreSQL":
	                   	rs = stmt.executeQuery(strCadena1);
	                        break;
	                   case "Microsoft SQL Server":
	                   	rs = stmt.executeQuery(strCadena2);
	                        break;     
	                   }
	               
	               rows = 1;
	   		    rs.last();
	   		    rows = rs.getRow();
	               ////System.out.println(rows);

	               ResultSetMetaData rsmd = rs.getMetaData();
	           	columns = rsmd.getColumnCount();
	   		    ////System.out.println(columns);
	           	arr = new String[rows][columns];

	               int i = 0;
	   		    rs.beforeFirst();
	               while (rs.next()){
	                   for (int j = 0; j < columns; j++)
	   				arr [i][j] = rs.getString(j+1);
	   				i++;
	                  }
	                   } catch (SQLException e) {
	                   }
	               stmt.close();
	               con.close(); 
	               rs.close();

	           } catch (Exception e) {
	               e.printStackTrace();
	           }
	       }
	       
	       
	       
	       
	       
	       
	       



	 /**
	 * @return Retorna el arreglo
	 **/
	public String[][] getArray(){
		return arr;
	}

	/**
	 * @return Retorna número de filas
	 **/
	public int getRows(){
		return rows;
	}
	/**
	 * @return Retorna número de columnas
	 **/
	public int getColumns(){
		return columns;
	}

	
	
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
        		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
		 		
		 		ResultSet r;
		 			 		
		 		
		 		switch ( productName ) {
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
        		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
		 		
		 		ResultSet r;
		 			 		
		 		
		 		switch ( productName ) {
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
