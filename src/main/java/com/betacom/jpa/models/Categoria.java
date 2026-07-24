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

// Proprietario: Mattia
@Setter
@Getter
@ToString
@Entity
@Table(name = "categoria")
public class Categoria {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_categoria")
	private Integer idCategoria;

	@Column(length = 100, nullable = false, unique = true)
	private String nome;

	@Column(length = 255)
	private String immagine;

	@OneToMany(mappedBy = "categoria", fetch = FetchType.LAZY)
	private List<Prodotto> prodotti;

}
