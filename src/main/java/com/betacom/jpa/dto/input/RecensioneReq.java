package com.betacom.jpa.dto.input;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// Proprietario: Sarah
@Setter
@Getter
@ToString
public class RecensioneReq {
	@NotNull(groups = ValidationGroups.Update.class, message = "recensione.no.id")
	private Integer id;

	@NotNull(groups = ValidationGroups.Create.class, message = "recensione.no.prodotto")
	private Integer idProdotto;

	@NotNull(groups = ValidationGroups.Create.class, message = "recensione.no.voto")
	@Min(value = 1, groups = { ValidationGroups.Create.class, ValidationGroups.Update.class }, message = "recensione.voto.invalid")
	@Max(value = 5, groups = { ValidationGroups.Create.class, ValidationGroups.Update.class }, message = "recensione.voto.invalid")
	private Integer voto;

	private String titolo;
	private String commento;
}
