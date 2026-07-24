package com.betacom.jpa.services.implementations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

	// estensioni accettate per l'immagine di una variante
	private static final Set<String> ESTENSIONI_VALIDE = Set.of("jpg", "jpeg", "png", "gif", "webp");

	private final IVarianteProdottoRepository repV;
	private final IProdottoRepository repP;

	// cartella su disco dove vengono salvate le immagini, configurata in application.properties
	@Value("${upload.dir.varianti}")
	private String uploadDir;

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

		int quantitaPrima = v.getQuantitaDisponibile();

		Optional.ofNullable(req.getGusto()).ifPresent(v::setGusto);
		Optional.ofNullable(req.getFormato()).ifPresent(v::setFormato);
		Optional.ofNullable(req.getColore()).ifPresent(v::setColore);
		Optional.ofNullable(req.getPrezzo()).ifPresent(v::setPrezzo);
		Optional.ofNullable(req.getQuantitaDisponibile()).ifPresent(v::setQuantitaDisponibile);

		// se la quantita' passa da esaurita (0 o meno) a positiva, e' un rifornimento: si registra
		// il momento per la sezione "Nuovamente disponibile" della home
		if (quantitaPrima <= 0 && v.getQuantitaDisponibile() > 0) {
			v.setDataRifornimento(LocalDateTime.now());
		}
	}

	@Transactional
	@Override
	public void delete(Integer id) throws Exception {
		log.debug("delete {}", id);
		VarianteProdotto v = repV.findById(id)
				.orElseThrow(() -> new ApiException("variante.ntfnd"));
		eliminaFileImmagine(v.getImmagine());
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

	@Transactional
	@Override
	public String uploadImmagine(Integer id, MultipartFile file) throws Exception {
		log.debug("uploadImmagine {} {}", id, file != null ? file.getOriginalFilename() : null);
		VarianteProdotto v = repV.findById(id)
				.orElseThrow(() -> new ApiException("variante.ntfnd"));

		if (file == null || file.isEmpty())
			throw new ApiException("variante.immagine.mancante");

		String nomeOriginale = file.getOriginalFilename();
		int puntoIdx = (nomeOriginale != null) ? nomeOriginale.lastIndexOf('.') : -1;
		String estensione = (puntoIdx >= 0) ? nomeOriginale.substring(puntoIdx + 1).toLowerCase() : "";

		if (!ESTENSIONI_VALIDE.contains(estensione))
			throw new ApiException("variante.immagine.formato.invalido");

		eliminaFileImmagine(v.getImmagine());

		String nuovoNomeFile = "variante_" + id + "." + estensione;

		try {
			Path cartella = Path.of(uploadDir);
			Files.createDirectories(cartella);
			file.transferTo(cartella.resolve(nuovoNomeFile));
		} catch (IOException e) {
			throw new ApiException("variante.immagine.errore.salvataggio");
		}

		v.setImmagine(nuovoNomeFile);
		return nuovoNomeFile;
	}

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
