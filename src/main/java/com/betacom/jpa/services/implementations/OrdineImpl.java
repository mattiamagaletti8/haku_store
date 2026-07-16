package com.betacom.jpa.services.implementations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.betacom.jpa.dto.input.OrdineReq;
import com.betacom.jpa.dto.output.OrdineDTO;
import com.betacom.jpa.enums.StatoOrdine;
import com.betacom.jpa.enums.StatoPagamento;
import com.betacom.jpa.exceptions.ApiException;
import com.betacom.jpa.mapping.CouponMap;
import com.betacom.jpa.mapping.OrdineMap;
import com.betacom.jpa.models.Carrello;
import com.betacom.jpa.models.Coupon;
import com.betacom.jpa.models.DettaglioCarrello;
import com.betacom.jpa.models.DettaglioOrdine;
import com.betacom.jpa.models.Indirizzo;
import com.betacom.jpa.models.Ordine;
import com.betacom.jpa.models.VarianteProdotto;
import com.betacom.jpa.repositories.IDettaglioCarrelloRepository;
import com.betacom.jpa.repositories.IDettaglioOrdineRepository;
import com.betacom.jpa.repositories.IIndirizzoRepository;
import com.betacom.jpa.repositories.IOrdineRepository;
import com.betacom.jpa.repositories.IVarianteProdottoRepository;
import com.betacom.jpa.services.interfaces.ICarrelloServices;
import com.betacom.jpa.services.interfaces.ICouponServices;
import com.betacom.jpa.services.interfaces.IOrdineServices;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// ============================================================================
// PROPRIETARIO: Valerio — Modulo Ordini (Ordine / DettaglioOrdine / checkout)
// ============================================================================
// La classe piu' importante del modulo: qui vive il checkout, il punto in cui tre moduli diversi
// (Carrello di Pier, Catalogo di Mattia, Utente di Sarah) confluiscono in un'unica operazione.
@Slf4j
@RequiredArgsConstructor
@Service
public class OrdineImpl implements IOrdineServices {

	private final IOrdineRepository repOrd;
	private final IDettaglioOrdineRepository repDetOrd;
	// Collegamento verso il modulo di Sarah: serve per verificare l'indirizzo di spedizione al checkout
	private final IIndirizzoRepository repInd;
	// Collegamento verso il modulo di Mattia: serve per leggere/decrementare lo stock delle varianti
	private final IVarianteProdottoRepository repVar;
	// Collegamento verso il modulo di Pier: serve per svuotare le righe del carrello dopo il checkout
	private final IDettaglioCarrelloRepository repDetCar;
	// Collegamento verso il SERVIZIO di Pier (non solo il repository): riusa getOrCreateForUtente
	// invece di duplicare la logica "trova o crea il carrello"
	private final ICarrelloServices carrelloS;
	// Collegamento verso il servizio Coupon di Pier: la validita' del coupon viene ricontrollata qui,
	// non solo quando il cliente lo applica al carrello
	private final ICouponServices couponS;

	@Transactional
	@Override
	public OrdineDTO checkout(Integer idUtente, OrdineReq req) throws Exception {
		log.debug("checkout {} / {}", idUtente, req);

		// idUtente arriva dal controller (preso dal token JWT, mai dal body): recupera il SUO carrello
		Carrello car = carrelloS.getOrCreateForUtente(idUtente);
		if (car.getRighe() == null || car.getRighe().isEmpty())
			throw new ApiException("carrello.empty");

		// Verifica che l'indirizzo scelto esista E appartenga davvero a questo utente
		// (altrimenti un utente potrebbe far spedire un ordine all'indirizzo salvato di qualcun altro)
		Indirizzo ind = repInd.findById(req.getIdIndirizzo())
				.orElseThrow(() -> new ApiException("indirizzo.ntfnd"));
		if (!ind.getUtente().getIdUtente().equals(idUtente))
			throw new ApiException("indirizzo.ntfnd");

		// 1) validazione stock su tutte le righe prima di mutare qualsiasi cosa: se anche una sola
		// variante non ha scorta sufficiente, il checkout fallisce subito, senza aver gia' creato l'ordine
		// o decrementato lo stock di altre righe (tutto o niente, grazie a @Transactional)
		List<String> insufficienti = car.getRighe().stream()
				.filter(r -> r.getVariante().getQuantitaDisponibile() < r.getQuantita())
				.map(r -> String.valueOf(r.getVariante().getIdVariante()))
				.toList();
		if (!insufficienti.isEmpty())
			throw new ApiException("variante.stock.insufficient");

		// 2) ri-validazione del coupon al momento del checkout: un carrello puo' restare aperto giorni,
		// quindi il coupon applicato potrebbe essere scaduto nel frattempo — validateAndGet lo ricontrolla
		// da zero (esistenza, attivo, finestra data), non si fida di quello gia' salvato sul carrello
		Coupon coupon = car.getCoupon() == null ? null : couponS.validateAndGet(car.getCoupon().getCodice());

		// 3) calcolo totali sui prezzi CORRENTI delle varianti — da qui in poi questi numeri
		// vengono storicizzati e non cambieranno mai piu', anche se i prezzi cambiano in futuro
		BigDecimal totaleProdotti = car.getRighe().stream()
				.map(r -> r.getVariante().getPrezzo().multiply(BigDecimal.valueOf(r.getQuantita())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		// Stessa formula di sconto usata da CarrelloMap (delegata a CouponMap), per coerenza tra
		// il totale mostrato nel carrello e quello effettivamente addebitato al checkout
		BigDecimal valoreSconto = coupon == null ? BigDecimal.ZERO : CouponMap.calcolaSconto(totaleProdotti, coupon);
		BigDecimal totalePagato = totaleProdotti.subtract(valoreSconto);

		// 4) creazione dell'ordine con indirizzo e sconto storicizzati come campi piatti
		// (non FK verso Indirizzo/Coupon): l'ordine resta valido anche se l'originale cambia o sparisce
		Ordine ordine = new Ordine();
		ordine.setUtente(car.getUtente());
		ordine.setDataOrdine(LocalDateTime.now());
		ordine.setTotaleProdotti(totaleProdotti);
		ordine.setValoreSconto(valoreSconto);
		ordine.setTotalePagato(totalePagato);
		ordine.setCodiceCouponUsato(coupon == null ? null : coupon.getCodice());
		ordine.setStato(StatoOrdine.IN_ATTESA);
		ordine.setSpedizioneVia(ind.getVia());
		ordine.setSpedizioneCitta(ind.getCitta());
		ordine.setSpedizioneCap(ind.getCap());
		ordine.setSpedizioneProvincia(ind.getProvincia());
		ordine.setSpedizioneNazione(ind.getNazione());
		ordine.setMetodoPagamento(req.getMetodoPagamento());
		ordine.setStatoPagamento(StatoPagamento.DA_PAGARE);
		ordine.setRighe(new ArrayList<>());
		repOrd.save(ordine);

		// 5) per ogni riga del carrello: crea la riga d'ordine corrispondente col prezzo CONGELATO
		// (var.getPrezzo() copiato in questo istante, non piu' ricollegato al prezzo live), poi
		// decrementa lo stock della variante di quanto acquistato
		List<DettaglioCarrello> righeCarrello = List.copyOf(car.getRighe());
		for (DettaglioCarrello rigaCar : righeCarrello) {
			VarianteProdotto var = rigaCar.getVariante();

			DettaglioOrdine rigaOrd = new DettaglioOrdine();
			rigaOrd.setOrdine(ordine);
			rigaOrd.setVariante(var);
			rigaOrd.setQuantita(rigaCar.getQuantita());
			rigaOrd.setPrezzoUnitario(var.getPrezzo());
			repDetOrd.save(rigaOrd);
			ordine.getRighe().add(rigaOrd);

			var.setQuantitaDisponibile(var.getQuantitaDisponibile() - rigaCar.getQuantita());
			repVar.save(var);
		}

		// 6) il carrello si svuota e resta pronto per una nuova sessione di shopping
		//    (e' 1:1 con l'utente: non se ne puo' creare uno nuovo per l'ordine successivo)
		repDetCar.deleteAll(righeCarrello);
		car.getRighe().clear();
		car.setCoupon(null);

		return OrdineMap.buildOrdineDTO(ordine);
	}

	@Override
	public List<OrdineDTO> list(Integer callerId, boolean isAdmin, Integer idUtenteFiltro, String stato, String statoPagamento) throws Exception {
		log.debug("list caller:{} admin:{} utente:{} stato:{} statoPagamento:{}", callerId, isAdmin, idUtenteFiltro, stato, statoPagamento);

		// Se NON e' admin, il filtro utente e' forzato a callerId (i propri ordini), ignorando
		// completamente idUtenteFiltro — un cliente non puo' mai vedere gli ordini di un altro
		// passando semplicemente un idUtente diverso nella query string
		Integer idUtente = isAdmin ? idUtenteFiltro : callerId;
		StatoOrdine s = stato == null ? null : StatoOrdine.valueOf(stato);
		StatoPagamento sp = statoPagamento == null ? null : StatoPagamento.valueOf(statoPagamento);

		return OrdineMap.buildOrdineDTOList(repOrd.searchByFilter(idUtente, s, sp));
	}

	@Override
	public OrdineDTO getById(Integer id, Integer callerId, boolean isAdmin) throws Exception {
		log.debug("getById {}", id);
		Ordine o = repOrd.findById(id)
				.orElseThrow(() -> new ApiException("ordine.ntfnd"));

		// Controllo di ownership manuale (non un @PreAuthorize): solo l'autore dell'ordine o un ADMIN
		// possono vederne il dettaglio, altrimenti 403 "ordine.forbidden"
		if (!isAdmin && !o.getUtente().getIdUtente().equals(callerId))
			throw new ApiException("ordine.forbidden");

		return OrdineMap.buildOrdineDTO(o);
	}

	@Transactional
	@Override
	public void updateStato(OrdineReq req) throws Exception {
		log.debug("updateStato {}", req);
		Ordine o = repOrd.findById(req.getId())
				.orElseThrow(() -> new ApiException("ordine.ntfnd"));
		// Aggiornamento parziale: converte la stringa nell'enum solo se e' stata effettivamente passata
		Optional.ofNullable(req.getStato()).ifPresent(s -> o.setStato(StatoOrdine.valueOf(s)));
	}

	@Transactional
	@Override
	public void updateStatoPagamento(OrdineReq req) throws Exception {
		log.debug("updateStatoPagamento {}", req);
		Ordine o = repOrd.findById(req.getId())
				.orElseThrow(() -> new ApiException("ordine.ntfnd"));
		// Stesso pattern di updateStato, ma sull'asse indipendente del pagamento
		Optional.ofNullable(req.getStatoPagamento()).ifPresent(s -> o.setStatoPagamento(StatoPagamento.valueOf(s)));
	}

}
