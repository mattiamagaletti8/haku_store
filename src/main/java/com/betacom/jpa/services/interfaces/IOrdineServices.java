package com.betacom.jpa.services.interfaces;

import java.util.List;

import com.betacom.jpa.dto.input.OrdineReq;
import com.betacom.jpa.dto.output.OrdineDTO;

// Proprietario: Valerio
public interface IOrdineServices {
	OrdineDTO checkout(Integer idUtente, OrdineReq req) throws Exception;

	List<OrdineDTO> list(Integer callerId, boolean isAdmin, Integer idUtenteFiltro, String stato, String statoPagamento) throws Exception;

	OrdineDTO getById(Integer id, Integer callerId, boolean isAdmin) throws Exception;

	void updateStato(OrdineReq req) throws Exception;

	void updateStatoPagamento(OrdineReq req) throws Exception;
}
