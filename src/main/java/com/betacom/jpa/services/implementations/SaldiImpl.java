package com.betacom.jpa.services.implementations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.betacom.jpa.dto.output.SaldiStatoDTO;
import com.betacom.jpa.models.Prodotto;
import com.betacom.jpa.models.VarianteProdotto;
import com.betacom.jpa.repositories.IDettaglioOrdineRepository;
import com.betacom.jpa.repositories.IProdottoRepository;
import com.betacom.jpa.services.interfaces.ISaldiServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Proprietario: Infrastruttura condivisa (non appartiene a una sola persona)
// Genera settimane di saldi "casuali" nell'anno (stesso seed per tutti i visitatori, quindi
// deterministico, ma imprevedibile senza guardare il codice) e applica uno sconto REALE
// (non solo un banner) ai prodotti meno venduti del catalogo: sia nella vetrina che nel
// carrello e nel checkout, cosi' il prezzo mostrato e' sempre quello davvero pagato.
@Slf4j
@RequiredArgsConstructor
@Service
public class SaldiImpl implements ISaldiServices {

	private static final int NUMERO_SETTIMANE_SALDI = 5;
	private static final int NUMERO_PRODOTTI_SALDO = 8;
	private static final BigDecimal PERCENTUALE_SCONTO = new BigDecimal("20");

	private final IProdottoRepository repP;
	private final IDettaglioOrdineRepository repDO;

	private record Settimana(int giornoInizio, int giornoFine) {
	}

	// stesse settimane per tutto l'anno indicato, diverse da un anno all'altro: una e' fissa a
	// inizio agosto (i saldi di lancio, coerenti con la fine del percorso di formazione
	// raccontata in about), le altre sono pseudo-casuali (LCG seedato sull'anno)
	private List<Settimana> settimaneAnno(int anno) {
		List<Settimana> settimane = new ArrayList<>();

		int giornoInizioAgosto = LocalDate.of(anno, 8, 1).getDayOfYear();
		settimane.add(new Settimana(giornoInizioAgosto, giornoInizioAgosto + 7));

		long seed = anno;
		for (int i = 0; i < NUMERO_SETTIMANE_SALDI - 1; i++) {
			seed = (seed * 1103515245L + 12345L) & 0x7fffffffL;
			double random = seed / (double) 0x7fffffff;
			int giornoInizio = (int) Math.floor(random * 350) + 1;
			settimane.add(new Settimana(giornoInizio, giornoInizio + 7));
		}
		return settimane;
	}

	private Settimana settimanaAttiva(LocalDate oggi) {
		int giornoDellAnno = oggi.getDayOfYear();
		return settimaneAnno(oggi.getYear()).stream()
				.filter(s -> giornoDellAnno >= s.giornoInizio() && giornoDellAnno < s.giornoFine())
				.findFirst()
				.orElse(null);
	}

	@Override
	public boolean isAttivo() {
		return settimanaAttiva(LocalDate.now()) != null;
	}

	@Override
	public SaldiStatoDTO getStato() throws Exception {
		log.debug("getStato");
		LocalDate oggi = LocalDate.now();
		Settimana attiva = settimanaAttiva(oggi);

		if (attiva != null) {
			return SaldiStatoDTO.builder()
					.attivo(true)
					.inizioSettimana(LocalDate.ofYearDay(oggi.getYear(), attiva.giornoInizio()))
					.fineSettimana(LocalDate.ofYearDay(oggi.getYear(), attiva.giornoFine()))
					.percentualeSconto(PERCENTUALE_SCONTO)
					.build();
		}

		return SaldiStatoDTO.builder()
				.attivo(false)
				.prossimoInizio(prossimoInizioSaldi(oggi))
				.percentualeSconto(PERCENTUALE_SCONTO)
				.build();
	}

	// cerca la prossima settimana di saldi a partire da oggi, nell'anno corrente o, se sono
	// gia' passate tutte, nella prima dell'anno prossimo
	private LocalDate prossimoInizioSaldi(LocalDate oggi) {
		int giornoDellAnno = oggi.getDayOfYear();
		return settimaneAnno(oggi.getYear()).stream()
				.filter(s -> s.giornoInizio() > giornoDellAnno)
				.map(s -> LocalDate.ofYearDay(oggi.getYear(), s.giornoInizio()))
				.min(LocalDate::compareTo)
				.orElseGet(() -> {
					int annoProssimo = oggi.getYear() + 1;
					return settimaneAnno(annoProssimo).stream()
							.map(s -> LocalDate.ofYearDay(annoProssimo, s.giornoInizio()))
							.min(LocalDate::compareTo)
							.orElseThrow();
				});
	}

	@Override
	public Set<Integer> idProdottiInSaldo() {
		if (!isAttivo())
			return Set.of();

		// ogni riga e' [idProdotto, quantitaTotaleVenduta], gia' ordinata dal piu' venduto:
		// qui si inverte per avere i meno venduti, e si aggiungono in testa i prodotti mai
		// venduti (che altrimenti non comparirebbero affatto in questa query)
		List<Integer> idVendutiDesc = repDO.selectProdottiPiuVenduti().stream()
				.map(riga -> (Integer) riga[0])
				.toList();
		Set<Integer> setVenduti = new HashSet<>(idVendutiDesc);

		List<Integer> maiVenduti = repP.findAll().stream()
				.map(Prodotto::getIdProdotto)
				.filter(id -> !setVenduti.contains(id))
				.toList();

		List<Integer> vendutiAscendente = new ArrayList<>(idVendutiDesc);
		Collections.reverse(vendutiAscendente);

		return Stream.concat(maiVenduti.stream(), vendutiAscendente.stream())
				.limit(NUMERO_PRODOTTI_SALDO)
				.collect(Collectors.toCollection(HashSet::new));
	}

	@Override
	public BigDecimal calcolaPrezzoScontato(BigDecimal prezzoOriginale) {
		if (prezzoOriginale == null)
			return null;
		BigDecimal moltiplicatore = BigDecimal.ONE.subtract(PERCENTUALE_SCONTO.divide(new BigDecimal("100")));
		return prezzoOriginale.multiply(moltiplicatore).setScale(2, RoundingMode.HALF_UP);
	}

	@Override
	public BigDecimal prezzoEffettivo(VarianteProdotto v) {
		if (v.getProdotto() == null || !idProdottiInSaldo().contains(v.getProdotto().getIdProdotto()))
			return v.getPrezzo();
		return calcolaPrezzoScontato(v.getPrezzo());
	}
}
