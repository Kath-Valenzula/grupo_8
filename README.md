# Proyecto Spring Boot

## Requisitos

- Java 21 LTS
- Maven 3.9+
- Docker (para MySQL)

## Ejecutar para iniciar MySQL

1. Construir la imagen de Docker:
```bash
docker build -t my-mysql-db .
```

2. Ejecutar el contenedor:
```bash
docker run -d \
  --name mysql-container \
  -p 3306:3306 \
  -p 33060:33060 \
  my-mysql-db
```
