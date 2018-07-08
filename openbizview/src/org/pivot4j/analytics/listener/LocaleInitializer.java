/*
 * ====================================================================
 * This software is subject to the terms of the Common Public License
 * Agreement, available at the following URL:
 *   http://www.opensource.org/licenses/cpl.html .
 * You must accept the terms of that agreement to use this software.
 * ====================================================================
 */
package org.pivot4j.analytics.listener;

import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import org.openbizview.util.Bd;

public class LocaleInitializer implements PhaseListener {

	private static final long serialVersionUID = -2477093113131236331L;

	/**
	 * @return
	 */
	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

	/**
	 * @param event
	 */
	@Override
	public void beforePhase(PhaseEvent event) {
	}

	/**
	 * @param event
	 */
//	@Override
//	public void afterPhase(PhaseEvent event) {
//		FacesContext context = FacesContext.getCurrentInstance();
//		ExternalContext externalContext = context.getExternalContext();
//
//		Settings settings = (Settings) externalContext.getApplicationMap().get(
//				"settings");
//
//		Locale locale = null;
//
//		HttpSession session = (HttpSession) externalContext.getSession(false);
//
//		if (session != null) {
//			String key = settings.getLocaleAttributeName();
//
//			if (key != null) {
//				Object value = session.getAttribute(key);
//				if (value instanceof Locale) {
//					locale = (Locale) value;
//				} else if (value != null) {
//					String[] args = value.toString().split("_");
//
//					if (args.length == 1) {
//						locale = new Locale(args[0]);
//					} else if (args.length == 2) {
//						locale = new Locale(args[0], args[1]);
//					} else if (args.length == 3) {
//						locale = new Locale(args[0], args[1], args[2]);
//					}
//				}
//			}
//		}
//
//		if (locale != null && context.getViewRoot() != null) {
//			context.getViewRoot().setLocale(locale);
//		}
//	}
	
	
	/**
	 * @param event
	 */
	@Override
	public void afterPhase(PhaseEvent event) {
		FacesContext context = FacesContext.getCurrentInstance();

		String lenguaje;
		String pais;
		Bd bd = new Bd();
		Locale locale = null;
		if(bd.getBundle()){
    		lenguaje = "es";
    		pais = "VEN";
    	} else {
    		lenguaje = "en";
    		pais = "US";        		
    	}
    	//System.out.println(localidad);
		locale = new Locale(lenguaje, pais);
		context.getViewRoot().setLocale(locale);
	
	}
	
}
