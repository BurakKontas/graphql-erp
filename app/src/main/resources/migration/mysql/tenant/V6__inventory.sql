-- MySQL adapted version of V6__inventory.sql
-- Warehouses
CREATE TABLE warehouses (
    id VARCHAR(36) PRIMARY KEY,
    tenant_id VARCHAR(36) NOT NULL,
    company_id VARCHAR(36) NOT NULL,
    code VARCHAR(20) NOT NULL,
    name VARCHAR(200) NOT NULL,
    active TINYINT(1) NOT NULL DEFAULT 1,
    CONSTRAINT fk_warehouse_company FOREIGN KEY (company_id) REFERENCES companies (id),
    CONSTRAINT uq_warehouse_code UNIQUE (tenant_id, company_id, code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_warehouse_tenant_id ON warehouses (tenant_id);
CREATE INDEX idx_warehouse_company_id ON warehouses (company_id);

-- Categories
CREATE TABLE categories (
    id VARCHAR(36) PRIMARY KEY,
    tenant_id VARCHAR(36) NOT NULL,
    company_id VARCHAR(36) NOT NULL,
    name VARCHAR(200) NOT NULL,
    parent_category_id VARCHAR(36),
    active TINYINT(1) NOT NULL DEFAULT 1,
    CONSTRAINT fk_category_company FOREIGN KEY (company_id) REFERENCES companies (id),
    CONSTRAINT fk_category_parent FOREIGN KEY (parent_category_id) REFERENCES categories (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_category_tenant_id ON categories (tenant_id);
CREATE INDEX idx_category_company_id ON categories (company_id);
CREATE INDEX idx_category_parent_id ON categories (parent_category_id);

-- Items
CREATE TABLE items (
    id VARCHAR(36) PRIMARY KEY,
    tenant_id VARCHAR(36) NOT NULL,
    company_id VARCHAR(36) NOT NULL,
    code VARCHAR(50) NOT NULL,
    name VARCHAR(200) NOT NULL,
    type VARCHAR(50) NOT NULL,
    unit_code VARCHAR(50),
    category_id VARCHAR(36),
    allow_negative_stock TINYINT(1) NOT NULL DEFAULT 0,
    active TINYINT(1) NOT NULL DEFAULT 1,
    CONSTRAINT fk_item_company FOREIGN KEY (company_id) REFERENCES companies (id),
    CONSTRAINT fk_item_category FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT fk_item_unit FOREIGN KEY (unit_code) REFERENCES units (code),
    CONSTRAINT uq_item_code UNIQUE (tenant_id, company_id, code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_item_tenant_id ON items (tenant_id);
CREATE INDEX idx_item_company_id ON items (company_id);
CREATE INDEX idx_item_category_id ON items (category_id);
CREATE INDEX idx_item_type ON items (type);

-- Stock Levels
CREATE TABLE stock_levels (
    id VARCHAR(36) PRIMARY KEY,
    tenant_id VARCHAR(36) NOT NULL,
    company_id VARCHAR(36) NOT NULL,
    item_id VARCHAR(36) NOT NULL,
    warehouse_id VARCHAR(36) NOT NULL,
    quantity_on_hand DECIMAL(19,4) NOT NULL DEFAULT 0,
    quantity_reserved DECIMAL(19,4) NOT NULL DEFAULT 0,
    reorder_point DECIMAL(19,4),
    allow_negative_stock TINYINT(1) NOT NULL DEFAULT 0,
    CONSTRAINT fk_stock_level_company FOREIGN KEY (company_id) REFERENCES companies (id),
    CONSTRAINT fk_stock_level_item FOREIGN KEY (item_id) REFERENCES items (id),
    CONSTRAINT fk_stock_level_warehouse FOREIGN KEY (warehouse_id) REFERENCES warehouses (id),
    CONSTRAINT uq_stock_level_item_warehouse UNIQUE (tenant_id, item_id, warehouse_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_stock_level_tenant_id ON stock_levels (tenant_id);
CREATE INDEX idx_stock_level_item_id ON stock_levels (item_id);
CREATE INDEX idx_stock_level_warehouse_id ON stock_levels (warehouse_id);

-- Stock Movements
CREATE TABLE stock_movements (
    id VARCHAR(36) PRIMARY KEY,
    tenant_id VARCHAR(36) NOT NULL,
    company_id VARCHAR(36) NOT NULL,
    item_id VARCHAR(36) NOT NULL,
    warehouse_id VARCHAR(36) NOT NULL,
    movement_type VARCHAR(50) NOT NULL,
    quantity DECIMAL(19,4) NOT NULL,
    reference_type VARCHAR(50) NOT NULL,
    reference_id VARCHAR(255),
    note VARCHAR(1000),
    movement_date DATE NOT NULL,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_stock_movement_company FOREIGN KEY (company_id) REFERENCES companies (id),
    CONSTRAINT fk_stock_movement_item FOREIGN KEY (item_id) REFERENCES items (id),
    CONSTRAINT fk_stock_movement_warehouse FOREIGN KEY (warehouse_id) REFERENCES warehouses (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_stock_movement_tenant_id ON stock_movements (tenant_id);
CREATE INDEX idx_stock_movement_item_id ON stock_movements (item_id);
CREATE INDEX idx_stock_movement_warehouse_id ON stock_movements (warehouse_id);
CREATE INDEX idx_stock_movement_date ON stock_movements (movement_date);
CREATE INDEX idx_stock_movement_type ON stock_movements (movement_type);

-- Permissions inserts remain unchanged

