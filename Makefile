.PHONY: infra-up infra-down monitoring-up monitoring-down test-unit test-integration test-all clean

# ==========================================
# Infraestrutura
# ==========================================
infra-up:
	docker compose -f infrastructure/docker-compose.infra.yml up -d
	@echo "Infraestrutura iniciada com sucesso!"

infra-down:
	docker compose -f infrastructure/docker-compose.infra.yml down -v
	@echo "Infraestrutura parada."

# ==========================================
# Monitoramento
# ==========================================
monitoring-up:
	docker compose -f infrastructure/docker-compose.monitoring.yml up -d
	@echo "Monitoramento iniciado! Grafana: http://localhost:3000"

monitoring-down:
	docker compose -f infrastructure/docker-compose.monitoring.yml down -v

# ==========================================
# Testes
# ==========================================
test-unit:
	@echo "Rodando testes unitarios..."
	cd reconix-ingestion && ./mvnw test
	cd reconix-matching && ./mvnw test
	cd reconix-ledger && ./mvnw test
	cd reconix-fraud && ./mvnw test
	cd reconix-reporting && ./mvnw test
	cd reconix-notification && ./mvnw test
	@echo "Todos os testes unitarios passaram!"

test-integration:
	@echo "Rodando testes de integracao (requer Docker)..."
	cd reconix-ingestion && ./mvnw verify -P integration-test
	cd reconix-matching && ./mvnw verify -P integration-test
	@echo "Testes de integracao concluidos!"

test-all: test-unit test-integration
	@echo "Todos os testes passaram!"

# ==========================================
# Limpeza
# ==========================================
clean:
	docker compose -f infrastructure/docker-compose.infra.yml down -v
	docker compose -f infrastructure/docker-compose.monitoring.yml down -v
	docker volume prune -f
	@echo "Tudo limpo!"