-- Crear usuario
CREATE USER 'juancarlos'@'localhost' IDENTIFIED BY 'juancarlos';
-- Dar privilegios específicos
GRANT ALL PRIVILEGES ON guzpasen.* TO 'juancarlos'@'localhost';
-- Verificar los privilegios
SHOW GRANTS FOR 'juancarlos'@'localhost';

-- -----------------------------------------------------
-- SCHEMA guzpasen
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS guzpasen;
CREATE DATABASE IF NOT EXISTS guzpasen;
USE guzpasen;
ALTER DATABASE guzpasen CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
SET default_storage_engine = InnoDB;

-- -----------------------------------------------------
-- TABLA USUARIO
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS usuario (
    id_usuario BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    clave VARCHAR(255) NOT NULL,
    rol ENUM('ADMINISTRADOR', 'PROFESOR', 'GESTOR_INCIDENCIAS', 'TECNICO') NOT NULL,
    usuario_movil BOOLEAN DEFAULT FALSE
);

-- -----------------------------------------------------
-- TABLA ZONA
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS zona (
    id_zona BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(30) NOT NULL,
    tipo ENUM('AULA', 'PASILLO', 'ASEO', 'PATIO', 'GIMNASIO', 'DEPARTAMENTO', 'BIBLIOTECA', 'OTROS') NOT NULL,
    planta ENUM('P0', 'P1', 'P2') NOT NULL
);

-- -----------------------------------------------------
-- TABLA GUARDIA
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS guardia (
    id_guardia BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha DATE NOT NULL,
    hora_inicio ENUM('PRIMERA', 'SEGUNDA', 'TERCERA', 'CUARTA', 'QUINTA', 'SEXTA') NOT NULL,
    hora_fin ENUM('PRIMERA', 'SEGUNDA', 'TERCERA', 'CUARTA', 'QUINTA', 'SEXTA') NOT NULL,
    id_profesor BIGINT NOT NULL,
    id_aula BIGINT NOT NULL,
    FOREIGN KEY (id_profesor) REFERENCES usuario(id_usuario),
    FOREIGN KEY (id_aula) REFERENCES zona(id_zona)
);

-- -----------------------------------------------------
-- TABLA AUSENCIA
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS ausencia (
    id_ausencia BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha DATE NOT NULL,
    hora_inicio ENUM('PRIMERA', 'SEGUNDA', 'TERCERA', 'CUARTA', 'QUINTA', 'SEXTA') NOT NULL,
    hora_fin ENUM('PRIMERA', 'SEGUNDA', 'TERCERA', 'CUARTA', 'QUINTA', 'SEXTA') NOT NULL,
    motivo VARCHAR(255) NOT NULL,
    estado ENUM('PENDIENTE_DE_GUARDIA', 'GUARDIA_ASIGNADA') NOT NULL,
    tarea_alumnado VARCHAR(255),
    id_guardia BIGINT,
    id_profesor BIGINT NOT NULL,
    id_zona BIGINT NOT NULL,
    CONSTRAINT fk_ausencia_guardia FOREIGN KEY (id_guardia) REFERENCES guardia(id_guardia),
    CONSTRAINT fk_ausencia_usuario FOREIGN KEY (id_profesor) REFERENCES usuario(id_usuario),
    CONSTRAINT fk_ausencia_zona FOREIGN KEY (id_zona) REFERENCES zona(id_zona)
);

-- -----------------------------------------------------
-- INSERTAR 10 REGISTROS POR TABLA
-- -----------------------------------------------------
INSERT INTO usuario (nombre, apellidos, email, clave, rol, usuario_movil) VALUES
('Juan', 'Pérez García', 'juan.perez@example.com', 'clave123', 'PROFESOR', true),
('Laura', 'Sánchez Ruiz', 'laura.sanchez@example.com', 'clave123', 'PROFESOR', true),
('Carlos', 'López Díaz', 'carlos.lopez@example.com', 'clave123', 'TECNICO', false),
('Ana', 'Martínez Gómez', 'ana.martinez@example.com', 'clave123', 'GESTOR_INCIDENCIAS', true),
('Marta', 'Núñez Pérez', 'marta.nunez@example.com', 'clave123', 'PROFESOR', false),
('Pedro', 'Ramírez Torres', 'pedro.ramirez@example.com', 'clave123', 'ADMINISTRADOR', true),
('Lucía', 'Ortega León', 'lucia.ortega@example.com', 'clave123', 'PROFESOR', true),
('David', 'Iglesias Mora', 'david.iglesias@example.com', 'clave123', 'PROFESOR', true),
('Elena', 'Navarro Vidal', 'elena.navarro@example.com', 'clave123', 'TECNICO', false),
('Javier', 'Castro Reyes', 'javier.castro@example.com', 'clave123', 'PROFESOR', true);

INSERT INTO zona (nombre, tipo, planta) VALUES
('Aula 101', 'AULA', 'P0'),
('Aula 102', 'AULA', 'P0'),
('Pasillo Norte', 'PASILLO', 'P0'),
('Aseo Principal', 'ASEO', 'P1'),
('Patio Central', 'PATIO', 'P0'),
('Gimnasio A', 'GIMNASIO', 'P1'),
('Departamento Matemáticas', 'DEPARTAMENTO', 'P1'),
('Biblioteca General', 'BIBLIOTECA', 'P2'),
('Aula 201', 'AULA', 'P1'),
('Sala de profesores', 'OTROS', 'P1');

INSERT INTO guardia (fecha, hora_inicio, hora_fin, id_profesor, id_aula) VALUES
('2025-05-20', 'PRIMERA', 'SEGUNDA', 1, 1),
('2025-05-20', 'TERCERA', 'CUARTA', 2, 2),
('2025-05-21', 'QUINTA', 'SEXTA', 5, 3),
('2025-05-21', 'SEGUNDA', 'TERCERA', 7, 4),
('2025-05-22', 'CUARTA', 'QUINTA', 8, 5),
('2025-05-22', 'PRIMERA', 'SEGUNDA', 1, 6),
('2025-05-23', 'TERCERA', 'CUARTA', 2, 7),
('2025-05-23', 'QUINTA', 'SEXTA', 5, 8),
('2025-05-24', 'SEGUNDA', 'TERCERA', 7, 9),
('2025-05-24', 'CUARTA', 'QUINTA', 8, 10);

INSERT INTO ausencia (fecha, hora_inicio, hora_fin, motivo, estado, tarea_alumnado, id_guardia, id_profesor, id_zona) VALUES
('2025-05-20', 'PRIMERA', 'SEGUNDA', 'Reunión externa', 'PENDIENTE_DE_GUARDIA', 'Leer capítulo 4', 1, 1, 1),
('2025-05-20', 'TERCERA', 'CUARTA', 'Cita médica', 'GUARDIA_ASIGNADA', 'Ejercicios página 30', 2, 2, 2),
('2025-05-21', 'QUINTA', 'SEXTA', 'Curso de formación', 'PENDIENTE_DE_GUARDIA', 'Trabajo en grupo', 3, 5, 3),
('2025-05-21', 'SEGUNDA', 'TERCERA', 'Motivos personales', 'GUARDIA_ASIGNADA', 'Estudio libre', 4, 7, 4),
('2025-05-22', 'CUARTA', 'QUINTA', 'Taller fuera del centro', 'PENDIENTE_DE_GUARDIA', 'Presentación', 5, 8, 5),
('2025-05-22', 'PRIMERA', 'SEGUNDA', 'Médico familiar', 'GUARDIA_ASIGNADA', 'Lectura dirigida', 6, 1, 6),
('2025-05-23', 'TERCERA', 'CUARTA', 'Revisión médica', 'PENDIENTE_DE_GUARDIA', 'Práctica escrita', 7, 2, 7),
('2025-05-23', 'QUINTA', 'SEXTA', 'Asuntos personales', 'GUARDIA_ASIGNADA', 'Investigación', 8, 5, 8),
('2025-05-24', 'SEGUNDA', 'TERCERA', 'Trámite administrativo', 'PENDIENTE_DE_GUARDIA', 'Trabajo por parejas', 9, 7, 9),
('2025-05-24', 'CUARTA', 'QUINTA', 'Congreso docente', 'GUARDIA_ASIGNADA', 'Lectura individual', 10, 8, 10);