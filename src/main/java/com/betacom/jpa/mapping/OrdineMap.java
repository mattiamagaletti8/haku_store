package com.betacom.jpa.mapping;

import java.util.List;

import com.betacom.jpa.dto.output.OrdineDTO;
import com.betacom.jpa.models.Ordine;

// Proprietario: Valerio
public class OrdineMap {

	public static List<OrdineDTO> buildOrdineDTOList(List<Ordine> lO) {
		return lO.stream()
				.map(OrdineMap::buildOrdineDTO)
				.toList();
	}

	public static OrdineDTO buildOrdineDTO(Ordine o) {
		return OrdineDTO.builder()
				.id(o.getIdOrdine())
				.idUtente(o.getUtente().getIdUtente())
				.dataOrdine(o.getDataOrdine())
				.totaleProdotti(o.getTotaleProdotti())
				.valoreSconto(o.getValoreSconto())
				.totalePagato(o.getTotalePagato())
				.codiceCouponUsato(o.getCodiceCouponUsato())
				.stato(o.getStato().toString())
				.spedizioneVia(o.getSpedizioneVia())
				.spedizioneCitta(o.getSpedizioneCitta())
				.spedizioneCap(o.getSpedizioneCap())
				.spedizioneProvincia(o.getSpedizioneProvincia())
				.spedizioneNazione(o.getSpedizioneNazione())
				.metodoPagamento(o.getMetodoPagamento())
				.statoPagamento(o.getStatoPagamento().toString())
				.righe(DettaglioOrdineMap.buildDettaglioOrdineDTOList(o.getRighe()))
				.build();
	}
}
