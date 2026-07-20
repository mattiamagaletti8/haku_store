package com.betacom.jpa.mapping;

import java.math.BigDecimal;
import java.util.List;

import com.betacom.jpa.dto.output.DettaglioOrdineDTO;
import com.betacom.jpa.models.DettaglioOrdine;

// Proprietario: Valerio
public class DettaglioOrdineMap {

	public static List<DettaglioOrdineDTO> buildDettaglioOrdineDTOList(List<DettaglioOrdine> lD) {
		return lD.stream()
				.map(DettaglioOrdineMap::buildDettaglioOrdineDTO)
				.toList();
	}

	public static DettaglioOrdineDTO buildDettaglioOrdineDTO(DettaglioOrdine d) {
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
