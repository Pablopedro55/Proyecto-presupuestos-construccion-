# Script para ejecutar pruebas de integracion
Write-Host "Compilando codigo de produccion..." -ForegroundColor Yellow
$srcFiles = Get-ChildItem -Path . -Include *.java -Recurse -Exclude test\* | Where-Object { $_.FullName -notlike "*\test\*" }
& javac -d bin -cp "lib\*" $srcFiles.FullName

if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR al compilar" -ForegroundColor Red
    exit 1
}

Write-Host "Compilando pruebas..." -ForegroundColor Yellow
$testFiles = Get-ChildItem -Path test -Filter *.java -Recurse
& javac -d bin -cp "bin;lib\*" $testFiles.FullName

if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR al compilar pruebas" -ForegroundColor Red
    exit 1
}

Write-Host "Ejecutando pruebas..." -ForegroundColor Yellow
& java -jar lib\junit-platform-console-standalone-1.10.0.jar --class-path "bin;lib\postgresql-42.7.2.jar" --scan-class-path --details=tree

if ($LASTEXITCODE -eq 0) {
    Write-Host "TODAS LAS PRUEBAS PASARON" -ForegroundColor Green
} else {
    Write-Host "ALGUNAS PRUEBAS FALLARON" -ForegroundColor Red
}
