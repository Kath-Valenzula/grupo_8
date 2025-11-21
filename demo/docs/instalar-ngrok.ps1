# Script para descargar e instalar ngrok en Windows
# Fecha: 21 de noviembre de 2025

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Instalador de ngrok para Windows" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Verificar si ya está instalado
$ngrokInstalled = Get-Command ngrok -ErrorAction SilentlyContinue
if ($ngrokInstalled) {
    Write-Host "✅ ngrok ya está instalado en: $($ngrokInstalled.Source)" -ForegroundColor Green
    ngrok version
    exit 0
}

Write-Host "📦 Descargando ngrok..." -ForegroundColor Yellow

# Crear directorio temporal
$tempDir = "$env:TEMP\ngrok-install"
if (!(Test-Path $tempDir)) {
    New-Item -ItemType Directory -Path $tempDir -Force | Out-Null
}

# URL de descarga de ngrok para Windows
$ngrokUrl = "https://bin.equinox.io/c/bNyj1mQVY4c/ngrok-v3-stable-windows-amd64.zip"
$zipPath = "$tempDir\ngrok.zip"
$extractPath = "$tempDir\ngrok"

try {
    # Descargar ngrok
    Write-Host "⬇️  Descargando desde: $ngrokUrl" -ForegroundColor Cyan
    Invoke-WebRequest -Uri $ngrokUrl -OutFile $zipPath -UseBasicParsing
    
    # Extraer ZIP
    Write-Host "📂 Extrayendo archivos..." -ForegroundColor Cyan
    if (Test-Path $extractPath) {
        Remove-Item $extractPath -Recurse -Force
    }
    Expand-Archive -Path $zipPath -DestinationPath $extractPath -Force
    
    # Copiar ngrok.exe a una ubicación en PATH
    $installPath = "$env:LOCALAPPDATA\ngrok"
    if (!(Test-Path $installPath)) {
        New-Item -ItemType Directory -Path $installPath -Force | Out-Null
    }
    
    Copy-Item "$extractPath\ngrok.exe" -Destination "$installPath\ngrok.exe" -Force
    
    # Agregar al PATH del usuario si no está
    $userPath = [Environment]::GetEnvironmentVariable("Path", "User")
    if ($userPath -notlike "*$installPath*") {
        Write-Host "🔧 Agregando ngrok al PATH..." -ForegroundColor Cyan
        [Environment]::SetEnvironmentVariable(
            "Path",
            "$userPath;$installPath",
            "User"
        )
        # Actualizar PATH en la sesión actual
        $env:Path = "$env:Path;$installPath"
    }
    
    Write-Host ""
    Write-Host "✅ ngrok instalado correctamente en: $installPath" -ForegroundColor Green
    Write-Host ""
    Write-Host "📋 Próximos pasos:" -ForegroundColor Yellow
    Write-Host "   1. Visita https://dashboard.ngrok.com/signup para crear cuenta (GRATIS)" -ForegroundColor White
    Write-Host "   2. Copia tu authtoken desde https://dashboard.ngrok.com/get-started/your-authtoken" -ForegroundColor White
    Write-Host "   3. Ejecuta: ngrok config add-authtoken TU_TOKEN_AQUI" -ForegroundColor White
    Write-Host "   4. Inicia tu app: mvn spring-boot:run" -ForegroundColor White
    Write-Host "   5. En otra terminal: ngrok http 8080" -ForegroundColor White
    Write-Host ""
    
    # Limpiar archivos temporales
    Remove-Item $tempDir -Recurse -Force
    
} catch {
    Write-Host ""
    Write-Host "❌ Error durante la instalación: $_" -ForegroundColor Red
    Write-Host ""
    Write-Host "🔧 Instalación Manual:" -ForegroundColor Yellow
    Write-Host "   1. Visita: https://ngrok.com/download" -ForegroundColor White
    Write-Host "   2. Descarga ngrok para Windows" -ForegroundColor White
    Write-Host "   3. Extrae el ZIP y mueve ngrok.exe a una carpeta en tu PATH" -ForegroundColor White
    exit 1
}
