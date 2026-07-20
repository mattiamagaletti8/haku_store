package com.betacom.jpa.mapping;

import java.util.List;

import com.betacom.jpa.dto.output.VarianteProdottoDTO;
import com.betacom.jpa.models.VarianteProdotto;

// Proprietario: Mattia
public class VarianteProdottoMap {

	public static List<VarianteProdottoDTO> buildVarianteProdottoDTOList(List<VarianteProdotto> lV) {
		if (lV == null)
			return List.of();
		return lV.stream()
				.map(VarianteProdottoMap::buildVarianteProdottoDTO)
				.toList();
	}

	public static VarianteProdottoDTO buildVarianteProdottoDTO(VarianteProdotto v) {
		return VarianteProdottoDTO.builder()
				.id(v.getIdVariante())
				.idProdotto(v.getProdotto() == null ? null : v.getProdotto().getIdProdotto())
				.nomeProdotto(v.getProdotto() == null ? null : v.getProdotto().getNome())
				.gusto(v.getGusto())
				.formato(v.getFormato())
				.colore(v.getColore())
				.prezzo(v.getPrezzo())
				.quantitaDisponibile(v.getQuantitaDisponibile())
				.build();
	}
}
