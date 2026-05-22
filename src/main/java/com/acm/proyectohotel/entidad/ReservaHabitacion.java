package com.acm.proyectohotel.entidad;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reserva_habitacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaHabitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reserva_id", nullable = false)
    private Reserva reserva;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habitacion_id", nullable = false)
    private Habitacion habitacion;

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

    @JsonProperty("habitacionId")
    public Long getHabitacionId() {
        return habitacion != null ? habitacion.getId() : null;
    }

    public void setHabitacionId(Long habitacionId) {
        if (habitacionId == null) {
            this.habitacion = null;
        } else {
            Habitacion referencia = new Habitacion();
            referencia.setId(habitacionId);
            this.habitacion = referencia;
        }
    }
}
