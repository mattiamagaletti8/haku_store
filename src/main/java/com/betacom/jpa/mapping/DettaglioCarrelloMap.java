package com.betacom.jpa.mapping;

import java.math.BigDecimal;
import java.util.List;

import com.betacom.jpa.dto.output.DettaglioCarrelloDTO;
import com.betacom.jpa.dto.output.VarianteProdottoDTO;
import com.betacom.jpa.models.DettaglioCarrello;
import com.betacom.jpa.services.interfaces.ISaldiServices;

// Proprietario: Pier
public class DettaglioCarrelloMap {

	public static List<DettaglioCarrelloDTO> buildDettaglioCarrelloDTOList(List<DettaglioCarrello> lD, ISaldiServices saldiS) {
		return lD.stream()
				.map(d -> buildDettaglioCarrelloDTO(d, saldiS))
				.toList();
	}

	public static DettaglioCarrelloDTO buildDettaglioCarrelloDTO(DettaglioCarrello d, ISaldiServices saldiS) {
		// prezzo effettivo (scontato se il prodotto e' in saldo in questo momento): e' quello
		// che verra' davvero pagato al checkout, non solo un prezzo mostrato
		BigDecimal prezzoUnitario = saldiS.prezzoEffettivo(d.getVariante());
		BigDecimal subtotale = prezzoUnitario.multiply(BigDecimal.valueOf(d.getQuantita()));

		VarianteProdottoDTO variante = VarianteProdottoMap.buildVarianteProdottoDTO(d.getVariante());
		if (prezzoUnitario.compareTo(d.getVariante().getPrezzo()) < 0)
			variante.setPrezzoScontato(prezzoUnitario);

		return DettaglioCarrelloDTO.builder()
				.id(d.getIdDettaglio())
				.variante(variante)
				.quantita(d.getQuantita())
				.subtotale(subtotale)
				.build();
	}
}
