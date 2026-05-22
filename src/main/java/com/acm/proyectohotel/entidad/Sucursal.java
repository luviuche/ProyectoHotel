package com.acm.proyectohotel.entidad;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.acm.proyectohotel.enums.EstadoGeneral;

@Entity
@Table(name = "sucursal")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cadena_id", nullable = false)
    private CadenaHotel cadenaHotel;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private String ciudad;

    @Column(nullable = false)
    private String telefono;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String categoria;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoGeneral estado;

    /** Clave foranea hacia la cadena, expuesta como id plano en el JSON. */
    @JsonProperty("cadenaId")
    public Long getCadenaId() {
        return cadenaHotel != null ? cadenaHotel.getId() : null;
    }

    public void setCadenaId(Long cadenaId) {
        if (cadenaId == null) {
            this.cadenaHotel = null;
        } else {
            CadenaHotel referencia = new CadenaHotel();
            referencia.setId(cadenaId);
            this.cadenaHotel = referencia;
        }
    }
}
