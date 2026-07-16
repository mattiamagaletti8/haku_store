package com.betacom.jpa.services.interfaces;

import java.util.List;

import com.betacom.jpa.dto.input.IndirizzoReq;
import com.betacom.jpa.dto.output.IndirizzoDTO;

// ============================================================================
// PROPRIETARIO: Sarah — Modulo Utente, Recensioni & Sicurezza
// ============================================================================
public interface IIndirizzoServices {
	// idUtente separato da req: arriva sempre dal principal autenticato nel controller, mai dal body
	void create(IndirizzoReq req, Integer idUtente) throws Exception;

	void update(IndirizzoReq req, Integer callerId, boolean isAdmin) throws Exception;

	void delete(Integer id, Integer callerId, boolean isAdmin) throws Exception;

	List<IndirizzoDTO> listByUtente(Integer idUtente) throws Exception;

	IndirizzoDTO getById(Integer id, Integer callerId, boolean isAdmin) throws Exception;
}
