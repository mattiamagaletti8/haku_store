package com.betacom.jpa.dto.output;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// Proprietario: Mattia
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VarianteProdottoDTO {
	private Integer id;
	private Integer idProdotto;
	private String nomeProdotto;
	private String gusto;
	private String formato;
	private String colore;
	private BigDecimal prezzo;
	private Integer quantitaDisponibile;
	private String immagine;
}
