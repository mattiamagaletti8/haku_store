package com.betacom.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.betacom.jpa.models.MessageID;
import com.betacom.jpa.models.Messaggi;

// ============================================================================
// PROPRIETARIO: Infrastruttura condivisa (non appartiene a una sola persona)
// ============================================================================
// Nessun metodo custom: MessaggioImpl usa solo findById(new MessageID(lang, code)),
// gia' fornito gratuitamente da JpaRepository grazie alla chiave composita MessageID
public interface IMessagiRepository extends JpaRepository<Messaggi, MessageID>{

}
