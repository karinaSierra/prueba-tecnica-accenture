-- ============================
-- Tabla Franquicia
-- ============================
CREATE TABLE IF NOT EXISTS franquicia (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_franquicia_nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================
-- Tabla Sucursal
-- ============================
CREATE TABLE IF NOT EXISTS sucursal (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    franquicia_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_sucursal_franquicia
      FOREIGN KEY (franquicia_id)
      REFERENCES franquicia(id)
      ON DELETE CASCADE,
    UNIQUE KEY uk_sucursal_franquicia_nombre (franquicia_id, nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================
-- Tabla Producto
-- ============================
CREATE TABLE IF NOT EXISTS producto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    sucursal_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_producto_sucursal
      FOREIGN KEY (sucursal_id)
      REFERENCES sucursal(id)
      ON DELETE CASCADE,
    UNIQUE KEY uk_producto_sucursal_nombre (sucursal_id, nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================
-- Índices
-- ============================
CREATE INDEX idx_sucursal_franquicia_id ON sucursal(franquicia_id);
CREATE INDEX idx_producto_sucursal_id ON producto(sucursal_id);
CREATE INDEX idx_producto_stock ON producto(stock);
