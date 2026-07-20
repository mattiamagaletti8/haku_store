package com.betacom.jpa.services.interfaces;

import java.util.List;

import com.betacom.jpa.dto.input.CategoriaReq;
import com.betacom.jpa.dto.output.CategoriaDTO;

// Proprietario: Mattia
public interface ICategoriaServices {
	void create(CategoriaReq req) throws Exception;

	void update(CategoriaReq req) throws Exception;

	void delete(Integer id) throws Exception;

	List<CategoriaDTO> list() throws Exception;

	CategoriaDTO getById(Integer id) throws Exception;
}
