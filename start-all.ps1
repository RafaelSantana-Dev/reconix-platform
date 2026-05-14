# Script para iniciar todos os serviços da Plataforma Reconix
# Uso: .\start-all.ps1

Write-Host "🚀 Iniciando Plataforma Reconix..." -ForegroundColor Cyan
Write-Host ""

# Verificar se Docker está rodando
Write-Host "📦 Verificando Docker..." -ForegroundColor Yellow
$dockerRunning = docker ps 2>$null
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Docker não está rodando. Inicie o Docker Desktop primeiro." -ForegroundColor Red
    exit 1
}
Write-Host "✅ Docker está rodando" -ForegroundColor Green
Write-Host ""

# Subir infraestrutura
Write-Host "🏗️  Subindo infraestrutura (PostgreSQL, MongoDB, Redis, Kafka, RabbitMQ, etc)..." -ForegroundColor Yellow
docker compose -f infrastructure/docker-compose.infra.yml up -d

if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Erro ao subir infraestrutura" -ForegroundColor Red
    exit 1
}

Write-Host "✅ Infraestrutura iniciada" -ForegroundColor Green
Write-Host "⏳ Aguardando containers ficarem healthy (30 segundos)..." -ForegroundColor Yellow
Start-Sleep -Seconds 30
Write-Host ""

# Função para iniciar serviço em nova janela
function Start-Service {
    param(
        [string]$ServiceName,
        [string]$ServicePath,
        [int]$WaitSeconds = 10
    )
    
    Write-Host "🔄 Iniciando $ServiceName..." -ForegroundColor Yellow
    
    $command = "cd '$ServicePath'; ./mvnw spring-boot:run"
    Start-Process powershell -ArgumentList "-NoExit", "-Command", $command
    
    Write-Host "✅ $ServiceName iniciado (aguardando $WaitSeconds segundos)" -ForegroundColor Green
    Start-Sleep -Seconds $WaitSeconds
}

# Iniciar serviços na ordem correta
Write-Host "🎯 Iniciando microsserviços..." -ForegroundColor Cyan
Write-Host ""

Start-Service "Config Server" "$PSScriptRoot\reconix-config" 15
Start-Service "Discovery Server" "$PSScriptRoot\reconix-discovery" 15
Start-Service "API Gateway" "$PSScriptRoot\reconix-gateway" 10
Start-Service "Auth Service" "$PSScriptRoot\reconix-auth" 5
Start-Service "Ingestion Service" "$PSScriptRoot\reconix-ingestion" 5
Start-Service "Matching Service" "$PSScriptRoot\reconix-matching" 5
Start-Service "Ledger Service" "$PSScriptRoot\reconix-ledger" 5
Start-Service "Fraud Service" "$PSScriptRoot\reconix-fraud" 5
Start-Service "Notification Service" "$PSScriptRoot\reconix-notification" 5
Start-Service "Reporting Service" "$PSScriptRoot\reconix-reporting" 5
Start-Service "Scheduler Service" "$PSScriptRoot\reconix-scheduler" 5

Write-Host ""
Write-Host "🎨 Iniciando Frontend..." -ForegroundColor Yellow
$frontendCommand = "cd '$PSScriptRoot\reconix-frontend'; npm install; npm run dev"
Start-Process powershell -ArgumentList "-NoExit", "-Command", $frontendCommand
Write-Host "✅ Frontend iniciado" -ForegroundColor Green

Write-Host ""
Write-Host "🎉 Todos os serviços foram iniciados!" -ForegroundColor Green
Write-Host ""
Write-Host "📋 URLs importantes:" -ForegroundColor Cyan
Write-Host "   Frontend:        http://localhost:5173 (admin/admin123)" -ForegroundColor White
Write-Host "   API Gateway:     http://localhost:8080" -ForegroundColor White
Write-Host "   Eureka:          http://localhost:8761" -ForegroundColor White
Write-Host "   RabbitMQ:        http://localhost:15672 (guest/guest)" -ForegroundColor White
Write-Host "   Keycloak:        http://localhost:8180 (admin/admin)" -ForegroundColor White
Write-Host ""
Write-Host "⏳ Aguarde ~2 minutos para todos os serviços ficarem prontos" -ForegroundColor Yellow
Write-Host ""
Write-Host "Para parar tudo, execute: .\stop-all.ps1" -ForegroundColor Cyan
