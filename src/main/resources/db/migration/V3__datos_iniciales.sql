-- Datos iniciales de catalogo: valores fijos definidos en el documento.

insert into rol (nombre, descripcion) values
    ('CLIENTE',       'Usuario que realiza y gestiona sus reservas'),
    ('EMPLEADO',      'Personal operativo de una sucursal'),
    ('ADMINISTRADOR', 'Gestiona hoteles, usuarios y el sistema');

insert into tipo_habitacion (nombre, descripcion) values
    ('SENCILLA',     'Habitacion individual basica'),
    ('DOBLE',        'Habitacion para dos personas'),
    ('SUITE',        'Suite con sala independiente'),
    ('PRESIDENCIAL', 'Suite presidencial de lujo');
