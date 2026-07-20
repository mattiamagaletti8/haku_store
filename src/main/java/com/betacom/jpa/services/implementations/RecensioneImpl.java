package com.betacom.jpa.services.implementations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.betacom.jpa.dto.input.RecensioneReq;
import com.betacom.jpa.dto.output.RecensioneDTO;
import com.betacom.jpa.exceptions.ApiException;
import com.betacom.jpa.mapping.RecensioneMap;
import com.betacom.jpa.models.Prodotto;
import com.betacom.jpa.models.Recensione;
import com.betacom.jpa.models.Utente;
import com.betacom.jpa.repositories.IProdottoRepository;
import com.betacom.jpa.repositories.IRecensioneRepository;
import com.betacom.jpa.repositories.IUtenteRepository;
import com.betacom.jpa.services.interfaces.IRecensioneServices;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Proprietario: Sarah
@Slf4j
@RequiredArgsConstructor
@Service
public class RecensioneImpl implements IRecensioneServices {

	private final IRecensioneRepository repR;
	private final IProdottoRepository repP;
	private final IUtenteRepository repU;

	@Transactional
	@Override
	public void create(RecensioneReq req, Integer idUtente) throws Exception {
		log.debug("create {} for utente {}", req, idUtente);
		Prodotto p = repP.findById(req.getIdProdotto())
				.orElseThrow(() -> new ApiException("prodotto.ntfnd"));
		Utente ut = repU.findById(idUtente)
				.orElseThrow(() -> new ApiException("utente.ntfnd"));

		Recensione r = new Recensione();
		r.setProdotto(p);
		r.setUtente(ut);
		r.setVoto(req.getVoto());
		r.setTitolo(req.getTitolo());
		r.setCommento(req.getCommento());
		r.setDataRecensione(LocalDateTime.now());

		repR.save(r);
	}

	@Transactional
	@Override
	public void update(RecensioneReq req, Integer callerId, boolean isAdmin) throws Exception {
		log.debug("update {}", req);
		Recensione r = repR.findById(req.getId())
				.orElseThrow(() -> new ApiException("recensione.ntfnd"));

		if (!isAdmin && !r.getUtente().getIdUtente().equals(callerId))
			throw new ApiException("recensione.forbidden");

		Optional.ofNullable(req.getVoto()).ifPresent(r::setVoto);
		Optional.ofNullable(req.getTitolo()).ifPresent(r::setTitolo);
		Optional.ofNullable(req.getCommento()).ifPresent(r::setCommento);
	}

	@Transactional
	@Override
	public void delete(Integer id, Integer callerId, boolean isAdmin) throws Exception {
		log.debug("delete {}", id);
		Recensione r = repR.findById(id)
				.orElseThrow(() -> new ApiException("recensione.ntfnd"));

		if (!isAdmin && !r.getUtente().getIdUtente().equals(callerId))
			throw new ApiException("recensione.forbidden");

		repR.delete(r);
	}

	@Override
	public List<RecensioneDTO> listByProdotto(Integer idProdotto) throws Exception {
		log.debug("listByProdotto {}", idProdotto);
		return RecensioneMap.buildRecensioneDTOList(repR.findByProdottoIdProdotto(idProdotto));
	}

}
