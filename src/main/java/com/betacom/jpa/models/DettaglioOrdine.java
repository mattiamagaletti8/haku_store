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
// PROPRIETARIO: Valerio — Modulo Ordini (Ordine / DettaglioOrdine / checkout)
// ============================================================================
@Setter
@Getter
@ToString
@Entity
@Table(name = "dettaglio_ordine")
public class DettaglioOrdine {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_dettaglio")
	private Integer idDettaglio;

	// Collegamento verso l'Ordine "genitore" di questa riga
	@ManyToOne
	@JoinColumn(
			name = "id_ordine",
			nullable = false,
			foreignKey = @ForeignKey(name = "fk_dettaglio_ordine_ordine")
			)
	// Se l'ordine viene cancellato, le sue righe vengono cancellate a cascata
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Ordine ordine;

	// Collegamento verso la variante acquistata. Nessun cascade: la variante continua a esistere
	// indipendentemente dal destino di questo ordine
	@ManyToOne
	@JoinColumn(
			name = "id_variante",
			nullable = false,
			foreignKey = @ForeignKey(name = "fk_dettaglio_ordine_variante")
			)
	private VarianteProdotto variante;

	@Column(nullable = false)
	private Integer quantita;

	// Prezzo CONGELATO al momento del checkout: a differenza di DettaglioCarrello (che legge sempre
	// il prezzo corrente della variante), qui il prezzo viene copiato una volta e non cambia mai piu',
	// anche se in futuro il prezzo della variante viene modificato dall'admin
	@Column(name = "prezzo_unitario", precision = 10, scale = 2, nullable = false)
	private BigDecimal prezzoUnitario;

}
