package org.openbizview.util;

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

import java.io.IOException;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name="navigateBean")
@SessionScoped
public class NavigationController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private String targetUrl;
	   private String pageToDisplay = "bvt001";

	   public String getPageToDisplay(){
	      return this.pageToDisplay;
	   }
	   
	   public void setPageToDisplay(String pageToDisplay){
	      this.pageToDisplay = pageToDisplay;
	   }
	   
	   public void href(String page) throws IOException{
		   FacesContext.getCurrentInstance().getExternalContext().redirect(page + ".xhtml"); 
	   }
}
