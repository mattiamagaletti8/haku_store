package com.betacom.jpa.services.implementations;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.betacom.jpa.dto.input.UtenteReq;
import com.betacom.jpa.dto.output.UtenteDTO;
import com.betacom.jpa.enums.Roles;
import com.betacom.jpa.exceptions.ApiException;
import com.betacom.jpa.mapping.UtenteMap;
import com.betacom.jpa.models.Utente;
import com.betacom.jpa.repositories.IUtenteRepository;
import com.betacom.jpa.services.interfaces.IUtenteServices;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// ============================================================================
// PROPRIETARIO: Sarah — Modulo Utente, Recensioni & Sicurezza
// ============================================================================
@Slf4j
@RequiredArgsConstructor
@Service
public class UtenteImpl implements IUtenteServices {

	private final IUtenteRepository utR;
	// Collegamento verso l'infrastruttura di sicurezza: il bean BCryptPasswordEncoder definito in SecurityConfig
	private final PasswordEncoder passwordEncoder;

	@Transactional
	@Override
	public UtenteDTO create(UtenteReq req) throws Exception {
		log.debug("create {}", req);
		if (utR.existsByEmail(req.getEmail()))
			throw new ApiException("utente.email.exists");

		Utente ut = new Utente();
		ut.setNome(req.getNome());
		ut.setCognome(req.getCognome());
		ut.setEmail(req.getEmail());
		// La password non viene MAI salvata in chiaro: passwordEncoder.encode la trasforma in hash BCrypt
		ut.setPassword(passwordEncoder.encode(req.getPassword()));
		ut.setTelefono(req.getTelefono());
		// Ruolo forzato a CLIENTE: req.getRuolo() viene ignorato qui, nessuno puo' auto-registrarsi come ADMIN
		ut.setRuolo(Roles.CLIENTE);

		utR.save(ut);
		return UtenteMap.buildUtenteDTO(ut);
	}

	@Transactional
	@Override
	public void update(UtenteReq req, Integer callerId, boolean isAdmin) throws Exception {
		log.debug("update {}", req);
		Utente ut = utR.findById(req.getId())
				.orElseThrow(() -> new ApiException("utente.ntfnd"));

		// Controllo di ownership manuale: un cliente puo' modificare solo il proprio profilo
		if (!isAdmin && !ut.getIdUtente().equals(callerId))
			throw new ApiException("utente.forbidden");

		if (req.getEmail() != null && !req.getEmail().equalsIgnoreCase(ut.getEmail())) {
			if (utR.existsByEmail(req.getEmail()))
				throw new ApiException("utente.email.exists");
			ut.setEmail(req.getEmail());
		}

		Optional.ofNullable(req.getNome()).ifPresent(ut::setNome);
		Optional.ofNullable(req.getCognome()).ifPresent(ut::setCognome);
		Optional.ofNullable(req.getTelefono()).ifPresent(ut::setTelefono);
		// Se la password viene cambiata, viene ri-hashata da zero, mai salvata in chiaro
		Optional.ofNullable(req.getPassword()).ifPresent(pwd -> ut.setPassword(passwordEncoder.encode(pwd)));

		// Il campo ruolo viene onorato SOLO se il chiamante e' gia' ADMIN: un cliente che passa
		// "ruolo": "ADMIN" nel proprio update viene semplicemente ignorato
		if (isAdmin)
			Optional.ofNullable(req.getRuolo()).ifPresent(r -> ut.setRuolo(Roles.valueOf(r)));
	}

	@Transactional
	@Override
	public void delete(Integer id, Integer callerId, boolean isAdmin) throws Exception {
		log.debug("delete {}", id);
		Utente ut = utR.findById(id)
				.orElseThrow(() -> new ApiException("utente.ntfnd"));

		if (!isAdmin && !ut.getIdUtente().equals(callerId))
			throw new ApiException("utente.forbidden");

		utR.delete(ut);
	}

	@Override
	public List<UtenteDTO> list() throws Exception {
		log.debug("list");
		return UtenteMap.buildUtenteDTOList(utR.findAll());
	}

	@Override
	public UtenteDTO getById(Integer id, Integer callerId, boolean isAdmin) throws Exception {
		log.debug("getById {}", id);
		Utente ut = utR.findById(id)
				.orElseThrow(() -> new ApiException("utente.ntfnd"));

		if (!isAdmin && !ut.getIdUtente().equals(callerId))
			throw new ApiException("utente.forbidden");

		return UtenteMap.buildUtenteDTO(ut);
	}

	@Override
	public Utente getEntityByEmail(String email) throws Exception {
		log.debug("getEntityByEmail {}", email);
		// Restituisce l'entity GREZZA (password hash inclusa): usata da AuthImpl.login per il confronto
		// password e da UtenteDetailsService per costruire il principal — mai passata a un DTO output
		return utR.findByEmail(email)
				.orElseThrow(() -> new ApiException("auth.badcredentials"));
	}

}
