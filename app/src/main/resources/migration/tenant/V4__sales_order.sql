-- Sales Orders table
CREATE TABLE sales_orders
(
    id                         UUID PRIMARY KEY,
    tenant_id                  UUID           NOT NULL,
    company_id                 UUID           NOT NULL,
    order_number               VARCHAR(20)    NOT NULL,
    order_date                 DATE           NOT NULL,
    expiry_date                DATE,
    customer_id                UUID,
    currency_code              VARCHAR(3),
    payment_term_code          VARCHAR(50),
    shipping_address_line1     VARCHAR(500),
    shipping_address_line2     VARCHAR(500),
    shipping_city              VARCHAR(255),
    shipping_state_or_province VARCHAR(255),
    shipping_postal_code       VARCHAR(50),
    shipping_country_code      VARCHAR(2),
    status                     VARCHAR(50)    NOT NULL DEFAULT 'DRAFT',
    fulfillment_status         VARCHAR(50)    NOT NULL DEFAULT 'NOT_STARTED',
    invoiced_amount            NUMERIC(19, 2) NOT NULL DEFAULT 0,
    subtotal                   NUMERIC(19, 2) NOT NULL DEFAULT 0,
    tax_total                  NUMERIC(19, 2) NOT NULL DEFAULT 0,
    total                      NUMERIC(19, 2) NOT NULL DEFAULT 0,

    CONSTRAINT fk_sales_order_company
        FOREIGN KEY (company_id) REFERENCES companies (id),

    CONSTRAINT fk_sales_order_customer
        FOREIGN KEY (customer_id) REFERENCES business_partner (id),

    CONSTRAINT fk_sales_order_currency
        FOREIGN KEY (currency_code) REFERENCES currencies (code),

    CONSTRAINT fk_sales_order_payment_term
        FOREIGN KEY (payment_term_code) REFERENCES payment_terms (code),

    CONSTRAINT uq_sales_order_number UNIQUE (tenant_id, order_number)
);

CREATE INDEX idx_sales_order_tenant_id ON sales_orders (tenant_id);
CREATE INDEX idx_sales_order_company_id ON sales_orders (company_id);
CREATE INDEX idx_sales_order_customer_id ON sales_orders (customer_id);
CREATE INDEX idx_sales_order_status ON sales_orders (status);
CREATE INDEX idx_sales_order_order_date ON sales_orders (order_date);

-- Sales Order Lines table
CREATE TABLE sales_order_lines
(
    id                  UUID PRIMARY KEY,
    order_id            UUID           NOT NULL,
    sequence            INT            NOT NULL,
    item_id             VARCHAR(255),
    item_description    VARCHAR(500)   NOT NULL,
    unit_code           VARCHAR(50)    NOT NULL,
    quantity            NUMERIC(19, 4) NOT NULL,
    unit_price          NUMERIC(19, 6) NOT NULL,
    tax_code            VARCHAR(50),
    tax_rate            NUMERIC(10, 4),
    line_total          NUMERIC(19, 2) NOT NULL,
    tax_amount          NUMERIC(19, 2) NOT NULL,
    line_total_with_tax NUMERIC(19, 2) NOT NULL,

    CONSTRAINT fk_sales_order_line_order
        FOREIGN KEY (order_id) REFERENCES sales_orders (id)
            ON DELETE CASCADE
);

CREATE INDEX idx_sales_order_line_order_id ON sales_order_lines (order_id);


-- ─── SALES_ORDER Permissions ───

INSERT INTO permissions (id, module, action, description)
VALUES ('00000000-0000-0000-0009-000000000001', 'SALES_ORDER', 'CREATE', 'Create a new sales order'),
       ('00000000-0000-0000-0009-000000000002', 'SALES_ORDER', 'READ', 'View sales order details'),
       ('00000000-0000-0000-0009-000000000003', 'SALES_ORDER', 'UPDATE', 'Update sales order information'),
       ('00000000-0000-0000-0009-000000000004', 'SALES_ORDER', 'DELETE', 'Delete a sales order'),
       ('00000000-0000-0000-0009-000000000005', 'SALES_ORDER', 'SEND', 'Send a sales order to customer'),
       ('00000000-0000-0000-0009-000000000006', 'SALES_ORDER', 'ACCEPT', 'Accept a sales order'),
       ('00000000-0000-0000-0009-000000000007', 'SALES_ORDER', 'CONFIRM', 'Confirm a sales order'),
       ('00000000-0000-0000-0009-000000000008', 'SALES_ORDER', 'CANCEL', 'Cancel a sales order');
