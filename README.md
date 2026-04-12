# 🏦 Reconix — Plataforma de Reconciliação Financeira Inteligente

![Java CI](https://github.com/RafaelSantana-Dev/reconix-platform/actions/workflows/ci.yml/badge.svg)
![Java](https://img.shields.io/badge/Java-21-orange?style=flat&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3-brightgreen?style=flat&logo=spring-boot&logoColor=white)
![Spring Cloud](https://img.shields.io/badge/Spring_Cloud-2023.0-brightgreen?style=flat&logo=spring&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?style=flat&logo=postgresql&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-7-green?style=flat&logo=mongodb&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-7-red?style=flat&logo=redis&logoColor=white)
![Elasticsearch](https://img.shields.io/badge/Elasticsearch-8-yellow?style=flat&logo=elasticsearch&logoColor=white)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-3.13-orange?style=flat&logo=rabbitmq&logoColor=white)
![Kafka](https://img.shields.io/badge/Kafka-3.7-black?style=flat&logo=apachekafka&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Compose-blue?style=flat&logo=docker&logoColor=white)
![React](https://img.shields.io/badge/React-18-blue?style=flat&logo=react&logoColor=white)
![TypeScript](https://img.shields.io/badge/TypeScript-5-blue?style=flat&logo=typescript&logoColor=white)
![Keycloak](https://img.shields.io/badge/Keycloak-24-blue?style=flat&logo=keycloak&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-green?style=flat)

**Sistema SaaS B2B** de reconciliação financeira automatizada, construído com **arquitetura de microsserviços orientados a eventos**, observabilidade completa, detecção de fraudes e dashboard em tempo real.

O Reconix recebe extratos bancários, notas fiscais e registros internos de empresas, **cruza tudo automaticamente em segundos**, encontra divergências, detecta possíveis fraudes e gera relatórios de conciliação — eliminando dias de trabalho manual do setor financeiro.

---

## 💡 O Problema que o Reconix Resolve

| Sem o Reconix | Com o Reconix |
|---|---|
| Analista abre extrato bancário | Analista faz upload dos arquivos |
| Abre o ERP da empresa | Sistema cruza tudo em 12 segundos |
| Abre as notas fiscais | Divergências são destacadas |
| Compara **LINHA POR LINHA** | Fraudes detectadas por IA |
| 500 transações, 6 fontes | Relatório pronto para o CFO |
| Resultado: **2 a 3 DIAS** de trabalho | Resultado: **15 MINUTOS** |
| Erros humanos, dinheiro perdido | Zero erro, zero fraude |

---

## 🏗️ Arquitetura (Microsserviços Cloud-Native)

O sistema é composto por **8 microsserviços independentes**, cada um com seu próprio banco de dados, comunicando-se via eventos assíncronos (`RabbitMQ` + `Kafka`), orquestrados por um `API Gateway` e registrados em um `Service Discovery`.

### Microsserviços:

| Serviço | Responsabilidade |
|---|---|
| **Gateway** | Roteamento, autenticação, rate limiting, correlation ID |
| **Auth** | Autenticação via Keycloak (OAuth2/OIDC), multi-tenant, roles |
| **Ingestion** | Upload e parsing de arquivos (OFX, CSV, XLSX, XML, JSON) |
| **Matching** | Algoritmo de conciliação com scoring e fuzzy matching |
| **Ledger** | Livro-razão imutável com Event Sourcing e CQRS |
| **Reporting** | Geração de relatórios (PDF, Excel), dashboard, busca full-text |
| **Fraud Detection** | Detecção de anomalias e fraudes via regras e ML simplificado |
| **Notification** | Alertas via e-mail, Slack, webhook e WebSocket |

---

## ⚙️ Funcionalidades Principais

### 📥 Ingestão Inteligente de Dados
- Upload de arquivos via **drag & drop** (OFX, CSV, XLSX, XML, JSON)
- Ingestão via **API/Webhook** (integração com ERPs e gateways de pagamento)
- **Parsers especializados** por formato (Factory Pattern)
- Normalização automática de moeda, data e encoding
- Armazenamento dos arquivos originais no **MinIO** (S3-compatible)
- Validação de tipo, tamanho e integridade do arquivo

### 🧠 Motor de Conciliação (O Cérebro)
- **Algoritmo de matching score-based** (0.0 a 1.0)
- **Fuzzy matching** com distância de Levenshtein para descrições
- **Tolerância configurável** por valor (centavos) e data (D+1, D+2)
- **Resolução de conflitos** para matches ambíguos (1:1 e 1:N)
- **Strategy Pattern:** ExactMatch, FuzzyMatch, DateRange, AmountTolerance
- **Chain of Responsibility:** Estratégias em cadeia com peso configurável
- Status por transação: `MATCHED`, `PARTIAL_MATCH`, `UNMATCHED`

### 🔍 Detecção de Fraudes
- **Rule Engine** com regras configuráveis por tenant:
  - Pagamento duplicado
  - Valor fora do padrão histórico
  - Alta frequência de transações
  - Valores redondos suspeitos
  - Transações em horários atípicos
- **Detecção de anomalias estatísticas** (desvio padrão, z-score)
- Classificação de risco: `LOW`, `MEDIUM`, `HIGH`, `CRITICAL`
- Alertas automáticos em tempo real via WebSocket

### 📒 Livro-Razão (Event Sourcing + CQRS)
- **Cada evento do sistema é salvo como fato imutável** (append-only)
- Eventos: `IngestionCompleted`, `MatchFound`, `DivergenceDetected`, `FraudSuspected`
- **Projeções de leitura** otimizadas (CQRS): saldo, timeline, relatórios
- Capacidade de **reconstruir o estado** a partir do replay de eventos
- **Auditoria completa:** quem fez o quê, quando e por quê

### 📊 Dashboard e Relatórios
- Dashboard em **tempo real** com gráficos de conciliação (React + WebSocket)
- **Busca full-text** em transações via Elasticsearch
- Geração de relatórios em **PDF** (JasperReports) e **Excel** (Apache POI)
- Relatórios automáticos agendados (diário/semanal)
- Exportação para **MinIO/S3**
- KPIs: taxa de conciliação, divergências por período, valor em aberto

### 🔐 Segurança Enterprise
- **Keycloak** como Identity Provider (OAuth2/OIDC)
- JWT (Access Token + Refresh Token)
- **Multi-tenant:** cada empresa vê apenas seus dados (isolamento por `tenant_id`)
- Roles: `ADMIN`, `ANALYST`, `VIEWER`, `API_CLIENT`
- **API Keys** para autenticação máquina-a-máquina
- Criptografia **AES-256** para dados financeiros sensíveis
- Rate Limiting por tenant via Redis

### 📡 Observabilidade Completa
- **Prometheus** coletando métricas de todos os serviços
- **Grafana** com dashboards pré-configurados
- **Jaeger** para distributed tracing (rastrear uma request entre microsserviços)
- **ELK Stack** (Elasticsearch + Logstash + Kibana) para logs centralizados
- **Correlation ID** propagado em toda a cadeia de requisições
- **Health checks** via Spring Actuator

### 🛡️ Resiliência
- **Circuit Breaker** (Resilience4j) entre microsserviços
- **Dead Letter Queue (DLQ)** para mensagens com falha
- **Retry com Backoff Exponencial** automático
- **Idempotência** garantida via Redis (mesmo arquivo nunca processado 2x)
- **Graceful Shutdown** em todos os serviços

---

## 🧪 Stack Tecnológica Completa

### Backend (Java)
- **Java 21** (Records, Sealed Classes, Pattern Matching, Virtual Threads)
- **Spring Boot 3.3**
- **Spring Cloud Gateway** (API Gateway)
- **Spring Cloud Netflix Eureka** (Service Discovery)
- **Spring Cloud Config** (Configuração centralizada via Git)
- **Spring Cloud OpenFeign** (Comunicação síncrona entre serviços)
- **Spring Security + OAuth2/OIDC** (Keycloak)
- **Spring AMQP** (RabbitMQ)
- **Spring Kafka** (Apache Kafka)
- **Spring Data JPA** (PostgreSQL)
- **Spring Data MongoDB**
- **Spring Data Redis**
- **Spring Data Elasticsearch**
- **Spring WebSocket** (Tempo real)
- **Spring Mail** (E-mails)
- **Spring Batch** (Processamento em lote)
- **Spring Actuator** (Health checks + métricas)
- **Resilience4j** (Circuit Breaker, Retry, Bulkhead, Rate Limiter)
- **Flyway** (Migrations SQL)
- **MapStruct** (Mapeamento de DTOs)
- **Quartz Scheduler** (Agendamento de tarefas)

### Frontend
- **React 18** + **TypeScript 5**
- **Tailwind CSS 3**
- **Zustand** (State Management)
- **TanStack Query** (Cache de requisições)
- **Recharts** (Gráficos)
- **React Hook Form + Zod** (Formulários + validação)
- **Axios** (HTTP Client)
- **Vitest + Testing Library** (Testes frontend)

### Bancos de Dados (Polyglot Persistence)
- **PostgreSQL 16** (Dados transacionais + Ledger)
- **MongoDB 7** (Event Store + documentos + logs)
- **Redis 7** (Cache, sessões, idempotência, rate limiting)
- **Elasticsearch 8** (Full-text search + analytics)

### Mensageria (Event-Driven Architecture)
- **RabbitMQ 3.13** (Commands + DLQ + Retry + TTL)
- **Apache Kafka 3.7** (Event Log imutável + Streams)

### Segurança
- **Keycloak 24** (Identity Provider — OAuth2/OIDC)
- **JWT** (Access Token + Refresh Token)
- **BCrypt** (Hash de senhas)
- **AES-256** (Criptografia de dados sensíveis)
- **API Keys** (Autenticação máquina-a-máquina)

### Observabilidade
- **Prometheus** (Métricas)
- **Grafana** (Dashboards de monitoramento)
- **Jaeger** (Distributed Tracing)
- **OpenTelemetry** (Instrumentação padronizada)
- **ELK Stack:** Elasticsearch + Logstash + Kibana (Logs centralizados)
- **Micrometer** (Métricas customizadas)

### Testes
- **JUnit 5** (Unitários)
- **Mockito** (Mocks)
- **Testcontainers** (Integração com banco/fila reais em Docker)
- **WireMock** (Mock de APIs externas)
- **ArchUnit** (Testes de arquitetura)
- **Spring Cloud Contract** (Contract Tests entre microsserviços)
- **Gatling** (Testes de carga e performance)
- **PITest** (Mutation Testing)

### Infraestrutura & DevOps
- **Docker** (Container por serviço)
- **Docker Compose** (Orquestração local)
- **Nginx** (Reverse Proxy + Load Balancer)
- **MinIO** (Object Storage S3-compatible)
- **GitHub Actions** (CI/CD Pipeline)
- **SonarQube** (Code Quality + Coverage)
- **Trivy** (Security Scan de containers)

### Padrões & Princípios
- **Domain-Driven Design (DDD)** — Aggregates, Value Objects, Domain Events
- **Event Sourcing** — Estado reconstruído a partir de eventos
- **CQRS** — Separação de modelos de escrita e leitura
- **Event-Driven Architecture** — Comunicação assíncrona entre serviços
- **SOLID Principles**
- **Clean Code**
- **12-Factor App**
- **Design Patterns:** Strategy, Factory, Observer, Chain of Responsibility, Builder, Template Method
- **Architecture Decision Records (ADR)**
- **Conventional Commits + Semantic Versioning**
- **RFC 7807** (Problem Detail for HTTP APIs)

---

## 📦 Estrutura do Projeto

```text
reconix-platform/
├── infrastructure/
│   ├── docker-compose.infra.yml
│   ├── docker-compose.monitoring.yml
│   ├── nginx/
│   ├── prometheus/
│   ├── grafana/
│   └── keycloak/
│
├── reconix-discovery/           # Eureka Server
├── reconix-config/              # Spring Cloud Config Server
├── reconix-gateway/             # API Gateway + Filters
├── reconix-auth/                # Autenticação (Keycloak + OAuth2)
│
├── reconix-ingestion/           # Ingestão e parsing de arquivos
│   └── src/main/java/.../
│       ├── controller/          # Endpoints de upload
│       ├── service/             # Lógica de negócio de ingestão
│       ├── dto/                 # DTOs imutáveis (Records)
│       ├── producer/            # Publicação de eventos RabbitMQ
│       ├── service/             # Serviços de armazenamento
│       └── config/              # Configuração do MinIO
│
├── reconix-matching/            # Motor de conciliação
│   └── src/main/java/.../
│       ├── controller/          # Endpoints de matching
│       ├── consumer/            # Consumo de eventos RabbitMQ
│       ├── producer/            # Publicação de eventos Kafka
│       ├── engine/              # Motor de matching principal
│       │   ├── strategy/        # ExactMatch, FuzzyMatch, DateRange, AmountTolerance
│       │   ├── scorer/          # Algoritmos de similaridade (Levenshtein)
│       │   └── impl/            # Implementações concretas
│       ├── resolver/            # Resolvedor de conflitos
│       ├── service/             # Serviço de matching
│       ├── domain/              # Modelos de domínio
│       └── dto/                 # DTOs para eventos e resultados
│
├── reconix-ledger/              # Event Sourcing + CQRS
│   └── src/main/java/.../
│       ├── controller/          # Endpoints de consulta
│       ├── consumer/            # Consumo de eventos Kafka
│       ├── eventstore/          # Repositório de eventos MongoDB
│       │   └── repository/      # Repositório Spring Data
│       ├── projection/          # Projeções CQRS para leitura
│       ├── service/             # Serviço de ledger
│       ├── domain/              # Modelos de agregação
│       └── dto/                 # DTOs de eventos imutáveis
│
├── reconix-fraud/               # Detecção de fraudes
│   └── src/main/java/.../
│       ├── consumer/
│       ├── detector/
│       │   ├── rules/           # Rule Engine
│       │   └── ml/              # Anomaly Detection
│       ├── producer/
│       └── domain/
│
├── reconix-reporting/           # Relatórios + Dashboard
│   └── src/main/java/.../
│       ├── consumer/
│       ├── generator/           # PDF, Excel, JSON
│       ├── dashboard/
│       ├── search/              # Elasticsearch
│       └── exporter/            # MinIO/S3
│
├── reconix-notification/        # Notificações multicanal
│   └── src/main/java/.../
│       ├── consumer/
│       ├── channel/             # Email, Slack, Webhook, WebSocket
│       └── template/
│
├── reconix-scheduler/           # Jobs e retry automático
│   └── src/main/java/.../
│       ├── job/                 # Quartz Jobs
│       └── config/
│
├── reconix-frontend/            # React + TypeScript
│   └── src/
│       ├── pages/
│       │   ├── Dashboard/
│       │   ├── Upload/
│       │   ├── Reconciliation/
│       │   ├── FraudCenter/
│       │   ├── Reports/
│       │   └── Settings/
│       ├── components/
│       ├── hooks/
│       ├── services/
│       └── store/
│
├── shared-libs/
│   ├── reconix-common/          # DTOs, exceções, Value Objects
│   └── reconix-observability/   # Logs, métricas, tracing
│
├── docs/
│   ├── architecture.md
│   ├── adr/                     # Architecture Decision Records
│   ├── api/                     # OpenAPI specs por serviço
│   └── runbooks/
│
├── .github/workflows/
│   ├── ci.yml
│   ├── cd.yml
│   ├── security-scan.yml
│   └── code-quality.yml
│
├── Makefile
├── README.md
└── LICENSE
```

---

## 🚀 Como Executar o Projeto

### ✅ Pré-requisitos
- **Java 21**
- **Node.js 20+** (para o frontend)
- **Docker** e **Docker Compose**
- **Git**

### 1️⃣ Clonar o repositório
```bash
git clone https://github.com/RafaelSantana-Dev/reconix-platform.git
cd reconix-platform
```

### 2️⃣ Subir toda a infraestrutura
```bash
docker compose -f infrastructure/docker-compose.infra.yml up -d
```
Isso sobe: PostgreSQL, MongoDB, Redis, Elasticsearch, RabbitMQ, Kafka, MinIO e Keycloak.

### 3️⃣ Subir o monitoramento (opcional)
```bash
docker compose -f infrastructure/docker-compose.monitoring.yml up -d
```
Isso sobe: Prometheus, Grafana e Jaeger.

### 4️⃣ Iniciar os microsserviços (na ordem correta)

**1. Config Server:**
```bash
cd reconix-config && ./mvnw spring-boot:run
```

**2. Discovery Server (Eureka):**
```bash
cd reconix-discovery && ./mvnw spring-boot:run
```

**3. API Gateway:**
```bash
cd reconix-gateway && ./mvnw spring-boot:run
```

**4. Demais serviços (em terminais separados):**
```bash
cd reconix-auth && ./mvnw spring-boot:run
cd reconix-ingestion && ./mvnw spring-boot:run
cd reconix-matching && ./mvnw spring-boot:run
cd reconix-ledger && ./mvnw spring-boot:run
cd reconix-fraud && ./mvnw spring-boot:run
cd reconix-reporting && ./mvnw spring-boot:run
cd reconix-notification && ./mvnw spring-boot:run
cd reconix-scheduler && ./mvnw spring-boot:run
```

### 5️⃣ Iniciar o Frontend
```bash
cd reconix-frontend && npm install && npm run dev
```

### 🌐 URLs de Acesso

| Serviço | URL |
|---|---|
| **Frontend (Dashboard)** | http://localhost:5173 |
| **API Gateway** | http://localhost:8080 |
| **Eureka Dashboard** | http://localhost:8761 |
| **Keycloak (Admin)** | http://localhost:8180 |
| **RabbitMQ Management** | http://localhost:15672 |
| **Grafana** | http://localhost:3000 |
| **Jaeger UI** | http://localhost:16686 |
| **Kibana** | http://localhost:5601 |
| **MinIO Console** | http://localhost:9001 |
| **Swagger (por serviço)** | http://localhost:{porta}/swagger-ui.html |

---

## 🧪 Testes

### Rodar testes unitários
```bash
make test-unit
```

### Rodar testes de integração (requer Docker)
```bash
make test-integration
```

### Rodar todos os testes
```bash
make test-all
```

---

## 🧭 Boas Práticas Adotadas

- 📐 **Domain-Driven Design (DDD):** Aggregates, Value Objects, Domain Events e Bounded Contexts.
- 📨 **Event-Driven Architecture:** Comunicação assíncrona entre serviços via RabbitMQ e Kafka.
- 📒 **Event Sourcing:** Estado reconstruído a partir de eventos imutáveis no Ledger.
- 🔀 **CQRS:** Modelos separados para escrita (commands) e leitura (queries).
- 🛡️ **DTOs imutáveis:** `Records` do Java 21 para entrada e saída de dados.
- 🗄️ **Polyglot Persistence:** Cada serviço usa o banco mais adequado à sua necessidade.
- 🚨 **RFC 7807:** Respostas de erro padronizadas globalmente (`ProblemDetail`).
- 🔒 **Multi-tenant:** Isolamento completo de dados por empresa.
- 📊 **Observabilidade:** Métricas, logs e tracing distribuído em todos os serviços.
- 🔄 **CI/CD:** Pipeline completo com build, testes, scan de segurança e quality gate.
- 📑 **ADR:** Decisões arquiteturais documentadas formalmente.
- 📝 **Conventional Commits + Semantic Versioning.**

---

---

## 🧠 Como o Algoritmo de Matching Funciona

O motor de conciliação é o coração do Reconix. Ele cruza transações de diferentes fontes usando um sistema de **pontuação (score)** de 0.0 a 1.0:

### Exemplo prático:

| Campo | Extrato Bancário | Registro Interno (ERP) | Score |
|---|---|---|---|
| **Data** | 2025-01-15 | 2025-01-15 | `1.0` (100% match) |
| **Valor** | R$ 1.500,00 | R$ 1.500,00 | `1.0` (100% match) |
| **Descrição** | "PAG FORNEC" | "Pagamento Fornecedor ABC" | `0.78` (78% similar) |
| | | **Score Final** | **0.93** ✅ `MATCHED` |

### Outro exemplo (divergência):

| Campo | Extrato Bancário | Registro Interno (ERP) | Score |
|---|---|---|---|
| **Data** | 2025-01-20 | 2025-01-20 | `1.0` (100% match) |
| **Valor** | R$ 3.200,00 | R$ 3.000,00 | `0.45` ❌ (diferença de R$ 200) |
| **Descrição** | "TED RECEBI" | "Recebimento Cliente XYZ" | `0.72` (72% similar) |
| | | **Score Final** | **0.61** ⚠️ `PARTIAL_MATCH` |

### Estratégias de matching (Strategy Pattern):

```text
Transação A (banco) + Transação B (ERP)
            │
            ▼
   ┌─────────────────┐
   │  ExactMatch      │ → Valor e data são iguais? (+0.4)
   └────────┬─────────┘
            ▼
   ┌─────────────────┐
   │  FuzzyMatch      │ → Descrição é similar? (Levenshtein) (+0.3)
   └────────┬─────────┘
            ▼
   ┌─────────────────┐
   │  DateRange       │ → Data está dentro de D+1 ou D+2? (+0.15)
   └────────┬─────────┘
            ▼
   ┌─────────────────┐
   │  AmountTolerance │ → Diferença de valor < R$ 0,50? (+0.15)
   └────────┬─────────┘
            ▼
      Score Final (0.0 a 1.0)
            │
     ┌──────┼──────┐
     │      │      │
  ≥ 0.85  0.5-0.84  < 0.5
     │      │      │
  MATCHED  PARTIAL  UNMATCHED
     ✅     ⚠️      ❌
```

---

## 📨 Fluxo de Eventos entre Microsserviços

Toda a comunicação entre os serviços acontece via eventos assíncronos. Aqui está o fluxo completo de uma reconciliação:

```text
┌──────────┐     ┌──────────┐     ┌──────────┐     ┌──────────┐
│ Ingestion│     │ Matching │     │  Ledger  │     │ Reporting│
│ Service  │     │  Engine  │     │ Service  │     │ Service  │
└────┬─────┘     └────┬─────┘     └────┬─────┘     └────┬─────┘
     │                │                │                │
     │ 1. Upload      │                │                │
     │    arquivo      │                │                │
     │────────────>    │                │                │
     │                │                │                │
     │ 2. Publica:    │                │                │
     │ "FileProcessed"│                │                │
     │ (RabbitMQ)     │                │                │
     │────────────────>│               │                │
     │                │                │                │
     │                │ 3. Executa     │                │
     │                │    matching    │                │
     │                │────────>       │                │
     │                │                │                │
     │                │ 4. Publica:    │                │
     │                │ "MatchFound"   │                │
     │                │ (Kafka)        │                │
     │                │────────────────>│               │
     │                │                │                │
     │                │                │ 5. Salva       │
     │                │                │    evento      │
     │                │                │    (append)    │
     │                │                │                │
     │                │                │ 6. Publica:    │
     │                │                │ "LedgerUpdated"│
     │                │                │────────────────>│
     │                │                │                │
     │                │                │                │ 7. Atualiza
     │                │                │                │    dashboard
     │                │                │                │
     │                │ 8. Se fraude   │                │
     │                │    detectada:  │                │
     │                │────────────────────────────────>│
     │                │ "FraudAlert"   │                │ 9. Notifica
     │                │ (RabbitMQ)     │                │    analista
     │                │                │                │    (WebSocket)
```

### Por que usar RabbitMQ E Kafka?

| Mensageria | Usado para | Motivo |
|---|---|---|
| **RabbitMQ** | Commands (ações) | Garantia de entrega, DLQ, retry. Ideal para comandos como "processar arquivo" |
| **Kafka** | Event Log (fatos) | Log imutável, replay de eventos. Ideal para Event Sourcing no Ledger |

---

## 🔧 Variáveis de Ambiente

O sistema utiliza variáveis de ambiente para configuração. Nunca faça `commit` de credenciais reais.

### Infraestrutura

| Variável | Descrição | Valor padrão (dev) |
|---|---|---|
| `POSTGRES_USER` | Usuário do PostgreSQL | `reconix` |
| `POSTGRES_PASSWORD` | Senha do PostgreSQL | `reconix123` |
| `POSTGRES_DB` | Nome do banco | `reconix_db` |
| `MONGO_USER` | Usuário do MongoDB | `reconix` |
| `MONGO_PASSWORD` | Senha do MongoDB | `reconix123` |
| `REDIS_PASSWORD` | Senha do Redis | `reconix123` |
| `RABBITMQ_USER` | Usuário do RabbitMQ | `reconix` |
| `RABBITMQ_PASSWORD` | Senha do RabbitMQ | `reconix123` |

### Aplicação

| Variável | Descrição | Valor padrão (dev) |
|---|---|---|
| `JWT_SECRET` | Secret para assinatura de tokens JWT | `reconix-dev-secret` |
| `KEYCLOAK_URL` | URL do Keycloak | `http://localhost:8180` |
| `KEYCLOAK_REALM` | Realm do Keycloak | `reconix` |
| `MINIO_ACCESS_KEY` | Chave de acesso do MinIO | `reconix` |
| `MINIO_SECRET_KEY` | Chave secreta do MinIO | `reconix123` |
| `EUREKA_URL` | URL do Eureka Server | `http://localhost:8761/eureka` |
| `CONFIG_SERVER_URL` | URL do Config Server | `http://localhost:8888` |

---

## 📊 Monitoramento e Observabilidade

Após subir o stack de monitoramento (`docker compose -f infrastructure/docker-compose.monitoring.yml up -d`), acesse:

### Grafana (Dashboards visuais)
- **URL:** http://localhost:3000
- **Login:** `admin` / `admin`
- **Dashboards disponíveis:**
  - Reconix Overview (visão geral de todos os serviços)
  - JVM Metrics (memória, threads, GC)
  - HTTP Metrics (latência, throughput, erros)
  - RabbitMQ Metrics (filas, mensagens, consumers)

### Jaeger (Distributed Tracing)
- **URL:** http://localhost:16686
- **Como usar:** Selecione um serviço no dropdown, clique em "Find Traces" e veja o caminho completo de uma requisição passando por todos os microsserviços.

### Prometheus (Métricas brutas)
- **URL:** http://localhost:9090
- **Como usar:** Execute queries PromQL para consultar métricas de qualquer serviço.

---

## 📐 Architecture Decision Records (ADR)

Todas as decisões arquiteturais importantes são documentadas formalmente na pasta `docs/adr/`. Cada ADR explica **o contexto, a decisão tomada e as consequências**.

| ADR | Decisão | Status |
|---|---|---|
| [ADR-001](docs/adr/001-why-microservices.md) | Por que microsserviços ao invés de monolith? | ✅ Aceito |
| [ADR-002](docs/adr/002-why-event-sourcing.md) | Por que Event Sourcing no Ledger? | ✅ Aceito |
| [ADR-003](docs/adr/003-why-rabbitmq-and-kafka.md) | Por que usar RabbitMQ E Kafka (e não só um)? | ✅ Aceito |
| [ADR-004](docs/adr/004-why-multi-database.md) | Por que Polyglot Persistence (múltiplos bancos)? | ✅ Aceito |
| [ADR-005](docs/adr/005-why-keycloak.md) | Por que Keycloak ao invés de auth customizado? | ✅ Aceito |
| [ADR-006](docs/adr/006-matching-algorithm.md) | Como o algoritmo de matching foi projetado? | ✅ Aceito |

---

## 🗺️ Roadmap

### ✅ Fase 1 — Fundação (Concluída)
- [x] Estrutura do projeto e infraestrutura Docker
- [x] Config Server + Discovery Server + API Gateway
- [x] Autenticação com Keycloak (OAuth2/OIDC)
- [x] CI/CD com GitHub Actions

### ✅ Fase 2 — Core (Concluída)
- [x] Serviço de Ingestão (upload + parsing de arquivos) - Implementado
- [x] Serviço de Matching (algoritmo de conciliação) - Implementado
- [x] Serviço de Ledger (Event Sourcing + CQRS) - Implementado
- [x] Comunicação via RabbitMQ entre serviços - Implementado

### 📋 Fase 3 — Inteligência
- [ ] Serviço de Detecção de Fraudes (Rule Engine)
- [ ] Detecção de anomalias estatísticas
- [ ] Alertas em tempo real via WebSocket

### 📊 Fase 4 — Relatórios e Dashboard
- [ ] Serviço de Reporting (PDF, Excel)
- [ ] Frontend React + TypeScript
- [ ] Dashboard com gráficos em tempo real
- [ ] Busca full-text com Elasticsearch

### 🔭 Fase 5 — Produção
- [ ] Observabilidade completa (Prometheus + Grafana + Jaeger)
- [ ] Testes de carga com Gatling
- [ ] Contract Tests entre microsserviços
- [ ] SonarQube + Trivy no pipeline

---

## ❓ Troubleshooting / FAQ

### O Docker não sobe todos os containers
```bash
# Verifique se tem memória suficiente (mínimo 8GB RAM para rodar tudo)
docker stats

# Se algum container morreu, reinicie
docker compose -f infrastructure/docker-compose.infra.yml restart
```

### O Kafka não conecta
```bash
# Verifique se o Zookeeper subiu primeiro
docker logs reconix-zookeeper

# Reinicie o Kafka após o Zookeeper estar healthy
docker restart reconix-kafka
```

### O Keycloak não inicia
```bash
# O Keycloak depende do PostgreSQL estar healthy
# Verifique o PostgreSQL primeiro
docker logs reconix-postgres

# Depois reinicie o Keycloak
docker restart reconix-keycloak
```

### Porta já está em uso
```bash
# Descubra qual processo está usando a porta (ex: 8080)
netstat -ano | findstr :8080

# Mate o processo pelo PID
taskkill /PID <numero_do_pid> /F
```

### Como resetar tudo do zero
```bash
make clean
```

## 🤝 Como Contribuir

Contribuições são muito bem-vindas!

1. Faça um `fork` deste repositório.
2. Crie uma nova `branch`:
   ```bash
   git checkout -b feat/minha-feature
   ```
3. Faça as alterações e garanta que os testes passam:
   ```bash
   make test-all
   ```
4. Faça o `commit` (padrão *Conventional Commits*):
   ```bash
   git commit -m "feat(matching): add fuzzy matching strategy"
   ```
5. Envie para o seu `fork`:
   ```bash
   git push origin feat/minha-feature
   ```
6. Abra um **Pull Request** apontando para a branch `main`.

---

## 📄 Licença

Este projeto está licenciado sob a licença **MIT**.  
Consulte o arquivo `LICENSE` para mais detalhes.