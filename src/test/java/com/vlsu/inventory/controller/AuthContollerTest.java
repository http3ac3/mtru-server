package com.vlsu.inventory.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthContollerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthController authController;

    @Test
    void signIn_withReliableCredentials_returnOkWithToken() throws Exception {
        this.mockMvc.perform(post("/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"username\" : \"admin\", \"password\" : \"admin\" }"))
                .andExpect(status().isOk())
                .andExpect(content().string(notNullValue()));
    }

    @Test
    void signIn_withNotReliableCredentials_returnForbidden() throws Exception {
        this.mockMvc.perform(post("/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"username\" : \"notadmin\", \"password\" : \"incorrectpassword\" }"))
                .andExpect(status().isForbidden())
                .andExpect(content().string(""));
    }
}
