# Batismo API

[![Java](https://img.shields.io/badge/language-Java-blue.svg)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-Framework-green.svg)](https://spring.io/projects/spring-boot)
![Public](https://img.shields.io/badge/status-public-brightgreen.svg)

API para gerenciamento do processo de batismo, catecumenato e usuários da paróquia. Desenvolvida em Java com Spring Boot.

## 🚀 Funcionalidades

A API oferece endpoints REST para:

- **Batizados**
    - Cadastrar, editar e excluir batizados (`POST`, `PATCH`, `DELETE`)
    - Listar batizados com filtros de mês e ano (`GET /batizados`)
    - Realocar casais relacionados ao batizado (`PATCH /batizados/realocar`)
- **Catecúmenos**
    - Listar todos os catecúmenos (`GET /catecumenos`)
- **Casais**
    - Listar casais (`GET /casais`)
    - Trocar ordem dos casais (`PATCH /casais/ordem`)
- **Usuários**
    - Login/autenticação JWT (`POST /login`)
    - Listar, cadastrar, editar, ativar/inativar usuários (`GET`, `POST`, `PATCH` em `/usuarios`)
    - Permissões por escopo de cargo (admin, secretaria, coordenador)

Todos os endpoints sensíveis utilizam autorização baseada em cargos e escopos (admin, secretaria, coordenador).

## 🧑‍💻 Principais Tecnologias

- **Java** e **Spring Boot** (REST API)
- **Spring Security** (autenticação/autorizações JWT)
- **Hibernate** (ORM para persistência e manipulação dos dados)
- **Flyway** (controle e versionamento de migrações do banco de dados)
- **Banco de Dados**: Utiliza JPA/Hibernate para persistência (pode ser adaptado para o banco de dados desejado)
- **Maven** (gerenciamento de dependências)

As migrações do banco de dados são gerenciadas automaticamente pelo **Flyway** ao iniciar o projeto.


## 🔒 Segurança

- Autenticação via JWT (token gerado pelo endpoint `/login`)
- Autorizações baseadas em cargos (`admin`, `secretaria`, `coordenador`)
- Proteção dos endpoints com `@PreAuthorize`


## Autor

- [davicesarm](https://github.com/davicesarm)

---

> Projeto público para fins de aprendizado e apoio à organização paroquial.