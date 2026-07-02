# E-commerce API - Microservicos

[![Java](https://img.shields.io/badge/Java-17-blue.svg)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![CI](https://github.com/HenedioNeto/projeto-ecommerce/actions/workflows/ci.yml/badge.svg)](https://github.com/HenedioNeto/projeto-ecommerce/actions)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## Sobre o Projeto

Sistema de e-commerce construido com microservicos em Java Spring Boot, com autenticacao JWT, banco de dados PostgreSQL e Docker.

### Funcionalidades

- CRUD completo de usuarios
- Autenticacao JWT com Spring Security
- Roles (ADMIN/CUSTOMER)
- Senhas criptografadas (BCrypt)
- CRUD de produtos
- Categorias e estoque
- Carrinho de compras
- Criacao de pedidos
- Testes unitarios e de integracao
- Docker + PostgreSQL
- CI/CD com GitHub Actions
- API Gateway com Rate Limiting e Circuit Breaker
- Service Discovery com Eureka

## Tecnologias

- Java 17
- Spring Boot 3.2.0
- Spring Security
- JWT (JSON Web Token)
- PostgreSQL
- Docker
- JUnit 5 + Mockito
- GitHub Actions
- Feign Client
- Spring Cloud Gateway
- Resilience4j (Circuit Breaker)
- Redis (Rate Limiting)
- Netflix Eureka (Service Discovery)

### Arquitetura

**Servicos:**

- **Eureka Server (porta 8761)**
    - Service Discovery
    - Registro e descoberta de servicos
    - Dashboard de monitoramento

- **API Gateway (porta 8080)**
    - Roteamento de requisicoes
    - Rate Limiting (5 req/segundo)
    - Circuit Breaker com fallback
    - Autenticacao JWT
    - CORS configurado

- **User Service (porta 8081)**
    - CRUD de usuarios
    - Autenticacao JWT
    - Roles (ADMIN/CUSTOMER)
    - Banco: user_db (porta 5432)

- **Product Service (porta 8082)**
    - CRUD de produtos
    - Categorias
    - Estoque
    - Banco: product_db (porta 5433)

- **Order Service (porta 8083)**
    - Carrinho de compras
    - Criacao de pedidos
    - Estatisticas de produtos
    - Comunicacao com Product Service via Feign
    - Banco: order_db (porta 5434)

**Infraestrutura:**
- PostgreSQL com Docker
- Redis para Rate Limiting
- GitHub Actions para CI/CD
- Eureka para Service Discovery

## Endpoints

### Eureka Dashboard

Acesse o dashboard do Eureka para visualizar todos os servicos registrados:
http://localhost:8761

### API Gateway (porta 8080)

Todas as requisicoes passam pelo Gateway, que redireciona para os servicos correspondentes.

### User Service (porta 8081)

| Metodo | Rota | Descricao | Autenticacao |
|--------|------|-----------|--------------|
| POST | /api/auth/login | Login | Publico |
| POST | /api/usuarios | Criar usuario | Publico |
| GET | /api/usuarios | Listar usuarios | ADMIN |
| GET | /api/usuarios/{id} | Buscar por ID | ADMIN |
| PUT | /api/usuarios/{id} | Atualizar | ADMIN |
| DELETE | /api/usuarios/{id} | Deletar | ADMIN |

### Product Service (porta 8082)

| Metodo | Rota | Descricao | Autenticacao |
|--------|------|-----------|--------------|
| GET | /api/produtos | Listar produtos | Publico |
| GET | /api/produtos/{id} | Buscar por ID | Publico |
| GET | /api/produtos/categoria/{categoria} | Buscar por categoria | Publico |
| GET | /api/produtos/buscar?nome= | Buscar por nome | Publico |
| POST | /api/produtos | Criar produto | ADMIN |
| PUT | /api/produtos/{id} | Atualizar | ADMIN |
| DELETE | /api/produtos/{id} | Deletar | ADMIN |

### Order Service (porta 8083)

| Metodo | Rota | Descricao | Autenticacao |
|--------|------|-----------|--------------|
| POST | /api/carrinho/adicionar | Adicionar ao carrinho | Usuario logado |
| GET | /api/carrinho | Buscar carrinho | Usuario logado |
| DELETE | /api/carrinho | Limpar carrinho | Usuario logado |
| POST | /api/pedidos | Criar pedido | Usuario logado |
| GET | /api/pedidos | Listar pedidos | Usuario logado |
| GET | /api/pedidos/{id} | Buscar pedido | Usuario logado |
| GET | /api/pedidos/admin/produto/{id} | Pedidos por produto | ADMIN |
| GET | /api/pedidos/admin/produto/{id}/estatisticas | Estatisticas do produto | ADMIN |

## Como Rodar

### Pre-requisitos

- Docker Desktop
- JDK 17
- Maven

### Passos

```bash
# 1. Subir os bancos de dados e Redis
docker compose up -d

# 2. Rodar o Eureka Server (porta 8761)
cd eureka-server
./mvnw spring-boot:run

# 3. Rodar o API Gateway (porta 8080)
# Em outro terminal:
cd api-gateway
./mvnw spring-boot:run

# 4. Rodar o User Service (porta 8081)
# Em outro terminal:
cd user-service
./mvnw spring-boot:run

# 5. Rodar o Product Service (porta 8082)
# Em outro terminal:
cd product-service
./mvnw spring-boot:run

# 6. Rodar o Order Service (porta 8083)
# Em outro terminal:
cd order-service
./mvnw spring-boot:run