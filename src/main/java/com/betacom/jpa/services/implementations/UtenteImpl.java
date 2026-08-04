package com.betacom.jpa.services.implementations;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.betacom.jpa.dto.input.ChangePasswordReq;
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

// Proprietario: Sarah
@Slf4j
@RequiredArgsConstructor
@Service
public class UtenteImpl implements IUtenteServices {

	private final IUtenteRepository utR;
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
		ut.setPassword(passwordEncoder.encode(req.getPassword()));
		ut.setTelefono(req.getTelefono());
		ut.setRuolo(Roles.CLIENTE);

		utR.save(ut);
		return UtenteMap.buildUtenteDTO(ut);
	}

	@Transactional
	@Override
	public UtenteDTO createByAdmin(UtenteReq req) throws Exception {
		log.debug("createByAdmin {}", req);
		if (utR.existsByEmail(req.getEmail()))
			throw new ApiException("utente.email.exists");

		Utente ut = new Utente();
		ut.setNome(req.getNome());
		ut.setCognome(req.getCognome());
		ut.setEmail(req.getEmail());
		ut.setPassword(passwordEncoder.encode(req.getPassword()));
		ut.setTelefono(req.getTelefono());
		ut.setRuolo(req.getRuolo() != null ? Roles.valueOf(req.getRuolo()) : Roles.CLIENTE);

		utR.save(ut);
		return UtenteMap.buildUtenteDTO(ut);
	}

	@Transactional
	@Override
	public void changePassword(Integer callerId, ChangePasswordReq req) throws Exception {
		log.debug("changePassword {}", callerId);
		Utente ut = utR.findById(callerId)
				.orElseThrow(() -> new ApiException("utente.ntfnd"));

		if (!passwordEncoder.matches(req.getOldPassword(), ut.getPassword()))
			throw new ApiException("utente.password.errata");

		ut.setPassword(passwordEncoder.encode(req.getNewPassword()));
	}

	@Transactional
	@Override
	public void update(UtenteReq req, Integer callerId, boolean isAdmin) throws Exception {
		log.debug("update {}", req);
		Utente ut = utR.findById(req.getId())
				.orElseThrow(() -> new ApiException("utente.ntfnd"));

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
		Optional.ofNullable(req.getPassword()).ifPresent(pwd -> ut.setPassword(passwordEncoder.encode(pwd)));

		if (isAdmin) {
			Optional.ofNullable(req.getRuolo()).ifPresent(r -> ut.setRuolo(Roles.valueOf(r)));
			Optional.ofNullable(req.getAttivo()).ifPresent(ut::setAttivo);
		}
	}

	@Transactional
	@Override
	public String delete(Integer id, Integer callerId, boolean isAdmin) throws Exception {
		log.debug("delete {}", id);
		Utente ut = utR.findById(id)
				.orElseThrow(() -> new ApiException("utente.ntfnd"));

		if (!isAdmin && !ut.getIdUtente().equals(callerId))
			throw new ApiException("utente.forbidden");

		if (ut.getOrdini() != null && !ut.getOrdini().isEmpty()) {
			// per obblighi fiscali non si cancellano gli utenti con ordini: si disattiva l'account
			ut.setAttivo(false);
			return "Account disattivato: risultano ordini collegati che devono essere conservati per obblighi fiscali. Non sara' piu' possibile accedere con queste credenziali.";
		}

		utR.delete(ut);
		return "Account eliminato con successo.";
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
		return utR.findByEmail(email)
				.orElseThrow(() -> new ApiException("auth.badcredentials"));
	}

}
