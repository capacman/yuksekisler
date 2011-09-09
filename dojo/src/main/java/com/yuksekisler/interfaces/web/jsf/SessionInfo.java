package com.yuksekisler.interfaces.web.jsf;

import java.util.Date;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.LocaleResolver;

import com.yuksekisler.domain.employee.Employee;
import com.yuksekisler.infrastructure.security.GrantedAuthorityImpl;

public class SessionInfo {
	private static final String EQUIPMENT = "equipment";
	private static final String EMPLOYEE = "employee";
	private static final String WORK = "work";
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SessionInfo.class);

	private Locale locale;
	private LocaleResolver localeResolver;
	private MessageSource messageSource;
	private AuthenticationManager authenticationManager;

	public String login(Credentials credentials) {
		try {
			Authentication request = new UsernamePasswordAuthenticationToken(
					credentials.getUsername(), credentials.getPassword());
			Authentication result = authenticationManager.authenticate(request);
			SecurityContextHolder.getContext().setAuthentication(result);

		} catch (AuthenticationException e) {
			// TODO: handle blocked account exception
			LOGGER.info("authentication failed", e);
			String message = messageSource.getMessage("loginfailed", null,
					getLocale());
			Utils.addFacesMessage(null, FacesMessage.SEVERITY_ERROR, message,
					"");
			return null;
		}
		return "success";
	}

	public Locale getLocale() {
		LOGGER.info("locale requestet");
		if (locale == null)
			locale = localeResolver
					.resolveLocale((HttpServletRequest) FacesContext
							.getCurrentInstance().getExternalContext()
							.getRequest());
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
		localeResolver.setLocale((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest(),
				(HttpServletResponse) FacesContext.getCurrentInstance()
						.getExternalContext().getResponse(), this.locale);
	}

	public void setLocaleResolver(LocaleResolver localeResolver) {
		this.localeResolver = localeResolver;
	}

	public Date getCurrentDate() {
		return new Date();
	}

	public Employee getUser() {
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		if (authentication != null) {
			return (Employee) authentication.getPrincipal();
		} else {
			return null;
		}
	}

	public boolean isAdmin() {
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		if (authentication != null) {
			return authentication.getAuthorities().contains(
					GrantedAuthorityImpl.ADMIN);
		}
		return false;
	}

	public String navigationToolBarNewPart() {
		String viewId = FacesContext.getCurrentInstance().getViewRoot()
				.getViewId();
		if (viewId != null && viewId.contains(EQUIPMENT))
			return getLocalizedString(EQUIPMENT);
		if (viewId != null && viewId.contains(EMPLOYEE))
			return getLocalizedString(EMPLOYEE);
		if (viewId != null && viewId.contains(WORK))
			return getLocalizedString(WORK);
		return null;
	}

	private String getLocalizedString(String cons) {
		return messageSource.getMessage(cons, null, cons, getLocale());
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
}
