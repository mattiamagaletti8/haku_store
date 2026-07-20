package com.betacom.jpa.models;

import java.util.List;

import com.betacom.jpa.enums.Roles;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// Proprietario: Sarah
@Setter
@Getter
@ToString
@Entity
@Table(name = "utente")
public class Utente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_utente")
	private Integer idUtente;

	@Column(length = 50, nullable = false)
	private String nome;

	@Column(length = 50, nullable = false)
	private String cognome;

	@Column(length = 100, nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(length = 20)
	private String telefono;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Roles ruolo;

	@OneToMany(mappedBy = "utente", fetch = FetchType.LAZY)
	private List<Indirizzo> indirizzi;

	@OneToOne(mappedBy = "utente", fetch = FetchType.LAZY)
	private Carrello carrello;

	@OneToMany(mappedBy = "utente", fetch = FetchType.LAZY)
	private List<Ordine> ordini;

	@OneToMany(mappedBy = "utente", fetch = FetchType.LAZY)
	private List<Recensione> recensioni;

}
