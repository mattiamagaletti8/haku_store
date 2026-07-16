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
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// ============================================================================
// PROPRIETARIO: Pier — Modulo Carrello (Carrello / DettaglioCarrello / Coupon)
// ============================================================================
// Tabella aggiunta rispetto allo schema originale: senza questa tabella intermedia
// il carrello non avrebbe modo di sapere QUALI prodotti/quantita' contiene.
@Setter
@Getter
@ToString
@Entity
@Table(
		name = "dettaglio_carrello",
		uniqueConstraints = {
				// Vincolo di unicita' sulla coppia (carrello, variante): impedisce due righe separate
				// per la stessa variante nello stesso carrello — aggiungere due volte lo stesso prodotto
				// deve incrementare la quantita' della riga esistente, non crearne una nuova
				@UniqueConstraint(
						name = "uk_carrello_variante",
						columnNames = { "id_carrello", "id_variante" }
						)
		}
	)
public class DettaglioCarrello {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_dettaglio")
	private Integer idDettaglio;

	// Collegamento verso il Carrello "genitore" di questa riga
	@ManyToOne
	@JoinColumn(
			name = "id_carrello",
			nullable = false,
			foreignKey = @ForeignKey(name = "fk_dettaglio_carrello_carrello")
			)
	// Se il carrello viene cancellato, tutte le sue righe vengono cancellate a cascata
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Carrello carrello;

	// Collegamento verso la variante di prodotto acquistata in questa riga.
	// Nessun @OnDelete qui: la variante non viene mai cancellata mentre e' referenziata da un carrello attivo
	@ManyToOne
	@JoinColumn(
			name = "id_variante",
			nullable = false,
			foreignKey = @ForeignKey(name = "fk_dettaglio_carrello_variante")
			)
	private VarianteProdotto variante;

	@Column(nullable = false)
	private Integer quantita;

}
