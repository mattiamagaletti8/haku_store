package com.betacom.jpa.dto.output;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// ============================================================================
// PROPRIETARIO: Valerio — Modulo Ordini (Ordine / DettaglioOrdine / checkout)
// ============================================================================
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrdineDTO {
	private Integer id;
	// Solo l'id dell'utente (non uno UtenteDTO annidato): un ordine nella lista admin non ha bisogno
	// di tutti i dati dell'utente, solo di sapere a chi appartiene
	private Integer idUtente;
	private LocalDateTime dataOrdine;
	// Questi campi rispecchiano ESATTAMENTE le colonne congelate dell'entity Ordine: nessun ricalcolo,
	// a differenza di CarrelloDTO dove gli stessi nomi di campo sono invece calcolati al volo
	private BigDecimal totaleProdotti;
	private BigDecimal valoreSconto;
	private BigDecimal totalePagato;
	private String codiceCouponUsato;
	private String stato;
	private String spedizioneVia;
	private String spedizioneCitta;
	private String spedizioneCap;
	private String spedizioneProvincia;
	private String spedizioneNazione;
	private String metodoPagamento;
	private String statoPagamento;
	private List<DettaglioOrdineDTO> righe;
}
