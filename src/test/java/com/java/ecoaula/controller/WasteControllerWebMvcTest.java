/*package com.java.ecoaula.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.ecoaula.entity.Category;
import com.java.ecoaula.entity.Waste;
import com.java.ecoaula.service.WasteService;
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

@WebMvcTest(WasteController.class)
class WasteControllerWebMvcTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockitoBean
    WasteService wasteService;

    @Test
    void post_wastes_returnsCreated() throws Exception {
        Waste w = new Waste();
        w.setId(1);
        w.setName("Botella");
        w.setDescription("Plastico");
        w.setHeavy(10f);
        w.setCategory(Category.PLASTIC);

        when(wasteService.createWaste(any(Waste.class))).thenReturn(w);

        mvc.perform(post("/api/v1/wastes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(w)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Botella"))
                .andExpect(jsonPath("$.category").value("PLASTIC"));

        verify(wasteService, times(1)).createWaste(any(Waste.class));
        verifyNoMoreInteractions(wasteService);
    }

    @Test
    void put_wastes_returnsOk() throws Exception {
        Waste w = new Waste();
        w.setId(2);
        w.setName("Papel");
        w.setDescription("Papel");
        w.setHeavy(3f);
        w.setCategory(Category.PAPER);

        when(wasteService.updateWaste(eq(2), any(Waste.class))).thenReturn(w);

        mvc.perform(put("/api/v1/wastes/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(w)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.category").value("PAPER"));

        verify(wasteService, times(1)).updateWaste(eq(2), any(Waste.class));
        verifyNoMoreInteractions(wasteService);
    }

    @Test
    void delete_wastes_returnsNoContent() throws Exception {
        doNothing().when(wasteService).deleteWaste(9);

        mvc.perform(delete("/api/v1/wastes/9"))
                .andExpect(status().isNoContent());

        verify(wasteService, times(1)).deleteWaste(9);
        verifyNoMoreInteractions(wasteService);
    }
}*/
