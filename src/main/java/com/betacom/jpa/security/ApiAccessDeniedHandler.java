package com.betacom.jpa.security;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.betacom.jpa.services.interfaces.IMessaggioServices;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

// Proprietario: Sara e Mattia
@RequiredArgsConstructor
@Component
public class ApiAccessDeniedHandler implements AccessDeniedHandler {

	private final IMessaggioServices msgS;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
			throws IOException {
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write(JsonMsg.responseDTOJson(msgS.get("auth.forbidden")));
	}
}
