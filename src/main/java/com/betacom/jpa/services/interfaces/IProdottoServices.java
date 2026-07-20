package com.betacom.jpa.services.interfaces;

import java.util.List;

import com.betacom.jpa.dto.input.ProdottoReq;
import com.betacom.jpa.dto.output.ProdottoDTO;

// Proprietario: Mattia
public interface IProdottoServices {
	void create(ProdottoReq req) throws Exception;

	void update(ProdottoReq req) throws Exception;

	void delete(Integer id) throws Exception;

	List<ProdottoDTO> list(Integer idCategoria, String marca, String nome) throws Exception;

	ProdottoDTO getById(Integer id) throws Exception;
}
