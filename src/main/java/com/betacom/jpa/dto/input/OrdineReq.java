package com.betacom.jpa.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// ============================================================================
// PROPRIETARIO: Valerio — Modulo Ordini (Ordine / DettaglioOrdine / checkout)
// ============================================================================
// Un'unica classe Req condivisa da 2 operazioni molto diverse (checkout e cambio-stato admin),
// distinte tramite due gruppi di validazione dedicati: Checkout e OrdineStato
@Setter
@Getter
@ToString
public class OrdineReq {
	// Obbligatorio solo nel gruppo OrdineStato: serve all'admin per sapere quale ordine aggiornare
	@NotNull(groups = ValidationGroups.OrdineStato.class, message = "ordine.no.id")
	private Integer id;

	// Obbligatorio solo nel gruppo Checkout: il checkout non puo' avvenire senza un indirizzo di spedizione
	@NotNull(groups = ValidationGroups.Checkout.class, message = "ordine.no.indirizzo")
	private Integer idIndirizzo;

	@NotNull(groups = ValidationGroups.Checkout.class, message = "ordine.no.metodo.pagamento")
	@NotBlank(groups = ValidationGroups.Checkout.class, message = "ordine.no.metodo.pagamento")
	private String metodoPagamento;

	// Nessuna validazione su questi due: sono opzionali, il service li applica solo se presenti
	// (updateStato tocca solo "stato", updateStatoPagamento tocca solo "statoPagamento")
	private String stato;

	private String statoPagamento;
}
