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

// ============================================================================
// PROPRIETARIO: Sarah — Modulo Utente, Recensioni & Sicurezza
// ============================================================================
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

	// L'email e' il "login": unique=true e' cio' che rende possibile autenticarsi con essa
	// (vedi UtenteDetailsService.loadUserByUsername, che cerca proprio per email)
	@Column(length = 100, nullable = false, unique = true)
	private String email;

	// Mai la password in chiaro: viene sempre hashata con BCrypt PRIMA di arrivare qui (vedi UtenteImpl/AuthImpl)
	@Column(nullable = false)
	private String password;

	@Column(length = 20)
	private String telefono;

	// EnumType.STRING per leggibilita' nel DB ("CLIENTE"/"ADMIN" invece di 0/1) —
	// e' il campo che UtentePrincipal.isAdmin() legge per decidere i permessi
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Roles ruolo;

	// Tutte e 4 le relazioni verso i moduli degli altri sono LAZY: il profilo utente da solo
	// non deve mai trascinarsi dietro l'intero storico ordini/recensioni ogni volta che viene letto
	@OneToMany(mappedBy = "utente", fetch = FetchType.LAZY)
	private List<Indirizzo> indirizzi;

	// mappedBy="utente": il lato proprietario della relazione 1:1 e' Carrello (che ha la colonna id_utente),
	// qui c'e' solo il riferimento di lettura inverso
	@OneToOne(mappedBy = "utente", fetch = FetchType.LAZY)
	private Carrello carrello;

	@OneToMany(mappedBy = "utente", fetch = FetchType.LAZY)
	private List<Ordine> ordini;

	@OneToMany(mappedBy = "utente", fetch = FetchType.LAZY)
	private List<Recensione> recensioni;

}
