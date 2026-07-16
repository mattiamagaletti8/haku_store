package com.betacom.jpa.mapping;

import java.util.List;

import com.betacom.jpa.dto.output.VarianteProdottoDTO;
import com.betacom.jpa.models.VarianteProdotto;

// ============================================================================
// PROPRIETARIO: Mattia — Modulo Catalogo (Categoria / Prodotto / VarianteProdotto)
// ============================================================================
public class VarianteProdottoMap {

	public static List<VarianteProdottoDTO> buildVarianteProdottoDTOList(List<VarianteProdotto> lV) {
		// Guardia di sicurezza: se la lista non e' stata caricata (LAZY non ancora agganciata), niente NPE
		if (lV == null)
			return List.of();
		return lV.stream()
				.map(VarianteProdottoMap::buildVarianteProdottoDTO)
				.toList();
	}

	public static VarianteProdottoDTO buildVarianteProdottoDTO(VarianteProdotto v) {
		return VarianteProdottoDTO.builder()
				.id(v.getIdVariante())
				// idProdotto/nomeProdotto vengono "appiattiti" leggendoli dal Prodotto collegato,
				// con controllo null per sicurezza (in teoria non dovrebbe mai essere null, essendo nullable=false)
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
