package com.acm.proyectohotel.controlador;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de integracion de la capa web para el CRUD de Rol y para el manejo
 * de errores 404. Usan el perfil "test" (H2 en memoria).
 */
@SpringBootTest
@ActiveProfiles("test")
class RolControllerTest {

    @Autowired
    private WebApplicationContext contexto;

    private MockMvc mockMvc;

    @BeforeEach
    void configurar() {
        mockMvc = MockMvcBuilders.webAppContextSetup(contexto).build();
    }

    @Test
    void cicloCrudCompleto() throws Exception {
        // Create
        String creado = mockMvc.perform(post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"GERENTE\",\"descripcion\":\"Gestiona la sucursal\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nombre").value("GERENTE"))
                .andReturn().getResponse().getContentAsString();

        int id = JsonPath.parse(creado).read("$.id", Integer.class);

        // Read
        mockMvc.perform(get("/api/roles/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("GERENTE"));

        // Update
        mockMvc.perform(put("/api/roles/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"GERENTE\",\"descripcion\":\"Texto actualizado\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descripcion").value("Texto actualizado"));

        // Delete
        mockMvc.perform(delete("/api/roles/" + id))
                .andExpect(status().isNoContent());

        // Ya no existe
        mockMvc.perform(get("/api/roles/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void obtenerInexistenteDevuelve404ConCuerpoDeError() throws Exception {
        mockMvc.perform(get("/api/roles/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.estado").value(404))
                .andExpect(jsonPath("$.mensaje").exists());
    }
}
