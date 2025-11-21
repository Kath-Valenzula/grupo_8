# 🚀 Guía Rápida: Despliegue con ngrok

**Fecha**: 21 de noviembre de 2025  
**Proyecto**: Sistema de Gestión de Recetas - Grupo 8

---

## ✅ Paso 1: Instalación Completada

ngrok ya está instalado en: `C:\Users\Kath Stark\AppData\Local\ngrok`

---

## 🔐 Paso 2: Crear Cuenta en ngrok (GRATIS - 2 minutos)

1. **Abre tu navegador** y ve a: <https://dashboard.ngrok.com/signup>

2. **Regístrate** usando una de estas opciones:
   - GitHub (recomendado - más rápido)
   - Google
   - Email

3. **Confirma tu email** (si usaste email directo)

---

## 🔑 Paso 3: Obtener tu Token de Autenticación (1 minuto)

1. **Inicia sesión** en ngrok

2. **Ve a**: <https://dashboard.ngrok.com/get-started/your-authtoken>

3. **Copia tu authtoken** (se ve así: `2abc123XYZ_4def567...`)

4. **Pega el token en esta terminal**:

   ```powershell
   ngrok config add-authtoken TU_TOKEN_AQUI
   ```

   Ejemplo real:
   ```powershell
   ngrok config add-authtoken 2abc123XYZ_4def567UVW890ghi234JKL
   ```

---

## 🚀 Paso 4: Iniciar la Aplicación (30 segundos)

### Opción A: Si la app YA está corriendo

Si ya tienes la aplicación corriendo en `http://localhost:8080`, **salta al Paso 5**.

### Opción B: Si necesitas iniciarla

Abre una **NUEVA terminal PowerShell** y ejecuta:

```powershell
cd "c:\Users\Kath Stark\sc-s2\sc-s2\demo"
mvn spring-boot:run
```

**Espera** a ver el mensaje:
```
Started DemoApplication in X.XXX seconds
```

✅ La aplicación está corriendo en `http://localhost:8080`

---

## 🌐 Paso 5: Exponer la Aplicación con ngrok (10 segundos)

Abre **OTRA terminal PowerShell nueva** (deja la anterior corriendo) y ejecuta:

```powershell
ngrok http 8080
```

---

## 📋 Paso 6: Copiar la URL Pública

Verás una pantalla como esta:

```
ngrok

Session Status                online
Account                       tu_email@example.com (Plan: Free)
Version                       3.x.x
Region                        United States (us)
Web Interface                 http://127.0.0.1:4040
Forwarding                    https://abc123xyz.ngrok-free.app -> http://localhost:8080

Connections                   ttl     opn     rt1     rt5     p50     p90
                              0       0       0.00    0.00    0.00    0.00
```

**COPIA** la URL de "Forwarding" que comienza con `https://` (ejemplo: `https://abc123xyz.ngrok-free.app`)

---

## ✅ Paso 7: Probar la URL Pública

1. **Abre tu navegador** en una ventana privada/incógnito

2. **Pega la URL** de ngrok (ejemplo: `https://abc123xyz.ngrok-free.app`)

3. **Click en "Visit Site"** (ngrok muestra una página de advertencia la primera vez)

4. **Deberías ver** tu aplicación de gestión de recetas funcionando

---

## 📝 Paso 8: Actualizar el README

Una vez que tengas la URL pública funcionando, necesitas agregarla al README:

```powershell
cd "c:\Users\Kath Stark\sc-s2\sc-s2\demo"
code README.md
```

Busca la línea 262 que dice:
```
[IP-DE-TU-VM]
```

Reemplázala con tu URL de ngrok:
```
https://abc123xyz.ngrok-free.app
```

---

## ⚠️ Notas Importantes

### Plan Gratuito de ngrok

- ✅ **Dominio público** funcional
- ✅ **HTTPS** incluido
- ⚠️ **La URL cambia** cada vez que reinicias ngrok
- ⚠️ **Límite**: 40 conexiones/minuto (suficiente para demos y tareas)

### Mantener ngrok Activo

Para que la URL siga funcionando:

1. **NO cierres** la terminal de ngrok
2. **NO cierres** la terminal de la aplicación (mvn spring-boot:run)
3. Si cierras ngrok, la URL deja de funcionar

### Obtener URL Estática (Opcional - Requiere Plan Pago)

Si necesitas una URL que no cambie:

- Plan Personal: $8/mes → URL estática personalizada
- No necesario para esta tarea académica

---

## 🎥 Paso 9: Grabar el Video Demo

Una vez que tengas la URL pública funcionando:

1. **Abre OBS Studio** o **Loom** o **Grabadora de Windows** (Win + G)

2. **Graba** mostrando:
   - URL pública de ngrok funcionando
   - Login y registro de usuario
   - CRUD de recetas (crear, listar, editar, eliminar)
   - Búsqueda de recetas
   - Headers de seguridad (F12 → Network → Headers)
   - Explicación del informe OWASP
   - Mostrar código de `WebSecurityConfig.java`

3. **Duración**: 5-10 minutos

4. **Sube** a YouTube o Google Drive

5. **Actualiza** README línea 268 con el link del video

---

## 🆘 Solución de Problemas

### Error: "command not found: ngrok"

```powershell
# Reinicia la terminal PowerShell y vuelve a intentar
$env:Path = [System.Environment]::GetEnvironmentVariable("Path","User")
ngrok version
```

### Error: "authentication required"

```powershell
# Verifica que agregaste el authtoken
ngrok config check
# Si no aparece, agrega el token nuevamente
ngrok config add-authtoken TU_TOKEN_AQUI
```

### Error: "port 8080 already in use"

```powershell
# Mata los procesos en puerto 8080
Get-Process -Id (Get-NetTCPConnection -LocalPort 8080).OwningProcess | Stop-Process -Force
# Reinicia la aplicación
cd "c:\Users\Kath Stark\sc-s2\sc-s2\demo"
mvn spring-boot:run
```

### La URL de ngrok no carga

1. Verifica que la aplicación esté corriendo: <http://localhost:8080>
2. Verifica que ngrok esté corriendo (terminal no cerrada)
3. Copia la URL correcta (debe ser `https://` no `http://`)

---

## 📊 Estado del Proyecto Después de ngrok

| Tarea | Estado | Progreso |
|-------|--------|----------|
| ✅ Aplicación Java 21 + Spring Boot | Completo | 100% |
| ✅ Informe OWASP ZAP | Completo | 100% |
| ⏳ **URL pública con ngrok** | **En proceso** | 50% |
| ⏳ Video demo | Pendiente | 0% |
| ⏳ README actualizado | Pendiente | 0% |

**Completado**: ~85% del proyecto

---

## 🎯 Siguiente Paso

Una vez que tengas ngrok funcionando y la URL pública, avísame y te ayudo a:

1. ✅ Actualizar el README con la URL
2. 🎥 Crear guión para el video demo
3. 📤 Hacer el commit final con todos los cambios

---

**Elaborado por**: GitHub Copilot + Grupo 8  
**Fecha**: 21 de noviembre de 2025  
**Versión**: 1.0
