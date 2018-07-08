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
    document.getElementById("formBvt003:vop").value="0";
    clearUpdateInput('formBvt003:codrol', 'white');
}

function enviar(vT0,vT1,vT2){
	  document.getElementById("formBvt003:codrol").value= rTrim(vT0);
	  document.getElementById("formBvt003:desrol").value= rTrim(vT1);
	  document.getElementById("formBvt003:vop").value= rTrim(vT2);
	  updateInput('formBvt003:codrol', '#b6c6c0');
	}

