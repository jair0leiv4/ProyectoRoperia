#!/usr/bin/env bash
# Crea el repositorio Git de cada microservicio con ramas main / develop / feature/* y commits descriptivos.
# Uso (desde la carpeta ropaclinica):  bash scripts/git-setup.sh
# Requiere tener configurado:  git config --global user.name "Tu Nombre"  y  user.email "tu@correo"
set -euo pipefail

SERVICIOS="auth-service inventario-service movimientos-service bajas-service reportes-service"
RAIZ="$(cd "$(dirname "$0")/.." && pwd)"

git config user.name  >/dev/null 2>&1 || [ -n "${GIT_AUTHOR_NAME:-}" ]  || { echo "Falta: git config --global user.name \"Tu Nombre\""; exit 1; }
git config user.email >/dev/null 2>&1 || [ -n "${GIT_AUTHOR_EMAIL:-}" ] || { echo "Falta: git config --global user.email \"tu@correo\""; exit 1; }

# Agrega al staging las carpetas con ese nombre (model, service, ...) bajo src/ y hace commit si hay cambios.
commit_dirs() {   # $1 = mensaje, resto = nombres de carpeta
  local msg="$1"; shift
  for d in "$@"; do find src/main -type d -name "$d" -exec git add {} + 2>/dev/null || true; done
  if ! git diff --cached --quiet; then git commit -q -m "$msg"; fi
}
commit_paths() {  # $1 = mensaje, resto = rutas
  local msg="$1"; shift
  for p in "$@"; do [ -e "$p" ] && git add "$p" || true; done
  if ! git diff --cached --quiet; then git commit -q -m "$msg"; fi
}
feature() {       # $1 = rama, $2 = mensaje del merge
  git checkout -q develop
  git checkout -q -b "$1"
}
integrar() {      # $1 = rama, $2 = mensaje del merge
  git checkout -q develop
  git merge -q --no-ff "$1" -m "$2"
}

for S in $SERVICIOS; do
  echo "==> $S"
  cd "$RAIZ/$S"
  [ -d .git ] && { echo "   ya tiene .git, se omite"; continue; }
  NOMBRE="${S%-service}"
  git init -q -b main
  commit_paths "chore: estructura inicial del proyecto Maven con Spring Boot ($S)" .gitignore pom.xml src/main/java/cl/duoc/ropaclinica/$NOMBRE/Application.java README.md
  git checkout -q -b develop

  feature feature/modelo-jpa
  commit_dirs "feat(model): agrega entidades JPA y relaciones OneToMany/ManyToOne" model
  integrar feature/modelo-jpa "merge: integra feature/modelo-jpa en develop"

  feature feature/persistencia-y-logica
  commit_dirs "feat(repository): agrega repositorios Spring Data JPA" repository
  commit_dirs "feat(dto): agrega DTOs de entrada y salida con validaciones" dto
  commit_dirs "feat(service): implementa la logica de negocio y transacciones" service
  commit_paths "feat(config): configura la conexion a MySQL en application.properties" src/main/resources
  integrar feature/persistencia-y-logica "merge: integra feature/persistencia-y-logica en develop"

  feature feature/api-rest
  commit_dirs "feat(controller): implementa los endpoints REST del CRUD" controller
  commit_dirs "feat(exception): agrega manejo global de errores con respuestas uniformes" exception
  integrar feature/api-rest "merge: integra feature/api-rest en develop"

  feature feature/seguridad-jwt
  commit_dirs "feat(security): protege la API con JWT y roles" security
  commit_dirs "feat(config): documenta la API con OpenAPI/Swagger y carga datos iniciales" config
  integrar feature/seguridad-jwt "merge: integra feature/seguridad-jwt en develop"

  feature feature/pruebas-y-docker
  commit_paths "test: agrega pruebas unitarias de la capa de servicio con Mockito" src/test
  commit_paths "build: agrega Dockerfile y docker-compose con MySQL" Dockerfile .dockerignore docker-compose.yml .env.example sql
  commit_paths "docs: agrega coleccion de Postman con casos exitosos y de error" postman
  integrar feature/pruebas-y-docker "merge: integra feature/pruebas-y-docker en develop"

  git add -A
  git diff --cached --quiet || git commit -q -m "chore: ajustes finales"
  git checkout -q main
  git merge -q --no-ff develop -m "release: version 1.0.0 del microservicio $S"
  git tag -a v1.0.0 -m "Version 1.0.0"
  git checkout -q develop
  git checkout -q main
  echo "   ramas: $(git branch --format='%(refname:short)' | tr '\n' ' ')"
done

# Repositorio de orquestación (docker-compose, Postman completo, docs)
cd "$RAIZ"
if [ ! -d .git ]; then
  echo "==> ropaclinica (orquestacion)"
  git init -q -b main
  commit_paths "chore: estructura inicial del repositorio de orquestacion" .gitignore README.md
  git checkout -q -b develop
  git checkout -q -b feature/docker-compose
  commit_paths "build: agrega docker-compose para levantar los 5 microservicios con sus bases de datos" docker-compose.yml .env.example
  git checkout -q develop && git merge -q --no-ff feature/docker-compose -m "merge: integra feature/docker-compose en develop"
  git checkout -q -b feature/postman-y-docs
  commit_paths "docs: agrega coleccion de Postman completa, checklist de rubrica y guion del video" postman docs scripts
  git checkout -q develop && git merge -q --no-ff feature/postman-y-docs -m "merge: integra feature/postman-y-docs en develop"
  git checkout -q main && git merge -q --no-ff develop -m "release: version 1.0.0"
fi
echo "Listo. Revisa un repositorio con:  git log --oneline --graph --all"
echo "Siguiente paso: crear los repositorios vacios en GitHub y en cada carpeta ejecutar:"
echo "  git remote add origin <URL> && git push -u origin --all && git push --tags"
