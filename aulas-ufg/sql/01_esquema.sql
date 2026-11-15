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

CREATE TABLE IF NOT EXISTS plantas (
    numero INT NOT NULL,
    PRIMARY KEY (numero),
    CONSTRAINT ck_plantas_numero CHECK (numero > 0)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS tipos_aula (
    id INT NOT NULL,
    nombre VARCHAR(80) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_tipos_nombre UNIQUE (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS estados_aula (
    id INT NOT NULL,
    nombre VARCHAR(80) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_estados_nombre UNIQUE (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS aulas (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(80) NOT NULL,
    edificio VARCHAR(100) NOT NULL,
    planta INT NOT NULL,
    capacidad INT NOT NULL,
    tipo_aula_id INT NOT NULL,
    responsable VARCHAR(120) NOT NULL,
    estado_aula_id INT NOT NULL,
    registrada_por BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_aulas_edificio_nombre UNIQUE (edificio, nombre),
    CONSTRAINT ck_aulas_planta CHECK (planta > 0),
    CONSTRAINT ck_aulas_capacidad CHECK (capacidad > 0),
    CONSTRAINT fk_aulas_planta FOREIGN KEY (planta) REFERENCES plantas (numero),
    CONSTRAINT fk_aulas_tipo FOREIGN KEY (tipo_aula_id) REFERENCES tipos_aula (id),
    CONSTRAINT fk_aulas_estado FOREIGN KEY (estado_aula_id) REFERENCES estados_aula (id),
    CONSTRAINT fk_aulas_usuario FOREIGN KEY (registrada_por) REFERENCES usuarios (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO plantas (numero) VALUES (1), (2), (3), (4), (5)
ON DUPLICATE KEY UPDATE numero = VALUES(numero);

INSERT INTO tipos_aula (id, nombre) VALUES
    (1, 'Normal'), (2, 'Centro de cómputo'), (3, 'Laboratorio'), (4, 'Especial')
ON DUPLICATE KEY UPDATE nombre = tipos_aula.nombre;

INSERT INTO estados_aula (id, nombre) VALUES
    (1, 'Asignada'), (2, 'Asignada temporalmente'), (3, 'En mantenimiento'), (4, 'Disponible')
ON DUPLICATE KEY UPDATE nombre = estados_aula.nombre;
