CREATE DATABASE IF NOT EXISTS aulas_ufg
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE aulas_ufg;

CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT NOT NULL AUTO_INCREMENT,
    correo VARCHAR(254) NOT NULL,
    nombre VARCHAR(120) NOT NULL,
    clave_hash VARCHAR(160) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_usuarios_correo UNIQUE (correo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Cuenta de demostración. El valor incluye el algoritmo, las iteraciones, la sal y el hash.
INSERT INTO usuarios (correo, nombre, clave_hash)
VALUES ('demo@aulas.test', 'Usuario de prueba', 'pbkdf2_sha256$600000$ab1f6c37ab0658829e0602e48900ab62$fee243916a7e64632373f81c4c8e4fe920b163c47b9669a738a69e4bc2e35d88')
ON DUPLICATE KEY UPDATE correo = VALUES(correo);
