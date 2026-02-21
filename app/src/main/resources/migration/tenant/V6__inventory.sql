-- ─── Warehouses ───

CREATE TABLE warehouses
(
    id         UUID PRIMARY KEY,
    tenant_id  UUID        NOT NULL,
    company_id UUID        NOT NULL,
    code       VARCHAR(20) NOT NULL,
    name       VARCHAR(200) NOT NULL,
    active     BOOLEAN     NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_warehouse_company
        FOREIGN KEY (company_id) REFERENCES companies (id),

    CONSTRAINT uq_warehouse_code UNIQUE (tenant_id, company_id, code)
);

CREATE INDEX idx_warehouse_tenant_id ON warehouses (tenant_id);
CREATE INDEX idx_warehouse_company_id ON warehouses (company_id);

-- ─── Categories ───

CREATE TABLE categories
(
    id                 UUID PRIMARY KEY,
    tenant_id          UUID         NOT NULL,
    company_id         UUID         NOT NULL,
    name               VARCHAR(200) NOT NULL,
    parent_category_id UUID,
    active             BOOLEAN      NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_category_company
        FOREIGN KEY (company_id) REFERENCES companies (id),

    CONSTRAINT fk_category_parent
        FOREIGN KEY (parent_category_id) REFERENCES categories (id)
);

CREATE INDEX idx_category_tenant_id ON categories (tenant_id);
CREATE INDEX idx_category_company_id ON categories (company_id);
CREATE INDEX idx_category_parent_id ON categories (parent_category_id);

-- ─── Items ───

CREATE TABLE items
(
    id                   UUID PRIMARY KEY,
    tenant_id            UUID         NOT NULL,
    company_id           UUID         NOT NULL,
    code                 VARCHAR(50)  NOT NULL,
    name                 VARCHAR(200) NOT NULL,
    type                 VARCHAR(50)  NOT NULL,
    unit_code            VARCHAR(50),
    category_id          UUID,
    allow_negative_stock BOOLEAN      NOT NULL DEFAULT FALSE,
    active               BOOLEAN      NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_item_company
        FOREIGN KEY (company_id) REFERENCES companies (id),

    CONSTRAINT fk_item_category
        FOREIGN KEY (category_id) REFERENCES categories (id),

    CONSTRAINT fk_item_unit
        FOREIGN KEY (unit_code) REFERENCES units (code),

    CONSTRAINT uq_item_code UNIQUE (tenant_id, company_id, code)
);

CREATE INDEX idx_item_tenant_id ON items (tenant_id);
CREATE INDEX idx_item_company_id ON items (company_id);
CREATE INDEX idx_item_category_id ON items (category_id);
CREATE INDEX idx_item_type ON items (type);

-- ─── Stock Levels ───

CREATE TABLE stock_levels
(
    id                   UUID PRIMARY KEY,
    tenant_id            UUID           NOT NULL,
    company_id           UUID           NOT NULL,
    item_id              UUID           NOT NULL,
    warehouse_id         UUID           NOT NULL,
    quantity_on_hand     NUMERIC(19, 4) NOT NULL DEFAULT 0,
    quantity_reserved    NUMERIC(19, 4) NOT NULL DEFAULT 0,
    reorder_point        NUMERIC(19, 4),
    allow_negative_stock BOOLEAN        NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_stock_level_company
        FOREIGN KEY (company_id) REFERENCES companies (id),

    CONSTRAINT fk_stock_level_item
        FOREIGN KEY (item_id) REFERENCES items (id),

    CONSTRAINT fk_stock_level_warehouse
        FOREIGN KEY (warehouse_id) REFERENCES warehouses (id),

    CONSTRAINT uq_stock_level_item_warehouse UNIQUE (tenant_id, item_id, warehouse_id)
);

CREATE INDEX idx_stock_level_tenant_id ON stock_levels (tenant_id);
CREATE INDEX idx_stock_level_item_id ON stock_levels (item_id);
CREATE INDEX idx_stock_level_warehouse_id ON stock_levels (warehouse_id);

-- ─── Stock Movements ───

CREATE TABLE stock_movements
(
    id             UUID PRIMARY KEY,
    tenant_id      UUID           NOT NULL,
    company_id     UUID           NOT NULL,
    item_id        UUID           NOT NULL,
    warehouse_id   UUID           NOT NULL,
    movement_type  VARCHAR(50)    NOT NULL,
    quantity       NUMERIC(19, 4) NOT NULL,
    reference_type VARCHAR(50)    NOT NULL,
    reference_id   VARCHAR(255),
    note           VARCHAR(1000),
    movement_date  DATE           NOT NULL,
    created_at     TIMESTAMP      NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_stock_movement_company
        FOREIGN KEY (company_id) REFERENCES companies (id),

    CONSTRAINT fk_stock_movement_item
        FOREIGN KEY (item_id) REFERENCES items (id),

    CONSTRAINT fk_stock_movement_warehouse
        FOREIGN KEY (warehouse_id) REFERENCES warehouses (id)
);

CREATE INDEX idx_stock_movement_tenant_id ON stock_movements (tenant_id);
CREATE INDEX idx_stock_movement_item_id ON stock_movements (item_id);
CREATE INDEX idx_stock_movement_warehouse_id ON stock_movements (warehouse_id);
CREATE INDEX idx_stock_movement_date ON stock_movements (movement_date);
CREATE INDEX idx_stock_movement_type ON stock_movements (movement_type);

-- ─── Inventory Permissions ───

INSERT INTO permissions (id, module, action, description)
VALUES
    -- WAREHOUSE
    ('00000000-0000-0000-000A-000000000001', 'WAREHOUSE', 'CREATE', 'Create a new warehouse'),
    ('00000000-0000-0000-000A-000000000002', 'WAREHOUSE', 'READ', 'View warehouse details'),
    ('00000000-0000-0000-000A-000000000003', 'WAREHOUSE', 'UPDATE', 'Update warehouse information'),
    ('00000000-0000-0000-000A-000000000004', 'WAREHOUSE', 'DELETE', 'Delete a warehouse'),
    -- CATEGORY
    ('00000000-0000-0000-000B-000000000001', 'CATEGORY', 'CREATE', 'Create a new category'),
    ('00000000-0000-0000-000B-000000000002', 'CATEGORY', 'READ', 'View category details'),
    ('00000000-0000-0000-000B-000000000003', 'CATEGORY', 'UPDATE', 'Update category information'),
    ('00000000-0000-0000-000B-000000000004', 'CATEGORY', 'DELETE', 'Delete a category'),
    -- ITEM
    ('00000000-0000-0000-000C-000000000001', 'ITEM', 'CREATE', 'Create a new item'),
    ('00000000-0000-0000-000C-000000000002', 'ITEM', 'READ', 'View item details'),
    ('00000000-0000-0000-000C-000000000003', 'ITEM', 'UPDATE', 'Update item information'),
    ('00000000-0000-0000-000C-000000000004', 'ITEM', 'DELETE', 'Delete an item'),
    -- STOCK_LEVEL
    ('00000000-0000-0000-000D-000000000001', 'STOCK_LEVEL', 'CREATE', 'Create a stock level entry'),
    ('00000000-0000-0000-000D-000000000002', 'STOCK_LEVEL', 'READ', 'View stock level details'),
    ('00000000-0000-0000-000D-000000000003', 'STOCK_LEVEL', 'UPDATE', 'Update stock level'),
    ('00000000-0000-0000-000D-000000000004', 'STOCK_LEVEL', 'DELETE', 'Delete a stock level entry'),
    -- STOCK_MOVEMENT
    ('00000000-0000-0000-000E-000000000001', 'STOCK_MOVEMENT', 'CREATE', 'Record a stock movement'),
    ('00000000-0000-0000-000E-000000000002', 'STOCK_MOVEMENT', 'READ', 'View stock movement details'),
    ('00000000-0000-0000-000E-000000000003', 'STOCK_MOVEMENT', 'UPDATE', 'Update stock movement'),
    ('00000000-0000-0000-000E-000000000004', 'STOCK_MOVEMENT', 'DELETE', 'Delete a stock movement');
