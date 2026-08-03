package com.betacom.jpa.services.implementations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.betacom.jpa.dto.input.ProdottoReq;
import com.betacom.jpa.dto.output.ProdottoDTO;
import com.betacom.jpa.exceptions.ApiException;
import com.betacom.jpa.mapping.ProdottoMap;
import com.betacom.jpa.models.Categoria;
import com.betacom.jpa.models.Prodotto;
import com.betacom.jpa.repositories.ICategoriaRepository;
import com.betacom.jpa.repositories.IDettaglioOrdineRepository;
import com.betacom.jpa.repositories.IProdottoRepository;
import com.betacom.jpa.repositories.IVarianteProdottoRepository;
import com.betacom.jpa.services.interfaces.IProdottoServices;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Proprietario: Mattia
@Slf4j
@RequiredArgsConstructor
@Service
public class ProdottoImpl implements IProdottoServices {

	// quanti prodotti mostrare al massimo in ciascuna sezione della home (in evidenza/novita'/nuovamente disponibili)
	private static final int LIMITE_SEZIONE_HOME = 8;

	private final IProdottoRepository repP;
	private final ICategoriaRepository repC;
	private final IDettaglioOrdineRepository repDO;
	private final IVarianteProdottoRepository repV;

	// cartella su disco dove vengono salvate le immagini, configurata in application.properties
	@Value("${upload.dir.prodotti}")
	private String uploadDir;

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
		p.setDataCreazione(LocalDateTime.now());

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
		eliminaFileImmagine(p.getImmagine());
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

	private void eliminaFileImmagine(String filename) {
		if (filename == null)
			return;
		try {
			Files.deleteIfExists(Path.of(uploadDir).resolve(filename));
		} catch (IOException e) {
			log.warn("impossibile eliminare il vecchio file immagine {}", filename, e);
		}
	}

	@Override
	public List<ProdottoDTO> selectInEvidenza() throws Exception {
		log.debug("selectInEvidenza");
		// ogni riga e' [idProdotto, quantitaTotaleVenduta], gia' ordinata dal piu' venduto
		List<Integer> idOrdinati = repDO.selectProdottiPiuVenduti().stream()
				.map(riga -> (Integer) riga[0])
				.limit(LIMITE_SEZIONE_HOME)
				.toList();
		return prodottiOrdinatiComeIds(idOrdinati);
	}

	@Override
	public List<ProdottoDTO> selectNovita() throws Exception {
		log.debug("selectNovita");
		List<Prodotto> lP = repP.selectNovita().stream().limit(LIMITE_SEZIONE_HOME).toList();
		return ProdottoMap.buildProdottoDTOList(lP);
	}

	@Override
	public List<ProdottoDTO> selectNuovamenteDisponibili() throws Exception {
		log.debug("selectNuovamenteDisponibili");
		// piu' varianti dello stesso prodotto possono essere state rifornite: si tiene solo
		// il primo incontro (il piu' recente, dato l'ordine della query) per non ripetere il prodotto
		List<Integer> idOrdinati = repV.selectNuovamenteDisponibili().stream()
				.map(v -> v.getProdotto().getIdProdotto())
				.distinct()
				.limit(LIMITE_SEZIONE_HOME)
				.toList();
		return prodottiOrdinatiComeIds(idOrdinati);
	}

	// repP.findAllById non garantisce l'ordine di ritorno: qui si rimappano i risultati
	// per rispettare l'ordine di "idOrdinati" (che rappresenta gia' un ranking)
	private List<ProdottoDTO> prodottiOrdinatiComeIds(List<Integer> idOrdinati) {
		Map<Integer, Prodotto> mappaPerId = repP.findAllById(idOrdinati).stream()
				.collect(Collectors.toMap(Prodotto::getIdProdotto, p -> p));
		return idOrdinati.stream()
				.map(mappaPerId::get)
				.filter(Objects::nonNull)
				.map(ProdottoMap::buildProdottoDTO)
				.toList();
	}

}
