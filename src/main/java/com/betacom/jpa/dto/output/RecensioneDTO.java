package com.betacom.jpa.dto.output;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// Proprietario: Sarah
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RecensioneDTO {
	private Integer id;
	private Integer idProdotto;
	private Integer idUtente;
	private String nomeUtente;
	private String cognomeUtente;
	private Integer voto;
	private String titolo;
	private String commento;
	private LocalDateTime dataRecensione;
}
