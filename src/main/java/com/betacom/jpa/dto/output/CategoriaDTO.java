package com.betacom.jpa.dto.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// ============================================================================
// PROPRIETARIO: Mattia — Modulo Catalogo (Categoria / Prodotto / VarianteProdotto)
// ============================================================================
// DTO di OUTPUT: cio' che il backend restituisce al frontend per una categoria.
// Volutamente piu' semplice dell'entity Categoria (niente lista prodotti: non serve al client
// quando naviga il catalogo, ed evitarla previene un giro JSON enorme e ricorsivo).
@Getter
@Setter
// Abilita CategoriaDTO.builder()...build(), usato in CategoriaMap per costruirlo comodamente
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CategoriaDTO {
	private Integer id;
	private String nome;
}
