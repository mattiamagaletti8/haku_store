package com.betacom.jpa.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// ============================================================================
// PROPRIETARIO: Infrastruttura condivisa (non appartiene a una sola persona)
// ============================================================================
// Chiave composita del sistema i18n: (lingua, codice messaggio) insieme identificano
// un'unica traduzione. @Embeddable: non e' un'entity a se stante, viene incorporata
// dentro Messaggi come @EmbeddedId
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
