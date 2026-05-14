# 🎨 Reconix Frontend

Interface web moderna para a Plataforma de Reconciliação Financeira Reconix.

## 🚀 Tecnologias

- **React 18** - Biblioteca UI
- **TypeScript 5** - Tipagem estática
- **Vite** - Build tool e dev server
- **Tailwind CSS 3** - Framework CSS utilitário
- **Zustand** - State management
- **TanStack Query** - Cache e gerenciamento de requisições
- **React Router v6** - Roteamento
- **React Hook Form + Zod** - Formulários e validação
- **Recharts** - Gráficos
- **Axios** - Cliente HTTP
- **STOMP.js + SockJS** - WebSocket para notificações em tempo real
- **Lucide React** - Ícones

## 📦 Instalação

```bash
# Instalar dependências
npm install

# Iniciar servidor de desenvolvimento
npm run dev

# Build para produção
npm run build

# Preview do build de produção
npm run preview

# Lint
npm run lint

# Format
npm run format
```

## 🌐 Acesso

Após iniciar o servidor de desenvolvimento:

- **URL:** http://localhost:5173
- **Usuário demo:** admin
- **Senha demo:** admin123

## 📁 Estrutura do Projeto

```
src/
├── components/          # Componentes reutilizáveis
│   ├── Layout/         # Layout principal (Sidebar, Header)
│   └── Toast/          # Sistema de notificações toast
├── hooks/              # Custom hooks
│   ├── useAuth.ts      # Hook de autenticação
│   └── useWebSocket.ts # Hook para WebSocket
├── pages/              # Páginas da aplicação
│   ├── Dashboard/      # Dashboard com KPIs
│   ├── Upload/         # Upload de arquivos
│   ├── Reconciliation/ # Visualização de conciliações
│   ├── FraudCenter/    # Centro de alertas de fraude
│   ├── Reports/        # Geração de relatórios
│   ├── Notifications/  # Histórico de notificações
│   ├── Settings/       # Configurações
│   └── Login/          # Página de login
├── services/           # Serviços de API
│   ├── api.ts          # Configuração do Axios
│   ├── authService.ts
│   ├── ingestionService.ts
│   ├── matchingService.ts
│   ├── fraudService.ts
│   └── reportingService.ts
├── store/              # Stores Zustand
│   ├── authStore.ts    # Estado de autenticação
│   └── notificationStore.ts # Estado de notificações
├── types/              # Tipos TypeScript
│   └── index.ts
├── styles/             # Estilos globais
│   └── index.css
├── App.tsx             # Componente raiz
└── main.tsx            # Entry point
```

## 🎯 Funcionalidades

### 🏠 Dashboard
- KPIs em tempo real (transações, taxa de conciliação, alertas)
- Gráficos de status de conciliação
- Alertas críticos em destaque
- Atualização automática via WebSocket

### 📤 Upload
- Drag & drop de arquivos
- Suporte a múltiplos formatos (CSV, XLSX, XML, JSON, OFX)
- Seleção de origem dos dados (Banco, ERP, Notas Fiscais, Gateway)
- Histórico de uploads com status

### 🔄 Conciliação
- Tabela de resultados de matching
- Filtros por status (Matched, Partial Match, Unmatched)
- Score visual de similaridade
- Paginação

### 🛡️ Centro de Fraudes
- Grid de alertas por nível de risco
- Filtros por risco e status
- Modal de detalhes do alerta
- Ações de revisão (Em Revisão, Confirmar Fraude, Falso Positivo)
- Notificações em tempo real via WebSocket

### 📊 Relatórios
- Geração de relatórios (Diário, Semanal, Mensal)
- Formatos PDF e Excel
- Download de relatórios gerados
- Histórico de relatórios

### 🔔 Notificações
- Histórico de notificações enviadas
- Visualização por canal (Email, Slack, Webhook, WebSocket)
- Status de entrega

### ⚙️ Configurações
- Perfil do usuário
- Configuração de notificações (Email, Slack)
- Gerenciamento de segurança

## 🔌 Integração com Backend

O frontend se comunica com o backend através do API Gateway:

- **API Gateway:** http://localhost:8080
- **WebSocket:** http://localhost:8086/ws

### Proxy de Desenvolvimento

O Vite está configurado para fazer proxy das requisições:

```typescript
proxy: {
  '/api': {
    target: 'http://localhost:8080',
    changeOrigin: true,
  },
  '/ws': {
    target: 'http://localhost:8086',
    ws: true,
    changeOrigin: true,
  },
}
```

## 🔐 Autenticação

O sistema utiliza JWT (JSON Web Tokens) para autenticação:

1. Login retorna `accessToken` e `refreshToken`
2. Token é armazenado no Zustand com persistência no localStorage
3. Interceptor do Axios adiciona o token em todas as requisições
4. Refresh automático quando o token expira
5. Logout limpa o estado e redireciona para login

## 📡 WebSocket (Tempo Real)

Notificações em tempo real usando STOMP sobre SockJS:

```typescript
// Exemplo de uso
useWebSocket<FraudAlert>('fraud-alerts', alert => {
  // Callback quando recebe novo alerta
  console.log('Novo alerta:', alert)
})
```

## 🎨 Estilização

### Tailwind CSS

Classes utilitárias customizadas:

```css
.btn - Botão base
.btn-primary - Botão primário
.btn-secondary - Botão secundário
.btn-danger - Botão de perigo
.card - Card container
.input - Input de formulário
.label - Label de formulário
```

### Cores Primárias

```javascript
primary: {
  50: '#f0f9ff',
  500: '#0ea5e9',
  600: '#0284c7',
  700: '#0369a1',
}
```

## 🧪 Desenvolvimento

### Variáveis de Ambiente

Crie um arquivo `.env.local`:

```env
VITE_API_URL=http://localhost:8080
VITE_WS_URL=http://localhost:8086
```

### Hot Module Replacement (HMR)

O Vite oferece HMR instantâneo. Qualquer alteração no código é refletida imediatamente no navegador.

## 📦 Build para Produção

```bash
npm run build
```

Gera os arquivos otimizados na pasta `dist/`:

- Minificação de JS e CSS
- Tree-shaking
- Code splitting
- Asset optimization

## 🚀 Deploy

### Opção 1: Servir com Nginx

```nginx
server {
  listen 80;
  server_name reconix.example.com;
  root /var/www/reconix-frontend/dist;
  index index.html;

  location / {
    try_files $uri $uri/ /index.html;
  }

  location /api {
    proxy_pass http://localhost:8080;
  }

  location /ws {
    proxy_pass http://localhost:8086;
    proxy_http_version 1.1;
    proxy_set_header Upgrade $http_upgrade;
    proxy_set_header Connection "upgrade";
  }
}
```

### Opção 2: Docker

```dockerfile
FROM node:20-alpine as build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

## 🐛 Troubleshooting

### Erro de CORS

Certifique-se de que o backend está configurado para aceitar requisições do frontend:

```java
@CrossOrigin(origins = "http://localhost:5173")
```

### WebSocket não conecta

Verifique se o serviço de notificação está rodando na porta 8086.

### Erro 401 (Unauthorized)

Token expirado ou inválido. Faça logout e login novamente.

## 📝 Convenções de Código

- **Componentes:** PascalCase (ex: `DashboardPage.tsx`)
- **Hooks:** camelCase com prefixo `use` (ex: `useAuth.ts`)
- **Services:** camelCase (ex: `authService.ts`)
- **Types:** PascalCase (ex: `User`, `FraudAlert`)
- **Constantes:** UPPER_SNAKE_CASE (ex: `API_URL`)

## 🤝 Contribuindo

1. Crie uma branch: `git checkout -b feat/minha-feature`
2. Faça suas alterações
3. Commit: `git commit -m "feat: minha feature"`
4. Push: `git push origin feat/minha-feature`
5. Abra um Pull Request

## 📄 Licença

MIT License - veja o arquivo LICENSE para detalhes.

---

**Desenvolvido com ❤️ para a Plataforma Reconix**
