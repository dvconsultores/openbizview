	/*
	 * Funciones estandart que pueden ser utilizadas en todas las páginas
	 * 
	 */
	/*
	 *  Copyright (C) 2004  DVCONSULTORES
	 */
	
	

	/******************************************************************************
	  *********************Funciones genéricas************
	 ******************************************************************************/
			
	function fm_check(vTcheck){	
		var chkBoxes = $('input[name='+vTcheck+']');
	    chkBoxes.prop("checked", !chkBoxes.prop("checked"));
	}
	
	/** ******  /cookies  *********************** **/
	
	function getLanguage(){
		if(!$.cookie('obv_language')){
			$.cookie("obv_language", navigator.language, { expires: 365 });
		}
	}

	function setLanguage(vT0){
		$.cookie("obv_language", vT0, { expires: 365 });
	}

	
	
	

	/******************************************************************************
	  *********************Funciones genéricas************
	 ******************************************************************************/
	
	//Modal delete
	function modalDelete(){
		$("#myModalDelete").modal();
	}
	
	function myModalAccesRol(){
		$("#myModalAccesRol").modal();
	}
	
	function myModalAccesRolHide(){
		$("#myModalAccesRol").modal('hide');
	}
	

	//Modal delete
	function modalHide(){
		$("#myModalDelete").modal('hide');
	}

	
	
	function rTrim(sStr){
	    while (sStr.charAt(sStr.length - 1) == " ")
	        sStr = sStr.substr(0, sStr.length - 1);
	    return sStr;
	}
	
	function updateInput(vinputid, vcolor){
		document.getElementById(vinputid).style.backgroundColor = vcolor;
		document.getElementById(vinputid).readOnly = true;
	}
	
	function clearUpdateInput(vinputid, vcolor){
		document.getElementById(vinputid).style.backgroundColor = vcolor;
		document.getElementById(vinputid).readOnly = false;
	}
	
	function fm_check(vTcheck){	
		var chkBoxes = $('input[name='+vTcheck+']');
	    chkBoxes.prop("checked", !chkBoxes.prop("checked"));
	}
	
	
