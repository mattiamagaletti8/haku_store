package com.betacom.jpa.services.implementations;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.betacom.jpa.dto.input.DettaglioCarrelloReq;
import com.betacom.jpa.dto.output.CarrelloDTO;
import com.betacom.jpa.exceptions.ApiException;
import com.betacom.jpa.mapping.CarrelloMap;
import com.betacom.jpa.models.Carrello;
import com.betacom.jpa.models.Coupon;
import com.betacom.jpa.models.DettaglioCarrello;
import com.betacom.jpa.models.Utente;
import com.betacom.jpa.models.VarianteProdotto;
import com.betacom.jpa.repositories.ICarrelloRepository;
import com.betacom.jpa.repositories.IDettaglioCarrelloRepository;
import com.betacom.jpa.repositories.IUtenteRepository;
import com.betacom.jpa.repositories.IVarianteProdottoRepository;
import com.betacom.jpa.services.interfaces.ICarrelloServices;
import com.betacom.jpa.services.interfaces.ICouponServices;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Proprietario: Pier
@Slf4j
@RequiredArgsConstructor
@Service
public class CarrelloImpl implements ICarrelloServices {

	private final ICarrelloRepository repCar;
	private final IDettaglioCarrelloRepository repDet;
	private final IUtenteRepository repU;
	private final IVarianteProdottoRepository repVar;
	private final ICouponServices couponS;

	@Transactional
	@Override
	public Carrello getOrCreateForUtente(Integer idUtente) throws Exception {
		log.debug("getOrCreateForUtente {}", idUtente);
		Carrello car = repCar.findByUtenteIdUtente(idUtente).orElse(null);

		if (car == null) {
			Utente ut = repU.findById(idUtente)
					.orElseThrow(() -> new ApiException("utente.ntfnd"));
			car = new Carrello();
			car.setUtente(ut);
			car.setDataCreazione(LocalDateTime.now());
			car.setRighe(new ArrayList<>());
			repCar.save(car);
		}

		return car;
	}

	@Override
	public CarrelloDTO getCarrello(Integer idUtente) throws Exception {
		log.debug("getCarrello {}", idUtente);
		return CarrelloMap.buildCarrelloDTO(getOrCreateForUtente(idUtente));
	}

	@Transactional
	@Override
	public void addItem(Integer idUtente, DettaglioCarrelloReq req) throws Exception {
		log.debug("addItem {} / {}", idUtente, req);
		Carrello car = getOrCreateForUtente(idUtente);
		VarianteProdotto var = repVar.findById(req.getIdVariante())
				.orElseThrow(() -> new ApiException("variante.ntfnd"));

		DettaglioCarrello riga = repDet.findByCarrelloIdCarrelloAndVarianteIdVariante(car.getIdCarrello(), var.getIdVariante())
				.orElse(null);

		if (riga == null) {
			riga = new DettaglioCarrello();
			riga.setCarrello(car);
			riga.setVariante(var);
			riga.setQuantita(req.getQuantita());
		} else {
			riga.setQuantita(riga.getQuantita() + req.getQuantita());
		}

		repDet.save(riga);
	}

	@Transactional
	@Override
	public void updateItemQuantity(Integer idUtente, Integer idVariante, Integer quantita) throws Exception {
		log.debug("updateItemQuantity {} / {} / {}", idUtente, idVariante, quantita);
		Carrello car = getOrCreateForUtente(idUtente);
		DettaglioCarrello riga = repDet.findByCarrelloIdCarrelloAndVarianteIdVariante(car.getIdCarrello(), idVariante)
				.orElseThrow(() -> new ApiException("dettaglio.ntfnd"));

		riga.setQuantita(quantita);
	}

	@Transactional
	@Override
	public void removeItem(Integer idUtente, Integer idVariante) throws Exception {
		log.debug("removeItem {} / {}", idUtente, idVariante);
		Carrello car = getOrCreateForUtente(idUtente);
		DettaglioCarrello riga = repDet.findByCarrelloIdCarrelloAndVarianteIdVariante(car.getIdCarrello(), idVariante)
				.orElseThrow(() -> new ApiException("dettaglio.ntfnd"));

		repDet.delete(riga);
	}

	@Transactional
	@Override
	public void applyCoupon(Integer idUtente, String codice) throws Exception {
		log.debug("applyCoupon {} / {}", idUtente, codice);
		Carrello car = getOrCreateForUtente(idUtente);
		Coupon coupon = couponS.validateAndGet(codice, idUtente);
		car.setCoupon(coupon);
	}

	@Transactional
	@Override
	public void removeCoupon(Integer idUtente) throws Exception {
		log.debug("removeCoupon {}", idUtente);
		Carrello car = getOrCreateForUtente(idUtente);
		car.setCoupon(null);
	}

	@Transactional
	@Override
	public void clear(Integer idUtente) throws Exception {
		log.debug("clear {}", idUtente);
		Carrello car = getOrCreateForUtente(idUtente);
		repDet.deleteAll(car.getRighe());
		car.getRighe().clear();
		car.setCoupon(null);
	}

}
