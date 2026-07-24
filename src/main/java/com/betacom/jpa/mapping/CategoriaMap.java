package com.betacom.jpa.mapping;

import java.util.List;

// import del DTO da costruire e dell'entity da leggere
import com.betacom.jpa.dto.output.CategoriaDTO;
import com.betacom.jpa.models.Categoria;

// Proprietario: Mattia
public class CategoriaMap {

	// converte una lista di entity Categoria in una lista di CategoriaDTO, riusando il metodo qui sotto per ogni elemento
	public static List<CategoriaDTO> buildCategoriaDTOList(List<Categoria> lC) {
		return lC.stream()
				.map(CategoriaMap::buildCategoriaDTO)
				.toList();
	}

	// converte una singola entity Categoria nel suo DTO, copiando i campi utili al frontend
	public static CategoriaDTO buildCategoriaDTO(Categoria c) {
		return CategoriaDTO.builder()
				.id(c.getIdCategoria())
				.nome(c.getNome())
				// in DB e' salvato solo il nome del file; qui si costruisce il path pubblico da cui scaricarlo
				.immagine(c.getImmagine() != null ? "/immagini/categorie/" + c.getImmagine() : null)
				.build();
	}
}
