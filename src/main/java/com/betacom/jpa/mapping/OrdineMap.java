package com.betacom.jpa.mapping;

import java.util.List;

import com.betacom.jpa.dto.output.OrdineDTO;
import com.betacom.jpa.models.Ordine;

// ============================================================================
// PROPRIETARIO: Valerio — Modulo Ordini (Ordine / DettaglioOrdine / checkout)
// ============================================================================
public class OrdineMap {

	public static List<OrdineDTO> buildOrdineDTOList(List<Ordine> lO) {
		return lO.stream()
				.map(OrdineMap::buildOrdineDTO)
				.toList();
	}

	public static OrdineDTO buildOrdineDTO(Ordine o) {
		return OrdineDTO.builder()
				.id(o.getIdOrdine())
				// Solo l'id, non l'intero Utente: evita di tirarsi dietro tutti i dati personali
				// ogni volta che si legge un ordine
				.idUtente(o.getUtente().getIdUtente())
				.dataOrdine(o.getDataOrdine())
				// Questi totali vengono copiati COSI' COME SONO dall'entity: nessun ricalcolo,
				// sono gia' congelati dal momento del checkout (OrdineImpl)
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
				// Collegamento verso DettaglioOrdineMap per convertire tutte le righe
				.righe(DettaglioOrdineMap.buildDettaglioOrdineDTOList(o.getRighe()))
				.build();
	}
}
