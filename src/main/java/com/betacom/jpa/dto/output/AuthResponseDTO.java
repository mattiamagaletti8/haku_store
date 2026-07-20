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
public class AuthResponseDTO {
	private String token;
	private String tokenType;
	private long expiresIn;
	private UtenteDTO utente;
}
