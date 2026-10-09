# Convierte ropaclinica en UN solo repositorio (monorepo) con los 5 microservicios dentro,
# usando ramas main / develop / feature/* y commits descriptivos.
#
# Requisitos previos:
#   - Haber ejecutado antes git-setup.ps1 (o .sh): la raiz ya tiene su .git con main y develop.
#   - Haber reemplazado el README.md de la raiz por el nuevo.
#
# Uso (desde PowerShell, dentro de la carpeta ropaclinica):
#   powershell -ExecutionPolicy Bypass -File .\scripts\git-monorepo.ps1

$ErrorActionPreference = 'Continue'
$Raiz     = Split-Path -Parent $PSScriptRoot
$UrlRepo  = 'https://github.com/jair0leiv4/ProyectoRoperia.git'   # cambiar si usas otro repositorio
$NomRepo  = 'ProyectoRoperia'

Set-Location $Raiz
if (-not (Test-Path '.git')) { Write-Host 'La raiz no tiene .git. Ejecuta primero git-setup.ps1'; exit 1 }
if (-not (git config user.name))  { Write-Host 'Falta: git config --global user.name "Tu Nombre"';  exit 1 }
if (-not (git config user.email)) { Write-Host 'Falta: git config --global user.email "tu@correo"'; exit 1 }

function G {
    & git @args
    if ($LASTEXITCODE -ne 0) { throw "git $($args -join ' ') fallo (codigo $LASTEXITCODE)" }
}
function HayCambiosEnStaging { & git diff --cached --quiet; return ($LASTEXITCODE -ne 0) }

function CommitPaths([string]$Mensaje, [string[]]$Rutas) {
    foreach ($p in $Rutas) { if (Test-Path $p) { & git add $p } }
    if (HayCambiosEnStaging) { G commit -q -m $Mensaje }
}
# Agrega las carpetas con ese nombre (model, service, ...) que haya bajo <servicio>/src/main
function CommitDirs([string]$Mensaje, [string]$Servicio, [string[]]$Nombres) {
    foreach ($n in $Nombres) {
        Get-ChildItem -Path "$Servicio/src/main" -Directory -Recurse -Filter $n -ErrorAction SilentlyContinue |
            ForEach-Object { & git add $_.FullName }
    }
    if (HayCambiosEnStaging) { G commit -q -m $Mensaje }
}
function Integrar([string]$Rama, [string]$Mensaje) {
    G checkout -q develop
    G merge -q --no-ff $Rama -m $Mensaje
}

$Servicios = @('auth-service', 'inventario-service', 'movimientos-service', 'bajas-service', 'reportes-service')
$Utf8SinBom = New-Object System.Text.UTF8Encoding($false)

# 1) Quitar los .git anidados (cada servicio pasa a ser una carpeta del repositorio de la raiz)
foreach ($s in $Servicios) {
    if (Test-Path "$s/.git") { Remove-Item -Recurse -Force "$s/.git"; Write-Host "   .git anidado eliminado en $s" }
}

# 2) Ajustar los README de los servicios: ahora se clona el repo y se entra a la carpeta del servicio
foreach ($s in $Servicios) {
    $ruta = Join-Path $Raiz "$s/README.md"
    if (Test-Path $ruta) {
        $txt = [System.IO.File]::ReadAllText($ruta, [System.Text.Encoding]::UTF8)
        $txt = $txt.Replace('git clone <URL-DEL-REPOSITORIO>', "git clone $UrlRepo")
        $txt = $txt.Replace("cd $s`n", "cd $NomRepo/$s`n").Replace("cd $s`r`n", "cd $NomRepo/$s`r`n")
        [System.IO.File]::WriteAllText($ruta, $txt, $Utf8SinBom)
    }
}

# 3) Rama base del monorepo: .gitignore sin exclusiones de servicios + README nuevo
G checkout -q develop
G checkout -q -b feature/monorepo-base
$gitignore = ".env`n.idea/`ntarget/`n*.iml`n*.log`n"
[System.IO.File]::WriteAllText((Join-Path $Raiz '.gitignore'), $gitignore, $Utf8SinBom)
CommitPaths 'chore: versiona los 5 microservicios dentro del mismo repositorio (monorepo)' @('.gitignore')
CommitPaths 'docs: actualiza el README de la raiz con las instrucciones del monorepo' @('README.md')
Integrar 'feature/monorepo-base' 'merge: integra feature/monorepo-base en develop'

# 4) Una rama feature por microservicio, con commits por capa
foreach ($s in $Servicios) {
    $n = $s -replace '-service$', ''
    Write-Host "==> $s (feature/$s)"
    G checkout -q develop
    G checkout -q -b "feature/$s"

    CommitPaths "chore($n): estructura inicial del microservicio con Maven y Spring Boot" @("$s/.gitignore", "$s/pom.xml", "$s/src/main/java/cl/duoc/ropaclinica/$n/Application.java", "$s/README.md")
    CommitDirs  "feat($n): agrega entidades JPA y relaciones OneToMany/ManyToOne" $s @('model')
    CommitDirs  "feat($n): agrega repositorios Spring Data JPA" $s @('repository')
    CommitDirs  "feat($n): agrega DTOs de entrada y salida con validaciones" $s @('dto')
    CommitDirs  "feat($n): implementa la logica de negocio y transacciones" $s @('service')
    CommitPaths "feat($n): configura la conexion a MySQL en application.properties" @("$s/src/main/resources")
    CommitDirs  "feat($n): implementa los endpoints REST del CRUD" $s @('controller')
    CommitDirs  "feat($n): agrega manejo global de errores con respuestas uniformes" $s @('exception')
    CommitDirs  "feat($n): protege la API con JWT y roles" $s @('security')
    CommitDirs  "feat($n): documenta la API con OpenAPI/Swagger y carga datos iniciales" $s @('config')
    CommitPaths "test($n): agrega pruebas unitarias de la capa de servicio con Mockito" @("$s/src/test")
    CommitPaths "build($n): agrega Dockerfile y docker-compose con MySQL" @("$s/Dockerfile", "$s/.dockerignore", "$s/docker-compose.yml", "$s/.env.example", "$s/sql")
    CommitPaths "docs($n): agrega coleccion de Postman con casos exitosos y de error" @("$s/postman")

    & git add $s
    if (HayCambiosEnStaging) { G commit -q -m "chore($n): ajustes finales" }
    Integrar "feature/$s" "merge: integra feature/$s en develop"
}

# 5) Cualquier otro cambio pendiente (por ejemplo los scripts) y release a main
& git add -A
if (HayCambiosEnStaging) { G commit -q -m 'chore: agrega scripts de apoyo al repositorio' }
G checkout -q main
G merge -q --no-ff develop -m 'release: version 1.0.0 del monorepo RopaClinica'
G tag -a v1.0.0 -m 'Version 1.0.0'
G checkout -q develop
G checkout -q main

Write-Host ''
Write-Host ('ramas: ' + ((& git branch --format='%(refname:short)') -join ' '))
Write-Host 'Listo. Revisa con:  git --no-pager log --oneline --graph --all'
Write-Host 'Para subir:'
Write-Host "  git remote remove origin   (si ya existe)"
Write-Host "  git remote add origin $UrlRepo"
Write-Host '  git push -u origin --all --force'
Write-Host '  git push --tags'
