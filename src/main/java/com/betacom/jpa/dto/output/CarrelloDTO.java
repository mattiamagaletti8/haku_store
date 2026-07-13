package com.betacom.jpa.dto.output;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CarrelloDTO {
	private Integer id;
	private String stato;
	private LocalDateTime dataCreazione;
	private List<DettaglioCarrelloDTO> righe;
	private CouponDTO coupon;
	private BigDecimal totaleProdotti;
	private BigDecimal valoreSconto;
	private BigDecimal totalePagato;
}
