package com.acm.proyectohotel.entidad;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.acm.proyectohotel.enums.EstadoGeneral;

@Entity
@Table(name = "cadena_hotel")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CadenaHotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoGeneral estado;
}

