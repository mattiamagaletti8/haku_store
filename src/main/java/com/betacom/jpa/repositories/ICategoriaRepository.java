package com.betacom.jpa.repositories;

// import di Spring Data JPA (repository generico) e dell'entity da gestire
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.betacom.jpa.models.Categoria;

// Proprietario: Mattia
@Repository    // dice a Spring che questa è una repository, gestita in automatico
public interface ICategoriaRepository extends JpaRepository<Categoria, Integer> {
	// true se esiste già una categoria con questo nome esatto (Spring Data genera la query dal nome del metodo)
	boolean existsByNome(String nome);
}
