package com.betacom.jpa.services.interfaces;

import java.util.List;

import com.betacom.jpa.dto.input.IndirizzoReq;
import com.betacom.jpa.dto.output.IndirizzoDTO;

// Proprietario: Sarah
public interface IIndirizzoServices {
	void create(IndirizzoReq req, Integer idUtente) throws Exception;

	void update(IndirizzoReq req, Integer callerId, boolean isAdmin) throws Exception;

	void delete(Integer id, Integer callerId, boolean isAdmin) throws Exception;

	List<IndirizzoDTO> listByUtente(Integer idUtente) throws Exception;

	IndirizzoDTO getById(Integer id, Integer callerId, boolean isAdmin) throws Exception;
}
