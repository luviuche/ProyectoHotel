package com.acm.proyectohotel.entidad;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "habitacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sucursal_id", nullable = false)
    private Sucursal sucursal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_id", nullable = false)
    private TipoHabitacion tipo;

    @Column(nullable = false)
    private String numero;

    @Column(nullable = false)
    private Integer piso;

    @Column(nullable = false)
    private Integer capacidad;

    @Column(name = "precio_noche", nullable = false)
    private BigDecimal precioNoche;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private Boolean disponible;
}

