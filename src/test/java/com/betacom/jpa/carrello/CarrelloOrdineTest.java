package com.betacom.jpa.carrello;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.betacom.jpa.dto.input.CarrelloReq;
import com.betacom.jpa.dto.input.DettaglioCarrelloReq;
import com.betacom.jpa.dto.input.IndirizzoReq;
import com.betacom.jpa.dto.input.OrdineReq;
import com.betacom.jpa.dto.input.UtenteReq;
import com.betacom.jpa.dto.output.AuthResponseDTO;
import com.betacom.jpa.dto.output.CarrelloDTO;
import com.betacom.jpa.dto.output.OrdineDTO;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * Flusso completo cliente: registrazione -> indirizzo -> carrello -> coupon -> checkout.
 * Presuppone che CatalogoTest (variante id=1) e CouponTest (codice WELCOME10) siano
 * gia' stati eseguiti nella stessa suite (vedi SuiteClass, contesto Spring condiviso).
 */
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CarrelloOrdineTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private static String clienteToken;

	@Test
	@Order(1)
	public void registerClienteTest() throws Exception {
		log.debug("registerClienteTest");

		UtenteReq req = new UtenteReq();
		req.setNome("Luca");
		req.setCognome("Bianchi");
		req.setEmail("cliente.carrello@test.it");
		req.setPassword("password123");

		MvcResult result = mockMvc.perform(post("/rest/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isOk())
				.andReturn();

		AuthResponseDTO dto = objectMapper.readValue(result.getResponse().getContentAsString(), AuthResponseDTO.class);
		clienteToken = "Bearer " + dto.getToken();
	}

	@Test
	@Order(2)
	public void createIndirizzoTest() throws Exception {
		log.debug("createIndirizzoTest");

		IndirizzoReq req = new IndirizzoReq();
		req.setVia("Via Roma 1");
		req.setCitta("Milano");
		req.setCap("20100");

		mockMvc.perform(post("/rest/indirizzo/create")
				.header("Authorization", clienteToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.msg").exists());
	}

	@Test
	@Order(3)
	public void addItemCarrelloTestError() throws Exception {
		log.debug("addItemCarrelloTestError - variante inesistente");

		DettaglioCarrelloReq req = new DettaglioCarrelloReq();
		req.setIdVariante(9999);
		req.setQuantita(1);

		mockMvc.perform(post("/rest/carrello/items")
				.header("Authorization", clienteToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.msg").exists());
	}

	@Test
	@Order(4)
	public void addItemCarrelloTest() throws Exception {
		log.debug("addItemCarrelloTest");

		DettaglioCarrelloReq req = new DettaglioCarrelloReq();
		req.setIdVariante(1);
		req.setQuantita(2);

		mockMvc.perform(post("/rest/carrello/items")
				.header("Authorization", clienteToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.msg").exists());
	}

	@Test
	@Order(5)
	public void applyCouponTest() throws Exception {
		log.debug("applyCouponTest");

		CarrelloReq req = new CarrelloReq();
		req.setCodiceCoupon("WELCOME10");

		mockMvc.perform(post("/rest/carrello/coupon")
				.header("Authorization", clienteToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.msg").exists());
	}

	@Test
	@Order(6)
	public void getCarrelloTest() throws Exception {
		log.debug("getCarrelloTest");

		MvcResult result = mockMvc.perform(get("/rest/carrello")
				.header("Authorization", clienteToken))
				.andExpect(status().isOk())
				.andReturn();

		CarrelloDTO dto = objectMapper.readValue(result.getResponse().getContentAsString(), CarrelloDTO.class);

		assertNotNull(dto.getCoupon());
		assertFalse(dto.getRighe().isEmpty());
		// 2 x 29.90 = 59.80, sconto 10% = 5.98
		assertEquals(0, dto.getValoreSconto().compareTo(new BigDecimal("5.98")));
		log.debug("carrello: {}", dto);
	}

	@Test
	@Order(7)
	public void checkoutTest() throws Exception {
		log.debug("checkoutTest");

		OrdineReq req = new OrdineReq();
		req.setIdIndirizzo(1);
		req.setMetodoPagamento("CARTA");

		MvcResult result = mockMvc.perform(post("/rest/ordine/checkout")
				.header("Authorization", clienteToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isOk())
				.andReturn();

		OrdineDTO dto = objectMapper.readValue(result.getResponse().getContentAsString(), OrdineDTO.class);

		assertFalse(dto.getRighe().isEmpty());
		assertEquals("IN_ATTESA", dto.getStato());
		log.debug("ordine creato: {}", dto);
	}

	@Test
	@Order(8)
	public void checkoutTestErrorCarrelloVuoto() throws Exception {
		log.debug("checkoutTestErrorCarrelloVuoto - il carrello e' stato svuotato dal checkout precedente");

		OrdineReq req = new OrdineReq();
		req.setIdIndirizzo(1);
		req.setMetodoPagamento("CARTA");

		mockMvc.perform(post("/rest/ordine/checkout")
				.header("Authorization", clienteToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.msg").exists());
	}

	@Test
	@Order(9)
	public void listOrdiniTest() throws Exception {
		log.debug("listOrdiniTest");

		MvcResult result = mockMvc.perform(get("/rest/ordine/list")
				.header("Authorization", clienteToken))
				.andExpect(status().isOk())
				.andReturn();

		String json = result.getResponse().getContentAsString();
		List<OrdineDTO> lista = objectMapper.readValue(json, new TypeReference<List<OrdineDTO>>() {});

		assertFalse(lista.isEmpty());
		lista.forEach(o -> log.debug(o.toString()));
	}

}
