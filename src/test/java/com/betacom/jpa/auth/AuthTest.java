package com.betacom.jpa.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.betacom.jpa.dto.input.LoginReq;
import com.betacom.jpa.dto.input.UtenteReq;
import com.betacom.jpa.dto.output.AuthResponseDTO;
import com.betacom.jpa.dto.output.ResponseDTO;

import tools.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

// Proprietario: Pier e Valerio
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@Order(1)
	public void registerTest() throws Exception {
		log.debug("registerTest");

		UtenteReq req = new UtenteReq();
		req.setNome("Mario");
		req.setCognome("Rossi");
		req.setEmail("mario.auth@test.it");
		req.setPassword("password123");

		MvcResult result = mockMvc.perform(post("/rest/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.token").exists())
				.andReturn();

		String json = result.getResponse().getContentAsString();
		AuthResponseDTO dto = objectMapper.readValue(json, AuthResponseDTO.class);
		log.debug("rc register: {}", dto.getUtente());
	}

	@Test
	@Order(2)
	public void registerTestError() throws Exception {
		log.debug("registerTestError - email gia' registrata");

		UtenteReq req = new UtenteReq();
		req.setNome("Mario");
		req.setCognome("Rossi");
		req.setEmail("mario.auth@test.it");
		req.setPassword("password123");

		MvcResult result = mockMvc.perform(post("/rest/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.msg").exists())
				.andReturn();

		String json = result.getResponse().getContentAsString();
		ResponseDTO dto = objectMapper.readValue(json, ResponseDTO.class);
		log.debug("rc registerError: {}", dto.getMsg());
	}

	@Test
	@Order(3)
	public void loginTest() throws Exception {
		log.debug("loginTest");

		LoginReq req = new LoginReq();
		req.setEmail("mario.auth@test.it");
		req.setPassword("password123");

		MvcResult result = mockMvc.perform(post("/rest/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.token").exists())
				.andReturn();

		String json = result.getResponse().getContentAsString();
		AuthResponseDTO dto = objectMapper.readValue(json, AuthResponseDTO.class);
		log.debug("rc login: {}", dto.getToken());
	}

	@Test
	@Order(4)
	public void loginTestError() throws Exception {
		log.debug("loginTestError - password errata");

		LoginReq req = new LoginReq();
		req.setEmail("mario.auth@test.it");
		req.setPassword("passwordSbagliata");

		MvcResult result = mockMvc.perform(post("/rest/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.msg").exists())
				.andReturn();

		String json = result.getResponse().getContentAsString();
		ResponseDTO dto = objectMapper.readValue(json, ResponseDTO.class);
		log.debug("rc loginError: {}", dto.getMsg());
	}

}
