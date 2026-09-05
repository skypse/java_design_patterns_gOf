@echo off
chcp 65001 > nul
echo ===================================================
echo   Compilando o Projeto Java Design Patterns (GoF)
echo ===================================================

if not exist bin (
    mkdir bin
)

echo Coletando arquivos .java...
powershell -Command "$files = (Get-ChildItem -Recurse -Filter *.java src).FullName; javac -encoding UTF-8 -d bin $files"

if %ERRORLEVEL% NEQ 0 (
    echo [ERRO] Falha na compilação do projeto.
    pause
    exit /b %ERRORLEVEL%
)

echo [SUCESSO] Compilação finalizada com sucesso! A pasta 'bin' está atualizada.
