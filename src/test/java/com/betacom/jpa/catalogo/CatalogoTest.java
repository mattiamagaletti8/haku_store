package com.betacom.jpa.catalogo;

import static org.junit.jupiter.api.Assertions.assertFalse;
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

import com.betacom.jpa.dto.input.CategoriaReq;
import com.betacom.jpa.dto.input.LoginReq;
import com.betacom.jpa.dto.input.ProdottoReq;
import com.betacom.jpa.dto.input.UtenteReq;
import com.betacom.jpa.dto.input.VarianteProdottoReq;
import com.betacom.jpa.dto.output.AuthResponseDTO;
import com.betacom.jpa.dto.output.ProdottoDTO;
import com.betacom.jpa.enums.Roles;
import com.betacom.jpa.models.Utente;
import com.betacom.jpa.repositories.IUtenteRepository;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * Esercita categoria -> prodotto -> variante (in quest'ordine, dato che sono in cascata).
 * L'utente ADMIN non puo' essere creato via API (per scelta, va promosso a mano via SQL in
 * produzione): qui lo si promuove direttamente via repository, solo per i test.
 */
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CatalogoTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private IUtenteRepository utenteRepository;

	private static String adminToken;

	@Test
	@Order(1)
	public void loginAdminTest() throws Exception {
		log.debug("loginAdminTest - registra e promuove un utente ADMIN");

		UtenteReq req = new UtenteReq();
		req.setNome("Admin");
		req.setCognome("Catalogo");
		req.setEmail("admin.catalogo@test.it");
		req.setPassword("password123");

		mockMvc.perform(post("/rest/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isOk());

		Utente admin = utenteRepository.findByEmail("admin.catalogo@test.it").orElseThrow();
		admin.setRuolo(Roles.ADMIN);
		utenteRepository.save(admin);

		LoginReq login = new LoginReq();
		login.setEmail("admin.catalogo@test.it");
		login.setPassword("password123");

		MvcResult result = mockMvc.perform(post("/rest/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(login)))
				.andExpect(status().isOk())
				.andReturn();

		AuthResponseDTO dto = objectMapper.readValue(result.getResponse().getContentAsString(), AuthResponseDTO.class);
		adminToken = "Bearer " + dto.getToken();
	}

	@Test
	@Order(2)
	public void createCategoriaTest() throws Exception {
		log.debug("createCategoriaTest");

		CategoriaReq req = new CategoriaReq();
		req.setNome("Integratori");

		mockMvc.perform(post("/rest/categoria/create")
				.header("Authorization", adminToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.msg").exists());
	}

	@Test
	@Order(3)
	public void createCategoriaTestError() throws Exception {
		log.debug("createCategoriaTestError - nome duplicato");

		CategoriaReq req = new CategoriaReq();
		req.setNome("Integratori");

		mockMvc.perform(post("/rest/categoria/create")
				.header("Authorization", adminToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.msg").exists());
	}

	@Test
	@Order(4)
	public void createCategoriaTestUnauthorized() throws Exception {
		log.debug("createCategoriaTestUnauthorized - nessun token");

		CategoriaReq req = new CategoriaReq();
		req.setNome("Altra categoria");

		mockMvc.perform(post("/rest/categoria/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@Order(5)
	public void createProdottoTest() throws Exception {
		log.debug("createProdottoTest");

		ProdottoReq req = new ProdottoReq();
		req.setIdCategoria(1);
		req.setNome("Proteine Whey");
		req.setMarca("Hakustore");
		req.setDescrizione("Proteine in polvere gusto cioccolato");

		mockMvc.perform(post("/rest/prodotto/create")
				.header("Authorization", adminToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.msg").exists());
	}

	@Test
	@Order(6)
	public void createProdottoTestError() throws Exception {
		log.debug("createProdottoTestError - categoria inesistente");

		ProdottoReq req = new ProdottoReq();
		req.setIdCategoria(9999);
		req.setNome("Prodotto fantasma");
		req.setMarca("Hakustore");

		mockMvc.perform(post("/rest/prodotto/create")
				.header("Authorization", adminToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.msg").exists());
	}

	@Test
	@Order(7)
	public void createVarianteTest() throws Exception {
		log.debug("createVarianteTest");

		VarianteProdottoReq req = new VarianteProdottoReq();
		req.setIdProdotto(1);
		req.setGusto("Cioccolato");
		req.setFormato("1kg");
		req.setPrezzo(new BigDecimal("29.90"));
		req.setQuantitaDisponibile(10);

		mockMvc.perform(post("/rest/varianteProdotto/create")
				.header("Authorization", adminToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.msg").exists());
	}

	@Test
	@Order(8)
	public void createVarianteTestError() throws Exception {
		log.debug("createVarianteTestError - prodotto inesistente");

		VarianteProdottoReq req = new VarianteProdottoReq();
		req.setIdProdotto(9999);
		req.setPrezzo(new BigDecimal("9.90"));

		mockMvc.perform(post("/rest/varianteProdotto/create")
				.header("Authorization", adminToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.msg").exists());
	}

	@Test
	@Order(9)
	public void listProdottiTest() throws Exception {
		log.debug("listProdottiTest");

		MvcResult result = mockMvc.perform(get("/rest/prodotto/list"))
				.andExpect(status().isOk())
				.andReturn();

		String json = result.getResponse().getContentAsString();
		List<ProdottoDTO> lista = objectMapper.readValue(json, new TypeReference<List<ProdottoDTO>>() {});

		assertFalse(lista.isEmpty());
		lista.forEach(p -> log.debug(p.toString()));
	}

}
