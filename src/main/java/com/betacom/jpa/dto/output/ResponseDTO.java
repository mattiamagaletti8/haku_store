package com.betacom.jpa.dto.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Proprietario: Infrastruttura condivisa
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO {
	private String msg;
}
