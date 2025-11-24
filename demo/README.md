# Backend - API de Recetas

API REST desarrollada con Spring Boot 3.5.8 y Java 21 para gestión de recetas.

## Características

- API REST con autenticación JWT
- Endpoints para recetas, comentarios y valoraciones
- Base de datos MySQL
- Spring Security para autenticación
- Spring Data JPA para persistencia

## Requisitos

- Java 21
- Maven 3.9+
- MySQL 8.0

## Ejecución Local

```bash
# Compilar el proyecto
mvn clean package

# Ejecutar
mvn spring-boot:run
```

La API estará disponible en `http://localhost:8080`

## Ejecución en Servidor

Usar el script `start-backend.sh` que configura las variables de entorno necesarias:

```bash
./start-backend.sh
```

## Endpoints Principales

- `POST /api/auth/login` - Login de usuarios
- `POST /api/auth/register` - Registro de usuarios
- `GET /api/recetas` - Listar recetas (público)
- `POST /api/recetas` - Crear receta (requiere JWT)
- `GET /api/recetas/{id}` - Ver detalle de receta

## Base de Datos

Configurar en `application-prod.properties`:

- Host: localhost
- Puerto: 3306
- Base de datos: mydatabase
- Usuario: recetas_user
- Contraseña: (configurar en variables de entorno)

Crear archivo `.env` en la raíz del proyecto (ya está en `.gitignore`):

```properties
DB_HOST=localhost
DB_PORT=3306
DB_NAME=mydatabase
DB_USERNAME=myuser
DB_PASSWORD=mypassword
```

## Ejecución

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

## Estructura del Proyecto

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

## Seguridad

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

## Testing

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

- **SecurityConfigTest**: Rutas públicas/privadas, login/logout, CSRF, headers de seguridad
- **SecurityConfigTest**: Rutas públicas/privadas, login/logout, CSRF, headers de seguridad
- **RecetaServiceTest**: CRUD de recetas, búsquedas, validaciones

### Testing de APIs con Postman

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

- **Login JWT** → Autentica y guarda token (3 tests)
- **Buscar Recetas** → Búsqueda pública sin autenticación
- **Compartir Receta** → POST con JWT (3 tests: valida status 200, url, texto)
- **Agregar Comentario** → POST con JWT (2 tests: acepta 200 ó 201 - idempotente)
- **Valorar Receta** → POST con JWT (2 tests: valida rating 1-5)
- **Agregar Multimedia** → POST con JWT (3 tests: valida url y tipo foto/video)

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

## Despliegue

### Despliegue con Docker (Recomendado)

Este proyecto incluye configuración completa de Docker lista para producción.

#### Archivos Docker Incluidos

- `Dockerfile` - Imagen multi-stage optimizada de Spring Boot
- `docker-compose.yml` - Orquestación de Spring Boot + MySQL
- `deploy.sh` - Script automatizado de despliegue en VM Linux
- `test-docker.sh` - Script de prueba local
- `DEPLOYMENT.md` - Guía completa paso a paso
- `.dockerignore` - Optimización del build

#### Despliegue Rápido en VM

```bash
# 1. Clonar repositorio en la VM
git clone https://github.com/Kath-Valenzula/grupo_8.git
cd grupo_8/sc-s2/demo

# 2. Ejecutar script de despliegue automatizado
chmod +x deploy.sh
sudo ./deploy.sh
```

El script automáticamente:

- Instala Docker y Docker Compose
- Configura el firewall (puertos 80, 443, 22)
- Construye las imágenes
- Levanta MySQL + Spring Boot
- Muestra la URL de acceso público

**URL de producción actual**: `http://149.112.142.245/recetas`

**Acceso a la aplicación:**

```text
http://149.112.142.245/recetas
```

#### Testing Local con Docker

Antes de desplegar en producción, prueba localmente:

```bash
# Ejecutar tests locales
chmod +x test-docker.sh
./test-docker.sh

# Ver logs en tiempo real
docker compose logs -f app

# Reiniciar servicios
docker compose restart

# Detener servicios
docker compose down
```

#### Requisitos de la VM

- **SO**: Ubuntu 20.04 LTS o superior
- **RAM**: Mínimo 2GB (recomendado 4GB)
- **CPU**: Mínimo 2 cores
- **Disco**: Mínimo 20GB
- **Red**: IP pública con puertos 80, 443, 22 abiertos

**Proveedores recomendados:**

- AWS EC2 (t2.medium)
- Azure VM (Standard B2s)
- Google Cloud (e2-medium)
- DigitalOcean Droplet
- Oracle Cloud Always Free

#### Documentación Completa

Ver **[DEPLOYMENT.md](./DEPLOYMENT.md)** para:

- Configuración detallada de VM en AWS/Azure/GCP
- Configuración de Security Groups y firewall
- Despliegue manual paso a paso
- Configuración SSL/HTTPS con Let's Encrypt
- Troubleshooting y solución de problemas
- Comandos útiles de Docker
- Configuración de backups automáticos

---

### Despliegue Tradicional (Sin Docker)

Si prefieres desplegar sin Docker, sigue estos pasos:

1. Instalar MySQL y crear base de datos:

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

### URL Pública de Producción

**Acceso a la aplicación desplegada en Oracle Cloud**:

```text
http://149.112.142.245/recetas
```

> **Nota**: Esta VM está desplegada en Oracle Cloud (Always Free tier) en la región de Santiago, Chile.
> La IP es pública y permanente. Para más detalles técnicos, ver [`infra/VM_INFO.md`](./infra/VM_INFO.md).

---

## Notas Adicionales

- **Perfil dev vs prod**: En desarrollo usa `secure=false` para cookies (HTTP local), en producción usa `secure=true` (HTTPS)
- **Inicialización de BD**: En dev usa `ddl-auto=create` (recrea tablas), en prod usa `validate` (solo valida esquema)
- **Logs**: En producción los logs están en nivel `WARN`, en desarrollo en `DEBUG`

## Autores

- Proyecto desarrollado para **ISY2202 - Seguridad y Calidad en el Desarrollo de Software**
- **Institución**:DUOC UC
- **Grupo**: 8

## Licencia

Este proyecto es parte de una evaluación académica.
