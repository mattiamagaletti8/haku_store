package com.betacom.jpa.mapping;

import java.util.List;

import com.betacom.jpa.dto.output.IndirizzoDTO;
import com.betacom.jpa.models.Indirizzo;

// Proprietario: Sarah
public class IndirizzoMap {

	public static List<IndirizzoDTO> buildIndirizzoDTOList(List<Indirizzo> lI) {
		return lI.stream()
				.map(IndirizzoMap::buildIndirizzoDTO)
				.toList();
	}

	public static IndirizzoDTO buildIndirizzoDTO(Indirizzo i) {
		return IndirizzoDTO.builder()
				.id(i.getIdIndirizzo())
				.via(i.getVia())
				.citta(i.getCitta())
				.cap(i.getCap())
				.provincia(i.getProvincia())
				.nazione(i.getNazione())
				.build();
	}
}
