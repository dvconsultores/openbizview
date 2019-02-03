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
	public class BiAudit extends Bd implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		
		private LazyDataModel<BiAudit> lazyModel; 
		
		/**
		 * @return the lazyModel
		 */
		public LazyDataModel<BiAudit> getLazyModel() {
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
			if(segu.listMenu.get(x).opcMenu.equals("M08") && segu.listMenu.get(x).vistaMenu.equals("1")) {
				RequestContext.getCurrentInstance().execute("PF('idleDialogNP').show()");
			}
		});

		
		lazyModel  = new LazyDataModel<BiAudit>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 7217573531435419432L;
			
            @Override
			public List<BiAudit> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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
	     }
	}

		private String fechadia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fecha"); 
		private String vfecaacc = "";
		private String detfaz = "";
        private String hora = "";
        private String descripcion = "";
        private String estatus = "";
        private String negocio = "";
		private Object filterValue = "";
		private List<BiAudit> list = new ArrayList<BiAudit>();
		private String instancia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instancia"); //Usuario logeado
		SeguridadMenuBean segu = new SeguridadMenuBean();

       
		/**
		 * @return the fechadia
		 */
		public String getFechadia() {
			return fechadia;
		}


		/**
		 * @param fechadia the fechadia to set
		 */
		public void setFechadia(String fechadia) {
			this.fechadia = fechadia;
		}


		/**
		 * @return the hora
		 */
		public String getHora() {
			return hora;
		}


		/**
		 * @param hora the hora to set
		 */
		public void setHora(String hora) {
			this.hora = hora;
		}


		/**
		 * @return the descripcion
		 */
		public String getDescripcion() {
			return descripcion;
		}


		/**
		 * @param descripcion the descripcion to set
		 */
		public void setDescripcion(String descripcion) {
			this.descripcion = descripcion;
		}


		/**
		 * @return the estatus
		 */
		public String getEstatus() {
			return estatus;
		}


		/**
		 * @param estatus the estatus to set
		 */
		public void setEstatus(String estatus) {
			this.estatus = estatus;
		}


		/**
		 * @return the negocio
		 */
		public String getNegocio() {
			return negocio;
		}


		/**
		 * @param negocio the negocio to set
		 */
		public void setNegocio(String negocio) {
			this.negocio = negocio;
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
	 * @return the list
	 */
	public List<BiAudit> getList() {
		return list;
	}


	/**
	 * @param list the list to set
	 */
	public void setList(List<BiAudit> list) {
		this.list = list;
	}
	
	

		/**
	 * @return the vfecaacc
	 */
	public String getVfecaacc() {
		return vfecaacc;
	}


	/**
	 * @param vfecaacc the vfecaacc to set
	 */
	public void setVfecaacc(String vfecaacc) {
		this.vfecaacc = vfecaacc;
	}
	
	

		/**
	 * @return the detfaz
	 */
	public String getDetfaz() {
		return detfaz;
	}


	/**
	 * @param detfaz the detfaz to set
	 */
	public void setDetfaz(String detfaz) {
		this.detfaz = detfaz;
	}
	

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Variables seran utilizadas para capturar mensajes de errores de Oracle y parametros de metodos
		FacesMessage msj = null;
		PntGenerica consulta = new PntGenerica();
		private int rows; //Registros de tabla
		private String login = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario"); //Usuario logeado
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


		//Coneccion a base de datos
		//Pool de conecciones JNDI
			Connection con;
			PreparedStatement pstmt = null;
			ResultSet r;
	
	
	    
	     
	    /**
	     * Leer Datos de paises
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

      		switch ( productName ) {
            case "Oracle":
            	   query += "  select * from ";
            	   query += " ( select query.*, rownum as rn from";
            	   query += " (select to_char(fechadia,'dd/mm/yyyy'), substr(fecacc,12,22), detfaz, result, negocio";
            	   query += " FROM biaudit";
            	   query += " WHERE substr(fecacc,12,22)||detfaz||negocio like '%" + ((String) filterValue).toUpperCase() + "%'";
            	   query += " AND   instancia = '" + instancia + "'";
            	   if(fechadia != null && !fechadia.equals("0") && !fechadia.equals(getMessage("biauditFecD"))){
                	   query += " and fechadia = '" + fechadia + "'";
                	   }
    	  		   query += " order by fechadia desc) query";
    	  		 query += " ) where rownum <= ?";
  	             query += " and rn > (?)";
                 break;
            case "PostgreSQL":
            	   query += " select to_char(fechadia,'dd/mm/yyyy'), substr(fecacc,12,22), detfaz, result, negocio ";
            	   query += " FROM biaudit";
            	   query += " WHERE substr(fecacc,12,22)||detfaz||negocio like '%" + ((String) filterValue).toUpperCase() + "%'";
            	   query += " AND   instancia = '" + instancia + "'";
            	   if(fechadia != null && !fechadia.equals("0") && !fechadia.equals(getMessage("biauditFecD"))){
            	   query += " and fechadia = '" + fechadia + "'";
            	   }
            	   query += " order by fechadia desc";
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
	        BiAudit select = new BiAudit();
	     	select.setFechadia(r.getString(1));
	        select.setVfecaacc(r.getString(2));
	        select.setDetfaz(r.getString(3));
	        select.setEstatus(r.getString(4).toUpperCase());
	        select.setNegocio(r.getString(5));

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

     	   String query = " select to_char(fechadia,'dd/mm/yyyy'), substr(fecacc,12,22), detfaz, result, negocio";
     	   query += " FROM biaudit";
     	   query += " WHERE substr(fecacc,12,22)||detfaz||negocio like '%" + ((String) filterValue).toUpperCase() + "%'";
     	   query += " AND   instancia = '" + instancia + "'";
     	   if(fechadia != null && !fechadia.equals("0") && !fechadia.equals(getMessage("biauditFecD"))){
           query += " and fechadia = '" + fechadia + "'";
         	   }
	  	   query += " order by fechadia , ? desc";


     	   String querypg = " select to_char(fechadia,'dd/mm/yyyy'), substr(fecacc,12,22), detfaz, result, negocio ";
     	   querypg += " FROM biaudit";
     	   querypg += " WHERE substr(fecacc,12,22)||detfaz||negocio like '%" + ((String) filterValue).toUpperCase() + "%'";
     	   querypg += " AND   instancia = '" + instancia + "'";
     	   if(fechadia != null && !fechadia.equals("0") && !fechadia.equals(getMessage("biauditFecD"))){
     	   querypg += " and fechadia = '" + fechadia + "'";
     	   }
     	   querypg += " order by fechadia, ? desc";
     	   
     	  consulta.selectGenericaMDB(query, querypg, JNDI);
          rows = consulta.getData().get(0).size();


	  	}
	
	      
	  	public void reset() {
	  		fechadia = null;     
	    }

}
