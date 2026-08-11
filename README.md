# 📅 Agendamento API

> API REST para gerenciamento de agendamentos, desenvolvida com Java e Spring Boot, com persistência em PostgreSQL e validação de conflitos de horários por usuário.

[![Java](https://img.shields.io/badge/Java-25-ED8B00?logo=openjdk\&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.x-6DB33F?logo=springboot\&logoColor=white)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16%2B-4169E1?logo=postgresql\&logoColor=white)](https://www.postgresql.org/)
[![Maven](https://img.shields.io/badge/Maven-3.9%2B-C71A36?logo=apachemaven\&logoColor=white)](https://maven.apache.org/)

## 📖 Sobre o projeto

O **Agendamento API** é um serviço backend REST criado para centralizar o gerenciamento do ciclo de vida de agendamentos.

A aplicação permite criar, consultar, atualizar, cancelar e concluir agendamentos, mantendo regras de negócio para evitar sobreposição de horários entre agendamentos ativos do mesmo usuário.

O projeto foi estruturado seguindo uma separação clara de responsabilidades entre **controllers, DTOs, services, repositories, mappers e models**, favorecendo manutenção, evolução e organização do código.

## ✨ Funcionalidades

* ✅ Criação de novos agendamentos
* 🔎 Consulta de agendamento por ID
* ✏️ Atualização parcial de dados
* ❌ Cancelamento de agendamentos
* 🏁 Conclusão de agendamentos
* 🚫 Validação de conflitos de horário por usuário
* ⏱️ Validação do intervalo entre início e fim
* 🧾 Validação dos dados de entrada
* 🗄️ Persistência com Spring Data JPA e PostgreSQL
* 🔄 Gerenciamento de banco com Flyway
* 🧩 Separação entre entidades e contratos da API por meio de DTOs

## 🏗️ Arquitetura

```text
src/main/java/dev/spjch/agendamento
├── controller/     # Endpoints HTTP
├── dto/            # Objetos de entrada e saída
├── mapper/         # Conversão DTO ↔ entidade
├── model/          # Entidades e enums de domínio
├── repository/     # Acesso ao banco
└── service/        # Regras de negócio
```

### Fluxo de uma requisição

```text
Cliente HTTP
    │
    ▼
Controller
    │
    ▼
DTO + Validation
    │
    ▼
Service
    │
    ├── Regras de negócio
    ├── Validação de intervalo
    └── Verificação de conflito
    │
    ▼
Repository
    │
    ▼
PostgreSQL
```

## 🧰 Tecnologias

| Tecnologia             | Utilização             |
| ---------------------- | ---------------------- |
| **Java 25**            | Linguagem principal    |
| **Spring Boot**        | Framework da aplicação |
| **Spring Web MVC**     | API REST               |
| **Spring Data JPA**    | Persistência           |
| **Jakarta Validation** | Validação dos payloads |
| **PostgreSQL**         | Banco de dados         |
| **Flyway**             | Migrações do banco     |
| **Lombok**             | Redução de boilerplate |
| **Maven**              | Build e dependências   |

## 📋 Modelo de domínio

Um agendamento possui:

* `id`
* `titulo`
* `descricao`
* `dataInicio`
* `dataFim`
* `status`
* `usuario`
* `criadoEm`
* `atualizadoEm`

### Status disponíveis

| Status      | Descrição              |
| ----------- | ---------------------- |
| `AGENDADO`  | Agendamento ativo      |
| `CANCELADO` | Agendamento cancelado  |
| `CONCLUIDO` | Agendamento finalizado |

Novos agendamentos são criados com status `AGENDADO`.

## 🔌 API REST

A API utiliza o prefixo:

```text
/agendamentos
```

### Criar agendamento

```http
POST /agendamentos
Content-Type: application/json
```

Exemplo:

```json
{
  "titulo": "Reunião de planejamento",
  "descricao": "Reunião semanal da equipe",
  "dataInicio": "2026-08-12T09:00:00",
  "dataFim": "2026-08-12T10:00:00",
  "usuario": "carlos"
}
```

Campos obrigatórios:

* `titulo` — até 120 caracteres
* `dataInicio`
* `dataFim`
* `usuario` — até 80 caracteres

O campo `descricao` é opcional e aceita até 4.000 caracteres.

### Buscar por ID

```http
GET /agendamentos/{id}
```

Exemplo:

```bash
curl http://localhost:8080/agendamentos/1
```

### Atualizar agendamento

```http
PUT /agendamentos/{id}
Content-Type: application/json
```

Exemplo:

```json
{
  "titulo": "Reunião atualizada",
  "descricao": "Nova descrição",
  "dataInicio": "2026-08-12T14:00:00",
  "dataFim": "2026-08-12T15:00:00"
}
```

Os campos são opcionais, permitindo atualizar somente as informações necessárias.

### Cancelar

```http
PUT /agendamentos/{id}/cancelar
```

Exemplo:

```bash
curl -X PUT http://localhost:8080/agendamentos/1/cancelar
```

### Concluir

```http
PUT /agendamentos/{id}/concluir
```

Exemplo:

```bash
curl -X PUT http://localhost:8080/agendamentos/1/concluir
```

## 🚫 Regra de conflito de horários

A aplicação impede que um mesmo usuário possua dois agendamentos com status `AGENDADO` que se sobreponham.

A regra utiliza:

```text
agendamentoExistente.inicio < novoFim
AND
agendamentoExistente.fim > novoInicio
```

Intervalos adjacentes não são considerados conflitantes:

```text
09:00 ───── 10:00
             10:00 ───── 11:00
```

Nesse caso, os dois horários podem coexistir.

## ⚙️ Pré-requisitos

* **JDK 25**
* **Maven 3.9+** — opcional, pois o projeto possui Maven Wrapper
* **PostgreSQL**
* **Git**

Crie o banco:

```sql
CREATE DATABASE agendamento;
```

## 🚀 Como executar

### 1. Clone o projeto

```bash
git clone https://github.com/Carlos1681/agendamento.git
cd agendamento
```

### 2. Configure o PostgreSQL

A aplicação utiliza as variáveis de ambiente:

```bash
export DB_USERNAME=postgres
export DB_PASSWORD=sua_senha
```

No Windows PowerShell:

```powershell
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="sua_senha"
```

Configuração padrão:

```text
Host:     localhost
Porta:    5432
Banco:    agendamento
Usuário:  ${DB_USERNAME}
Senha:    ${DB_PASSWORD}
```

### 3. Execute a aplicação

Linux/macOS:

```bash
./mvnw spring-boot:run
```

Windows:

```powershell
mvnw.cmd spring-boot:run
```

Ou utilizando Maven instalado:

```bash
mvn spring-boot:run
```

A aplicação ficará disponível em:

```text
http://localhost:8080
```

## 🧪 Testes

Execute:

```bash
./mvnw test
```

No Windows:

```powershell
mvnw.cmd test
```

O projeto possui testes baseados no suporte de testes do Spring Boot, incluindo teste de carregamento do contexto da aplicação.

## 📦 Build

Para gerar o artefato:

```bash
./mvnw clean package
```

O `.jar` será criado em:

```text
target/
```

Execute com:

```bash
java -jar target/agendamento-0.0.1-SNAPSHOT.jar
```

## 🗂️ Estrutura do projeto

```text
agendamento/
├── .mvn/
│   └── wrapper/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── dev/spjch/agendamento/
│   │   │       ├── controller/
│   │   │       ├── dto/
│   │   │       ├── mapper/
│   │   │       ├── model/
│   │   │       ├── repository/
│   │   │       └── service/
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
├── .gitignore
├── mvnw
├── mvnw.cmd
└── pom.xml
```

## 🔐 Configuração e segurança

As credenciais do banco **não devem ser versionadas**.

O projeto utiliza:

```text
DB_USERNAME
DB_PASSWORD
```

Para ambientes de produção, recomenda-se utilizar secrets ou mecanismos de configuração segura fornecidos pela infraestrutura de deploy.

## 🛠️ Decisões de implementação

### DTOs

A API utiliza objetos específicos para entrada e saída, evitando expor diretamente as entidades JPA.

Principais DTOs:

* `AgendamentoCreateRequest`
* `AgendamentoUpdateRequest`
* `AgendamentoResponse`

### Service Layer

O `AgendamentoService` concentra as principais regras de negócio:

* validação do intervalo;
* prevenção de conflitos;
* busca de registros;
* alteração de status;
* persistência das operações.

### Repository

O `AgendamentoRepository` estende `JpaRepository` e possui uma consulta específica para detectar sobreposição de horários de agendamentos ativos.

### Mapper

O `AgendamentoMapper` concentra a conversão entre DTOs e entidades, mantendo controllers e services mais organizados.



<p align="center">
  <strong>Agendamento API</strong><br>
  Backend REST para gerenciamento de agendamentos com Java, Spring Boot e PostgreSQL.
</p>
