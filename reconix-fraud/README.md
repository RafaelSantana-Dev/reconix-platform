# 🛡️ Reconix Fraud Detection Service

Serviço de detecção de fraudes com **Rule Engine** e **detecção de anomalias estatísticas**.

## 📋 Funcionalidades

### Rule Engine (Regras Configuráveis)

1. **Duplicate Payment** - Detecta pagamentos duplicados em janela de tempo
2. **Unusual Amount** - Detecta valores fora do padrão histórico (Z-Score)
3. **High Frequency** - Detecta alta frequência de transações
4. **Round Amount** - Detecta valores "redondos" suspeitos
5. **Unusual Time** - Detecta transações fora do horário comercial

### Níveis de Risco

- **LOW** (< 0.3) - Risco baixo
- **MEDIUM** (0.3 - 0.6) - Risco médio
- **HIGH** (0.6 - 0.8) - Risco alto
- **CRITICAL** (> 0.8) - Risco crítico

## 🔄 Fluxo de Eventos

```
Kafka (match.found) → Fraud Detection → RabbitMQ (fraud.alert) → Notification Service
```

## 🗄️ Armazenamento

- **MongoDB**: Histórico de alertas de fraude
- **Redis**: Cache de estatísticas e detecção de duplicatas

## 🚀 Como Executar

```bash
./mvnw spring-boot:run
```

## 📊 Endpoints

### Listar Alertas
```http
GET /api/fraud/alerts?tenantId={tenantId}&status=PENDING
```

### Buscar Alerta
```http
GET /api/fraud/alerts/{alertId}
```

### Revisar Alerta
```http
PUT /api/fraud/alerts/{alertId}/review?status=CONFIRMED_FRAUD&reviewedBy=analyst@reconix.com
```

### Alertas por Período
```http
GET /api/fraud/alerts/period?tenantId={tenantId}&startDate=2025-01-01T00:00:00&endDate=2025-01-31T23:59:59
```

## ⚙️ Configuração

Todas as regras podem ser habilitadas/desabilitadas via `application.yml`:

```yaml
fraud:
  rules:
    duplicate-payment:
      enabled: true
      time-window-minutes: 5
    unusual-amount:
      enabled: true
      z-score-threshold: 3.0
```

## 🧪 Testes

```bash
./mvnw test
```
