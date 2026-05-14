# 📧 Reconix Notification Service

Serviço de notificações multicanal para alertas de fraude e eventos do sistema.

## 📋 Canais Suportados

### 1. Email
- Templates HTML com Thymeleaf
- Suporte a SMTP
- Configurável via `application.yml`

### 2. Slack
- Integração via Webhook
- Mensagens formatadas com cores por nível de risco
- Suporte a attachments

### 3. Webhook
- HTTP POST para URLs configuradas
- Timeout configurável
- Retry automático em caso de falha

### 4. WebSocket
- Notificações em tempo real
- Tópicos por tenant
- Integração com frontend via STOMP

## 🔄 Fluxo de Eventos

```
RabbitMQ (fraud.alert) → Notification Service → [Email, Slack, Webhook, WebSocket]
```

## 🗄️ Armazenamento

- **MongoDB**: Histórico de notificações enviadas

## 🚀 Como Executar

```bash
./mvnw spring-boot:run
```

## 📊 Endpoints

### Listar Logs de Notificações
```http
GET /api/notifications/logs?tenantId={tenantId}&status=SENT
```

### Buscar Log
```http
GET /api/notifications/logs/{logId}
```

### Logs por Alerta
```http
GET /api/notifications/logs/alert/{alertId}
```

### Logs por Período
```http
GET /api/notifications/logs/period?tenantId={tenantId}&startDate=2025-01-01T00:00:00&endDate=2025-01-31T23:59:59
```

## ⚙️ Configuração

### Email (SMTP)
```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: your-email@gmail.com
    password: your-app-password
```

### Slack
```yaml
notification:
  slack:
    enabled: true
    webhook-url: https://hooks.slack.com/services/YOUR/WEBHOOK/URL
```

### WebSocket
Frontend pode conectar em:
```
ws://localhost:8086/ws
```

E subscrever no tópico:
```
/topic/fraud-alerts/{tenantId}
```

## 🧪 Testes

```bash
./mvnw test
```
