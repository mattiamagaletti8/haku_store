package com.betacom.jpa.dto.output;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// Proprietario: Valerio
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DettaglioOrdineDTO {
	private Integer id;
	private VarianteProdottoDTO variante;
	private Integer quantita;
	private BigDecimal prezzoUnitario;
	private BigDecimal subtotale;
}
