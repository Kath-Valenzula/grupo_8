#!/bin/bash

# Script de prueba local con Docker Compose
# Ejecuta este script antes de desplegar en la VM para verificar que todo funciona

set -e

echo "=========================================="
echo "🧪 Testing Docker Deployment Locally"
echo "=========================================="

# 1. Detener contenedores anteriores
echo "🛑 Deteniendo contenedores anteriores..."
docker compose down -v 2>/dev/null || true

# 2. Limpiar imágenes antiguas
echo "🧹 Limpiando imágenes antiguas..."
docker system prune -f

# 3. Construir imágenes
echo "🏗️  Construyendo imágenes..."
docker compose build --no-cache

# 4. Levantar servicios
echo "🚀 Levantando servicios..."
docker compose up -d

# 5. Esperar a que MySQL esté listo
echo "⏳ Esperando a que MySQL esté listo..."
sleep 15

# 6. Verificar estado de contenedores
echo "🔍 Verificando estado..."
docker compose ps

# 7. Ver logs
echo ""
echo "📋 Logs de la aplicación:"
docker compose logs app --tail=50

# 8. Probar endpoints
echo ""
echo "🧪 Probando endpoints..."

echo -n "  - Health check: "
if curl -s http://localhost/actuator/health | grep -q "UP"; then
    echo "✅ OK"
else
    echo "❌ FAIL"
fi

echo -n "  - Home: "
if curl -s http://localhost/recetas | grep -q "Recetas"; then
    echo "✅ OK"
else
    echo "❌ FAIL"
fi

echo -n "  - API Recetas: "
if curl -s http://localhost/api/recetas | grep -q "\["; then
    echo "✅ OK"
else
    echo "❌ FAIL"
fi

echo ""
echo "=========================================="
echo "✅ Testing completado!"
echo "=========================================="
echo ""
echo "🌐 Accede a: http://localhost/recetas"
echo ""
echo "📊 Comandos útiles:"
echo "   - Ver logs:    docker compose logs -f app"
echo "   - Reiniciar:   docker compose restart"
echo "   - Detener:     docker compose down"
echo ""
