package com.yuksekisler.interfaces.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class ContentNegotiationInterceptor extends HandlerInterceptorAdapter {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ContentNegotiationInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		if (!request.getRequestURI().startsWith("/login.html")
				&& (request.getHeader("X-Requested-With") == null || !request
						.getHeader("X-Requested-With").equalsIgnoreCase(
								"XMLHttpRequest"))) {
			request.getRequestDispatcher("/index.jsp").forward(request,
					response);
			return false;
		}
		return true;
	}
}
