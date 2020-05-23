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
	
	function limpiar()
	{  //Llamado por el boton limpiar
	    document.getElementById("formBvt001:vop").value="0";
	    clearUpdateInput('formBvt001:codrep', 'white');
	}
	
	function enviar(vT0,vT1,vT2,vT3,vT4){
		  document.getElementById("combo").value= rTrim(vT0);
		  document.getElementById("codrep").value= rTrim(vT1);
		  document.getElementById("desrep").value= rTrim(vT2);
		  document.getElementById("comrep").value= rTrim(vT3);
		  document.getElementById("vop").value= rTrim(vT4);
		  
		  updateInput('codrep', '#b6c6c0');
		  $('#myModalForm').modal(); 
	}
	
	function imprimir(rep, usuario, rol, descripcion, instancia, locale){
		//alert(locale);
	  // Si el mensaje que retorna es acceso
	    window.open('../ct/reportes.jsp?reporte='+rep+'.rptdesign'
				+ "&usuario=" + usuario 
				+ "&rol=" + rol
				+ "&replog=" + rep
				+ "&descripcion=" + descripcion
				+ "&hora=" + displayTime(locale)
				+ "&instancia=" + instancia);
	   //alert(displayTime());
	}
	
	

	//Muestra la hora del lado del cliente
	function displayTime(vT0)
	   {
	       var localTime = new Date();
	       var month= localTime.getMonth()+1; 
	       var cadena = month.toString();
	       var hora = localTime.getHours();
	       var minutos = localTime.getMinutes();
		   //var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun",  "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
		   var monthNames = ["01", "02", "03", "04", "05", "06",  "07", "08", "09", "10", "11", "12"];
		   if(vT0=='es'){
			   monthNames = ["01", "02", "03", "04", "05", "06",  "07", "08", "09", "10", "11", "12"];
		   } else {
			   monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun",  "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
		   }
		   var mes = monthNames[localTime.getMonth()];
	      
	       if(cadena.length==1){
	       	month = '0'+month;
	       }
	       if(hora.length==1){
	    	   hora = '0'+hora;
	          }
	       if(minutos.length==1){
	    	   minutos = '0'+minutos;
	          } 
	       
	       var fechaHora = localTime.getDate()+"/"+mes+"/"+localTime.getFullYear()+" "+formatAMPM(localTime);
	     
		   //alert(month + ' ' + mes);
		 
		   return fechaHora;
	   
	   } 
	
	function formatAMPM(date) {
		  var hours = date.getHours();
		  var minutes = date.getMinutes();
		  var segundos = date.getSeconds();
		  var ampm = hours >= 12 ? 'PM' : 'AM';
		  hours = hours % 12;
		  hours = hours ? hours : 12; // the hour '0' should be '12'
		  minutes = minutes < 10 ? '0'+minutes : minutes;
		  var strTime = hours + ':' + minutes + ':' + segundos + " " + ampm;
		  return strTime;
		}
	
	function log(){	
		
		document.getElementById("formBvt001:codrep").value= "1";
		document.getElementById("formBvt001:desrep").value= "1";
		document.getElementById("formBvt001:comrep").value= "1";
	
	    setTimeout("document.getElementById('formBvt001:codrep').value=''",300);
	   	setTimeout("document.getElementById('formBvt001:desrep').value=''",300);  
	   	setTimeout("document.getElementById('formBvt001:comrep').value=''",300);  
	   	
	}
	
	
	function detalle(vT0,vT1,vT2,vT3){
		$("#txt_det_1").text(vT0);
		$("#txt_det_2").text(vT1);
		$("#txt_det_3").text(vT2);
		$("#txt_det_4").text(vT3);
	}
	
	
	
