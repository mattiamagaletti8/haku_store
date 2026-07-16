package com.betacom.jpa.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// ============================================================================
// PROPRIETARIO: Mattia — Modulo Catalogo (Categoria / Prodotto / VarianteProdotto)
// ============================================================================
// DTO di INPUT: cio' che il frontend manda quando crea o modifica una categoria.
// Non e' l'entity Categoria: e' scollegato da JPA, esiste solo per validare i dati in arrivo.
@Setter
@Getter
@ToString
public class CategoriaReq {
	// Obbligatorio SOLO quando si aggiorna (gruppo Update) — serve a sapere quale riga toccare.
	// In creazione l'id non esiste ancora, quindi non e' richiesto (gruppo Create non lo controlla).
	@NotNull(groups = ValidationGroups.Update.class, message = "categoria.no.id")
	private Integer id;

	// Obbligatorio SOLO in creazione (gruppo Create): niente categoria senza nome quando la crei,
	// ma in un update parziale puoi anche non passarlo (resta quello gia' salvato).
	@NotNull(groups = ValidationGroups.Create.class, message = "categoria.no.nome")
	@NotBlank(groups = ValidationGroups.Create.class, message = "categoria.no.nome")
	private String nome;
}
