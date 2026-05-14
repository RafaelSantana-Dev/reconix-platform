# 🧠 Reconix Matching Service

Serviço responsável pelo motor de conciliação (matching engine) do sistema Reconix. Este serviço implementa o algoritmo de matching para reconciliar transações financeiras provenientes de diferentes fontes (extratos bancários, ERP, notas fiscais, etc.).

## 🎯 Objetivo

O serviço de matching é o **cérebro** do sistema Reconix. Ele cruza transações de diferentes fontes usando um sistema de **pontuação (score)** de 0.0 a 1.0 para determinar o grau de similaridade entre transações e encontrar pares correspondentes.

## 🏗️ Arquitetura

### Componentes Principais

- **Matching Engine**: Orquestra as estratégias de matching e calcula pontuações
- **Matching Strategies**: Implementam diferentes abordagens de matching (Exact, Fuzzy, Date Range, Amount Tolerance)
- **String Similarity Scorer**: Algoritmos para calcular similaridade entre strings (Levenshtein, etc.)
- **Match Resolver**: Resolve conflitos quando múltiplas transações podem corresponder a uma única
- **Domain Model**: Representação das transações e entidades de negócio

### Estratégias de Matching

1. **Exact Match**: Verifica igualdade exata de valores, datas e moedas
2. **Fuzzy Match**: Calcula similaridade textual entre descrições usando algoritmos como Levenshtein
3. **Date Range**: Permite variação na data de transação dentro de um intervalo configurável
4. **Amount Tolerance**: Permite pequenas variações no valor da transação dentro de uma tolerância configurável

## 📨 Comunicação

### Consumo de Eventos (RabbitMQ)
- `file.processed`: Recebe eventos de arquivos processados do serviço de ingestão

### Produção de Eventos (Kafka)
- `match.found`: Publica eventos de matches encontrados para consumo por outros serviços

## 🚀 Como Executar

### Pré-requisitos
- Java 21+
- RabbitMQ (para consumo de eventos)
- Kafka (para produção de eventos)
- Eureka Server (para descoberta de serviços)

### Execução Local
```bash
./mvnw spring-boot:run
```

### Variáveis de Ambiente
- `RABBITMQ_HOST`: Host do RabbitMQ (padrão: localhost)
- `KAFKA_BOOTSTRAP_SERVERS`: Servidores do Kafka (padrão: localhost:9092)
- `EUREKA_SERVER_URL`: URL do servidor Eureka (padrão: http://localhost:8761/eureka)

## 🔧 Configurações

O serviço pode ser configurado via `application.yml`:

```yaml
matching:
  exact:
    weight: 0.4          # Peso da estratégia de match exato
  fuzzy:
    weight: 0.3          # Peso da estratégia de match difuso
    threshold: 0.7       # Limiar mínimo para considerar um match
  daterange:
    weight: 0.2          # Peso da estratégia de variação de data
    max-days: 2          # Dias máximos de variação permitida
  amounttolerance:
    weight: 0.1          # Peso da estratégia de tolerância de valor
  amount:
    tolerance: 0.50      # Tolerância máxima de valor em unidades monetárias
```

## 🧪 Testes

Executar testes unitários:
```bash
./mvnw test
```

Executar testes de integração:
```bash
./mvnw verify
```

## 📊 Métricas

O serviço expõe métricas via Spring Actuator e Prometheus:
- `/actuator/health` - Status de saúde do serviço
- `/actuator/prometheus` - Métricas para coleta do Prometheus
- `/actuator/metrics` - Métricas detalhadas

## 🔄 Algoritmo de Matching

O algoritmo de matching funciona da seguinte forma:

1. **Normalização**: As transações são normalizadas para remover ruídos e padronizar dados
2. **Avaliação por Estratégia**: Cada estratégia de matching avalia o par de transações e retorna uma pontuação
3. **Combinação de Pontuações**: As pontuações são combinadas usando pesos configuráveis
4. **Classificação**: O resultado é classificado como:
   - `MATCHED` (≥ 0.85): Match com alta confiança
   - `PARTIAL_MATCH` (≥ 0.50): Match com média confiança
   - `UNMATCHED` (< 0.50): Sem correspondência
   - `CONFLICT`: Conflito devido a múltiplas correspondências potenciais

## 🛡️ Segurança

- **Multi-tenant**: Cada empresa vê apenas seus próprios dados
- **JWT Tokens**: Autenticação via tokens JWT
- **OAuth2**: Integração com Keycloak para gerenciamento de identidade