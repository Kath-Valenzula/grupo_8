#!/bin/bash

# Script de despliegue para VM Linux
# Este script automatiza la instalación y configuración de la aplicación Recetas

set -e  # Salir si hay errores

echo "=========================================="
echo "🚀 Iniciando despliegue de Recetas App"
echo "=========================================="

# Colores para output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Verificar que se ejecuta como root
if [ "$EUID" -ne 0 ]; then 
    echo -e "${RED}❌ Por favor ejecuta este script como root (sudo)${NC}"
    exit 1
fi

# 1. Actualizar sistema
echo -e "${YELLOW}📦 Actualizando sistema...${NC}"
apt-get update -y
apt-get upgrade -y

# 2. Instalar dependencias
echo -e "${YELLOW}📦 Instalando dependencias básicas...${NC}"
apt-get install -y \
    apt-transport-https \
    ca-certificates \
    curl \
    gnupg \
    lsb-release \
    git \
    wget

# 3. Instalar Docker
echo -e "${YELLOW}🐳 Instalando Docker...${NC}"
if ! command -v docker &> /dev/null; then
    # Agregar repositorio oficial de Docker
    mkdir -p /etc/apt/keyrings
    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | gpg --dearmor -o /etc/apt/keyrings/docker.gpg
    echo \
      "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
      $(lsb_release -cs) stable" | tee /etc/apt/sources.list.d/docker.list > /dev/null
    
    apt-get update -y
    apt-get install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
    
    # Iniciar y habilitar Docker
    systemctl start docker
    systemctl enable docker
    
    echo -e "${GREEN}✅ Docker instalado correctamente${NC}"
else
    echo -e "${GREEN}✅ Docker ya está instalado${NC}"
fi

# Verificar instalación
docker --version
docker compose version

# 4. Configurar firewall
echo -e "${YELLOW}🔥 Configurando firewall...${NC}"
if command -v ufw &> /dev/null; then
    ufw allow 80/tcp
    ufw allow 443/tcp
    ufw allow 22/tcp
    echo -e "${GREEN}✅ Firewall configurado${NC}"
fi

# 5. Clonar o actualizar repositorio
REPO_DIR="/opt/recetas-app"
REPO_URL="https://github.com/Kath-Valenzula/grupo_8.git"

echo -e "${YELLOW}📥 Descargando código...${NC}"
if [ -d "$REPO_DIR" ]; then
    echo "Directorio existe, actualizando..."
    cd "$REPO_DIR/sc-s2/demo"
    git pull
else
    echo "Clonando repositorio..."
    git clone "$REPO_URL" "$REPO_DIR"
    cd "$REPO_DIR/sc-s2/demo"
fi

# 6. Detener contenedores anteriores
echo -e "${YELLOW}🛑 Deteniendo contenedores anteriores...${NC}"
docker compose down || true

# 7. Limpiar recursos antiguos
echo -e "${YELLOW}🧹 Limpiando recursos antiguos...${NC}"
docker system prune -f

# 8. Construir y levantar aplicación
echo -e "${YELLOW}🏗️  Construyendo y levantando aplicación...${NC}"
docker compose up -d --build

# 9. Esperar a que la aplicación esté lista
echo -e "${YELLOW}⏳ Esperando a que la aplicación esté lista...${NC}"
sleep 30

# 10. Verificar estado
echo -e "${YELLOW}🔍 Verificando estado de contenedores...${NC}"
docker compose ps

# 11. Verificar logs
echo -e "${YELLOW}📋 Últimos logs de la aplicación:${NC}"
docker compose logs --tail=50 app

# 12. Obtener IP pública
PUBLIC_IP=$(curl -s ifconfig.me || echo "No se pudo obtener IP")

echo ""
echo "=========================================="
echo -e "${GREEN}✅ ¡DESPLIEGUE COMPLETADO!${NC}"
echo "=========================================="
echo ""
echo "🌐 Accede a tu aplicación en:"
echo -e "${GREEN}   http://$PUBLIC_IP/recetas${NC}"
echo ""
echo "📊 Comandos útiles:"
echo "   - Ver logs:           docker compose logs -f"
echo "   - Reiniciar:          docker compose restart"
echo "   - Detener:            docker compose down"
echo "   - Estado:             docker compose ps"
echo ""
echo "🔧 Localización del proyecto:"
echo "   $REPO_DIR/sc-s2/demo"
echo ""
echo "=========================================="
