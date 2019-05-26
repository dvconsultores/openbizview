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
    document.getElementById("formmaillista:vop").value='0';
}



function borrar(){
	document.getElementById("formmaillista:idgrupo_input").value= " ";
	document.getElementById("formmaillista:idmail").value= "1";
	document.getElementById("formmaillista:mail").value= "a@a.com";
    
	setTimeout("document.getElementById('formmaillista:idgrupo_input').value=''",100);
    setTimeout("document.getElementById('formmaillista:idmail').value=''",100);
   	setTimeout("document.getElementById('formmaillista:mail').value=''",100);  
   	updateInput('formmaillista:idgrupo_input', 'white');
    updateInput('formmaillista:idmail', 'white');
}


function enviar(vT0,vT1,vT2,vT3){
	  document.getElementById("formmaillista:idgrupo_input").value= rTrim(vT0);
	  document.getElementById("formmaillista:idmail").value= rTrim(vT1);
	  document.getElementById("formmaillista:mail").value= rTrim(vT2);
	  document.getElementById("formmaillista:vop").value= rTrim(vT3);
	  updateInput('formmaillista:idgrupo_input', '#F2F2F2');
	  updateInput('formmaillista:idmail', '#F2F2F2');
	}


