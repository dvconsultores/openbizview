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
	
	function view_toggle1(){
		   document.getElementById('tg1').style.display = 'none';
		   document.getElementById('tg2').style.display = '';
		   jQuery('#p3c').hide( "fast" ); //oculto mediante id
		   jQuery('#p9c').removeClass( "col-lg-9 col-md-9 col-sm-6 col-xs-12 margin-top-5" ).addClass( "col-md-12 col-sm-6 col-xs-12 margin-top-5" );
	}
	
	function view_toggle2(){ 
		   document.getElementById('tg1').style.display = '';
		   document.getElementById('tg2').style.display = 'none';
		   jQuery('#p3c').fadeIn(1200); //oculto mediante id
		   jQuery('#p9c').removeClass( "col-lg-12 col-md-12 col-sm-6 col-xs-12 margin-top-5" ).addClass( "col-md-9 col-sm-6 col-xs-12 margin-top-5" );
	}
	
	
	/**
	 * Workarround to filter tree in hierarchy.xhtml, because primefaces filter did not work
	 * not for an issue. The way loads from olap
	 * @param filter
	 * @returns
	 * September 2017
	 */
	function filterFunction(vTid) {	    
		
		    $('#textsearch').keyup(function(){
		        var searchText = $(this).val();
		        
		        $("." + vTid + " li").each(function(){
		            
		            var currentLiText = $(this).text().toUpperCase(),
		                showCurrentLi = currentLiText.indexOf(searchText.toUpperCase()) != -1;
		            
		            $(this).toggle(showCurrentLi);
		        });     
		    });
	}
 

	
