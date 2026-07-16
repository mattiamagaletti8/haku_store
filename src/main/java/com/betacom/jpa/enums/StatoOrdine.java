package com.betacom.jpa.enums;

// ============================================================================
// PROPRIETARIO: Valerio — Modulo Ordini (Ordine / DettaglioOrdine / checkout)
// ============================================================================
// Ciclo di vita dell'ordine dal punto di vista logistico (spedizione), indipendente
// dallo stato del pagamento (vedi StatoPagamento) — un ordine puo' essere IN_ATTESA
// di elaborazione anche se il pagamento e' gia' APPROVATO
public enum StatoOrdine {
	// Stato iniziale impostato dal checkout, prima di qualunque intervento admin
	IN_ATTESA,
	ELABORATO,
	SPEDITO,
	ANNULLATO
}
