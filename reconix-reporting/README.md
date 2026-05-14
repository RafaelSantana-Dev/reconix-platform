# 📊 Reconix Reporting Service

Serviço de relatórios, dashboard e busca full-text.

## 📋 Funcionalidades

### Geração de Relatórios
- **PDF**: Relatórios formatados com iText
- **Excel**: Planilhas com Apache POI
- Relatórios de conciliação, divergências e fraudes

### Busca Full-Text
- **Elasticsearch**: Busca rápida em transações
- Filtros por data, valor, status, tenant
- Agregações e estatísticas

### Dashboard
- KPIs em tempo real
- Taxa de conciliação
- Divergências por período
- Alertas de fraude

## 🔄 Fluxo de Eventos

```
Kafka (match.found, fraud.alert) → Reporting Service → Elasticsearch + PostgreSQL
```

## 🗄️ Armazenamento

- **PostgreSQL**: Dados agregados e relatórios gerados
- **Elasticsearch**: Índice de transações para busca

## 🚀 Como Executar

```bash
./mvnw spring-boot:run
```

## 📊 Endpoints

### Gerar Relatório PDF
```http
POST /api/reports/generate/pdf
Content-Type: application/json

{
  "tenantId": "tenant-123",
  "reportType": "RECONCILIATION",
  "startDate": "2025-01-01T00:00:00",
  "endDate": "2025-01-31T23:59:59"
}
```

### Gerar Relatório Excel
```http
POST /api/reports/generate/excel
```

### Buscar Transações
```http
GET /api/search/transactions?tenantId={tenantId}&query=pagamento&from=0&size=20
```

### Dashboard KPIs
```http
GET /api/dashboard/kpis?tenantId={tenantId}&startDate=2025-01-01T00:00:00&endDate=2025-01-31T23:59:59
```

### Estatísticas de Conciliação
```http
GET /api/dashboard/reconciliation-stats?tenantId={tenantId}
```

## 🧪 Testes

```bash
./mvnw test
```
