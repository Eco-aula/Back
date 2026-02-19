package com.java.ecoaula.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.ecoaula.dto.UpdateFillDTO;
import com.java.ecoaula.entity.Container;
import com.java.ecoaula.entity.State;
import com.java.ecoaula.service.ContainerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContainerController.class)
class ContainerControllerWebMvcTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockitoBean
    ContainerService containerService;

    @Test
    void put_fill_callsService() throws Exception {
        UpdateFillDTO dto = new UpdateFillDTO(75f);

        mvc.perform(put("/api/v1/containers/1/fill")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(containerService, times(1)).updateFillPercentage(1, 75f);
        verifyNoMoreInteractions(containerService);
    }

    @Test
    void get_status_returnsDto() throws Exception {
        Container c = new Container();
        c.setId(10);
        c.setFillPercentage(70f);
        c.setState(State.LIMIT);

        when(containerService.getById(10)).thenReturn(c);

        mvc.perform(get("/api/v1/containers/10/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.fillPercentage").value(70.0))
                .andExpect(jsonPath("$.state").value("LIMIT"));

        verify(containerService, times(1)).getById(10);
        verifyNoMoreInteractions(containerService);
    }
}
