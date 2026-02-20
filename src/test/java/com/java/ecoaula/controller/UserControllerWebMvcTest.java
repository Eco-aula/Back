package com.java.ecoaula.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.ecoaula.entity.User;
import com.java.ecoaula.exception.ResourceNotFoundException;
import com.java.ecoaula.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerWebMvcTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockitoBean
    UserService userService;

    @Test
    void post_users_returnsCreated() throws Exception {
        User u = new User();
        u.setId(1);
        u.setName("David");
        u.setEmail("david@test.com");
        u.setPassword("1234");

        when(userService.createUser(any(User.class))).thenReturn(u);

        mvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(u)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("David"))
                .andExpect(jsonPath("$.email").value("david@test.com"));

        verify(userService, times(1)).createUser(any(User.class));
        verifyNoMoreInteractions(userService);
    }

    @Test
    void get_userById_returnsOk() throws Exception {
        User u = new User();
        u.setId(7);
        u.setName("Ana");
        u.setEmail("ana@test.com");

        when(userService.getUserById(7)).thenReturn(u);

        mvc.perform(get("/api/v1/users/7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.name").value("Ana"))
                .andExpect(jsonPath("$.email").value("ana@test.com"));

        verify(userService, times(1)).getUserById(7);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void put_user_updatesAndReturnsOk() throws Exception {
        User upd = new User();
        upd.setId(2);
        upd.setName("New");
        upd.setEmail("new@test.com");
        upd.setPassword("456");

        when(userService.updateUser(eq(2), any(User.class))).thenReturn(upd);

        mvc.perform(put("/api/v1/users/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(upd)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("New"))
                .andExpect(jsonPath("$.email").value("new@test.com"));

        verify(userService, times(1)).updateUser(eq(2), any(User.class));
        verifyNoMoreInteractions(userService);
    }

    @Test
    void delete_user_returnsNoContent() throws Exception {
        doNothing().when(userService).deleteUser(5);

        mvc.perform(delete("/api/v1/users/5"))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(5);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void get_createUserPath_returnsTemplateName() throws Exception {
        mvc.perform(get("/api/v1/users/create/99"))
                .andExpect(status().isOk())
                .andExpect(content().string("createUser"));

        verifyNoInteractions(userService);
    }

    @Test
    void get_userById_whenNotFound_returns404() throws Exception {
        when(userService.getUserById(77))
                .thenThrow(new ResourceNotFoundException("Usuario no encontrado"));

        mvc.perform(get("/api/v1/users/77"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Usuario no encontrado"))
                .andExpect(jsonPath("$.path").value("/api/v1/users/77"));

        verify(userService, times(1)).getUserById(77);
        verifyNoMoreInteractions(userService);
    }
}
