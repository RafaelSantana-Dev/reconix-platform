# 🚀 Quick Start - Reconix Platform

Guia rápido para rodar o projeto localmente.

---

## ⚡ Start Rápido (5 minutos)

### 1. Subir Infraestrutura
```bash
docker compose -f infrastructure/docker-compose.infra.yml up -d
```

Aguarde ~2 minutos para todos os containers iniciarem.

### 2. Iniciar Serviços (na ordem)

**Terminal 1 - Config Server:**
```bash
cd reconix-config && ./mvnw spring-boot:run
```

**Terminal 2 - Discovery Server:**
```bash
cd reconix-discovery && ./mvnw spring-boot:run
```

**Terminal 3 - API Gateway:**
```bash
cd reconix-gateway && ./mvnw spring-boot:run
```

**Terminais 4-11 - Demais Serviços:**
```bash
cd reconix-auth && ./mvnw spring-boot:run
cd reconix-ingestion && ./mvnw spring-boot:run
cd reconix-matching && ./mvnw spring-boot:run
cd reconix-ledger && ./mvnw spring-boot:run
cd reconix-fraud && ./mvnw spring-boot:run
cd reconix-notification && ./mvnw spring-boot:run
cd reconix-reporting && ./mvnw spring-boot:run
cd reconix-scheduler && ./mvnw spring-boot:run
```

### 3. Iniciar Frontend

**Terminal 12 - Frontend React:**
```bash
cd reconix-frontend
npm install
npm run dev
```

### 4. Acessar a Aplicação
- **Frontend (Dashboard):** http://localhost:5173
- **Usuário demo:** admin
- **Senha demo:** admin123

---

## 🎯 URLs Importantes

| Serviço | URL | Credenciais |
|---------|-----|-------------|
| **Frontend** | http://localhost:5173 | admin/admin123 |
| API Gateway | http://localhost:8080 | - |
| Eureka | http://localhost:8761 | - |
| Keycloak | http://localhost:8180 | admin/admin |
| RabbitMQ | http://localhost:15672 | guest/guest |
| MinIO | http://localhost:9001 | reconix/reconix123 |
| Grafana | http://localhost:3000 | admin/admin |
| Jaeger | http://localhost:16686 | - |

---

## 📊 Testar o Sistema

### 1. Upload de Arquivo
```bash
curl -X POST http://localhost:8080/api/ingestion/upload \
  -H "Content-Type: multipart/form-data" \
  -F "file=@seu-arquivo.csv" \
  -F "tenantId=tenant-123"
```

### 2. Ver Resultados de Matching
```bash
curl http://localhost:8080/api/matching/results?tenantId=tenant-123
```

### 3. Ver Alertas de Fraude
```bash
curl http://localhost:8080/api/fraud/alerts?tenantId=tenant-123&status=PENDING
```

### 4. Gerar Relatório PDF
```bash
curl -X POST http://localhost:8080/api/reports/generate/pdf \
  -H "Content-Type: application/json" \
  -d '{
    "tenantId": "tenant-123",
    "reportType": "RECONCILIATION",
    "startDate": "2025-01-01T00:00:00",
    "endDate": "2025-01-31T23:59:59"
  }' \
  --output report.pdf
```

---

## 🛑 Parar Tudo

### Parar Serviços
Pressione `Ctrl+C` em cada terminal.

### Parar Infraestrutura
```bash
docker compose -f infrastructure/docker-compose.infra.yml down
```

### Limpar Volumes (CUIDADO: apaga dados)
```bash
docker compose -f infrastructure/docker-compose.infra.yml down -v
```

---

## 🐛 Troubleshooting

### Porta já em uso
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <numero_do_pid> /F

# Linux/Mac
lsof -ti:8080 | xargs kill -9
```

### Container não inicia
```bash
docker logs <container_name>
docker restart <container_name>
```

### Kafka não conecta
```bash
# Reiniciar Kafka após Zookeeper estar healthy
docker restart reconix-kafka
```

### Limpar e recomeçar
```bash
docker compose -f infrastructure/docker-compose.infra.yml down -v
docker compose -f infrastructure/docker-compose.infra.yml up -d
```

---

## 📚 Documentação Completa

- **README.md** - Documentação principal
- **IMPLEMENTATION_SUMMARY.md** - Resumo da implementação
- **NEXT_STEPS.md** - Próximos passos
- **PROJECT_STATUS.md** - Status atual
- **CHECKLIST.md** - Checklist de tarefas

---

## 🎯 Próximo Passo

A plataforma está **100% completa**! 🎉

Explore as funcionalidades:
- 📊 **Dashboard** - Visualize KPIs em tempo real
- 📤 **Upload** - Envie arquivos financeiros
- 🔄 **Conciliação** - Veja os resultados de matching
- 🛡️ **Centro de Fraudes** - Monitore alertas de segurança
- 📈 **Relatórios** - Gere relatórios PDF e Excel

Para configurar observabilidade completa (Grafana dashboards, alertas), consulte `NEXT_STEPS.md`.

---

**Dica:** Use `tmux` ou `screen` para gerenciar múltiplos terminais facilmente!
