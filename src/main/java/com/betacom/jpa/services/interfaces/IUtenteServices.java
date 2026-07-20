package com.betacom.jpa.services.interfaces;

import java.util.List;

import com.betacom.jpa.dto.input.UtenteReq;
import com.betacom.jpa.dto.output.UtenteDTO;
import com.betacom.jpa.models.Utente;

// Proprietario: Sarah
public interface IUtenteServices {
	UtenteDTO create(UtenteReq req) throws Exception;

	void update(UtenteReq req, Integer callerId, boolean isAdmin) throws Exception;

	void delete(Integer id, Integer callerId, boolean isAdmin) throws Exception;

	List<UtenteDTO> list() throws Exception;

	UtenteDTO getById(Integer id, Integer callerId, boolean isAdmin) throws Exception;

	Utente getEntityByEmail(String email) throws Exception;
}
