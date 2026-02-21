-- ─── Delivery Orders ───

CREATE TABLE delivery_orders
(
    id                  UUID PRIMARY KEY,
    tenant_id           UUID         NOT NULL,
    company_id          UUID         NOT NULL,
    number              VARCHAR(20)  NOT NULL,
    sales_order_id      VARCHAR(255),
    sales_order_number  VARCHAR(20),
    customer_id         VARCHAR(255),
    address_line1       VARCHAR(255),
    address_line2       VARCHAR(255),
    city                VARCHAR(100),
    state_or_province   VARCHAR(100),
    postal_code         VARCHAR(20),
    country_code        VARCHAR(2),
    status              VARCHAR(50)  NOT NULL DEFAULT 'PENDING',

    CONSTRAINT fk_delivery_order_company
        FOREIGN KEY (company_id) REFERENCES companies (id),

    CONSTRAINT uq_delivery_order_number UNIQUE (tenant_id, number)
);

CREATE INDEX idx_delivery_order_tenant_id ON delivery_orders (tenant_id);
CREATE INDEX idx_delivery_order_company_id ON delivery_orders (company_id);
CREATE INDEX idx_delivery_order_sales_order_id ON delivery_orders (sales_order_id);
CREATE INDEX idx_delivery_order_status ON delivery_orders (status);

-- ─── Delivery Order Lines ───

CREATE TABLE delivery_order_lines
(
    id                    UUID PRIMARY KEY,
    delivery_order_id     UUID           NOT NULL,
    sales_order_line_id   VARCHAR(255),
    item_id               VARCHAR(255)   NOT NULL,
    item_description      VARCHAR(500),
    unit_code             VARCHAR(50)    NOT NULL,
    ordered_qty           NUMERIC(19, 4) NOT NULL,
    shipped_qty           NUMERIC(19, 4) NOT NULL DEFAULT 0,

    CONSTRAINT fk_do_line_delivery_order
        FOREIGN KEY (delivery_order_id) REFERENCES delivery_orders (id) ON DELETE CASCADE
);

CREATE INDEX idx_do_line_delivery_order_id ON delivery_order_lines (delivery_order_id);

-- ─── Shipments ───

CREATE TABLE shipments
(
    id                UUID PRIMARY KEY,
    tenant_id         UUID         NOT NULL,
    company_id        UUID         NOT NULL,
    number            VARCHAR(20)  NOT NULL,
    delivery_order_id VARCHAR(255),
    sales_order_id    VARCHAR(255),
    warehouse_id      VARCHAR(255),
    address_line1     VARCHAR(255),
    address_line2     VARCHAR(255),
    city              VARCHAR(100),
    state_or_province VARCHAR(100),
    postal_code       VARCHAR(20),
    country_code      VARCHAR(2),
    tracking_number   VARCHAR(255),
    carrier_name      VARCHAR(255),
    status            VARCHAR(50)  NOT NULL DEFAULT 'PREPARING',
    dispatched_at     TIMESTAMP,
    delivered_at      TIMESTAMP,

    CONSTRAINT fk_shipment_company
        FOREIGN KEY (company_id) REFERENCES companies (id),

    CONSTRAINT uq_shipment_number UNIQUE (tenant_id, number)
);

CREATE INDEX idx_shipment_tenant_id ON shipments (tenant_id);
CREATE INDEX idx_shipment_company_id ON shipments (company_id);
CREATE INDEX idx_shipment_delivery_order_id ON shipments (delivery_order_id);
CREATE INDEX idx_shipment_sales_order_id ON shipments (sales_order_id);
CREATE INDEX idx_shipment_status ON shipments (status);

-- ─── Shipment Lines ───

CREATE TABLE shipment_lines
(
    id                      UUID PRIMARY KEY,
    shipment_id             UUID           NOT NULL,
    delivery_order_line_id  VARCHAR(255),
    item_id                 VARCHAR(255)   NOT NULL,
    item_description        VARCHAR(500),
    unit_code               VARCHAR(50)    NOT NULL,
    quantity                NUMERIC(19, 4) NOT NULL,

    CONSTRAINT fk_shipment_line_shipment
        FOREIGN KEY (shipment_id) REFERENCES shipments (id) ON DELETE CASCADE
);

CREATE INDEX idx_shipment_line_shipment_id ON shipment_lines (shipment_id);

-- ─── Shipment Returns ───

CREATE TABLE shipment_returns
(
    id             UUID PRIMARY KEY,
    tenant_id      UUID         NOT NULL,
    company_id     UUID         NOT NULL,
    number         VARCHAR(20)  NOT NULL,
    shipment_id    VARCHAR(255),
    sales_order_id VARCHAR(255),
    warehouse_id   VARCHAR(255),
    reason         VARCHAR(500),
    status         VARCHAR(50)  NOT NULL DEFAULT 'REQUESTED',
    received_at    TIMESTAMP,

    CONSTRAINT fk_shipment_return_company
        FOREIGN KEY (company_id) REFERENCES companies (id),

    CONSTRAINT uq_shipment_return_number UNIQUE (tenant_id, number)
);

CREATE INDEX idx_shipment_return_tenant_id ON shipment_returns (tenant_id);
CREATE INDEX idx_shipment_return_company_id ON shipment_returns (company_id);
CREATE INDEX idx_shipment_return_shipment_id ON shipment_returns (shipment_id);
CREATE INDEX idx_shipment_return_status ON shipment_returns (status);

-- ─── Shipment Return Lines ───

CREATE TABLE shipment_return_lines
(
    id                 UUID PRIMARY KEY,
    shipment_return_id UUID           NOT NULL,
    shipment_line_id   VARCHAR(255),
    item_id            VARCHAR(255)   NOT NULL,
    item_description   VARCHAR(500),
    unit_code          VARCHAR(50)    NOT NULL,
    quantity           NUMERIC(19, 4) NOT NULL,
    line_reason        VARCHAR(500),

    CONSTRAINT fk_sr_line_shipment_return
        FOREIGN KEY (shipment_return_id) REFERENCES shipment_returns (id) ON DELETE CASCADE
);

CREATE INDEX idx_sr_line_shipment_return_id ON shipment_return_lines (shipment_return_id);

-- ─── Shipment Permissions ───

INSERT INTO permissions (id, module, action, description)
VALUES
    -- DELIVERY_ORDER
    ('00000000-0000-0000-00F0-000000000001', 'DELIVERY_ORDER', 'CREATE', 'Create a delivery order'),
    ('00000000-0000-0000-00F0-000000000002', 'DELIVERY_ORDER', 'READ', 'View delivery order details'),
    ('00000000-0000-0000-00F0-000000000003', 'DELIVERY_ORDER', 'UPDATE', 'Update delivery order'),
    ('00000000-0000-0000-00F0-000000000004', 'DELIVERY_ORDER', 'CANCEL', 'Cancel a delivery order'),
    -- SHIPMENT
    ('00000000-0000-0000-00F1-000000000001', 'SHIPMENT', 'CREATE', 'Create a shipment'),
    ('00000000-0000-0000-00F1-000000000002', 'SHIPMENT', 'READ', 'View shipment details'),
    ('00000000-0000-0000-00F1-000000000003', 'SHIPMENT', 'UPDATE', 'Update shipment'),
    ('00000000-0000-0000-00F1-000000000004', 'SHIPMENT', 'DISPATCH', 'Dispatch a shipment'),
    ('00000000-0000-0000-00F1-000000000005', 'SHIPMENT', 'DELIVER', 'Mark shipment as delivered'),
    -- SHIPMENT_RETURN
    ('00000000-0000-0000-00F2-000000000001', 'SHIPMENT_RETURN', 'CREATE', 'Create a shipment return'),
    ('00000000-0000-0000-00F2-000000000002', 'SHIPMENT_RETURN', 'READ', 'View shipment return details'),
    ('00000000-0000-0000-00F2-000000000003', 'SHIPMENT_RETURN', 'RECEIVE', 'Receive a shipment return'),
    ('00000000-0000-0000-00F2-000000000004', 'SHIPMENT_RETURN', 'COMPLETE', 'Complete a shipment return'),
    ('00000000-0000-0000-00F2-000000000005', 'SHIPMENT_RETURN', 'CANCEL', 'Cancel a shipment return');

