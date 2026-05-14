# 📘 Reconix Ledger Service

Serviço responsável pelo livro-razão imutável do sistema Reconix, implementando Event Sourcing e CQRS (Command Query Responsibility Segregation). Este serviço armazena todos os eventos do sistema como fatos imutáveis e fornece projeções otimizadas para leitura.

## 🎯 Objetivo

O serviço de ledger é o **registro imutável** de todos os eventos do sistema Reconix. Ele implementa Event Sourcing para manter um histórico completo de todas as mudanças de estado e utiliza CQRS para fornecer projeções otimizadas para diferentes necessidades de leitura.

## 🏗️ Arquitetura

### Componentes Principais

- **Event Store**: Armazenamento persistente dos eventos em MongoDB
- **Aggregate Roots**: Representação dos estados reconstruídos a partir dos eventos
- **Projections**: Visualizações otimizadas para leitura (transações, matches, divergências, fraudes)
- **Domain Events**: Fatos imutáveis que representam mudanças de estado

### Tipos de Eventos Suportados

1. **TransactionCreatedEvent**: Quando uma nova transação é criada
2. **TransactionMatchedEvent**: Quando duas transações são pareadas
3. **TransactionUnmatchedEvent**: Quando uma transação não encontra correspondência
4. **DivergenceDetectedEvent**: Quando uma divergência é detectada entre transações
5. **FraudSuspectedEvent**: Quando uma possível fraude é detectada

## 📨 Comunicação

### Consumo de Eventos (Kafka)
- `match.found`: Recebe eventos de matches encontrados do serviço de matching

## 🚀 Como Executar

### Pré-requisitos
- Java 21+
- MongoDB (para armazenamento de eventos)
- Kafka (para consumo de eventos)
- Eureka Server (para descoberta de serviços)

### Execução Local
```bash
./mvnw spring-boot:run
```

### Variáveis de Ambiente
- `MONGODB_URI`: URI do MongoDB (padrão: mongodb://localhost:27017/reconix_ledger)
- `KAFKA_BOOTSTRAP_SERVERS`: Servidores do Kafka (padrão: localhost:9092)
- `EUREKA_SERVER_URL`: URL do servidor Eureka (padrão: http://localhost:8761/eureka)

## 📊 Endpoints da API

### Consulta de Eventos
- `GET /api/v1/ledger/events/{transactionId}` - Eventos de uma transação específica
- `GET /api/v1/ledger/tenant/{tenantId}/events` - Eventos de um tenant específico

### Consulta de Estados
- `GET /api/v1/ledger/transaction/{transactionId}` - Estado atual de uma transação
- `GET /api/v1/ledger/tenant/{tenantId}/transactions` - Todas as transações de um tenant
- `GET /api/v1/ledger/tenant/{tenantId}/matched` - Transações pareadas de um tenant
- `GET /api/v1/ledger/tenant/{tenantId}/unmatched` - Transações não pareadas de um tenant
- `GET /api/v1/ledger/tenant/{tenantId}/divergences` - Transações com divergências
- `GET /api/v1/ledger/tenant/{tenantId}/frauds` - Transações com suspeita de fraude
- `GET /api/v1/ledger/statistics` - Estatísticas de eventos

## 🔄 Event Sourcing

O Event Sourcing funciona da seguinte forma:

1. **Eventos Imutáveis**: Todos os eventos são armazenados permanentemente como fatos
2. **Replay de Eventos**: Os estados são reconstruídos aplicando os eventos em ordem
3. **Auditoria Completa**: Histórico completo de todas as mudanças
4. **Consistência Eventual**: Atualizações de projeções podem ter pequeno atraso

## 🛡️ Segurança

- **Multi-tenant**: Isolamento completo de dados por tenant
- **JWT Tokens**: Autenticação via tokens JWT
- **OAuth2**: Integração com Keycloak para gerenciamento de identidade
- **Criptografia**: Dados sensíveis criptografados em repouso

## 📈 Benefícios do Event Sourcing

- **Auditoria Completa**: Histórico de todas as mudanças de estado
- **Capacidade de Replay**: Possibilidade de reconstruir estados antigos
- **Análise de Causa Raiz**: Compreensão completa de como o estado atual foi atingido
- **Escalabilidade**: Arquitetura orientada a eventos altamente escalável
- **Consistência**: Eventos imutáveis garantem integridade dos dados