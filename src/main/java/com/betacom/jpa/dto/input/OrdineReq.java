package com.betacom.jpa.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// Proprietario: Valerio
@Setter
@Getter
@ToString
public class OrdineReq {
	@NotNull(groups = ValidationGroups.OrdineStato.class, message = "ordine.no.id")
	private Integer id;

	@NotNull(groups = ValidationGroups.Checkout.class, message = "ordine.no.indirizzo")
	private Integer idIndirizzo;

	@NotNull(groups = ValidationGroups.Checkout.class, message = "ordine.no.metodo.pagamento")
	@NotBlank(groups = ValidationGroups.Checkout.class, message = "ordine.no.metodo.pagamento")
	private String metodoPagamento;

	private String stato;

	private String statoPagamento;
}
