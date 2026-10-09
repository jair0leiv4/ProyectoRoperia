# Crea el repositorio Git de cada microservicio con ramas main / develop / feature/* y commits descriptivos.
# Version PowerShell de scripts/git-setup.sh (para usar cuando Git Bash falla).
#
# Uso (desde PowerShell, dentro de la carpeta ropaclinica):
#   powershell -ExecutionPolicy Bypass -File .\scripts\git-setup.ps1
#
# Requiere tener configurado:  git config --global user.name "Tu Nombre"  y  user.email "tu@correo"

$ErrorActionPreference = 'Continue'
$Raiz = Split-Path -Parent $PSScriptRoot

if (-not (git config user.name))  { Write-Host 'Falta: git config --global user.name "Tu Nombre"';  exit 1 }
if (-not (git config user.email)) { Write-Host 'Falta: git config --global user.email "tu@correo"'; exit 1 }

# Ejecuta git y detiene el script si el comando falla.
function G {
    & git @args
    if ($LASTEXITCODE -ne 0) { throw "git $($args -join ' ') fallo (codigo $LASTEXITCODE)" }
}

function HayCambiosEnStaging {
    & git diff --cached --quiet
    return ($LASTEXITCODE -ne 0)
}

# Agrega al staging las carpetas con ese nombre (model, service, ...) bajo src/main y hace commit si hay cambios.
function CommitDirs([string]$Mensaje, [string[]]$Nombres) {
    foreach ($n in $Nombres) {
        Get-ChildItem -Path 'src/main' -Directory -Recurse -Filter $n -ErrorAction SilentlyContinue |
            ForEach-Object { & git add $_.FullName }
    }
    if (HayCambiosEnStaging) { G commit -q -m $Mensaje }
}

# Agrega rutas concretas al staging y hace commit si hay cambios.
function CommitPaths([string]$Mensaje, [string[]]$Rutas) {
    foreach ($p in $Rutas) { if (Test-Path $p) { & git add $p } }
    if (HayCambiosEnStaging) { G commit -q -m $Mensaje }
}

function NuevaFeature([string]$Rama) {
    G checkout -q develop
    G checkout -q -b $Rama
}

function Integrar([string]$Rama, [string]$Mensaje) {
    G checkout -q develop
    G merge -q --no-ff $Rama -m $Mensaje
}

$Servicios = @('auth-service', 'inventario-service', 'movimientos-service', 'bajas-service', 'reportes-service')

foreach ($S in $Servicios) {
    Write-Host "==> $S"
    Set-Location (Join-Path $Raiz $S)
    if (Test-Path '.git') { Write-Host '   ya tiene .git, se omite'; continue }
    $Nombre = $S -replace '-service$', ''

    G init -q -b main
    CommitPaths "chore: estructura inicial del proyecto Maven con Spring Boot ($S)" @('.gitignore', 'pom.xml', "src/main/java/cl/duoc/ropaclinica/$Nombre/Application.java", 'README.md')
    G checkout -q -b develop

    NuevaFeature 'feature/modelo-jpa'
    CommitDirs 'feat(model): agrega entidades JPA y relaciones OneToMany/ManyToOne' @('model')
    Integrar 'feature/modelo-jpa' 'merge: integra feature/modelo-jpa en develop'

    NuevaFeature 'feature/persistencia-y-logica'
    CommitDirs 'feat(repository): agrega repositorios Spring Data JPA' @('repository')
    CommitDirs 'feat(dto): agrega DTOs de entrada y salida con validaciones' @('dto')
    CommitDirs 'feat(service): implementa la logica de negocio y transacciones' @('service')
    CommitPaths 'feat(config): configura la conexion a MySQL en application.properties' @('src/main/resources')
    Integrar 'feature/persistencia-y-logica' 'merge: integra feature/persistencia-y-logica en develop'

    NuevaFeature 'feature/api-rest'
    CommitDirs 'feat(controller): implementa los endpoints REST del CRUD' @('controller')
    CommitDirs 'feat(exception): agrega manejo global de errores con respuestas uniformes' @('exception')
    Integrar 'feature/api-rest' 'merge: integra feature/api-rest en develop'

    NuevaFeature 'feature/seguridad-jwt'
    CommitDirs 'feat(security): protege la API con JWT y roles' @('security')
    CommitDirs 'feat(config): documenta la API con OpenAPI/Swagger y carga datos iniciales' @('config')
    Integrar 'feature/seguridad-jwt' 'merge: integra feature/seguridad-jwt en develop'

    NuevaFeature 'feature/pruebas-y-docker'
    CommitPaths 'test: agrega pruebas unitarias de la capa de servicio con Mockito' @('src/test')
    CommitPaths 'build: agrega Dockerfile y docker-compose con MySQL' @('Dockerfile', '.dockerignore', 'docker-compose.yml', '.env.example', 'sql')
    CommitPaths 'docs: agrega coleccion de Postman con casos exitosos y de error' @('postman')
    Integrar 'feature/pruebas-y-docker' 'merge: integra feature/pruebas-y-docker en develop'

    & git add -A
    if (HayCambiosEnStaging) { G commit -q -m 'chore: ajustes finales' }
    G checkout -q main
    G merge -q --no-ff develop -m "release: version 1.0.0 del microservicio $S"
    G tag -a v1.0.0 -m 'Version 1.0.0'
    G checkout -q develop
    G checkout -q main
    Write-Host ("   ramas: " + ((& git branch --format='%(refname:short)') -join ' '))
}

# Repositorio de orquestacion (docker-compose, Postman completo, docs)
Set-Location $Raiz
if (-not (Test-Path '.git')) {
    Write-Host '==> ropaclinica (orquestacion)'
    G init -q -b main
    CommitPaths 'chore: estructura inicial del repositorio de orquestacion' @('.gitignore', 'README.md')
    G checkout -q -b develop

    G checkout -q -b feature/docker-compose
    CommitPaths 'build: agrega docker-compose para levantar los 5 microservicios con sus bases de datos' @('docker-compose.yml', '.env.example')
    G checkout -q develop
    G merge -q --no-ff feature/docker-compose -m 'merge: integra feature/docker-compose en develop'

    G checkout -q -b feature/postman-y-docs
    CommitPaths 'docs: agrega coleccion de Postman completa, checklist de rubrica y guion del video' @('postman', 'docs', 'scripts')
    G checkout -q develop
    G merge -q --no-ff feature/postman-y-docs -m 'merge: integra feature/postman-y-docs en develop'

    G checkout -q main
    G merge -q --no-ff develop -m 'release: version 1.0.0'
}

Write-Host 'Listo. Revisa un repositorio con:  git log --oneline --graph --all'
Write-Host 'Siguiente paso: crear los repositorios vacios en GitHub y en cada carpeta ejecutar:'
Write-Host '  git remote add origin <URL>; git push -u origin --all; git push --tags'
