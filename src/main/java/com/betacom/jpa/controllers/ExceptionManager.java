package com.betacom.jpa.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.betacom.jpa.dto.output.ResponseDTO;
import com.betacom.jpa.services.interfaces.IMessaggioServices;

import lombok.RequiredArgsConstructor;

// Proprietario: Infrastruttura condivisa
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionManager {
	private final IMessaggioServices msgS;

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ResponseDTO> handleAccessDenied(AccessDeniedException e) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(ResponseDTO.builder()
						.msg(msgS.get("auth.forbidden"))
						.build()
						);
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ResponseDTO> handleAuthentication(AuthenticationException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(ResponseDTO.builder()
						.msg(msgS.get("auth.unauthorized"))
						.build()
						);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResponseDTO> handleException(Exception e){
		return ResponseEntity.badRequest()
				.body(ResponseDTO.builder()
						.msg(msgS.get(e.getMessage()))
						.build()
						);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ResponseDTO> handleValidationException(MethodArgumentNotValidException e) {
		  String msg = e.getBindingResult()
	                .getFieldErrors()
	                .stream()
	                .findFirst()
	                .map(FieldError::getDefaultMessage)
	                .orElse("Errore di validazione");

		  return ResponseEntity.badRequest()
					.body(ResponseDTO.builder()
							.msg(msgS.get(msg))
							.build()
							);

	}

}
