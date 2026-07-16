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

// ============================================================================
// PROPRIETARIO: Pier — Modulo Carrello (Carrello / DettaglioCarrello / Coupon)
// ============================================================================
@Slf4j
@RequiredArgsConstructor
@Service
public class CarrelloImpl implements ICarrelloServices {

	private final ICarrelloRepository repCar;
	private final IDettaglioCarrelloRepository repDet;
	// Serve solo nel ramo "il carrello non esiste ancora", per collegare il nuovo carrello all'utente giusto
	private final IUtenteRepository repU;
	// Serve per verificare che la variante richiesta in addItem esista davvero
	private final IVarianteProdottoRepository repVar;
	// Collegamento verso il modulo di Pier stesso (Coupon): la validazione del coupon non e' duplicata qui,
	// viene delegata a ICouponServices.validateAndGet
	private final ICouponServices couponS;

	@Transactional
	@Override
	public Carrello getOrCreateForUtente(Integer idUtente) throws Exception {
		log.debug("getOrCreateForUtente {}", idUtente);
		Carrello car = repCar.findByUtenteIdUtente(idUtente).orElse(null);

		// Se l'utente non ha ancora un carrello, lo crea al volo: non esiste un endpoint dedicato
		// "crea carrello", il primo accesso lo genera automaticamente
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

		// Cerca se questa variante e' gia' una riga del carrello (rispecchia il vincolo UNIQUE della tabella)
		DettaglioCarrello riga = repDet.findByCarrelloIdCarrelloAndVarianteIdVariante(car.getIdCarrello(), var.getIdVariante())
				.orElse(null);

		if (riga == null) {
			// Prima volta che questa variante entra nel carrello: nuova riga
			riga = new DettaglioCarrello();
			riga.setCarrello(car);
			riga.setVariante(var);
			riga.setQuantita(req.getQuantita());
		} else {
			// Gia' presente: incrementa la quantita' invece di creare una riga duplicata
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

		// Qui la quantita' viene SOSTITUITA (non sommata come in addItem): serve per il caso
		// "l'utente cambia la quantita' direttamente nel carrello" (es. da 3 a 1)
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
		// Delega interamente la validita' del coupon a ICouponServices: se non valido, lancia
		// direttamente l'eccezione appropriata (scaduto/non attivo/non ancora iniziato/non trovato)
		Coupon coupon = couponS.validateAndGet(codice);
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
		// Cancella fisicamente tutte le righe dal database, poi svuota anche la lista in memoria
		// (altrimenti Hibernate potrebbe non accorgersi subito della modifica nella stessa transazione)
		repDet.deleteAll(car.getRighe());
		car.getRighe().clear();
		car.setCoupon(null);
	}

}
