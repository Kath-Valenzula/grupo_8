# 🚀 Guía de Inicio Rápido - Después de Reiniciar PC

**Fecha**: 21 de noviembre de 2025  
**Proyecto**: Sistema de Gestión de Recetas - Grupo 8

---

## ⚡ Pasos para Iniciar Todo (5 minutos)

### 📋 Pre-requisitos

- MySQL debe estar corriendo en Docker
- Java 21 y Maven instalados
- ngrok instalado

---

## 🔢 Orden de Inicio

### **Paso 1: Iniciar MySQL (si no está corriendo)** ⏱️ 30 segundos

Abre **PowerShell** y ejecuta:

```powershell
docker ps
```

**Si NO ves `mysql-container` en la lista**, inícialo:

```powershell
docker start mysql-container
```

**Si NO existe el contenedor**, créalo:

```powershell
cd "C:\Users\Kath Stark\sc-s2\sc-s2\demo"
docker run -d --name mysql-container -p 3306:3306 -p 33060:33060 -e MYSQL_ROOT_PASSWORD=rootpassword -e MYSQL_DATABASE=mydatabase -e MYSQL_USER=myuser -e MYSQL_PASSWORD=mypassword mysql:8.0
```

---

### **Paso 2: Iniciar la Aplicación Spring Boot** ⏱️ 30 segundos

Abre **NUEVA ventana de PowerShell** y ejecuta:

```powershell
cd "C:\Users\Kath Stark\sc-s2\sc-s2\demo"
mvn spring-boot:run
```

**Espera hasta ver**: `Started DemoApplication in X seconds`

✅ **Prueba local**: Abre navegador → `http://localhost:8080`

---

### **Paso 3: Iniciar ngrok** ⏱️ 10 segundos

Abre **OTRA ventana de PowerShell** (diferente) y ejecuta:

```powershell
ngrok http 8080
```

Verás algo como esto:

```
Session Status                online
Forwarding                    https://complementarily-foundrous-carmon.ngrok-free.dev -> http://localhost:8080
```

✅ **Copia la URL** que aparece en `Forwarding` (línea que dice `https://...ngrok-free.dev`)

---

### **Paso 4: Probar la URL Pública** ⏱️ 10 segundos

1. Abre tu navegador
2. Pega la URL de ngrok (ejemplo: `https://complementarily-foundrous-carmon.ngrok-free.dev`)
3. Si ves página de ngrok "Visit Site", haz clic
4. Deberías ver tu aplicación ✅

---

## 🎯 Resumen de Comandos (Copia y Pega)

### Terminal 1 (MySQL):
```powershell
docker start mysql-container
```

### Terminal 2 (Spring Boot):
```powershell
cd "C:\Users\Kath Stark\sc-s2\sc-s2\demo"
mvn spring-boot:run
```

### Terminal 3 (ngrok):
```powershell
ngrok http 8080
```

---

## ⚠️ Problemas Comunes

### ❌ Error: "Puerto 8080 ya está en uso"

**Solución**: Mata el proceso Java anterior:

```powershell
Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue | ForEach-Object { Stop-Process -Id $_.OwningProcess -Force }
```

Luego vuelve a ejecutar `mvn spring-boot:run`

---

### ❌ Error: "MySQL connection refused"

**Solución**: Verifica que MySQL esté corriendo:

```powershell
docker ps
```

Si no aparece `mysql-container`, ejecuta:

```powershell
docker start mysql-container
```

---

### ❌ Error: "ngrok command not found"

**Solución**: Verifica la instalación:

```powershell
ngrok version
```

Si no funciona, reinstala ngrok ejecutando:

```powershell
.\demo\docs\instalar-ngrok.ps1
```

---

### ❌ ngrok dice "endpoint is offline"

**Causa**: ngrok se inició ANTES que la aplicación.

**Solución**:
1. Cierra la ventana de ngrok (Ctrl+C)
2. Verifica que la app esté corriendo: `http://localhost:8080`
3. Vuelve a ejecutar: `ngrok http 8080`

---

## 🔄 Orden de Apagado

Cuando termines de trabajar, cierra en este orden:

1. **ngrok**: Presiona `Ctrl+C` en su terminal
2. **Spring Boot**: Presiona `Ctrl+C` en su terminal
3. **MySQL**: (opcional) `docker stop mysql-container`

---

## 📝 Notas Importantes

### ⚠️ La URL de ngrok CAMBIA cada vez que lo reinicias

**Plan gratuito**: Nueva URL aleatoria cada vez  
**Ejemplo**: `https://random-words-123.ngrok-free.dev`

**Si necesitas URL fija**: Considera el plan pago de ngrok ($8/mes)

### 🔑 Token de ngrok ya está configurado

No necesitas volver a ejecutar:
```powershell
ngrok config add-authtoken TU_TOKEN
```

Solo se hace UNA vez (ya está hecho).

---

## ✅ Checklist Visual

```
□ MySQL corriendo (docker ps)
□ Aplicación Spring Boot iniciada (localhost:8080 responde)
□ ngrok corriendo (ves "Session Status: online")
□ URL pública funcionando (navegador carga la app)
```

---

## 🎥 Para Grabar el Video

1. Ejecuta los 3 pasos anteriores
2. Asegúrate que TODO funcione (checklist ✅)
3. Abre `demo/docs/GUION_VIDEO.md` para seguir el guión
4. Graba con OBS Studio o Loom

---

## 📞 Si Algo No Funciona

### Verificar todo el estado:

```powershell
# Ver MySQL
docker ps

# Ver aplicación
curl http://localhost:8080

# Ver procesos Java
Get-Process java

# Ver ngrok
Get-Process ngrok
```

---

**¡Listo! Con estos comandos puedes iniciar todo tú sola después de reiniciar.** 🚀

---

**Creado por**: GitHub Copilot + Grupo 8  
**Última actualización**: 21 de noviembre de 2025
