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



function enviar(vT0,vT1){
	  document.getElementById("formBvtDashboardPublic:folder").value= rTrim(vT0);
	  document.getElementById("formBvtDashboardPublic:index").value= rTrim(vT1);
	}

function publicar(vT0){
	var contextpath = window.location.pathname.substring(0, window.location.pathname.indexOf("/",2));
	window.open(contextpath+vT0,'_blank');
}

