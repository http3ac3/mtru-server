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

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class EquipmentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EquipmentController equipmentController;
    @Autowired
    private AuthController authController;

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    void getAll_withNoExistInventoryNumber_returnEmptyArray() throws Exception {
        this.mockMvc.perform(get("/api/v1/equipment")
                        .param("inventoryNumber", "1234567890")
                )
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    void getAll_withExistingInventoryNumber_returnArrayWithInventoryNumberStartingWith100() throws Exception {
        this.mockMvc.perform(get("/api/v1/equipment")
                        .param("inventoryNumber", "100")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("[*].inventoryNumber", everyItem(startsWith("100"))));
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    void getAll_withInitialCostFromFilter_returnEquipmentWithMoreCost() throws Exception {
        this.mockMvc.perform(get("/api/v1/equipment")
                        .param("initialCostFrom", "15000")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("[*].initialCost", everyItem(greaterThan(15000d))));
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    void create_withFullForm_returnCreated() throws Exception {
        User user = getAdminUserDetails();
        this.mockMvc.perform(post("/api/v1/equipment")
                        .with(user(user))
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .param("inventoryNumber", "2002002020")
                .param("name", "Принтер Kyocera Ecosys PA2001")
                .param("initialCost", "10000")
                .param("commissioningDate", "2024-01-01")
                .param("commissioningActNumber", "АМТС-30п")
                .param("responsibleId", "4")
                .param("placementId", "1")
                .param("subcategoryId", "3")
                .param("description", "TEST")
        ).andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    void create_withBadForm_returnBadRequest() throws Exception {
        User user = getAdminUserDetails();
        this.mockMvc.perform(post("/api/v1/equipment")
                .with(user(user))
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .param("inventoryNumber", "2002002021")
                .param("name", "Принтер Kyocera Ecosys PA2001")
                .param("initialCost", "привет")
                .param("commissioningDate", "2024-01-01")
                .param("commissioningActNumber", "АМТС-30п")
                .param("responsibleId", "4")
                .param("placementId", "1")
                .param("subcategoryId", "3")
                .param("description", "TEST")
        ).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    void create_withNotFullForm_returnBadRequest() throws Exception {
        User user = getAdminUserDetails();
        this.mockMvc.perform(post("/api/v1/equipment")
                .with(user(user))
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .param("inventoryNumber", "2002002021")
                .param("name", "Принтер Kyocera Ecosys PA2001")
                .param("commissioningActNumber", "АМТС-30п")
                .param("responsibleId", "4")
                .param("placementId", "1")
                .param("subcategoryId", "3")
                .param("description", "TEST")
        ).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    void delete_withNotExistingId_returnNotFound() throws Exception {
        User user = getAdminUserDetails();
        this.mockMvc.perform(delete("/api/v1/equipment/-1")
                        .with(user(user))
        ).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    void update_withCorrectFullForm_returnOk() throws Exception {
        User user = getAdminUserDetails();
        this.mockMvc.perform(put("/api/v1/equipment")
                    .with(user(user))
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                    .param("id", "88")
                    .param("initialCost", "10000")
                    .param("inventoryNumber", "7008009234")
                    .param("name", "Ноутбук HP br572")
                    .param("commissioningDate", "2024-01-01")
                    .param("commissioningActNumber", "АМТС-30п")
                    .param("description", "PUT TEST")
                    .param("responsibleId", "9")
                    .param("placementId", "3")
                    .param("subcategoryId", "16"))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    void update_withIncorrectId_returnNotFound() throws Exception {
        User user = getAdminUserDetails();
        this.mockMvc.perform(put("/api/v1/equipment")
                    .with(user(user))
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                    .param("id", "-1")
                    .param("initialCost", "10000")
                    .param("inventoryNumber", "7008009234")
                    .param("name", "Ноутбук HP br572")
                    .param("commissioningDate", "2024-01-01")
                    .param("commissioningActNumber", "АМТС-30п")
                    .param("description", "PUT TEST")
                    .param("responsibleId", "9")
                    .param("placementId", "3")
                    .param("subcategoryId", "16"))
                .andDo(print())
                .andExpect(status().isOk());
    }


    private User getAdminUserDetails() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("admin");
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        user.setRoles(List.of(role));
        return user;
    }
}
