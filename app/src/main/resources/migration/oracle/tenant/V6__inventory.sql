-- V6__inventory.sql (Main Application)
-- Oracle conversion:
--   UUID    -> VARCHAR2(32)
--   BOOLEAN -> NUMBER(1,0)
--   NUMERIC -> NUMBER
--   NOW()   -> SYSTIMESTAMP

-- ─── Warehouses ───
CREATE TABLE "${schema}".warehouses (
    id         VARCHAR2(32)       NOT NULL,
    tenant_id  VARCHAR2(32)       NOT NULL,
    company_id VARCHAR2(32)       NOT NULL,
    code       VARCHAR2(20)  NOT NULL,
    name       VARCHAR2(200) NOT NULL,
    active     NUMBER(1,0)   DEFAULT 1 NOT NULL,
    CONSTRAINT pk_warehouses PRIMARY KEY (id),
    CONSTRAINT fk_warehouse_company FOREIGN KEY (company_id) REFERENCES "${schema}".companies (id),
    CONSTRAINT uq_warehouse_code    UNIQUE (tenant_id, company_id, code)
);

CREATE INDEX idx_warehouse_tenant_id  ON "${schema}".warehouses (tenant_id);
CREATE INDEX idx_warehouse_company_id ON "${schema}".warehouses (company_id);

-- ─── Categories ───
CREATE TABLE "${schema}".categories (
    id                 VARCHAR2(32)       NOT NULL,
    tenant_id          VARCHAR2(32)       NOT NULL,
    company_id         VARCHAR2(32)       NOT NULL,
    name               VARCHAR2(200) NOT NULL,
    parent_category_id VARCHAR2(32),
    active             NUMBER(1,0)   DEFAULT 1 NOT NULL,
    CONSTRAINT pk_categories PRIMARY KEY (id),
    CONSTRAINT fk_category_company FOREIGN KEY (company_id)         REFERENCES "${schema}".companies (id),
    CONSTRAINT fk_category_parent  FOREIGN KEY (parent_category_id) REFERENCES "${schema}".categories (id)
);

CREATE INDEX idx_category_tenant_id  ON "${schema}".categories (tenant_id);
CREATE INDEX idx_category_company_id ON "${schema}".categories (company_id);
CREATE INDEX idx_category_parent_id  ON "${schema}".categories (parent_category_id);

-- ─── Items ───
CREATE TABLE "${schema}".items (
    id                   VARCHAR2(32)       NOT NULL,
    tenant_id            VARCHAR2(32)       NOT NULL,
    company_id           VARCHAR2(32)       NOT NULL,
    code                 VARCHAR2(50)  NOT NULL,
    name                 VARCHAR2(200) NOT NULL,
    type                 VARCHAR2(50)  NOT NULL,
    unit_code            VARCHAR2(50),
    category_id          VARCHAR2(32),
    allow_negative_stock NUMBER(1,0)   DEFAULT 0 NOT NULL,
    active               NUMBER(1,0)   DEFAULT 1 NOT NULL,
    CONSTRAINT pk_items PRIMARY KEY (id),
    CONSTRAINT fk_item_company  FOREIGN KEY (company_id)  REFERENCES "${schema}".companies (id),
    CONSTRAINT fk_item_category FOREIGN KEY (category_id) REFERENCES "${schema}".categories (id),
    CONSTRAINT fk_item_unit     FOREIGN KEY (unit_code)   REFERENCES "${schema}".units (code),
    CONSTRAINT uq_item_code     UNIQUE (tenant_id, company_id, code)
);

CREATE INDEX idx_item_tenant_id  ON "${schema}".items (tenant_id);
CREATE INDEX idx_item_company_id ON "${schema}".items (company_id);
CREATE INDEX idx_item_category_id ON "${schema}".items (category_id);
CREATE INDEX idx_item_type       ON "${schema}".items (type);

-- ─── Stock Levels ───
CREATE TABLE "${schema}".stock_levels (
    id                   VARCHAR2(32)      NOT NULL,
    tenant_id            VARCHAR2(32)      NOT NULL,
    company_id           VARCHAR2(32)      NOT NULL,
    item_id              VARCHAR2(32)      NOT NULL,
    warehouse_id         VARCHAR2(32)      NOT NULL,
    quantity_on_hand     NUMBER(19,4) DEFAULT 0 NOT NULL,
    quantity_reserved    NUMBER(19,4) DEFAULT 0 NOT NULL,
    reorder_point        NUMBER(19,4),
    allow_negative_stock NUMBER(1,0)  DEFAULT 0 NOT NULL,
    CONSTRAINT pk_stock_levels PRIMARY KEY (id),
    CONSTRAINT fk_sl_company   FOREIGN KEY (company_id)  REFERENCES "${schema}".companies (id),
    CONSTRAINT fk_sl_item      FOREIGN KEY (item_id)     REFERENCES "${schema}".items (id),
    CONSTRAINT fk_sl_warehouse FOREIGN KEY (warehouse_id) REFERENCES "${schema}".warehouses (id),
    CONSTRAINT uq_sl_item_warehouse UNIQUE (tenant_id, item_id, warehouse_id)
);

CREATE INDEX idx_stock_level_tenant_id    ON "${schema}".stock_levels (tenant_id);
CREATE INDEX idx_stock_level_item_id      ON "${schema}".stock_levels (item_id);
CREATE INDEX idx_stock_level_warehouse_id ON "${schema}".stock_levels (warehouse_id);

-- ─── Stock Movements ───
CREATE TABLE "${schema}".stock_movements (
    id             VARCHAR2(32)        NOT NULL,
    tenant_id      VARCHAR2(32)        NOT NULL,
    company_id     VARCHAR2(32)        NOT NULL,
    item_id        VARCHAR2(32)        NOT NULL,
    warehouse_id   VARCHAR2(32)        NOT NULL,
    movement_type  VARCHAR2(50)   NOT NULL,
    quantity       NUMBER(19,4)   NOT NULL,
    reference_type VARCHAR2(50)   NOT NULL,
    reference_id   VARCHAR2(255),
    note           VARCHAR2(1000),
    movement_date  DATE           NOT NULL,
    created_at     TIMESTAMP      DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT pk_stock_movements PRIMARY KEY (id),
    CONSTRAINT fk_sm_company   FOREIGN KEY (company_id)   REFERENCES "${schema}".companies (id),
    CONSTRAINT fk_sm_item      FOREIGN KEY (item_id)      REFERENCES "${schema}".items (id),
    CONSTRAINT fk_sm_warehouse FOREIGN KEY (warehouse_id) REFERENCES "${schema}".warehouses (id)
);

CREATE INDEX idx_stock_movement_tenant_id    ON "${schema}".stock_movements (tenant_id);
CREATE INDEX idx_stock_movement_item_id      ON "${schema}".stock_movements (item_id);
CREATE INDEX idx_stock_movement_warehouse_id ON "${schema}".stock_movements (warehouse_id);
CREATE INDEX idx_stock_movement_date         ON "${schema}".stock_movements (movement_date);
CREATE INDEX idx_stock_movement_type         ON "${schema}".stock_movements (movement_type);

-- ─── Inventory Permissions ───
-- WAREHOUSE
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('0000000000000000000A000000000001'), 'WAREHOUSE', 'CREATE', 'Create a new warehouse');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('0000000000000000000A000000000002'), 'WAREHOUSE', 'READ',   'View warehouse details');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('0000000000000000000A000000000003'), 'WAREHOUSE', 'UPDATE', 'Update warehouse information');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('0000000000000000000A000000000004'), 'WAREHOUSE', 'DELETE', 'Delete a warehouse');
-- CATEGORY
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('0000000000000000000B000000000001'), 'CATEGORY', 'CREATE', 'Create a new category');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('0000000000000000000B000000000002'), 'CATEGORY', 'READ',   'View category details');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('0000000000000000000B000000000003'), 'CATEGORY', 'UPDATE', 'Update category information');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('0000000000000000000B000000000004'), 'CATEGORY', 'DELETE', 'Delete a category');
-- ITEM
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('0000000000000000000C000000000001'), 'ITEM', 'CREATE', 'Create a new item');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('0000000000000000000C000000000002'), 'ITEM', 'READ',   'View item details');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('0000000000000000000C000000000003'), 'ITEM', 'UPDATE', 'Update item information');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('0000000000000000000C000000000004'), 'ITEM', 'DELETE', 'Delete an item');
-- STOCK_LEVEL
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('0000000000000000000D000000000001'), 'STOCK_LEVEL', 'CREATE', 'Create a stock level entry');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('0000000000000000000D000000000002'), 'STOCK_LEVEL', 'READ',   'View stock level details');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('0000000000000000000D000000000003'), 'STOCK_LEVEL', 'UPDATE', 'Update stock level');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('0000000000000000000D000000000004'), 'STOCK_LEVEL', 'DELETE', 'Delete a stock level entry');
-- STOCK_MOVEMENT
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('0000000000000000000E000000000001'), 'STOCK_MOVEMENT', 'CREATE', 'Record a stock movement');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('0000000000000000000E000000000002'), 'STOCK_MOVEMENT', 'READ',   'View stock movement details');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('0000000000000000000E000000000003'), 'STOCK_MOVEMENT', 'UPDATE', 'Update stock movement');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('0000000000000000000E000000000004'), 'STOCK_MOVEMENT', 'DELETE', 'Delete a stock movement');

COMMIT;
