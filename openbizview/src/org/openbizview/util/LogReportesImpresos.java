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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/*
 * Clase para imprimir log de reportes
 */
public class LogReportesImpresos {
	
	Connection con;
	PreparedStatement pstmt = null;
	String productName; //Manejador de base de datos
	
	/*
	 * Imprime log de reportes en la tabla bvt006
	 * Parámetros: codrepm desrep, jni, login
	 */
	public void insertBvt006(String codrep, String desrep, String login, String hora, String instancia)  {
        //Pool de conecciones JNDI(). Cambio de metodologia de conexion a Bd. Julio 2010
        try {
        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(Bd.JNDI);
            con = ds.getConnection();
     		String query = "";
            	query = "INSERT INTO Bvt006 VALUES (trim('" + codrep.toUpperCase() + "'),'" + desrep.toUpperCase() + "','" + login + "','" + hora + "'," + instancia + ")";
           
            pstmt = con.prepareStatement(query);
            //pstmt.setString(1, codrep.toUpperCase());
            //pstmt.setString(2, desrep.toUpperCase());
            //pstmt.setString(3, login);
            //pstmt.setInt(4, Integer.parseInt(instancia));
            //System.out.println(query);
            try {
                //Avisando
            	pstmt.executeUpdate();
             } catch (SQLException e)  {
            	e.printStackTrace();
            }
            
            pstmt.close();
            con.close();
        } catch (Exception e) {
        	e.printStackTrace();
        }	
    }

}
