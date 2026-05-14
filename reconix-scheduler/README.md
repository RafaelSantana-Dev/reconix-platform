# ⏰ Reconix Scheduler Service

Serviço de agendamento de tarefas com **Quartz Scheduler**.

## 📋 Funcionalidades

### Jobs Automáticos

1. **Daily Report Job** - Relatório diário às 8h
2. **Weekly Report Job** - Relatório semanal toda segunda às 9h
3. **Fraud Analysis Job** - Análise de fraudes a cada 30 minutos
4. **Retry Failed Notifications Job** - Retry de notificações falhadas

### Características

- **Persistência**: Jobs armazenados no PostgreSQL
- **Clustering**: Suporte a múltiplas instâncias
- **Retry automático**: Em caso de falha
- **Configurável**: Cron expressions via application.yml

## 🔄 Fluxo de Execução

```
Quartz Scheduler → Job Execution → Feign Client → Other Services (Reporting, Notification)
```

## 🗄️ Armazenamento

- **PostgreSQL**: Tabelas do Quartz para persistência de jobs

## 🚀 Como Executar

```bash
./mvnw spring-boot:run
```

## 📊 Endpoints

### Listar Jobs
```http
GET /api/scheduler/jobs
```

### Agendar Job Manualmente
```http
POST /api/scheduler/jobs/schedule
Content-Type: application/json

{
  "jobName": "custom-report",
  "jobGroup": "reports",
  "cronExpression": "0 0 10 * * ?",
  "description": "Custom report at 10 AM"
}
```

### Pausar Job
```http
PUT /api/scheduler/jobs/{jobName}/pause
```

### Retomar Job
```http
PUT /api/scheduler/jobs/{jobName}/resume
```

### Executar Job Imediatamente
```http
POST /api/scheduler/jobs/{jobName}/trigger
```

### Deletar Job
```http
DELETE /api/scheduler/jobs/{jobName}
```

## ⚙️ Configuração

### Cron Expressions

```yaml
scheduler:
  jobs:
    daily-report:
      enabled: true
      cron: "0 0 8 * * ?" # Todos os dias às 8h
```

### Exemplos de Cron

- `0 0 8 * * ?` - Todos os dias às 8h
- `0 0 9 ? * MON` - Toda segunda-feira às 9h
- `0 */30 * * * ?` - A cada 30 minutos
- `0 0 0 1 * ?` - Todo dia 1 do mês à meia-noite

## 🧪 Testes

```bash
./mvnw test
```
