@echo off
REM ========================================
REM Script de Inicio Rápido - Sistema HCE
REM ========================================

echo.
echo ====================================
echo   Sistema HCE - Docker Setup
echo ====================================
echo.

REM Verificar si Docker está instalado
docker --version >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Docker no esta instalado o no esta en PATH
    echo Por favor instala Docker Desktop: https://www.docker.com/products/docker-desktop
    pause
    exit /b 1
)

echo [OK] Docker encontrado
echo.

REM Verificar si existe .env
if not exist ".env" (
    echo [INFO] Creando archivo .env desde .env.example
    copy .env.example .env
    echo [OK] Archivo .env creado. Puedes editarlo si lo deseas.
    echo.
)

REM Verificar si Docker está corriendo
docker info >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Docker Desktop no esta corriendo
    echo Por favor inicia Docker Desktop y vuelve a ejecutar este script
    pause
    exit /b 1
)

echo [OK] Docker Desktop esta corriendo
echo.

REM Construir e iniciar contenedores
echo ====================================
echo   Iniciando Contenedores
echo ====================================
echo.
echo Esto puede tomar 2-3 minutos la primera vez...
echo.

docker-compose up -d --build

if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Fallo al iniciar los contenedores
    echo Revisa los logs con: docker-compose logs
    pause
    exit /b 1
)

echo.
echo ====================================
echo   Sistema Iniciado con Exito!
echo ====================================
echo.
echo Servicios disponibles:
echo   - Backend API: http://localhost:8080
echo   - MySQL:       localhost:3306
echo   - Health:      http://localhost:8080/actuator/health
echo.
echo Ver logs en tiempo real:
echo   docker-compose logs -f
echo.
echo Detener el sistema:
echo   docker-compose down
echo.

REM Esperar a que los servicios estén listos
echo Esperando a que los servicios esten listos...
timeout /t 10 /nobreak >nul

REM Verificar health del backend
echo.
echo Verificando estado del backend...
curl -s http://localhost:8080/actuator/health >nul 2>&1
if %errorlevel% equ 0 (
    echo [OK] Backend esta respondiendo!
) else (
    echo [INFO] Backend aun esta iniciando...
    echo       Espera 30-60 segundos y verifica con:
    echo       curl http://localhost:8080/actuator/health
)

echo.
pause
