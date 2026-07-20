package com.betacom.jpa.models;

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
@Table(name = "indirizzo")
public class Indirizzo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_indirizzo")
	private Integer idIndirizzo;

	@ManyToOne
	@JoinColumn(
			name = "id_utente",
			nullable = false,
			foreignKey = @ForeignKey(name = "fk_indirizzo_utente")
			)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Utente utente;

	@Column(length = 100, nullable = false)
	private String via;

	@Column(length = 50, nullable = false)
	private String citta;

	@Column(length = 10, nullable = false)
	private String cap;

	@Column(length = 50)
	private String provincia;

	@Column(length = 50, nullable = false)
	private String nazione;

}
