# Project Context

## Tech Stack
* **Language:** Java
* **Database:** PostgreSQL
* **Driver:** JDBC (`PreparedStatement`, `ResultSet`)
* **Architecture Pattern:** Layered Architecture (Models, Repositories, Config) with Hybrid JSONB persistence.

## Core Features
* Role-Based Access Control (RBAC) supporting **ADMIN**, **HOD**, **FACULTY**, and **STUDENT**.
* Unified authentication handling across all user profiles via `UserRepository`.