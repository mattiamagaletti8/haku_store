package com.betacom.jpa.services.interfaces;

import org.springframework.web.multipart.MultipartFile;

// Proprietario: Infrastruttura condivisa
public interface ILogoServices {
	// carica il logo e sostituisce quello precedente, qualunque fosse la sua estensione
	void upload(MultipartFile file) throws Exception;

	// restituisce il path pubblico del logo attuale, o null se non e' mai stato caricato
	String getUrl() throws Exception;
}
