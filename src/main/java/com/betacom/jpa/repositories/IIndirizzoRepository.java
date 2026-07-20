package com.betacom.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.betacom.jpa.models.Indirizzo;

// Proprietario: Sarah
@Repository
public interface IIndirizzoRepository extends JpaRepository<Indirizzo, Integer> {
	List<Indirizzo> findByUtenteIdUtente(Integer idUtente);
}
