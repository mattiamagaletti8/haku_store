package com.betacom.jpa.catalogo;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
import com.betacom.jpa.dto.output.CategoriaDTO;
import com.betacom.jpa.dto.output.ProdottoDTO;
import com.betacom.jpa.dto.output.VarianteProdottoDTO;
import com.betacom.jpa.enums.Roles;
import com.betacom.jpa.models.Utente;
import com.betacom.jpa.repositories.IUtenteRepository;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

// Proprietario: Pier e Valerio
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

	@Test
	@Order(10)
	public void getByIdCategoriaTest() throws Exception {
		log.debug("getByIdCategoriaTest");

		MvcResult result = mockMvc.perform(get("/rest/categoria/getById").param("id", "1"))
				.andExpect(status().isOk())
				.andReturn();

		CategoriaDTO dto = objectMapper.readValue(result.getResponse().getContentAsString(), CategoriaDTO.class);
		log.debug("categoria: {}", dto);
	}

	@Test
	@Order(11)
	public void updateCategoriaTest() throws Exception {
		log.debug("updateCategoriaTest");

		CategoriaReq req = new CategoriaReq();
		req.setId(1);
		req.setNome("Integratori Alimentari");

		mockMvc.perform(patch("/rest/categoria/update")
				.header("Authorization", adminToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.msg").exists());
	}

	@Test
	@Order(12)
	public void deleteCategoriaTestErrorHasProdotti() throws Exception {
		log.debug("deleteCategoriaTestErrorHasProdotti - la categoria 1 ha il prodotto 1 collegato");

		mockMvc.perform(delete("/rest/categoria/delete/1")
				.header("Authorization", adminToken))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.msg").exists());
	}

	@Test
	@Order(13)
	public void createEDeleteCategoriaVuotaTest() throws Exception {
		log.debug("createEDeleteCategoriaVuotaTest - categoria senza prodotti, cancellabile");

		CategoriaReq req = new CategoriaReq();
		req.setNome("Categoria Temporanea");

		mockMvc.perform(post("/rest/categoria/create")
				.header("Authorization", adminToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isOk());

		mockMvc.perform(delete("/rest/categoria/delete/2")
				.header("Authorization", adminToken))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.msg").exists());
	}

	@Test
	@Order(14)
	public void getByIdProdottoTest() throws Exception {
		log.debug("getByIdProdottoTest");

		MvcResult result = mockMvc.perform(get("/rest/prodotto/getById").param("id", "1"))
				.andExpect(status().isOk())
				.andReturn();

		ProdottoDTO dto = objectMapper.readValue(result.getResponse().getContentAsString(), ProdottoDTO.class);
		log.debug("prodotto: {}", dto);
	}

	@Test
	@Order(15)
	public void updateProdottoTest() throws Exception {
		log.debug("updateProdottoTest");

		ProdottoReq req = new ProdottoReq();
		req.setId(1);
		req.setDescrizione("Descrizione aggiornata");

		mockMvc.perform(patch("/rest/prodotto/update")
				.header("Authorization", adminToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.msg").exists());
	}

	@Test
	@Order(16)
	public void findByFilterProdottoTest() throws Exception {
		log.debug("findByFilterProdottoTest");

		mockMvc.perform(get("/rest/prodotto/list")
				.param("idCategoria", "1")
				.param("marca", "Hakustore")
				.param("nome", "Proteine"))
				.andExpect(status().isOk());
	}

	@Test
	@Order(17)
	public void createEDeleteProdottoTest() throws Exception {
		log.debug("createEDeleteProdottoTest - prodotto usa e getta, per non toccare l'id=1 usato dagli altri test");

		ProdottoReq req = new ProdottoReq();
		req.setIdCategoria(1);
		req.setNome("Prodotto temporaneo");
		req.setMarca("Hakustore");

		mockMvc.perform(post("/rest/prodotto/create")
				.header("Authorization", adminToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isOk());

		mockMvc.perform(delete("/rest/prodotto/delete/2")
				.header("Authorization", adminToken))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.msg").exists());
	}

	@Test
	@Order(18)
	public void getByIdVarianteTest() throws Exception {
		log.debug("getByIdVarianteTest");

		MvcResult result = mockMvc.perform(get("/rest/varianteProdotto/getById").param("id", "1"))
				.andExpect(status().isOk())
				.andReturn();

		VarianteProdottoDTO dto = objectMapper.readValue(result.getResponse().getContentAsString(), VarianteProdottoDTO.class);
		log.debug("variante: {}", dto);
	}

	@Test
	@Order(19)
	public void updateVarianteTest() throws Exception {
		log.debug("updateVarianteTest");

		VarianteProdottoReq req = new VarianteProdottoReq();
		req.setId(1);
		req.setPrezzo(new BigDecimal("27.90"));

		mockMvc.perform(patch("/rest/varianteProdotto/update")
				.header("Authorization", adminToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.msg").exists());
	}

	@Test
	@Order(20)
	public void listVarianteByProdottoTest() throws Exception {
		log.debug("listVarianteByProdottoTest");

		MvcResult result = mockMvc.perform(get("/rest/varianteProdotto/list").param("idProdotto", "1"))
				.andExpect(status().isOk())
				.andReturn();

		List<VarianteProdottoDTO> lista = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<VarianteProdottoDTO>>() {});
		assertFalse(lista.isEmpty());
	}

	@Test
	@Order(21)
	public void createEDeleteVarianteTest() throws Exception {
		log.debug("createEDeleteVarianteTest - variante usa e getta, per non toccare l'id=1 usato dal carrello");

		VarianteProdottoReq req = new VarianteProdottoReq();
		req.setIdProdotto(1);
		req.setGusto("Vaniglia");
		req.setPrezzo(new BigDecimal("25.00"));

		mockMvc.perform(post("/rest/varianteProdotto/create")
				.header("Authorization", adminToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isOk());

		mockMvc.perform(delete("/rest/varianteProdotto/delete/2")
				.header("Authorization", adminToken))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.msg").exists());
	}

}
