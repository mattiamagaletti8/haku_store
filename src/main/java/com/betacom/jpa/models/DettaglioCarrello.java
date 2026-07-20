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

// Proprietario: Pier
@Setter
@Getter
@ToString
@Entity
@Table(
		name = "dettaglio_carrello",
		uniqueConstraints = {
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

	@ManyToOne
	@JoinColumn(
			name = "id_carrello",
			nullable = false,
			foreignKey = @ForeignKey(name = "fk_dettaglio_carrello_carrello")
			)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Carrello carrello;

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
