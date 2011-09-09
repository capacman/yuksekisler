package com.yuksekisler.interfaces.web.jsf;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(Application.class);
	public static final String FLASH_MESSAGE = "facesContext.flash.messages";
	//private List<Locale> locales;

//	public Application(List<Locale> locales) {
//		this.locales = locales;
//	}

//	public List<Locale> getLocales() {
//		LOGGER.debug("get Locales called");
//		return locales;
//	}
	
	public Application(){
		
	}

	@SuppressWarnings("unchecked")
	public void restoreViewMessages(ComponentSystemEvent event) {
		LOGGER.debug("restoreViewMessages called!");
		List<FacesMessage> messages = (List<FacesMessage>) FacesContext
				.getCurrentInstance().getExternalContext().getFlash()
				.get(FLASH_MESSAGE);
		if (messages != null) {
			for (FacesMessage flashScopeEntry : messages) {
				FacesContext.getCurrentInstance().addMessage(null,
						flashScopeEntry);
			}
		}
	}
}
