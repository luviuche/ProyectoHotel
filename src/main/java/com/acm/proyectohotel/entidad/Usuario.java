package com.acm.proyectohotel.entidad;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Se ignora el objeto Rol en la serializacion JSON; el cliente trabaja con "rolId".
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false)
    private String email;

    // Se acepta al crear/actualizar pero nunca se expone en las respuestas.
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String telefono;

    @Column(nullable = false)
    private String documento;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** Expone la clave foranea como id plano en el JSON (entrada y salida). */
    @JsonProperty("rolId")
    public Long getRolId() {
        return rol != null ? rol.getId() : null;
    }

    public void setRolId(Long rolId) {
        if (rolId == null) {
            this.rol = null;
        } else {
            Rol referencia = new Rol();
            referencia.setId(rolId);
            this.rol = referencia;
        }
    }
}
