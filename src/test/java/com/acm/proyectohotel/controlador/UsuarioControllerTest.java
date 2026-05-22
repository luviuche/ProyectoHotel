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
 * Verifica que las entidades expuestas directamente en la API se serializan de
 * forma segura: el password nunca aparece y las claves foraneas se ven como id
 * plano (rolId), sin provocar errores de carga perezosa al leerlas de nuevo.
 */
@SpringBootTest
@ActiveProfiles("test")
class UsuarioControllerTest {

    @Autowired
    private WebApplicationContext contexto;

    private MockMvc mockMvc;

    @BeforeEach
    void configurar() {
        mockMvc = MockMvcBuilders.webAppContextSetup(contexto).build();
    }

    @Test
    void crearUsuarioOcultaPasswordYExponeRolId() throws Exception {
        // Primero un rol al que asociar el usuario.
        String rol = mockMvc.perform(post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"CLIENTE\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        int rolId = JsonPath.parse(rol).read("$.id", Integer.class);

        String cuerpo = "{"
                + "\"rolId\":" + rolId + ","
                + "\"nombre\":\"Ana\","
                + "\"apellido\":\"Perez\","
                + "\"email\":\"ana@correo.com\","
                + "\"password\":\"secreta123\","
                + "\"telefono\":\"3001234567\","
                + "\"documento\":\"CC-100\""
                + "}";

        // Create: password se acepta en la entrada pero no se devuelve.
        String creado = mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpo))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.rolId").value(rolId))
                .andExpect(jsonPath("$.email").value("ana@correo.com"))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andReturn().getResponse().getContentAsString();
        int usuarioId = JsonPath.parse(creado).read("$.id", Integer.class);

        // Read: al releer, "rol" es un proxy perezoso; getRolId() no lo inicializa.
        mockMvc.perform(get("/api/usuarios/" + usuarioId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rolId").value(rolId))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void emailDuplicadoDevuelve400() throws Exception {
        String rol = mockMvc.perform(post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"RECEPCION\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        int rolId = JsonPath.parse(rol).read("$.id", Integer.class);

        String cuerpo = "{"
                + "\"rolId\":" + rolId + ","
                + "\"nombre\":\"Luis\",\"apellido\":\"Gomez\","
                + "\"email\":\"luis@correo.com\",\"password\":\"x\","
                + "\"telefono\":\"300\",\"documento\":\"CC-200\""
                + "}";

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON).content(cuerpo))
                .andExpect(status().isCreated());

        // Mismo email, distinto documento -> regla de negocio (400).
        String duplicado = cuerpo.replace("CC-200", "CC-201");
        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON).content(duplicado))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.estado").value(400));
    }

    @Test
    void rolInexistenteDevuelve404() throws Exception {
        String cuerpo = "{"
                + "\"rolId\":888888,"
                + "\"nombre\":\"Sin\",\"apellido\":\"Rol\","
                + "\"email\":\"sinrol@correo.com\",\"password\":\"x\","
                + "\"telefono\":\"300\",\"documento\":\"CC-300\""
                + "}";

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON).content(cuerpo))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.estado").value(404));
    }
}
