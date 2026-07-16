package com.betacom.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.betacom.jpa.models.VarianteProdotto;

// ============================================================================
// PROPRIETARIO: Mattia — Modulo Catalogo (Categoria / Prodotto / VarianteProdotto)
// ============================================================================
@Repository
public interface IVarianteProdottoRepository extends JpaRepository<VarianteProdotto, Integer> {
	// Query derivata che attraversa la relazione: "prodotto.idProdotto" diventa "ProdottoIdProdotto"
	// nel nome del metodo — trova tutte le varianti di un determinato prodotto
	List<VarianteProdotto> findByProdottoIdProdotto(Integer idProdotto);

	// Controllo duplicati: stessa combinazione prodotto+gusto+formato+colore gia' esistente
	// (usata per impedire due varianti identiche sullo stesso prodotto)
	boolean existsByProdottoIdProdottoAndGustoAndFormatoAndColore(Integer idProdotto, String gusto, String formato, String colore);
}
