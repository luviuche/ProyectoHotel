# Modelo Relacional — Sistema de Gestión Hotelera

Diagrama entidad-relación de la base de datos `proyecto_hotel`. Refleja la
migración Flyway `V1__crear_tablas.sql` y las restricciones añadidas en
`V2__restricciones_e_indices.sql`.

## Diagrama ER

```mermaid
erDiagram
    CADENA_HOTEL ||--o{ SUCURSAL : contiene
    ROL ||--o{ USUARIO : clasifica
    SUCURSAL ||--o{ HABITACION : aloja
    TIPO_HABITACION ||--o{ HABITACION : define
    USUARIO ||--o{ RESERVA : realiza
    RESERVA ||--o{ RESERVA_HABITACION : incluye
    HABITACION ||--o{ RESERVA_HABITACION : "asignada en"
    RESERVA ||--o{ RESERVA_SERVICIO : agrega
    SERVICIO ||--o{ RESERVA_SERVICIO : detalla
    RESERVA ||--|| PAGO : genera
    PAGO ||--|| FACTURA : respalda

    CADENA_HOTEL {
        bigint id PK
        varchar nombre
        varchar descripcion
        varchar estado "ACTIVO|INACTIVO"
    }
    SUCURSAL {
        bigint id PK
        bigint cadena_id FK
        varchar nombre
        varchar direccion
        varchar ciudad
        varchar telefono
        varchar email
        varchar categoria
        varchar estado "ACTIVO|INACTIVO"
    }
    ROL {
        bigint id PK
        varchar nombre UK
        varchar descripcion
    }
    USUARIO {
        bigint id PK
        bigint rol_id FK
        varchar nombre
        varchar apellido
        varchar email UK
        varchar password
        varchar telefono
        varchar documento UK
        timestamp created_at
    }
    TIPO_HABITACION {
        bigint id PK
        varchar nombre UK
        varchar descripcion
    }
    HABITACION {
        bigint id PK
        bigint sucursal_id FK
        bigint tipo_id FK
        varchar numero
        int piso
        int capacidad
        numeric precio_noche
        varchar estado "DISPONIBLE|OCUPADA|MANTENIMIENTO|LIMPIEZA"
        boolean disponible
    }
    RESERVA {
        bigint id PK
        bigint cliente_id FK
        date fecha_entrada
        date fecha_salida
        varchar estado "PENDIENTE|CONFIRMADA|CANCELADA|FINALIZADA"
        numeric total
        timestamp created_at
    }
    RESERVA_HABITACION {
        bigint id PK
        bigint reserva_id FK
        bigint habitacion_id FK
    }
    SERVICIO {
        bigint id PK
        varchar nombre
        varchar tipo "RESTAURANTE|SPA|LAVANDERIA|TRANSPORTE"
        varchar descripcion
        numeric precio
        boolean activo
    }
    RESERVA_SERVICIO {
        bigint id PK
        bigint reserva_id FK
        bigint servicio_id FK
        int cantidad
        numeric precio_unitario
        numeric subtotal
    }
    PAGO {
        bigint id PK
        bigint reserva_id FK "UNIQUE (1:1)"
        numeric monto
        varchar metodo_pago "EFECTIVO|TARJETA|TRANSFERENCIA|PAGO_DIGITAL"
        varchar estado "PENDIENTE|COMPLETADO|FALLIDO|REEMBOLSADO"
        timestamp fecha_pago
    }
    FACTURA {
        bigint id PK
        bigint pago_id FK "UNIQUE (1:1)"
        varchar numero_factura UK
        numeric subtotal
        numeric impuestos
        numeric total
        timestamp fecha_emision
        varchar descripcion
    }
```

## Relaciones

| Origen | Cardinalidad | Destino | Descripción |
|---|---|---|---|
| `cadena_hotel` | 1 : N | `sucursal` | Una cadena tiene varias sucursales |
| `rol` | 1 : N | `usuario` | Un rol agrupa varios usuarios |
| `sucursal` | 1 : N | `habitacion` | Una sucursal aloja varias habitaciones |
| `tipo_habitacion` | 1 : N | `habitacion` | Un tipo clasifica varias habitaciones |
| `usuario` (cliente) | 1 : N | `reserva` | Un cliente realiza varias reservas |
| `reserva` ↔ `habitacion` | N : M | vía `reserva_habitacion` | Reserva de varias habitaciones |
| `reserva` ↔ `servicio` | N : M | vía `reserva_servicio` | Servicios adicionales por reserva |
| `reserva` | 1 : 1 | `pago` | Un pago por reserva (`uq_pago_reserva`) |
| `pago` | 1 : 1 | `factura` | Una factura por pago (`uq_factura_pago`) |

## Normalización y restricciones

- **3FN**: catálogos separados (`rol`, `tipo_habitacion`, `servicio`) y tablas
  puente (`reserva_habitacion`, `reserva_servicio`) eliminan redundancia y
  relaciones N:M directas.
- **Unicidad**: `usuario.email`, `usuario.documento`, `rol.nombre`,
  `tipo_habitacion.nombre`, `(habitacion.sucursal_id, numero)`,
  `factura.numero_factura`.
- **Dominios controlados**: restricciones `CHECK` que reflejan los enums de la
  aplicación (estados, métodos de pago, tipos de servicio).
- **Reglas de negocio**: `fecha_salida > fecha_entrada`, montos y cantidades
  no negativos.
- **Índices** sobre todas las claves foráneas para escalabilidad de consultas.
