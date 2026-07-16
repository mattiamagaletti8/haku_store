package com.betacom.jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// ============================================================================
// PROPRIETARIO: Infrastruttura condivisa (non appartiene a una sola persona)
// ============================================================================
// Il punto di ingresso dell'intera applicazione Spring Boot
@SpringBootApplication
public class ProjectJpaApplication {

	public static void main(String[] args) {
		// Avvia il contesto Spring: scansiona i package, istanzia tutti i bean (@Service, @Repository,
		// @RestController, @Component...) e apre il server web sulla porta configurata (9090)
		SpringApplication.run(ProjectJpaApplication.class, args);
	}

}
