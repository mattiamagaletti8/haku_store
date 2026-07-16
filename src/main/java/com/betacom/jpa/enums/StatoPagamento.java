package com.betacom.jpa.enums;

// ============================================================================
// PROPRIETARIO: Valerio — Modulo Ordini (Ordine / DettaglioOrdine / checkout)
// ============================================================================
// Stato del pagamento, tenuto separato da StatoOrdine: sono due assi indipendenti
// (es. ordine SPEDITO ma pagamento ancora DA_PAGARE in caso di pagamento alla consegna)
public enum StatoPagamento {
	// Stato iniziale impostato dal checkout
	DA_PAGARE,
	APPROVATO,
	FALLITO
}
