package com.java.ecoaula.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.ecoaula.dto.CategoryVolumeDTO;
import com.java.ecoaula.dto.UpdateFillDTO;
import com.java.ecoaula.entity.Category;
import com.java.ecoaula.entity.Container;
import com.java.ecoaula.entity.State;
import com.java.ecoaula.exception.ContainerNotFoundException;
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

    @Test
    void get_status_whenContainerDoesNotExist_returns404() throws Exception {
        when(containerService.getById(999)).thenThrow(new ContainerNotFoundException(999));

        mvc.perform(get("/api/v1/containers/999/status"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.path").value("/api/v1/containers/999/status"));

        verify(containerService, times(1)).getById(999);
        verifyNoMoreInteractions(containerService);
    }

    @Test
    void put_fill_whenPercentageInvalid_returns400() throws Exception {
        UpdateFillDTO dto = new UpdateFillDTO(101f);

        doThrow(new IllegalArgumentException("El porcentaje debe estar entre 0 y 100"))
                .when(containerService).updateFillPercentage(1, 101f);

        mvc.perform(put("/api/v1/containers/1/fill")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("El porcentaje debe estar entre 0 y 100"));

        verify(containerService, times(1)).updateFillPercentage(1, 101f);
        verifyNoMoreInteractions(containerService);
    }

    @Test
    void get_volumeByCategory_returnsOk() throws Exception {
        CategoryVolumeDTO dto = new CategoryVolumeDTO(Category.GLASS, 23.0);
        when(containerService.getVolumeByCategory()).thenReturn(java.util.List.of(dto));

        mvc.perform(get("/api/v1/containers/volume-by-category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("GLASS"))
                .andExpect(jsonPath("$[0].totalWeight").value(23.0));

        verify(containerService, times(1)).getVolumeByCategory();
        verifyNoMoreInteractions(containerService);
    }

    @Test
    void get_volumeByCategory_whenEmpty_returns204() throws Exception {
        when(containerService.getVolumeByCategory()).thenReturn(java.util.List.of());

        mvc.perform(get("/api/v1/containers/volume-by-category"))
                .andExpect(status().isNoContent());

        verify(containerService, times(1)).getVolumeByCategory();
        verifyNoMoreInteractions(containerService);
    }

    @Test
    void options_preflight_withAllowedOrigin_returnsCorsHeaders() throws Exception {
        mvc.perform(options("/api/v1/containers/10/status")
                        .header("Origin", "http://localhost:5173")
                        .header("Access-Control-Request-Method", "GET"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:5173"));

        verifyNoInteractions(containerService);
    }
}
