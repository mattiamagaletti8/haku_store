package com.betacom.jpa.dto.output;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// ============================================================================
// PROPRIETARIO: Valerio — Modulo Ordini (Ordine / DettaglioOrdine / checkout)
// ============================================================================
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DettaglioOrdineDTO {
	private Integer id;
	// Variante mostrata con i suoi dati CORRENTI (nome, gusto...), ma il prezzo qui sotto e' quello congelato
	private VarianteProdottoDTO variante;
	private Integer quantita;
	private BigDecimal prezzoUnitario;
	// Subtotale calcolato in DettaglioOrdineMap come prezzoUnitario * quantita (prezzo congelato, non quello corrente)
	private BigDecimal subtotale;
}
