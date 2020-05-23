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
	

package org.openbizview.util;
	
	import javax.faces.bean.ManagedBean;
	import javax.faces.bean.RequestScoped;
	import javax.faces.model.SelectItem;
	
	
	@ManagedBean
	@RequestScoped
	public class ListBean extends Bd {

	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////LISTA DE TAREAS////////////////////////////////////////////////		
	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	//Selecciona las opciones de paginación de todas las tablas
	private SelectItem[] frecuencia = new SelectItem[]{
	new SelectItem("0", getMessage("mailtareaDiario")),
	new SelectItem("1", getMessage("mailtareaSemanal")),
	new SelectItem("2", getMessage("mailtareaPersonalizada")),
	new SelectItem("3", getMessage("mailtareaHoraRep")),
	new SelectItem("4", getMessage("mailimes1")),
	new SelectItem("5", getMessage("maillidiasSelect"))};
	
	/**
	 * @return the frecuencia
	 */
	public SelectItem[] getFrecuencia() {
		return frecuencia;
	}
	
	/**
	 * @param frecuencia the frecuencia to set
	 */
	public void setFrecuencia(SelectItem[] frecuencia) {
		this.frecuencia = frecuencia;
	}
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////DIAS DE LA SEMANA////////////////////////////////////////////////		
	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	//Selecciona las opciones de paginación de todas las tablas
	private SelectItem[] diassemana = new SelectItem[]{
	new SelectItem("2", getMessage("mailtarealunes")),
	new SelectItem("3", getMessage("mailtareamartes")),
	new SelectItem("4", getMessage("mailtareamiercoles")),
	new SelectItem("5", getMessage("mailtareajueves")),
	new SelectItem("6", getMessage("mailtareaviernes")),
	new SelectItem("7", getMessage("mailtareasabado")),
	new SelectItem("1", getMessage("mailtareadomingo"))};
	
	/**
	 * @return the diassemana
	 */
	public SelectItem[] getDiassemana() {
		return diassemana;
	}
	
	/**
	 * @param diassemana the diassemana to set
	 */
	public void setDiassemana(SelectItem[] diassemana) {
		this.diassemana = diassemana;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////DIAS DEL MES//////////////////////////////////////////////////////////////		
	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	//Selecciona las opciones de paginación de todas las tablas
	private SelectItem[] diasmes = new SelectItem[]{
	new SelectItem("1", getMessage("mailidiap1")),
	new SelectItem("2", getMessage("mailidiap2")),
	new SelectItem("3", getMessage("mailidiap3")),
	new SelectItem("4", getMessage("mailidiap4")),
	new SelectItem("5", getMessage("mailidiap5")),
	new SelectItem("6", getMessage("mailidiap6")),
	new SelectItem("7", getMessage("mailidiap7")),
	new SelectItem("8", getMessage("mailidiap8")),
	new SelectItem("9", getMessage("mailidiap9")),
	new SelectItem("10", getMessage("mailidiap10")),
	new SelectItem("11", getMessage("mailidiap11")),
	new SelectItem("12", getMessage("mailidiap12")),
	new SelectItem("13", getMessage("mailidiap13")),
	new SelectItem("14", getMessage("mailidiap14")),
	new SelectItem("15", getMessage("mailidiap15")),
	new SelectItem("16", getMessage("mailidiap16")),
	new SelectItem("17", getMessage("mailidiap17")),
	new SelectItem("18", getMessage("mailidiap18")),
	new SelectItem("19", getMessage("mailidiap19")),
	new SelectItem("20", getMessage("mailidiap20")),
	new SelectItem("21", getMessage("mailidiap21")),
	new SelectItem("22", getMessage("mailidiap22")),
	new SelectItem("23", getMessage("mailidiap23")),
	new SelectItem("24", getMessage("mailidiap24")),
	new SelectItem("25", getMessage("mailidiap25")),
	new SelectItem("26", getMessage("mailidiap26")),
	new SelectItem("27", getMessage("mailidiap27")),
	new SelectItem("28", getMessage("mailidiap28")),
	new SelectItem("29", getMessage("mailidiap29")),
	new SelectItem("30", getMessage("mailidiap30"))};
	
	/**
	 * @return the diasmes
	 */
	public SelectItem[] getDiasmes() {
		return diasmes;
	}
	
	/**
	 * @param diasmes the diasmes to set
	 */
	public void setDiasmes(SelectItem[] diasmes) {
		this.diasmes = diasmes;
	}


    //////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////lista de opciones de tareas envíos//////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Selecciona las opciones de paginación de todas las tablas
		private SelectItem[] opcTareas = new SelectItem[]{
		new SelectItem("1", getMessage("mailtareaopc1")),
		new SelectItem("2", getMessage("mailtareaopc2"))};

		/**
		 * @return the opcTareas
		 */
		public SelectItem[] getOpcTareas() {
			return opcTareas;
		}

		/**
		 * @param opcTareas the opcTareas to set
		 */
		public void setOpcTareas(SelectItem[] opcTareas) {
			this.opcTareas = opcTareas;
		}
		
//////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////lista de opciones formato//////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////
//Selecciona las opciones de paginación de todas las tablas
	private SelectItem[] opcEmiters = new SelectItem[]{
	new SelectItem("pdf", getMessage("maltareaoutputPdf")),
	new SelectItem("xls", getMessage("maltareaoutputXls")),
	new SelectItem("xlsx", getMessage("maltareaoutputXlsx")),
	new SelectItem("ods", getMessage("maltareaoutputOds"))};

	/**
	 * @return the opcEmiters
	 */
	public SelectItem[] getOpcEmiters() {
		return opcEmiters;
	}

	/**
	 * @param opcEmiters the opcEmiters to set
	 */
	public void setOpcEmiters(SelectItem[] opcEmiters) {
		this.opcEmiters = opcEmiters;
	}
	
	
	
	}
