package com.betacom.jpa.mapping;

import java.util.List;

import com.betacom.jpa.dto.output.CategoriaDTO;
import com.betacom.jpa.models.Categoria;

public class CategoriaMap {

	public static List<CategoriaDTO> buildCategoriaDTOList(List<Categoria> lC) {
		return lC.stream()
				.map(CategoriaMap::buildCategoriaDTO)
				.toList();
	}

	public static CategoriaDTO buildCategoriaDTO(Categoria c) {
		return CategoriaDTO.builder()
				.id(c.getIdCategoria())
				.nome(c.getNome())
				.build();
	}
}
