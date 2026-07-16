package com.betacom.jpa.dto.input;

// ============================================================================
// PROPRIETARIO: Infrastruttura condivisa (non appartiene a una sola persona)
// ============================================================================
// 5 interfacce marcatore vuote — nessun campo, nessun metodo. Servono solo come "etichetta"
// per @Validated(...): un controller scrive @Validated(ValidationGroups.Create.class) sul PARAMETRO
// del metodo, non sulla classe Req, cosi' lo STESSO Req puo' avere regole diverse a seconda
// del contesto in cui viene usato, senza duplicare la classe.
public interface ValidationGroups {
	// Campi obbligatori solo quando si crea una nuova riga
	interface Create {}

	// Campi obbligatori solo in modifica (tipicamente l'id, per sapere quale riga aggiornare)
	interface Update {}

	// Riservato a CarrelloReq: rende obbligatorio solo codiceCoupon, per l'endpoint applica-coupon
	interface Coupon {}

	// Riservato a OrdineReq: rende obbligatori idIndirizzo e metodoPagamento solo al momento del checkout
	interface Checkout {}

	// Riservato a OrdineReq: rende obbligatorio solo l'id, per gli endpoint admin di cambio stato
	interface OrdineStato {}
}
