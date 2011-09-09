package com.yuksekisler.interfaces.web.jsf;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public final class Utils {

	@SuppressWarnings("unchecked")
	public static void addToFlashScopeMessages(FacesMessage message) {
		List<FacesMessage> messages = (List<FacesMessage>) FacesContext
				.getCurrentInstance().getExternalContext().getFlash()
				.get(Application.FLASH_MESSAGE);
		if (messages == null) {
			messages = new ArrayList<FacesMessage>();
			FacesContext.getCurrentInstance().getExternalContext().getFlash()
					.put(Application.FLASH_MESSAGE, messages);
		}
		messages.add(message);
	}

	public static void addFacesMessage(String targetComponent,
			FacesMessage.Severity severity, String summary, String detail) {
		FacesMessage facesMessage = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(targetComponent,
				facesMessage);
	}
}
