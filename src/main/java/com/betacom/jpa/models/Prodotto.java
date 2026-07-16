package com.betacom.jpa.models;

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

// ============================================================================
// PROPRIETARIO: Mattia — Modulo Catalogo (Categoria / Prodotto / VarianteProdotto)
// ============================================================================
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

	// Collegamento verso la Categoria di appartenenza: "molti Prodotto per una Categoria".
	// nullable=false: ogni prodotto DEVE avere una categoria, non puo' restare orfano.
	@ManyToOne
	@JoinColumn(
			name = "id_categoria",
			nullable = false,
			// Nome esplicito del vincolo FK sul database, utile per leggere gli errori SQL
			foreignKey = @ForeignKey(name = "fk_prodotto_categoria")
			)
	private Categoria categoria;

	@Column(length = 150, nullable = false)
	private String nome;

	// TEXT invece di VARCHAR: la descrizione puo' essere lunga quanto serve, nessun limite di caratteri
	@Column(columnDefinition = "TEXT")
	private String descrizione;

	@Column(length = 80, nullable = false)
	private String marca;

	// Le varianti (gusto/formato/colore/prezzo/stock) di questo prodotto.
	// FetchType.EAGER: al contrario della lista prodotti dentro Categoria, qui il caricamento e' immediato —
	// una pagina prodotto ha SEMPRE bisogno delle sue varianti per mostrare prezzo e disponibilita',
	// quindi non ha senso rimandarne il caricamento.
	@OneToMany(mappedBy = "prodotto", fetch = FetchType.EAGER)
	private List<VarianteProdotto> varianti;

	// Le recensioni di questo prodotto: LAZY perche' non servono ogni volta che si carica il prodotto,
	// solo quando l'utente apre esplicitamente la sezione recensioni.
	@OneToMany(mappedBy = "prodotto", fetch = FetchType.LAZY)
	private List<Recensione> recensioni;

}
