package com.betacom.jpa.services.interfaces;

import java.util.List;

import com.betacom.jpa.dto.input.RecensioneReq;
import com.betacom.jpa.dto.output.RecensioneDTO;

// Proprietario: Sarah
public interface IRecensioneServices {
	void create(RecensioneReq req, Integer idUtente) throws Exception;

	void update(RecensioneReq req, Integer callerId, boolean isAdmin) throws Exception;

	void delete(Integer id, Integer callerId, boolean isAdmin) throws Exception;

	List<RecensioneDTO> listByProdotto(Integer idProdotto) throws Exception;
}
