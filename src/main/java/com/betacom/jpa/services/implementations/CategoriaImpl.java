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

// ============================================================================
// PROPRIETARIO: Mattia — Modulo Catalogo (Categoria / Prodotto / VarianteProdotto)
// ============================================================================
// Abilita il log.debug(...) usato in ogni metodo
@Slf4j
// Genera un costruttore con un parametro per ogni campo "final" (qui repC) —
// e' cosi' che Spring inietta ICategoriaRepository senza bisogno di @Autowired esplicito
@RequiredArgsConstructor
@Service
public class CategoriaImpl implements ICategoriaServices {

	// Collegamento diretto al database per l'entity Categoria, iniettato da Spring
	private final ICategoriaRepository repC;

	// @Transactional: se qualcosa fallisce a meta' metodo, tutte le modifiche fatte finora vengono annullate
	@Transactional
	@Override
	public void create(CategoriaReq req) throws Exception {
		log.debug("create {}", req);
		// Controllo duplicati: niente due categorie con lo stesso nome
		if (repC.existsByNome(req.getNome()))
			throw new ApiException("categoria.nome.exist");

		Categoria cat = new Categoria();
		cat.setNome(req.getNome());
		// INSERT vero e proprio sul database
		repC.save(cat);
	}

	@Transactional
	@Override
	public void update(CategoriaReq req) throws Exception {
		log.debug("update {}", req);
		// Recupera la categoria da modificare, o lancia un errore "non trovata"
		Categoria cat = repC.findById(req.getId())
				.orElseThrow(() -> new ApiException("categoria.ntfnd"));

		// Aggiorna il nome solo se e' stato passato ED e' effettivamente diverso da quello attuale
		// (evita di ricontrollare i duplicati inutilmente se il nome non cambia)
		if (req.getNome() != null && !req.getNome().equalsIgnoreCase(cat.getNome())) {
			if (repC.existsByNome(req.getNome()))
				throw new ApiException("categoria.nome.exist");
			cat.setNome(req.getNome());
			// Nessun repC.save(cat) esplicito: dentro una @Transactional, Hibernate rileva da solo
			// che l'entity "cat" (gia' agganciata alla sessione da findById) e' cambiata e fa l'UPDATE da solo.
		}
	}

	@Transactional
	@Override
	public void delete(Integer id) throws Exception {
		log.debug("delete {}", id);
		Categoria cat = repC.findById(id)
				.orElseThrow(() -> new ApiException("categoria.ntfnd"));

		// Blocco di integrita': se esistono ancora prodotti collegati a questa categoria,
		// impedisce la cancellazione con un messaggio chiaro invece di lasciare che il DB lanci un errore FK grezzo
		if (cat.getProdotti() != null && !cat.getProdotti().isEmpty())
			throw new ApiException("categoria.has.prodotti");

		repC.delete(cat);
	}

	@Override
	public List<CategoriaDTO> list() throws Exception {
		log.debug("list");
		// Prende tutte le righe dal DB (repC.findAll()) e le converte in DTO con CategoriaMap
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
