# 🚀 Guía de Despliegue en Máquina Virtual

Esta guía te ayudará a desplegar la aplicación Recetas en una VM con acceso público usando Docker.

## 📋 Requisitos Previos

### 1. Máquina Virtual
- **SO**: Ubuntu 20.04 LTS o superior (también funciona con Debian/CentOS)
- **RAM**: Mínimo 2GB (recomendado 4GB)
- **CPU**: Mínimo 2 cores
- **Disco**: Mínimo 20GB
- **Red**: IP pública accesible

### 2. Proveedores Recomendados
- **AWS EC2** (t2.medium o superior)
- **Azure VM** (Standard B2s o superior)
- **Google Cloud Compute Engine** (e2-medium o superior)
- **DigitalOcean Droplet** ($12/mes)
- **Oracle Cloud** (Always Free tier - ARM)

## 🔧 Configuración Inicial de la VM

### Paso 1: Conectarse a la VM

```bash
# Conectar vía SSH (reemplaza con tu IP y usuario)
ssh usuario@IP_PUBLICA

# O si usas archivo de clave
ssh -i mi-clave.pem ubuntu@IP_PUBLICA
```

### Paso 2: Configurar Firewall/Security Groups

**En tu proveedor cloud (AWS/Azure/GCP):**
- Abrir puerto **80** (HTTP) - Para acceso web
- Abrir puerto **443** (HTTPS) - Para SSL (futuro)
- Abrir puerto **22** (SSH) - Para administración

**Ejemplo en AWS Security Group:**
```
Type        Protocol    Port Range    Source
SSH         TCP         22            Tu IP / 0.0.0.0/0
HTTP        TCP         80            0.0.0.0/0
HTTPS       TCP         443           0.0.0.0/0
```

## 🚀 Despliegue Automático (Método Recomendado)

### Opción 1: Script Automatizado

```bash
# 1. Descargar y ejecutar script de despliegue
sudo apt-get update
sudo apt-get install -y curl git

# 2. Descargar el repositorio
git clone https://github.com/Kath-Valenzula/grupo_8.git
cd grupo_8/sc-s2/demo

# 3. Dar permisos de ejecución al script
chmod +x deploy.sh

# 4. Ejecutar script de despliegue
sudo ./deploy.sh
```

El script automáticamente:
- ✅ Actualiza el sistema
- ✅ Instala Docker y Docker Compose
- ✅ Configura el firewall
- ✅ Construye las imágenes Docker
- ✅ Inicia los contenedores (MySQL + Spring Boot)
- ✅ Muestra el link de acceso

**Tiempo estimado**: 5-10 minutos

## 🛠️ Despliegue Manual (Paso a Paso)

### Paso 1: Instalar Docker

```bash
# Actualizar sistema
sudo apt-get update
sudo apt-get upgrade -y

# Instalar dependencias
sudo apt-get install -y apt-transport-https ca-certificates curl gnupg lsb-release

# Agregar repositorio oficial de Docker
sudo mkdir -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg

echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

# Instalar Docker
sudo apt-get update
sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

# Verificar instalación
docker --version
docker compose version
```

### Paso 2: Clonar Repositorio

```bash
# Clonar proyecto
cd /opt
sudo git clone https://github.com/Kath-Valenzula/grupo_8.git
cd grupo_8/sc-s2/demo
```

### Paso 3: Configurar Variables de Entorno (Opcional)

```bash
# Crear archivo .env para personalizar configuración
sudo nano .env
```

Contenido del `.env`:
```env
# Database
MYSQL_ROOT_PASSWORD=rootpassword
MYSQL_DATABASE=mydatabase
MYSQL_USER=myuser
MYSQL_PASSWORD=mypassword

# Spring Boot
SPRING_PROFILES_ACTIVE=prod
```

### Paso 4: Construir y Levantar Aplicación

```bash
# Construir imágenes
sudo docker compose build

# Levantar contenedores en segundo plano
sudo docker compose up -d

# Ver logs en tiempo real
sudo docker compose logs -f
```

### Paso 5: Verificar Despliegue

```bash
# Ver estado de contenedores
sudo docker compose ps

# Verificar logs de la aplicación
sudo docker compose logs app

# Verificar logs de MySQL
sudo docker compose logs mysql

# Probar endpoint de salud
curl http://localhost/actuator/health
```

## 🌐 Acceso a la Aplicación

Una vez desplegada, accede a:

```
http://[IP_PUBLICA]/recetas
```

**Ejemplo:**
```
http://54.123.45.67/recetas
```

### Rutas Disponibles

- **Home**: `http://[IP]/recetas`
- **Login**: `http://[IP]/recetas/login`
- **Register**: `http://[IP]/recetas/register`
- **API Pública**: `http://[IP]/api/recetas`
- **Health Check**: `http://[IP]/actuator/health`

## 🔐 Credenciales de Prueba

```
Usuario: juanperez
Password: password123

Usuario: mariagonzalez
Password: password123

Usuario: se.valdivia
Password: password123
```

## 📊 Comandos Útiles de Docker

### Ver estado de contenedores
```bash
sudo docker compose ps
```

### Ver logs en tiempo real
```bash
# Todos los servicios
sudo docker compose logs -f

# Solo aplicación
sudo docker compose logs -f app

# Solo MySQL
sudo docker compose logs -f mysql
```

### Reiniciar servicios
```bash
# Reiniciar todo
sudo docker compose restart

# Reiniciar solo app
sudo docker compose restart app
```

### Detener servicios
```bash
sudo docker compose down
```

### Actualizar aplicación
```bash
# Detener contenedores
sudo docker compose down

# Actualizar código
cd /opt/grupo_8/sc-s2/demo
sudo git pull

# Reconstruir y levantar
sudo docker compose up -d --build
```

### Ver uso de recursos
```bash
sudo docker stats
```

### Limpiar recursos no usados
```bash
sudo docker system prune -f
```

## 🐛 Troubleshooting

### Problema: Contenedor no inicia

```bash
# Ver logs detallados
sudo docker compose logs app

# Verificar que MySQL esté listo
sudo docker compose logs mysql | grep "ready for connections"
```

### Problema: Puerto 80 ocupado

```bash
# Ver qué proceso usa el puerto 80
sudo lsof -i :80

# Detener servicio (ejemplo Apache)
sudo systemctl stop apache2
sudo systemctl disable apache2
```

### Problema: Sin memoria

```bash
# Ver memoria disponible
free -h

# Limpiar cache
sudo sync; echo 3 | sudo tee /proc/sys/vm/drop_caches
```

### Problema: Aplicación no responde

```bash
# Reiniciar solo la aplicación
sudo docker compose restart app

# Ver logs de error
sudo docker compose logs app --tail=100 | grep -i error
```

### Problema: Base de datos no conecta

```bash
# Verificar que MySQL esté corriendo
sudo docker compose ps mysql

# Probar conexión manual
sudo docker compose exec mysql mysql -u myuser -pmypassword mydatabase

# Reiniciar MySQL
sudo docker compose restart mysql
```

## 🔒 Seguridad y Mejores Prácticas

### 1. Cambiar Contraseñas Predeterminadas

Edita `docker-compose.yml` y cambia:
```yaml
environment:
  MYSQL_ROOT_PASSWORD: TU_PASSWORD_SEGURO
  MYSQL_PASSWORD: TU_PASSWORD_SEGURO
```

### 2. Configurar SSL/HTTPS

Instala Nginx como reverse proxy con Let's Encrypt:

```bash
sudo apt-get install -y nginx certbot python3-certbot-nginx

# Configurar certificado SSL
sudo certbot --nginx -d tudominio.com
```

### 3. Configurar Backups Automáticos

```bash
# Crear script de backup
sudo nano /opt/backup-recetas.sh
```

Contenido:
```bash
#!/bin/bash
DATE=$(date +%Y%m%d_%H%M%S)
docker compose exec mysql mysqldump -u root -prootpassword mydatabase > /opt/backups/backup_$DATE.sql
```

### 4. Monitoreo

```bash
# Instalar herramientas de monitoreo
sudo docker run -d -p 9090:9090 prom/prometheus
sudo docker run -d -p 3000:3000 grafana/grafana
```

## 📦 Estructura de Archivos Docker

```
demo/
├── Dockerfile                 # Imagen multi-stage de Spring Boot
├── docker-compose.yml         # Orquestación de servicios
├── deploy.sh                  # Script de despliegue automatizado
├── .dockerignore              # Archivos a excluir del build
└── src/
    └── main/
        └── resources/
            ├── application.properties        # Config desarrollo
            └── application-prod.properties   # Config producción
```

## 🎯 Checklist de Despliegue

- [ ] VM creada con Ubuntu 20.04+
- [ ] Puertos 80, 443, 22 abiertos en Security Group
- [ ] Docker instalado y funcionando
- [ ] Repositorio clonado en `/opt/grupo_8`
- [ ] Contenedores levantados con `docker compose up -d`
- [ ] Health check respondiendo: `curl http://localhost/actuator/health`
- [ ] Acceso público funcionando: `http://[IP]/recetas`
- [ ] Login funcionando con usuarios de prueba
- [ ] APIs públicas accesibles sin JWT
- [ ] APIs privadas requieren JWT (401 sin token)

## 📞 Soporte

Si encuentras problemas:

1. **Revisa logs**: `sudo docker compose logs -f`
2. **Verifica estado**: `sudo docker compose ps`
3. **Reinicia servicios**: `sudo docker compose restart`
4. **Consulta el repositorio**: https://github.com/Kath-Valenzula/grupo_8

## 📚 Referencias

- [Spring Boot Docker Guide](https://spring.io/guides/topicals/spring-boot-docker)
- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [MySQL Docker Image](https://hub.docker.com/_/mysql)

---

**Nota**: Este despliegue está optimizado para evaluación académica. Para producción real, considera configurar HTTPS, backups automáticos, monitoreo y alta disponibilidad.
