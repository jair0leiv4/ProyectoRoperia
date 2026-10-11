# RopaClínica – microservicios (Java 21 · Spring Boot · Maven · MySQL · Docker)

Sistema para controlar la ropa clínica de una ropería hospitalaria (sábanas, cubrecamas, camisas, etc.).
Cinco microservicios Spring Boot, cada uno con su propia base de datos MySQL, seguridad JWT y documentación Swagger.

**Integrantes:** Jairo Leiva · _(nombre de tu compañero/a)_

| Microservicio | Puerto | Base de datos (puerto host) | Función |
|---|---|---|---|
| auth-service | 8081 | auth_db (3309) | Autenticación y Usuarios |
| inventario-service | 8082 | inventario_db (3310) | Inventario de Ropa Clínica |
| movimientos-service | 8083 | movimientos_db (3311) | Movimientos (Entrega y Recolección) |
| bajas-service | 8084 | bajas_db (3312) | Bajas y Pérdidas |
| reportes-service | 8085 | reportes_db (3313) | Reportes e Historial |

Todos validan el mismo token JWT que emite `auth-service` (mismo `JWT_SECRET`).
Quedan fuera por ahora (diseño futuro): API Gateway, Servicio de Registro, Event Bus y Lavandería Externa con Circuit Breaker.

## Requisitos
- JDK 21
- Maven 3.9+ (o el Maven integrado de IntelliJ)
- Docker Desktop
- Git

## Clonar y levantar TODO con Docker
```bash
git clone https://github.com/jair0leiv4/ProyectoRoperia.git
cd ProyectoRoperia
cp .env.example .env
docker compose up --build
```
Usuario inicial: `admin` / `Admin1234`.
Swagger de cada servicio: `http://localhost:<puerto>/swagger-ui.html`

## Ejecutar un solo servicio (compilar y correr con Maven)
```bash
cd ProyectoRoperia/inventario-service
cp .env.example .env
docker compose up -d mysql      # solo la base de datos del servicio
mvn clean install               # compila, ejecuta pruebas y genera el .jar
mvn package                     # solo empaqueta
java -jar target/inventario-service-1.0.0.jar
```
Cada carpeta de servicio trae su propio `README.md` con sus endpoints y su modelo de datos.
Los servicios distintos de `auth-service` necesitan un token: `auth-service` debe estar corriendo (puerto 8081).

## Probar con Postman
Importar `postman/RopaClinica-completo.postman_collection.json` (o la colección de cada servicio, en `<servicio>/postman/`).
Cada request trae sus tests con el código HTTP esperado; las que empiezan con `ERROR` verifican el manejo de errores.

## Estructura del repositorio
```
ProyectoRoperia/
├── auth-service/          Autenticación y usuarios (JWT)
├── inventario-service/    Tipos de prenda y stock
├── movimientos-service/   Entregas y recolecciones
├── bajas-service/         Bajas y pérdidas
├── reportes-service/      Reportes e historial
├── postman/               Colección completa de Postman
├── docs/                  Checklist de la rúbrica y guion del video
├── docker-compose.yml     Levanta los 5 servicios con sus bases de datos
└── .env.example
```
Cada servicio sigue la arquitectura en capas `controller / service / repository / model` (más `dto`, `security`, `exception` y `config`).

## Flujo de ramas Git
`main` (estable) ← `develop` (integración) ← `feature/*` (cada funcionalidad), con commits descriptivos.
