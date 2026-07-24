package com.betacom.jpa.mapping;

import java.util.List;

// import del DTO da costruire e dell'entity da leggere
import com.betacom.jpa.dto.output.ProdottoDTO;
import com.betacom.jpa.models.Prodotto;

// Proprietario: Mattia
public class ProdottoMap {

	// converte una lista di entity Prodotto in una lista di ProdottoDTO, riusando il metodo qui sotto per ogni elemento
	public static List<ProdottoDTO> buildProdottoDTOList(List<Prodotto> lP) {
		return lP.stream()
				.map(ProdottoMap::buildProdottoDTO)
				.toList();
	}

	// converte una singola entity Prodotto nel suo DTO, copiando i campi e convertendo anche categoria e varianti collegate
	public static ProdottoDTO buildProdottoDTO(Prodotto p) {
		return ProdottoDTO.builder()
				.id(p.getIdProdotto())
				.nome(p.getNome())
				.descrizione(p.getDescrizione())
				.marca(p.getMarca())
				.immagine(p.getImmagine() != null ? "/immagini/prodotti/" + p.getImmagine() : null)
				.categoria(p.getCategoria() == null ? null : CategoriaMap.buildCategoriaDTO(p.getCategoria()))    // se manca la categoria evito il NullPointerException
				.varianti(VarianteProdottoMap.buildVarianteProdottoDTOList(p.getVarianti()))                     // converte anche tutte le varianti del prodotto
				.build();
	}
}
