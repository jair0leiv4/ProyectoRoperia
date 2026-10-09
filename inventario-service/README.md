# inventario-service

Microservicio **Inventario de Ropa Clínica** del sistema *RopaClínica* (control de ropa clínica de una ropería hospitalaria).
Java 21 · Spring Boot 3.5 · Maven · Spring Data JPA · MySQL 8 · JWT · Swagger (OpenAPI).

> **Importante:** este servicio no emite tokens, solo los valida con el mismo `JWT_SECRET`. Para obtener un token hay que tener corriendo `auth-service` (puerto 8081). Si levantas todo junto, usa el `docker-compose.yml` del repositorio `ropaclinica` (raíz).

## Requisitos
- JDK 21
- Maven 3.9+ (o el Maven integrado de IntelliJ)
- Docker Desktop (para MySQL)
- Git

## Pasos para reproducir (clonar → compilar → ejecutar)
```bash
# 1. Clonar el repositorio
git clone https://github.com/jair0leiv4/ProyectoRoperia.git
cd ProyectoRoperia/inventario-service

# 2. Variables de entorno (valores de laboratorio ya incluidos)
cp .env.example .env

# 3. Levantar solo la base de datos MySQL (puerto 3310 en tu PC)
docker compose up -d mysql

# 4. Descargar dependencias, compilar, probar y empaquetar
mvn clean install        # compila + ejecuta pruebas + genera el .jar
# (también: mvn clean package  /  mvn test)

# 5. Ejecutar la aplicación (puerto 8082)
java -jar target/inventario-service-1.0.0.jar
# o bien: mvn spring-boot:run
```
Las dependencias se descargan automáticamente desde Maven Central según el `pom.xml`.

**Todo en Docker** (base de datos + aplicación): `docker compose up --build`

## Probar
- Swagger UI: http://localhost:8082/swagger-ui.html
- Estado (público): http://localhost:8082/api/publico/estado
- Postman: importar `postman/RopaClinica.postman_collection.json` (incluye casos exitosos y de error con sus códigos HTTP esperados).

## Endpoints
| Método | Ruta | Acceso | Descripción |
|---|---|---|---|
| GET/POST | `/api/tipos-prenda` | Autenticado | Lista / crea tipos de prenda |
| GET/PUT | `/api/tipos-prenda/{id}` | Autenticado | Busca / actualiza un tipo |
| DELETE | `/api/tipos-prenda/{id}` | ADMIN | Elimina un tipo (409 si tiene prendas) |
| GET/POST | `/api/prendas` | Autenticado | Lista (filtro ?tipoPrendaId=) / registra stock |
| GET | `/api/prendas/bajo-stock` | Autenticado | Prendas con cantidad <= stock mínimo |
| GET/PUT | `/api/prendas/{id}` | Autenticado | Busca / actualiza una prenda |
| DELETE | `/api/prendas/{id}` | ADMIN | Elimina una prenda |

Los endpoints protegidos requieren el header `Authorization: Bearer <token>`.
Errores con formato único: `{timestamp, status, error, mensaje, detalles}` (400 validación, 401 sin/mal token, 403 sin rol, 404 no existe, 409 conflicto).

## Modelo de datos
`TipoPrenda` (1) ─── (N) `PrendaStock`  → `@OneToMany` en TipoPrenda, `@ManyToOne` en PrendaStock

## Arquitectura (por capas)
```
src/main/java/cl/duoc/ropaclinica/inventario
├── controller/   endpoints REST
├── service/      lógica de negocio y transacciones
├── repository/   acceso a datos (Spring Data JPA)
├── model/        entidades JPA
├── dto/          objetos de entrada/salida con validaciones
├── security/     JWT y reglas de acceso
├── exception/    manejo global de errores
└── config/       OpenAPI y datos iniciales
```

## Flujo de ramas Git
`main` (estable) ← `develop` (integración) ← `feature/*` (cada funcionalidad).
