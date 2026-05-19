-- Restricciones de integridad, dominios controlados e indices.
-- Refuerza la normalizacion y robustez exigidas por el documento.

-- 1. Unicidad
alter table usuario         add constraint uq_usuario_email             unique (email);
alter table usuario         add constraint uq_usuario_documento          unique (documento);
alter table rol             add constraint uq_rol_nombre                 unique (nombre);
alter table tipo_habitacion add constraint uq_tipo_habitacion_nombre     unique (nombre);
alter table habitacion      add constraint uq_habitacion_sucursal_numero unique (sucursal_id, numero);
alter table factura         add constraint uq_factura_numero             unique (numero_factura);

-- 2. Dominios controlados (alineados con los enums de la aplicacion)
alter table cadena_hotel add constraint chk_cadena_estado
    check (estado in ('ACTIVO', 'INACTIVO'));
alter table sucursal add constraint chk_sucursal_estado
    check (estado in ('ACTIVO', 'INACTIVO'));
alter table habitacion add constraint chk_habitacion_estado
    check (estado in ('DISPONIBLE', 'OCUPADA', 'MANTENIMIENTO', 'LIMPIEZA'));
alter table reserva add constraint chk_reserva_estado
    check (estado in ('PENDIENTE', 'CONFIRMADA', 'CANCELADA', 'FINALIZADA'));
alter table pago add constraint chk_pago_metodo
    check (metodo_pago in ('EFECTIVO', 'TARJETA', 'TRANSFERENCIA', 'PAGO_DIGITAL'));
alter table pago add constraint chk_pago_estado
    check (estado in ('PENDIENTE', 'COMPLETADO', 'FALLIDO', 'REEMBOLSADO'));
alter table servicio add constraint chk_servicio_tipo
    check (tipo in ('RESTAURANTE', 'SPA', 'LAVANDERIA', 'TRANSPORTE'));

-- 3. Reglas de negocio basicas
alter table reserva add constraint chk_reserva_fechas
    check (fecha_salida > fecha_entrada);
alter table reserva add constraint chk_reserva_total
    check (total >= 0);
alter table habitacion add constraint chk_habitacion_precio
    check (precio_noche >= 0);
alter table habitacion add constraint chk_habitacion_capacidad
    check (capacidad > 0);
alter table habitacion add constraint chk_habitacion_piso
    check (piso >= 0);
alter table pago add constraint chk_pago_monto
    check (monto > 0);
alter table factura add constraint chk_factura_montos
    check (subtotal >= 0 and impuestos >= 0 and total >= 0);
alter table servicio add constraint chk_servicio_precio
    check (precio >= 0);
alter table reserva_servicio add constraint chk_reserva_servicio_cantidad
    check (cantidad > 0);
alter table reserva_servicio add constraint chk_reserva_servicio_montos
    check (precio_unitario >= 0 and subtotal >= 0);

-- 4. Indices en claves foraneas (escalabilidad de consultas)
create index idx_sucursal_cadena              on sucursal (cadena_id);
create index idx_usuario_rol                  on usuario (rol_id);
create index idx_habitacion_sucursal          on habitacion (sucursal_id);
create index idx_habitacion_tipo              on habitacion (tipo_id);
create index idx_reserva_cliente              on reserva (cliente_id);
create index idx_reserva_habitacion_reserva   on reserva_habitacion (reserva_id);
create index idx_reserva_habitacion_habitacion on reserva_habitacion (habitacion_id);
create index idx_reserva_servicio_reserva     on reserva_servicio (reserva_id);
create index idx_reserva_servicio_servicio    on reserva_servicio (servicio_id);
