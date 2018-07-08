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
    clearUpdateInput('formBvt002:coduser', 'white');
}

function enviar(vT0,vT1,vT2,vT3,vT4,vT5,vT6){
	  document.getElementById("formBvt002:coduser").value= rTrim(vT0);
	  document.getElementById("formBvt002:desuser").value= rTrim(vT1);
	  document.getElementById("formBvt002:cluser").value= rTrim(vT2);
	  document.getElementById("formBvt002:rol_input").value= rTrim(vT3);
	  document.getElementById("formBvt002:vop").value= rTrim(vT4);
	  document.getElementById("formBvt002:mail").value= rTrim(vT5);
	  document.getElementById("formBvt002:instancia_input").value= rTrim(vT6);
	  document.getElementById("formBvt002:roladic_input").value= "ABC";
	  updateInput('formBvt002:tabView:coduser', '#F2F2F2');
	  updateInput('formBvt002:tabView:cluser', '#F2F2F2');
	}

function guardar(){
	//alert('aa');
	document.getElementById("formBvt002:roladic_input").value= "ABC";
}
