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
- Testes unitarios e de integracao
- Docker + PostgreSQL
- CI/CD com GitHub Actions

## Tecnologias

- Java 17
- Spring Boot 3.2.0
- Spring Security
- JWT (JSON Web Token)
- PostgreSQL
- Docker
- JUnit 5 + Mockito
- GitHub Actions

### Arquitetura

**Servicos:**

- **User Service (porta 8080)**
    - CRUD de usuarios
    - Autenticacao JWT
    - Roles (ADMIN/CUSTOMER)
    - Banco: user_db (porta 5432)

- **Product Service (porta 8082)**
    - CRUD de produtos
    - Categorias
    - Estoque
    - Banco: product_db (porta 5433)

**Infraestrutura:**
- PostgreSQL com Docker
- GitHub Actions para CI/CD

## Endpoints

### User Service (porta 8080)

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

## Como Rodar

### Pre-requisitos

- Docker Desktop
- JDK 17
- Maven

### Passos

```bash
# 1. Subir os bancos de dados
docker compose up -d

# 2. Rodar o User Service (porta 8080)
cd user-service
./mvnw spring-boot:run

# 3. Rodar o Product Service (porta 8082)
# Em outro terminal:
cd product-service
./mvnw spring-boot:run
```
### Testes

```bash
# Rodar testes do User Service
cd user-service
./mvnw clean test

# Rodar testes do Product Service
cd product-service
./mvnw clean test
```