# Frontend - Aplicación Web de Recetas

Aplicación web desarrollada con Spring Boot 3.5.8, Java 21 y Thymeleaf.

## Características

- Interfaz web con Thymeleaf y Bootstrap
- Login con autenticación personalizada (CustomLoginFilter)
- Visualización de recetas
- Comentarios y valoraciones
- Integración con API backend

## Requisitos

- Java 21
- Maven 3.9+

## Ejecución Local

```bash
# Compilar el proyecto
mvn clean package

# Ejecutar
mvn spring-boot:run
```

La aplicación estará disponible en `http://localhost:8081`

## Configuración

El frontend se conecta al backend mediante:

```properties
backend.api.base-url=http://localhost:8080
```

## Login

El sistema usa `CustomLoginFilter` y `CustomAuthenticationProvider` para autenticar contra la API backend.

Usuarios de prueba:
- juanperez / password123
- admin / admin123
- user / user123

## Nota sobre Java

El proyecto requiere Java 21. Si tienes Java 17, actualiza la versión en el `pom.xml`.