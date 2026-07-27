package com.betacom.jpa.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.betacom.jpa.dto.output.ResponseDTO;
import com.betacom.jpa.exceptions.ApiException;
import com.betacom.jpa.services.interfaces.ICategoriaServices;
import com.betacom.jpa.services.interfaces.IProdottoServices;
import com.betacom.jpa.services.interfaces.IVarianteProdottoServices;

import lombok.RequiredArgsConstructor;

// Proprietario: Mattia
// stesso pattern generico usato nel progetto Veicoli: POST .../image (file + id) e GET .../getUrl (filename);
// "tipo" in piu' rispetto a Veicoli, perche' qui servono piu' entita' diverse con immagine
// (categoria, prodotto, variante)
@RequiredArgsConstructor
@RestController
@RequestMapping("/upload/admin")
public class UploadController {
	private final ICategoriaServices catS;
	private final IProdottoServices prodS;
	private final IVarianteProdottoServices variS;

	// POST /upload/admin/image - salva il file e lo collega all'entita' "id" del "tipo" indicato;
	// risponde col nome del file salvato
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("image")
	public ResponseEntity<ResponseDTO> uploadImage(
			@RequestParam("file") MultipartFile file,
			@RequestParam("id") Integer id,
			@RequestParam(value = "tipo", defaultValue = "categoria") String tipo) throws Exception {
		String filename = switch (tipo) {
			case "categoria" -> catS.uploadImmagine(id, file);
			case "variante" -> variS.uploadImmagine(id, file);
			default -> throw new ApiException("upload.tipo.invalido");
		};
		return ResponseEntity.ok(ResponseDTO.builder()
				.msg(filename)
				.build());
	}

	// GET /upload/admin/getUrl?filename=...&tipo=... - trasforma il nome di un file salvato nel path pubblico da cui scaricarlo
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("getUrl")
	public ResponseEntity<ResponseDTO> getUrl(
			@RequestParam("filename") String filename,
			@RequestParam(value = "tipo", defaultValue = "categoria") String tipo) throws Exception {
		String cartella = switch (tipo) {
			case "categoria" -> "categorie";
			case "variante" -> "varianti";
			default -> throw new ApiException("upload.tipo.invalido");
		};
		return ResponseEntity.ok(ResponseDTO.builder()
				.msg("/immagini/" + cartella + "/" + filename)
				.build());
	}
}
