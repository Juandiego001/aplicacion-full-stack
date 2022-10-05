-- Database creation
CREATE DATABASE app_db;

USE app_db;

-- Table users creation
CREATE TABLE USUARIO(
    cedula INTEGER PRIMARY KEY,
    contrasena VARCHAR(100),
    nombre VARCHAR(100),
    apellido VARCHAR(100),
    telefono VARCHAR(20)
);

-- Insert test users
INSERT INTO USUARIO VALUES(123, 'juan123', 'Juan', 'C.', '3101234446');
INSERT INTO USUARIO VALUES(124, 'juan124', 'Diego', 'C.', '3111245556');