package com.betacom.jpa.models;

import java.math.BigDecimal;
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

// Proprietario: Mattia
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

	@ManyToOne
	@JoinColumn(
			name = "id_prodotto",
			nullable = false,
			foreignKey = @ForeignKey(name = "fk_variante_prodotto")
			)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Prodotto prodotto;

	@Column(length = 50)
	private String gusto;

	@Column(length = 50)
	private String formato;

	@Column(precision = 10, scale = 2, nullable = false)
	private BigDecimal prezzo;

	@Column(name = "quantita_disponibile", nullable = false)
	private Integer quantitaDisponibile;

	@Column(length = 50)
	private String colore;

	@Column(length = 255)
	private String immagine;

	// valorizzato quando la quantita' torna positiva dopo essere stata esaurita: usato
	// dalla sezione "Nuovamente disponibile" della home (null se non e' mai stata riesaurita/rifornita)
	@Column(name = "data_rifornimento")
	private LocalDateTime dataRifornimento;

}
