# Prestamos Bancarios

Aplicacion de demostracion para la gestion de prestamos bancarios. Los clientes
solicitan prestamos, el personal los aprueba o rechaza, y se registran los pagos
hasta saldar cada prestamo.

La aplicacion esta compuesta por tres servicios:

- **Frontend** (`fe-test/`): Angular con Angular Material.
- **Backend** (`be_test/`): API REST en Spring Boot, con autenticacion JWT.
- **Base de datos**: MySQL.

## Stack tecnologico

- **Backend**: Java 21, Spring Boot 4.1, Spring Security (JWT), Spring Data JPA, MySQL
- **Frontend**: Angular 22, Angular Material, servido con nginx en produccion
- **Documentacion de la API**: OpenAPI/Swagger UI

## Requisitos previos

Solo se necesita tener instalado **Docker** y **Docker Compose**. No hace falta
instalar Java, Node ni MySQL de forma local: todo corre en contenedores.

## Ejecutar localmente

Desde la raiz del repositorio:

```bash
docker compose up --build
```

Esto levanta los tres servicios (base de datos, backend y frontend) en un solo
comando. La primera vez puede tardar un poco mientras se construyen las
imagenes y se descarga MySQL.

Una vez arriba, la aplicacion queda disponible en:

| Servicio    | URL                                                            |
|-------------|-----------------------------------------------------------------|
| Frontend    | http://localhost                                                 |
| API backend | http://localhost:8080                                            |
| Swagger UI  | http://localhost:8080/swagger-ui.html                            |
| MySQL       | localhost:3306 (usuario `root`, password `root`, base `chn_test`) |

Para detener los servicios:

```bash
docker compose down
```

Para detenerlos y ademas borrar los datos de la base (volver a empezar desde
cero):

```bash
docker compose down -v
```

## Primer uso

No hay credenciales de demo predefinidas. Al entrar por primera vez, hay que
crear una cuenta desde el enlace **"Crear una cuenta"** en la pantalla de
inicio de sesion del frontend. La cuenta se registra automaticamente con rol
`USER`.

## Estructura del repositorio

```
├── be_test/          Backend (Spring Boot)
├── fe-test/           Frontend (Angular)
├── SQL_Script/         Esquema de la base de datos y datos semilla (plazos de
│                       prestamo), se ejecutan automaticamente al iniciar el
│                       contenedor de la base de datos por primera vez
├── docs/               Requerimientos funcionales y diagramas
└── docker-compose.yml  Orquesta los tres servicios
```
