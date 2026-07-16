package com.betacom.jpa.mapping;

import java.util.List;

import com.betacom.jpa.dto.output.RecensioneDTO;
import com.betacom.jpa.models.Recensione;

// ============================================================================
// PROPRIETARIO: Sarah — Modulo Utente, Recensioni & Sicurezza
// ============================================================================
public class RecensioneMap {

	public static List<RecensioneDTO> buildRecensioneDTOList(List<Recensione> lR) {
		return lR.stream()
				.map(RecensioneMap::buildRecensioneDTO)
				.toList();
	}

	public static RecensioneDTO buildRecensioneDTO(Recensione r) {
		return RecensioneDTO.builder()
				.id(r.getIdRecensione())
				.idProdotto(r.getProdotto().getIdProdotto())
				.idUtente(r.getUtente().getIdUtente())
				// Solo nome/cognome dell'autore vengono letti dall'Utente collegato,
				// mai l'intera entity (niente email/password/telefono esposti)
				.nomeUtente(r.getUtente().getNome())
				.cognomeUtente(r.getUtente().getCognome())
				.voto(r.getVoto())
				.titolo(r.getTitolo())
				.commento(r.getCommento())
				.dataRecensione(r.getDataRecensione())
				.build();
	}
}
