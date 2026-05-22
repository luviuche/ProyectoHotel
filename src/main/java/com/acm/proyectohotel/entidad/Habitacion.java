package com.acm.proyectohotel.entidad;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.acm.proyectohotel.enums.EstadoHabitacion;
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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sucursal_id", nullable = false)
    private Sucursal sucursal;

    @JsonIgnore
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoHabitacion estado;

    @Column(nullable = false)
    private Boolean disponible;

    @JsonProperty("sucursalId")
    public Long getSucursalId() {
        return sucursal != null ? sucursal.getId() : null;
    }

    public void setSucursalId(Long sucursalId) {
        if (sucursalId == null) {
            this.sucursal = null;
        } else {
            Sucursal referencia = new Sucursal();
            referencia.setId(sucursalId);
            this.sucursal = referencia;
        }
    }

    @JsonProperty("tipoId")
    public Long getTipoId() {
        return tipo != null ? tipo.getId() : null;
    }

    public void setTipoId(Long tipoId) {
        if (tipoId == null) {
            this.tipo = null;
        } else {
            TipoHabitacion referencia = new TipoHabitacion();
            referencia.setId(tipoId);
            this.tipo = referencia;
        }
    }
}
