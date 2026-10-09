# RopaClínica – microservicios (Java 21 · Spring Boot · Maven · MySQL · Docker)

Sistema para controlar la ropa clínica de una ropería hospitalaria (sábanas, cubrecamas, camisas, etc.).
Cada microservicio tiene su propio repositorio Git, su propia base de datos MySQL y se documenta con Swagger.

| Microservicio | Puerto | Base de datos (puerto host) | Función |
|---|---|---|---|
| auth-service | 8081 | auth_db (3309) | Autenticación y Usuarios |
| inventario-service | 8082 | inventario_db (3310) | Inventario de Ropa Clínica |
| movimientos-service | 8083 | movimientos_db (3311) | Movimientos (Entrega y Recolección) |
| bajas-service | 8084 | bajas_db (3312) | Bajas y Pérdidas |
| reportes-service | 8085 | reportes_db (3313) | Reportes e Historial |

Todos validan el mismo token JWT que emite `auth-service` (mismo `JWT_SECRET`).
Quedan fuera por ahora (diseño futuro): API Gateway, Servicio de Registro, Event Bus y Lavandería Externa con Circuit Breaker.

## Levantar TODO con Docker
```bash
# Los 5 repositorios deben estar clonados dentro de la misma carpeta que este docker-compose.yml
cp .env.example .env
docker compose up --build
```
Swagger de cada servicio: `http://localhost:<puerto>/swagger-ui.html`

## Probar con Postman
Importar `postman/RopaClinica-completo.postman_collection.json` (o la colección de cada servicio).
Cada request trae sus tests con el código HTTP esperado; las que empiezan con `ERROR` verifican el manejo de errores.

## Repositorios Git (entrega)
Cada servicio se entrega como su propio repositorio de GitHub (+ este repositorio de orquestación).
Para crear el historial de ramas y commits (`main`, `develop`, `feature/*`) ejecutar una vez: `bash scripts/git-setup.sh`
Detalle en `docs/`.
