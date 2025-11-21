# Script para ejecutar OWASP ZAP automáticamente
# Asegúrate de tener OWASP ZAP instalado en C:\Program Files\ZAP\Zed Attack Proxy\

$zapPath = "C:\Program Files\ZAP\Zed Attack Proxy\zap.bat"
$targetUrl = "http://localhost:8080"
$reportPath = "$PSScriptRoot\zap-report.html"

# Verificar si ZAP está instalado
if (!(Test-Path $zapPath)) {
    Write-Host "ERROR: OWASP ZAP no encontrado en: $zapPath" -ForegroundColor Red
    Write-Host "Descarga ZAP desde: https://www.zaproxy.org/download/" -ForegroundColor Yellow
    exit 1
}

Write-Host "Iniciando escaneo de seguridad con OWASP ZAP..." -ForegroundColor Green
Write-Host "Target: $targetUrl" -ForegroundColor Cyan
Write-Host "Esto puede tomar varios minutos..." -ForegroundColor Yellow

# Ejecutar ZAP en modo headless con quick scan
& $zapPath -cmd -quickurl $targetUrl -quickout $reportPath

if ($LASTEXITCODE -eq 0) {
    Write-Host "`n¡Escaneo completado!" -ForegroundColor Green
    Write-Host "Reporte generado en: $reportPath" -ForegroundColor Cyan
    Write-Host "`nAbriendo reporte en navegador..." -ForegroundColor Yellow
    Start-Process $reportPath
} else {
    Write-Host "`nError al ejecutar ZAP. Código de salida: $LASTEXITCODE" -ForegroundColor Red
}
