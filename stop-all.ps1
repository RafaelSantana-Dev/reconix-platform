# Script para parar todos os serviços da Plataforma Reconix
# Uso: .\stop-all.ps1

Write-Host "🛑 Parando Plataforma Reconix..." -ForegroundColor Cyan
Write-Host ""

# Parar processos Java (Spring Boot)
Write-Host "🔄 Parando microsserviços Java..." -ForegroundColor Yellow
$javaProcesses = Get-Process -Name "java" -ErrorAction SilentlyContinue
if ($javaProcesses) {
    $javaProcesses | Stop-Process -Force
    Write-Host "✅ Microsserviços Java parados" -ForegroundColor Green
} else {
    Write-Host "ℹ️  Nenhum processo Java encontrado" -ForegroundColor Gray
}

# Parar processos Node (Frontend)
Write-Host "🔄 Parando frontend..." -ForegroundColor Yellow
$nodeProcesses = Get-Process -Name "node" -ErrorAction SilentlyContinue
if ($nodeProcesses) {
    $nodeProcesses | Stop-Process -Force
    Write-Host "✅ Frontend parado" -ForegroundColor Green
} else {
    Write-Host "ℹ️  Nenhum processo Node encontrado" -ForegroundColor Gray
}

# Parar infraestrutura Docker
Write-Host "🔄 Parando infraestrutura Docker..." -ForegroundColor Yellow
docker compose -f infrastructure/docker-compose.infra.yml down

if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ Infraestrutura Docker parada" -ForegroundColor Green
} else {
    Write-Host "⚠️  Erro ao parar infraestrutura Docker" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "🎉 Todos os serviços foram parados!" -ForegroundColor Green
Write-Host ""
Write-Host "Para limpar volumes (APAGA DADOS), execute:" -ForegroundColor Yellow
Write-Host "   docker compose -f infrastructure/docker-compose.infra.yml down -v" -ForegroundColor White
