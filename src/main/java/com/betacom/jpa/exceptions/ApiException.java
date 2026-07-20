package com.betacom.jpa.exceptions;

// Proprietario: Infrastruttura condivisa
public class ApiException extends RuntimeException {

	public ApiException() {
		super();
	}

	public ApiException(String message) {
		super(message);
	}

}
