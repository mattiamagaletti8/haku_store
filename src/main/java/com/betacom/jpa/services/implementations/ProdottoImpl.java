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

// ============================================================================
// PROPRIETARIO: Mattia — Modulo Catalogo (Categoria / Prodotto / VarianteProdotto)
// ============================================================================
@Slf4j
@RequiredArgsConstructor
@Service
public class ProdottoImpl implements IProdottoServices {

	// Repository del prodotto stesso
	private final IProdottoRepository repP;
	// Collegamento verso il repository di Categoria: serve per verificare che la categoria indicata esista davvero
	private final ICategoriaRepository repC;

	@Transactional
	@Override
	public void create(ProdottoReq req) throws Exception {
		log.debug("create {}", req);
		// Verifica che la categoria indicata esista prima di agganciare il prodotto ad essa
		Categoria cat = repC.findById(req.getIdCategoria())
				.orElseThrow(() -> new ApiException("categoria.ntfnd"));

		// Controllo duplicati: stesso nome+marca gia' presente = errore, evita cataloghi con doppioni
		if (repP.existsByNomeIgnoreCaseAndMarcaIgnoreCase(req.getNome(), req.getMarca())) {
			throw new ApiException("prodotto.exists");
		}

		Prodotto p = new Prodotto();
		// Collega il prodotto alla Categoria appena verificata (relazione @ManyToOne)
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

		// Se viene passata una nuova categoria, la verifica e ricollega il prodotto ad essa
		if (req.getIdCategoria() != null) {
			Categoria cat = repC.findById(req.getIdCategoria())
					.orElseThrow(() -> new ApiException("categoria.ntfnd"));
			p.setCategoria(cat);
		}

		// Pattern di aggiornamento parziale: aggiorna il campo solo se e' stato effettivamente passato
		// (Optional.ofNullable + ifPresent evita if/null-check espliciti ripetuti per ogni campo)
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
		// Nessun controllo su varianti/recensioni collegate: la cancellazione del prodotto e' pensata
		// per propagarsi (a differenza di Categoria che blocca se ha prodotti collegati)
		repP.delete(p);
	}

	@Override
	public List<ProdottoDTO> list(Integer idCategoria, String marca, String nome) throws Exception {
		log.debug("list {} / {} / {}", idCategoria, marca, nome);
		// Delega interamente il filtro alla query nominata nel repository
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
