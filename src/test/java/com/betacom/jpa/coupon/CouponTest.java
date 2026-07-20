package com.betacom.jpa.coupon;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

import com.betacom.jpa.dto.input.CouponReq;
import com.betacom.jpa.dto.input.LoginReq;
import com.betacom.jpa.dto.input.UtenteReq;
import com.betacom.jpa.dto.output.AuthResponseDTO;
import com.betacom.jpa.enums.Roles;
import com.betacom.jpa.models.Utente;
import com.betacom.jpa.repositories.IUtenteRepository;

import tools.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

// Proprietario: Pier e Valerio
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CouponTest {

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
		log.debug("loginAdminTest");

		UtenteReq req = new UtenteReq();
		req.setNome("Admin");
		req.setCognome("Coupon");
		req.setEmail("admin.coupon@test.it");
		req.setPassword("password123");

		mockMvc.perform(post("/rest/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isOk());

		Utente admin = utenteRepository.findByEmail("admin.coupon@test.it").orElseThrow();
		admin.setRuolo(Roles.ADMIN);
		utenteRepository.save(admin);

		LoginReq login = new LoginReq();
		login.setEmail("admin.coupon@test.it");
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
	public void createCouponTest() throws Exception {
		log.debug("createCouponTest");

		CouponReq req = new CouponReq();
		req.setCodice("WELCOME10");
		req.setTipologia("PERCENTUALE");
		req.setValore(new BigDecimal("10.00"));
		req.setDataInizio(LocalDateTime.of(2020, 1, 1, 0, 0));
		req.setDataFine(LocalDateTime.of(2030, 1, 1, 0, 0));

		mockMvc.perform(post("/rest/coupon/create")
				.header("Authorization", adminToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.msg").exists());
	}

	@Test
	@Order(3)
	public void createCouponTestErrorCodiceDuplicato() throws Exception {
		log.debug("createCouponTestErrorCodiceDuplicato");

		CouponReq req = new CouponReq();
		req.setCodice("WELCOME10");
		req.setTipologia("FISSO");
		req.setValore(new BigDecimal("5.00"));
		req.setDataInizio(LocalDateTime.of(2020, 1, 1, 0, 0));
		req.setDataFine(LocalDateTime.of(2030, 1, 1, 0, 0));

		mockMvc.perform(post("/rest/coupon/create")
				.header("Authorization", adminToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.msg").exists());
	}

	@Test
	@Order(4)
	public void createCouponTestErrorDateInvalide() throws Exception {
		log.debug("createCouponTestErrorDateInvalide - fine prima di inizio");

		CouponReq req = new CouponReq();
		req.setCodice("BADDATES");
		req.setTipologia("FISSO");
		req.setValore(new BigDecimal("5.00"));
		req.setDataInizio(LocalDateTime.of(2030, 1, 1, 0, 0));
		req.setDataFine(LocalDateTime.of(2020, 1, 1, 0, 0));

		mockMvc.perform(post("/rest/coupon/create")
				.header("Authorization", adminToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.msg").exists());
	}

	@Test
	@Order(5)
	public void listCouponTest() throws Exception {
		log.debug("listCouponTest");

		mockMvc.perform(get("/rest/coupon/list")
				.header("Authorization", adminToken))
				.andExpect(status().isOk());
	}

	@Test
	@Order(6)
	public void getByIdCouponTest() throws Exception {
		log.debug("getByIdCouponTest");

		mockMvc.perform(get("/rest/coupon/getById")
				.param("id", "1")
				.header("Authorization", adminToken))
				.andExpect(status().isOk());
	}

	@Test
	@Order(7)
	public void getByIdCouponTestError() throws Exception {
		log.debug("getByIdCouponTestError - id inesistente");

		mockMvc.perform(get("/rest/coupon/getById")
				.param("id", "9999")
				.header("Authorization", adminToken))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.msg").exists());
	}

	@Test
	@Order(8)
	public void createUpdateEDeleteCouponUsaEGettaTest() throws Exception {
		log.debug("createUpdateEDeleteCouponUsaEGettaTest - non tocca WELCOME10 (id=1), usato da CarrelloOrdineTest");

		CouponReq creaReq = new CouponReq();
		creaReq.setCodice("TEMP5");
		creaReq.setTipologia("FISSO");
		creaReq.setValore(new BigDecimal("5.00"));
		creaReq.setDataInizio(LocalDateTime.of(2020, 1, 1, 0, 0));
		creaReq.setDataFine(LocalDateTime.of(2030, 1, 1, 0, 0));

		mockMvc.perform(post("/rest/coupon/create")
				.header("Authorization", adminToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(creaReq)))
				.andExpect(status().isOk());

		CouponReq updateReq = new CouponReq();
		updateReq.setId(2);
		updateReq.setIsAttivo(false);

		mockMvc.perform(patch("/rest/coupon/update")
				.header("Authorization", adminToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateReq)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.msg").exists());

		mockMvc.perform(delete("/rest/coupon/delete/2")
				.header("Authorization", adminToken))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.msg").exists());
	}

	@Test
	@Order(9)
	public void createCouponTestUnauthorized() throws Exception {
		log.debug("createCouponTestUnauthorized - nessun token");

		CouponReq req = new CouponReq();
		req.setCodice("NOAUTH");
		req.setTipologia("FISSO");
		req.setValore(new BigDecimal("1.00"));
		req.setDataInizio(LocalDateTime.of(2020, 1, 1, 0, 0));
		req.setDataFine(LocalDateTime.of(2030, 1, 1, 0, 0));

		mockMvc.perform(post("/rest/coupon/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isUnauthorized());
	}

}
