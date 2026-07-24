# OficinaMecanicaBackend - Documentação Completa

Backend para um **Sistema de Oficina Mecânica** desenvolvido com **Java 17** e **Spring Boot 4.1.0**.
Projeto voltado para estudos, utilizando arquitetura **Controller → Service → Repository → Entity** com **PostgreSQL**, **Spring Data JPA**, **Maven** e **Lombok**.

---

## Sumário

1. [Visão Geral](#1-visão-geral)
2. [Tecnologias](#2-tecnologias)
3. [Pré-requisitos e Configuração](#3-pré-requisitos-e-configuração)
4. [Estrutura do Projeto](#4-estrutura-do-projeto)
5. [Arquitetura e Fluxo](#5-arquitetura-e-fluxo)
6. [Modelo de Dados (Entidades e Relacionamentos)](#6-modelo-de-dados)
7. [DTOs (Data Transfer Objects)](#7-dtos)
8. [Services (Lógica de Negócio)](#8-services)
9. [Controllers (Endpoints REST)](#9-controllers)
10. [Tratamento de Exceções](#10-tratamento-de-exceções)
11. [Regras de Negócio](#11-regras-de-negócio)
12. [Exemplos de Requisições (Postman)](#12-exemplos-de-requisições)
13. [Tabelas do Banco de Dados](#13-tabelas-do-banco-de-dados)

---

## 1. Visão Geral

Este projeto é uma API RESTful para gerenciamento de uma oficina mecânica. Ele permite cadastrar clientes, veículos, mecânicos, equipes, serviços, peças e ordens de serviço, com regras de negócio para controle de status e cálculo automático de valores.

**O que o sistema faz:**
- Gerencia o cadastro completo de clientes e seus veículos
- Gerencia mecânicos e equipes mecânicas
- Cadastra serviços (com valor de mão de obra) e peças (com preço e estoque)
- Abre Ordens de Serviço vinculando veículo + equipe
- Permite adicionar/remover serviços e peças a uma OS
- Calcula automaticamente o valor total da OS
- Controla o fluxo de status da OS com regras de transição

---

## 2. Tecnologias

| Tecnologia | Versão | Uso |
|---|---|---|
| Java | 17 | Linguagem do projeto |
| Spring Boot | 4.1.0 | Framework principal |
| Spring Web (MVC) | - | Endpoints REST |
| Spring Data JPA | - | Acesso ao banco via repositories |
| PostgreSQL | - | Banco de dados relacional |
| Hibernate | - | ORM (mapeamento objeto-relacional) |
| Lombok | - | Redução de boilerplate (getters, setters, builders) |
| Jakarta Validation | - | Validação de DTOs nos controllers |
| Maven | 3.9.16 | Gerenciador de dependências e build |

---

## 3. Pré-requisitos e Configuração

### Pré-requisitos
- **Java 17** ou superior
- **PostgreSQL** instalado e rodando na porta 5432
- **Maven** (ou utilize o wrapper `./mvnw`)

### Configuração do Banco de Dados

Crie o banco de dados no PostgreSQL antes de rodar o projeto:

```sql
CREATE DATABASE oficina_mecanica;
```

### application.properties

**Arquivo:** `src/main/resources/application.properties`

```properties
spring.application.name=OficinaMecanicaBackend

# Servidor
server.port=8080

# Banco de Dados PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/oficina_mecanica
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA / Hibernate
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

| Propriedade | Descrição |
|---|---|
| `server.port` | Porta do servidor (8080) |
| `spring.datasource.url` | URL de conexão com o PostgreSQL |
| `spring.datasource.username` | Usuário do banco |
| `spring.datasource.password` | Senha do banco |
| `spring.jpa.hibernate.ddl-auto=update` | Hibernate cria/atualiza tabelas automaticamente |
| `spring.jpa.show-sql=true` | Exibe SQL executado no console |

### Rodar o Projeto

```bash
# Compilar
./mvnw compile

# Executar
./mvnw spring-boot:run

# Gerar JAR
./mvnw package
```

### CORS

O backend possui configuração global de CORS (`config/CorsConfig.java`) que permite requisições de:
- `http://localhost:3000`
- `http://localhost:5500` (Live Server)
- `file://` (abertura direta do HTML)

### Frontend

O frontend é uma aplicação vanilla HTML/CSS/JS localizada na pasta `../OficinaMecanicaFrontend/`. Para utilizá-lo:

```bash
# Opção 1: Abrir index.html diretamente no navegador

# Opção 2: Usar Live Server (VS Code)
# Clique com botão direito em index.html → "Open with Live Server"

# Opção 3: Servidor HTTP local (Python)
cd ../OficinaMecanicaFrontend
python3 -m http.server 5500
```

O frontend se conecta automaticamente ao backend correto:
- **Local:** `http://localhost:8080/api` (detecta `localhost`)
- **Produção:** URL do Azure App Service (detecta outro hostname)

### URLs de Produção

| Serviço | URL |
|---|---|
| Frontend (Netlify) | https://oficinadocarlao.netlify.app |
| Backend (Azure App Service) | https://<seu-app>.azurewebsites.net |
| Banco de Dados | caxa.postgres.database.azure.com:5432 |

### Deploy

#### Frontend (Netlify)
1. Conecte o repositório ao Netlify
2. Diretório de build: `.` (raiz da pasta `OficinaMecanicaFrontend`)
3. O arquivo `netlify.toml` já está configurado

#### Backend (Azure App Service)
1. Crie um App Service no Azure Portal (Java 17, Linux)
2. Configure as variáveis de ambiente:
   - `DB_URL` = URL do PostgreSQL Azure com `?sslmode=require`
   - `DB_USER` = usuário do PostgreSQL
   - `DB_PASS` = senha do PostgreSQL
3. Faça deploy via ZIP ou Git

---

## 4. Estrutura do Projeto

```
src/main/java/com/estudos/oficinamecanicabackend/
│
├── OficinaMecanicaBackendApplication.java    ← Ponto de entrada (main)
│
├── config/
│   └── CorsConfig.java                       ← Configuração global de CORS
│
├── enums/
│   └── StatusOS.java                         ← Enum dos status da OS
│
├── entity/                                    ← Entidades JPA (tabelas)
│   ├── Cliente.java
│   ├── Veiculo.java
│   ├── Mecanico.java
│   ├── EquipeMecanica.java
│   ├── Servico.java
│   ├── Peca.java
│   ├── OrdemServico.java
│   ├── OrdemServicoServico.java              ← Tabela intermediária N:N
│   └── OrdemServicoPeca.java                 ← Tabela intermediária N:N
│
├── repository/                                ← Acesso ao banco (JPA)
│   ├── ClienteRepository.java
│   ├── VeiculoRepository.java
│   ├── MecanicoRepository.java
│   ├── EquipeMecanicaRepository.java
│   ├── ServicoRepository.java
│   ├── PecaRepository.java
│   ├── OrdemServicoRepository.java
│   ├── OrdemServicoServicoRepository.java
│   └── OrdemServicoPecaRepository.java
│
├── dto/                                       ← Objetos de transferência de dados
│   ├── ClienteDTO.java
│   ├── VeiculoDTO.java
│   ├── MecanicoDTO.java
│   ├── EquipeMecanicaDTO.java
│   ├── ServicoDTO.java
│   ├── PecaDTO.java
│   ├── OrdemServicoDTO.java
│   ├── StatusOSDTO.java
│   ├── OrdemServicoServicoDTO.java
│   ├── OrdemServicoPecaDTO.java
│   └── OrdemServicoResponseDTO.java
│
├── service/                                   ← Lógica de negócio
│   ├── ClienteService.java
│   ├── VeiculoService.java
│   ├── MecanicoService.java
│   ├── EquipeMecanicaService.java
│   ├── ServicoService.java
│   ├── PecaService.java
│   └── OrdemServicoService.java
│
├── controller/                                ← Endpoints REST
│   ├── ClienteController.java
│   ├── VeiculoController.java
│   ├── MecanicoController.java
│   ├── EquipeMecanicaController.java
│   ├── ServicoController.java
│   ├── PecaController.java
│   └── OrdemServicoController.java
│
└── exception/                                 ← Tratamento de erros
    ├── ResourceNotFoundException.java
    ├── BusinessException.java
    └── GlobalExceptionHandler.java
```

**Arquitetura em camadas:**

```
HTTP Request
    ↓
Controller (recebe, valida, retorna HTTP)
    ↓
Service (regras de negócio, transações)
    ↓
Repository (acesso ao banco via JPA)
    ↓
Entity (mapeamento das tabelas)
    ↓
PostgreSQL
```

---

## 5. Arquitetura e Fluxo

### Camada por camada

| Camada | Responsabilidade | Anotação Principal |
|---|---|---|
| **Controller** | Receber requisições HTTP, validar entrada, chamar o Service, retornar resposta | `@RestController` |
| **Service** | Conter toda a lógica de negócio, orquestrar chamadas ao Repository | `@Service` + `@Transactional` |
| **Repository** | Realizar operações CRUD no banco de dados | `@Repository` (extends `JpaRepository`) |
| **Entity** | Mapear tabelas do banco de dados | `@Entity` + `@Table` |
| **DTO** | Transportar dados entre camadas, validação de entrada | `@Getter/@Setter` + Jakarta Validation |
| **Exception** | Tratar erros de forma padronizada | `@RestControllerAdvice` |
| **Config** | Configurações transversais (CORS, etc.) | `@Configuration` + `@Bean` |

> **Nota:** As entidades utilizam `@JsonIgnore` nos relacionamentos `@OneToMany` bidirecionais para evitar recursão infinita na serialização JSON (StackOverflowError). Apenas o lado "pai" do relacionamento é serializado na resposta da API.

### Fluxo de uma requisição (exemplo: cadastrar cliente)

```
1. POST /api/clientes com JSON { "nome": "João", "cpf": "123" }
       ↓
2. ClienteController.cadastrar() recebe o ClienteDTO
       ↓  (validação @NotBlank rod automaticamente)
3. ClienteService.cadastrar() monta a entidade e salva
       ↓
4. ClienteRepository.save() persiste no PostgreSQL
       ↓
5. Resposta HTTP 201 com o objeto Cliente salvo
```

---

## 6. Modelo de Dados

### Diagrama de Relacionamentos

```
┌──────────┐     1:N     ┌──────────┐
│ CLIENTE  │────────────→│ VEICULO  │
└──────────┘             └────┬─────┘
                              │ 1:N
                              ↓
┌──────────────┐   N:1   ┌──────────────┐    1:N    ┌───────────────────────┐
│ EQUIPE_MECAN │←────────│ ORDEM_SERVICO │──────────→│ ORDEM_SERVICO_SERVICO │
└──────┬───────┘         └──────┬────────┘           └───────────┬───────────┘
       │ N:1                    │ 1:N                            │ N:1
       ↓                        ↓                                ↓
┌──────────────┐               │                       ┌──────────┐
│   MECANICO   │               │                       │  SERVICO │
└──────────────┘               │                       └──────────┘
                               │ 1:N
                               ↓
                    ┌──────────────────────┐    N:1    ┌──────────┐
                    │ ORDEM_SERVICO_PECA   │──────────→│   PECA   │
                    └──────────────────────┘           └──────────┘
```

### Entidade: Cliente

**Tabela:** `clientes`

| Campo | Tipo | Restrição | Descrição |
|---|---|---|---|
| `id` | Long (BIGINT) | PK, auto-increment | Identificador único |
| `nome` | String (VARCHAR) | NOT NULL | Nome completo |
| `cpf` | String (VARCHAR) | NOT NULL, UNIQUE | CPF do cliente |
| `telefone` | String (VARCHAR) | - | Telefone de contato |
| `email` | String (VARCHAR) | - | E-mail de contato |
| `endereco` | String (VARCHAR) | - | Endereço completo |

**Relacionamentos:**
- `OneToMany` com Veiculo (um cliente tem vários veículos)

**Arquivo:** `entity/Cliente.java`

---

### Entidade: Veiculo

**Tabela:** `veiculos`

| Campo | Tipo | Restrição | Descrição |
|---|---|---|---|
| `id` | Long (BIGINT) | PK, auto-increment | Identificador único |
| `marca` | String (VARCHAR) | NOT NULL | Marca do veículo (ex: Chevrolet) |
| `modelo` | String (VARCHAR) | NOT NULL | Modelo (ex: Onix) |
| `ano` | Integer (INTEGER) | NOT NULL | Ano de fabricação |
| `placa` | String (VARCHAR) | NOT NULL, UNIQUE | Placa do veículo |
| `cor` | String (VARCHAR) | - | Cor do veículo |
| `cliente_id` | Long (BIGINT) | FK, NOT NULL | Referência ao cliente |

**Relacionamentos:**
- `ManyToOne` com Cliente (cada veículo pertence a um cliente)
- `OneToMany` com OrdemServico (um veículo pode ter várias OS)

**Arquivo:** `entity/Veiculo.java`

---

### Entidade: Mecanico

**Tabela:** `mecanicos`

| Campo | Tipo | Restrição | Descrição |
|---|---|---|---|
| `id` | Long (BIGINT) | PK, auto-increment | Identificador único |
| `nome` | String (VARCHAR) | NOT NULL | Nome do mecânico |
| `cpf` | String (VARCHAR) | NOT NULL, UNIQUE | CPF do mecânico |
| `especialidade` | String (VARCHAR) | - | Especialidade (ex: Motor, Suspensão) |
| `telefone` | String (VARCHAR) | - | Telefone de contato |

**Relacionamentos:**
- `OneToMany` com EquipeMecanica (um mecânico pode estar em várias equipes)

**Arquivo:** `entity/Mecanico.java`

---

### Entidade: EquipeMecanica

**Tabela:** `equipes_mecanicas`

| Campo | Tipo | Restrição | Descrição |
|---|---|---|---|
| `id` | Long (BIGINT) | PK, auto-increment | Identificador único |
| `nome` | String (VARCHAR) | NOT NULL | Nome da equipe |
| `mecanico_id` | Long (BIGINT) | FK, NOT NULL | Mecânico responsável |

**Relacionamentos:**
- `ManyToOne` com Mecanico (cada equipe tem um mecânico responsável)
- `OneToMany` com OrdemServico (uma equipe atende várias OS)

**Arquivo:** `entity/EquipeMecanica.java`

---

### Entidade: Servico

**Tabela:** `servicos`

| Campo | Tipo | Restrição | Descrição |
|---|---|---|---|
| `id` | Long (BIGINT) | PK, auto-increment | Identificador único |
| `descricao` | String (VARCHAR) | NOT NULL | Descrição do serviço |
| `valor_mao_de_obra` | BigDecimal (NUMERIC) | NOT NULL | Valor cobrado pela mão de obra |

**Relacionamentos:**
- `OneToMany` com OrdemServicoServico (N:N com OrdemServico via tabela intermediária)

**Arquivo:** `entity/Servico.java`

---

### Entidade: Peca

**Tabela:** `pecas`

| Campo | Tipo | Restrição | Descrição |
|---|---|---|---|
| `id` | Long (BIGINT) | PK, auto-increment | Identificador único |
| `nome` | String (VARCHAR) | NOT NULL | Nome da peça |
| `descricao` | String (VARCHAR) | - | Descrição detalhada |
| `valor_unitario` | BigDecimal (NUMERIC) | NOT NULL | Preço unitário |
| `quantidade_estoque` | Integer (INTEGER) | NOT NULL | Quantidade em estoque |

**Relacionamentos:**
- `OneToMany` com OrdemServicoPeca (N:N com OrdemServico via tabela intermediária)

**Arquivo:** `entity/Peca.java`

---

### Entidade: OrdemServico

**Tabela:** `ordens_servico`

| Campo | Tipo | Restrição | Descrição |
|---|---|---|---|
| `id` | Long (BIGINT) | PK, auto-increment | Identificador único |
| `data_abertura` | LocalDate (DATE) | NOT NULL | Data de abertura da OS |
| `data_conclusao` | LocalDate (DATE) | - | Data de conclusão (null se ativa) |
| `status` | StatusOS (VARCHAR) | NOT NULL | Status atual (EM_ANALISE, etc.) |
| `observacoes` | String (TEXT) | - | Observações gerais |
| `valor_total` | BigDecimal (NUMERIC) | NOT NULL | Valor total (calculado automaticamente) |
| `problema_relatado` | String (VARCHAR) | NOT NULL | Descrição do problema |
| `veiculo_id` | Long (BIGINT) | FK, NOT NULL | Veículo atendido |
| `equipe_id` | Long (BIGINT) | FK, NOT NULL | Equipe responsável |

**Relacionamentos:**
- `ManyToOne` com Veiculo
- `ManyToOne` com EquipeMecanica
- `OneToMany` com OrdemServicoServico (serviços realizados)
- `OneToMany` com OrdemServicoPeca (peças utilizadas)

**Método especial:**
```java
// Calcula o valor total somando mão de obra + peças
public void calcularValorTotal() {
    BigDecimal totalServicos = Σ (servico.valorMaoDeObra)
    BigDecimal totalPecas    = Σ (peca.valorUnitario × quantidade)
    this.valorTotal = totalServicos + totalPecas
}
```

**Arquivo:** `entity/OrdemServico.java`

---

### Entidade: OrdemServicoServico (Tabela Intermediária)

**Tabela:** `ordens_servico_servicos`

Resolve o relacionamento **N:N** entre OrdemServico e Servico.

| Campo | Tipo | Restrição | Descrição |
|---|---|---|---|
| `id` | Long (BIGINT) | PK, auto-increment | Identificador único |
| `ordem_servico_id` | Long (BIGINT) | FK, NOT NULL | Referência à OS |
| `servico_id` | Long (BIGINT) | FK, NOT NULL | Referência ao serviço |

**Arquivo:** `entity/OrdemServicoServico.java`

---

### Entidade: OrdemServicoPeca (Tabela Intermediária)

**Tabela:** `ordens_servico_pecas`

Resolve o relacionamento **N:N** entre OrdemServico e Peca. Contém a **quantidade** utilizada.

| Campo | Tipo | Restrição | Descrição |
|---|---|---|---|
| `id` | Long (BIGINT) | PK, auto-increment | Identificador único |
| `ordem_servico_id` | Long (BIGINT) | FK, NOT NULL | Referência à OS |
| `peca_id` | Long (BIGINT) | FK, NOT NULL | Referência à peça |
| `quantidade` | Integer (INTEGER) | NOT NULL | Quantidade utilizada |

**Arquivo:** `entity/OrdemServicoPeca.java`

---

### Enum: StatusOS

```java
public enum StatusOS {
    EM_ANALISE("Em Análise"),
    EM_ANDAMENTO("Em Andamento"),
    CONCLUIDA("Concluída"),
    CANCELADA("Cancelada");
}
```

---

## 7. DTOs

### DTOs de Entrada (Request)

Usados para **receber dados** nos endpoints POST, PUT e PATCH. Possuem anotações de validação (`@NotBlank`, `@NotNull`, `@Positive`).

| DTO | Campos Obrigatórios | Usado em |
|---|---|---|
| `ClienteDTO` | `nome`, `cpf` | POST/PUT `/api/clientes` |
| `VeiculoDTO` | `marca`, `modelo`, `ano`, `placa`, `clienteId` | POST/PUT `/api/veiculos` |
| `MecanicoDTO` | `nome`, `cpf` | POST/PUT `/api/mecanicos` |
| `EquipeMecanicaDTO` | `nome`, `mecanicoId` | POST/PUT `/api/equipes` |
| `ServicoDTO` | `descricao`, `valorMaoDeObra` | POST/PUT `/api/servicos` |
| `PecaDTO` | `nome`, `valorUnitario`, `quantidadeEstoque` | POST/PUT `/api/pecas` |
| `OrdemServicoDTO` | `veiculoId`, `equipeId`, `problemaRelatado` | POST `/api/ordens-servico` |
| `StatusOSDTO` | `status` (enum) | PATCH `/api/ordens-servico/{id}/status` |
| `OrdemServicoServicoDTO` | `servicoId` | POST `/api/ordens-servico/{id}/servicos` |
| `OrdemServicoPecaDTO` | `pecaId`, `quantidade` | POST `/api/ordens-servico/{id}/pecas` |

### DTO de Saída (Response)

**`OrdemServicoResponseDTO`** — Usado para retornar dados completos de uma OS nos endpoints:

```json
{
    "id": 1,
    "dataAbertura": "2025-07-23",
    "dataConclusao": null,
    "status": "EM_ANDAMENTO",
    "observacoes": "Cliente relatou barulho",
    "valorTotal": 350.00,
    "problemaRelatado": "Barulho na suspensão",
    "veiculoId": 1,
    "veiculoPlaca": "ABC-1234",
    "equipeId": 1,
    "equipeNome": "Equipe Alfa",
    "servicos": [
        {
            "servicoId": 1,
            "descricao": "Alinhamento e Balanceamento",
            "valorMaoDeObra": 120.00
        },
        {
            "servicoId": 2,
            "descricao": "Troca de Amortecedor",
            "valorMaoDeObra": 150.00
        }
    ],
    "pecas": [
        {
            "pecaId": 1,
            "pecaNome": "Amortecedor Dianteiro",
            "quantidade": 2,
            "valorUnitario": 40.00,
            "subtotal": 80.00
        }
    ]
}
```

---

## 8. Services

Cada Service é anotado com `@Service` e utiliza `@Transactional` para gerenciar transações.

### Padrão dos Services CRUD (Cliente, Veiculo, Mecanico, Equipe, Servico, Peca)

Todos seguem o mesmo padrão:

| Método | Anotação | Descrição |
|---|---|---|
| `listarTodos()` | `@Transactional(readOnly = true)` | Retorna lista de todos os registros |
| `buscarPorId(id)` | `@Transactional(readOnly = true)` | Busca por ID, lança `ResourceNotFoundException` se não encontrar |
| `cadastrar(dto)` | `@Transactional` | Converte DTO para entidade e salva |
| `atualizar(id, dto)` | `@Transactional` | Busca existente, atualiza campos, salva |
| `excluir(id)` | `@Transactional` | Busca existente e deleta |

**Services que dependem de outros:**
- `VeiculoService` → depende de `ClienteRepository` (para vincular veículo a cliente)
- `EquipeMecanicaService` → depende de `MecanicoRepository` (para vincular equipe a mecânico)

### OrdemServicoService (o mais complexo)

Este service contém toda a regras de negócio do sistema:

| Método | Descrição |
|---|---|
| `abrirOrdemServico(dto)` | Cria OS com status `EM_ANALISE` e `valorTotal = 0` |
| `alterarStatus(id, dto)` | Altera status com validação de transições permitidas |
| `adicionarServico(osId, dto)` | Adiciona serviço à OS e recalcula valor total |
| `removerServico(osId, servicoId)` | Remove serviço da OS e recalcula valor total |
| `adicionarPeca(osId, dto)` | Adiciona peça à OS, verifica estoque, recalcula valor total |
| `removerPeca(osId, pecaId)` | Remove peça da OS e recalcula valor total |
| `paraResponseDTO(os)` | Converte entidade OrdemServico para DTO de resposta |

---

## 9. Controllers

### Clientes — `/api/clientes`

| Método | Endpoint | Descrição | Request Body |
|---|---|---|---|
| `GET` | `/api/clientes` | Lista todos os clientes | - |
| `GET` | `/api/clientes/{id}` | Busca cliente por ID | - |
| `POST` | `/api/clientes` | Cadastra novo cliente | `ClienteDTO` |
| `PUT` | `/api/clientes/{id}` | Atualiza cliente | `ClienteDTO` |
| `DELETE` | `/api/clientes/{id}` | Exclui cliente | - |

---

### Veículos — `/api/veiculos`

| Método | Endpoint | Descrição | Request Body |
|---|---|---|---|
| `GET` | `/api/veiculos` | Lista todos os veículos | - |
| `GET` | `/api/veiculos/{id}` | Busca veículo por ID | - |
| `POST` | `/api/veiculos` | Cadastra novo veículo | `VeiculoDTO` |
| `PUT` | `/api/veiculos/{id}` | Atualiza veículo | `VeiculoDTO` |
| `DELETE` | `/api/veiculos/{id}` | Exclui veículo | - |

---

### Mecânicos — `/api/mecanicos`

| Método | Endpoint | Descrição | Request Body |
|---|---|---|---|
| `GET` | `/api/mecanicos` | Lista todos os mecânicos | - |
| `GET` | `/api/mecanicos/{id}` | Busca mecânico por ID | - |
| `POST` | `/api/mecanicos` | Cadastra novo mecânico | `MecanicoDTO` |
| `PUT` | `/api/mecanicos/{id}` | Atualiza mecânico | `MecanicoDTO` |
| `DELETE` | `/api/mecanicos/{id}` | Exclui mecânico | - |

---

### Equipes Mecânicas — `/api/equipes`

| Método | Endpoint | Descrição | Request Body |
|---|---|---|---|
| `GET` | `/api/equipes` | Lista todas as equipes | - |
| `GET` | `/api/equipes/{id}` | Busca equipe por ID | - |
| `POST` | `/api/equipes` | Cadastra nova equipe | `EquipeMecanicaDTO` |
| `PUT` | `/api/equipes/{id}` | Atualiza equipe | `EquipeMecanicaDTO` |
| `DELETE` | `/api/equipes/{id}` | Exclui equipe | - |

---

### Serviços — `/api/servicos`

| Método | Endpoint | Descrição | Request Body |
|---|---|---|---|
| `GET` | `/api/servicos` | Lista todos os serviços | - |
| `GET` | `/api/servicos/{id}` | Busca serviço por ID | - |
| `POST` | `/api/servicos` | Cadastra novo serviço | `ServicoDTO` |
| `PUT` | `/api/servicos/{id}` | Atualiza serviço | `ServicoDTO` |
| `DELETE` | `/api/servicos/{id}` | Exclui serviço | - |

---

### Peças — `/api/pecas`

| Método | Endpoint | Descrição | Request Body |
|---|---|---|---|
| `GET` | `/api/pecas` | Lista todas as peças | - |
| `GET` | `/api/pecas/{id}` | Busca peça por ID | - |
| `POST` | `/api/pecas` | Cadastra nova peça | `PecaDTO` |
| `PUT` | `/api/pecas/{id}` | Atualiza peça | `PecaDTO` |
| `DELETE` | `/api/pecas/{id}` | Exclui peça | - |

---

### Ordens de Serviço — `/api/ordens-servico`

| Método | Endpoint | Descrição | Request Body |
|---|---|---|---|
| `GET` | `/api/ordens-servico` | Lista todas as OS | - |
| `GET` | `/api/ordens-servico/{id}` | Busca OS por ID | - |
| `POST` | `/api/ordens-servico` | Abre nova OS | `OrdemServicoDTO` |
| `PATCH` | `/api/ordens-servico/{id}/status` | Altera status da OS | `StatusOSDTO` |
| `POST` | `/api/ordens-servico/{id}/servicos` | Adiciona serviço à OS | `OrdemServicoServicoDTO` |
| `DELETE` | `/api/ordens-servico/{id}/servicos/{servicoId}` | Remove serviço da OS | - |
| `POST` | `/api/ordens-servico/{id}/pecas` | Adiciona peça à OS | `OrdemServicoPecaDTO` |
| `DELETE` | `/api/ordens-servico/{id}/pecas/{pecaId}` | Remove peça da OS | - |
| `DELETE` | `/api/ordens-servico/{id}` | Exclui OS | - |

---

## 10. Tratamento de Exceções

O projeto utiliza `@RestControllerAdvice` para tratar exceções de forma global e padronizada.

### Exceções Customizadas

| Exceção | Quando é lançada | HTTP Status |
|---|---|---|
| `ResourceNotFoundException` | Entidade não encontrada no banco | **404 Not Found** |
| `BusinessException` | Violação de regra de negócio | **400 Bad Request** |

### GlobalExceptionHandler

**Arquivo:** `exception/GlobalExceptionHandler.java`

| Exceção Capturada | Comportamento |
|---|---|
| `ResourceNotFoundException` | Retorna JSON com `status: 404` e mensagem do erro |
| `BusinessException` | Retorna JSON com `status: 400` e mensagem do erro |
| `MethodArgumentNotValidException` | Retorna JSON com `status: 400`, lista de campos com erros de validação |
| `Exception` (genérica) | Retorna JSON com `status: 500` e mensagem de erro interno |

### Formato da Resposta de Erro

```json
{
    "timestamp": "2025-07-23T10:30:00",
    "status": 404,
    "erro": "Cliente não encontrado com id: 99"
}
```

**Erro de Validação (400):**
```json
{
    "timestamp": "2025-07-23T10:30:00",
    "status": 400,
    "erro": "Erro de validação",
    "campos": {
        "nome": "Nome é obrigatório",
        "cpf": "CPF é obrigatório"
    }
}
```

---

## 11. Regras de Negócio

### Fluxo de Status da Ordem de Serviço

```
                    ┌─────────────┐
                    │ EM_ANALISE  │  ← Status ao abrir a OS
                    └──────┬──────┘
                           │
              ┌────────────┼────────────┐
              ↓                         ↓
     ┌────────────────┐        ┌──────────────┐
     │  EM_ANDAMENTO  │        │   CANCELADA  │  ← Pode cancelar direto
     └───────┬────────┘        └──────────────┘
             │
      ┌──────┴──────┐
      ↓             ↓
┌───────────┐  ┌──────────────┐
│ CONCLUIDA │  │   CANCELADA  │
└───────────┘  └──────────────┘
```

**Transições permitidas:**
| Status Atual | Novo Status Permitido |
|---|---|
| `EM_ANALISE` | `EM_ANDAMENTO`, `CANCELADA` |
| `EM_ANDAMENTO` | `CONCLUIDA`, `CANCELADA` |
| `CONCLUIDA` | *(nenhum - status final)* |
| `CANCELADA` | *(nenhum - status final)* |

**Transições bloqueadas:**
- `EM_ANALISE` → `CONCLUIDA` (deve passar por `EM_ANDAMENTO` primeiro)
- Qualquer status → `EM_ANALISE` (não pode voltar)
- `CONCLUIDA` ou `CANCELADA` → qualquer (status finais)

### Cálculo Automático do Valor Total

O valor total da OS é recalculado automaticamente sempre que um serviço ou peça é adicionado/removido:

```
valorTotal = Σ (valorMaoDeObra de cada serviço)
           + Σ (valorUnitario × quantidade de cada peça)
```

### Validação de Estoque

Ao adicionar uma peça a uma OS, o sistema verifica se há estoque suficiente:
- Se `quantidadeEstoque < quantidade solicitada` → lança `BusinessException`
- Mensagem: *"Estoque insuficiente para a peça X. Disponível: Y, Solicitado: Z"*

### Bloqueio de Edição

Não é possível adicionar ou remover serviços/peças de uma OS com status `CONCLUIDA` ou `CANCELADA`.

---

## 12. Exemplos de Requisições

### Cadastrar Cliente

```
POST http://localhost:8080/api/clientes
Content-Type: application/json

{
    "nome": "João da Silva",
    "cpf": "123.456.789-00",
    "telefone": "(11) 99999-0000",
    "email": "joao@email.com",
    "endereco": "Rua das Flores, 123"
}
```

### Cadastrar Veículo

```
POST http://localhost:8080/api/veiculos
Content-Type: application/json

{
    "marca": "Chevrolet",
    "modelo": "Onix",
    "ano": 2022,
    "placa": "ABC-1D23",
    "cor": "Prata",
    "clienteId": 1
}
```

### Cadastrar Mecânico

```
POST http://localhost:8080/api/mecanicos
Content-Type: application/json

{
    "nome": "Carlos Mecânico",
    "cpf": "987.654.321-00",
    "especialidade": "Motor e Câmbio",
    "telefone": "(11) 98888-7777"
}
```

### Cadastrar Equipe

```
POST http://localhost:8080/api/equipes
Content-Type: application/json

{
    "nome": "Equipe Alfa",
    "mecanicoId": 1
}
```

### Cadastrar Serviço

```
POST http://localhost:8080/api/servicos
Content-Type: application/json

{
    "descricao": "Alinhamento e Balanceamento",
    "valorMaoDeObra": 120.00
}
```

### Cadastrar Peça

```
POST http://localhost:8080/api/pecas
Content-Type: application/json

{
    "nome": "Amortecedor Dianteiro",
    "descricao": "Amortecedor dianteiro universais",
    "valorUnitario": 40.00,
    "quantidadeEstoque": 20
}
```

### Abrir Ordem de Serviço

```
POST http://localhost:8080/api/ordens-servico
Content-Type: application/json

{
    "veiculoId": 1,
    "equipeId": 1,
    "problemaRelatado": "Barulho na suspensão dianteira",
    "observacoes": "Cliente relata que o barulho piora em lombadas"
}
```

**Resposta (201 Created):**
```json
{
    "id": 1,
    "dataAbertura": "2025-07-23",
    "dataConclusao": null,
    "status": "EM_ANALISE",
    "valorTotal": 0,
    "problemaRelatado": "Barulho na suspensão dianteira",
    "veiculoPlaca": "ABC-1D23",
    "equipeNome": "Equipe Alfa",
    "servicos": [],
    "pecas": []
}
```

### Alterar Status da OS

```
PATCH http://localhost:8080/api/ordens-servico/1/status
Content-Type: application/json

{
    "status": "EM_ANDAMENTO"
}
```

### Adicionar Serviço à OS

```
POST http://localhost:8080/api/ordens-servico/1/servicos
Content-Type: application/json

{
    "servicoId": 1
}
```

### Adicionar Peça à OS

```
POST http://localhost:8080/api/ordens-servico/1/pecas
Content-Type: application/json

{
    "pecaId": 1,
    "quantidade": 2
}
```

**Resposta após adicionar 1 serviço (R$120) e 2 peças (R$40 cada):**
```json
{
    "id": 1,
    "status": "EM_ANDAMENTO",
    "valorTotal": 200.00,
    "servicos": [
        {
            "servicoId": 1,
            "descricao": "Alinhamento e Balanceamento",
            "valorMaoDeObra": 120.00
        }
    ],
    "pecas": [
        {
            "pecaNome": "Amortecedor Dianteiro",
            "quantidade": 2,
            "valorUnitario": 40.00,
            "subtotal": 80.00
        }
    ]
}
```

### Concluir OS

```
PATCH http://localhost:8080/api/ordens-servico/1/status
Content-Type: application/json

{
    "status": "CONCLUIDA"
}
```

### Remover Serviço/Peça da OS

```
DELETE http://localhost:8080/api/ordens-servico/1/servicos/1
DELETE http://localhost:8080/api/ordens-servico/1/pecas/1
```

---

## 13. Tabelas do Banco de Dados

O Hibernate cria automaticamente as seguintes tabelas com `ddl-auto=update`:

```sql
-- Tabela de clientes
CREATE TABLE clientes (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(255) NOT NULL UNIQUE,
    telefone VARCHAR(255),
    email VARCHAR(255),
    endereco VARCHAR(255)
);

-- Tabela de veículos
CREATE TABLE veiculos (
    id BIGSERIAL PRIMARY KEY,
    marca VARCHAR(255) NOT NULL,
    modelo VARCHAR(255) NOT NULL,
    ano INTEGER NOT NULL,
    placa VARCHAR(255) NOT NULL UNIQUE,
    cor VARCHAR(255),
    cliente_id BIGINT NOT NULL REFERENCES clientes(id)
);

-- Tabela de mecânicos
CREATE TABLE mecanicos (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(255) NOT NULL UNIQUE,
    especialidade VARCHAR(255),
    telefone VARCHAR(255)
);

-- Tabela de equipes mecânicas
CREATE TABLE equipes_mecanicas (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    mecanico_id BIGINT NOT NULL REFERENCES mecanicos(id)
);

-- Tabela de serviços
CREATE TABLE servicos (
    id BIGSERIAL PRIMARY KEY,
    descricao VARCHAR(255) NOT NULL,
    valor_mao_de_obra NUMERIC(19,2) NOT NULL
);

-- Tabela de peças
CREATE TABLE pecas (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao VARCHAR(255),
    valor_unitario NUMERIC(19,2) NOT NULL,
    quantidade_estoque INTEGER NOT NULL
);

-- Tabela de ordens de serviço
CREATE TABLE ordens_servico (
    id BIGSERIAL PRIMARY KEY,
    data_abertura DATE NOT NULL,
    data_conclusao DATE,
    status VARCHAR(255) NOT NULL,
    observacoes TEXT,
    valor_total NUMERIC(19,2) NOT NULL,
    problema_relatado VARCHAR(255) NOT NULL,
    veiculo_id BIGINT NOT NULL REFERENCES veiculos(id),
    equipe_id BIGINT NOT NULL REFERENCES equipes_mecanicas(id)
);

-- Tabela intermediária: OS ↔ Serviços
CREATE TABLE ordens_servico_servicos (
    id BIGSERIAL PRIMARY KEY,
    ordem_servico_id BIGINT NOT NULL REFERENCES ordens_servico(id),
    servico_id BIGINT NOT NULL REFERENCES servicos(id)
);

-- Tabela intermediária: OS ↔ Peças
CREATE TABLE ordens_servico_pecas (
    id BIGSERIAL PRIMARY KEY,
    ordem_servico_id BIGINT NOT NULL REFERENCES ordens_servico(id),
    peca_id BIGINT NOT NULL REFERENCES pecas(id),
    quantidade INTEGER NOT NULL
);
```

---

*Projeto gerado para fins de estudo — Sistema de Oficina Mecânica com Spring Boot e PostgreSQL.*
