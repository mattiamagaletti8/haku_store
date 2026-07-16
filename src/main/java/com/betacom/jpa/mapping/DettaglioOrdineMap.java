package com.betacom.jpa.mapping;

import java.math.BigDecimal;
import java.util.List;

import com.betacom.jpa.dto.output.DettaglioOrdineDTO;
import com.betacom.jpa.models.DettaglioOrdine;

// ============================================================================
// PROPRIETARIO: Valerio — Modulo Ordini (Ordine / DettaglioOrdine / checkout)
// ============================================================================
public class DettaglioOrdineMap {

	public static List<DettaglioOrdineDTO> buildDettaglioOrdineDTOList(List<DettaglioOrdine> lD) {
		return lD.stream()
				.map(DettaglioOrdineMap::buildDettaglioOrdineDTO)
				.toList();
	}

	public static DettaglioOrdineDTO buildDettaglioOrdineDTO(DettaglioOrdine d) {
		// Subtotale = prezzo CONGELATO (prezzoUnitario) * quantita' — diverso da DettaglioCarrelloMap,
		// che invece legge il prezzo corrente della variante, non quello storicizzato
		BigDecimal subtotale = d.getPrezzoUnitario().multiply(BigDecimal.valueOf(d.getQuantita()));
		return DettaglioOrdineDTO.builder()
				.id(d.getIdDettaglio())
				.variante(VarianteProdottoMap.buildVarianteProdottoDTO(d.getVariante()))
				.quantita(d.getQuantita())
				.prezzoUnitario(d.getPrezzoUnitario())
				.subtotale(subtotale)
				.build();
	}
}
