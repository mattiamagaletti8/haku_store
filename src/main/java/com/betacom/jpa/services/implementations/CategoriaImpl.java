package com.betacom.jpa.services.implementations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

// Proprietario: Mattia
@Slf4j
@RequiredArgsConstructor
@Service
public class CategoriaImpl implements ICategoriaServices {

	// estensioni accettate per l'immagine di una categoria
	private static final Set<String> ESTENSIONI_VALIDE = Set.of("jpg", "jpeg", "png", "gif", "webp");

	private final ICategoriaRepository repC;

	// cartella su disco dove vengono salvate le immagini, configurata in application.properties
	@Value("${upload.dir.categorie}")
	private String uploadDir;

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

		eliminaFileImmagine(cat.getImmagine());
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

	@Transactional
	@Override
	public String uploadImmagine(Integer id, MultipartFile file) throws Exception {
		log.debug("uploadImmagine {} {}", id, file != null ? file.getOriginalFilename() : null);
		Categoria cat = repC.findById(id)
				.orElseThrow(() -> new ApiException("categoria.ntfnd"));

		if (file == null || file.isEmpty())
			throw new ApiException("categoria.immagine.mancante");

		// prende l'estensione dal nome originale del file (es. "logo.png" -> "png")
		String nomeOriginale = file.getOriginalFilename();
		int puntoIdx = (nomeOriginale != null) ? nomeOriginale.lastIndexOf('.') : -1;
		String estensione = (puntoIdx >= 0) ? nomeOriginale.substring(puntoIdx + 1).toLowerCase() : "";

		if (!ESTENSIONI_VALIDE.contains(estensione))
			throw new ApiException("categoria.immagine.formato.invalido");

		// via la vecchia immagine (se c'era), cosi' non restano file orfani su disco
		eliminaFileImmagine(cat.getImmagine());

		// nome fisso per categoria: ogni nuovo upload sovrascrive quello precedente
		String nuovoNomeFile = "categoria_" + id + "." + estensione;

		try {
			Path cartella = Path.of(uploadDir);
			Files.createDirectories(cartella);
			file.transferTo(cartella.resolve(nuovoNomeFile));
		} catch (IOException e) {
			throw new ApiException("categoria.immagine.errore.salvataggio");
		}

		cat.setImmagine(nuovoNomeFile);
		// restituisce solo il nome del file appena salvato: il chiamante (UploadController) lo passa poi a getUrl()
		return nuovoNomeFile;
	}

	// cancella dal disco il file immagine indicato, se esiste; non fa nulla se filename e' null
	private void eliminaFileImmagine(String filename) {
		if (filename == null)
			return;
		try {
			Files.deleteIfExists(Path.of(uploadDir).resolve(filename));
		} catch (IOException e) {
			log.warn("impossibile eliminare il vecchio file immagine {}", filename, e);
		}
	}

}
