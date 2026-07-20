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

// Proprietario: Valerio
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrdineDTO {
	private Integer id;
	private Integer idUtente;
	private LocalDateTime dataOrdine;
	private BigDecimal totaleProdotti;
	private BigDecimal valoreSconto;
	private BigDecimal totalePagato;
	private String codiceCouponUsato;
	private String stato;
	private String spedizioneVia;
	private String spedizioneCitta;
	private String spedizioneCap;
	private String spedizioneProvincia;
	private String spedizioneNazione;
	private String metodoPagamento;
	private String statoPagamento;
	private List<DettaglioOrdineDTO> righe;
}
