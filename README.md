# ProyectoHotel

Sistema de Gestión Hotelera — **Primera Entrega del Proyecto Final**.

Esta entrega cubre el modelo relacional, las entidades JPA, la configuración de
persistencia y las migraciones automáticas de base de datos. Aún no incluye
lógica de negocio (servicios/controladores).

## Tecnologías

- Java 17
- Spring Boot 4.0.6 (Spring Data JPA / Hibernate)
- PostgreSQL (producción) — H2 en memoria (pruebas)
- Flyway (migraciones automáticas)
- Maven Wrapper (`mvnw`)

## Requisitos previos

- JDK 17 instalado.
- PostgreSQL en ejecución.
- No necesitas instalar Maven: el proyecto incluye el wrapper.

## 1. Crear la base de datos

En PostgreSQL (pgAdmin, DBeaver o `psql`) crea una base de datos llamada:

```
proyecto_hotel
```

## 2. Configurar las credenciales

`src/main/resources/application.properties` lee variables de entorno con
valores por defecto:

| Variable     | Valor por defecto                                   |
|--------------|-----------------------------------------------------|
| `URL_BD`     | `jdbc:postgresql://localhost:5432/proyecto_hotel`   |
| `USUARIO_BD` | `postgres`                                           |
| `CLAVE_BD`   | `postgres`                                            |

Si tu usuario/clave coinciden con los valores por defecto no necesitas hacer
nada. En caso contrario, define las variables antes de ejecutar.

## 3. Ejecutar la aplicación

**Linux / macOS:**

```bash
./mvnw spring-boot:run
```

**Windows (PowerShell):**

```powershell
.\mvnw.cmd spring-boot:run
```

Al iniciar, Flyway crea automáticamente todas las tablas con la migración
`src/main/resources/db/migration/V1__crear_tablas.sql`. Flyway es el dueño del
esquema; Hibernate solo valida que las entidades coincidan (`ddl-auto=validate`).

## 4. Ejecutar las pruebas

```bash
./mvnw test
```

Las pruebas usan H2 en memoria con el perfil `test` (Flyway desactivado, esquema
generado por Hibernate). No requieren PostgreSQL.

## Estructura del proyecto

```
ProyectoHotel/
├── docs/                                  # Documentación del proyecto
│   ├── Primera Entrega Proyecto Final 1.pdf   (enunciado)
│   └── Documento Técnico (...).docx            (análisis y requerimientos)
├── src/main/java/com/acm/proyectohotel/
│   ├── ProyectoHotelApplication.java
│   └── entidad/                           # Entidades JPA (modelo relacional)
├── src/main/resources/
│   ├── application.properties             # Configuración PostgreSQL
│   ├── application-test.properties        # Configuración H2 (pruebas)
│   └── db/migration/                      # Migraciones Flyway
└── src/test/java/...
```

## Notas

- El diagrama relacional aún no está incluido en `docs/`. Si modificas el modelo,
  actualiza las entidades y la migración Flyway de forma coherente.
- Para añadir cambios al esquema, crea una nueva migración
  (`V2__...sql`); no edites `V1`.
