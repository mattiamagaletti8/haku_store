package com.betacom.jpa.models;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// ============================================================================
// PROPRIETARIO: Mattia — Modulo Catalogo (Categoria / Prodotto / VarianteProdotto)
// ============================================================================
// Genera automaticamente getter/setter per tutti i campi (evita di scriverli a mano)
@Setter
@Getter
// Genera un toString() leggibile, utile nei log
@ToString
// Dice a Hibernate/JPA che questa classe corrisponde a una tabella del database
@Entity
// Nome esatto della tabella collegata: "categoria"
@Table(name = "categoria")
public class Categoria {

	// Chiave primaria della tabella
	@Id
	// L'id viene generato dal database stesso (colonna IDENTITY), non calcolato in Java
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	// Nome della colonna nel database (diverso dal nome del campo Java per convenzione snake_case)
	@Column(name = "id_categoria")
	private Integer idCategoria;

	// Nome della categoria: lunghezza massima 100, obbligatorio, e deve essere unico
	// (l'unicita' e' quello che rende possibile il controllo duplicati in CategoriaImpl.create)
	@Column(length = 100, nullable = false, unique = true)
	private String nome;

	// Collegamento verso tutti i Prodotto che appartengono a questa categoria.
	// "mappedBy = categoria" vuol dire: la relazione e' gestita dal campo "categoria" dentro Prodotto,
	// qui c'e' solo il lato "di sola lettura" della relazione (nessuna colonna aggiuntiva su questa tabella).
	// FetchType.LAZY: la lista di prodotti NON viene caricata automaticamente quando leggo una categoria,
	// solo se qualcuno la richiede esplicitamente (es. CategoriaImpl.delete per controllare se ci sono prodotti collegati).
	@OneToMany(mappedBy = "categoria", fetch = FetchType.LAZY)
	private List<Prodotto> prodotti;

}
