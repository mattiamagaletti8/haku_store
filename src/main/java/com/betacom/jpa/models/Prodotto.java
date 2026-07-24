package com.betacom.jpa.models;

import java.time.LocalDateTime;
import java.util.List;

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
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// Proprietario: Mattia
@Setter
@Getter
@ToString
@Entity
@Table(name = "prodotto")
public class Prodotto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_prodotto")
	private Integer idProdotto;

	@ManyToOne
	@JoinColumn(
			name = "id_categoria",
			nullable = false,
			foreignKey = @ForeignKey(name = "fk_prodotto_categoria")
			)
	private Categoria categoria;

	@Column(length = 150, nullable = false)
	private String nome;

	@Column(columnDefinition = "TEXT")
	private String descrizione;

	@Column(length = 80, nullable = false)
	private String marca;

	@Column(length = 255)
	private String immagine;

	// usata per la sezione "Novita'" della home: gli ultimi prodotti aggiunti dall'admin
	@Column(name = "data_creazione", nullable = false)
	private LocalDateTime dataCreazione;

	@OneToMany(mappedBy = "prodotto", fetch = FetchType.EAGER)
	private List<VarianteProdotto> varianti;

	@OneToMany(mappedBy = "prodotto", fetch = FetchType.LAZY)
	private List<Recensione> recensioni;

}
