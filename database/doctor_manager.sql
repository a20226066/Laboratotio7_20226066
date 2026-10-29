-- Laboratorio Doctor-Manager: esquema basado en el diagrama del enunciado.
-- Ejecutar en MySQL Workbench ANTES de iniciar la aplicación.
-- NOTA: al ejecutar otra vez, se BORRAN los registros de estas tablas.
CREATE DATABASE IF NOT EXISTS doctor_manager CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE doctor_manager;

DROP TABLE IF EXISTS clinic_doctor;
DROP TABLE IF EXISTS doctor_specialty;
DROP TABLE IF EXISTS doctor;
DROP TABLE IF EXISTS clinic;
DROP TABLE IF EXISTS specialty;
DROP TABLE IF EXISTS doctor_state;

CREATE TABLE doctor_state (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE
);
CREATE TABLE specialty (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(70) NOT NULL UNIQUE,
    code CHAR(6) NOT NULL UNIQUE
);
CREATE TABLE clinic (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(150) NOT NULL,
    phone_number CHAR(9) NOT NULL
);
CREATE TABLE doctor (
    id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    dni CHAR(8) NOT NULL UNIQUE,
    phone_number CHAR(9) NOT NULL,
    email VARCHAR(100) NOT NULL,
    incorporation_date DATE NOT NULL,
    state_id INT NOT NULL,
    CONSTRAINT fk_doctor_state FOREIGN KEY (state_id) REFERENCES doctor_state(id)
);
-- Estas tablas intermedias representan relaciones muchos-a-muchos.
-- PK compuesta: cada pareja se guarda como máximo una vez.
CREATE TABLE doctor_specialty (
    doctor_id INT NOT NULL,
    specialty_id INT NOT NULL,
    PRIMARY KEY (doctor_id, specialty_id),
    CONSTRAINT fk_ds_doctor FOREIGN KEY (doctor_id) REFERENCES doctor(id) ON DELETE CASCADE,
    CONSTRAINT fk_ds_specialty FOREIGN KEY (specialty_id) REFERENCES specialty(id) ON DELETE RESTRICT
);
CREATE TABLE clinic_doctor (
    clinic_id INT NOT NULL,
    doctor_id INT NOT NULL,
    PRIMARY KEY (clinic_id, doctor_id),
    CONSTRAINT fk_cd_clinic FOREIGN KEY (clinic_id) REFERENCES clinic(id) ON DELETE CASCADE,
    CONSTRAINT fk_cd_doctor FOREIGN KEY (doctor_id) REFERENCES doctor(id) ON DELETE CASCADE
);

INSERT INTO doctor_state (name) VALUES ('Activo'), ('Inactivo'), ('Jubilado');
INSERT INTO specialty (name, code) VALUES
('Cardiología', 'CARDIO'), ('Pediatría', 'PEDIAT'),
('Traumatología', 'TRAUMA'), ('Dermatología', 'DERMAT'),
('Neurología', 'NEUROL'), ('Oftalmología', 'OFTALM');
