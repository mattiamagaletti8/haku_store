package com.betacom.jpa.mapping;

import java.util.List;

import com.betacom.jpa.dto.output.ProdottoDTO;
import com.betacom.jpa.models.Prodotto;

// ============================================================================
// PROPRIETARIO: Mattia — Modulo Catalogo (Categoria / Prodotto / VarianteProdotto)
// ============================================================================
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
				// Collegamento verso CategoriaMap: ProdottoMap non duplica la logica di conversione della categoria,
				// la delega alla classe dedicata (controllo null perche' in teoria potrebbe non essere caricata)
				.categoria(p.getCategoria() == null ? null : CategoriaMap.buildCategoriaDTO(p.getCategoria()))
				// Stesso principio per le varianti: delega a VarianteProdottoMap
				.varianti(VarianteProdottoMap.buildVarianteProdottoDTOList(p.getVarianti()))
				.build();
	}
}
