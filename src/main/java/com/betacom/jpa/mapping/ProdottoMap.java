package com.betacom.jpa.mapping;

import java.util.List;

import com.betacom.jpa.dto.output.ProdottoDTO;
import com.betacom.jpa.models.Prodotto;

public class ProdottoMap {

	public static List<ProdottoDTO> buildProdottoDTOList(List<Prodotto> lP) {
		return lP.stream()
				.map(ProdottoMap::buildProdottoDTO)
				.toList();
	}

	public static ProdottoDTO buildProdottoDTO(Prodotto p) {
		return ProdottoDTO.builder()
				.id(p.getIdProdotto())
				.nome(p.getNome())
				.descrizione(p.getDescrizione())
				.marca(p.getMarca())
				.categoria(p.getCategoria() == null ? null : CategoriaMap.buildCategoriaDTO(p.getCategoria()))
				.varianti(VarianteProdottoMap.buildVarianteProdottoDTOList(p.getVarianti()))
				.build();
	}
}
