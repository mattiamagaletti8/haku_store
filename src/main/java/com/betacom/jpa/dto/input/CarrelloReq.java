package com.betacom.jpa.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// Proprietario: Pier
@Setter
@Getter
@ToString
public class CarrelloReq {
	@NotNull(groups = ValidationGroups.Coupon.class, message = "carrello.no.codice.coupon")
	@NotBlank(groups = ValidationGroups.Coupon.class, message = "carrello.no.codice.coupon")
	private String codiceCoupon;
}
