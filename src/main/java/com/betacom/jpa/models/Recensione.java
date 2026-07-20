package com.betacom.jpa.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// Proprietario: Sarah
@Setter
@Getter
@ToString
@Entity
@Table(name = "recensione")
public class Recensione {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_recensione")
	private Integer idRecensione;

	@ManyToOne
	@JoinColumn(
			name = "id_prodotto",
			nullable = false,
			foreignKey = @ForeignKey(name = "fk_recensione_prodotto")
			)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Prodotto prodotto;

	@ManyToOne
	@JoinColumn(
			name = "id_utente",
			nullable = false,
			foreignKey = @ForeignKey(name = "fk_recensione_utente")
			)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Utente utente;

	@Column(nullable = false)
	private Integer voto;

	@Column(length = 100)
	private String titolo;

	@Column(columnDefinition = "TEXT")
	private String commento;

	@Column(name = "data_recensione", nullable = false)
	private LocalDateTime dataRecensione;

}
