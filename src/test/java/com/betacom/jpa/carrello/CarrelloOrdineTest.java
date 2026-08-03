package com.betacom.jpa.carrello;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
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
import com.betacom.jpa.dto.input.CouponReq;
import com.betacom.jpa.dto.input.DettaglioCarrelloReq;
import com.betacom.jpa.dto.input.IndirizzoReq;
import com.betacom.jpa.dto.input.LoginReq;
import com.betacom.jpa.dto.input.OrdineReq;
import com.betacom.jpa.dto.input.UtenteReq;
import com.betacom.jpa.dto.output.AuthResponseDTO;
import com.betacom.jpa.dto.output.CarrelloDTO;
import com.betacom.jpa.dto.output.IndirizzoDTO;
import com.betacom.jpa.dto.output.OrdineDTO;
import com.betacom.jpa.dto.output.UtenteDTO;
import com.betacom.jpa.enums.Roles;
import com.betacom.jpa.models.Prodotto;
import com.betacom.jpa.models.Utente;
import com.betacom.jpa.models.VarianteProdotto;
import com.betacom.jpa.repositories.IProdottoRepository;
import com.betacom.jpa.repositories.IUtenteRepository;
import com.betacom.jpa.repositories.IVarianteProdottoRepository;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

// Proprietario: Pier e Valerio
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CarrelloOrdineTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private IUtenteRepository utenteRepository;

	@Autowired
	private IProdottoRepository prodottoRepository;

	@Autowired
	private IVarianteProdottoRepository varianteRepository;

	private static String clienteToken;
	private static Integer clienteId;

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
		clienteId = dto.getUtente().getIdUtente();
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
		// WELCOME10 e' un 10% sul totale prodotti: si verifica la relazione invece di un importo
		// fisso, per non dipendere dai prezzi esatti dei prodotti di test
		BigDecimal scontoAtteso = dto.getTotaleProdotti().multiply(new BigDecimal("0.10")).setScale(2, RoundingMode.HALF_UP);
		assertEquals(0, dto.getValoreSconto().compareTo(scontoAtteso));
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

	@Test
	@Order(10)
	public void addEUpdateItemCarrelloTest() throws Exception {
		log.debug("addEUpdateItemCarrelloTest");

		DettaglioCarrelloReq addReq = new DettaglioCarrelloReq();
		addReq.setIdVariante(1);
		addReq.setQuantita(1);

		mockMvc.perform(post("/rest/carrello/items")
				.header("Authorization", clienteToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(addReq)))
				.andExpect(status().isOk());

		DettaglioCarrelloReq updateReq = new DettaglioCarrelloReq();
		updateReq.setIdVariante(1);
		updateReq.setQuantita(3);

		mockMvc.perform(patch("/rest/carrello/items")
				.header("Authorization", clienteToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateReq)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.msg").exists());

		MvcResult result = mockMvc.perform(get("/rest/carrello")
				.header("Authorization", clienteToken))
				.andExpect(status().isOk())
				.andReturn();

		CarrelloDTO dto = objectMapper.readValue(result.getResponse().getContentAsString(), CarrelloDTO.class);
		assertEquals(1, dto.getRighe().size());
		assertEquals(3, dto.getRighe().get(0).getQuantita());
	}

	@Test
	@Order(11)
	public void updateItemCarrelloTestErrorNonEsiste() throws Exception {
		log.debug("updateItemCarrelloTestErrorNonEsiste");

		DettaglioCarrelloReq req = new DettaglioCarrelloReq();
		req.setIdVariante(9999);
		req.setQuantita(1);

		mockMvc.perform(patch("/rest/carrello/items")
				.header("Authorization", clienteToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.msg").exists());
	}

	@Test
	@Order(12)
	public void removeItemCarrelloTestErrorNonEsiste() throws Exception {
		log.debug("removeItemCarrelloTestErrorNonEsiste");

		mockMvc.perform(delete("/rest/carrello/items/9999")
				.header("Authorization", clienteToken))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.msg").exists());
	}

	@Test
	@Order(13)
	public void removeCouponTest() throws Exception {
		log.debug("removeCouponTest - WELCOME10 e' gia' stato usato dal cliente in checkoutTest, quindi si crea un coupon nuovo per testare l'apply/remove");

		String adminToken = registraLoggaEPromuoviAdmin("admin.removecoupon@test.it");

		CouponReq creaReq = new CouponReq();
		creaReq.setCodice("REMOVETEST");
		creaReq.setTipologia("FISSO");
		creaReq.setValore(new BigDecimal("5.00"));
		creaReq.setDataInizio(LocalDateTime.of(2020, 1, 1, 0, 0));
		creaReq.setDataFine(LocalDateTime.of(2030, 1, 1, 0, 0));

		mockMvc.perform(post("/rest/coupon/create")
				.header("Authorization", adminToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(creaReq)))
				.andExpect(status().isOk());

		CarrelloReq req = new CarrelloReq();
		req.setCodiceCoupon("REMOVETEST");

		mockMvc.perform(post("/rest/carrello/coupon")
				.header("Authorization", clienteToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isOk());

		mockMvc.perform(delete("/rest/carrello/coupon")
				.header("Authorization", clienteToken))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.msg").exists());

		MvcResult result = mockMvc.perform(get("/rest/carrello")
				.header("Authorization", clienteToken))
				.andExpect(status().isOk())
				.andReturn();

		CarrelloDTO dto = objectMapper.readValue(result.getResponse().getContentAsString(), CarrelloDTO.class);
		assertEquals(null, dto.getCoupon());
	}

	@Test
	@Order(14)
	public void removeItemCarrelloTest() throws Exception {
		log.debug("removeItemCarrelloTest");

		mockMvc.perform(delete("/rest/carrello/items/1")
				.header("Authorization", clienteToken))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.msg").exists());
	}

	@Test
	@Order(15)
	public void clearCarrelloTest() throws Exception {
		log.debug("clearCarrelloTest");

		DettaglioCarrelloReq req = new DettaglioCarrelloReq();
		req.setIdVariante(1);
		req.setQuantita(1);

		mockMvc.perform(post("/rest/carrello/items")
				.header("Authorization", clienteToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isOk());

		mockMvc.perform(delete("/rest/carrello/clear")
				.header("Authorization", clienteToken))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.msg").exists());

		MvcResult result = mockMvc.perform(get("/rest/carrello")
				.header("Authorization", clienteToken))
				.andExpect(status().isOk())
				.andReturn();

		CarrelloDTO dto = objectMapper.readValue(result.getResponse().getContentAsString(), CarrelloDTO.class);
		assertTrue(dto.getRighe().isEmpty());
	}

	@Test
	@Order(16)
	public void checkoutTestErrorStockInsufficiente() throws Exception {
		log.debug("checkoutTestErrorStockInsufficiente");

		Prodotto prodotto = prodottoRepository.findById(1).orElseThrow();

		VarianteProdotto scarsa = new VarianteProdotto();
		scarsa.setProdotto(prodotto);
		scarsa.setGusto("Scorta Limitata");
		scarsa.setPrezzo(new BigDecimal("10.00"));
		scarsa.setQuantitaDisponibile(1);
		scarsa = varianteRepository.save(scarsa);

		DettaglioCarrelloReq addReq = new DettaglioCarrelloReq();
		addReq.setIdVariante(scarsa.getIdVariante());
		addReq.setQuantita(5);

		mockMvc.perform(post("/rest/carrello/items")
				.header("Authorization", clienteToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(addReq)))
				.andExpect(status().isOk());

		OrdineReq checkoutReq = new OrdineReq();
		checkoutReq.setIdIndirizzo(1);
		checkoutReq.setMetodoPagamento("CARTA");

		mockMvc.perform(post("/rest/ordine/checkout")
				.header("Authorization", clienteToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(checkoutReq)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.msg").exists());

		mockMvc.perform(delete("/rest/carrello/clear")
				.header("Authorization", clienteToken))
				.andExpect(status().isOk());
	}

	@Test
	@Order(17)
	public void getOrdineByIdForbiddenTest() throws Exception {
		log.debug("getOrdineByIdForbiddenTest - un altro cliente non vede l'ordine altrui");

		UtenteReq req = new UtenteReq();
		req.setNome("Altro");
		req.setCognome("Cliente");
		req.setEmail("altro.cliente@test.it");
		req.setPassword("password123");

		MvcResult result = mockMvc.perform(post("/rest/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isOk())
				.andReturn();

		AuthResponseDTO dto = objectMapper.readValue(result.getResponse().getContentAsString(), AuthResponseDTO.class);
		String altroToken = "Bearer " + dto.getToken();

		mockMvc.perform(get("/rest/ordine/getById")
				.param("id", "1")
				.header("Authorization", altroToken))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.msg").exists());
	}

	@Test
	@Order(18)
	public void updateOrdineStatoTest() throws Exception {
		log.debug("updateOrdineStatoTest");

		String adminToken = registraLoggaEPromuoviAdmin("admin.ordine@test.it");

		OrdineReq req = new OrdineReq();
		req.setId(1);
		req.setStato("SPEDITO");

		mockMvc.perform(patch("/rest/ordine/updateStato")
				.header("Authorization", adminToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.msg").exists());

		OrdineReq reqPagamento = new OrdineReq();
		reqPagamento.setId(1);
		reqPagamento.setStatoPagamento("APPROVATO");

		mockMvc.perform(patch("/rest/ordine/updateStatoPagamento")
				.header("Authorization", adminToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(reqPagamento)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.msg").exists());

		mockMvc.perform(get("/rest/ordine/list")
				.param("idUtente", clienteId.toString())
				.header("Authorization", adminToken))
				.andExpect(status().isOk());
	}

	private String registraLoggaEPromuoviAdmin(String email) throws Exception {
		UtenteReq req = new UtenteReq();
		req.setNome("Admin");
		req.setCognome("Test");
		req.setEmail(email);
		req.setPassword("password123");

		mockMvc.perform(post("/rest/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isOk());

		Utente admin = utenteRepository.findByEmail(email).orElseThrow();
		admin.setRuolo(Roles.ADMIN);
		utenteRepository.save(admin);

		LoginReq login = new LoginReq();
		login.setEmail(email);
		login.setPassword("password123");

		MvcResult result = mockMvc.perform(post("/rest/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(login)))
				.andExpect(status().isOk())
				.andReturn();

		AuthResponseDTO dto = objectMapper.readValue(result.getResponse().getContentAsString(), AuthResponseDTO.class);
		return "Bearer " + dto.getToken();
	}

	@Test
	@Order(19)
	public void meUtenteTest() throws Exception {
		log.debug("meUtenteTest");

		MvcResult result = mockMvc.perform(get("/rest/utente/me")
				.header("Authorization", clienteToken))
				.andExpect(status().isOk())
				.andReturn();

		UtenteDTO dto = objectMapper.readValue(result.getResponse().getContentAsString(), UtenteDTO.class);
		assertEquals(clienteId, dto.getIdUtente());
	}

	@Test
	@Order(20)
	public void updateUtenteTest() throws Exception {
		log.debug("updateUtenteTest");

		UtenteReq req = new UtenteReq();
		req.setId(clienteId);
		req.setTelefono("3331234567");

		mockMvc.perform(patch("/rest/utente/update")
				.header("Authorization", clienteToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.msg").exists());
	}

	@Test
	@Order(21)
	public void deleteUtenteTestForbidden() throws Exception {
		log.debug("deleteUtenteTestForbidden - un cliente non puo' cancellare un altro utente");

		mockMvc.perform(delete("/rest/utente/delete/9999")
				.header("Authorization", clienteToken))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.msg").exists());
	}

	@Test
	@Order(22)
	public void listUtentiTestForbidden() throws Exception {
		log.debug("listUtentiTestForbidden - solo ADMIN");

		mockMvc.perform(get("/rest/utente/list")
				.header("Authorization", clienteToken))
				.andExpect(status().isForbidden());
	}

	@Test
	@Order(23)
	public void getByIdIndirizzoTest() throws Exception {
		log.debug("getByIdIndirizzoTest");

		MvcResult result = mockMvc.perform(get("/rest/indirizzo/getById")
				.param("id", "1")
				.header("Authorization", clienteToken))
				.andExpect(status().isOk())
				.andReturn();

		IndirizzoDTO dto = objectMapper.readValue(result.getResponse().getContentAsString(), IndirizzoDTO.class);
		log.debug("indirizzo: {}", dto);
	}

	@Test
	@Order(24)
	public void updateEDeleteIndirizzoTest() throws Exception {
		log.debug("updateEDeleteIndirizzoTest");

		IndirizzoReq updateReq = new IndirizzoReq();
		updateReq.setId(1);
		updateReq.setCitta("Torino");

		mockMvc.perform(patch("/rest/indirizzo/update")
				.header("Authorization", clienteToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateReq)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.msg").exists());

		mockMvc.perform(delete("/rest/indirizzo/delete/1")
				.header("Authorization", clienteToken))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.msg").exists());
	}

}
