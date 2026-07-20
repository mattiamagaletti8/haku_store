package com.betacom.jpa.mapping;

import java.util.List;

import com.betacom.jpa.dto.output.UtenteDTO;
import com.betacom.jpa.models.Utente;

// Proprietario: Sarah
public class UtenteMap {

	public static List<UtenteDTO> buildUtenteDTOList(List<Utente> lU) {
		return lU.stream()
				.map(UtenteMap::buildUtenteDTO)
				.toList();
	}

	public static UtenteDTO buildUtenteDTO(Utente u) {
		return UtenteDTO.builder()
				.idUtente(u.getIdUtente())
				.nome(u.getNome())
				.cognome(u.getCognome())
				.email(u.getEmail())
				.telefono(u.getTelefono())
				.ruolo(u.getRuolo().toString())
				.build();
	}
}
