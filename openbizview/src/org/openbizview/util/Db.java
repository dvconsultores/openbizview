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
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;



@ManagedBean
@ViewScoped
public class Db extends Bd implements Serializable  {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@PostConstruct
	public void init() {
		if (instancia == null){instancia = "99999";}
		if (login==null) {
			//rq.getCurrentInstance().execute("PF('yourdialogid').show()");
		   new LoginBean().logout();
		} else {
		   
		q1 = "select count(codrep) from bvt001 where instancia =  '" + instancia + "' and codrep IN (SELECT B_CODREP FROM BVT007 WHERE B_CODROL ='" + rol + "' UNION ALL SELECT B_CODrol FROM BVT008 WHERE CODUSER = '" + login + "')";
		//System.out.println(q1);
		consulta.selectGenericaNP(q1, JNDI);	
		if(consulta.getData().get(0).size()>0) {
		r1 = Integer.parseInt(consulta.getData().get(0).get(0));
		}
		consulta.getData().clear();

		//Lee cuantas tareas
		q2 = "select count(disparador) from t_programacion where instancia =  '" + instancia + "'";
		consulta.selectGenericaNP(q2, JNDI);
		if(consulta.getData().get(0).size()>0) {
		r2 = Integer.parseInt(consulta.getData().get(0).get(0));
		}
		consulta.getData().clear();

		//Lee cuantos usuarios
		q3 = "select count(coduser) from bvt002 where instancia =  '" + instancia + "'";
		consulta.selectGenericaNP(q3, JNDI);
		if(consulta.getData().get(0).size()>0) {
		r3 = Integer.parseInt(consulta.getData().get(0).get(0));
		}
		consulta.getData().clear();
	
		//Lee cuantos reportes impresos
		q4 = "select count(b_codrep) from bvt006 where instancia =  '" + instancia + "'";
		consulta.selectGenericaNP(q4, JNDI);
		if(consulta.getData().get(0).size()>0) {
		r4 = Integer.parseInt(consulta.getData().get(0).get(0));
		}
		consulta.getData().clear();
		
	   selectTop();
	   selectTopPerformer();
		}
	   //System.out.println("instancia: " + instancia);
    }
	
	private String instancia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instancia");
	private String login = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("login"); //Usuario logeado
	private String rol = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("rol"); //Usuario logeado
	
	//Coneccion a base de datos
	//Pool de conecciones JNDI
	//Coneccion a base de datos
	//Pool de conecciones JNDIFARM
	Connection con;
	PreparedStatement pstmt = null;
	PreparedStatement pstmt1 = null;
	ResultSet r;
	
	

	//Querys
	private String q1;
	private String q2;
	private String q3;
	private String q4, codrep, desrep, count, coduser, topperformer, inciales;

	//Chart
    protected int rowschart;
    protected String[][] vlTabla;
	//Registros
	private int r1 = 0, r2 = 0, r3 = 0, r4 = 0, r5 = 0;
	PntGenerica consulta = new PntGenerica();
	private List<Db> list = new ArrayList<Db>();
	private List<Db> list1 = new ArrayList<Db>();
	
	
	/**
	 * @return the r1
	 */
	public int getR1() {
		return r1;
	}
	/**
	 * @param r1 the r1 to set
	 */
	public void setR1(int r1) {
		this.r1 = r1;
	}
	/**
	 * @return the r2
	 */
	public int getR2() {
		return r2;
	}
	/**
	 * @param r2 the r2 to set
	 */
	public void setR2(int r2) {
		this.r2 = r2;
	}
	/**
	 * @return the r3
	 */
	public int getR3() {
		return r3;
	}
	/**
	 * @param r3 the r3 to set
	 */
	public void setR3(int r3) {
		this.r3 = r3;
	}
	/**
	 * @return the r4
	 */
	public int getR4() {
		return r4;
	}
	/**
	 * @param r4 the r4 to set
	 */
	public void setR4(int r4) {
		this.r4 = r4;
	}
	

	/**
	 * @return the instancia
	 */
	public String getInstancia() {
		return instancia;
	}
	/**
	 * @param instancia the instancia to set
	 */
	public void setInstancia(String instancia) {
		this.instancia = instancia;
	}
	
	

	/**
	 * @return the r5
	 */
	public int getR5() {
		return r5;
	}
	/**
	 * @param r5 the r5 to set
	 */
	public void setR5(int r5) {
		this.r5 = r5;
	}
	
	
	
	/**
	 * @return the list
	 */
	public List<Db> getList() {
		return list;
	}
	/**
	 * @param list the list to set
	 */
	public void setList(List<Db> list) {
		this.list = list;
	}
	/**
	 * @return the codrep
	 */
	public String getCodrep() {
		return codrep;
	}
	/**
	 * @param codrep the codrep to set
	 */
	public void setCodrep(String codrep) {
		this.codrep = codrep;
	}
	/**
	 * @return the desrep
	 */
	public String getDesrep() {
		return desrep;
	}
	/**
	 * @param desrep the desrep to set
	 */
	public void setDesrep(String desrep) {
		this.desrep = desrep;
	}
	/**
	 * @return the count
	 */
	public String getCount() {
		return count;
	}
	/**
	 * @param count the count to set
	 */
	public void setCount(String count) {
		this.count = count;
	}
	
	
	/**
	 * @return the coduser
	 */
	public String getCoduser() {
		return coduser;
	}
	/**
	 * @param coduser the coduser to set
	 */
	public void setCoduser(String coduser) {
		this.coduser = coduser;
	}
	/**
	 * @return the list1
	 */
	public List<Db> getList1() {
		return list1;
	}
	/**
	 * @param list1 the list1 to set
	 */
	public void setList1(List<Db> list1) {
		this.list1 = list1;
	}
	
	
	
	/**
	 * @return the topperformer
	 */
	public String getTopperformer() {
		return topperformer;
	}
	/**
	 * @param topperformer the topperformer to set
	 */
	public void setTopperformer(String topperformer) {
		this.topperformer = topperformer;
	}
	
	
	/**
	 * @return the inciales
	 */
	public String getInciales() {
		return inciales;
	}
	/**
	 * @param inciales the inciales to set
	 */
	public void setInciales(String inciales) {
		this.inciales = inciales;
	}
	
	
	/**
	 * Listar instancias al momento del login
	 * si el usuario no tiene alguna instancia predefinida
	 * muestra el modal para seleccionar la instancia,
	 * lee de las instancias asociadas al usuario
	 */
     public List<String> select() throws NamingException, SQLException   {
  		
  		Context initContext = new InitialContext();     
 		DataSource ds = (DataSource) initContext.lookup(JNDI);
 		con = ds.getConnection();
 		List<String> values = new ArrayList<String>();
 		
 		String query = "SELECT a.instancia||' - '||trim(b.descripcion) ";
	       query += " FROM instancias_usr a, instancias b";
	       query += " where a.instancia=b.instancia";
	       query += " and coduser = ?";

  		  		
  		pstmt = con.prepareStatement(query);
  		pstmt.setString(1, login);
        //System.out.println(query);
  		
        r =  pstmt.executeQuery();
        		
        while (r.next()){
 
        values.add(r.getString(1));

        }
        //Cierra las conecciones
        pstmt.close();
        con.close();
        return values;
    }
	
     
     
     /**
      * Leer Datos de reportes
      **/ 	
   	public void selectTop()   {
   		
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
         	   query += " (select B_CODREP, B_DESREP, COUNT(B_CODREP) CUENTA";
 		       query += " from bvt006";
 		       query += " where b_coduser = '" + login + "'";
 		       query += " AND   instancia = '" + instancia + "'";
 		       query += " GROUP BY b_codrep, b_desrep";
 		       query += " ORDER BY 3 DESC) query";
 	  		   query += " ) where rownum <= 5";
 	           query += " and rn > (0)";
              break;
         case "PostgreSQL":
         	   query += " select B_CODREP, B_DESREP, COUNT(B_CODREP) CUENTA";
         	   query += " from bvt006";
 		       query += " WHERE A.CODGRUP=B.CODGRUP";
 		       query += " where b_coduser = '" + login + "'";
 		       query += " AND   instancia = '" + instancia + "'";
 		       query += " ORDER BY 3 DESC";
 	  		   query += " LIMIT 5";
              break;     		   
   		}
   		//System.out.println(query);
   		pstmt = con.prepareStatement(query);
   		
         r =  pstmt.executeQuery();
         		
         while (r.next()){
         Db select = new Db();
      	 select.setCodrep(r.getString(1));
      	 select.setDesrep(r.getString(2));
      	 select.setCount(r.getString(3));
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
 		} finally {
			try {
				if (con == null) {
					con.close();
				}
				if (pstmt == null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
 		}
     }
   	
   	
   	
   	/**
     * Leer Datos de reportes
     **/ 	
  	public void selectTopPerformer()   {
  		
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
        	   query += " (select b_coduser, COUNT(b_coduser) CUENTA, substr(b_coduser, 1, 2)";
		       query += " from bvt006";
		       query += " where   instancia = '" + instancia + "'";
		       query += " GROUP BY b_coduser";
		       query += " ORDER BY 2 DESC) query";
	  		   query += " ) where rownum <= 4";
	           query += " and rn > (0)";
             break;
        case "PostgreSQL":
        	   query += " select b_coduser, COUNT(b_coduser) CUENTA, substr(b_coduser, 1, 2)";
        	   query += " from bvt006";
		       query += " GROUP BY b_coduser";
		       query += " where   instancia = '" + instancia + "'";
		       query += " ORDER BY 2 DESC";
	  		   query += " LIMIT 4";
             break;     		   
  		}
  		//System.out.println(query);
  		pstmt = con.prepareStatement(query);
  		
        r =  pstmt.executeQuery();
        		
        while (r.next()){
        Db select = new Db();
     	 select.setCoduser(r.getString(1));
     	 select.setTopperformer(r.getString(2));
     	 select.setInciales(r.getString(3));
        	//Agrega la lista
        	list1.add(select);
        }
        //Cierra las conecciones
        pstmt.close();
        con.close();
        r.close();
        
		} catch (NamingException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (con == null) {
					con.close();
				}
				if (pstmt == null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    }

     

     
 
     	
}

 