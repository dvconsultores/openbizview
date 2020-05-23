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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;


import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;


	/**
	 *
	 * @author Andres
	 */
	@ManagedBean
	@ViewScoped
	public class Acccat2 extends Bd implements Serializable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		
		private LazyDataModel<Acccat2> lazyModel;  
		
		
	
	/**
		 * @return the lazyModel
		 */
		public LazyDataModel<Acccat2> getLazyModel() {
			return lazyModel;
		}
		
		
		@PostConstruct
		public void init() {
			if(login==null) {
		    	 new LoginBean().logout();
		     } else {
			if (instancia == null){instancia = "999990817832122222";}
			IntStream streamMenu = IntStream.range(0,segu.listMenu.size());
			//Recorrer opciones de seguridad y salir si no tiene acceso
			//Recorre todo el menú de la lista de sesión y por opción verifica
			streamMenu.forEach(x -> {
				if(segu.listMenu.get(x).opcMenu.equals("M18") && segu.listMenu.get(x).vistaMenu.equals("1")) {
					new LoginBean().logout();
				}
			});

			lazyModel  = new LazyDataModel<Acccat2>(){
				/**
				 * 
				 */
				private static final long serialVersionUID = 7217573531435419432L;
				
	            @Override
				public List<Acccat2> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
	            	//Filtro
	            	if (filters != null) {
	               	 for(Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
	               		 String filterProperty = it.next(); // table column name = field name
	                     filterValue = filters.get(filterProperty);
	               	 }
	                }

	            		//Consulta
						select(first, pageSize,sortField, filterValue);
						//Counter
						counter(filterValue);
						//Contador lazy
						lazyModel.setRowCount(rows);  //Necesario para crear la paginación
          
					return list;  
	            } 
	            
			};	
			//selectAcccat2();	
		     }//Fin validacion login null
		}
		
	
	 private String b_codrol = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("segrol");
	 private String b_codcat1 = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cat1"); 
	 private String b_codcat2 = "";
	 private String descat1 = "";
	 private String descat2 = "";
	 private Object filterValue = "";
	 private String b_codcat2acc = "";
	 private List<Acccat2> list = new ArrayList<Acccat2>();
	 @SuppressWarnings("unchecked")
	 private List<Acccat2> listaccesoacccat2 = (List<Acccat2>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("listaccesoacccat2");	    
	 private String exito = "exito";
	 private String instancia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instancia"); //Usuario logeado
	 SeguridadMenuBean segu = new SeguridadMenuBean();
	

	/**
	 * @return the b_codrol
	 */
	public String getB_codrol() {
		return b_codrol;
	}


	/**
	 * @param b_codrol the b_codrol to set
	 */
	public void setB_codrol(String b_codrol) {
		this.b_codrol = b_codrol;
	}


	/**
	 * @return the b_codcat1
	 */
	public String getB_codcat1() {
		return b_codcat1;
	}


	/**
	 * @param b_codcat1 the b_codcat1 to set
	 */
	public void setB_codcat1(String b_codcat1) {
		this.b_codcat1 = b_codcat1;
	}


	/**
	 * @return the b_codcat2
	 */
	public String getB_codcat2() {
		return b_codcat2;
	}


	/**
	 * @param b_codcat2 the b_codcat2 to set
	 */
	public void setB_codcat2(String b_codcat2) {
		this.b_codcat2 = b_codcat2;
	}


	/**
	 * @return the descat1
	 */
	public String getDescat1() {
		return descat1;
	}


	/**
	 * @param descat1 the descat1 to set
	 */
	public void setDescat1(String descat1) {
		this.descat1 = descat1;
	}


	/**
	 * @return the descat2
	 */
	public String getDescat2() {
		return descat2;
	}


	/**
	 * @param descat2 the descat2 to set
	 */
	public void setDescat2(String descat2) {
		this.descat2 = descat2;
	}


	/**
	 * @return the filterValue
	 */
	public Object getFilterValue() {
		return filterValue;
	}


	/**
	 * @param filterValue the filterValue to set
	 */
	public void setFilterValue(Object filterValue) {
		this.filterValue = filterValue;
	}


	/**
	 * @return the list
	 */
	public List<Acccat2> getList() {
		return list;
	}


	/**
	 * @param list the list to set
	 */
	public void setList(List<Acccat2> list) {
		this.list = list;
	}



	/**
	 * @param rows the rows to set
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}
	
	
	

	/**
	 * @return the listaccesoacccat2
	 */
	public List<Acccat2> getListaccesoacccat2() {
		return listaccesoacccat2;
	}


	/**
	 * @param listaccesoacccat2 the listaccesoacccat2 to set
	 */
	public void setListaccesoacccat2(List<Acccat2> listaccesoacccat2) {
		this.listaccesoacccat2 = listaccesoacccat2;
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listaccesoacccat2", listaccesoacccat2);
	}

	
	

	/**
	 * @return the b_codcat2acc
	 */
	public String getB_codcat2acc() {
		return b_codcat2acc;
	}


	/**
	 * @param b_codcat2acc the b_codcat2acc to set
	 */
	public void setB_codcat2acc(String b_codcat2acc) {
		this.b_codcat2acc = b_codcat2acc;
	}


	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Variables seran utilizadas para capturar mensajes de errores de Oracle y parametros de metodos
	FacesMessage msj = null;
	PntGenerica consulta = new PntGenerica();
	boolean vGacc; //Validador de opciones del menú
	private int rows; //Registros de tabla
	private String login = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario"); //Usuario logeado
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	//Coneccion a base de datos
	//Pool de conecciones JNDI
	Connection con;
	PreparedStatement pstmt = null;
	ResultSet r;

     /**
     * Inserta acceso categoria2.
     * <p>
     * <b>Parametros del Metodo:<b> String codrol, String cat1, String cat2 unidos como un solo string.<br>
     * String pool, String login.<br><br>
     **/
    private void insert(String pcodcat2)  { 
    	String[] veccodrol = b_codrol.split("\\ - ", -1);
    	if(veriInsert(pcodcat2, veccodrol[0].toUpperCase())) {	
        try {
        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);
            con = ds.getConnection();
            
            //Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
     		DatabaseMetaData databaseMetaData = con.getMetaData();
     		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
            
            String query = "INSERT INTO acccat2 VALUES (?,?,?,?," + getFecha(productName) + ",?," + getFecha(productName) + ",?)";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, veccodrol[0].toUpperCase());
            pstmt.setString(2, b_codcat1.split(" - ")[0].toUpperCase());
            pstmt.setString(3, pcodcat2.toUpperCase());
            pstmt.setString(4, login);
            pstmt.setString(5, login);
            pstmt.setInt(6, Integer.parseInt(instancia));
            ////System.out.println(query);

                //Avisando
            	pstmt.executeUpdate();

            pstmt.close();
            con.close();
        } catch (Exception e) {
        	e.printStackTrace();
        	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
        	FacesContext.getCurrentInstance().addMessage(null, msj);
        	exito = "error";
        }
    	}
    }
    
    /**
	  * Filtrado de table
	  */
	  public void updatetbfilter(String pcodrol, String categ1) {
		 setRol(pcodrol); 
		 setAccCat1(categ1);
		  
		 listaccesoacccat2 = new ArrayList<Acccat2>();
		 selectAcccat2();
	  }
   
	  /*
	     * Actualizar acceso a categoria2
	     * */
	    public void guardar()  { 
	    	String[] veccodrol = b_codrol.split("\\ - ", -1);
	 		String[] veccodcat1 = b_codcat1.split("\\ - ", -1);
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
	    		insert(paramAcc[i]);
	    	});
	    		//listaccesoreportes.clear();
	            updatetbfilter(veccodrol[0].toUpperCase(), veccodcat1[0].toUpperCase());
	    		
	    	} else if (paramAcc==null && paramDacc!=null && paramDacc.length>0){
	         //Recorrido del calculo
	    	IntStream.range(0, paramDacc.length).forEach(i -> {
	    		delete(paramDacc[i]);
	    	});
	    	//listaccesoreportes.clear();
	        updatetbfilter(veccodrol[0].toUpperCase(), veccodcat1[0].toUpperCase());
	    	}
			if (exito.equals("exito") ) {
				 msj = new FacesMessage(FacesMessage.SEVERITY_INFO,  getMessage("msnUpdate"), "");
				 FacesContext.getCurrentInstance().addMessage(null, msj);
			}

	    }

	/**
     * Elimina el acceso a reportes.
     * <p>
     * Parámetros del Mátodo: String rol
     **/
    public void delete(String pcodcat2)  { 
    	String[] veccodrol = b_codrol.split("\\ - ", -1);
    	if(veriDelete(pcodcat2, veccodrol[0].toUpperCase())) {
	        try {
	        	
	        	Context initContext = new InitialContext();     
	     		DataSource ds = (DataSource) initContext.lookup(JNDI);

	     		con = ds.getConnection();		
	     		
	     		if(b_codcat1==null){
	     			b_codcat1 = " - ";
	     		}
	     		if(b_codcat1==""){
	     			b_codcat1 = " - ";
	     		}

	     		String[] veccodcat1 = b_codcat1.split("\\ - ", -1);

	        	String query = "DELETE from acccat2 WHERE b_codrol = '" + veccodrol[0].toUpperCase() + "' and instancia = '" + instancia + "' and b_codcat1 = '" + veccodcat1[0] + "' and b_codcat2 ='" + pcodcat2 + "'";
	        		        	
	            pstmt = con.prepareStatement(query);
	            ////System.out.println(query);
	                //Avisando
	                pstmt.executeUpdate();
	               // msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnDelete"), "");

	            pstmt.close();
	            con.close();
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	            msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
	            FacesContext.getCurrentInstance().addMessage(null, msj); 
	            exito = "error";
    	}
    	}
    }
    
    
    
    /**
     * Leer Datos de categoria2
     * @throws NamingException 
     * @throws IOException 
     **/ 	
  	public void select(int first, int pageSize, String sortField, Object filterValue)  {
  		
  		Context initContext;
		try {
			initContext = new InitialContext();
		    
 		DataSource ds = (DataSource) initContext.lookup(JNDI);
 		con = ds.getConnection();		
 		//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
 		DatabaseMetaData databaseMetaData = con.getMetaData();
 		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
 		
 		
 		String query = "";
 		if(b_codcat1==null){
 			b_codcat1 = " - ";
 		}
 		if(b_codcat1==""){
 			b_codcat1 = " - ";
 		}

 		String[] veccodcat1 = b_codcat1.split("\\ - ", -1);
 		
 		switch ( productName ) {
        case "Oracle":
        	   query += "  select * from ";
        	   query += " ( select query.*, rownum as rn from";
        	   query += " (SELECT trim(A.codcat2), trim(A.descat2), trim(A.B_CODCAT1), trim(B.DESCAT1) ";
        	   query += " FROM BVTcat2 A, BVTCAT1 B";
               query += " WHERE A.B_CODCAT1=B.CODCAT1";
               query += " and A.instancia=B.instancia";
               if(!veccodcat1[0].equals("")){
               query += " and  A.b_codcat1 = '" + veccodcat1[0].toUpperCase() + "'";
               }
               query += " and  A.codcat2 ||a.descat2 like  '%" + ((String) filterValue).toUpperCase() + "%'";
               query += " AND   a.instancia = '" + instancia + "'";
               query += " order by  a." + sortField + ") query";
               query += " ) where rownum <= ?";
	           query += " and rn > (?)";
             break;
        case "PostgreSQL":
        	   query += " SELECT trim(A.codcat2), trim(A.descat2), trim(A.B_CODCAT1), trim(B.DESCAT1) ";
        	   query += " FROM BVTcat2 A, BVTCAT1 B";
               query += " WHERE A.B_CODCAT1=B.CODCAT1";
               query += " and A.instancia=B.instancia";
               if(!veccodcat1[0].equals("")){
               query += " and  A.b_codcat1 = '" + veccodcat1[0].toUpperCase() + "'";
               }
               query += " and  A.codcat2 ||a.descat2 like  '%" + ((String) filterValue).toUpperCase() + "%'";
               query += " AND   a.instancia = '" + instancia + "'";
               query += " order by a." + sortField + "";
               query += " LIMIT ?";
	           query += " OFFSET ?";
             break;
          }
 		 		
        
        pstmt = con.prepareStatement(query);
        pstmt.setInt(1, pageSize);
        pstmt.setInt(2, first);
        //System.out.println(query);
  		
        r =  pstmt.executeQuery();

        
        while (r.next()){
        	Acccat2 select = new Acccat2();
        	select.setB_codcat1(r.getString(3));
        	select.setDescat1(r.getString(4));
        	select.setB_codcat2(r.getString(1));
        	select.setDescat2(r.getString(2));
        	//Agrega la lista
        	list.add(select);
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
     * Leer registros en la tabla
     * @throws NamingException 
     * @throws IOException 
     **/ 	
  	public void counter(Object filterValue )  {

  		if(b_codcat1 == null){
			b_codcat1 = " - ";
		}
		if(b_codcat1 == ""){
			b_codcat1 = " - ";
		}
		String[] veccodcat1 = b_codcat1.split("\\ - ", -1);
		
 	    String query = " SELECT trim(A.codcat2), trim(A.descat2), trim(A.B_CODCAT1), trim(B.DESCAT1) ";
 	    query += " FROM BVTcat2 A, BVTCAT1 B";
        query += " WHERE A.B_CODCAT1=B.CODCAT1";
        query += " and A.instancia=B.instancia";
        if(!veccodcat1[0].equals("")){
        query += " and  A.b_codcat1 = '" + veccodcat1[0].toUpperCase() + "'";
        }
        query += " and  A.codcat2 ||a.descat2 like  '%" + ((String) filterValue).toUpperCase() + "%'";
        query += " AND   a.instancia = '" + instancia + "' order by ?";
        
        
        String querypg = " SELECT trim(A.codcat2), trim(A.descat2), trim(A.B_CODCAT1), trim(B.DESCAT1) ";
        querypg += " FROM BVTcat2 A, BVTCAT1 B";
        querypg += " WHERE A.B_CODCAT1=B.CODCAT1";
        querypg += " and A.instancia=B.instancia";
        if(!veccodcat1[0].equals("")){
        querypg += " and  A.b_codcat1 = '" + veccodcat1[0].toUpperCase() + "'";
        }
        querypg += " and  A.codcat2 ||a.descat2 like  '%" + ((String) filterValue).toUpperCase() + "%'";
        querypg += " AND   a.instancia = '" + instancia + "' order by ?";
        
        
        consulta.selectGenericaMDB(query, querypg, JNDI);
        rows = consulta.getData().get(0).size(); 

  	}
  	
  	/**
     * Leer Datos de nominas para asignar a menucheck
     **/ 	
  	private void selectAcccat2()   {
  		Context initContext;
		try {
		initContext = new InitialContext();
		   
    	DataSource ds = (DataSource) initContext.lookup(JNDI);
        con = ds.getConnection();
		
  		   		
  		String query = "";
  	
        String cat1 = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cat1"); //Usuario logeado
         
        if(cat1==null){
        	cat1 = " - ";
        }
        if(cat1==""){
        	cat1 = " - ";
        }
        
        if(b_codrol==null){
        	b_codrol = " - ";
        }
        if(b_codrol==""){
        	b_codrol = " - ";
        }
        
        String[] veccodrol = b_codrol.split("\\ - ", -1);
        String[] veccat1 = cat1.split("\\ - ", -1);

        	query = "Select trim(b_codcat2)";
            query += " from acccat2";
            query += " where B_CODCAT1 = '" + veccat1[0].toUpperCase() + "'";
            query += " and b_codrol = '" + veccodrol[0] + "'";
            query += " and   instancia = '" + instancia + "'";
            query += " order by b_codcat2";


        
        pstmt = con.prepareStatement(query);
        //System.out.println(query);
  		
        r =  pstmt.executeQuery();
        
  		
        
        while (r.next()){
        	Acccat2 select = new Acccat2();
        	select.setB_codcat2acc(r.getString(1));

        	//Agrega la lista
        	listaccesoacccat2.add(select);
        }
        setListaccesoacccat2(listaccesoacccat2);

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
  	 * @return the rows
  	 */
  	public int getRows() {
  		return rows;
  	}


  	 /**
  	 * Retorna si el usuario tiene asignado algún reporte
  	 */
   public String isAcccat2(String pcodcat2){
 	String valor = "fa fa-circle fa-2x text-danger";
 	Acccat2 result = null;
 	//carga lista de roles
 	if(listaccesoacccat2==null) {
 		listaccesoacccat2 = new ArrayList<Acccat2>();
 	}
  		
 	result = listaccesoacccat2.stream().parallel()           //Recorre lista             
             .filter(x -> pcodcat2.equals(x.getB_codcat2acc()))       //Compara con parametro
             .findAny()                                      //Retorna valor o null si no consigue
             .orElse(null);	
 		
 	if(result!=null){
 		valor = "fa fa-circle fa-2x text-success";
 	}
 	return valor;
  	}
  	
  	/**
	 * Verificar si ya existe el reporte y retornar el mensaje para inserción
	 */
	private Boolean veriInsert(String pcodcat2, String pcodrol) {
		if(b_codcat1==null){
 			b_codcat1 = " - ";
 		}
 		if(b_codcat1==""){
 			b_codcat1 = " - ";
 		}

 		String[] veccodcat1 = b_codcat1.split("\\ - ", -1);
 		
		Boolean retorno = true;
		String query = "select b_codcat2 from acccat2 where b_codcat1 = '" + veccodcat1[0] + "' and b_codcat2 = '" + pcodcat2 + "' and b_codrol = '" + pcodrol + "' and instancia = '"+ instancia + "' order by ?";
		PntGenerica consulta = new PntGenerica();
		consulta.selectGenerica(query, JNDI);
		int row = consulta.getData().get(0).size();
		if(row >0) {
			retorno = false;
		}
		return retorno;
	}
	
	
	/**
	 * Verificar si ya existe el reporte y retornar el mensaje para borrado
	 */
	private Boolean veriDelete(String pcodcat2, String pcodrol) {
		if(b_codcat1==null){
 			b_codcat1 = " - ";
 		}
 		if(b_codcat1==""){
 			b_codcat1 = " - ";
 		}

 		String[] veccodcat1 = b_codcat1.split("\\ - ", -1);
 		
		Boolean retorno = true;
		String query = "select b_codcat2 from acccat2 where b_codcat1 = '" + veccodcat1[0] + "' and b_codcat2 = '" + pcodcat2 + "' and b_codrol = '" + pcodrol + "' and instancia = '"+ instancia + "' order by ?";
        PntGenerica consulta = new PntGenerica();
		consulta.selectGenerica(query, JNDI);
		int row = consulta.getData().get(0).size();
		if(row == 0) {
			retorno = false;
		}
		return retorno;
	}

  	public void reset() {
  		b_codrol = null;    
  		b_codcat1 = null;
  		updatetbfilter(null, "");
    }

}
