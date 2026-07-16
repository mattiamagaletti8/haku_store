package com.betacom.jpa.dto.output;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// ============================================================================
// PROPRIETARIO: Mattia — Modulo Catalogo (Categoria / Prodotto / VarianteProdotto)
// ============================================================================
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProdottoDTO {
	private Integer id;
	private String nome;
	private String descrizione;
	private String marca;
	// Categoria annidata gia' convertita in DTO (non l'id grezzo): il frontend riceve subito il nome
	// della categoria senza dover fare una seconda chiamata
	private CategoriaDTO categoria;
	// Lista delle varianti, gia' convertite: prezzo/stock/gusto per ogni combinazione disponibile
	private List<VarianteProdottoDTO> varianti;
}
