package com.betacom.jpa.services.interfaces;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.betacom.jpa.dto.input.VarianteProdottoReq;
import com.betacom.jpa.dto.output.VarianteProdottoDTO;

// Proprietario: Mattia
public interface IVarianteProdottoServices {
	void create(VarianteProdottoReq req) throws Exception;

	void update(VarianteProdottoReq req) throws Exception;

	void delete(Integer id) throws Exception;

	List<VarianteProdottoDTO> listByProdotto(Integer idProdotto) throws Exception;

	VarianteProdottoDTO getById(Integer id) throws Exception;

	// salva il file e aggiorna la variante; restituisce il nome del file salvato
	String uploadImmagine(Integer id, MultipartFile file) throws Exception;
}
