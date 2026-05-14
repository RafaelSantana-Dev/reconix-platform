# 🏗️ Arquitetura da Plataforma Reconix

## 📐 Visão Geral

A Plataforma Reconix é construída com **arquitetura de microsserviços orientados a eventos**, seguindo os princípios de **Domain-Driven Design (DDD)**, **Event Sourcing** e **CQRS**.

---

## 🎯 Diagrama de Arquitetura

```
┌─────────────────────────────────────────────────────────────────────────┐
│                          FRONTEND (React)                                │
│                        http://localhost:5173                             │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐ │
│  │Dashboard │  │  Upload  │  │Reconcile │  │  Fraud   │  │ Reports  │ │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘  └──────────┘ │
└────────────────────────────┬────────────────────────────────────────────┘
                             │ HTTP/REST + WebSocket
                             ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                       API GATEWAY (8080)                                 │
│  ┌────────────────────────────────────────────────────────────────┐    │
│  │ • Roteamento Inteligente                                        │    │
│  │ • Rate Limiting                                                 │    │
│  │ • CORS                                                          │    │
│  │ • Correlation ID                                                │    │
│  │ • Circuit Breaker                                               │    │
│  └────────────────────────────────────────────────────────────────┘    │
└────────────────────────────┬────────────────────────────────────────────┘
                             │
        ┌────────────────────┼────────────────────┐
        │                    │                    │
        ▼                    ▼                    ▼
┌──────────────┐    ┌──────────────┐    ┌──────────────┐
│   Config     │    │  Discovery   │    │     Auth     │
│   Server     │    │   (Eureka)   │    │  (Keycloak)  │
│   (8888)     │    │   (8761)     │    │   (8081)     │
└──────────────┘    └──────────────┘    └──────────────┘
                                                 │
                                                 │ JWT
                                                 ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                         MICROSSERVIÇOS                                   │
│                                                                          │
│  ┌──────────────┐    ┌──────────────┐    ┌──────────────┐             │
│  │  Ingestion   │    │   Matching   │    │    Ledger    │             │
│  │   (8083)     │───▶│   (8082)     │───▶│   (8084)     │             │
│  │              │    │              │    │              │             │
│  │ • Upload     │    │ • Scoring    │    │ • Events     │             │
│  │ • Parsing    │    │ • Strategies │    │ • CQRS       │             │
│  │ • MinIO      │    │ • Conflicts  │    │ • Replay     │             │
│  └──────┬───────┘    └──────┬───────┘    └──────────────┘             │
│         │                   │                                           │
│         │ RabbitMQ          │ Kafka                                     │
│         │                   │                                           │
│         ▼                   ▼                                           │
│  ┌──────────────┐    ┌──────────────┐    ┌──────────────┐             │
│  │    Fraud     │    │  Reporting   │    │Notification  │             │
│  │   (8085)     │    │   (8087)     │    │   (8086)     │             │
│  │              │    │              │    │              │             │
│  │ • Rules      │    │ • PDF        │    │ • Email      │             │
│  │ • ML         │    │ • Excel      │    │ • Slack      │             │
│  │ • Alerts     │    │ • Dashboard  │    │ • WebSocket  │             │
│  └──────────────┘    └──────────────┘    └──────────────┘             │
│                                                                          │
│  ┌──────────────┐                                                       │
│  │  Scheduler   │                                                       │
│  │   (8088)     │                                                       │
│  │              │                                                       │
│  │ • Quartz     │                                                       │
│  │ • Jobs       │                                                       │
│  │ • Cron       │                                                       │
│  └──────────────┘                                                       │
└─────────────────────────────────────────────────────────────────────────┘
                             │
                             ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                      CAMADA DE DADOS                                     │
│                                                                          │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  ┌────────────┐ │
│  │  PostgreSQL  │  │   MongoDB    │  │    Redis     │  │Elasticsearch│ │
│  │              │  │              │  │              │  │            │ │
│  │ • Auth       │  │ • Ledger     │  │ • Cache      │  │ • Search   │ │
│  │ • Reporting  │  │ • Fraud      │  │ • Sessions   │  │ • Analytics│ │
│  │ • Scheduler  │  │ • Notif.     │  │ • Rate Limit │  │            │ │
│  └──────────────┘  └──────────────┘  └──────────────┘  └────────────┘ │
│                                                                          │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐                 │
│  │   RabbitMQ   │  │    Kafka     │  │    MinIO     │                 │
│  │              │  │              │  │              │                 │
│  │ • Commands   │  │ • Event Log  │  │ • Files      │                 │
│  │ • DLQ        │  │ • Streams    │  │ • S3 API     │                 │
│  │ • Retry      │  │ • Replay     │  │              │                 │
│  └──────────────┘  └──────────────┘  └──────────────┘                 │
└─────────────────────────────────────────────────────────────────────────┘
                             │
                             ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                      OBSERVABILIDADE                                     │
│                                                                          │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐                 │
│  │  Prometheus  │  │   Grafana    │  │    Jaeger    │                 │
│  │              │  │              │  │              │                 │
│  │ • Métricas   │  │ • Dashboards │  │ • Tracing    │                 │
│  │ • Alertas    │  │ • Alertas    │  │ • Spans      │                 │
│  └──────────────┘  └──────────────┘  └──────────────┘                 │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## 🔄 Fluxo de Dados

### 1. Upload de Arquivo

```
Frontend → API Gateway → Ingestion Service
                              │
                              ├─▶ MinIO (armazena arquivo)
                              │
                              └─▶ RabbitMQ (publica evento "file.processed")
                                      │
                                      ▼
                              Matching Service (consome evento)
```

### 2. Conciliação (Matching)

```
Matching Service
    │
    ├─▶ Executa estratégias de matching
    │   ├─ ExactMatch
    │   ├─ FuzzyMatch
    │   ├─ DateRange
    │   └─ AmountTolerance
    │
    ├─▶ Calcula score (0.0 a 1.0)
    │
    ├─▶ Resolve conflitos
    │
    └─▶ Kafka (publica evento "match.found")
            │
            ├─▶ Ledger Service (Event Sourcing)
            │
            └─▶ Fraud Service (análise de fraude)
```

### 3. Detecção de Fraude

```
Fraud Service
    │
    ├─▶ Aplica regras de fraude
    │   ├─ Duplicate Payment
    │   ├─ Unusual Amount
    │   ├─ High Frequency
    │   ├─ Round Amount
    │   └─ Unusual Time
    │
    ├─▶ Calcula risco (LOW, MEDIUM, HIGH, CRITICAL)
    │
    └─▶ RabbitMQ (publica evento "fraud.alert")
            │
            ▼
    Notification Service
            │
            ├─▶ Email
            ├─▶ Slack
            ├─▶ Webhook
            └─▶ WebSocket (tempo real para frontend)
```

### 4. Geração de Relatório

```
Frontend → API Gateway → Reporting Service
                              │
                              ├─▶ Busca dados (PostgreSQL + Elasticsearch)
                              │
                              ├─▶ Gera relatório (PDF ou Excel)
                              │
                              └─▶ Armazena no MinIO
                                      │
                                      ▼
                              Frontend (download)
```

---

## 🗄️ Polyglot Persistence

Cada serviço usa o banco de dados mais adequado:

| Serviço | Banco | Motivo |
|---------|-------|--------|
| **Auth** | PostgreSQL | Dados relacionais (usuários, roles) |
| **Ingestion** | MinIO | Armazenamento de arquivos (S3-compatible) |
| **Matching** | - | Stateless (apenas eventos) |
| **Ledger** | MongoDB | Event Store (append-only, documentos) |
| **Fraud** | MongoDB + Redis | Alertas (documentos) + Cache (estatísticas) |
| **Notification** | MongoDB | Log de notificações (documentos) |
| **Reporting** | PostgreSQL + Elasticsearch | Dados agregados + Busca full-text |
| **Scheduler** | PostgreSQL | Jobs do Quartz (tabelas específicas) |

---

## 📨 Mensageria

### RabbitMQ (Commands)
Usado para **comandos** que precisam de garantia de entrega:

- `file.processed` - Arquivo processado
- `fraud.alert` - Alerta de fraude detectado
- Dead Letter Queue (DLQ) para mensagens com falha
- Retry automático com backoff exponencial

### Kafka (Event Log)
Usado para **eventos** que precisam ser persistidos:

- `match.found` - Match encontrado
- `ledger.updated` - Ledger atualizado
- Log imutável de eventos
- Capacidade de replay

---

## 🔐 Segurança

### Autenticação
- **Keycloak** como Identity Provider
- **OAuth2/OIDC** para autenticação
- **JWT** (Access Token + Refresh Token)
- **API Keys** para autenticação máquina-a-máquina

### Autorização
- **Roles:** ADMIN, ANALYST, VIEWER, API_CLIENT
- **Multi-tenant:** Isolamento por `tenant_id`
- **Row-Level Security** no PostgreSQL

### Comunicação
- **HTTPS** em produção
- **TLS** para comunicação entre serviços
- **Secrets** gerenciados via Spring Cloud Config

---

## 🛡️ Resiliência

### Circuit Breaker (Resilience4j)
Protege contra falhas em cascata:

```java
@CircuitBreaker(name = "matchingService", fallbackMethod = "fallback")
public MatchResult getMatch(String id) {
    // Chamada ao serviço
}
```

### Retry
Retry automático com backoff exponencial:

```java
@Retry(name = "matchingService", fallbackMethod = "fallback")
public MatchResult getMatch(String id) {
    // Chamada ao serviço
}
```

### Timeout
Timeout configurável por serviço:

```yaml
resilience4j:
  timelimiter:
    instances:
      matchingService:
        timeout-duration: 3s
```

### Idempotência
Garantida via Redis (mesmo arquivo nunca processado 2x):

```java
if (redis.exists("file:" + fileId)) {
    return "Already processed";
}
redis.set("file:" + fileId, "processing", 3600);
```

---

## 📊 Observabilidade

### Métricas (Prometheus)
- Métricas de JVM (memória, threads, GC)
- Métricas de HTTP (latência, throughput, erros)
- Métricas de negócio (taxa de conciliação, alertas)

### Dashboards (Grafana)
- Reconix Overview
- JVM Metrics
- HTTP Metrics
- RabbitMQ Metrics
- Kafka Metrics
- Business Metrics

### Tracing (Jaeger)
- Distributed tracing entre microsserviços
- Correlation ID propagado em toda a cadeia
- Visualização de latência por serviço

### Logs (ELK Stack)
- Logs centralizados
- Busca e análise
- Alertas baseados em logs

---

## 🚀 Escalabilidade

### Horizontal Scaling
Todos os serviços são **stateless** e podem ser escalados horizontalmente:

```bash
# Kubernetes
kubectl scale deployment matching-service --replicas=5
```

### Load Balancing
- **API Gateway** faz load balancing automático
- **Eureka** registra múltiplas instâncias
- **Spring Cloud LoadBalancer** distribui requisições

### Caching
- **Redis** para cache de dados frequentes
- **Spring Cache** com TTL configurável
- **HTTP Cache** no API Gateway

### Async Processing
- **RabbitMQ** para processamento assíncrono
- **Kafka** para streaming de eventos
- **Spring @Async** para operações não-bloqueantes

---

## 🎯 Padrões de Design

### Criacionais
- **Factory** - Criação de parsers por tipo de arquivo
- **Builder** - Construção de objetos complexos
- **Singleton** - Configurações globais

### Estruturais
- **Adapter** - Adaptação de APIs externas
- **Facade** - Simplificação de subsistemas complexos
- **Proxy** - Circuit Breaker, Cache

### Comportamentais
- **Strategy** - Estratégias de matching
- **Observer** - Event-Driven Architecture
- **Chain of Responsibility** - Cadeia de estratégias
- **Template Method** - Fluxos de processamento

---

## 📐 Princípios SOLID

- **S**ingle Responsibility - Cada classe tem uma única responsabilidade
- **O**pen/Closed - Aberto para extensão, fechado para modificação
- **L**iskov Substitution - Subtipos substituíveis
- **I**nterface Segregation - Interfaces específicas
- **D**ependency Inversion - Dependa de abstrações

---

## 🏆 Boas Práticas

- ✅ **Clean Code** - Código limpo e legível
- ✅ **DRY** - Don't Repeat Yourself
- ✅ **KISS** - Keep It Simple, Stupid
- ✅ **YAGNI** - You Aren't Gonna Need It
- ✅ **12-Factor App** - Metodologia para SaaS
- ✅ **Conventional Commits** - Commits padronizados
- ✅ **Semantic Versioning** - Versionamento semântico
- ✅ **API First** - Design de API antes da implementação
- ✅ **Documentation as Code** - Documentação versionada

---

## 📚 Referências

- [Microservices Patterns](https://microservices.io/patterns/)
- [Domain-Driven Design](https://martinfowler.com/bliki/DomainDrivenDesign.html)
- [Event Sourcing](https://martinfowler.com/eaaDev/EventSourcing.html)
- [CQRS](https://martinfowler.com/bliki/CQRS.html)
- [12-Factor App](https://12factor.net/)
- [Spring Cloud](https://spring.io/projects/spring-cloud)
- [React Documentation](https://react.dev/)

---

**Desenvolvido com ❤️ para demonstrar as melhores práticas de arquitetura de software**

