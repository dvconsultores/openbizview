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

function enviar(vT0,vT1,vT2,vT3,vT4,vT5){
	  document.getElementById("formPivotDef:grupo_input").value= rTrim(vT0);
	  document.getElementById("formPivotDef:context").value= rTrim(vT1);
	  document.getElementById("formPivotDef:desc").value= rTrim(vT2);
	  document.getElementById("formPivotDef:jar").value= rTrim(vT3);
	  document.getElementById("formPivotDef:index").value= rTrim(vT4);  
	  document.getElementById("formPivotDef:vop").value= rTrim(vT5);	 
	  updateInput('formPivotDef:context', '#b6c6c0');
	}

function toggleUpload(vT0){
	if(vT0=="1"){
		document.getElementById('upload1').style.display = '';
		document.getElementById('upload2').style.display = 'none';
	} else {
		document.getElementById('upload1').style.display = 'none';
		document.getElementById('upload2').style.display = '';
	}
}