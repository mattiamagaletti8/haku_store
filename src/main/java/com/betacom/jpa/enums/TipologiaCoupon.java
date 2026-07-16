package com.betacom.jpa.enums;

// ============================================================================
// PROPRIETARIO: Pier — Modulo Carrello (Carrello / DettaglioCarrello / Coupon)
// ============================================================================
// Le due modalita' di sconto supportate da Coupon.valore: interpretate in CouponMap.calcolaSconto
public enum TipologiaCoupon {
	// Sconto percentuale sul totale (es. valore=10 -> -10%)
	PERCENTUALE,
	// Sconto fisso in euro, con min(fisso, totale) applicato altrove per non andare mai in negativo
	FISSO
}
