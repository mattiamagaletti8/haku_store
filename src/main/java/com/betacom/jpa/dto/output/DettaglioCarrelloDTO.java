package com.betacom.jpa.dto.output;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// Proprietario: Pier
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DettaglioCarrelloDTO {
	private Integer id;
	private VarianteProdottoDTO variante;
	private Integer quantita;
	private BigDecimal subtotale;
}
