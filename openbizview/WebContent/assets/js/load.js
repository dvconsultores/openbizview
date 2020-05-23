	/*
	 * Funciones estandart que pueden ser utilizadas en todas las páginas
	 * 
	 */
	/*
	 *  Copyright (C) 2011 - 2016  GLOBAL FORGE
	 */
	
	

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
	
	function HideUpload(){
		jQuery('#upload').hide("linear"); //oculto mediante id
	}
	
	

	
