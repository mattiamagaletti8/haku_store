package com.betacom.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.betacom.jpa.models.Prodotto;

// ============================================================================
// PROPRIETARIO: Mattia — Modulo Catalogo (Categoria / Prodotto / VarianteProdotto)
// ============================================================================
@Repository
public interface IProdottoRepository extends JpaRepository<Prodotto, Integer> {
	// Query nominata definita in jpa-named-queries.properties (non e' una query derivata dal nome del metodo):
	// i tre parametri sono tutti opzionali, la query e' scritta con l'idioma ":param is null or campo = :param"
	// cosi' filtra solo sui campi effettivamente passati dal frontend
	@Query(name = "prodotto.selectByFilter")
	List<Prodotto> searchByFilter(
			@Param("idCategoria") Integer idCategoria,
			@Param("marca") String marca,
			@Param("nome") String nome
			);

	// Query derivata: controlla se esiste gia' un prodotto con stesso nome+marca (case-insensitive),
	// usata per bloccare i duplicati in fase di creazione
	boolean existsByNomeIgnoreCaseAndMarcaIgnoreCase(String nome, String marca);
}
