# 🚀 Guía Rápida de Despliegue en VM

## ✅ Archivos Listos para Despliegue

Tu proyecto está 100% listo para desplegar en una máquina virtual. Se han creado todos los archivos necesarios:

### 📦 Archivos Docker
- ✅ `Dockerfile` - Imagen multi-stage de Spring Boot (build + runtime)
- ✅ `docker-compose.yml` - Orquestación de MySQL + Spring Boot
- ✅ `.dockerignore` - Optimización del build

### 🛠️ Scripts de Automatización
- ✅ `deploy.sh` - Instalación y despliegue automatizado
- ✅ `test-docker.sh` - Prueba local antes de desplegar

### 📚 Documentación
- ✅ `DEPLOYMENT.md` - Guía completa paso a paso
- ✅ `README.md` - Actualizado con sección Docker

## 🎯 Despliegue en 3 Pasos

### 1. Crear VM (AWS/Azure/GCP/DigitalOcean)

**Requisitos mínimos:**
- Ubuntu 20.04 LTS o superior
- 2GB RAM (recomendado 4GB)
- 2 CPU cores
- 20GB disco
- IP pública

**Configurar Security Group/Firewall:**
```
Puerto 80  (HTTP)   - 0.0.0.0/0
Puerto 443 (HTTPS)  - 0.0.0.0/0
Puerto 22  (SSH)    - Tu IP
```

### 2. Conectarse y Ejecutar Script

```bash
# Conectar a la VM vía SSH
ssh ubuntu@TU_IP_PUBLICA

# Clonar repositorio
git clone https://github.com/Kath-Valenzula/grupo_8.git
cd grupo_8/sc-s2/demo

# Ejecutar script de despliegue (instala todo automáticamente)
chmod +x deploy.sh
sudo ./deploy.sh
```

### 3. ¡Listo! Aplicación Desplegada

El script automáticamente:
- ✅ Instala Docker y Docker Compose
- ✅ Configura el firewall
- ✅ Construye las imágenes
- ✅ Levanta MySQL + Spring Boot
- ✅ Te muestra la URL de acceso

**Acceder a tu aplicación:**
```
http://[TU_IP_PUBLICA]/recetas
```

## 🧪 Testing Local (Opcional)

Antes de desplegar en la VM, prueba localmente:

```bash
# En tu máquina con Docker instalado
chmod +x test-docker.sh
./test-docker.sh

# Ver la app en tu navegador
http://localhost/recetas
```

## 📖 Documentación Completa

Ver **[DEPLOYMENT.md](./DEPLOYMENT.md)** para:
- Configuración detallada de VM en cada proveedor
- Despliegue manual paso a paso
- Troubleshooting
- Configuración SSL/HTTPS
- Backups automáticos
- Monitoreo

## 🔐 Credenciales de Prueba

```
Usuario: juanperez
Password: password123

Usuario: mariagonzalez
Password: password123

Usuario: se.valdivia
Password: password123
```

## 📊 Comandos Útiles

```bash
# Ver logs
sudo docker compose logs -f app

# Reiniciar aplicación
sudo docker compose restart

# Detener todo
sudo docker compose down

# Actualizar código
cd /opt/recetas-app/sc-s2/demo
sudo git pull
sudo docker compose up -d --build
```

## ✨ Características del Despliegue

- 🐳 **Contenedorizado**: Fácil de desplegar y escalar
- 🔄 **Auto-restart**: Contenedores se reinician automáticamente
- 💾 **Datos persistentes**: MySQL con volúmenes persistentes
- 🏥 **Health checks**: Monitoreo automático de salud
- 🔒 **Seguro**: Usuario no-root en contenedor Spring Boot
- ⚡ **Optimizado**: Build multi-stage para menor tamaño

## 🎓 Para la Evaluación

**Link de acceso:**
```
http://[TU_IP_PUBLICA]/recetas
```

Este link debe:
- ✅ Cargar la página home del sitio
- ✅ Mostrar el listado de recetas
- ✅ Permitir login con usuarios de prueba
- ✅ Acceso a APIs públicas sin JWT
- ✅ APIs privadas requieren JWT (401 sin token)

## 📞 Soporte

Si encuentras problemas durante el despliegue:

1. Revisa los logs: `sudo docker compose logs -f`
2. Verifica el estado: `sudo docker compose ps`
3. Consulta [DEPLOYMENT.md](./DEPLOYMENT.md) sección Troubleshooting
4. Reinicia los servicios: `sudo docker compose restart`

---

**Nota:** El script `deploy.sh` está diseñado para Ubuntu/Debian. Si usas otra distribución, consulta [DEPLOYMENT.md](./DEPLOYMENT.md) para instrucciones específicas.

## 🚀 ¡Tu aplicación está lista para evaluación!

Todo está configurado y probado. Solo necesitas:
1. Crear una VM
2. Ejecutar `deploy.sh`
3. Compartir el link: `http://[IP]/recetas`

¡Éxito! 🎉
