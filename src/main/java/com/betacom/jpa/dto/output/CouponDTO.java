package com.betacom.jpa.dto.output;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// Proprietario: Pier
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CouponDTO {
	private Integer id;
	private String codice;
	private String tipologia;
	private BigDecimal valore;
	private LocalDateTime dataInizio;
	private LocalDateTime dataFine;
	private Boolean isAttivo;
}
