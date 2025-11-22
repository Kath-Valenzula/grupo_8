# 🚀 GUÍA DE INICIO RÁPIDO - Recetas Pro

Esta guía te permite iniciar la aplicación completa después de reiniciar el PC.

---

## ⚡ INICIO RÁPIDO (Copiar y pegar en PowerShell)

```powershell
# Ir al directorio del proyecto
cd "C:\Users\Kath Stark\sc-s2\sc-s2\demo"

# 1. Iniciar MySQL (si usas Docker)
docker start mysql-container

# ESPERAR 10 segundos para que MySQL inicie
Start-Sleep -Seconds 10

# 2. Compilar aplicación (opcional si ya está compilada)
mvn clean package -DskipTests

# 3. Iniciar Spring Boot en background
Start-Job -ScriptBlock { 
    Set-Location "C:\Users\Kath Stark\sc-s2\sc-s2\demo"
    java -jar "target\demo-0.0.1-SNAPSHOT.jar" 
} -Name "SpringApp"

Write-Host "✓ Spring Boot iniciando..." -ForegroundColor Green
Write-Host "  Esperando 20 segundos para que inicie completamente..." -ForegroundColor Yellow
Start-Sleep -Seconds 20

# 4. Verificar que Spring Boot está corriendo
$test = Test-NetConnection localhost -Port 8080 -InformationLevel Quiet
if ($test) {
    Write-Host "✓ Spring Boot funcionando en puerto 8080" -ForegroundColor Green
} else {
    Write-Host "✗ ERROR: Spring Boot no responde" -ForegroundColor Red
    exit 1
}

# 5. Iniciar Cloudflare Tunnel para URL pública
Start-Process PowerShell -ArgumentList "-NoExit", "-Command", "& `"$env:USERPROFILE\cloudflared.exe`" tunnel --url http://localhost:8080"

Write-Host "`n=== APLICACIÓN INICIADA ===" -ForegroundColor Cyan
Write-Host "✓ MySQL: Corriendo" -ForegroundColor Green
Write-Host "✓ Spring Boot: http://localhost:8080" -ForegroundColor Green
Write-Host "✓ Cloudflare Tunnel: Iniciando en nueva ventana..." -ForegroundColor Green
Write-Host "`nEspera 10 segundos y busca la URL en la ventana de Cloudflare:" -ForegroundColor Yellow
Write-Host "  https://XXXXX.trycloudflare.com" -ForegroundColor Cyan
```

---

## 📋 PASO A PASO DETALLADO

### 1️⃣ Abrir PowerShell como Administrador

- Presiona `Win + X`
- Selecciona "Windows PowerShell (Admin)" o "Terminal (Admin)"

### 2️⃣ Navegar al Proyecto

```powershell
cd "C:\Users\Kath Stark\sc-s2\sc-s2\demo"
```

### 3️⃣ Iniciar MySQL

**Opción A: Con Docker (recomendado)**

```powershell
# Verificar si el contenedor existe
docker ps -a | Select-String "mysql-container"

# Si existe, iniciarlo
docker start mysql-container

# Si NO existe, crearlo
docker run -d `
  --name mysql-container `
  -p 3306:3306 `
  -p 33060:33060 `
  -e MYSQL_ROOT_PASSWORD=rootpassword `
  -e MYSQL_DATABASE=mydatabase `
  -e MYSQL_USER=myuser `
  -e MYSQL_PASSWORD=mypassword `
  mysql:8.0
```

**Opción B: MySQL Nativo (si instalaste manualmente)**

```powershell
# Verificar si está corriendo
Get-Process mysqld -ErrorAction SilentlyContinue

# Si no está corriendo, iniciarlo como servicio
net start MySQL80
```

**Verificar MySQL:**

```powershell
# Esperar 10 segundos
Start-Sleep -Seconds 10

# Probar conexión
Test-NetConnection localhost -Port 3306
```

### 4️⃣ Compilar la Aplicación (si hay cambios)

```powershell
mvn clean package -DskipTests
```

**Resultado esperado:** `BUILD SUCCESS` y archivo `target\demo-0.0.1-SNAPSHOT.jar` creado

### 5️⃣ Iniciar Spring Boot

**Opción A: En background (recomendado para demos)**

```powershell
Start-Job -ScriptBlock { 
    Set-Location "C:\Users\Kath Stark\sc-s2\sc-s2\demo"
    java -jar "target\demo-0.0.1-SNAPSHOT.jar" 
} -Name "SpringApp"

# Ver logs en tiempo real
Get-Job -Name "SpringApp" | Receive-Job -Keep
```

**Opción B: En terminal visible (para debugging)**

```powershell
java -jar target\demo-0.0.1-SNAPSHOT.jar
```

**Verificar que está corriendo:**

```powershell
# Esperar 20 segundos
Start-Sleep -Seconds 20

# Probar endpoint
Test-NetConnection localhost -Port 8080

# Probar en navegador
Start-Process "http://localhost:8080/home"
```

### 6️⃣ Iniciar Cloudflare Tunnel (URL Pública)

**En una NUEVA ventana de PowerShell:**

```powershell
& "$env:USERPROFILE\cloudflared.exe" tunnel --url http://localhost:8080
```

**Resultado esperado:**

```
+--------------------------------------------------------------------------------------------+
|  Your quick Tunnel has been created! Visit it at (it may take some time to be reachable): |
|  https://random-words-here.trycloudflare.com                                               |
+--------------------------------------------------------------------------------------------+
```

**⚠️ IMPORTANTE:** 
- **Copia la URL** que aparece (ejemplo: `https://dover-poster-accepted-moves.trycloudflare.com`)
- Esta URL es **temporal** y cambia cada vez que reinicias el túnel
- Mantén esta ventana **abierta** mientras demuestras la aplicación

---

## ✅ VERIFICACIÓN COMPLETA

Copia este script para verificar que todo funciona:

```powershell
Write-Host "`n=== VERIFICACIÓN DEL SISTEMA ===" -ForegroundColor Cyan

# MySQL
$mysql = Test-NetConnection localhost -Port 3306 -InformationLevel Quiet
Write-Host "MySQL (3306): $(if($mysql){'✓ OK'}else{'✗ FALLA'})" -ForegroundColor $(if($mysql){'Green'}else{'Red'})

# Spring Boot
$spring = Test-NetConnection localhost -Port 8080 -InformationLevel Quiet
Write-Host "Spring Boot (8080): $(if($spring){'✓ OK'}else{'✗ FALLA'})" -ForegroundColor $(if($spring){'Green'}else{'Red'})

# Cloudflare Tunnel
$tunnel = Get-Process cloudflared -ErrorAction SilentlyContinue
Write-Host "Cloudflare Tunnel: $(if($tunnel){'✓ OK'}else{'✗ FALLA'})" -ForegroundColor $(if($tunnel){'Green'}else{'Red'})

# Test HTTP
if ($spring) {
    try {
        $response = Invoke-WebRequest http://localhost:8080/home -UseBasicParsing -TimeoutSec 5
        Write-Host "HTTP Response: ✓ Status $($response.StatusCode)" -ForegroundColor Green
    } catch {
        Write-Host "HTTP Response: ✗ Error" -ForegroundColor Red
    }
}

Write-Host "`n=== URLs ===" -ForegroundColor Cyan
Write-Host "Local: http://localhost:8080" -ForegroundColor Yellow
Write-Host "Pública: [Ver ventana de Cloudflare Tunnel]" -ForegroundColor Yellow
```

---

## 🎥 GUÍA PARA VIDEO DEMO (PROFESOR)

### Preparación (antes de grabar)

```powershell
# 1. Iniciar todo (usar script de INICIO RÁPIDO arriba)
# 2. Esperar 30 segundos
# 3. Abrir pestañas del navegador:
Start-Process "http://localhost:8080/home"
Start-Process "http://localhost:8080/recetas"
# 4. Abrir Postman con la collection importada
# 5. Tener VS Code abierto en WebSecurityConfig.java
```

### Estructura del Video (3-5 minutos)

**MINUTO 0:00-0:30 - Introducción**
```
"Hola profesor, soy [Nombre] del Grupo 8.
Les presentamos el Sistema de Gestión de Recetas con seguridad implementada."
```

**MINUTO 0:30-1:30 - Navegación Pública**

1. Mostrar URL pública en navegador
2. Navegar a `/home` - Mostrar carousel
3. Ir a `/recetas` - Mostrar listado con imágenes externas
4. Hacer búsqueda por ingrediente (ejemplo: "pollo")
5. Click en una receta → Ver detalles

**MINUTO 1:30-2:00 - Registro y Login Web**

1. Ir a `/register`
2. Crear usuario: `demo` / `demo@test.com` / `Demo1234!`
3. Mostrar validaciones (si falta campo)
4. Submit → Redirección a `/login`
5. Ingresar credenciales → Redirección a `/home`
6. Abrir DevTools (F12) → Application → Cookies
7. Mostrar cookie `JSESSIONID` con flags `HttpOnly`, `SameSite`

**MINUTO 2:00-3:00 - API REST con JWT**

1. Abrir Postman
2. **Request 1:** POST `/api/auth/login`
   ```json
   {
     "username": "demo",
     "password": "Demo1234!"
   }
   ```
3. Mostrar respuesta con `token`, `username`, `email`
4. Copiar token
5. **Request 2:** POST `/api/recetas/1/comentarios` **SIN** token
   - Mostrar error 401 Unauthorized
6. **Request 3:** POST `/api/recetas/1/comentarios` **CON** token
   - Header: `Authorization: Bearer [TOKEN]`
   - Body:
   ```json
   {
     "texto": "Excelente receta, muy fácil de seguir",
     "calificacion": 5
   }
   ```
   - Mostrar respuesta 201 Created con comentario guardado

**MINUTO 3:00-4:00 - Seguridad (Headers y Código)**

1. Volver al navegador → F12 → Network tab
2. Refresh `/recetas`
3. Click en request → Headers → Response Headers
4. Mostrar:
   - `content-security-policy: default-src 'self'; img-src 'self' data: https: ...`
   - `x-frame-options: DENY`
   - `x-content-type-options: nosniff`
   - `x-xss-protection: 1; mode=block`

5. Abrir VS Code → `WebSecurityConfig.java`
6. Mostrar configuración de CSP (líneas 35-45)
7. Explicar: "Configuramos Content Security Policy para permitir imágenes externas de recetas HTTPS"

**MINUTO 4:00-4:30 - Análisis OWASP ZAP**

1. Abrir `docs/2025-11-21-ZAP-Report-.html` en navegador
2. Mostrar resumen: "1 vulnerabilidad media (CSP), ya mitigada"
3. Scroll a tabla de vulnerabilidades
4. Mostrar sección de mitigación en `docs/INFORME_OWASP.md`

**MINUTO 4:30-5:00 - Conclusión**

```
"En resumen:
- ✓ Aplicación funcional con autenticación web (sesiones)
- ✓ API REST con JWT para autenticación stateless
- ✓ Endpoints privados protegidos (comentarios, valoraciones)
- ✓ Headers de seguridad configurados (CSP, HSTS, XSS)
- ✓ Análisis con OWASP ZAP documentado
- ✓ Vulnerabilidades mitigadas en código

El proyecto está publicado en GitHub: github.com/Kath-Valenzula/grupo_8
Muchas gracias."
```

---

## 🎬 SCRIPT DE DEMOSTRACIÓN PARA COMPAÑEROS

Si quieres demostrar en vivo a tus compañeros:

```powershell
# 1. Obtener la URL pública del túnel
Write-Host "`n=== URL PÚBLICA ===" -ForegroundColor Cyan
Write-Host "Comparte esta URL con tus compañeros:" -ForegroundColor Yellow
Write-Host "[Buscar en la ventana de Cloudflare Tunnel]" -ForegroundColor Cyan

# 2. Crear usuario de prueba para demostración
Write-Host "`n=== CREDENCIALES DE PRUEBA ===" -ForegroundColor Cyan
Write-Host "Usuario: demo" -ForegroundColor Green
Write-Host "Email: demo@test.com" -ForegroundColor Green
Write-Host "Password: Demo1234!" -ForegroundColor Green

# 3. Test rápido de endpoints
Write-Host "`n=== TESTS RÁPIDOS ===" -ForegroundColor Cyan

# Home
$home = Invoke-WebRequest http://localhost:8080/home -UseBasicParsing
Write-Host "✓ Home: Status $($home.StatusCode)" -ForegroundColor Green

# Recetas
$recetas = Invoke-WebRequest http://localhost:8080/recetas -UseBasicParsing
Write-Host "✓ Recetas: Status $($recetas.StatusCode)" -ForegroundColor Green

# API Pública
$api = Invoke-RestMethod http://localhost:8080/api/recetas
Write-Host "✓ API Recetas: $($api.Count) recetas disponibles" -ForegroundColor Green

Write-Host "`n✓ TODO LISTO PARA DEMOSTRAR" -ForegroundColor Green
```

---

## 🛑 DETENER TODO (Al finalizar)

```powershell
# 1. Detener Spring Boot
Get-Job -Name "SpringApp" | Stop-Job
Get-Job -Name "SpringApp" | Remove-Job

# 2. Detener Cloudflare Tunnel
Get-Process cloudflared | Stop-Process

# 3. Detener MySQL (Docker)
docker stop mysql-container

# 4. Verificar que todo se detuvo
Write-Host "Spring Boot detenido: $(!(Get-Job -Name SpringApp -ErrorAction SilentlyContinue))" -ForegroundColor Green
Write-Host "Cloudflare detenido: $(!(Get-Process cloudflared -ErrorAction SilentlyContinue))" -ForegroundColor Green
Write-Host "MySQL detenido: $(!(docker ps | Select-String mysql-container))" -ForegroundColor Green
```

---

## ⚠️ SOLUCIÓN DE PROBLEMAS

### MySQL no inicia

```powershell
# Ver logs del contenedor
docker logs mysql-container

# Reiniciar contenedor
docker restart mysql-container

# Si falla, recrear desde cero
docker rm -f mysql-container
docker run -d --name mysql-container -p 3306:3306 -e MYSQL_ROOT_PASSWORD=rootpassword -e MYSQL_DATABASE=mydatabase -e MYSQL_USER=myuser -e MYSQL_PASSWORD=mypassword mysql:8.0
```

### Spring Boot no inicia

```powershell
# Ver logs del job
Get-Job -Name "SpringApp" | Receive-Job

# Si hay error, detener y reiniciar
Get-Job -Name "SpringApp" | Stop-Job
Get-Job -Name "SpringApp" | Remove-Job

# Compilar de nuevo
mvn clean package -DskipTests

# Iniciar de nuevo
Start-Job -ScriptBlock { 
    Set-Location "C:\Users\Kath Stark\sc-s2\sc-s2\demo"
    java -jar "target\demo-0.0.1-SNAPSHOT.jar" 
} -Name "SpringApp"
```

### Puerto 8080 ya en uso

```powershell
# Encontrar proceso que usa el puerto
Get-NetTCPConnection -LocalPort 8080 | Select-Object OwningProcess

# Matar proceso (cambiar PID)
Stop-Process -Id [PID] -Force
```

### Cloudflare Tunnel no muestra URL

```powershell
# Cerrar y reiniciar
Get-Process cloudflared | Stop-Process
Start-Sleep -Seconds 2
& "$env:USERPROFILE\cloudflared.exe" tunnel --url http://localhost:8080
```

---

## 📝 CHECKLIST PRE-GRABACIÓN

Antes de grabar el video final:

- [ ] PC reiniciado
- [ ] MySQL corriendo (puerto 3306)
- [ ] Spring Boot corriendo (puerto 8080)
- [ ] Cloudflare Tunnel con URL pública
- [ ] URL probada en navegador incógnito
- [ ] Postman con collection importada
- [ ] VS Code abierto en `WebSecurityConfig.java`
- [ ] Navegador con pestañas: /home, /recetas, /login
- [ ] DevTools (F12) listo
- [ ] Informe HTML de ZAP abierto
- [ ] Micr

ófono funcionando
- [ ] Pantalla limpia (cerrar apps innecesarias)

---

## 🎯 RESUMEN DE COMANDOS ESENCIALES

```powershell
# INICIO COMPLETO
cd "C:\Users\Kath Stark\sc-s2\sc-s2\demo"
docker start mysql-container
Start-Sleep -Seconds 10
Start-Job -ScriptBlock { Set-Location "C:\Users\Kath Stark\sc-s2\sc-s2\demo"; java -jar "target\demo-0.0.1-SNAPSHOT.jar" } -Name "SpringApp"
Start-Sleep -Seconds 20
Start-Process PowerShell -ArgumentList "-NoExit", "-Command", "& `"$env:USERPROFILE\cloudflared.exe`" tunnel --url http://localhost:8080"

# VERIFICACIÓN
Test-NetConnection localhost -Port 3306
Test-NetConnection localhost -Port 8080
Get-Process cloudflared

# DETENER TODO
Get-Job -Name "SpringApp" | Stop-Job; Get-Job -Name "SpringApp" | Remove-Job
Get-Process cloudflared | Stop-Process
docker stop mysql-container
```

---

**¡Listo para la demo!** 🚀
