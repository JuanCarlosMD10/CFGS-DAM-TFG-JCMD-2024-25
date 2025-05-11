-- Crear usuario
CREATE USER 'juancarlos'@'localhost' IDENTIFIED BY 'juancarlos';
-- Dar privilegios específicos
GRANT ALL PRIVILEGES ON proyectotransversaljcmd.* TO 'juancarlos'@'localhost';
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
    estado ENUM('PENDIENTE DE GUARDIA', 'GUARDIA ASIGNADA') NOT NULL,
    tarea_alumnado VARCHAR(255),
    id_guardia BIGINT NOT NULL,
    id_profesor BIGINT NOT NULL,
    id_zona BIGINT NOT NULL,
    CONSTRAINT fk_ausencia_guardia FOREIGN KEY (id_guardia) REFERENCES guardia(id_guardia),
    CONSTRAINT fk_ausencia_usuario FOREIGN KEY (id_profesor) REFERENCES usuario(id_usuario),
    CONSTRAINT fk_ausencia_zona FOREIGN KEY (id_zona) REFERENCES zona(id_zona)
);