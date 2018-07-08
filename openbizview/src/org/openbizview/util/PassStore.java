package org.openbizview.util;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class PassStore extends Bd implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//Coneccion a base de datos
	//Pool de conecciones JNDI
	Connection con;
	PreparedStatement pstmt = null;
	ResultSet r;
	//Cambio de password
    StringMD md = new StringMD();   
		
	
	/**
     * Inserta Pass Store>
     **/
    protected void insertPassStore(String coduser, String cluser, String instancia)  {   	
       		
        try {
        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);
            con = ds.getConnection();
            
            String query = "INSERT INTO pass_store VALUES (?,?,?)";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, coduser.toUpperCase());
            pstmt.setString(2, cluser);
            pstmt.setInt(3, Integer.parseInt(instancia));
            //System.out.println(query);

            	pstmt.executeUpdate();

            pstmt.close();
            con.close();
        } catch (Exception e) {
        	e.printStackTrace();
        }	
    }
    
    
    /**
     * Inserta Pass Store>
     **/
    protected void deletePassStore(String param)  {   	
       		
        try {
        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);
            con = ds.getConnection();
            
            String query = "DELETE from pass_store WHERE coduser in (" + param + ")";
            //System.out.println(query);
            pstmt = con.prepareStatement(query);
            pstmt.executeUpdate();

            pstmt.close();
            con.close();
        } catch (Exception e) {
        	e.printStackTrace();
        }	
    }

    
		

}
