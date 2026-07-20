package com.betacom.jpa.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.betacom.jpa.enums.TipologiaCoupon;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// Proprietario: Pier
@Setter
@Getter
@ToString
@Entity
@Table(name = "coupon")
public class Coupon {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_coupon")
	private Integer idCoupon;

	@Column(length = 50, nullable = false, unique = true)
	private String codice;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TipologiaCoupon tipologia;

	@Column(precision = 10, scale = 2, nullable = false)
	private BigDecimal valore;

	@Column(name = "data_inizio", nullable = false)
	private LocalDateTime dataInizio;

	@Column(name = "data_fine", nullable = false)
	private LocalDateTime dataFine;

	@Column(name = "is_attivo", nullable = false)
	private Boolean isAttivo;

}
