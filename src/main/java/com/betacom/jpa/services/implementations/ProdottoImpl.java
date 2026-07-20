package com.betacom.jpa.services.implementations;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.betacom.jpa.dto.input.ProdottoReq;
import com.betacom.jpa.dto.output.ProdottoDTO;
import com.betacom.jpa.exceptions.ApiException;
import com.betacom.jpa.mapping.ProdottoMap;
import com.betacom.jpa.models.Categoria;
import com.betacom.jpa.models.Prodotto;
import com.betacom.jpa.repositories.ICategoriaRepository;
import com.betacom.jpa.repositories.IProdottoRepository;
import com.betacom.jpa.services.interfaces.IProdottoServices;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Proprietario: Mattia
@Slf4j
@RequiredArgsConstructor
@Service
public class ProdottoImpl implements IProdottoServices {

	private final IProdottoRepository repP;
	private final ICategoriaRepository repC;

	@Transactional
	@Override
	public void create(ProdottoReq req) throws Exception {
		log.debug("create {}", req);
		Categoria cat = repC.findById(req.getIdCategoria())
				.orElseThrow(() -> new ApiException("categoria.ntfnd"));

		if (repP.existsByNomeIgnoreCaseAndMarcaIgnoreCase(req.getNome(), req.getMarca())) {
			throw new ApiException("prodotto.exists");
		}

		Prodotto p = new Prodotto();
		p.setCategoria(cat);
		p.setNome(req.getNome());
		p.setDescrizione(req.getDescrizione());
		p.setMarca(req.getMarca());

		repP.save(p);
	}

	@Transactional
	@Override
	public void update(ProdottoReq req) throws Exception {
		log.debug("update {}", req);
		Prodotto p = repP.findById(req.getId())
				.orElseThrow(() -> new ApiException("prodotto.ntfnd"));

		if (req.getIdCategoria() != null) {
			Categoria cat = repC.findById(req.getIdCategoria())
					.orElseThrow(() -> new ApiException("categoria.ntfnd"));
			p.setCategoria(cat);
		}

		Optional.ofNullable(req.getNome()).ifPresent(p::setNome);
		Optional.ofNullable(req.getDescrizione()).ifPresent(p::setDescrizione);
		Optional.ofNullable(req.getMarca()).ifPresent(p::setMarca);
	}

	@Transactional
	@Override
	public void delete(Integer id) throws Exception {
		log.debug("delete {}", id);
		Prodotto p = repP.findById(id)
				.orElseThrow(() -> new ApiException("prodotto.ntfnd"));
		repP.delete(p);
	}

	@Override
	public List<ProdottoDTO> list(Integer idCategoria, String marca, String nome) throws Exception {
		log.debug("list {} / {} / {}", idCategoria, marca, nome);
		List<Prodotto> lP = repP.searchByFilter(idCategoria, marca, nome);
		return ProdottoMap.buildProdottoDTOList(lP);
	}

	@Override
	public ProdottoDTO getById(Integer id) throws Exception {
		log.debug("getById {}", id);
		Prodotto p = repP.findById(id)
				.orElseThrow(() -> new ApiException("prodotto.ntfnd"));
		return ProdottoMap.buildProdottoDTO(p);
	}

}
