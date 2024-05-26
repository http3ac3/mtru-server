package com.vlsu.inventory.controller;

import com.vlsu.inventory.model.Role;
import com.vlsu.inventory.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RentController rentController;

    @Test
    @WithMockUser(username = "rogov-ra", password = "rogov-ra", roles = "USER")
    void create_withCorrectForm_returnOk() throws Exception {
        User user = getDefaultUser();
        this.mockMvc.perform(post("/api/v1/rents")
                .with(user(user))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                        {
                            "description" : "description from test",
                            "equipment" : { 
                                "id" : 88
                            },
                            "placement" : {
                                "id" : 1
                            }
                        }
                        """)
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "rogov-ra", password = "rogov-ra", roles = "USER")
    void create_withIncorrectEquipmentId_returnNotFound() throws Exception {
        User user = getDefaultUser();
        this.mockMvc.perform(post("/api/v1/rents")
                .with(user(user))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                        {
                            "description" : "description from test",
                            "equipment" : { 
                                "id" : -1
                            },
                            "placement" : {
                                "id" : 1
                            }
                        }
                        """)
        ).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "rogov-ra", password = "rogov-ra", roles = "USER")
    void close_withSamePrincipalWhoCreated_returnOk() throws Exception {
        User user = getDefaultUser();
        this.mockMvc.perform(put("/api/v1/rents/39")
                        .with(user(user)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "dimov-aa", password = "dimov-aa", roles = "USER")
    void close_withPrincipalWhoNotCreated_returnLocked() throws Exception {
        User user = getWrongUser();
        this.mockMvc.perform(put("/api/v1/rents/39")
                        .with(user(user)))
                .andDo(print())
                .andExpect(status().isLocked());
    }

    @Test
    @WithMockUser(username = "rogov-ra", password = "rogov-ra", roles = "USER")
    void close_withWrongId_returnNotFound() throws Exception {
        User user = getDefaultUser();
        this.mockMvc.perform(put("/api/v1/rents/-1")
                        .with(user(user)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    private User getWrongUser() {
        User user = new User();
        user.setUsername("dimov-aa");
        user.setPassword("dimov-aa");
        Role role = new Role();
        role.setName("ROLE_USER");
        user.setRoles(List.of(role));
        return user;
    }

    private User getDefaultUser() {
        User user = new User();
        user.setUsername("rogov-ra");
        user.setPassword("rogov-ra");
        Role role = new Role();
        role.setName("ROLE_USER");
        user.setRoles(List.of(role));
        return user;
    }
}
