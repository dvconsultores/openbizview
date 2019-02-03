package org.openbizview.dashboard;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;


import com.google.gson.Gson;



@WebServlet("/db/readFromDashboard")
public class ReadFromDashboard extends HttpServlet   {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
	
	//Coneccion a base de datos
	//Pool de conecciones JNDI
	Connection con;
	PreparedStatement pstmt = null;
	ResultSet r;
	private List<ReadFromDashboard> list = new ArrayList<ReadFromDashboard>();
	private String id, instancia;
	
    
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInstancia() {
		return instancia;
	}

	public void setInstancia(String instancia) {
		this.instancia = instancia;
	}

	
	protected void doGet(HttpServletRequest request,  HttpServletResponse response) {
		Context initContext;
		list.clear();
		list = new ArrayList<ReadFromDashboard>();
		id = request.getParameter("id");
		instancia = request.getParameter("inst");
        //Eliminar registros viejos
		delete();
		//
		try {
			initContext = new InitialContext();
		   
		DataSource ds = (DataSource) initContext.lookup("java:comp/env/jdbc/orabiz");
 		con = ds.getConnection();		
 		
 		String query = " Select id from t_dashboard where id = ? and instancia = ?" ;
  		
        
        pstmt = con.prepareStatement(query);
        pstmt.setString(1, id);
        pstmt.setInt(2, Integer.parseInt(instancia));
          		
        r =  pstmt.executeQuery();

        
        while (r.next()){
        	ReadFromDashboard select = new ReadFromDashboard();
     	    select.setId(r.getString(1));
        	//Agrega la lista
        	list.add(select);
        }
        
        Gson gson = new Gson();
        String jsonString = gson.toJson(list);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonString);
        
        
        
        //Cierra las conecciones
        pstmt.close();
        con.close();
        r.close();
        
		} catch (NamingException | SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
	
	/*
	 * Eliminar aquellos registros vencidos
	 */
	private void delete() {
        try {
       	
        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup("java:comp/env/jdbc/orabiz");

     		con = ds.getConnection();
     		//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
     		DatabaseMetaData databaseMetaData = con.getMetaData();
     		String productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
     		String query = "";
     		
     		switch ( productName ) {
            case "Oracle":
            	 query = "delete  T_DASHBOARD where fecven <= sysdate";
                 break;
            case "PostgreSQL":
            	 query = "delete from T_DASHBOARD where fecven <= Now()";
                 break;     		
            }
        		        	
            pstmt = con.prepareStatement(query);
            ////System.out.println(query);

            pstmt.executeUpdate();
            pstmt.close();
            con.close();

        } catch (Exception e) {
        	e.printStackTrace();
	  }
   }

}
