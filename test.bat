@echo off
chcp 65001 > nul
echo ===================================================
echo   Executando Testes Automatizados dos Padrões GoF
echo ===================================================

if not exist bin (
    echo Compilando o projeto primeiro...
    call build.bat
)

java -cp bin com.gfb.designpatterns.test.TestSuiteRunner

echo.
pause
