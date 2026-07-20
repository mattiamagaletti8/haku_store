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

// Proprietario: Valerio
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

	@ManyToOne
	@JoinColumn(
			name = "id_ordine",
			nullable = false,
			foreignKey = @ForeignKey(name = "fk_dettaglio_ordine_ordine")
			)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Ordine ordine;

	@ManyToOne
	@JoinColumn(
			name = "id_variante",
			nullable = false,
			foreignKey = @ForeignKey(name = "fk_dettaglio_ordine_variante")
			)
	private VarianteProdotto variante;

	@Column(nullable = false)
	private Integer quantita;

	@Column(name = "prezzo_unitario", precision = 10, scale = 2, nullable = false)
	private BigDecimal prezzoUnitario;

}
