package com.betacom.jpa.mapping;

import java.util.List;

// import del DTO da costruire e dell'entity da leggere
import com.betacom.jpa.dto.output.VarianteProdottoDTO;
import com.betacom.jpa.models.VarianteProdotto;

// Proprietario: Mattia
public class VarianteProdottoMap {

	// converte una lista di entity VarianteProdotto in una lista di DTO, riusando il metodo qui sotto per ogni elemento
	public static List<VarianteProdottoDTO> buildVarianteProdottoDTOList(List<VarianteProdotto> lV) {
		if (lV == null)
			return List.of();   // lista vuota invece di null, così chi la usa non deve controllare i null
		return lV.stream()
				.map(VarianteProdottoMap::buildVarianteProdottoDTO)
				.toList();
	}

	// converte una singola entity VarianteProdotto nel suo DTO, copiando anche id e nome del prodotto collegato
	public static VarianteProdottoDTO buildVarianteProdottoDTO(VarianteProdotto v) {
		return VarianteProdottoDTO.builder()
				.id(v.getIdVariante())
				.idProdotto(v.getProdotto() == null ? null : v.getProdotto().getIdProdotto())      // evita il NullPointerException se il prodotto manca
				.nomeProdotto(v.getProdotto() == null ? null : v.getProdotto().getNome())
				.gusto(v.getGusto())
				.formato(v.getFormato())
				.colore(v.getColore())
				.prezzo(v.getPrezzo())
				.quantitaDisponibile(v.getQuantitaDisponibile())
				.immagine(v.getImmagine() != null ? "/immagini/varianti/" + v.getImmagine() : null)
				.build();
	}
}
