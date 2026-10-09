# auth-service

Microservicio **Autenticación y Usuarios** del sistema *RopaClínica* (control de ropa clínica de una ropería hospitalaria).
Java 21 · Spring Boot 3.5 · Maven · Spring Data JPA · MySQL 8 · JWT · Swagger (OpenAPI).

## Requisitos
- JDK 21
- Maven 3.9+ (o el Maven integrado de IntelliJ)
- Docker Desktop (para MySQL)
- Git

## Pasos para reproducir (clonar → compilar → ejecutar)
```bash
# 1. Clonar el repositorio
git clone https://github.com/jair0leiv4/ProyectoRoperia.git
cd ProyectoRoperia/auth-service

# 2. Variables de entorno (valores de laboratorio ya incluidos)
cp .env.example .env

# 3. Levantar solo la base de datos MySQL (puerto 3309 en tu PC)
docker compose up -d mysql

# 4. Descargar dependencias, compilar, probar y empaquetar
mvn clean install        # compila + ejecuta pruebas + genera el .jar
# (también: mvn clean package  /  mvn test)

# 5. Ejecutar la aplicación (puerto 8081)
java -jar target/auth-service-1.0.0.jar
# o bien: mvn spring-boot:run
```
Las dependencias se descargan automáticamente desde Maven Central según el `pom.xml`.

Usuario inicial: `admin` / `Admin1234` (se crea al arrancar, junto con los roles ADMIN, BODEGA y DIGITADOR).

**Todo en Docker** (base de datos + aplicación): `docker compose up --build`

## Probar
- Swagger UI: http://localhost:8081/swagger-ui.html
- Estado (público): http://localhost:8081/api/publico/estado
- Postman: importar `postman/RopaClinica.postman_collection.json` (incluye casos exitosos y de error con sus códigos HTTP esperados).

## Endpoints
| Método | Ruta | Acceso | Descripción |
|---|---|---|---|
| POST | `/auth/login` | Público | Login; devuelve el token JWT |
| GET | `/api/usuarios` | ADMIN | Lista usuarios |
| GET | `/api/usuarios/{id}` | ADMIN | Busca un usuario |
| POST | `/api/usuarios` | ADMIN | Crea un usuario con rol |
| PUT | `/api/usuarios/{id}` | ADMIN | Actualiza un usuario |
| DELETE | `/api/usuarios/{id}` | ADMIN | Elimina un usuario |

Los endpoints protegidos requieren el header `Authorization: Bearer <token>`.
Errores con formato único: `{timestamp, status, error, mensaje, detalles}` (400 validación, 401 sin/mal token, 403 sin rol, 404 no existe, 409 conflicto).

## Modelo de datos
`Rol` (1) ─── (N) `Usuario`  → `@OneToMany` en Rol, `@ManyToOne` en Usuario

## Arquitectura (por capas)
```
src/main/java/cl/duoc/ropaclinica/auth
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
