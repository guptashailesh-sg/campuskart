@echo off
title CampusKart Build
echo ========================================
echo          CampusKart Build
echo ========================================
echo.

where mvn >nul 2>nul
if errorlevel 1 (
    echo [ERROR] Maven was not found.
    echo Install Maven and add it to PATH.
    echo.
    pause
    exit /b 1
)

echo [1/2] Cleaning previous build...
call mvn clean
if errorlevel 1 (
    echo.
    echo [ERROR] Maven clean failed.
    pause
    exit /b 1
)

echo.
echo [2/2] Building CampusKart WAR...
call mvn package
if errorlevel 1 (
    echo.
    echo [ERROR] Build failed.
    pause
    exit /b 1
)

echo.
echo ========================================
echo          BUILD SUCCESS
echo ========================================
echo.
echo WAR file:
echo target\campuskart.war
echo.
echo Copy this WAR into your Tomcat 9 webapps folder.
echo Then start Tomcat and open:
echo http://localhost:8080/campuskart/
echo.
pause
