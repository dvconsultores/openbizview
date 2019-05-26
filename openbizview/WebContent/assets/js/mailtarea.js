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

function enviar(vT0,vT1,vT2,vT3,vT4,vT5,vT6,vT7,vT8,vT9,vT10,vT11,vT12,vT13){

	
	  if(vT5=='0'){
		  vT5='2';
	  }
	  if(vT10=='0'){
		  vT10='1';
	  }	  
	  
	  if(vT12=='0'){
		  document.getElementById("formmailconfig:btnStartTask").style.display = "none";
		  document.getElementById("formmailconfig:btnStopTask").style.display = "";
	  } else {
		  document.getElementById("formmailconfig:btnStartTask").style.display = "";
		  document.getElementById("formmailconfig:btnStopTask").style.display = "none";
	  }
	 
	  listarBt2();
	  //Bloquea trigger, codigo de tarea y reporte
	  //Se bloquea reporte para no tener que crear operaciones nuevamente de leer parámtros
	  //Si desea cambiar reporte, eliminar tarea y crear una nueva
	  updateInput('formmailconfig:tabView:tarea', '#b6c6c0');
	  updateInput('formmailconfig:tabView:prg', '#b6c6c0');

	  //
	  //
	  console.log(vT4);
	  document.getElementById("formmailconfig:tabView:tarea").value= rTrim(vT0);
	  document.getElementById("formmailconfig:tabView:prg").value= rTrim(vT1);
	  //document.getElementById("formmailconfig:tabView:hora").value= rTrim(vT2);
	  //document.getElementById("formmailconfig:tabView:minutos").value= rTrim(vT3);
	  document.getElementById("formmailconfig:tabView:idfrecuencia_label").innerHTML= rTrim(vT4);
	  document.getElementById("formmailconfig:tabView:dias_label").innerHTML= rTrim(vT5);
	  document.getElementById("formmailconfig:tabView:reporte1_input").value= rTrim(vT6);
	  document.getElementById("formmailconfig:tabView:idgrupo1_input").value= rTrim(vT7);
	  document.getElementById("formmailconfig:tabView:asunto").value= vT8;
	  document.getElementById("formmailconfig:tabView:contenido_input").innerHTML= vT9;
	  document.getElementById("formmailconfig:tabView:diames_label").innerHTML= rTrim(vT10);
	  document.getElementById("formmailconfig:tabView:ini_input").value= rTrim(vT11);	
	  document.getElementById("formmailconfig:tabView:horarep").value= rTrim(vT13);	
	  //document.getElementById("formmailconfig:ruta").value = "NA";

	}

    function listarBt1(){
    	document.getElementById("bt1").style.display = '';
    	document.getElementById("bt2").style.display = 'none';
    }
    
    function listarBt2(){
    	document.getElementById("bt1").style.display = 'none';
    	document.getElementById("bt2").style.display = '';  
    }


	
	function inputs(){
		var opt = document.getElementById("formmailconfig:tabView:opciones").value;
		//alert(opt);
        if(opt=='2'){
			document.getElementById("formmailconfig:tabView:asunto").value = "NA";
			document.getElementById("formmailconfig:tabView:contenido_input").value= "NA";
		} 
	}

	/*
	function loadT(){
		document.getElementById("formmailconfig:ruta").value = "NA";
	}*/
