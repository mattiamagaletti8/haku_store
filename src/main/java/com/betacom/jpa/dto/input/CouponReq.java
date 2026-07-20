package com.betacom.jpa.dto.input;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// Proprietario: Pier
@Setter
@Getter
@ToString
public class CouponReq {
	@NotNull(groups = ValidationGroups.Update.class, message = "coupon.no.id")
	private Integer id;

	@NotNull(groups = ValidationGroups.Create.class, message = "coupon.no.codice")
	@NotBlank(groups = ValidationGroups.Create.class, message = "coupon.no.codice")
	private String codice;

	@NotNull(groups = ValidationGroups.Create.class, message = "coupon.no.tipologia")
	private String tipologia;

	@NotNull(groups = ValidationGroups.Create.class, message = "coupon.no.valore")
	@DecimalMin(value = "0.01", groups = { ValidationGroups.Create.class, ValidationGroups.Update.class }, message = "coupon.valore.invalid")
	private BigDecimal valore;

	@NotNull(groups = ValidationGroups.Create.class, message = "coupon.no.data.inizio")
	private LocalDateTime dataInizio;

	@NotNull(groups = ValidationGroups.Create.class, message = "coupon.no.data.fine")
	private LocalDateTime dataFine;

	private Boolean isAttivo;
}
