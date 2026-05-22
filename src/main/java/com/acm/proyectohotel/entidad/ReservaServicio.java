package com.acm.proyectohotel.entidad;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "reserva_servicio")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reserva_id", nullable = false)
    private Reserva reserva;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servicio_id", nullable = false)
    private Servicio servicio;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false)
    private BigDecimal precioUnitario;

    @Column(nullable = false)
    private BigDecimal subtotal;

    @JsonProperty("reservaId")
    public Long getReservaId() {
        return reserva != null ? reserva.getId() : null;
    }

    public void setReservaId(Long reservaId) {
        if (reservaId == null) {
            this.reserva = null;
        } else {
            Reserva referencia = new Reserva();
            referencia.setId(reservaId);
            this.reserva = referencia;
        }
    }

    @JsonProperty("servicioId")
    public Long getServicioId() {
        return servicio != null ? servicio.getId() : null;
    }

    public void setServicioId(Long servicioId) {
        if (servicioId == null) {
            this.servicio = null;
        } else {
            Servicio referencia = new Servicio();
            referencia.setId(servicioId);
            this.servicio = referencia;
        }
    }
}
