package com.betacom.jpa.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// Proprietario: Sarah
@Setter
@Getter
@ToString
public class IndirizzoReq {
	@NotNull(groups = ValidationGroups.Update.class, message = "indirizzo.no.id")
	private Integer id;

	@NotNull(groups = ValidationGroups.Create.class, message = "indirizzo.no.via")
	@NotBlank(groups = ValidationGroups.Create.class, message = "indirizzo.no.via")
	private String via;

	@NotNull(groups = ValidationGroups.Create.class, message = "indirizzo.no.citta")
	@NotBlank(groups = ValidationGroups.Create.class, message = "indirizzo.no.citta")
	private String citta;

	@NotNull(groups = ValidationGroups.Create.class, message = "indirizzo.no.cap")
	@NotBlank(groups = ValidationGroups.Create.class, message = "indirizzo.no.cap")
	private String cap;

	private String provincia;

	private String nazione;
}
