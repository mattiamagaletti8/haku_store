package com.betacom.jpa.dto.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// Proprietario: Sarah
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UtenteDTO {
	private Integer idUtente;
	private String nome;
	private String cognome;
	private String email;
	private String telefono;
	private String ruolo;
}
