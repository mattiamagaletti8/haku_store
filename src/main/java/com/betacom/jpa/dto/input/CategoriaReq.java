package com.betacom.jpa.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// Proprietario: Mattia
@Setter
@Getter
@ToString
public class CategoriaReq {
	@NotNull(groups = ValidationGroups.Update.class, message = "categoria.no.id")
	private Integer id;

	@NotNull(groups = ValidationGroups.Create.class, message = "categoria.no.nome")
	@NotBlank(groups = ValidationGroups.Create.class, message = "categoria.no.nome")
	private String nome;
}
