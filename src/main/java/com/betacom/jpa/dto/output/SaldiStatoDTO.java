package com.betacom.jpa.dto.output;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// Proprietario: Infrastruttura condivisa (non appartiene a una sola persona)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SaldiStatoDTO {
	private boolean attivo;
	private LocalDate inizioSettimana;   // valorizzato solo se attivo
	private LocalDate fineSettimana;     // valorizzato solo se attivo
	private LocalDate prossimoInizio;    // valorizzato solo se non attivo
	private BigDecimal percentualeSconto;
}
