@echo off
chcp 65001 > nul
echo ===================================================
echo   Executando Demonstração de Padrões GoF
echo ===================================================

if not exist bin (
    echo Diretório 'bin' não encontrado. Executando build primeiro...
    call build.bat
)

java -cp bin com.gfb.designpatterns.Main

echo.
pause
