package com.betacom.jpa.models;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

// Proprietario: Infrastruttura condivisa
@Setter
@Getter
@Entity
@Table (name="messaggi_sistema")
public class Messaggi {

	@EmbeddedId
	private MessageID msgID;

	private String messagio;
}
