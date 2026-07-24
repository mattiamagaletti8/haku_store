package com.betacom.jpa.mapping;

import java.util.List;

import com.betacom.jpa.dto.output.RecensioneDTO;
import com.betacom.jpa.models.Recensione;

// Proprietario: Sarah
public class RecensioneMap {

	// converte una lista di entità Recensione in una lista di RecensioneDTO
	public static List<RecensioneDTO> buildRecensioneDTOList(List<Recensione> lR) {
		return lR.stream()
				.map(RecensioneMap::buildRecensioneDTO)
				.toList();
	}

	// converte una singola Recensione nel suo DTO, "appiattendo" dentro anche nome e cognome dell'autore
	public static RecensioneDTO buildRecensioneDTO(Recensione r) {
		return RecensioneDTO.builder()
				.id(r.getIdRecensione())
				.idProdotto(r.getProdotto().getIdProdotto())
				.idUtente(r.getUtente().getIdUtente())
				.nomeUtente(r.getUtente().getNome())        // preso dalla relazione con Utente, non salvato qui
				.cognomeUtente(r.getUtente().getCognome())
				.voto(r.getVoto())
				.titolo(r.getTitolo())
				.commento(r.getCommento())
				.dataRecensione(r.getDataRecensione())
				.build();
	}
}
