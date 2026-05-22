# API REST — Segunda Entrega

Documentación de la capa funcional del backend (repositorios, servicios y
controladores REST) del Sistema de Gestión Hotelera.

## Arquitectura por capas

```
Controlador (REST)  ->  Servicio (lógica de negocio)  ->  Repositorio (JpaRepository)  ->  Base de datos
        |                        |                                  |
   /api/...              validaciones, FKs,                  CRUD automático
   GET/POST/PUT/DELETE   reglas de negocio                   + consultas derivadas
```

| Capa         | Paquete                              | Responsabilidad                                        |
|--------------|--------------------------------------|--------------------------------------------------------|
| Entidad      | `com.acm.proyectohotel.entidad`      | Modelo JPA (mapeo objeto-relacional).                  |
| Repositorio  | `com.acm.proyectohotel.repositorio`  | Acceso a datos vía `JpaRepository` (CRUD + queries).   |
| Servicio     | `com.acm.proyectohotel.servicio`     | Lógica de negocio, validaciones, resolución de FKs.    |
| Controlador  | `com.acm.proyectohotel.controlador`  | Endpoints REST (`@RestController`).                    |
| Excepción    | `com.acm.proyectohotel.excepcion`    | Manejo global de errores (`@RestControllerAdvice`).    |

## Convenciones de la API

- **Formato:** JSON (`Content-Type: application/json`).
- **Claves foráneas como id plano.** Las relaciones se envían y se reciben como
  un id simple, no como objetos anidados. Por ejemplo, al crear una habitación se
  envía `"sucursalId": 1` y `"tipoId": 1`. En las respuestas también aparecen así.
- **El `password` nunca se devuelve.** Se acepta al crear/actualizar un usuario,
  pero jamás se incluye en las respuestas.
- **Valores por defecto.** Algunos campos se completan en el servidor si se omiten
  (p. ej. `estado`, `disponible`, `fechaPago`, `fechaEmision`, `subtotal` de un
  detalle de servicio).

### Códigos de estado HTTP

| Código | Cuándo se usa                                                        |
|--------|---------------------------------------------------------------------|
| `200`  | Lectura o actualización correcta (GET, PUT).                        |
| `201`  | Recurso creado (POST).                                              |
| `204`  | Recurso eliminado (DELETE), sin cuerpo.                             |
| `400`  | Regla de negocio violada (datos duplicados, fechas incoherentes…). |
| `404`  | El recurso (o una FK referenciada) no existe.                       |
| `409`  | Conflicto de integridad en la base de datos.                       |

### Cuerpo de error

```json
{
  "estado": 404,
  "error": "Not Found",
  "mensaje": "Usuario con id 99 no encontrado.",
  "fecha": "2026-05-22T10:30:00"
}
```

## Operaciones CRUD (idénticas para todas las entidades)

Para un recurso en `/api/{recurso}`:

| Método   | Ruta                | Operación | Respuesta |
|----------|---------------------|-----------|-----------|
| `GET`    | `/api/{recurso}`    | Listar    | `200`     |
| `GET`    | `/api/{recurso}/{id}` | Consultar uno | `200` / `404` |
| `POST`   | `/api/{recurso}`    | Crear     | `201` / `400` / `404` |
| `PUT`    | `/api/{recurso}/{id}` | Actualizar | `200` / `400` / `404` |
| `DELETE` | `/api/{recurso}/{id}` | Eliminar | `204` / `404` |

## Recursos disponibles

| Entidad           | Ruta base                  |
|-------------------|----------------------------|
| Rol               | `/api/roles`               |
| Usuario           | `/api/usuarios`            |
| Cadena de hotel   | `/api/cadenas-hotel`       |
| Sucursal          | `/api/sucursales`          |
| Tipo de habitación| `/api/tipos-habitacion`    |
| Habitación        | `/api/habitaciones`        |
| Reserva           | `/api/reservas`            |
| Reserva-Habitación| `/api/reserva-habitaciones`|
| Servicio          | `/api/servicios`           |
| Reserva-Servicio  | `/api/reserva-servicios`   |
| Pago              | `/api/pagos`               |
| Factura           | `/api/facturas`            |

## Ejemplos de cuerpo (POST)

### Rol — `/api/roles`
```json
{ "nombre": "CLIENTE", "descripcion": "Usuario que reserva" }
```

### Usuario — `/api/usuarios`
```json
{
  "rolId": 1,
  "nombre": "Ana",
  "apellido": "Pérez",
  "email": "ana@correo.com",
  "password": "secreta123",
  "telefono": "3001234567",
  "documento": "CC-100"
}
```

### Cadena de hotel — `/api/cadenas-hotel`
```json
{ "nombre": "Hoteles ACM", "descripcion": "Cadena nacional", "estado": "ACTIVO" }
```

### Sucursal — `/api/sucursales`
```json
{
  "cadenaId": 1,
  "nombre": "ACM Centro",
  "direccion": "Cra 1 # 2-3",
  "ciudad": "Bogotá",
  "telefono": "6011234567",
  "email": "centro@acm.com",
  "categoria": "4 estrellas",
  "estado": "ACTIVO"
}
```

### Tipo de habitación — `/api/tipos-habitacion`
```json
{ "nombre": "Doble", "descripcion": "Dos camas" }
```

### Habitación — `/api/habitaciones`
```json
{
  "sucursalId": 1,
  "tipoId": 1,
  "numero": "101",
  "piso": 1,
  "capacidad": 2,
  "precioNoche": 150.00,
  "estado": "DISPONIBLE",
  "disponible": true
}
```

### Reserva — `/api/reservas`
```json
{
  "clienteId": 1,
  "fechaEntrada": "2026-06-01",
  "fechaSalida": "2026-06-05",
  "estado": "PENDIENTE",
  "total": 600.00
}
```

### Reserva-Habitación — `/api/reserva-habitaciones`
```json
{ "reservaId": 1, "habitacionId": 1 }
```

### Servicio — `/api/servicios`
```json
{ "nombre": "Desayuno buffet", "tipo": "RESTAURANTE", "descripcion": "Incluye bebidas", "precio": 20.00, "activo": true }
```

### Reserva-Servicio — `/api/reserva-servicios`
```json
{ "reservaId": 1, "servicioId": 1, "cantidad": 2, "precioUnitario": 20.00 }
```
> El `subtotal` se calcula en el servidor (`cantidad × precioUnitario`). Si se
> omite `precioUnitario`, se toma el precio del servicio.

### Pago — `/api/pagos`
```json
{ "reservaId": 1, "monto": 600.00, "metodoPago": "TARJETA", "estado": "COMPLETADO" }
```
> Un pago por reserva (relación 1:1). `fechaPago` se asigna automáticamente si se omite.

### Factura — `/api/facturas`
```json
{ "pagoId": 1, "numeroFactura": "F-0001", "subtotal": 600.00, "impuestos": 114.00, "descripcion": "Estancia jun-2026" }
```
> Una factura por pago (relación 1:1). `total` (= subtotal + impuestos) y
> `fechaEmision` se calculan si se omiten.

## Valores de enumeraciones

| Enum             | Valores                                               |
|------------------|-------------------------------------------------------|
| `EstadoGeneral`  | `ACTIVO`, `INACTIVO`                                   |
| `EstadoHabitacion`| `DISPONIBLE`, `OCUPADA`, `MANTENIMIENTO`, `LIMPIEZA` |
| `EstadoReserva`  | `PENDIENTE`, `CONFIRMADA`, `CANCELADA`, `FINALIZADA`   |
| `EstadoPago`     | `PENDIENTE`, `COMPLETADO`, `FALLIDO`, `REEMBOLSADO`    |
| `MetodoPago`     | `EFECTIVO`, `TARJETA`, `TRANSFERENCIA`, `PAGO_DIGITAL` |
| `TipoServicio`   | `RESTAURANTE`, `SPA`, `LAVANDERIA`, `TRANSPORTE`       |

## Ejemplo rápido con `curl`

```bash
# Crear un rol
curl -X POST http://localhost:8080/api/roles \
  -H "Content-Type: application/json" \
  -d '{"nombre":"CLIENTE","descripcion":"Usuario que reserva"}'

# Listar usuarios
curl http://localhost:8080/api/usuarios

# Actualizar una habitación
curl -X PUT http://localhost:8080/api/habitaciones/1 \
  -H "Content-Type: application/json" \
  -d '{"sucursalId":1,"tipoId":1,"numero":"101","piso":1,"capacidad":3,"precioNoche":180.00,"estado":"DISPONIBLE","disponible":true}'

# Eliminar una reserva
curl -X DELETE http://localhost:8080/api/reservas/1
```
