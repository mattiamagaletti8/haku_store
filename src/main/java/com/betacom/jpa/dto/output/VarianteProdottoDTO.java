package com.betacom.jpa.dto.output;

import java.math.BigDecimal;

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
public class VarianteProdottoDTO {
	private Integer id;
	// idProdotto + nomeProdotto: appiattiti qui invece di un ProdottoDTO annidato, per evitare un
	// giro circolare Prodotto -> varianti -> Prodotto quando la variante viene mostrata da sola
	// (es. dentro una riga del carrello)
	private Integer idProdotto;
	private String nomeProdotto;
	private String gusto;
	private String formato;
	private String colore;
	private BigDecimal prezzo;
	private Integer quantitaDisponibile;
}
