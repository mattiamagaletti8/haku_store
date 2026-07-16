package com.betacom.jpa.models;

import java.math.BigDecimal;

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

// ============================================================================
// PROPRIETARIO: Mattia — Modulo Catalogo (Categoria / Prodotto / VarianteProdotto)
// ============================================================================
@Setter
@Getter
@ToString
@Entity
@Table(name = "variante_prodotto")
public class VarianteProdotto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_variante")
	private Integer idVariante;

	// Collegamento verso il Prodotto "genitore" di questa variante
	@ManyToOne
	@JoinColumn(
			name = "id_prodotto",
			nullable = false,
			foreignKey = @ForeignKey(name = "fk_variante_prodotto")
			)
	// Se il Prodotto viene cancellato dal database, cancella automaticamente (CASCADE) anche tutte
	// le sue varianti — a livello di vincolo FK del DB, non solo lato Java/Hibernate
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Prodotto prodotto;

	// Attributi opzionali (nessun @Column(nullable=false)): non tutte le categorie di prodotto
	// usano tutti e tre questi campi (es. un integratore ha gusto, un attrezzo ha colore)
	@Column(length = 50)
	private String gusto;

	@Column(length = 50)
	private String formato;

	// BigDecimal invece di Long/Double: precisione esatta sui decimali, indispensabile per i prezzi
	@Column(precision = 10, scale = 2, nullable = false)
	private BigDecimal prezzo;

	// Quantita' effettivamente disponibile in magazzino: e' il campo che il checkout controlla e decrementa
	@Column(name = "quantita_disponibile", nullable = false)
	private Integer quantitaDisponibile;

	@Column(length = 50)
	private String colore;

}
