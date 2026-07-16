package com.betacom.jpa.models;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// ============================================================================
// PROPRIETARIO: Pier — Modulo Carrello (Carrello / DettaglioCarrello / Coupon)
// ============================================================================
@Setter
@Getter
@ToString
@Entity
@Table(name = "carrello")
public class Carrello {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_carrello")
	private Integer idCarrello;

	// Relazione 1:1 con Utente: unique=true sulla colonna id_utente e' quello che impone davvero
	// il vincolo "un solo carrello per utente" a livello di database (non solo lato Java)
	@OneToOne
	@JoinColumn(
			name = "id_utente",
			nullable = false,
			unique = true,
			foreignKey = @ForeignKey(name = "fk_carrello_utente")
			)
	// Se l'utente viene cancellato, il suo carrello viene cancellato a cascata dal database
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Utente utente;

	// Collegamento verso il Coupon applicato (facoltativo: niente @JoinColumn(nullable=false),
	// un carrello puo' benissimo non avere sconti attivi)
	@ManyToOne
	@JoinColumn(
			name = "id_coupon",
			foreignKey = @ForeignKey(name = "fk_carrello_coupon")
			)
	private Coupon coupon;

	@Column(name = "data_creazione", nullable = false)
	private LocalDateTime dataCreazione;

	// Le righe del carrello (prodotto+quantita'): EAGER perche' ogni volta che si legge il carrello
	// servono subito per calcolare i totali (vedi CarrelloMap)
	@OneToMany(mappedBy = "carrello", fetch = FetchType.EAGER)
	private List<DettaglioCarrello> righe;

}
