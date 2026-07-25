package com.betacom.jpa.services.interfaces;

import java.math.BigDecimal;
import java.util.Set;

import com.betacom.jpa.dto.output.SaldiStatoDTO;
import com.betacom.jpa.models.VarianteProdotto;

// Proprietario: Infrastruttura condivisa (non appartiene a una sola persona)
public interface ISaldiServices {

	// stato corrente (settimana in corso o data dei prossimi saldi), usato dalla topbar
	SaldiStatoDTO getStato() throws Exception;

	boolean isAttivo();

	// id dei prodotti scontati in questo momento (i meno venduti), vuoto se i saldi non sono attivi
	Set<Integer> idProdottiInSaldo();

	BigDecimal calcolaPrezzoScontato(BigDecimal prezzoOriginale);

	// prezzo realmente da pagare per questa variante in questo momento: scontato se il suo
	// prodotto e' tra quelli in saldo, altrimenti il prezzo pieno
	BigDecimal prezzoEffettivo(VarianteProdotto v);
}
