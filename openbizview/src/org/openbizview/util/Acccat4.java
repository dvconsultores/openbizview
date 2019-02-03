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

import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

	/**
	 *
	 * @author Andres
	 */
	@ManagedBean
	@ViewScoped
	public class Acccat4 extends Bd implements Serializable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		
		private LazyDataModel<Acccat4> lazyModel;  
		
		

	/**
		 * @return the lazyModel
		 */
		public LazyDataModel<Acccat4> getLazyModel() {
			return lazyModel;
		}
	
	@PostConstruct	
	public void init() {
		if(login==null) {
	    	 RequestContext.getCurrentInstance().execute("PF('idleDialog').show()");
	     } else {	
		if (instancia == null){instancia = "999990817832122222";}
		IntStream streamMenu = IntStream.range(0,segu.listMenu.size());
		//Recorrer opciones de seguridad y salir si no tiene acceso
		//Recorre todo el menú de la lista de sesión y por opción verifica
		streamMenu.forEach(x -> {
			if(segu.listMenu.get(x).opcMenu.equals("M20") && segu.listMenu.get(x).vistaMenu.equals("1")) {
				RequestContext.getCurrentInstance().execute("PF('idleDialogNP').show()");
			}
		});

		
		lazyModel  = new LazyDataModel<Acccat4>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 7217573531435419432L;
			
            @Override
			public List<Acccat4> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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
		//selectAcccat4();
	     } //Fin validación login null
	}
	
		
	private String b_codrol = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("segrol");
	private String b_codcat1 = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cat1"); 
	private String b_codcat2 = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cat2"); 
	private String b_codcat3 = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cat3");
	private String b_codcat4 = "";
	private String b_codcat4acc = "";
    private String descat1 = "";
	private String descat2 = "";
	private String descat3 = "";
	private String descat4 = "";
	private Object filterValue = "";
	private List<Acccat4> list = new ArrayList<Acccat4>();
	@SuppressWarnings("unchecked")
	 private List<Acccat4> listaccesoacccat4 = (List<Acccat4>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("listaccesoacccat4");	    
	 
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
	 * @return the b_codcat3
	 */
	public String getB_codcat3() {
		return b_codcat3;
	}

	/**
	 * @param b_codcat3 the b_codcat3 to set
	 */
	public void setB_codcat3(String b_codcat3) {
		this.b_codcat3 = b_codcat3;
	}

	/**
	 * @return the b_codcat4
	 */
	public String getB_codcat4() {
		return b_codcat4;
	}

	/**
	 * @param b_codcat4 the b_codcat4 to set
	 */
	public void setB_codcat4(String b_codcat4) {
		this.b_codcat4 = b_codcat4;
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
	 * @return the descat3
	 */
	public String getDescat3() {
		return descat3;
	}

	/**
	 * @param descat3 the descat3 to set
	 */
	public void setDescat3(String descat3) {
		this.descat3 = descat3;
	}

	/**
	 * @return the descat4
	 */
	public String getDescat4() {
		return descat4;
	}

	/**
	 * @param descat4 the descat4 to set
	 */
	public void setDescat4(String descat4) {
		this.descat4 = descat4;
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
	public List<Acccat4> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<Acccat4> list) {
		this.list = list;
	}

	

	/**
	 * @return the b_codcat4acc
	 */
	public String getB_codcat4acc() {
		return b_codcat4acc;
	}

	/**
	 * @param b_codcat4acc the b_codcat4acc to set
	 */
	public void setB_codcat4acc(String b_codcat4acc) {
		this.b_codcat4acc = b_codcat4acc;
	}

	

	/**
	 * @return the listaccesoacccat4
	 */
	public List<Acccat4> getListaccesoacccat4() {
		return listaccesoacccat4;
	}

	/**
	 * @param listaccesoacccat4 the listaccesoacccat4 to set
	 */
	public void setListaccesoacccat4(List<Acccat4> listaccesoacccat4) {
		this.listaccesoacccat4 = listaccesoacccat4;
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listaccesoacccat4", listaccesoacccat4);
	}

	/**
	 * @param rows the rows to set
	 */
	public void setRows(int rows) {
		this.rows = rows;
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
     * Inserta acceso categoria4.
     **/
    private void insert(String pcat4)  {
    	String[] veccodrol = b_codrol.split("\\ - ", -1);
    	//Valida que los campos no sean nulos
    	if(veriInsert(pcat4, veccodrol[0].toUpperCase())) {
        try {
        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);
            con = ds.getConnection();
            
            //Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
     		DatabaseMetaData databaseMetaData = con.getMetaData();
     		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
            
            String query = "INSERT INTO acccat4 VALUES (?,?,?,?,?,?," + getFecha(productName) + ",?," + getFecha(productName) + ",?)";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, veccodrol[0].toUpperCase());
            pstmt.setString(2, b_codcat1.split(" - ")[0].toUpperCase());
            pstmt.setString(3, b_codcat2.split(" - ")[0].toUpperCase());
            pstmt.setString(4, b_codcat3.split(" - ")[0].toUpperCase());
            pstmt.setString(5, pcat4.toUpperCase());
            pstmt.setString(6, login);
            pstmt.setString(7, login);
            pstmt.setInt(8, Integer.parseInt(instancia));
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
	  public void updatetbfilter(String pcodrol, String categ1, String categ2, String categ3) {
		 setRol(pcodrol); 
		 setAccCat1(categ1);
		 setAccCat2(categ2);
		 setAccCat3(categ3);
		  
		 listaccesoacccat4 = new ArrayList<Acccat4>();
		 selectAcccat4();
	  }
	  
	  /*
	     * Actualizar acceso a categoria2
	     * */
	    public void guardar()  { 
	    	String[] veccodrol = b_codrol.split("\\ - ", -1);
	 		String[] veccodcat1 = b_codcat1.split("\\ - ", -1);
	 		String[] veccodcat2 = b_codcat2.split("\\ - ", -1);
	 		String[] veccodcat3 = b_codcat3.split("\\ - ", -1);
	    	HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
	    	String[] paramAcc = request.getParameterValues("toAcc");
	    	String[] paramDacc = request.getParameterValues("toDacc");
	    	if(paramAcc!=null && paramDacc!=null){
	    		msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("bvtmenuUp1"), "");
	    		FacesContext.getCurrentInstance().addMessage(null, msj);
	    		exito = "error";
	    	} else 	if(paramAcc!=null && paramDacc==null && paramAcc.length>0){
	             //Recorrido del calculo
	    		IntStream.range(0, paramAcc.length).forEach(i -> {
	    		insert(paramAcc[i]);
	    	});
	    		//listaccesoreportes.clear();
	            updatetbfilter(veccodrol[0].toUpperCase(), veccodcat1[0].toUpperCase(), veccodcat2[0].toUpperCase(), veccodcat3[0].toUpperCase());
	    		
	    	} else if (paramAcc==null && paramDacc!=null && paramDacc.length>0){
	         //Recorrido del calculo
	    	IntStream.range(0, paramDacc.length).forEach(i -> {
	    		delete(paramDacc[i]);
	    	});
	    	//listaccesoreportes.clear();
	        updatetbfilter(veccodrol[0].toUpperCase(), veccodcat1[0].toUpperCase(), veccodcat2[0].toUpperCase(), veccodcat3[0].toUpperCase());
	    	}
			if (exito.equals("exito") ) {
				 msj = new FacesMessage(FacesMessage.SEVERITY_INFO,  getMessage("msnUpdate"), "");
				 FacesContext.getCurrentInstance().addMessage(null, msj);
			}

	    }
    
	    public void delete(String pcodcat4)  { 
	    	String[] veccodrol = b_codrol.split("\\ - ", -1);
	    	if(veriDelete(pcodcat4, veccodrol[0].toUpperCase())) {
		        try {
		        	
		        	Context initContext = new InitialContext();     
		     		DataSource ds = (DataSource) initContext.lookup(JNDI);

		     		con = ds.getConnection();		
		     		
		     		if(b_codcat1 == null){
		    			b_codcat1 = " - ";
		    		}
		    		if(b_codcat1 == ""){
		    			b_codcat1 = " - ";
		    		}
		    		
		    		if(b_codcat2 == null){
		    			b_codcat2 = " - ";
		    		}
		    		if(b_codcat2 == ""){
		    			b_codcat2 = " - ";
		    		}
		    		
		    		if(b_codcat3 == null){
		    			b_codcat3 = " - ";
		    		}
		    		if(b_codcat3 == ""){
		    			b_codcat3 = " - ";
		    		}
		    		
		     		String[] veccodcat1 = b_codcat1.split("\\ - ", -1);
		    		String[] veccodcat2 = b_codcat2.split("\\ - ", -1);
		    		String[] veccodcat3 = b_codcat3.split("\\ - ", -1);
		    		
		        	String query = "DELETE from acccat4 WHERE b_codrol = '" + veccodrol[0].toUpperCase() + "' and instancia = '" + instancia + "' and b_codcat1 = '" + veccodcat1[0] + "' and b_codcat2 ='" + veccodcat2[0] + "' and b_codcat3 = '" + veccodcat3[0] + "' and b_codcat4 = '" + pcodcat4 + "'";
		        		        	
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
     **/ 	
  	public void select(int first, int pageSize, String sortField, Object filterValue) {
  		
  		Context initContext;
		try {
			initContext = new InitialContext();
		  
 		DataSource ds = (DataSource) initContext.lookup(JNDI);
 		con = ds.getConnection();		
 		//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
 		DatabaseMetaData databaseMetaData = con.getMetaData();
 		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección		
 		
 		String query = "";
 		if(b_codcat1 == null){
			b_codcat1 = " - ";
		}
		if(b_codcat1 == ""){
			b_codcat1 = " - ";
		}
		
		if(b_codcat2 == null){
			b_codcat2 = " - ";
		}
		if(b_codcat2 == ""){
			b_codcat2 = " - ";
		}
		
		if(b_codcat3 == null){
			b_codcat3 = " - ";
		}
		if(b_codcat3 == ""){
			b_codcat3 = " - ";
		}
		
 		String[] veccodcat1 = b_codcat1.split("\\ - ", -1);
		String[] veccodcat2 = b_codcat2.split("\\ - ", -1);
		String[] veccodcat3 = b_codcat3.split("\\ - ", -1);
 		
	

  		switch ( productName ) {
        case "Oracle":
        	   query += "  select * from ";
        	   query += " ( select query.*, rownum as rn from";
        	   query += " (SELECT trim(A.codcat4), trim(A.descat4), trim(A.B_CODCAT1), trim(B.DESCAT1), trim(A.B_CODCAT2), trim(C.DESCAT2), trim(A.B_CODCAT3), trim(D.DESCAT3), trim(a.equicat4), trim(a.tippro) ";
        	   query +=  " FROM BVTCAT4 A, BVTCAT1 B, BVTCAT2 C, BVTCAT3 D";
        	   query += " WHERE A.B_CODCAT1=B.CODCAT1";
               query += " AND A.B_CODCAT1=C.B_CODCAT1";
               query += " AND A.B_CODCAT2=C.CODCAT2";
               query += " AND A.B_CODCAT1=D.B_CODCAT1";
               query += " AND A.B_CODCAT2=D.B_CODCAT2";
               query += " AND A.B_CODCAT3=D.CODCAT3";
               query += " and A.instancia=B.instancia";
               query += " and A.instancia=c.instancia";
               query += " and A.instancia=d.instancia";
               if(!veccodcat1[0].equals("")){
               query += " and  A.b_codcat1 = '" + veccodcat1[0].toUpperCase() + "'";
               }
               if(!veccodcat2[0].equals("")){
               query += " and  A.b_codcat2 = '" + veccodcat2[0].toUpperCase() + "'";
               }
               if(!veccodcat3[0].equals("")){
               query += " and  A.b_codcat3 = '" + veccodcat3[0].toUpperCase() + "'";
               }
               query += " and  A.codcat4 ||a.descat4 like  '%" + ((String) filterValue).toUpperCase() + "%'";
               query += " and  A.codcat4 like '" + b_codcat4 + "%'";
               query += " AND   a.instancia = '" + instancia + "'";
               query += " order by a." + sortField + ") query";
               query += " ) where rownum <= ?";
	           query += " and rn > (?)";
             break;
        case "PostgreSQL":
        	   query += " SELECT trim(A.codcat4), trim(A.descat4), trim(A.B_CODCAT1), trim(B.DESCAT1), trim(A.B_CODCAT2), trim(C.DESCAT2), trim(A.B_CODCAT3), trim(D.DESCAT3), trim(a.equicat4), trim(a.tippro) ";
        	   query +=  " FROM BVTCAT4 A, BVTCAT1 B, BVTCAT2 C, BVTCAT3 D";
        	   query += " WHERE A.B_CODCAT1=B.CODCAT1";
               query += " AND A.B_CODCAT1=C.B_CODCAT1";
               query += " AND A.B_CODCAT2=C.CODCAT2";
               query += " AND A.B_CODCAT1=D.B_CODCAT1";
               query += " AND A.B_CODCAT2=D.B_CODCAT2";
               query += " AND A.B_CODCAT3=D.CODCAT3";
               query += " and A.instancia=B.instancia";
               query += " and A.instancia=c.instancia";
               query += " and A.instancia=d.instancia";
               if(!veccodcat1[0].equals("")){
               query += " and  A.b_codcat1 = '" + veccodcat1[0].toUpperCase() + "'";
               }
               if(!veccodcat1[0].equals("")){
               query += " and  A.b_codcat2 = '" + veccodcat2[0].toUpperCase() + "'";
               }
               if(!veccodcat1[0].equals("")){
               query += " and  A.b_codcat3 = '" + veccodcat3[0].toUpperCase() + "'";
               }
               query += " and  A.codcat4 ||a.descat4 like  '%" + ((String) filterValue).toUpperCase() + "%'";
               query += " and  A.codcat4 like '" + b_codcat4 + "%'";
               query += " AND   a.instancia = '" + instancia + "'";
               query += " order by a." + sortField ;
               query += " LIMIT ?";
	           query += " OFFSET ?";
             break;
          }
  		//System.out.println(query);
        pstmt = con.prepareStatement(query);
        pstmt.setInt(1, pageSize);
        pstmt.setInt(2, first);

        r =  pstmt.executeQuery();
        
        while (r.next()){
        	Acccat4 select = new Acccat4();
        	select.setB_codcat1(r.getString(3));
            select.setDescat1(r.getString(4));
            select.setB_codcat2(r.getString(5));
            select.setDescat2(r.getString(6));
            select.setB_codcat3(r.getString(7));
            select.setDescat3(r.getString(8));
            select.setB_codcat4(r.getString(1));
            select.setDescat4(r.getString(2));
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
     **/ 	
  	public void counter(Object filterValue ) {
 		
 		if(b_codcat1 == null){
			b_codcat1 = " - ";
		}
		if(b_codcat1 == ""){
			b_codcat1 = " - ";
		}
		
		if(b_codcat2 == null){
			b_codcat2 = " - ";
		}
		if(b_codcat2 == ""){
			b_codcat2 = " - ";
		}
		
		if(b_codcat3 == null){
			b_codcat3 = " - ";
		}
		if(b_codcat3 == ""){
			b_codcat3 = " - ";
		}
		
 		String[] veccodcat1 = b_codcat1.split("\\ - ", -1);
		String[] veccodcat2 = b_codcat2.split("\\ - ", -1);
		String[] veccodcat3 = b_codcat3.split("\\ - ", -1);
		
		
 	   String query = "  SELECT trim(A.codcat4), trim(A.descat4), trim(A.B_CODCAT1), trim(B.DESCAT1), trim(A.B_CODCAT2), trim(C.DESCAT2), trim(A.B_CODCAT3), trim(D.DESCAT3), trim(a.equicat4), trim(a.tippro) ";
 	   query +=  " FROM BVTCAT4 A, BVTCAT1 B, BVTCAT2 C, BVTCAT3 D";
 	   query += " WHERE A.B_CODCAT1=B.CODCAT1";
       query += " AND A.B_CODCAT1=C.B_CODCAT1";
       query += " AND A.B_CODCAT2=C.CODCAT2";
       query += " AND A.B_CODCAT1=D.B_CODCAT1";
       query += " AND A.B_CODCAT2=D.B_CODCAT2";
       query += " AND A.B_CODCAT3=D.CODCAT3";
       query += " and A.instancia=B.instancia";
       query += " and A.instancia=c.instancia";
       query += " and A.instancia=d.instancia";
       if(!veccodcat1[0].equals("")){
       query += " and  A.b_codcat1 = '" + veccodcat1[0].toUpperCase() + "'";
       }
       if(!veccodcat2[0].equals("")){
       query += " and  A.b_codcat2 = '" + veccodcat2[0].toUpperCase() + "'";
       }
       if(!veccodcat3[0].equals("")){
       query += " and  A.b_codcat3 = '" + veccodcat3[0].toUpperCase() + "'";
       }
       query += " and  A.codcat4 ||a.descat4 like  '%" + ((String) filterValue).toUpperCase() + "%'";
       query += " and  A.codcat4 like '" + b_codcat4 + "%'";
       query += " AND   a.instancia = '" + instancia + "' order by ?";

        
        
       String querypg = " SELECT trim(A.codcat4), trim(A.descat4), trim(A.B_CODCAT1), trim(B.DESCAT1), trim(A.B_CODCAT2), trim(C.DESCAT2), trim(A.B_CODCAT3), trim(D.DESCAT3), trim(a.equicat4), trim(a.tippro) ";
       querypg +=  " FROM BVTCAT4 A, BVTCAT1 B, BVTCAT2 C, BVTCAT3 D";
       querypg += " WHERE A.B_CODCAT1=B.CODCAT1";
       querypg += " AND A.B_CODCAT1=C.B_CODCAT1";
       querypg += " AND A.B_CODCAT2=C.CODCAT2";
       querypg += " AND A.B_CODCAT1=D.B_CODCAT1";
       querypg += " AND A.B_CODCAT2=D.B_CODCAT2";
       querypg += " AND A.B_CODCAT3=D.CODCAT3";
       querypg += " and A.instancia=B.instancia";
       querypg += " and A.instancia=c.instancia";
       querypg += " and A.instancia=d.instancia";
       if(!veccodcat1[0].equals("")){
   	   querypg += " and  A.b_codcat1 = '" + veccodcat1[0].toUpperCase() + "'";
       }
       if(!veccodcat1[0].equals("")){
       querypg += " and  A.b_codcat2 = '" + veccodcat2[0].toUpperCase() + "'";
       }
       if(!veccodcat1[0].equals("")){
       querypg += " and  A.b_codcat3 = '" + veccodcat3[0].toUpperCase() + "'";
       }
       querypg += " and  A.codcat4 ||a.descat4 like  '%" + ((String) filterValue).toUpperCase() + "%'";
       querypg += " and  A.codcat4 like '" + b_codcat4 + "%'";
       querypg += " AND   a.instancia = '" + instancia + "' order by ?";

       consulta.selectGenericaMDB(query, querypg, JNDI);
       rows = consulta.getData().get(0).size();   
 
  	}

  	
  	/**
     * Leer Datos de nominas para asignar a menucheck
     **/ 	
  	private void selectAcccat4()  {
  		try {
  		Context initContext = new InitialContext();     
    	DataSource ds = (DataSource) initContext.lookup(JNDI);
        con = ds.getConnection();

    	//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
  		DatabaseMetaData databaseMetaData = con.getMetaData();
  		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección        
        
  		String query = "";
        String cat1 = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cat1"); //Usuario logeado
        String cat2 = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cat2"); //Usuario logeado
        String cat3 = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cat3"); //Usuario logeado
         
        if(cat1==null){
        	cat1 = " - ";
        }
        if(cat1==""){
        	cat1 = " - ";
        }
        if(cat2==null){
        	cat2 = " - ";
        }
        if(cat2==""){
        	cat2 = " - ";
        }
        if(cat3==null){
        	cat3 = " - ";
        }
        if(cat3==""){
        	cat3 = " - ";
        }
        if(b_codrol==null){
        	b_codrol = " - ";
        }
        if(b_codrol==""){
        	b_codrol = " - ";
        }
        String[] veccat1 = cat1.split("\\ - ", -1);
        String[] veccat2 = cat2.split("\\ - ", -1);
        String[] veccat3 = cat3.split("\\ - ", -1);
        String[] veccodrol = b_codrol.split("\\ - ", -1);

  		switch ( productName ) {
  		case "Oracle":
  			query = "Select trim(b_codcat4)";
  			query += " from acccat4";
  			query += " where B_CODCAT1 = '" + veccat1[0].toUpperCase() + "'";
  			query += " and B_CODCAT2 = '" + veccat2[0].toUpperCase() + "'";
  			query += " and B_CODCAT3 = '" + veccat3[0].toUpperCase() + "'";
  			query += " and b_codrol = '" + veccodrol[0] + "'";
  			query += " and   instancia = '" + instancia + "'";
  			query += " order by b_codcat4";
	        break;
  		case "PostgreSQL":
  			query = "Select trim(b_codcat4)";
  			query += " from acccat4";
  			query += " where B_CODCAT1 = '" + veccat1[0].toUpperCase() + "'";
  			query += " and B_CODCAT2 = '" + veccat2[0].toUpperCase() + "'";
  			query += " and B_CODCAT3 = '" + veccat3[0].toUpperCase() + "'";
  			query += " and b_codrol = '" + veccodrol[0] + "'";
  			query += " and   instancia = '" + instancia + "'";
  			query += " order by b_codcat4";
	        break;
	        }
        


        ////System.out.println(query);

        
        pstmt = con.prepareStatement(query);
        ////System.out.println(query);
  		
        r =  pstmt.executeQuery();
        
  		
        
        while (r.next()){
        	Acccat4 select = new Acccat4();
     	    select.setB_codcat4acc(r.getString(1));
        	//Agrega la lista
        	listaccesoacccat4.add(select);
        }
        setListaccesoacccat4(listaccesoacccat4);
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
  	 * Retorna si el usuario tiene asignado algún reporte
  	 */
   public String isAcccat4(String pcodcat4){
 	String valor = "fa fa-circle fa-2x text-danger";
 	Acccat4 result = null;
 	//carga lista de roles
 	if(listaccesoacccat4==null) {
 		listaccesoacccat4 = new ArrayList<Acccat4>();
 	}
  		
 	result = listaccesoacccat4.stream().parallel()           //Recorre lista             
             .filter(x -> pcodcat4.equals(x.getB_codcat4acc()))       //Compara con parametro
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
	private Boolean veriInsert(String pcodcat4, String pcodrol) {
		if(b_codcat1 == null){
			b_codcat1 = " - ";
		}
		if(b_codcat1 == ""){
			b_codcat1 = " - ";
		}
		
		if(b_codcat2 == null){
			b_codcat2 = " - ";
		}
		if(b_codcat2 == ""){
			b_codcat2 = " - ";
		}
		
		if(b_codcat3 == null){
			b_codcat3 = " - ";
		}
		if(b_codcat3 == ""){
			b_codcat3 = " - ";
		}
		
 		String[] veccodcat1 = b_codcat1.split("\\ - ", -1);
		String[] veccodcat2 = b_codcat2.split("\\ - ", -1);
		String[] veccodcat3 = b_codcat3.split("\\ - ", -1);
 		
		Boolean retorno = true;
		String query = "select b_codcat4 from acccat4 where b_codcat1 = '" + veccodcat1[0] + "' and b_codcat2 = '" + veccodcat2[0] + "' and b_codcat3 = '" +  veccodcat3[0] + "' and b_codcat4 = '" + pcodcat4 + "' and b_codrol = '" + pcodrol + "' and instancia = '"+ instancia + "' order by ?";
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
	private Boolean veriDelete(String pcodcat4, String pcodrol) {
		if(b_codcat1 == null){
			b_codcat1 = " - ";
		}
		if(b_codcat1 == ""){
			b_codcat1 = " - ";
		}
		
		if(b_codcat2 == null){
			b_codcat2 = " - ";
		}
		if(b_codcat2 == ""){
			b_codcat2 = " - ";
		}
		
		if(b_codcat3 == null){
			b_codcat3 = " - ";
		}
		if(b_codcat3 == ""){
			b_codcat3 = " - ";
		}
		
 		String[] veccodcat1 = b_codcat1.split("\\ - ", -1);
		String[] veccodcat2 = b_codcat2.split("\\ - ", -1);
		String[] veccodcat3 = b_codcat3.split("\\ - ", -1);
 		
		Boolean retorno = true;
		String query = "select b_codcat4 from acccat4 where b_codcat1 = '" + veccodcat1[0] + "' and b_codcat2 = '" + veccodcat2[0] + "' and b_codcat3 = '" +  veccodcat3[0] + "' and b_codcat4 = '" + pcodcat4 + "' and b_codrol = '" + pcodrol + "' and instancia = '"+ instancia + "' order by ?";
		PntGenerica consulta = new PntGenerica();
		consulta.selectGenerica(query, JNDI);
		int row = consulta.getData().get(0).size();
		if(row == 0) {
			retorno = false;
		}
		return retorno;
	}
  	
      
   /**
  	 * @return the rows
  	 */
  	public int getRows() {
  		return rows;
  	}

	
	public void reset() {
  		b_codrol = null;    
  		b_codcat1 = null;
  		b_codcat2 = null;
  		b_codcat3 = null;
  		updatetbfilter(null, "", "", "");
    }

}
