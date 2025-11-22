# 🍴 Recetas Pro - Sistema de Gestión de Recetas

Aplicación web desarrollada con **Spring Boot 3.5.8** y **Java 21 LTS** para la gestión y consulta de recetas de cocina. Implementa autenticación basada en sesiones (stateful) con **Spring Security** y vistas dinámicas con **Thymeleaf**.

## 📋 Tabla de Contenidos

- [✨ Características](#-características)
- [🏗️ Arquitectura](#️-arquitectura)
- [📦 Requisitos](#-requisitos)
- [🚀 Instalación y Configuración](#-instalación-y-configuración)
- [▶️ Ejecución](#️-ejecución)
- [📁 Estructura del Proyecto](#-estructura-del-proyecto)
- [🔒 Seguridad](#-seguridad)
- [🧪 Testing](#-testing)
- [🌐 Despliegue](#-despliegue)

## ✨ Características

- 🔐 **Autenticación y Autorización**: Sistema de registro/login con Spring Security + JWT para APIs
- 📱 **Interfaz Responsive**: Diseño adaptable con Bootstrap 5
- 🔍 **Búsqueda de Recetas**: Filtros por nombre, tipo de cocina, ingredientes, etc.
- 💬 **Comentarios y Valoraciones**: Sistema de comentarios y rating (1-5 estrellas) protegido con JWT
- 📸 **Multimedia**: Subir fotos y videos a recetas (API REST con autenticación)
- 🔗 **Compartir en Redes Sociales**: Botones para compartir recetas en Facebook, Twitter/X y WhatsApp
- ✅ **Validación de Formularios**: Bean Validation en backend + feedback visual en frontend
- 🛡️ **Headers de Seguridad**: HSTS, CSP, XSS Protection, X-Content-Type-Options
- 🍪 **Gestión de Sesiones**: Cookies seguras con HttpOnly y SameSite
- 📊 **Cobertura de Código**: Integración con JaCoCo
- 🔒 **Análisis de Vulnerabilidades**: OWASP Dependency Check

## 🏗️ Arquitectura

### Stack Tecnológico

- **Backend**: Spring Boot 3.5.8 (Spring MVC, Spring Security, Spring Data JPA)
- **Frontend**: Thymeleaf + Bootstrap 5 + JavaScript
- **Base de Datos**: MySQL 8.0
- **Autenticación**: Basada en sesiones (JSESSIONID) - **NO JWT**
- **Build Tool**: Maven 3.9+
- **Java**: 21 LTS

### Arquitectura de Seguridad

```text
Usuario → Thymeleaf (CSRF) → Spring Security (Session) → Controllers → Services → JPA → MySQL
                                     ↓
                            WebSecurityConfig
                                     ↓
                          - Form Login (stateful)
                          - Session Management
                          - Security Headers
                          - CSRF Protection
```

**Características de Seguridad:**

- Arquitectura **stateful** con gestión de sesiones HTTP
- Cookies `JSESSIONID` con flags `HttpOnly`, `Secure` (prod), `SameSite=Strict`
- Protección CSRF habilitada en todos los formularios POST
- Contraseñas hasheadas con BCrypt
- Headers de seguridad HTTP configurados (HSTS, CSP, XSS-Protection)

## 📦 Requisitos

- **Java**: 21 LTS o superior ([descargar](https://www.oracle.com/java/technologies/downloads/#java21))
- **Maven**: 3.9+ ([descargar](https://maven.apache.org/download.cgi))
- **Docker**: Para MySQL ([descargar](https://www.docker.com/))
- **MySQL**: 8.0+ (puede ejecutarse con Docker)

## 🚀 Instalación y Configuración

### 1. Clonar el repositorio

```bash
git clone https://github.com/Kath-Valenzula/grupo_8.git
cd grupo_8/demo
```

### 2. Configurar Base de Datos MySQL con Docker

```bash
# Construir imagen de MySQL
docker build -t my-mysql-db .

# Ejecutar contenedor
docker run -d \
  --name mysql-container \
  -p 3306:3306 \
  -p 33060:33060 \
  my-mysql-db
```

**Credenciales por defecto** (definidas en `Dockerfile`):

- Usuario: `myuser`
- Contraseña: `mypassword`
- Base de datos: `mydatabase`

### 3. Configurar Variables de Entorno (Opcional)

Crear archivo `.env` en la raíz del proyecto (ya está en `.gitignore`):

```properties
DB_HOST=localhost
DB_PORT=3306
DB_NAME=mydatabase
DB_USERNAME=myuser
DB_PASSWORD=mypassword
```

## ▶️ Ejecución

### Modo Desarrollo (con perfiles Spring)

```bash
# Ejecutar con perfil development (cookies sin secure flag)
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Modo Producción

```bash
# Compilar el proyecto
mvn clean package

# Ejecutar JAR con perfil production
java -jar target/demo-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

La aplicación estará disponible en: **<http://localhost:8080>**

### Usuarios por defecto

Los usuarios se crean en `data.sql`. Ejemplo:

- Usuario: `admin` / Contraseña: `admin123`
- Usuario: `user` / Contraseña: `user123`

## 📁 Estructura del Proyecto

```text
demo/
├── src/
│   ├── main/
│   │   ├── java/com/demo/demo/
│   │   │   ├── controller/          # Controladores MVC
│   │   │   │   ├── LoginController.java
│   │   │   │   ├── RegisterController.java
│   │   │   │   └── RecetasController.java
│   │   │   ├── exception/           # Manejo global de errores
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   ├── models/              # Entidades JPA
│   │   │   │   ├── User.java
│   │   │   │   └── Receta.java
│   │   │   ├── repository/          # Repositorios Spring Data
│   │   │   ├── service/             # Lógica de negocio
│   │   │   └── WebSecurityConfig.java  # Configuración de seguridad
│   │   └── resources/
│   │       ├── application.properties          # Config base
│   │       ├── application-dev.properties      # Config desarrollo
│   │       ├── application-prod.properties     # Config producción
│   │       ├── data.sql                        # Datos iniciales
│   │       ├── static/                         # CSS, JS, imágenes
│   │       └── templates/                      # Vistas Thymeleaf
│   │           ├── home.html
│   │           ├── login.html
│   │           ├── register.html
│   │           ├── recetas.html
│   │           ├── error.html
│   │           └── fragments/
│   └── test/                        # Tests unitarios e integración
│       └── java/com/demo/demo/
│           ├── controller/SecurityConfigTest.java
│           └── service/RecetaServiceTest.java
├── Dockerfile                       # Imagen MySQL
├── pom.xml                          # Dependencias Maven
└── README.md
```

## 🔒 Seguridad

### Configuración de Spring Security

- **Rutas públicas**: `/`, `/home`, `/recetas`, `/login`, `/register`, `/css/**`, `/js/**`, `/img/**`

- **Rutas protegidas**: Todas las demás requieren autenticación
- **Login**: Form-based login en `/login`
- **Logout**: POST a `/logout` (invalida sesión y elimina cookie)
- **Sesiones**: Máximo 1 sesión concurrente por usuario

### Headers de Seguridad HTTP

```http
Content-Security-Policy: default-src 'self'; script-src 'self' 'unsafe-inline' ...
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
X-Frame-Options: DENY
Strict-Transport-Security: max-age=31536000; includeSubDomains
```

### Análisis de Vulnerabilidades

```bash
# Ejecutar OWASP Dependency Check
mvn dependency-check:check

# Ver reporte en: target/dependency-check-report.html
```

## 🧪 Testing

### Ejecutar todos los tests

```bash
mvn test
```

### Ejecutar con reporte de cobertura (JaCoCo)

```bash
mvn clean test jacoco:report

# Ver reporte en: target/site/jacoco/index.html
```

### Tests implementados

- ✅ **SecurityConfigTest**: Rutas públicas/privadas, login/logout, CSRF, headers de seguridad
- ✅ **RecetaServiceTest**: CRUD de recetas, búsquedas, validaciones

### 🔌 Testing de APIs con Postman

El proyecto incluye una colección de Postman (`New Collection.postman_collection.json`) con tests automatizados para todas las APIs protegidas con JWT.

**Credenciales de prueba:**

- **Usuario**: `juanperez`
- **Password**: `password123`

**Ejecutar tests en Postman:**

1. Importar la colección `New Collection.postman_collection.json` en Postman
2. Ejecutar **"Login JWT"** primero - esto genera y guarda automáticamente el token en `{{jwt_token}}`
3. Ejecutar el resto de requests - todos usan el token guardado automáticamente
4. Ver resultados de tests en la pestaña "Test Results"

**Requests disponibles:**

- ✅ **Login JWT** → Autentica y guarda token (3 tests)
- ✅ **Buscar Recetas** → Búsqueda pública sin autenticación
- ✅ **Compartir Receta** → POST con JWT (3 tests: valida status 200, url, texto)
- ✅ **Agregar Comentario** → POST con JWT (2 tests: acepta 200 ó 201 - idempotente)
- ✅ **Valorar Receta** → POST con JWT (2 tests: valida rating 1-5)
- ✅ **Agregar Multimedia** → POST con JWT (3 tests: valida url y tipo foto/video)

**Tests idempotentes**: Los tests están diseñados para aceptar tanto código 200 (OK) como 201 (Created), permitiendo ejecutar la colección múltiples veces sin contaminar la base de datos con datos duplicados.

**Limpieza manual (opcional)**:

```sql
DELETE FROM multimedia WHERE receta_id = 1;
DELETE FROM valoracion WHERE receta_id = 1;
DELETE FROM comentario WHERE receta_id = 1;
```

**Troubleshooting:**

- Si el login falla (401), verifica que:
  - La aplicación esté corriendo en `http://localhost:8080`
  - El usuario `juanperez` exista con password `password123`
  - El hash BCrypt en `data.sql` sea: `$2a$10$kdPEx8CnOcZCEpJC8OK1ges/Flb11fDNYXNB01iRkyecGni6T0WTu`

## 🌐 Despliegue

### Despliegue en VM con Docker Compose

1. Crear archivo `docker-compose.yml`:

```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: rootpassword
      MYSQL_DATABASE: mydatabase
      MYSQL_USER: myuser
      MYSQL_PASSWORD: mypassword
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql

  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      SPRING_PROFILES_ACTIVE: prod
      DB_HOST: mysql
      DB_PORT: 3306
      DB_NAME: mydatabase
      DB_USERNAME: myuser
      DB_PASSWORD: mypassword
    depends_on:
      - mysql

volumes:
  mysql_data:
```

1. Ejecutar:

```bash
docker-compose up -d
```

### URL Pública

**🔗 Acceso a la aplicación desplegada**:

```text
https://complementarily-foundrous-carmon.ngrok-free.dev/recetas
```

> **Nota**: Esta URL pública está activa mediante ngrok. Para acceder:
>
> 1. Copia la URL completa
> 2. Pégala en tu navegador
> 3. Click en "Visit Site" (ngrok muestra una advertencia de seguridad la primera vez)
> 4. ¡Disfruta de la aplicación!

### Video Demo

**🎥 Demostración en video**: [Pendiente - Grabar después de verificar URL pública]

---

## 📝 Notas Adicionales

- **Perfil dev vs prod**: En desarrollo usa `secure=false` para cookies (HTTP local), en producción usa `secure=true` (HTTPS)
- **Inicialización de BD**: En dev usa `ddl-auto=create` (recrea tablas), en prod usa `validate` (solo valida esquema)
- **Logs**: En producción los logs están en nivel `WARN`, en desarrollo en `DEBUG`

## 👥 Autores

- Proyecto desarrollado para **ISY2202 - Seguridad y Calidad en el Desarrollo de Software**
- **Institución**: [Tu institución]
- **Grupo**: 8

## 📄 Licencia

Este proyecto es parte de una evaluación académica.
