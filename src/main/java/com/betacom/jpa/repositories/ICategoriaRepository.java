package com.betacom.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.betacom.jpa.models.Categoria;

// ============================================================================
// PROPRIETARIO: Mattia — Modulo Catalogo (Categoria / Prodotto / VarianteProdotto)
// ============================================================================
// Repository Spring Data: estendendo JpaRepository<Categoria, Integer> otteniamo GRATIS
// findAll(), findById(), save(), delete()... senza scrivere una riga di SQL.
@Repository
public interface ICategoriaRepository extends JpaRepository<Categoria, Integer> {
	// Query derivata dal nome del metodo: Spring Data legge "existsByNome" e genera da solo
	// "SELECT COUNT(*) > 0 FROM categoria WHERE nome = ?" — usata in CategoriaImpl per bloccare i duplicati.
	boolean existsByNome(String nome);
}
