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

	


