package com.betacom.jpa.services.implementations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.betacom.jpa.dto.input.CouponReq;
import com.betacom.jpa.dto.output.CouponDTO;
import com.betacom.jpa.enums.TipologiaCoupon;
import com.betacom.jpa.exceptions.ApiException;
import com.betacom.jpa.mapping.CouponMap;
import com.betacom.jpa.enums.StatoOrdine;
import com.betacom.jpa.models.Carrello;
import com.betacom.jpa.models.Coupon;
import com.betacom.jpa.repositories.ICarrelloRepository;
import com.betacom.jpa.repositories.ICouponRepository;
import com.betacom.jpa.repositories.IOrdineRepository;
import com.betacom.jpa.services.interfaces.ICouponServices;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Proprietario: Pier
@Slf4j
@RequiredArgsConstructor
@Service
public class CouponImpl implements ICouponServices {

	private final ICouponRepository repC;
	private final ICarrelloRepository repCar;
	private final IOrdineRepository repO;

	@Transactional
	@Override
	public void create(CouponReq req) throws Exception {
		log.debug("create {}", req);
		if (repC.existsByCodice(req.getCodice()))
			throw new ApiException("coupon.codice.exist");

		if (!req.getDataFine().isAfter(req.getDataInizio()))
			throw new ApiException("coupon.dates.invalid");

		Coupon c = new Coupon();
		c.setCodice(req.getCodice());
		c.setTipologia(TipologiaCoupon.valueOf(req.getTipologia()));
		c.setValore(req.getValore());
		c.setDataInizio(req.getDataInizio());
		c.setDataFine(req.getDataFine());
		c.setIsAttivo(req.getIsAttivo() == null ? Boolean.TRUE : req.getIsAttivo());

		repC.save(c);
	}

	@Transactional
	@Override
	public void update(CouponReq req) throws Exception {
		log.debug("update {}", req);
		Coupon c = repC.findById(req.getId())
				.orElseThrow(() -> new ApiException("coupon.ntfnd"));

		if (req.getCodice() != null && !req.getCodice().equalsIgnoreCase(c.getCodice())) {
			if (repC.existsByCodice(req.getCodice()))
				throw new ApiException("coupon.codice.exist");
			c.setCodice(req.getCodice());
		}

		Optional.ofNullable(req.getTipologia()).ifPresent(t -> c.setTipologia(TipologiaCoupon.valueOf(t)));
		Optional.ofNullable(req.getValore()).ifPresent(c::setValore);
		Optional.ofNullable(req.getIsAttivo()).ifPresent(c::setIsAttivo);

		LocalDateTime nuovoInizio = req.getDataInizio() != null ? req.getDataInizio() : c.getDataInizio();
		LocalDateTime nuovoFine = req.getDataFine() != null ? req.getDataFine() : c.getDataFine();
		if (!nuovoFine.isAfter(nuovoInizio))
			throw new ApiException("coupon.dates.invalid");
		c.setDataInizio(nuovoInizio);
		c.setDataFine(nuovoFine);
	}

	@Transactional
	@Override
	public void delete(Integer id) throws Exception {
		log.debug("delete {}", id);
		Coupon c = repC.findById(id)
				.orElseThrow(() -> new ApiException("coupon.ntfnd"));

		List<Carrello> carrelliCollegati = repCar.findByCouponIdCoupon(id);
		carrelliCollegati.forEach(car -> car.setCoupon(null));
		repCar.saveAll(carrelliCollegati);

		repC.delete(c);
	}

	@Override
	public List<CouponDTO> list() throws Exception {
		log.debug("list");
		return CouponMap.buildCouponDTOList(repC.findAll());
	}

	@Override
	public CouponDTO getById(Integer id) throws Exception {
		log.debug("getById {}", id);
		Coupon c = repC.findById(id)
				.orElseThrow(() -> new ApiException("coupon.ntfnd"));
		return CouponMap.buildCouponDTO(c);
	}

	@Override
	public Coupon validateAndGet(String codice, Integer idUtente) throws Exception {
		log.debug("validateAndGet {} / utente {}", codice, idUtente);
		Coupon c = repC.findByCodice(codice)
				.orElseThrow(() -> new ApiException("coupon.ntfnd"));

		if (!Boolean.TRUE.equals(c.getIsAttivo()))
			throw new ApiException("coupon.not.active");

		LocalDateTime now = LocalDateTime.now();
		if (now.isBefore(c.getDataInizio()))
			throw new ApiException("coupon.not.started");
		if (now.isAfter(c.getDataFine()))
			throw new ApiException("coupon.expired");

		// un coupon si puo' usare una volta sola per utente: se esiste gia' un suo ordine
		// non annullato con questo stesso codice, non puo' riapplicarlo
		if (repO.existsByUtenteIdUtenteAndCodiceCouponUsatoIgnoreCaseAndStatoNot(idUtente, codice, StatoOrdine.ANNULLATO))
			throw new ApiException("coupon.already.used");

		return c;
	}

}
