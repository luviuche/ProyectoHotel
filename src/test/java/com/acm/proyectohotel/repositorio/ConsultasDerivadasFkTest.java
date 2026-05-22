package com.acm.proyectohotel.repositorio;

import com.acm.proyectohotel.entidad.*;
import com.acm.proyectohotel.enums.EstadoGeneral;
import com.acm.proyectohotel.enums.EstadoHabitacion;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Regresion: las consultas derivadas sobre claves foraneas deben resolver la
 * travesia anidada (p. ej. sucursal.id) y NO interpretarse como un atributo
 * escalar inexistente. Los getters getXxxId() de las entidades hacian que Spring
 * Data generara JPQL invalido; el guion bajo (findBySucursal_Id) lo evita.
 */
@SpringBootTest
@ActiveProfiles("test")
class ConsultasDerivadasFkTest {

    @Autowired private RolRepository rolRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private CadenaHotelRepository cadenaHotelRepository;
    @Autowired private SucursalRepository sucursalRepository;
    @Autowired private TipoHabitacionRepository tipoHabitacionRepository;
    @Autowired private HabitacionRepository habitacionRepository;

    @Test
    void findByRol_Id_resuelveLaTravesia() {
        Rol rol = new Rol();
        rol.setNombre("ROL_FK_TEST");
        rol = rolRepository.save(rol);

        Usuario usuario = new Usuario();
        usuario.setRol(rol);
        usuario.setNombre("Ana");
        usuario.setApellido("Perez");
        usuario.setEmail("ana.fk@correo.com");
        usuario.setPassword("x");
        usuario.setTelefono("300");
        usuario.setDocumento("CC-FK-1");
        usuarioRepository.save(usuario);

        assertThat(usuarioRepository.findByRol_Id(rol.getId()))
                .extracting(Usuario::getEmail)
                .containsExactly("ana.fk@correo.com");
    }

    @Test
    void existsBySucursal_IdAndNumero_resuelveLaTravesia() {
        CadenaHotel cadena = new CadenaHotel();
        cadena.setNombre("ACM_FK");
        cadena.setEstado(EstadoGeneral.ACTIVO);
        cadena = cadenaHotelRepository.save(cadena);

        Sucursal sucursal = new Sucursal();
        sucursal.setCadenaHotel(cadena);
        sucursal.setNombre("Centro");
        sucursal.setDireccion("Cra 1");
        sucursal.setCiudad("Bogota");
        sucursal.setTelefono("601");
        sucursal.setEmail("centro.fk@acm.com");
        sucursal.setCategoria("4 estrellas");
        sucursal.setEstado(EstadoGeneral.ACTIVO);
        sucursal = sucursalRepository.save(sucursal);

        TipoHabitacion tipo = new TipoHabitacion();
        tipo.setNombre("DOBLE_FK");
        tipo = tipoHabitacionRepository.save(tipo);

        Habitacion habitacion = new Habitacion();
        habitacion.setSucursal(sucursal);
        habitacion.setTipo(tipo);
        habitacion.setNumero("101");
        habitacion.setPiso(1);
        habitacion.setCapacidad(2);
        habitacion.setPrecioNoche(new BigDecimal("150.00"));
        habitacion.setEstado(EstadoHabitacion.DISPONIBLE);
        habitacion.setDisponible(true);
        habitacionRepository.save(habitacion);

        assertThat(habitacionRepository.existsBySucursal_IdAndNumero(sucursal.getId(), "101")).isTrue();
        assertThat(habitacionRepository.existsBySucursal_IdAndNumero(sucursal.getId(), "999")).isFalse();
    }
}
