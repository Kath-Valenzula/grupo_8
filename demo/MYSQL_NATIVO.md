# 🔧 Configurar MySQL Nativo para la Aplicación

**Problema Detectado**: Puerto 3306 ocupado por MySQL nativo (proceso 6152)
**Solución**: Usar MySQL instalado en lugar del contenedor Docker

---

## ✅ Paso 1: Conectar MySQL Workbench

1. **En MySQL Workbench**, haz clic en el botón **"+"** junto a "MySQL Connections"

2. **Configura la conexión**:
   - **Connection Name**: `Localhost Recetas`
   - **Hostname**: `localhost`
   - **Port**: `3306`
   - **Username**: `root` (o el usuario que tengas configurado)
   - **Password**: Click en "Store in Vault" y pon tu contraseña de root

3. **Click en "Test Connection"** - Debe decir "Successfully connected"

4. **Click en "OK"** para guardar la conexión

5. **Haz doble clic** en la nueva conexión para abrirla

---

## 🗄️ Paso 2: Crear Usuario y Base de Datos

Una vez conectado en MySQL Workbench, ejecuta estos comandos en el Query tab:

```sql
-- 1. Crear base de datos
CREATE DATABASE IF NOT EXISTS mydatabase CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 2. Crear usuario con contraseña
CREATE USER IF NOT EXISTS 'myuser'@'localhost' IDENTIFIED BY 'mypassword';
CREATE USER IF NOT EXISTS 'myuser'@'%' IDENTIFIED BY 'mypassword';

-- 3. Dar permisos completos al usuario
GRANT ALL PRIVILEGES ON mydatabase.* TO 'myuser'@'localhost';
GRANT ALL PRIVILEGES ON mydatabase.* TO 'myuser'@'%';

-- 4. Aplicar cambios
FLUSH PRIVILEGES;

-- 5. Verificar que se creó correctamente
USE mydatabase;
SHOW TABLES;
```

**Resultado Esperado**:

```text
Database changed
Empty set (0.00 sec)
```

Esto es normal - la base está vacía porque Spring Boot creará las tablas automáticamente.

---

## 🚀 Paso 3: Iniciar la Aplicación

Ahora que MySQL está corriendo con la base de datos lista:

```powershell
# 1. Navegar al proyecto
cd "c:\Users\Kath Stark\sc-s2\sc-s2\demo"

# 2. Iniciar la aplicación
mvn spring-boot:run
```

**Espera a ver**:

```text
Started DemoApplication in X.XXX seconds (JVM running for X.XXX)
```

✅ **La aplicación está corriendo!**

---

## 🧪 Paso 4: Verificar que TODO Funciona

### Test 1: Verificar Aplicación Web

Abre tu navegador en: **<http://localhost:8080/home>**

**Deberías ver**:

- Carousel con 3 imágenes (banner1, banner2, banner3) ✅
- Botón "Iniciar sesión"
- Lista de recetas populares

### Test 2: Verificar Imágenes

Abre directamente: **<http://localhost:8080/img/banner1.png>**

**Deberías ver**: La imagen del banner cargando directamente

### Test 3: Verificar Login

1. Click en "Iniciar sesión"
2. **Usuario**: `juanperez`
3. **Contraseña**: `password123`
4. Click en "Entrar"

**Resultado**: Deberías ver la página de inicio logueado

### Test 4: Verificar JWT API

Abre **Postman** o ejecuta:

```powershell
curl -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d '{\"username\":\"juanperez\",\"password\":\"password123\"}'
```

**Respuesta Esperada**:

```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "username": "juanperez",
  "email": "juan.perez@example.com"
}
```

---

## 🔍 Verificar Tablas en MySQL Workbench

Después de iniciar la aplicación por primera vez:

```sql
USE mydatabase;
SHOW TABLES;
```

**Deberías ver**:

```text
+----------------------+
| Tables_in_mydatabase |
+----------------------+
| comentarios          |
| multimedia           |
| recetas              |
| users                |
| valoraciones         |
+----------------------+
5 rows in set (0.00 sec)
```

✅ **Spring Boot creó todas las tablas automáticamente!**

---

## 📊 Ver Datos de Prueba

Spring Boot carga datos iniciales desde `data.sql`:

```sql
-- Ver usuarios
SELECT id, username, email, role FROM users;

-- Ver recetas
SELECT id, nombre, tiempo_preparacion, dificultad, popular FROM recetas LIMIT 5;

-- Ver comentarios (vacío al inicio)
SELECT COUNT(*) as total_comentarios FROM comentarios;

-- Ver valoraciones (vacío al inicio)
SELECT COUNT(*) as total_valoraciones FROM valoraciones;
```

---

## 🐛 Troubleshooting

### Error: "Access denied for user 'myuser'@'localhost'"

**Solución**: Vuelve a ejecutar los comandos de creación de usuario en Paso 2

```sql
DROP USER IF EXISTS 'myuser'@'localhost';
DROP USER IF EXISTS 'myuser'@'%';

CREATE USER 'myuser'@'localhost' IDENTIFIED BY 'mypassword';
CREATE USER 'myuser'@'%' IDENTIFIED BY 'mypassword';

GRANT ALL PRIVILEGES ON mydatabase.* TO 'myuser'@'localhost';
GRANT ALL PRIVILEGES ON mydatabase.* TO 'myuser'@'%';
FLUSH PRIVILEGES;
```

### Error: "Unknown database 'mydatabase'"

**Solución**: Crear la base de datos

```sql
CREATE DATABASE mydatabase CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE mydatabase;
```

### Error: "Can't connect to MySQL server on 'localhost:3306'"

**Verificar que MySQL está corriendo**:

```powershell
Get-Process -Name mysqld -ErrorAction SilentlyContinue
```

Si NO aparece nada, inicia MySQL desde Servicios de Windows:

```powershell
Start-Service MySQL80
```

O desde MySQL Workbench: Server → Start Server

### Las imágenes NO cargan (404)

**Verificar recursos estáticos**:

```powershell
# Listar imágenes en target/classes
Get-ChildItem "c:\Users\Kath Stark\sc-s2\sc-s2\demo\target\classes\static\img"

# Deberías ver:
# banner1.png
# banner1.webp  
# banner2.png
# banner3.png
```

Si NO existen, recompilar:

```powershell
mvn clean compile
```

---

## ✅ Checklist de Verificación

- [ ] MySQL Workbench conectado a `localhost:3306`
- [ ] Base de datos `mydatabase` creada
- [ ] Usuario `myuser` con contraseña `mypassword` creado
- [ ] Permisos otorgados correctamente
- [ ] Aplicación iniciada con `mvn spring-boot:run`
- [ ] Mensaje "Started DemoApplication" visible
- [ ] <http://localhost:8080/home> carga correctamente
- [ ] Imágenes del carousel se ven
- [ ] Login funciona con `juanperez` / `password123`
- [ ] API JWT responde en `/api/auth/login`
- [ ] Tablas creadas en MySQL (users, recetas, comentarios, valoraciones, multimedia)

---

## 🎯 Próximos Pasos

Una vez que TODO lo anterior funcione:

1. ✅ Commit de cambios corregidos
2. 🚀 Despliegue con ngrok (ver GUIA_NGROK.md)
3. 🎥 Grabar video demo
4. 📤 Push final al repositorio

---

**Fecha**: 21 de noviembre de 2025  
**Estado**: MySQL nativo detectado en puerto 3306  
**Elaborado por**: Grupo 8
