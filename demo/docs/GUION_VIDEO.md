# 🎥 Guión para Video Demo - Sistema de Gestión de Recetas

**Duración objetivo**: 5-10 minutos  
**Fecha**: 21 de noviembre de 2025  
**URL pública**: <https://complementarily-foundrous-carmon.ngrok-free.dev>

---

## 📋 Preparación (Antes de Grabar)

### Verificar que todo funcione

- [ ] Aplicación corriendo: `http://localhost:8080`
- [ ] ngrok activo con URL pública funcionando
- [ ] Base de datos MySQL con datos de ejemplo
- [ ] Navegador en modo incógnito (para login limpio)
- [ ] Herramientas de desarrollo (F12) disponibles

### Software de Grabación

Opciones (elige una):

1. **OBS Studio** (gratis): <https://obsproject.com/>
2. **Loom** (gratis): <https://www.loom.com/>
3. **Windows Game Bar**: Win + G
4. **ShareX** (gratis): <https://getsharex.com/>

---

## 🎬 Estructura del Video (5-10 minutos)

### Introducción (30 segundos)

```text
"Hola, soy [Tu nombre] del Grupo 8. Les presento nuestro proyecto del curso 
ISY2202: Sistema de Gestión de Recetas, una aplicación web desarrollada con 
Spring Boot y Spring Security que implementa las mejores prácticas de 
seguridad según el OWASP Top 10 2021."
```

**Mostrar**:

- Pantalla inicial con logo/título del proyecto
- Tu nombre y grupo

---

### Parte 1: Demostración de la URL Pública (1 minuto)

```text
"La aplicación está desplegada públicamente usando ngrok en la siguiente URL..."
```

**Acciones**:

1. Mostrar la URL pública en el navegador
2. Mostrar la página de advertencia de ngrok
3. Click en "Visit Site"
4. Mostrar página de inicio (`/home`)

**Mencionar**:

- "La aplicación está accesible desde cualquier lugar con conexión a internet"
- "Ngrok proporciona HTTPS automáticamente para conexiones seguras"

---

### Parte 2: Funcionalidades - Registro y Autenticación (1.5 minutos)

```text
"Comenzamos mostrando el sistema de autenticación seguro..."
```

**Acciones**:

1. Click en "Registrarse"
2. Crear usuario nuevo:
   - Usuario: `demo_grupo8`
   - Email: `demo@grupo8.cl`
   - Password: `SecurePass123`
3. Enviar formulario
4. Mostrar redirección al login
5. Iniciar sesión con el usuario creado

**Mencionar**:

- "Las contraseñas se almacenan con hash BCrypt"
- "Validación de entrada con Bean Validation"
- "Protección CSRF habilitada en formularios"

---

### Parte 3: CRUD de Recetas (2-3 minutos)

```text
"Una vez autenticados, podemos gestionar recetas..."
```

**Acciones**:

#### Crear Receta

1. Click en "Nueva Receta"
2. Llenar formulario:
   - Nombre: `Pastel de Choclo`
   - Tipo: `Chilena`
   - Ingredientes: `Choclo, carne, pollo, cebolla...`
   - Instrucciones: `1. Preparar pino... 2. Moler choclo...`
   - Tiempo: `90 min`
   - Popular: ✅
3. Guardar

#### Listar Recetas

1. Ver lista completa
2. Mostrar recetas populares

#### Buscar Recetas

1. Buscar por nombre: `Pastel`
2. Buscar por tipo de cocina: `Chilena`

#### Editar Receta

1. Click en "Editar" de una receta
2. Modificar tiempo de preparación
3. Guardar cambios

#### Eliminar Receta (opcional)

1. Click en "Eliminar"
2. Confirmar eliminación

**Mencionar**:

- "CRUD completo implementado"
- "Validación de datos en backend"
- "Spring Data JPA con consultas parametrizadas previene SQL Injection"

---

### Parte 4: Seguridad - Headers HTTP (1.5 minutos)

```text
"Ahora veamos las medidas de seguridad implementadas..."
```

**Acciones**:

1. Abrir DevTools (F12)
2. Ir a pestaña "Network"
3. Recargar página principal
4. Click en cualquier request (ej: `/home`)
5. Mostrar "Response Headers"

**Headers a destacar**:

```text
Content-Security-Policy: default-src 'self'; script-src 'self' 'unsafe-inline' ...
Strict-Transport-Security: max-age=31536000; includeSubDomains
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 1; mode=block
```

**Mencionar**:

- "Content Security Policy previene XSS"
- "HSTS fuerza conexiones HTTPS"
- "X-Frame-Options previene clickjacking"
- "Headers configurados en Spring Security"

---

### Parte 5: Seguridad - Cookies de Sesión (1 minuto)

```text
"Revisemos la gestión segura de sesiones..."
```

**Acciones**:

1. En DevTools, ir a "Application" → "Cookies"
2. Mostrar cookie `JSESSIONID`
3. Expandir detalles

**Propiedades a destacar**:

```text
HttpOnly: ✅ (previene acceso desde JavaScript)
Secure: ✅ (solo en HTTPS en producción)
SameSite: Strict (previene CSRF)
```

**Mencionar**:

- "Sesiones stateful con Spring Security"
- "Cookies con flags de seguridad"
- "Logout invalida sesión completamente"

---

### Parte 6: Informe OWASP ZAP (1.5 minutos)

```text
"Realizamos un análisis de seguridad con OWASP ZAP..."
```

**Acciones**:

1. Abrir `demo/docs/INFORME_OWASP.md` o reporte HTML
2. Mostrar resumen ejecutivo:
   - 0 vulnerabilidades altas ✅
   - 1 vulnerabilidad media (CSP - mitigada) ✅
   - 3 alertas informativas ℹ️
3. Scroll rápido por secciones importantes

**Mencionar**:

- "Escaneo automatizado con ZAP"
- "Sin vulnerabilidades críticas"
- "CSP configurada correctamente"
- "Cumplimiento OWASP Top 10 2021"

---

### Parte 7: Código - WebSecurityConfig (1 minuto)

```text
"Veamos la configuración de seguridad en el código..."
```

**Acciones**:

1. Abrir `src/main/java/com/demo/demo/WebSecurityConfig.java`
2. Scroll mostrando:
   - Configuración de CSP (líneas 23-34)
   - Headers de seguridad
   - Rutas públicas vs protegidas (líneas 36-40)
   - Configuración de logout (líneas 46-52)
   - BCryptPasswordEncoder (línea 62)

**Mencionar**:

- "Spring Security 6.x"
- "Configuración declarativa con lambda DSL"
- "Separación de rutas públicas y protegidas"
- "Encriptación BCrypt con factor de trabajo por defecto"

---

### Conclusión (30 segundos)

```text
"En resumen, desarrollamos una aplicación web segura que implementa:
- Autenticación robusta con Spring Security
- Protección contra OWASP Top 10
- Headers de seguridad HTTP completos
- Gestión segura de sesiones
- Validación de entrada
- Y un análisis de seguridad completo con OWASP ZAP

Todo el código está disponible en GitHub. Gracias por su atención."
```

**Mostrar**:

- Pantalla final con:
  - URL del repositorio: <https://github.com/Kath-Valenzula/grupo_8>
  - Tecnologías usadas: Java 21, Spring Boot 3.5.8, Spring Security, MySQL
  - Grupo 8 - ISY2202

---

## ✅ Checklist Post-Grabación

Antes de subir el video:

- [ ] Duración: 5-10 minutos ✓
- [ ] Audio claro y sin ruido de fondo
- [ ] Pantalla visible (resolución mínima 720p)
- [ ] Todas las funcionalidades mostradas
- [ ] Seguridad explicada claramente
- [ ] Sin información sensible visible (contraseñas reales, tokens, etc.)

---

## 📤 Subir el Video

### Opción 1: YouTube (Recomendado)

1. Ir a: <https://studio.youtube.com>
2. Click en "Crear" → "Subir videos"
3. Arrastrar archivo
4. Título: `Sistema de Gestión de Recetas - Grupo 8 - ISY2202 - S3`
5. Descripción:

   ```text
   Demostración del proyecto Sistema de Gestión de Recetas
   Curso: ISY2202 - Seguridad y Calidad en el Desarrollo de Software
   Grupo: 8
   Fecha: Noviembre 2025

   Tecnologías:
   - Java 21 LTS
   - Spring Boot 3.5.8
   - Spring Security 6.x
   - MySQL 8.0
   - OWASP ZAP

   Repositorio: https://github.com/Kath-Valenzula/grupo_8

   Características de seguridad:
   - Autenticación con BCrypt
   - Content Security Policy
   - Headers HTTP seguros
   - Protección CSRF
   - Validación de entrada
   - Cumplimiento OWASP Top 10 2021
   ```

6. Visibilidad: **No listado** (solo personas con el enlace)
7. Click en "Publicar"
8. **Copiar URL del video**

### Opción 2: Google Drive

1. Ir a: <https://drive.google.com>
2. Subir video
3. Click derecho → "Compartir"
4. Cambiar a "Cualquiera con el enlace"
5. **Copiar enlace**

---

## 📝 Actualizar README con el Video

Una vez subido el video:

1. Abrir `demo/README.md`
2. Buscar línea: `[Pendiente - Grabar después de verificar URL pública]`
3. Reemplazar con tu URL de YouTube/Drive

Ejemplo:

```markdown
**🎥 Demostración en video**: https://youtu.be/ABC123XYZ
```

4. Hacer commit:

```powershell
git add demo/README.md
git commit -m "docs: Agregar link del video demo del proyecto"
git push
```

---

## 💡 Consejos para una Buena Grabación

### Audio

- Habla claro y a un ritmo moderado
- Usa micrófono si es posible (no el del laptop)
- Graba en ambiente silencioso
- Practica el guión antes

### Visual

- Resolución mínima: 1280x720 (720p)
- Cierra pestañas/programas innecesarios
- Aumenta tamaño de fuente en editor/terminal
- Usa modo oscuro o claro (el que prefieras)

### Contenido

- Sigue el guión pero sé natural
- Si te equivocas, pausa y reinicia esa sección
- Edita el video si es necesario (cortar partes)
- No necesita ser perfecto, pero sí claro

---

## 🎯 Resultado Final Esperado

Al final del video, quien lo vea debe entender:

✅ Qué hace tu aplicación (gestión de recetas)  
✅ Cómo funciona (demo completa de CRUD)  
✅ Qué medidas de seguridad implementa  
✅ Cómo se validó la seguridad (OWASP ZAP)  
✅ Que la aplicación está desplegada públicamente

---

**Creado por**: GitHub Copilot + Grupo 8  
**Fecha**: 21 de noviembre de 2025  
**Versión**: 1.0
