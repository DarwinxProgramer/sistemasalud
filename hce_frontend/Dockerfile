# ========================================
# BUILD STAGE - Compilar aplicación React
# ========================================
FROM node:20-alpine AS build

WORKDIR /app

# Copiar archivos de dependencias
COPY package*.json ./

# Instalar dependencias (se cachea esta capa)
RUN npm ci

# Copiar código fuente
COPY . .

# Build de producción con Vite
# Genera carpeta /app/dist con assets optimizados
RUN npm run build

# ========================================
# PRODUCTION STAGE - Servir con Nginx
# ========================================
FROM nginx:alpine

# Copiar build de React al directorio de Nginx
COPY --from=build /app/dist /usr/share/nginx/html

# Copiar configuración personalizada de Nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Exponer puerto 80
EXPOSE 80

# Health check (verifica que Nginx responda)
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost/ || exit 1

# Nginx se ejecuta en foreground por defecto
CMD ["nginx", "-g", "daemon off;"]
