package com.betacom.jpa.dto.input;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// Proprietario: Mattia
@Setter
@Getter
@ToString
public class VarianteProdottoReq {
	@NotNull(groups = ValidationGroups.Update.class, message = "variante.no.id")
	private Integer id;

	@NotNull(groups = ValidationGroups.Create.class, message = "variante.no.prodotto")
	private Integer idProdotto;

	private String gusto;
	private String formato;
	private String colore;

	@NotNull(groups = ValidationGroups.Create.class, message = "variante.no.prezzo")
	@DecimalMin(value = "0.0", groups = { ValidationGroups.Create.class, ValidationGroups.Update.class }, message = "variante.prezzo.invalid")
	private BigDecimal prezzo;

	@Min(value = 0, groups = { ValidationGroups.Create.class, ValidationGroups.Update.class }, message = "variante.quantita.invalid")
	private Integer quantitaDisponibile;
}
