package org.openbizview.dashboard;

import java.io.File;
import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.util.Random;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.openbizview.util.Bd;
import org.openbizview.util.StringMD;
import org.primefaces.context.RequestContext;

@ManagedBean
@ViewScoped
public class HandleDashboard extends Bd implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		//Cambio de password
		StringMD md = new StringMD();
		private String dias = "";
		private String instancia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instancia"); //Usuario logeado
		private String login = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario"); //Usuario logeado
		ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
		private String context = "",  index = "";
		
		//Coneccion a base de datos
		//Pool de conecciones JNDI
		Connection con;
		PreparedStatement pstmt = null;
		
		
		
		
		
		/**
		 * @return the dias
		 */
		public String getDias() {
			return dias;
		}





		/**
		 * @param dias the dias to set
		 */
		public void setDias(String dias) {
			this.dias = dias;
		}



       
		/**
		 * @return the context
		 */
		public String getContext() {
			return context;
		}





		/**
		 * @param context the context to set
		 */
		public void setContext(String context) {
			this.context = context;
		}





		/**
		 * @return the index
		 */
		public String getIndex() {
			return index;
		}





		/**
		 * @param index the index to set
		 */
		public void setIndex(String index) {
			this.index = index;
		}





		/**
	     * Inserta t_dashboard.
	     **/
	    public void startDb()  {          
            int n = 16;
            Random r = new Random();
            byte[] b = new byte[n];
            r.nextBytes(b);
            BigInteger i = new BigInteger(b);
            String token = String.valueOf(i);
            
            String url = File.separator + "db" + File.separator + context + File.separator + index + "?id="+token+"&inst="+instancia;
	        try {
	        	Context initContext = new InitialContext();     
	     		DataSource ds = (DataSource) initContext.lookup(JNDI);
	            con = ds.getConnection();
	            
	            DatabaseMetaData databaseMetaData = con.getMetaData();
	     		String productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
	     		String query = "";
	     		
	     		switch ( productName ) {
	            case "Oracle":
	            	  query = "INSERT INTO t_dashboard VALUES (?,sysdate+"+Integer.parseInt(dias)+",?," + getFecha(productName) + ",?)";
	                 break;
	            case "PostgreSQL":
	            	 query = "INSERT INTO t_dashboard VALUES (?,now() + interval '" + +Integer.parseInt(dias) +" day'"+",?," + getFecha(productName) + ",?)";
	                 break;     		   
	      		}

	            pstmt = con.prepareStatement(query);
	            pstmt.setString(1, token);
	            pstmt.setString(2, login);
	            pstmt.setInt(3, Integer.parseInt(instancia));
	            ////System.out.println(query);
	                //Avisando
	            	pstmt.executeUpdate();
	            
	            pstmt.close();
	            con.close();
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	        RequestContext.getCurrentInstance().execute("publicar('" +url + "')");
	    }
		
	

}
