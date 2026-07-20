package com.betacom.jpa.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.betacom.jpa.enums.StatoOrdine;
import com.betacom.jpa.enums.StatoPagamento;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// Proprietario: Valerio
@Setter
@Getter
@ToString
@Entity
@Table(name = "ordine")
public class Ordine {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_ordine")
	private Integer idOrdine;

	@ManyToOne
	@JoinColumn(
			name = "id_utente",
			nullable = false,
			foreignKey = @ForeignKey(name = "fk_ordine_utente")
			)
	private Utente utente;

	@Column(name = "data_ordine", nullable = false)
	private LocalDateTime dataOrdine;

	@Column(name = "totale_prodotti", precision = 10, scale = 2, nullable = false)
	private BigDecimal totaleProdotti;

	@Column(name = "valore_sconto", precision = 10, scale = 2, nullable = false)
	private BigDecimal valoreSconto;

	@Column(name = "totale_pagato", precision = 10, scale = 2, nullable = false)
	private BigDecimal totalePagato;

	@Column(name = "codice_coupon_usato", length = 50)
	private String codiceCouponUsato;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private StatoOrdine stato;

	@Column(name = "spedizione_via", length = 100, nullable = false)
	private String spedizioneVia;

	@Column(name = "spedizione_citta", length = 50, nullable = false)
	private String spedizioneCitta;

	@Column(name = "spedizione_cap", length = 10, nullable = false)
	private String spedizioneCap;

	@Column(name = "spedizione_provincia", length = 50)
	private String spedizioneProvincia;

	@Column(name = "spedizione_nazione", length = 50, nullable = false)
	private String spedizioneNazione;

	@Column(name = "metodo_pagamento", length = 50, nullable = false)
	private String metodoPagamento;

	@Enumerated(EnumType.STRING)
	@Column(name = "stato_pagamento", nullable = false)
	private StatoPagamento statoPagamento;

	@OneToMany(mappedBy = "ordine", fetch = FetchType.EAGER)
	private List<DettaglioOrdine> righe;

}
