-- V8__shipment.sql (Main Application)
-- Oracle conversion:
--   UUID    -> VARCHAR2(32)
--   BOOLEAN -> NUMBER(1,0)
--   VARCHAR -> VARCHAR2
--   TIMESTAMP -> TIMESTAMP

-- ─── Delivery Orders ───
CREATE TABLE "${schema}".delivery_orders (
    id                 VARCHAR2(32)       NOT NULL,
    tenant_id          VARCHAR2(32)       NOT NULL,
    company_id         VARCHAR2(32)       NOT NULL,
    delivery_number    VARCHAR2(20)  NOT NULL,
    sales_order_id     VARCHAR2(255),
    sales_order_number VARCHAR2(20),
    customer_id        VARCHAR2(255),
    address_line1      VARCHAR2(255),
    address_line2      VARCHAR2(255),
    city               VARCHAR2(100),
    state_or_province  VARCHAR2(100),
    postal_code        VARCHAR2(20),
    country_code       VARCHAR2(2),
    status             VARCHAR2(50)  DEFAULT 'PENDING' NOT NULL,
    CONSTRAINT pk_delivery_orders PRIMARY KEY (id),
    CONSTRAINT fk_do_company FOREIGN KEY (company_id) REFERENCES "${schema}".companies (id),
    CONSTRAINT uq_do_number  UNIQUE (tenant_id, delivery_number)
);

CREATE INDEX idx_delivery_order_tenant_id      ON "${schema}".delivery_orders (tenant_id);
CREATE INDEX idx_delivery_order_company_id     ON "${schema}".delivery_orders (company_id);
CREATE INDEX idx_delivery_order_sales_order_id ON "${schema}".delivery_orders (sales_order_id);
CREATE INDEX idx_delivery_order_status         ON "${schema}".delivery_orders (status);

-- ─── Delivery Order Lines ───
CREATE TABLE "${schema}".delivery_order_lines (
    id                  VARCHAR2(32)        NOT NULL,
    delivery_order_id   VARCHAR2(32)        NOT NULL,
    sales_order_line_id VARCHAR2(255),
    item_id             VARCHAR2(255)  NOT NULL,
    item_description    VARCHAR2(500),
    unit_code           VARCHAR2(50)   NOT NULL,
    ordered_qty         NUMBER(19,4)   NOT NULL,
    shipped_qty         NUMBER(19,4)   DEFAULT 0 NOT NULL,
    CONSTRAINT pk_delivery_order_lines PRIMARY KEY (id),
    CONSTRAINT fk_dol_delivery_order FOREIGN KEY (delivery_order_id)
        REFERENCES "${schema}".delivery_orders (id) ON DELETE CASCADE
);

CREATE INDEX idx_do_line_delivery_order_id ON "${schema}".delivery_order_lines (delivery_order_id);

-- ─── Shipments ───
CREATE TABLE "${schema}".shipments (
    id                VARCHAR2(32)       NOT NULL,
    tenant_id         VARCHAR2(32)       NOT NULL,
    company_id        VARCHAR2(32)       NOT NULL,
    shipment_number   VARCHAR2(20)  NOT NULL,
    delivery_order_id VARCHAR2(255),
    sales_order_id    VARCHAR2(255),
    warehouse_id      VARCHAR2(255),
    address_line1     VARCHAR2(255),
    address_line2     VARCHAR2(255),
    city              VARCHAR2(100),
    state_or_province VARCHAR2(100),
    postal_code       VARCHAR2(20),
    country_code      VARCHAR2(2),
    tracking_number   VARCHAR2(255),
    carrier_name      VARCHAR2(255),
    status            VARCHAR2(50)  DEFAULT 'PREPARING' NOT NULL,
    dispatched_at     TIMESTAMP,
    delivered_at      TIMESTAMP,
    CONSTRAINT pk_shipments PRIMARY KEY (id),
    CONSTRAINT fk_shipment_company FOREIGN KEY (company_id) REFERENCES "${schema}".companies (id),
    CONSTRAINT uq_shipment_number  UNIQUE (tenant_id, shipment_number)
);

CREATE INDEX idx_shipment_tenant_id          ON "${schema}".shipments (tenant_id);
CREATE INDEX idx_shipment_company_id         ON "${schema}".shipments (company_id);
CREATE INDEX idx_shipment_delivery_order_id  ON "${schema}".shipments (delivery_order_id);
CREATE INDEX idx_shipment_sales_order_id     ON "${schema}".shipments (sales_order_id);
CREATE INDEX idx_shipment_status             ON "${schema}".shipments (status);

-- ─── Shipment Lines ───
CREATE TABLE "${schema}".shipment_lines (
    id                     VARCHAR2(32)        NOT NULL,
    shipment_id            VARCHAR2(32)        NOT NULL,
    delivery_order_line_id VARCHAR2(255),
    item_id                VARCHAR2(255)  NOT NULL,
    item_description       VARCHAR2(500),
    unit_code              VARCHAR2(50)   NOT NULL,
    quantity               NUMBER(19,4)   NOT NULL,
    CONSTRAINT pk_shipment_lines PRIMARY KEY (id),
    CONSTRAINT fk_sl_shipment FOREIGN KEY (shipment_id)
        REFERENCES "${schema}".shipments (id) ON DELETE CASCADE
);

CREATE INDEX idx_shipment_line_shipment_id ON "${schema}".shipment_lines (shipment_id);

-- ─── Shipment Returns ───
CREATE TABLE "${schema}".shipment_returns (
    id             VARCHAR2(32)       NOT NULL,
    tenant_id      VARCHAR2(32)       NOT NULL,
    company_id     VARCHAR2(32)       NOT NULL,
    return_number  VARCHAR2(20)  NOT NULL,
    shipment_id    VARCHAR2(255),
    sales_order_id VARCHAR2(255),
    warehouse_id   VARCHAR2(255),
    reason         VARCHAR2(500),
    status         VARCHAR2(50)  DEFAULT 'REQUESTED' NOT NULL,
    received_at    TIMESTAMP,
    CONSTRAINT pk_shipment_returns PRIMARY KEY (id),
    CONSTRAINT fk_sr_company FOREIGN KEY (company_id) REFERENCES "${schema}".companies (id),
    CONSTRAINT uq_sr_number  UNIQUE (tenant_id, return_number)
);

CREATE INDEX idx_shipment_return_tenant_id   ON "${schema}".shipment_returns (tenant_id);
CREATE INDEX idx_shipment_return_company_id  ON "${schema}".shipment_returns (company_id);
CREATE INDEX idx_shipment_return_shipment_id ON "${schema}".shipment_returns (shipment_id);
CREATE INDEX idx_shipment_return_status      ON "${schema}".shipment_returns (status);

-- ─── Shipment Return Lines ───
CREATE TABLE "${schema}".shipment_return_lines (
    id                 VARCHAR2(32)        NOT NULL,
    shipment_return_id VARCHAR2(32)        NOT NULL,
    shipment_line_id   VARCHAR2(255),
    item_id            VARCHAR2(255)  NOT NULL,
    item_description   VARCHAR2(500),
    unit_code          VARCHAR2(50)   NOT NULL,
    quantity           NUMBER(19,4)   NOT NULL,
    line_reason        VARCHAR2(500),
    CONSTRAINT pk_shipment_return_lines PRIMARY KEY (id),
    CONSTRAINT fk_srl_return FOREIGN KEY (shipment_return_id)
        REFERENCES "${schema}".shipment_returns (id) ON DELETE CASCADE
);

CREATE INDEX idx_sr_line_shipment_return_id ON "${schema}".shipment_return_lines (shipment_return_id);

-- ─── Shipment Permissions ───
-- DELIVERY_ORDER
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('00000000000000000FF0000000000001'), 'DELIVERY_ORDER', 'CREATE', 'Create a delivery order');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('00000000000000000FF0000000000002'), 'DELIVERY_ORDER', 'READ',   'View delivery order details');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('00000000000000000FF0000000000003'), 'DELIVERY_ORDER', 'UPDATE', 'Update delivery order');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('00000000000000000FF0000000000004'), 'DELIVERY_ORDER', 'CANCEL', 'Cancel a delivery order');
-- SHIPMENT
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('00000000000000000FF1000000000001'), 'SHIPMENT', 'CREATE',   'Create a shipment');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('00000000000000000FF1000000000002'), 'SHIPMENT', 'READ',     'View shipment details');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('00000000000000000FF1000000000003'), 'SHIPMENT', 'UPDATE',   'Update shipment');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('00000000000000000FF1000000000004'), 'SHIPMENT', 'DISPATCH', 'Dispatch a shipment');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('00000000000000000FF1000000000005'), 'SHIPMENT', 'DELIVER',  'Mark shipment as delivered');
-- SHIPMENT_RETURN
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('00000000000000000FF2000000000001'), 'SHIPMENT_RETURN', 'CREATE',   'Create a shipment return');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('00000000000000000FF2000000000002'), 'SHIPMENT_RETURN', 'READ',     'View shipment return details');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('00000000000000000FF2000000000003'), 'SHIPMENT_RETURN', 'RECEIVE',  'Receive a shipment return');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('00000000000000000FF2000000000004'), 'SHIPMENT_RETURN', 'COMPLETE', 'Complete a shipment return');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('00000000000000000FF2000000000005'), 'SHIPMENT_RETURN', 'CANCEL',   'Cancel a shipment return');

COMMIT;
