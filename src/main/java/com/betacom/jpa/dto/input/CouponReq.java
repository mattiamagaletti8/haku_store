package com.betacom.jpa.dto.input;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// ============================================================================
// PROPRIETARIO: Pier — Modulo Carrello (Carrello / DettaglioCarrello / Coupon)
// ============================================================================
@Setter
@Getter
@ToString
public class CouponReq {
	@NotNull(groups = ValidationGroups.Update.class, message = "coupon.no.id")
	private Integer id;

	@NotNull(groups = ValidationGroups.Create.class, message = "coupon.no.codice")
	@NotBlank(groups = ValidationGroups.Create.class, message = "coupon.no.codice")
	private String codice;

	// Stringa e non l'enum direttamente: il service la converte con TipologiaCoupon.valueOf(...),
	// cosi' un valore non valido diventa un errore di business gestito, non un'eccezione di deserializzazione JSON
	@NotNull(groups = ValidationGroups.Create.class, message = "coupon.no.tipologia")
	private String tipologia;

	@NotNull(groups = ValidationGroups.Create.class, message = "coupon.no.valore")
	// 0.01 e non 0.0: un coupon con valore zero non avrebbe senso di esistere
	@DecimalMin(value = "0.01", groups = { ValidationGroups.Create.class, ValidationGroups.Update.class }, message = "coupon.valore.invalid")
	private BigDecimal valore;

	@NotNull(groups = ValidationGroups.Create.class, message = "coupon.no.data.inizio")
	private LocalDateTime dataInizio;

	@NotNull(groups = ValidationGroups.Create.class, message = "coupon.no.data.fine")
	private LocalDateTime dataFine;

	// Nessuna validazione: facoltativo sia in creazione (default gestito nel service) sia in update
	private Boolean isAttivo;
}
