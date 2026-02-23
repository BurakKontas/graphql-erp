-- V4__sales_order.sql (Main Application)
-- Oracle conversion:
--   UUID    -> VARCHAR2(32)
--   NUMERIC -> NUMBER
--   VARCHAR -> VARCHAR2
--   DEFAULT 'DRAFT' kept as-is

CREATE TABLE "${schema}".sales_orders (
    id                         VARCHAR2(32)        NOT NULL,
    tenant_id                  VARCHAR2(32)        NOT NULL,
    company_id                 VARCHAR2(32)        NOT NULL,
    order_number               VARCHAR2(20)   NOT NULL,
    order_date                 DATE           NOT NULL,
    expiry_date                DATE,
    customer_id                VARCHAR2(32),
    currency_code              VARCHAR2(3),
    payment_term_code          VARCHAR2(50),
    shipping_address_line1     VARCHAR2(500),
    shipping_address_line2     VARCHAR2(500),
    shipping_city              VARCHAR2(255),
    shipping_state_or_province VARCHAR2(255),
    shipping_postal_code       VARCHAR2(50),
    shipping_country_code      VARCHAR2(2),
    status                     VARCHAR2(50)   DEFAULT 'DRAFT' NOT NULL,
    fulfillment_status         VARCHAR2(50)   DEFAULT 'NOT_STARTED' NOT NULL,
    invoiced_amount            NUMBER(19,2)   DEFAULT 0 NOT NULL,
    subtotal                   NUMBER(19,2)   DEFAULT 0 NOT NULL,
    tax_total                  NUMBER(19,2)   DEFAULT 0 NOT NULL,
    total                      NUMBER(19,2)   DEFAULT 0 NOT NULL,
    CONSTRAINT pk_sales_orders PRIMARY KEY (id),
    CONSTRAINT fk_so_company      FOREIGN KEY (company_id)        REFERENCES "${schema}".companies (id),
    CONSTRAINT fk_so_customer     FOREIGN KEY (customer_id)       REFERENCES "${schema}".business_partner (id),
    CONSTRAINT fk_so_currency     FOREIGN KEY (currency_code)     REFERENCES "${schema}".currencies (code),
    CONSTRAINT fk_so_payment_term FOREIGN KEY (payment_term_code) REFERENCES "${schema}".payment_terms (code),
    CONSTRAINT uq_so_number       UNIQUE (tenant_id, order_number)
);

CREATE INDEX idx_sales_order_tenant_id   ON "${schema}".sales_orders (tenant_id);
CREATE INDEX idx_sales_order_company_id  ON "${schema}".sales_orders (company_id);
CREATE INDEX idx_sales_order_customer_id ON "${schema}".sales_orders (customer_id);
CREATE INDEX idx_sales_order_status      ON "${schema}".sales_orders (status);
CREATE INDEX idx_sales_order_order_date  ON "${schema}".sales_orders (order_date);

-- ─── Sales Order Lines ───
CREATE TABLE "${schema}".sales_order_lines (
    id                  VARCHAR2(32)        NOT NULL,
    order_id            VARCHAR2(32)        NOT NULL,
    sequence            NUMBER(10)     NOT NULL,
    item_id             VARCHAR2(255),
    item_description    VARCHAR2(500)  NOT NULL,
    unit_code           VARCHAR2(50)   NOT NULL,
    quantity            NUMBER(19,4)   NOT NULL,
    unit_price          NUMBER(19,6)   NOT NULL,
    tax_code            VARCHAR2(50),
    tax_rate            NUMBER(10,4),
    line_total          NUMBER(19,2)   NOT NULL,
    tax_amount          NUMBER(19,2)   NOT NULL,
    line_total_with_tax NUMBER(19,2)   NOT NULL,
    CONSTRAINT pk_sales_order_lines PRIMARY KEY (id),
    CONSTRAINT fk_sol_order FOREIGN KEY (order_id)
        REFERENCES "${schema}".sales_orders (id) ON DELETE CASCADE
);

CREATE INDEX idx_sales_order_line_order_id ON "${schema}".sales_order_lines (order_id);

-- ─── SALES_ORDER Permissions ───
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (HEXTORAW('00000000000000090000000000000001'), 'SALES_ORDER', 'CREATE', 'Create a new sales order');
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (HEXTORAW('00000000000000090000000000000002'), 'SALES_ORDER', 'READ', 'View sales order details');
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (HEXTORAW('00000000000000090000000000000003'), 'SALES_ORDER', 'UPDATE', 'Update sales order information');
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (HEXTORAW('00000000000000090000000000000004'), 'SALES_ORDER', 'DELETE', 'Delete a sales order');
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (HEXTORAW('00000000000000090000000000000005'), 'SALES_ORDER', 'SEND', 'Send a sales order to customer');
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (HEXTORAW('00000000000000090000000000000006'), 'SALES_ORDER', 'ACCEPT', 'Accept a sales order');
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (HEXTORAW('00000000000000090000000000000007'), 'SALES_ORDER', 'CONFIRM', 'Confirm a sales order');
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (HEXTORAW('00000000000000090000000000000008'), 'SALES_ORDER', 'CANCEL', 'Cancel a sales order');

COMMIT;
