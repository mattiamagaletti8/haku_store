package com.betacom.jpa.mapping;

import java.util.List;

import com.betacom.jpa.dto.output.CategoriaDTO;
import com.betacom.jpa.models.Categoria;

// ============================================================================
// PROPRIETARIO: Mattia — Modulo Catalogo (Categoria / Prodotto / VarianteProdotto)
// ============================================================================
// Classe di sola conversione: trasforma le entity JPA (Categoria) nei DTO di output (CategoriaDTO)
// che il controller restituisce al frontend. Nessuna logica di business qui dentro, solo mapping.
public class CategoriaMap {

	// Converte una lista intera di Categoria in una lista di CategoriaDTO,
	// riusando buildCategoriaDTO su ogni elemento (evita di duplicare la logica di conversione)
	public static List<CategoriaDTO> buildCategoriaDTOList(List<Categoria> lC) {
		return lC.stream()
				.map(CategoriaMap::buildCategoriaDTO)
				.toList();
	}

	// Converte una singola Categoria nel suo DTO: copia solo id e nome,
	// la lista "prodotti" dell'entity non viene toccata (il DTO non la espone).
	public static CategoriaDTO buildCategoriaDTO(Categoria c) {
		return CategoriaDTO.builder()
				.id(c.getIdCategoria())
				.nome(c.getNome())
				.build();
	}
}
