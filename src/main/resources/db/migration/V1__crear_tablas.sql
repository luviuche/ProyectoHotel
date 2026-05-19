create table cadena_hotel (
    id bigserial primary key,
    nombre varchar(100) not null,
    descripcion varchar(255),
    estado varchar(30) not null
);

create table sucursal (
    id bigserial primary key,
    cadena_id bigint not null,
    nombre varchar(100) not null,
    direccion varchar(150) not null,
    ciudad varchar(80) not null,
    telefono varchar(30) not null,
    email varchar(120) not null,
    categoria varchar(30) not null,
    estado varchar(30) not null,
    constraint fk_sucursal_cadena
        foreign key (cadena_id) references cadena_hotel (id)
);

create table rol (
    id bigserial primary key,
    nombre varchar(50) not null,
    descripcion varchar(255)
);

create table usuario (
    id bigserial primary key,
    rol_id bigint not null,
    nombre varchar(80) not null,
    apellido varchar(80) not null,
    email varchar(120) not null,
    password varchar(120) not null,
    telefono varchar(30) not null,
    documento varchar(30) not null,
    created_at timestamp not null default now(),
    constraint fk_usuario_rol
        foreign key (rol_id) references rol (id)
);

create table tipo_habitacion (
    id bigserial primary key,
    nombre varchar(50) not null,
    descripcion varchar(255)
);

create table habitacion (
    id bigserial primary key,
    sucursal_id bigint not null,
    tipo_id bigint not null,
    numero varchar(20) not null,
    piso int not null,
    capacidad int not null,
    precio_noche numeric(12, 2) not null,
    estado varchar(30) not null,
    disponible boolean not null,
    constraint fk_habitacion_sucursal
        foreign key (sucursal_id) references sucursal (id),
    constraint fk_habitacion_tipo
        foreign key (tipo_id) references tipo_habitacion (id)
);

create table reserva (
    id bigserial primary key,
    cliente_id bigint not null,
    fecha_entrada date not null,
    fecha_salida date not null,
    estado varchar(30) not null,
    total numeric(12, 2) not null,
    created_at timestamp not null default now(),
    constraint fk_reserva_cliente
        foreign key (cliente_id) references usuario (id)
);

create table reserva_habitacion (
    id bigserial primary key,
    reserva_id bigint not null,
    habitacion_id bigint not null,
    constraint fk_reserva_habitacion_reserva
        foreign key (reserva_id) references reserva (id),
    constraint fk_reserva_habitacion_habitacion
        foreign key (habitacion_id) references habitacion (id)
);

create table servicio (
    id bigserial primary key,
    nombre varchar(80) not null,
    tipo varchar(50) not null,
    descripcion varchar(255),
    precio numeric(12, 2) not null,
    activo boolean not null
);

create table reserva_servicio (
    id bigserial primary key,
    reserva_id bigint not null,
    servicio_id bigint not null,
    cantidad int not null,
    precio_unitario numeric(12, 2) not null,
    subtotal numeric(12, 2) not null,
    constraint fk_reserva_servicio_reserva
        foreign key (reserva_id) references reserva (id),
    constraint fk_reserva_servicio_servicio
        foreign key (servicio_id) references servicio (id)
);

create table pago (
    id bigserial primary key,
    reserva_id bigint not null,
    monto numeric(12, 2) not null,
    metodo_pago varchar(30) not null,
    estado varchar(30) not null,
    fecha_pago timestamp not null,
    constraint fk_pago_reserva
        foreign key (reserva_id) references reserva (id),
    constraint uq_pago_reserva
        unique (reserva_id)
);

create table factura (
    id bigserial primary key,
    pago_id bigint not null,
    numero_factura varchar(50) not null,
    subtotal numeric(12, 2) not null,
    impuestos numeric(12, 2) not null,
    total numeric(12, 2) not null,
    fecha_emision timestamp not null,
    descripcion varchar(255),
    constraint fk_factura_pago
        foreign key (pago_id) references pago (id),
    constraint uq_factura_pago
        unique (pago_id)
);

