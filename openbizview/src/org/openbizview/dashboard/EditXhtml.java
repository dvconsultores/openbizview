package org.openbizview.dashboard;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;



public class EditXhtml {
	
	/**
	 * Edita el archivo index del dashboard colocando opción
	 * para leer del servicio web y verificar la validez de la Url
	 * @param file
	 */
	public void editFile(String file) {
		 File f=new File(file);
	        
	        FileInputStream fs = null;
	        InputStreamReader in = null;
	        BufferedReader br = null;
	        
	        StringBuffer sb = new StringBuffer();
	        
	        String textinLine;
	        
	        try {
	             fs = new FileInputStream(f);
	             in = new InputStreamReader(fs);
	             br = new BufferedReader(in);
	            
	            while(true)
	            {
	                textinLine=br.readLine();
	                if(textinLine==null)
	                    break;
	                sb.append(textinLine+"\n");
	            }
	              String textToEdit1 = "</html>";
	              int cnt1 = sb.indexOf(textToEdit1);
	              sb.replace(cnt1,cnt1+textToEdit1.length(),"<script>\n" + 
	              		"setTimeout(function() {\n" + 
	              		"	  location.reload();\n" + 
	              		"	}, 36000000);\n" + 
	              		"	           	\n" + 
	              		"	window.onload = function () {\n" + 
	              		"		var contextpath = window.location.pathname.substring(0, window.location.pathname.indexOf(\"/\",2));\n" + 
	              		"		var params = window.location.search\n" + 
	              		"		var protocol = window.location.protocol;\n" + 
	              		"		var host = protocol + \"//\" + window.location.host + contextpath + \"/login\";\n" + 
	              		"		 $.ajax({\n" + 
	              		"	         type:\"GET\",\n" + 
	              		"	         url: contextpath + \"/db/readFromDashboard\"+params\n" + 
	              		"	     })\n" + 
	              		"	     .done(function (data) {\n" + 
	              		"	    	 if(data.length==0){\n" + 
	              		"	    		 $( location ).attr(\"href\", host);\n" + 
	              		"	    	 }\n" + 
	              		"	     });\n" + 
	              		"	};\n" + 
	              		"</script>\n</html>");


	              fs.close();
	              in.close();
	              br.close();

	            } catch (FileNotFoundException e) {
	              e.printStackTrace();
	            } catch (IOException e) {
	              e.printStackTrace();
	            }
	            
	            try{
	                FileWriter fstream = new FileWriter(f);
	                BufferedWriter outobj = new BufferedWriter(fstream);
	                outobj.write(sb.toString());
	                outobj.close();
	                
	            }catch (Exception e){
	              System.err.println("Error: " + e.getMessage());
	            }
	      }
		

}
