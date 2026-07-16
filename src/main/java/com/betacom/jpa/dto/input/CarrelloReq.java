package com.betacom.jpa.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// ============================================================================
// PROPRIETARIO: Pier — Modulo Carrello (Carrello / DettaglioCarrello / Coupon)
// ============================================================================
// Usato solo per l'endpoint "applica coupon al carrello": un unico campo, un gruppo dedicato (Coupon)
// cosi' non serve una classe Req separata solo per questa singola operazione
@Setter
@Getter
@ToString
public class CarrelloReq {
	@NotNull(groups = ValidationGroups.Coupon.class, message = "carrello.no.codice.coupon")
	@NotBlank(groups = ValidationGroups.Coupon.class, message = "carrello.no.codice.coupon")
	private String codiceCoupon;
}
