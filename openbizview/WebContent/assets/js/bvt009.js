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

function enviar(vT0,vT1,vT2,vT3,vT4,vT5,vT6,vT7){
	  document.getElementById("formPivotDef:nombre").value= rTrim(vT0);
	  document.getElementById("formPivotDef:desc").value= rTrim(vT1);
	  document.getElementById("formPivotDef:url").value= rTrim(vT2);
	  document.getElementById("formPivotDef:driver").value= rTrim(vT3);
	  document.getElementById("formPivotDef:usuario").value= rTrim(vT4);
	  document.getElementById("formPivotDef:clave").value= rTrim(vT5);
	  document.getElementById("formPivotDef:cube").value= rTrim(vT6);	  
	  document.getElementById("formPivotDef:vop").value= rTrim(vT7);	 
	  updateInput('formPivotDef:nombre', '#b6c6c0');
	}

