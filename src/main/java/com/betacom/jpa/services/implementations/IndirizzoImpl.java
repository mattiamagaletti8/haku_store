package com.betacom.jpa.services.implementations;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.betacom.jpa.dto.input.IndirizzoReq;
import com.betacom.jpa.dto.output.IndirizzoDTO;
import com.betacom.jpa.exceptions.ApiException;
import com.betacom.jpa.mapping.IndirizzoMap;
import com.betacom.jpa.models.Indirizzo;
import com.betacom.jpa.models.Utente;
import com.betacom.jpa.repositories.IIndirizzoRepository;
import com.betacom.jpa.repositories.IUtenteRepository;
import com.betacom.jpa.services.interfaces.IIndirizzoServices;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// ============================================================================
// PROPRIETARIO: Sarah — Modulo Utente, Recensioni & Sicurezza
// ============================================================================
@Slf4j
@RequiredArgsConstructor
@Service
public class IndirizzoImpl implements IIndirizzoServices {

	private final IIndirizzoRepository repI;
	// Serve per collegare il nuovo indirizzo all'utente giusto (verificandone l'esistenza)
	private final IUtenteRepository repU;

	@Transactional
	@Override
	public void create(IndirizzoReq req, Integer idUtente) throws Exception {
		log.debug("create {} for utente {}", req, idUtente);
		Utente ut = repU.findById(idUtente)
				.orElseThrow(() -> new ApiException("utente.ntfnd"));

		Indirizzo ind = new Indirizzo();
		// idUtente arriva come parametro separato (dal principal), MAI da req: e' cosi' che si impedisce
		// di creare un indirizzo intestato a un altro utente
		ind.setUtente(ut);
		ind.setVia(req.getVia());
		ind.setCitta(req.getCitta());
		ind.setCap(req.getCap());
		ind.setProvincia(req.getProvincia());
		// Default "Italia" se non specificata
		ind.setNazione(req.getNazione() == null ? "Italia" : req.getNazione());

		repI.save(ind);
	}

	@Transactional
	@Override
	public void update(IndirizzoReq req, Integer callerId, boolean isAdmin) throws Exception {
		log.debug("update {}", req);
		Indirizzo ind = repI.findById(req.getId())
				.orElseThrow(() -> new ApiException("indirizzo.ntfnd"));

		// Ownership manuale: solo il proprietario dell'indirizzo o un ADMIN possono modificarlo
		if (!isAdmin && !ind.getUtente().getIdUtente().equals(callerId))
			throw new ApiException("indirizzo.forbidden");

		Optional.ofNullable(req.getVia()).ifPresent(ind::setVia);
		Optional.ofNullable(req.getCitta()).ifPresent(ind::setCitta);
		Optional.ofNullable(req.getCap()).ifPresent(ind::setCap);
		Optional.ofNullable(req.getProvincia()).ifPresent(ind::setProvincia);
		Optional.ofNullable(req.getNazione()).ifPresent(ind::setNazione);
	}

	@Transactional
	@Override
	public void delete(Integer id, Integer callerId, boolean isAdmin) throws Exception {
		log.debug("delete {}", id);
		Indirizzo ind = repI.findById(id)
				.orElseThrow(() -> new ApiException("indirizzo.ntfnd"));

		if (!isAdmin && !ind.getUtente().getIdUtente().equals(callerId))
			throw new ApiException("indirizzo.forbidden");

		repI.delete(ind);
	}

	@Override
	public List<IndirizzoDTO> listByUtente(Integer idUtente) throws Exception {
		log.debug("listByUtente {}", idUtente);
		return IndirizzoMap.buildIndirizzoDTOList(repI.findByUtenteIdUtente(idUtente));
	}

	@Override
	public IndirizzoDTO getById(Integer id, Integer callerId, boolean isAdmin) throws Exception {
		log.debug("getById {}", id);
		Indirizzo ind = repI.findById(id)
				.orElseThrow(() -> new ApiException("indirizzo.ntfnd"));

		if (!isAdmin && !ind.getUtente().getIdUtente().equals(callerId))
			throw new ApiException("indirizzo.forbidden");

		return IndirizzoMap.buildIndirizzoDTO(ind);
	}

}
