.PHONY: help infra-up infra-down infra-clean monitoring-up monitoring-down start-frontend install-frontend build-all test-all clean urls status

# ==========================================
# Help
# ==========================================
help: ## Mostra esta mensagem de ajuda
	@echo "Reconix Platform - Comandos disponíveis:"
	@echo ""
	@echo "  make infra-up          - Sobe infraestrutura (PostgreSQL, MongoDB, Redis, Kafka, RabbitMQ)"
	@echo "  make infra-down        - Para infraestrutura"
	@echo "  make infra-clean       - Para e remove volumes (APAGA DADOS)"
	@echo "  make monitoring-up     - Sobe monitoramento (Prometheus, Grafana, Jaeger)"
	@echo "  make monitoring-down   - Para monitoramento"
	@echo "  make install-frontend  - Instala dependências do frontend"
	@echo "  make start-frontend    - Inicia frontend em modo dev"
	@echo "  make build-all         - Build de todos os módulos"
	@echo "  make test-all          - Roda todos os testes"
	@echo "  make clean             - Limpa tudo"
	@echo "  make urls              - Mostra URLs importantes"
	@echo "  make status            - Mostra status dos containers"
	@echo ""

# ==========================================
# Infraestrutura
# ==========================================
infra-up: ## Sobe toda a infraestrutura
	docker compose -f infrastructure/docker-compose.infra.yml up -d
	@echo "✅ Infraestrutura iniciada! Aguarde ~30 segundos para ficar pronta."

infra-down: ## Para a infraestrutura
	docker compose -f infrastructure/docker-compose.infra.yml down
	@echo "✅ Infraestrutura parada."

infra-clean: ## Para e remove volumes (APAGA DADOS)
	docker compose -f infrastructure/docker-compose.infra.yml down -v
	@echo "✅ Infraestrutura limpa (volumes removidos)."

# ==========================================
# Monitoramento
# ==========================================
monitoring-up: ## Sobe stack de monitoramento
	docker compose -f infrastructure/docker-compose.monitoring.yml up -d
	@echo "✅ Monitoramento iniciado!"
	@echo "   Grafana: http://localhost:3000 (admin/admin)"
	@echo "   Jaeger:  http://localhost:16686"

monitoring-down: ## Para stack de monitoramento
	docker compose -f infrastructure/docker-compose.monitoring.yml down -v
	@echo "✅ Monitoramento parado."

# ==========================================
# Frontend
# ==========================================
install-frontend: ## Instala dependências do frontend
	cd reconix-frontend && npm install
	@echo "✅ Dependências do frontend instaladas."

start-frontend: ## Inicia frontend em modo desenvolvimento
	cd reconix-frontend && npm run dev

build-frontend: ## Build do frontend para produção
	cd reconix-frontend && npm run build
	@echo "✅ Frontend buildado em reconix-frontend/dist/"

# ==========================================
# Build e Testes
# ==========================================
build-all: ## Build de todos os módulos (sem testes)
	mvn clean install -DskipTests
	@echo "✅ Build completo realizado."

test-unit: ## Roda testes unitários
	@echo "🧪 Rodando testes unitários..."
	cd reconix-ingestion && ./mvnw test
	cd reconix-matching && ./mvnw test
	cd reconix-ledger && ./mvnw test
	cd reconix-fraud && ./mvnw test
	cd reconix-reporting && ./mvnw test
	cd reconix-notification && ./mvnw test
	cd reconix-scheduler && ./mvnw test
	@echo "✅ Todos os testes unitários passaram!"

test-integration: ## Roda testes de integração (requer Docker)
	@echo "🧪 Rodando testes de integração..."
	cd reconix-ingestion && ./mvnw verify -P integration-test
	cd reconix-matching && ./mvnw verify -P integration-test
	@echo "✅ Testes de integração concluídos!"

test-all: test-unit test-integration ## Roda todos os testes
	@echo "✅ Todos os testes passaram!"

# ==========================================
# Limpeza
# ==========================================
clean: ## Limpa builds e caches
	mvn clean
	cd reconix-frontend && rm -rf node_modules dist
	@echo "✅ Limpeza realizada."

clean-all: clean infra-clean monitoring-down ## Limpa tudo (builds, volumes, containers)
	docker volume prune -f
	@echo "✅ Limpeza completa realizada!"

# ==========================================
# Logs
# ==========================================
logs-infra: ## Mostra logs da infraestrutura
	docker compose -f infrastructure/docker-compose.infra.yml logs -f

logs-monitoring: ## Mostra logs do monitoramento
	docker compose -f infrastructure/docker-compose.monitoring.yml logs -f

# ==========================================
# Status e Informações
# ==========================================
status: ## Mostra status de todos os containers
	@echo "📊 Status da Infraestrutura:"
	@docker compose -f infrastructure/docker-compose.infra.yml ps
	@echo ""
	@echo "📊 Status do Monitoramento:"
	@docker compose -f infrastructure/docker-compose.monitoring.yml ps

urls: ## Mostra todas as URLs importantes
	@echo "📋 URLs da Plataforma Reconix:"
	@echo ""
	@echo "🎨 Frontend:           http://localhost:5173 (admin/admin123)"
	@echo "🚪 API Gateway:        http://localhost:8080"
	@echo "🔍 Eureka Dashboard:   http://localhost:8761"
	@echo "🔐 Keycloak:           http://localhost:8180 (admin/admin)"
	@echo "🐰 RabbitMQ:           http://localhost:15672 (guest/guest)"
	@echo "📦 MinIO Console:      http://localhost:9001 (reconix/reconix123)"
	@echo "📊 Grafana:            http://localhost:3000 (admin/admin)"
	@echo "🔎 Jaeger:             http://localhost:16686"
	@echo ""
