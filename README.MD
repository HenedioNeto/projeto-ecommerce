# E-commerce API - Serviço de Usuários

[![Java](https://img.shields.io/badge/Java-17-blue.svg)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 📋 Sobre o Projeto

API para gerenciamento de usuários com autenticação JWT, parte de um sistema de e-commerce construído com microserviços.

### Funcionalidades

-  CRUD completo de usuários
-  Autenticação JWT
-  Roles (ADMIN/CUSTOMER)
-  Senhas criptografadas (BCrypt)
-  Testes unitários e de integração
-  Documentação Swagger/OpenAPI
-  Docker + PostgreSQL

## Tecnologias

- Java 17
- Spring Boot 3.2.0
- Spring Security
- JWT
- PostgreSQL
- Docker
- JUnit 5 + Mockito
- Swagger/OpenAPI

## Endpoints

| Método | Rota | Descrição | Autenticação |
|--------|------|-----------|--------------|
| POST | `/api/auth/login` | Login | Público |
| POST | `/api/usuarios` | Criar usuário | Público |
| GET | `/api/usuarios` | Listar usuários | ADMIN |
| GET | `/api/usuarios/{id}` | Buscar por ID | ADMIN |
| PUT | `/api/usuarios/{id}` | Atualizar | ADMIN |
| DELETE | `/api/usuarios/{id}` | Deletar | ADMIN |

## Como Rodar

### Pré-requisitos

- Docker Desktop
- JDK 17
- Maven

### Passos

```bash
# 1. Subir o banco de dados
docker compose up -d

# 2. Rodar a aplicação
./mvnw spring-boot:run