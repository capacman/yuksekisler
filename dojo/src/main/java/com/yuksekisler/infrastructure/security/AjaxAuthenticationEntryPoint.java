package com.yuksekisler.infrastructure.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

public class AjaxAuthenticationEntryPoint extends
		LoginUrlAuthenticationEntryPoint {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AjaxAuthenticationEntryPoint.class);

	@Override
	public void commence(HttpServletRequest arg0, HttpServletResponse arg1,
			AuthenticationException arg2) throws IOException, ServletException {
		LOGGER.debug("commence called and ajaxRequest={}",
				arg0.getParameter("ajaxRequest"));
		if (arg0.getHeader("X-Requested-With") == null
				|| !arg0.getHeader("X-Requested-With").equalsIgnoreCase(
						"XMLHttpRequest"))
			super.commence(arg0, arg1, arg2);
		else
			arg1.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	}

}
