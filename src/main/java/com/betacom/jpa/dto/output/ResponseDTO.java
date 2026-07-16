package com.betacom.jpa.dto.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// ============================================================================
// PROPRIETARIO: Infrastruttura condivisa (non appartiene a una sola persona)
// ============================================================================
// Il wrapper piu' usato di tutto il backend: ogni endpoint di mutazione ("created...", "updated...",
// "deleted...", "coupon applicato...") e ogni errore gestito da ExceptionManager risponde con
// questa stessa identica forma {"msg": "..."} — un solo campo, riusato da tutti gli 11 domini del backend.
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO {
	private String msg;
}
