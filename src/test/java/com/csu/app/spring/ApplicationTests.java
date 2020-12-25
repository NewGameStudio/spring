package com.csu.app.spring;

import com.csu.app.spring.dto.AuthenticationRequestDto;
import com.csu.app.spring.dto.ProductDto;
import com.csu.app.spring.model.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	protected String mapToJson(Object obj) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(obj);
	}

	protected <T> T mapFromJson(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, clazz);
	}

	protected String login(AuthenticationRequestDto dto) throws Exception {
		String json_string = mapToJson(dto);
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.post("/api/auth/login")
						.contentType(APPLICATION_JSON)
						.content(json_string))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

		if(status == 200) {
			return new JSONObject(mvcResult.getResponse().getContentAsString())
					.getString("token");
		}

		return null;
	}


	@Test
	public void getProductsList() throws Exception {
		String uri = "/api/products/all";
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

		String content = mvcResult.getResponse().getContentAsString();
		ProductDto[] products = mapFromJson(content, ProductDto[].class);
		assertTrue(products.length > 0);
	}

	@Test
	public void loginWithCorrectData() throws Exception {

		AuthenticationRequestDto dto = new AuthenticationRequestDto();
		dto.setUsername("test2");
		dto.setPassword("test2");

		login(dto);
	}

	@Test
	public void loginWithIncorrectData() throws Exception {
		String uri = "/api/auth/login";

		AuthenticationRequestDto dto = new AuthenticationRequestDto();
		dto.setUsername("test2");
		dto.setPassword("dasdas");

		String json_string = mapToJson(dto);
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.post(uri)
						.contentType(APPLICATION_JSON)
						.content(json_string))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(403, status);
	}

	@Test
	public void loginAndGetUserProducts() throws Exception {

		AuthenticationRequestDto dto = new AuthenticationRequestDto();
		dto.setUsername("test2");
		dto.setPassword("test2");

		String token = login(dto);

		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/api/user/products")
						.header("Authorization", "Bearer_" + token))
				.andReturn();

		assertEquals(200, mvcResult.getResponse().getStatus());
	}
}
