package com.betacom.jpa.mapping;

import java.math.BigDecimal;
import java.util.List;

import com.betacom.jpa.dto.output.DettaglioCarrelloDTO;
import com.betacom.jpa.models.DettaglioCarrello;

// Proprietario: Pier
public class DettaglioCarrelloMap {

	public static List<DettaglioCarrelloDTO> buildDettaglioCarrelloDTOList(List<DettaglioCarrello> lD) {
		return lD.stream()
				.map(DettaglioCarrelloMap::buildDettaglioCarrelloDTO)
				.toList();
	}

	public static DettaglioCarrelloDTO buildDettaglioCarrelloDTO(DettaglioCarrello d) {
		BigDecimal subtotale = d.getVariante().getPrezzo().multiply(BigDecimal.valueOf(d.getQuantita()));
		return DettaglioCarrelloDTO.builder()
				.id(d.getIdDettaglio())
				.variante(VarianteProdottoMap.buildVarianteProdottoDTO(d.getVariante()))
				.quantita(d.getQuantita())
				.subtotale(subtotale)
				.build();
	}
}
