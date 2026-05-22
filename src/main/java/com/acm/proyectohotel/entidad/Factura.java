package com.acm.proyectohotel.entidad;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "factura")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pago_id", nullable = false, unique = true)
    private Pago pago;

    @Column(name = "numero_factura", nullable = false)
    private String numeroFactura;

    @Column(nullable = false)
    private BigDecimal subtotal;

    @Column(nullable = false)
    private BigDecimal impuestos;

    @Column(nullable = false)
    private BigDecimal total;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDateTime fechaEmision;

    private String descripcion;

    @JsonProperty("pagoId")
    public Long getPagoId() {
        return pago != null ? pago.getId() : null;
    }

    public void setPagoId(Long pagoId) {
        if (pagoId == null) {
            this.pago = null;
        } else {
            Pago referencia = new Pago();
            referencia.setId(pagoId);
            this.pago = referencia;
        }
    }
}
