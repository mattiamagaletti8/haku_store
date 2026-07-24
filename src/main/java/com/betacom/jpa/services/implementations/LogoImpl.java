package com.betacom.jpa.services.implementations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.betacom.jpa.exceptions.ApiException;
import com.betacom.jpa.services.interfaces.ILogoServices;

import lombok.extern.slf4j.Slf4j;

// Proprietario: Infrastruttura condivisa
@Slf4j
@Service
public class LogoImpl implements ILogoServices {

	// estensioni accettate per il logo del sito
	private static final Set<String> ESTENSIONI_VALIDE = Set.of("jpg", "jpeg", "png", "gif", "webp", "svg");

	// cartella su disco dove viene salvato il logo, configurata in application.properties
	@Value("${upload.dir.logo}")
	private String uploadDir;

	@Override
	public void upload(MultipartFile file) throws Exception {
		log.debug("upload logo {}", file != null ? file.getOriginalFilename() : null);
		if (file == null || file.isEmpty())
			throw new ApiException("logo.mancante");

		// prende l'estensione dal nome originale del file (es. "haku_store.jpeg" -> "jpeg")
		String nomeOriginale = file.getOriginalFilename();
		int puntoIdx = (nomeOriginale != null) ? nomeOriginale.lastIndexOf('.') : -1;
		String estensione = (puntoIdx >= 0) ? nomeOriginale.substring(puntoIdx + 1).toLowerCase() : "";

		if (!ESTENSIONI_VALIDE.contains(estensione))
			throw new ApiException("logo.formato.invalido");

		Path cartella = Path.of(uploadDir);
		Files.createDirectories(cartella);

		// c'e' sempre un solo logo: cancella qualunque vecchio file "logo.*" prima di salvare quello nuovo
		eliminaVecchioLogo(cartella);

		try {
			file.transferTo(cartella.resolve("logo." + estensione));
		} catch (IOException e) {
			throw new ApiException("logo.errore.salvataggio");
		}
	}

	@Override
	public String getUrl() throws Exception {
		Path cartella = Path.of(uploadDir);
		if (!Files.exists(cartella))
			return null;

		// cerca il file "logo.*" presente in cartella, qualunque sia la sua estensione
		try (Stream<Path> contenuto = Files.list(cartella)) {
			Optional<Path> logo = contenuto
					.filter(p -> p.getFileName().toString().startsWith("logo."))
					.max(Comparator.comparingLong(p -> p.toFile().lastModified()));
			return logo.map(p -> "/immagini/logo/" + p.getFileName()).orElse(null);
		}
	}

	private void eliminaVecchioLogo(Path cartella) throws IOException {
		try (Stream<Path> contenuto = Files.list(cartella)) {
			contenuto.filter(p -> p.getFileName().toString().startsWith("logo."))
					.forEach(p -> {
						try {
							Files.delete(p);
						} catch (IOException e) {
							log.warn("impossibile eliminare il vecchio logo {}", p, e);
						}
					});
		}
	}
}
