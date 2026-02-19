package com.java.ecoaula.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.ecoaula.entity.User;
import com.java.ecoaula.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    void createUser_returns201AndBody() throws Exception {
        User input = new User();
        input.setName("David");
        input.setEmail("david@mail.com");
        input.setPassword("1234");

        User saved = new User();
        saved.setId(1);
        saved.setName("David");
        saved.setEmail("david@mail.com");
        saved.setPassword("1234");

        when(userService.createUser(any(User.class))).thenReturn(saved);

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("David"))
                .andExpect(jsonPath("$.email").value("david@mail.com"));

        verify(userService).createUser(any(User.class));
    }

    @Test
    void getUserById_returns200AndBody() throws Exception {
        User user = new User();
        user.setId(7);
        user.setName("Jenny");
        user.setEmail("jenny@mail.com");
        user.setPassword("x");

        when(userService.getUserById(7)).thenReturn(user);

        mockMvc.perform(get("/api/v1/users/7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.name").value("Jenny"))
                .andExpect(jsonPath("$.email").value("jenny@mail.com"));

        verify(userService).getUserById(7);
    }

    @Test
    void updateUser_returns200AndBody() throws Exception {
        User input = new User();
        input.setName("Nuevo");
        input.setEmail("nuevo@mail.com");
        input.setPassword("pass");

        User updated = new User();
        updated.setId(3);
        updated.setName("Nuevo");
        updated.setEmail("nuevo@mail.com");
        updated.setPassword("pass");

        when(userService.updateUser(eq(3), any(User.class))).thenReturn(updated);

        mockMvc.perform(put("/api/v1/users/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("Nuevo"))
                .andExpect(jsonPath("$.email").value("nuevo@mail.com"));

        verify(userService).updateUser(eq(3), any(User.class));
    }

    @Test
    void deleteUser_returns204() throws Exception {
        doNothing().when(userService).deleteUser(9);

        mockMvc.perform(delete("/api/v1/users/9"))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(9);
    }

    @Test
    void createUser_withInvalidJson_returns400() throws Exception {
        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_withInvalidJson_returns400() throws Exception {
        mockMvc.perform(put("/api/v1/users/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{"))
                .andExpect(status().isBadRequest());
    }
}
