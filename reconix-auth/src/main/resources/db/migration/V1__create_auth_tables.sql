-- ==========================================
-- V1__create_auth_tables.sql
-- Criacao das tabelas do servico de Auth
-- ==========================================

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Tabela de Tenants (empresas)
CREATE TABLE IF NOT EXISTS tenants (
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name        VARCHAR(255) NOT NULL,
    slug        VARCHAR(100) NOT NULL UNIQUE,
    active      BOOLEAN NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Tabela de Usuarios
CREATE TABLE IF NOT EXISTS users (
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name        VARCHAR(255) NOT NULL,
    email       VARCHAR(255) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    role        VARCHAR(50)  NOT NULL DEFAULT 'ANALYST',
    active      BOOLEAN NOT NULL DEFAULT TRUE,
    tenant_id   UUID NOT NULL REFERENCES tenants(id),
    created_at  TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Tabela de API Keys
CREATE TABLE IF NOT EXISTS api_keys (
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    key_hash    VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(255) NOT NULL,
    tenant_id   UUID NOT NULL REFERENCES tenants(id),
    active      BOOLEAN NOT NULL DEFAULT TRUE,
    expires_at  TIMESTAMP,
    created_at  TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Indices para performance
CREATE INDEX IF NOT EXISTS idx_users_email     ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_tenant_id ON users(tenant_id);
CREATE INDEX IF NOT EXISTS idx_tenants_slug    ON tenants(slug);
CREATE INDEX IF NOT EXISTS idx_api_keys_hash   ON api_keys(key_hash);

-- Tenant de desenvolvimento para testes
INSERT INTO tenants (name, slug) VALUES ('Reconix Dev', 'reconix-dev')
ON CONFLICT (slug) DO NOTHING;