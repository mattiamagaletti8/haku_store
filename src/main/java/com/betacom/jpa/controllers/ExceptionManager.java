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

// ============================================================================
// PROPRIETARIO: Infrastruttura condivisa (non appartiene a una sola persona)
// ============================================================================
// Non un controller REST, ma un consigliere globale (@RestControllerAdvice): intercetta
// le eccezioni lanciate da TUTTI i controller del backend e le traduce in risposte JSON coerenti,
// tutte nella stessa forma ResponseDTO{msg} usata anche dagli endpoint di successo
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionManager {
	// Collegamento verso il sistema i18n: ogni messaggio restituito passa da qui, mai testo grezzo
	private final IMessaggioServices msgS;

	/**
	 * AccessDeniedException (es. da @PreAuthorize) va gestita qui con lo stesso status/formato
	 * di ApiAccessDeniedHandler: se la si lasciasse cadere nel catch-all sotto verrebbe tradotta
	 * in un 400 generico invece di un 403, perche' @RestControllerAdvice intercetta l'eccezione
	 * prima che possa risalire fino alla security filter chain.
	 */
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ResponseDTO> handleAccessDenied(AccessDeniedException e) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(ResponseDTO.builder()
						.msg(msgS.get("auth.forbidden"))
						.build()
						);
	}

	// Stesso principio del precedente, ma per gli errori di autenticazione intercettati
	// a livello di controller invece che dal filtro JWT
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ResponseDTO> handleAuthentication(AuthenticationException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(ResponseDTO.builder()
						.msg(msgS.get("auth.unauthorized"))
						.build()
						);
	}

	// Catch-all: intercetta ogni ApiException lanciata da qualunque service (categoria.ntfnd,
	// variante.stock.insufficient, coupon.expired...) e la traduce in 400 con il messaggio tradotto.
	// e.getMessage() e' proprio il CODICE passato al costruttore di ApiException, non testo libero
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResponseDTO> handleException(Exception e){
		return ResponseEntity.badRequest()
				.body(ResponseDTO.builder()
						.msg(msgS.get(e.getMessage()))
						.build()
						);
	}

	// Gestisce gli errori di @Valid/@Validated (es. campi @NotNull mancanti): prende il PRIMO
	// errore di campo trovato, non l'elenco completo, per semplicita' lato client
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
