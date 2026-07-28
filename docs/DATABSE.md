# Database Schema & Configuration

## Overview
CampusFlow uses PostgreSQL with a **Hybrid JSONB Schema**. Core authentication and routing fields are stored as native relational columns, while role-specific attributes are encapsulated inside a flexible `metadata` JSONB column.

## Tables

### `users`
| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `user_id` | SERIAL | PRIMARY KEY | Unique identifier for every user |
| `username` | VARCHAR(50) | UNIQUE, NOT NULL | Login handle |
| `password` | VARCHAR(255) | NOT NULL | Account password (hashed/plaintext) |
| `role` | VARCHAR(20) | NOT NULL | Role classification (`ADMIN`, `HOD`, `FACULTY`, `STUDENT`) |
| `department` | VARCHAR(100) | NULLABLE | Associated department |
| `metadata` | JSONB | NULLABLE | Role-specific attributes (e.g., `year_of_study`, `office_room`) |

## Indexes
To ensure fast retrieval of metadata queries, a **GIN (Generalized Inverted Index)** is applied to the metadata column:
```sql
CREATE INDEX idx_users_metadata ON users USING GIN (metadata);

-- Admin Seed
INSERT INTO users (username, password, role, department, metadata) 
VALUES ('sys_admin', 'adminpass', 'ADMIN', 'Administration', '{"access_level": "super_admin"}');

-- HOD Seed
INSERT INTO users (username, password, role, department, metadata) 
VALUES ('cs_hod', 'hodpass', 'HOD', 'Computer Science', '{"office_room": "Lab 302", "extension": "4021"}');

-- Student Seed
INSERT INTO users (username, password, role, department, metadata) 
VALUES ('st_alex', 'studpass', 'STUDENT', 'Computer Science', '{"year_of_study": 3, "major": "Computer Science"}');
```