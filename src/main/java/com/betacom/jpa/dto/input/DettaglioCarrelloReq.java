package com.betacom.jpa.dto.input;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// Proprietario: Pier
@Setter
@Getter
@ToString
public class DettaglioCarrelloReq {
	@NotNull(groups = { ValidationGroups.Create.class, ValidationGroups.Update.class }, message = "carrello.no.variante")
	private Integer idVariante;

	@NotNull(groups = { ValidationGroups.Create.class, ValidationGroups.Update.class }, message = "carrello.no.quantita")
	@Min(value = 1, groups = { ValidationGroups.Create.class, ValidationGroups.Update.class }, message = "carrello.quantita.invalid")
	private Integer quantita;
}
