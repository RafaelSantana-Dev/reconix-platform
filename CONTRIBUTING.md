# 🤝 Guia de Contribuição - Reconix Platform

Obrigado por considerar contribuir com a Plataforma Reconix! Este documento fornece diretrizes para contribuir com o projeto.

---

## 📋 Índice

- [Código de Conduta](#código-de-conduta)
- [Como Contribuir](#como-contribuir)
- [Padrões de Código](#padrões-de-código)
- [Padrões de Commit](#padrões-de-commit)
- [Pull Requests](#pull-requests)
- [Reportar Bugs](#reportar-bugs)
- [Sugerir Melhorias](#sugerir-melhorias)

---

## 📜 Código de Conduta

Este projeto adota um Código de Conduta que esperamos que todos os participantes sigam. Por favor, leia o código completo para entender quais ações serão e não serão toleradas.

### Nossos Padrões

- Seja respeitoso e inclusivo
- Aceite críticas construtivas
- Foque no que é melhor para a comunidade
- Mostre empatia com outros membros

---

## 🚀 Como Contribuir

### 1. Fork o Repositório

```bash
# Clone seu fork
git clone https://github.com/seu-usuario/reconix-platform.git
cd reconix-platform

# Adicione o repositório original como upstream
git remote add upstream https://github.com/RafaelSantana-Dev/reconix-platform.git
```

### 2. Crie uma Branch

```bash
# Atualize sua main
git checkout main
git pull upstream main

# Crie uma nova branch
git checkout -b feat/minha-feature
# ou
git checkout -b fix/meu-bugfix
```

### 3. Faça suas Alterações

- Escreva código limpo e bem documentado
- Siga os padrões de código do projeto
- Adicione testes para novas funcionalidades
- Atualize a documentação se necessário

### 4. Commit suas Alterações

```bash
git add .
git commit -m "feat: adiciona nova funcionalidade X"
```

### 5. Push para seu Fork

```bash
git push origin feat/minha-feature
```

### 6. Abra um Pull Request

- Vá para o repositório original no GitHub
- Clique em "New Pull Request"
- Selecione sua branch
- Preencha o template de PR
- Aguarde a revisão

---

## 💻 Padrões de Código

### Java (Backend)

```java
// ✅ BOM
public class UserService {
    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public User findById(String id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));
    }
}

// ❌ RUIM
public class UserService {
    @Autowired
    private UserRepository userRepository; // Evite @Autowired em fields
    
    public User findById(String id) {
        User user = userRepository.findById(id).get(); // Evite .get()
        return user;
    }
}
```

### Princípios

- **SOLID** - Siga os princípios SOLID
- **DRY** - Don't Repeat Yourself
- **KISS** - Keep It Simple, Stupid
- **Clean Code** - Código limpo e legível
- **Testes** - Escreva testes para seu código

### Nomenclatura

- **Classes:** PascalCase (ex: `UserService`)
- **Métodos:** camelCase (ex: `findById`)
- **Constantes:** UPPER_SNAKE_CASE (ex: `MAX_RETRY_ATTEMPTS`)
- **Packages:** lowercase (ex: `com.reconix.auth`)

### TypeScript (Frontend)

```typescript
// ✅ BOM
interface User {
  id: string
  username: string
  email: string
}

export function UserCard({ user }: { user: User }) {
  return (
    <div className="card">
      <h3>{user.username}</h3>
      <p>{user.email}</p>
    </div>
  )
}

// ❌ RUIM
function UserCard(props: any) { // Evite 'any'
  return (
    <div>
      <h3>{props.user.username}</h3>
    </div>
  )
}
```

### Princípios

- **TypeScript** - Use tipagem forte
- **Hooks** - Use hooks do React
- **Componentes** - Componentes pequenos e reutilizáveis
- **Props** - Interfaces bem definidas

---

## 📝 Padrões de Commit

Usamos [Conventional Commits](https://www.conventionalcommits.org/) para mensagens de commit.

### Formato

```
<tipo>(<escopo>): <descrição>

[corpo opcional]

[rodapé opcional]
```

### Tipos

- **feat:** Nova funcionalidade
- **fix:** Correção de bug
- **docs:** Alterações na documentação
- **style:** Formatação, ponto e vírgula, etc
- **refactor:** Refatoração de código
- **test:** Adição ou correção de testes
- **chore:** Tarefas de manutenção

### Exemplos

```bash
# Feature
git commit -m "feat(auth): add JWT refresh token support"

# Bug fix
git commit -m "fix(matching): correct score calculation for fuzzy match"

# Documentation
git commit -m "docs: update README with new installation steps"

# Refactoring
git commit -m "refactor(ledger): extract event replay logic to separate class"

# Breaking change
git commit -m "feat(api)!: change authentication endpoint structure

BREAKING CHANGE: /auth/login now returns different response format"
```

---

## 🔍 Pull Requests

### Checklist

Antes de abrir um PR, certifique-se de que:

- [ ] O código compila sem erros
- [ ] Todos os testes passam
- [ ] Novos testes foram adicionados (se aplicável)
- [ ] A documentação foi atualizada (se aplicável)
- [ ] O código segue os padrões do projeto
- [ ] Os commits seguem o padrão Conventional Commits
- [ ] A branch está atualizada com a main

### Template de PR

```markdown
## Descrição
Breve descrição das alterações

## Tipo de Mudança
- [ ] Bug fix
- [ ] Nova funcionalidade
- [ ] Breaking change
- [ ] Documentação

## Como Testar
1. Passo 1
2. Passo 2
3. Passo 3

## Checklist
- [ ] Código compila
- [ ] Testes passam
- [ ] Documentação atualizada
```

### Revisão

- PRs serão revisados por mantenedores
- Feedback será fornecido via comentários
- Alterações podem ser solicitadas
- Após aprovação, o PR será merged

---

## 🐛 Reportar Bugs

### Antes de Reportar

- Verifique se o bug já foi reportado
- Verifique se você está usando a versão mais recente
- Tente reproduzir o bug

### Como Reportar

Abra uma issue com:

1. **Título claro** - Descreva o problema em poucas palavras
2. **Descrição** - Descreva o bug em detalhes
3. **Passos para reproduzir** - Como reproduzir o bug
4. **Comportamento esperado** - O que deveria acontecer
5. **Comportamento atual** - O que está acontecendo
6. **Screenshots** - Se aplicável
7. **Ambiente** - OS, versão do Java, Node, etc

### Template de Bug

```markdown
**Descrição do Bug**
Descrição clara do que está acontecendo

**Passos para Reproduzir**
1. Vá para '...'
2. Clique em '...'
3. Veja o erro

**Comportamento Esperado**
O que deveria acontecer

**Screenshots**
Se aplicável, adicione screenshots

**Ambiente**
- OS: [ex: Windows 11]
- Java: [ex: 21]
- Node: [ex: 20.10.0]
- Browser: [ex: Chrome 120]
```

---

## 💡 Sugerir Melhorias

### Como Sugerir

Abra uma issue com:

1. **Título claro** - Descreva a melhoria
2. **Problema** - Qual problema isso resolve
3. **Solução proposta** - Como você sugere resolver
4. **Alternativas** - Outras soluções consideradas
5. **Contexto adicional** - Qualquer informação relevante

### Template de Feature Request

```markdown
**Descrição da Feature**
Descrição clara da funcionalidade

**Problema que Resolve**
Qual problema essa feature resolve

**Solução Proposta**
Como você imagina que isso funcione

**Alternativas Consideradas**
Outras soluções que você considerou

**Contexto Adicional**
Qualquer informação adicional
```

---

## 🧪 Testes

### Executar Testes

```bash
# Todos os testes
make test-all

# Apenas unitários
make test-unit

# Apenas integração
make test-integration
```

### Escrever Testes

```java
// Teste unitário
@Test
void shouldFindUserById() {
    // Given
    String userId = "123";
    User expectedUser = new User(userId, "john", "john@example.com");
    when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));
    
    // When
    User actualUser = userService.findById(userId);
    
    // Then
    assertEquals(expectedUser, actualUser);
}

// Teste de integração
@SpringBootTest
@Testcontainers
class UserServiceIntegrationTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");
    
    @Test
    void shouldSaveAndRetrieveUser() {
        // Test implementation
    }
}
```

---

## 📚 Documentação

### Atualizar Documentação

- **README.md** - Visão geral do projeto
- **ARCHITECTURE.md** - Documentação de arquitetura
- **Javadoc** - Documentação de código Java
- **TSDoc** - Documentação de código TypeScript
- **OpenAPI** - Documentação de APIs

### Exemplo de Javadoc

```java
/**
 * Serviço responsável por gerenciar usuários.
 * 
 * @author Seu Nome
 * @since 1.0.0
 */
public class UserService {
    
    /**
     * Busca um usuário por ID.
     * 
     * @param id o ID do usuário
     * @return o usuário encontrado
     * @throws UserNotFoundException se o usuário não for encontrado
     */
    public User findById(String id) {
        // Implementation
    }
}
```

---

## 🎯 Áreas para Contribuir

### Backend
- Novos parsers de arquivo
- Novas estratégias de matching
- Novas regras de fraude
- Melhorias de performance
- Testes adicionais

### Frontend
- Novos componentes
- Melhorias de UX
- Testes E2E
- Acessibilidade
- Internacionalização

### Infraestrutura
- Kubernetes manifests
- Helm charts
- CI/CD pipelines
- Terraform modules
- Monitoring dashboards

### Documentação
- Tutoriais
- Guias de uso
- Exemplos de código
- Traduções
- Diagramas

---

## 🏆 Reconhecimento

Contribuidores serão reconhecidos:

- No arquivo CONTRIBUTORS.md
- Nos release notes
- Na documentação do projeto

---

## 📞 Contato

- **Issues:** Use o GitHub Issues
- **Discussões:** Use o GitHub Discussions
- **Email:** reconix@example.com (se disponível)

---

## 📄 Licença

Ao contribuir, você concorda que suas contribuições serão licenciadas sob a mesma licença do projeto (MIT License).

---

**Obrigado por contribuir com a Plataforma Reconix!** 🚀

