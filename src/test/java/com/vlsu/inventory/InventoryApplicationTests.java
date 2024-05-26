package com.vlsu.inventory;

import com.vlsu.inventory.controller.AuthController;
import com.vlsu.inventory.controller.EquipmentController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class InventoryApplicationTests {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private EquipmentController equipmentController;
	@Autowired
	private AuthController authController;

	@Test
	void contextLoads() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/auth/sign-in")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{ \"username\" : \"admin\", \"password\" : \"admin\" }"))
				.andExpect(status().isOk());
	}

	@Test
	void hello() throws Exception {
		this.mockMvc.perform(get("/auth/"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("hello!")));
	}

}
