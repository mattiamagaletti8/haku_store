package com.betacom.jpa.services.implementations;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.betacom.jpa.dto.input.VarianteProdottoReq;
import com.betacom.jpa.dto.output.VarianteProdottoDTO;
import com.betacom.jpa.exceptions.ApiException;
import com.betacom.jpa.mapping.VarianteProdottoMap;
import com.betacom.jpa.models.Prodotto;
import com.betacom.jpa.models.VarianteProdotto;
import com.betacom.jpa.repositories.IProdottoRepository;
import com.betacom.jpa.repositories.IVarianteProdottoRepository;
import com.betacom.jpa.services.interfaces.IVarianteProdottoServices;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Proprietario: Mattia
@Slf4j
@RequiredArgsConstructor
@Service
public class VarianteProdottoImpl implements IVarianteProdottoServices {

	private final IVarianteProdottoRepository repV;
	private final IProdottoRepository repP;

	@Transactional
	@Override
	public void create(VarianteProdottoReq req) throws Exception {
		log.debug("create {}", req);
		Prodotto p = repP.findById(req.getIdProdotto())
				.orElseThrow(() -> new ApiException("prodotto.ntfnd"));

		if (repV.existsByProdottoIdProdottoAndGustoAndFormatoAndColore(
				req.getIdProdotto(), req.getGusto(), req.getFormato(), req.getColore())) {
			throw new ApiException("variante.exists");
		}

		VarianteProdotto v = new VarianteProdotto();
		v.setProdotto(p);
		v.setGusto(req.getGusto());
		v.setFormato(req.getFormato());
		v.setColore(req.getColore());
		v.setPrezzo(req.getPrezzo());
		v.setQuantitaDisponibile(req.getQuantitaDisponibile() == null ? 0 : req.getQuantitaDisponibile());

		repV.save(v);
	}

	@Transactional
	@Override
	public void update(VarianteProdottoReq req) throws Exception {
		log.debug("update {}", req);
		VarianteProdotto v = repV.findById(req.getId())
				.orElseThrow(() -> new ApiException("variante.ntfnd"));

		Optional.ofNullable(req.getGusto()).ifPresent(v::setGusto);
		Optional.ofNullable(req.getFormato()).ifPresent(v::setFormato);
		Optional.ofNullable(req.getColore()).ifPresent(v::setColore);
		Optional.ofNullable(req.getPrezzo()).ifPresent(v::setPrezzo);
		Optional.ofNullable(req.getQuantitaDisponibile()).ifPresent(v::setQuantitaDisponibile);
	}

	@Transactional
	@Override
	public void delete(Integer id) throws Exception {
		log.debug("delete {}", id);
		VarianteProdotto v = repV.findById(id)
				.orElseThrow(() -> new ApiException("variante.ntfnd"));
		repV.delete(v);
	}

	@Override
	public List<VarianteProdottoDTO> listByProdotto(Integer idProdotto) throws Exception {
		log.debug("listByProdotto {}", idProdotto);
		return VarianteProdottoMap.buildVarianteProdottoDTOList(repV.findByProdottoIdProdotto(idProdotto));
	}

	@Override
	public VarianteProdottoDTO getById(Integer id) throws Exception {
		log.debug("getById {}", id);
		VarianteProdotto v = repV.findById(id)
				.orElseThrow(() -> new ApiException("variante.ntfnd"));
		return VarianteProdottoMap.buildVarianteProdottoDTO(v);
	}

}
