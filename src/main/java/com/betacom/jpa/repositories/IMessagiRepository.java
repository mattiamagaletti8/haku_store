package com.betacom.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.betacom.jpa.models.MessageID;
import com.betacom.jpa.models.Messaggi;

// Proprietario: Infrastruttura condivisa
public interface IMessagiRepository extends JpaRepository<Messaggi, MessageID>{

}
