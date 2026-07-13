package com.betacom.jpa.services.implementations;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.betacom.jpa.dto.input.CategoriaReq;
import com.betacom.jpa.dto.output.CategoriaDTO;
import com.betacom.jpa.exceptions.ApiException;
import com.betacom.jpa.mapping.CategoriaMap;
import com.betacom.jpa.models.Categoria;
import com.betacom.jpa.repositories.ICategoriaRepository;
import com.betacom.jpa.services.interfaces.ICategoriaServices;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class CategoriaImpl implements ICategoriaServices {

	private final ICategoriaRepository repC;

	@Transactional
	@Override
	public void create(CategoriaReq req) throws Exception {
		log.debug("create {}", req);
		if (repC.existsByNome(req.getNome()))
			throw new ApiException("categoria.nome.exist");

		Categoria cat = new Categoria();
		cat.setNome(req.getNome());
		repC.save(cat);
	}

	@Transactional
	@Override
	public void update(CategoriaReq req) throws Exception {
		log.debug("update {}", req);
		Categoria cat = repC.findById(req.getId())
				.orElseThrow(() -> new ApiException("categoria.ntfnd"));

		if (req.getNome() != null && !req.getNome().equalsIgnoreCase(cat.getNome())) {
			if (repC.existsByNome(req.getNome()))
				throw new ApiException("categoria.nome.exist");
			cat.setNome(req.getNome());
		}
	}

	@Transactional
	@Override
	public void delete(Integer id) throws Exception {
		log.debug("delete {}", id);
		Categoria cat = repC.findById(id)
				.orElseThrow(() -> new ApiException("categoria.ntfnd"));

		if (cat.getProdotti() != null && !cat.getProdotti().isEmpty())
			throw new ApiException("categoria.has.prodotti");

		repC.delete(cat);
	}

	@Override
	public List<CategoriaDTO> list() throws Exception {
		log.debug("list");
		return CategoriaMap.buildCategoriaDTOList(repC.findAll());
	}

	@Override
	public CategoriaDTO getById(Integer id) throws Exception {
		log.debug("getById {}", id);
		Categoria cat = repC.findById(id)
				.orElseThrow(() -> new ApiException("categoria.ntfnd"));
		return CategoriaMap.buildCategoriaDTO(cat);
	}

}
