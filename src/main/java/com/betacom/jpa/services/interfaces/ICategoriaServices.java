package com.betacom.jpa.services.interfaces;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.betacom.jpa.dto.input.CategoriaReq;
import com.betacom.jpa.dto.output.CategoriaDTO;

// Proprietario: Mattia
public interface ICategoriaServices {
	void create(CategoriaReq req) throws Exception;

	void update(CategoriaReq req) throws Exception;

	void delete(Integer id) throws Exception;

	List<CategoriaDTO> list() throws Exception;

	CategoriaDTO getById(Integer id) throws Exception;

	// salva il file e aggiorna la categoria; restituisce il nome del file salvato
	String uploadImmagine(Integer id, MultipartFile file) throws Exception;
}
