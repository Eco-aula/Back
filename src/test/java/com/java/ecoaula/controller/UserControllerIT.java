/*package com.java.ecoaula.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.ecoaula.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create_then_getById_end_to_end() throws Exception {
        User user = new User();
        user.setName("David");
        user.setEmail("david.it@ecoaula.com");
        user.setPassword("secret");

        String json = objectMapper.writeValueAsString(user);

        String responseBody = mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("David")))
                .andExpect(jsonPath("$.email", is("david.it@ecoaula.com")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        User created = objectMapper.readValue(responseBody, User.class);

        mockMvc.perform(get("/api/v1/users/{id}", created.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(created.getId())))
                .andExpect(jsonPath("$.name", is("David")))
                .andExpect(jsonPath("$.email", is("david.it@ecoaula.com")));
    }
}*/
