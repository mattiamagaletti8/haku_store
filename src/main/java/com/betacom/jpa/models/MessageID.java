package com.betacom.jpa.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Proprietario: Infrastruttura condivisa
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class MessageID {
	@Column (length=4)
	private String lang;

	@Column (length = 50)
	private String code;
}
