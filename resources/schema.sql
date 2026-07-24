-- 1. Create the Users table supporting all specific roles and department routing
CREATE TABLE IF NOT EXISTS users (
                                     user_id SERIAL PRIMARY KEY,
                                     username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('ADMIN', 'FACULTY', 'HOD', 'STUDENT')),
    department VARCHAR(50) NOT NULL,
    year_of_study INT -- Nullable for Faculty/Admins, populated for students
    );

-- 2. Create the Resources table mapped directly to specific departments
CREATE TABLE IF NOT EXISTS resources (
                                         resource_id SERIAL PRIMARY KEY,
                                         name VARCHAR(100) NOT NULL,
    capacity INT NOT NULL,
    department VARCHAR(50) NOT NULL, -- e.g., 'Computer Science', 'Electrical'
    requires_approval BOOLEAN DEFAULT FALSE
    );

-- 3. Seed initial system entities for testing
INSERT INTO users (username, password, role, department, year_of_study) VALUES
                                                                            ('cs_hod', 'hodpass', 'HOD', 'Computer Science', NULL),
                                                                            ('ee_hod', 'hodpass', 'HOD', 'Electrical Engineering', NULL),
                                                                            ('prof_smith', 'profpass', 'FACULTY', 'Computer Science', NULL)
    ON CONFLICT (username) DO NOTHING;

INSERT INTO resources (name, capacity, department, requires_approval) VALUES
                                                                          ('CS Advanced Computing Lab', 40, 'Computer Science', TRUE),
                                                                          ('CS Open Study Lounge', 60, 'Computer Science', FALSE),
                                                                          ('EE High Voltage Lab', 20, 'Electrical Engineering', TRUE),
                                                                          ('EE Circuit Design Room', 30, 'Electrical Engineering', FALSE)
    ON CONFLICT DO NOTHING;