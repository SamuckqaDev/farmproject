# 🦆 Farm Project API

API robusta para **gerenciamento de uma fazenda de patos**, focada em **controle de linhagem**, **automação de vendas com precificação dinâmica** e **auditoria de performance comercial**.

O projeto foi desenvolvido com foco em **boas práticas de arquitetura backend**, **Clean Code**, **alta testabilidade** e **regras de negócio bem definidas**, servindo tanto como base para sistemas reais quanto como projeto de portfólio avançado.

---

## 📌 Visão Geral

A Farm Project API permite:
- Rastrear a **linhagem hierárquica** dos patos (mães e filhos)
- Automatizar **vendas com regras dinâmicas de preço**
- Aplicar **descontos automáticos para clientes VIP**
- Avaliar a **performance de vendedores**
- Gerar **relatórios em Excel com hierarquia visual**
- Explorar e testar a API via **Swagger UI**

---

## 🚀 Funcionalidades Principais

### 🧬 Linhagem Hierárquica
- Cadastro de patos como **Mãe** ou **Filho**
- Relacionamento hierárquico persistido no banco
- Rastreabilidade completa da linhagem

### 💰 Vendas com Precificação Dinâmica
- Preço unitário calculado automaticamente:
  - Baseado na quantidade de filhos
- Aplicação automática de desconto para clientes elegíveis
- Regras isoladas na camada de serviço

### 📊 Ranking de Performance
- Monitoramento de vendas por vendedor
- Filtros por período customizado
- Ranking ordenado por volume de vendas

### 📈 Relatórios Inteligentes
- Exportação para **Excel (.xlsx)**
- Organização hierárquica visual:
  - Mães no topo
  - Filhos com recuo de **3 espaços**

### 📚 Documentação Viva
- Swagger UI integrado
- Testes de endpoints diretamente pelo navegador

---

## 🏗️ Arquitetura

O projeto segue uma arquitetura em camadas:

- **Controller** → Entrada da API (REST)
- **Service** → Regras de negócio
- **Repository** → Persistência (JPA)
- **Model** → Entidades 
- **DTOs / Records** → Comunicação entre camadas

Princípios aplicados:
- SRP (Single Responsibility Principle)
- Separation of Concerns
- Domain-driven business rules
- Código altamente testável

---

## 🛠️ Tecnologias e Stack

### Backend
- **Java 21**
  - Records
  - Pattern Matching
  - Stream API

- **Spring Boot 3.4.1**
- **Spring Data JPA**
- **Hibernate**

### Banco de Dados
- **PostgreSQL**
- **Liquibase** (versionamento de schema)

### Documentação
- **SpringDoc OpenAPI**
- **Swagger UI**

### Relatórios
- **Apache POI**

### Testes
- **JUnit 5**
- **Mockito**
- **AssertJ**

---

## 📋 Regras de Negócio

| Ação               | Regra                                                              |
| ------------------ | ------------------------------------------------------------------ |
| **Preço Unitário** | 0 filhos → R$ 70,00<br>1 filho → R$ 50,00                          |
| **Desconto**       | Clientes com `eligibleDiscount = true` recebem **20% de desconto** |
| **Exclusão**       | Vendedores com vendas associadas **não podem ser excluídos**       |
| **Relatório**      | Filhos aparecem com **recuo de 3 espaços** no Excel                |


## 📦 Como Instalar e Rodar

1️⃣ Clonar o repositório
git clone https://github.com/seu-usuario/farm-project.git
cd farm-project

2️⃣ Subir o banco de dados (PostgreSQL)
docker-compose up -d

3️⃣ Configurar a aplicação
src/main/resources/application.yml

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/farm_db
    username: duck
    password: duck_pass
    driver-class-name: org.postgresql.Driver

4️⃣ Executar os testes
mvn clean test

5️⃣ Iniciar a aplicação
mvn spring-boot:run

6️⃣ Acessar a aplicação
API:
http://localhost:8080

Swagger UI:
http://localhost:8080/swagger-ui.html

