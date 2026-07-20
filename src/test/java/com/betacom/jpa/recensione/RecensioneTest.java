package com.betacom.jpa.recensione;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.betacom.jpa.dto.input.RecensioneReq;
import com.betacom.jpa.dto.input.UtenteReq;
import com.betacom.jpa.dto.output.AuthResponseDTO;
import com.betacom.jpa.dto.output.RecensioneDTO;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

// Proprietario: Pier e Valerio
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RecensioneTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private static String autoreToken;
	private static String altroUtenteToken;

	@Test
	@Order(1)
	public void registerUtentiTest() throws Exception {
		log.debug("registerUtentiTest");

		autoreToken = registraELoggaCliente("autore.recensione@test.it");
		altroUtenteToken = registraELoggaCliente("altro.recensione@test.it");
	}

	private String registraELoggaCliente(String email) throws Exception {
		UtenteReq req = new UtenteReq();
		req.setNome("Test");
		req.setCognome("Recensore");
		req.setEmail(email);
		req.setPassword("password123");

		MvcResult result = mockMvc.perform(post("/rest/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isOk())
				.andReturn();

		AuthResponseDTO dto = objectMapper.readValue(result.getResponse().getContentAsString(), AuthResponseDTO.class);
		return "Bearer " + dto.getToken();
	}

	@Test
	@Order(2)
	public void createRecensioneTest() throws Exception {
		log.debug("createRecensioneTest");

		RecensioneReq req = new RecensioneReq();
		req.setIdProdotto(1);
		req.setVoto(5);
		req.setTitolo("Ottimo prodotto");
		req.setCommento("Consigliato!");

		mockMvc.perform(post("/rest/recensione/create")
				.header("Authorization", autoreToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.msg").exists());
	}

	@Test
	@Order(3)
	public void createRecensioneTestErrorVotoInvalido() throws Exception {
		log.debug("createRecensioneTestErrorVotoInvalido");

		RecensioneReq req = new RecensioneReq();
		req.setIdProdotto(1);
		req.setVoto(9);

		mockMvc.perform(post("/rest/recensione/create")
				.header("Authorization", autoreToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.msg").exists());
	}

	@Test
	@Order(4)
	public void createRecensioneTestErrorProdottoInesistente() throws Exception {
		log.debug("createRecensioneTestErrorProdottoInesistente");

		RecensioneReq req = new RecensioneReq();
		req.setIdProdotto(9999);
		req.setVoto(4);

		mockMvc.perform(post("/rest/recensione/create")
				.header("Authorization", autoreToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.msg").exists());
	}

	@Test
	@Order(5)
	public void listRecensioniTest() throws Exception {
		log.debug("listRecensioniTest");

		MvcResult result = mockMvc.perform(get("/rest/recensione/list").param("idProdotto", "1"))
				.andExpect(status().isOk())
				.andReturn();

		List<RecensioneDTO> lista = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<RecensioneDTO>>() {});
		assertFalse(lista.isEmpty());
		lista.forEach(r -> log.debug(r.toString()));
	}

	@Test
	@Order(6)
	public void updateRecensioneTestForbidden() throws Exception {
		log.debug("updateRecensioneTestForbidden - un altro utente non puo' modificarla");

		RecensioneReq req = new RecensioneReq();
		req.setId(1);
		req.setVoto(1);

		mockMvc.perform(patch("/rest/recensione/update")
				.header("Authorization", altroUtenteToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.msg").exists());
	}

	@Test
	@Order(7)
	public void updateRecensioneTest() throws Exception {
		log.debug("updateRecensioneTest - l'autore puo' modificarla");

		RecensioneReq req = new RecensioneReq();
		req.setId(1);
		req.setVoto(4);
		req.setCommento("Aggiornato dopo qualche settimana d'uso");

		mockMvc.perform(patch("/rest/recensione/update")
				.header("Authorization", autoreToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.msg").exists());
	}

	@Test
	@Order(8)
	public void deleteRecensioneTestForbidden() throws Exception {
		log.debug("deleteRecensioneTestForbidden");

		mockMvc.perform(delete("/rest/recensione/delete/1")
				.header("Authorization", altroUtenteToken))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.msg").exists());
	}

	@Test
	@Order(9)
	public void deleteRecensioneTest() throws Exception {
		log.debug("deleteRecensioneTest");

		mockMvc.perform(delete("/rest/recensione/delete/1")
				.header("Authorization", autoreToken))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.msg").exists());
	}

}
