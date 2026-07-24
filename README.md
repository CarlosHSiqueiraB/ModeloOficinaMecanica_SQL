# Briefing do Projeto

## Estado Inicial

O projeto nasceu como um **diagrama EER (Enhanced Entity-Relationship)** de uma oficina mecânica, criado com fins acadêmicos de estudo de modelagem de dados. A partir desse diagrama, evolui para o desenvolvimento de uma API RESTful completa.

## Ferramenta de Nuvem Utilizada

**Microsoft Azure** — utilizei o plano gratuito de estudante (Student Account) já cadastrado. A escolha pela Azure se deu pela oportunidade de experimentar a plataforma e pelo objetivo original de deploy do backend como **PaaS (Platform as a Service)** utilizando o Azure App Service. Essa etapa de deploy não foi finalizada, mas o serviço foi configurado.

## O que será entregue

Um projeto **full stack simples** composto por:

- **Backend:** API RESTful em Java com Spring Boot e PostgreSQL
- **Frontend:** Aplicação vanilla HTML/CSS/JS
- **Banco de dados:** PostgreSQL hospedado no Azure Database (ou local)

O foco é demonstrar o fluxo completo de cadastro e gerenciamento de uma oficina mecânica, desde clientes e veículos até ordens de serviço com cálculo automático de valores.

# OficinaMecanicaBackend

API RESTful para gerenciamento de uma oficina mecânica, desenvolvida com **Java 17** e **Spring Boot 4.1.0**.

## Visão Geral

O sistema permite cadastrar clientes, veículos, mecânicos, equipes, serviços, peças e ordens de serviço, com regras de negócio para controle de status e cálculo automático de valores.

### Funcionalidades

- Cadastro completo de clientes e veículos
- Gerenciamento de mecânicos e equipes mecânicas
- Cadastro de serviços (mão de obra) e peças (preço e estoque)
- Abertura de Ordens de Serviço vinculando veículo + equipe
- Adição/remoção de serviços e peças em uma OS
- Cálculo automático do valor total da OS
- Controle de fluxo de status com regras de transição

## Tecnologias

| Tecnologia | Versão | Uso |
|---|---|---|
| Java | 17 | Linguagem do projeto |
| Spring Boot | 4.1.0 | Framework principal |
| Spring Web (MVC) | - | Endpoints REST |
| Spring Data JPA | - | Acesso ao banco via repositories |
| PostgreSQL | - | Banco de dados relacional |
| Hibernate | - | ORM |
| Lombok | - | Redução de boilerplate |
| Jakarta Validation | - | Validação de DTOs |
| Maven | 3.9.16 | Gerenciador de dependências |

## Pré-requisitos

- **Java 17** ou superior
- **PostgreSQL** instalado e rodando na porta 5432
- **Maven** (ou utilize o wrapper `./mvnw`)

## Configuração

### 1. Criar o banco de dados

```sql
CREATE DATABASE oficina_mecanica;
```

### 2. Configurar o `application.properties`

O arquivo está em `src/main/resources/application.properties`:

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

> O Hibernate cria e atualiza as tabelas automaticamente com `ddl-auto=update`.

### 3. Rodar o projeto

```bash
# Compilar
./mvnw compile

# Executar
./mvnw spring-boot:run

# Gerar JAR
./mvnw package
```

## Estrutura do Projeto

```
src/main/java/com/estudos/oficinamecanicabackend/
│
├── OficinaMecanicaBackendApplication.java    ← Ponto de entrada
│
├── config/
│   └── CorsConfig.java                       ← Configuração CORS
│
├── enums/
│   └── StatusOS.java                         ← Enum dos status da OS
│
├── entity/                                    ← Entidades JPA
│   ├── Cliente.java
│   ├── Veiculo.java
│   ├── Mecanico.java
│   ├── EquipeMecanica.java
│   ├── Servico.java
│   ├── Peca.java
│   ├── OrdemServico.java
│   ├── OrdemServicoServico.java
│   └── OrdemServicoPeca.java
│
├── repository/                                ← Acesso ao banco
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
├── dto/                                       ← Data Transfer Objects
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

## Endpoints

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/api/clientes` | Lista todos os clientes |
| `POST` | `/api/clientes` | Cadastra novo cliente |
| `GET` | `/api/veiculos` | Lista todos os veículos |
| `POST` | `/api/veiculos` | Cadastra novo veículo |
| `GET` | `/api/mecanicos` | Lista todos os mecânicos |
| `POST` | `/api/mecanicos` | Cadastra novo mecânico |
| `GET` | `/api/equipes` | Lista todas as equipes |
| `POST` | `/api/equipes` | Cadastra nova equipe |
| `GET` | `/api/servicos` | Lista todos os serviços |
| `POST` | `/api/servicos` | Cadastra novo serviço |
| `GET` | `/api/pecas` | Lista todas as peças |
| `POST` | `/api/pecas` | Cadastra nova peça |
| `GET` | `/api/ordens-servico` | Lista todas as OS |
| `POST` | `/api/ordens-servico` | Abre nova OS |
| `PATCH` | `/api/ordens-servico/{id}/status` | Altera status da OS |
| `POST` | `/api/ordens-servico/{id}/servicos` | Adiciona serviço à OS |
| `POST` | `/api/ordens-servico/{id}/pecas` | Adiciona peça à OS |

## Regras de Negócio

### Fluxo de Status

```
EM_ANALISE → EM_ANDAMENTO → CONCLUIDA
                 ↓
              CANCELADA
```

| Status Atual | Novo Status Permitido |
|---|---|
| `EM_ANALISE` | `EM_ANDAMENTO`, `CANCELADA` |
| `EM_ANDAMENTO` | `CONCLUIDA`, `CANCELADA` |
| `CONCLUIDA` | *(nenhum)* |
| `CANCELADA` | *(nenhum)* |

### Cálculo Automático do Valor Total

```
valorTotal = Σ (valorMaoDeObra de cada serviço)
           + Σ (valorUnitario × quantidade de cada peça)
```

## Frontend

O frontend é uma aplicação vanilla HTML/CSS/JS localizada na pasta `../OficinaMecanicaFrontend/`.

```bash
# Abrir com Live Server (VS Code)
# Clique com botão direito em index.html → "Open with Live Server"

# Ou usar servidor HTTP local
cd ../OficinaMecanicaFrontend
python3 -m http.server 5500
```
