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

var milisec = 120000;


function limpiar(){
    limpiarInput('formmailconfig:usuario');
    limpiarInput('formmailconfig:clave');
    limpiarInput('formmailconfig:starttlsenable');
    limpiarInput('formmailconfig:auth');
    limpiarInput('formmailconfig:host');
    limpiarInput('formmailconfig:puerto');
    document.getElementById("formmailconfig:vop").value='0';
}



function borrar(){
	document.getElementById("formmailconfig:usuario").value= " ";
	document.getElementById("formmailconfig:clave").value= " ";
	document.getElementById("formmailconfig:starttlsenable").value= " ";
	document.getElementById("formmailconfig:auth").value= " ";
	document.getElementById("formmailconfig:host").value= " ";
	document.getElementById("formmailconfig:puerto").value= " ";
    
    setTimeout("document.getElementById('formmailconfig:usuario').value=''",100);
   	setTimeout("document.getElementById('formmailconfig:clave').value=''",100);  
   	setTimeout("document.getElementById('formmailconfig:starttlsenable').value=''",100);
   	setTimeout("document.getElementById('formmailconfig:auth').value=''",100);  
   	setTimeout("document.getElementById('formmailconfig:host').value=''",100);
   	setTimeout("document.getElementById('formmailconfig:puerto').value=''",100);  
}


function enviar(vT0,vT1,vT2,vT3,vT4,vT5,vT6){
	  document.getElementById("formmailconfig:usuario").value= rTrim(vT0);
	  document.getElementById("formmailconfig:clave").value= rTrim(vT1);
	  document.getElementById("formmailconfig:starttlsenable").value= rTrim(vT2);
	  document.getElementById("formmailconfig:auth").value= rTrim(vT3);
	  document.getElementById("formmailconfig:host").value= rTrim(vT4);
	  document.getElementById("formmailconfig:puerto").value= rTrim(vT5);
	  document.getElementById("formmailconfig:vop").value= rTrim(vT6);
	}



