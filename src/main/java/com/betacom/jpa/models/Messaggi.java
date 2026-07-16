package com.betacom.jpa.models;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

// ============================================================================
// PROPRIETARIO: Infrastruttura condivisa (non appartiene a una sola persona)
// ============================================================================
// Una riga = una traduzione per (lingua, codice). Es. (IT, "categoria.nome.exist") ->
// "Esiste gia' una categoria con questo nome." — questa e' la tabella che MessaggioImpl.get() interroga
@Setter
@Getter
@Entity
@Table (name="messaggi_sistema")
public class Messaggi {

	// Chiave composita (lang+code) invece di un id auto-generato: due colonne insieme
	// identificano univocamente una traduzione, non serve un id artificiale
	@EmbeddedId
	private MessageID msgID;

	private String messagio;
}
