-- V10__purchase.sql (Main Application)
-- Oracle conversion: UUID->VARCHAR2(32), TEXT->CLOB, BOOLEAN->NUMBER(1,0),
--                    NUMERIC->NUMBER, VARCHAR->VARCHAR2, ON CONFLICT->MERGE

-- ─── Purchase Requests ───
CREATE TABLE "${schema}".purchase_requests (
    id             VARCHAR2(32)       NOT NULL,
    tenant_id      VARCHAR2(32)       NOT NULL,
    company_id     VARCHAR2(32)       NOT NULL,
    request_number VARCHAR2(20)  NOT NULL,
    requested_by   VARCHAR2(255),
    approved_by    VARCHAR2(255),
    request_date   DATE          NOT NULL,
    needed_by      DATE,
    status         VARCHAR2(50)  DEFAULT 'DRAFT' NOT NULL,
    notes          CLOB,
    CONSTRAINT pk_purchase_requests PRIMARY KEY (id),
    CONSTRAINT fk_pr_company FOREIGN KEY (company_id) REFERENCES "${schema}".companies (id),
    CONSTRAINT uq_pr_number  UNIQUE (tenant_id, request_number)
);

CREATE INDEX idx_purchase_request_tenant_id  ON "${schema}".purchase_requests (tenant_id);
CREATE INDEX idx_purchase_request_company_id ON "${schema}".purchase_requests (company_id);
CREATE INDEX idx_purchase_request_status     ON "${schema}".purchase_requests (status);

CREATE TABLE "${schema}".purchase_request_lines (
    id                  VARCHAR2(32)        NOT NULL,
    request_id          VARCHAR2(32)        NOT NULL,
    item_id             VARCHAR2(255),
    item_description    VARCHAR2(500)  NOT NULL,
    unit_code           VARCHAR2(50)   NOT NULL,
    quantity            NUMBER(19,4)   NOT NULL,
    preferred_vendor_id VARCHAR2(255),
    notes               CLOB,
    CONSTRAINT pk_purchase_request_lines PRIMARY KEY (id),
    CONSTRAINT fk_prl_request FOREIGN KEY (request_id)
        REFERENCES "${schema}".purchase_requests (id) ON DELETE CASCADE
);

CREATE INDEX idx_pr_line_request_id ON "${schema}".purchase_request_lines (request_id);

-- ─── Purchase Orders ───
CREATE TABLE "${schema}".purchase_orders (
    id                     VARCHAR2(32)        NOT NULL,
    tenant_id              VARCHAR2(32)        NOT NULL,
    company_id             VARCHAR2(32)        NOT NULL,
    order_number           VARCHAR2(20)   NOT NULL,
    request_id             VARCHAR2(255),
    vendor_id              VARCHAR2(255)  NOT NULL,
    vendor_name            VARCHAR2(500),
    order_date             DATE           NOT NULL,
    expected_delivery_date DATE,
    currency_code          VARCHAR2(3),
    payment_term_code      VARCHAR2(50),
    address_line1          VARCHAR2(500),
    address_line2          VARCHAR2(500),
    city                   VARCHAR2(255),
    state_or_province      VARCHAR2(255),
    postal_code            VARCHAR2(50),
    country_code           VARCHAR2(2),
    status                 VARCHAR2(50)   DEFAULT 'DRAFT' NOT NULL,
    subtotal               NUMBER(19,2)   DEFAULT 0 NOT NULL,
    tax_total              NUMBER(19,2)   DEFAULT 0 NOT NULL,
    total                  NUMBER(19,2)   DEFAULT 0 NOT NULL,
    CONSTRAINT pk_purchase_orders PRIMARY KEY (id),
    CONSTRAINT fk_po_company FOREIGN KEY (company_id) REFERENCES "${schema}".companies (id),
    CONSTRAINT uq_po_number  UNIQUE (tenant_id, order_number)
);

CREATE INDEX idx_purchase_order_tenant_id  ON "${schema}".purchase_orders (tenant_id);
CREATE INDEX idx_purchase_order_company_id ON "${schema}".purchase_orders (company_id);
CREATE INDEX idx_purchase_order_vendor_id  ON "${schema}".purchase_orders (vendor_id);
CREATE INDEX idx_purchase_order_status     ON "${schema}".purchase_orders (status);

CREATE TABLE "${schema}".purchase_order_lines (
    id                 VARCHAR2(32)        NOT NULL,
    order_id           VARCHAR2(32)        NOT NULL,
    request_line_id    VARCHAR2(255),
    item_id            VARCHAR2(255),
    item_description   VARCHAR2(500)  NOT NULL,
    unit_code          VARCHAR2(50)   NOT NULL,
    ordered_qty        NUMBER(19,4)   NOT NULL,
    received_qty       NUMBER(19,4)   DEFAULT 0 NOT NULL,
    unit_price         NUMBER(19,6)   NOT NULL,
    tax_code           VARCHAR2(50),
    tax_rate           NUMBER(10,4),
    line_total         NUMBER(19,2)   NOT NULL,
    tax_amount         NUMBER(19,2)   NOT NULL,
    expense_account_id VARCHAR2(255),
    CONSTRAINT pk_purchase_order_lines PRIMARY KEY (id),
    CONSTRAINT fk_pol_order FOREIGN KEY (order_id)
        REFERENCES "${schema}".purchase_orders (id) ON DELETE CASCADE
);

CREATE INDEX idx_po_line_order_id ON "${schema}".purchase_order_lines (order_id);

-- ─── Goods Receipts ───
CREATE TABLE "${schema}".goods_receipts (
    id                   VARCHAR2(32)       NOT NULL,
    tenant_id            VARCHAR2(32)       NOT NULL,
    company_id           VARCHAR2(32)       NOT NULL,
    receipt_number       VARCHAR2(20)  NOT NULL,
    purchase_order_id    VARCHAR2(255),
    vendor_id            VARCHAR2(255),
    warehouse_id         VARCHAR2(255),
    receipt_date         DATE          NOT NULL,
    status               VARCHAR2(50)  DEFAULT 'DRAFT' NOT NULL,
    vendor_delivery_note VARCHAR2(500),
    CONSTRAINT pk_goods_receipts PRIMARY KEY (id),
    CONSTRAINT fk_gr_company FOREIGN KEY (company_id) REFERENCES "${schema}".companies (id),
    CONSTRAINT uq_gr_number  UNIQUE (tenant_id, receipt_number)
);

CREATE INDEX idx_goods_receipt_tenant_id  ON "${schema}".goods_receipts (tenant_id);
CREATE INDEX idx_goods_receipt_company_id ON "${schema}".goods_receipts (company_id);
CREATE INDEX idx_goods_receipt_po_id      ON "${schema}".goods_receipts (purchase_order_id);
CREATE INDEX idx_goods_receipt_status     ON "${schema}".goods_receipts (status);

CREATE TABLE "${schema}".goods_receipt_lines (
    id               VARCHAR2(32)        NOT NULL,
    receipt_id       VARCHAR2(32)        NOT NULL,
    po_line_id       VARCHAR2(255),
    item_id          VARCHAR2(255),
    item_description VARCHAR2(500),
    unit_code        VARCHAR2(50)   NOT NULL,
    quantity         NUMBER(19,4)   NOT NULL,
    batch_note       VARCHAR2(500),
    CONSTRAINT pk_goods_receipt_lines PRIMARY KEY (id),
    CONSTRAINT fk_grl_receipt FOREIGN KEY (receipt_id)
        REFERENCES "${schema}".goods_receipts (id) ON DELETE CASCADE
);

CREATE INDEX idx_gr_line_receipt_id ON "${schema}".goods_receipt_lines (receipt_id);

-- ─── Vendor Invoices ───
CREATE TABLE "${schema}".vendor_invoices (
    id                   VARCHAR2(32)        NOT NULL,
    tenant_id            VARCHAR2(32)        NOT NULL,
    company_id           VARCHAR2(32)        NOT NULL,
    invoice_number       VARCHAR2(25)   NOT NULL,
    vendor_invoice_ref   VARCHAR2(255),
    purchase_order_id    VARCHAR2(255),
    vendor_id            VARCHAR2(255),
    vendor_name          VARCHAR2(500),
    accounting_period_id VARCHAR2(255),
    invoice_date         DATE           NOT NULL,
    due_date             DATE,
    currency_code        VARCHAR2(3),
    exchange_rate        NUMBER(19,6),
    status               VARCHAR2(50)   DEFAULT 'DRAFT' NOT NULL,
    subtotal             NUMBER(19,2)   DEFAULT 0 NOT NULL,
    tax_total            NUMBER(19,2)   DEFAULT 0 NOT NULL,
    total                NUMBER(19,2)   DEFAULT 0 NOT NULL,
    paid_amount          NUMBER(19,2)   DEFAULT 0 NOT NULL,
    CONSTRAINT pk_vendor_invoices PRIMARY KEY (id),
    CONSTRAINT fk_vi_company FOREIGN KEY (company_id) REFERENCES "${schema}".companies (id),
    CONSTRAINT uq_vi_number  UNIQUE (tenant_id, invoice_number)
);

CREATE INDEX idx_vendor_invoice_tenant_id  ON "${schema}".vendor_invoices (tenant_id);
CREATE INDEX idx_vendor_invoice_company_id ON "${schema}".vendor_invoices (company_id);
CREATE INDEX idx_vendor_invoice_vendor_id  ON "${schema}".vendor_invoices (vendor_id);
CREATE INDEX idx_vendor_invoice_status     ON "${schema}".vendor_invoices (status);

CREATE TABLE "${schema}".vendor_invoice_lines (
    id                  VARCHAR2(32)        NOT NULL,
    invoice_id          VARCHAR2(32)        NOT NULL,
    po_line_id          VARCHAR2(255),
    item_id             VARCHAR2(255),
    item_description    VARCHAR2(500),
    unit_code           VARCHAR2(50)   NOT NULL,
    quantity            NUMBER(19,4)   NOT NULL,
    unit_price          NUMBER(19,6)   NOT NULL,
    tax_code            VARCHAR2(50),
    tax_rate            NUMBER(10,4),
    line_total          NUMBER(19,2)   NOT NULL,
    tax_amount          NUMBER(19,2)   NOT NULL,
    line_total_with_tax NUMBER(19,2)   NOT NULL,
    account_id          VARCHAR2(255),
    CONSTRAINT pk_vendor_invoice_lines PRIMARY KEY (id),
    CONSTRAINT fk_vil_invoice FOREIGN KEY (invoice_id)
        REFERENCES "${schema}".vendor_invoices (id) ON DELETE CASCADE
);

CREATE INDEX idx_vi_line_invoice_id ON "${schema}".vendor_invoice_lines (invoice_id);

-- ─── Purchase Returns ───
CREATE TABLE "${schema}".purchase_returns (
    id                VARCHAR2(32)       NOT NULL,
    tenant_id         VARCHAR2(32)       NOT NULL,
    company_id        VARCHAR2(32)       NOT NULL,
    return_number     VARCHAR2(25)  NOT NULL,
    purchase_order_id VARCHAR2(255),
    goods_receipt_id  VARCHAR2(255),
    vendor_id         VARCHAR2(255),
    warehouse_id      VARCHAR2(255),
    return_date       DATE          NOT NULL,
    reason            VARCHAR2(50)  NOT NULL,
    status            VARCHAR2(50)  DEFAULT 'DRAFT' NOT NULL,
    CONSTRAINT pk_purchase_returns PRIMARY KEY (id),
    CONSTRAINT fk_pret_company FOREIGN KEY (company_id) REFERENCES "${schema}".companies (id),
    CONSTRAINT uq_pret_number  UNIQUE (tenant_id, return_number)
);

CREATE INDEX idx_purchase_return_tenant_id  ON "${schema}".purchase_returns (tenant_id);
CREATE INDEX idx_purchase_return_company_id ON "${schema}".purchase_returns (company_id);
CREATE INDEX idx_purchase_return_po_id      ON "${schema}".purchase_returns (purchase_order_id);
CREATE INDEX idx_purchase_return_status     ON "${schema}".purchase_returns (status);

CREATE TABLE "${schema}".purchase_return_lines (
    id               VARCHAR2(32)        NOT NULL,
    return_id        VARCHAR2(32)        NOT NULL,
    receipt_line_id  VARCHAR2(255),
    item_id          VARCHAR2(255),
    item_description VARCHAR2(500),
    unit_code        VARCHAR2(50)   NOT NULL,
    quantity         NUMBER(19,4)   NOT NULL,
    line_reason      VARCHAR2(500),
    CONSTRAINT pk_purchase_return_lines PRIMARY KEY (id),
    CONSTRAINT fk_prl_return FOREIGN KEY (return_id)
        REFERENCES "${schema}".purchase_returns (id) ON DELETE CASCADE
);

CREATE INDEX idx_prl_line_return_id ON "${schema}".purchase_return_lines (return_id);

-- ─── Vendor Catalogs ───
CREATE TABLE "${schema}".vendor_catalogs (
    id            VARCHAR2(32)       NOT NULL,
    tenant_id     VARCHAR2(32)       NOT NULL,
    company_id    VARCHAR2(32)       NOT NULL,
    vendor_id     VARCHAR2(255) NOT NULL,
    vendor_name   VARCHAR2(500),
    currency_code VARCHAR2(3),
    valid_from    DATE          NOT NULL,
    valid_to      DATE,
    active        NUMBER(1,0)   DEFAULT 1 NOT NULL,
    CONSTRAINT pk_vendor_catalogs PRIMARY KEY (id),
    CONSTRAINT fk_vc_company FOREIGN KEY (company_id) REFERENCES "${schema}".companies (id)
);

CREATE INDEX idx_vendor_catalog_tenant_id  ON "${schema}".vendor_catalogs (tenant_id);
CREATE INDEX idx_vendor_catalog_company_id ON "${schema}".vendor_catalogs (company_id);
CREATE INDEX idx_vendor_catalog_vendor_id  ON "${schema}".vendor_catalogs (vendor_id);

CREATE TABLE "${schema}".vendor_catalog_entries (
    id                     VARCHAR2(32)        NOT NULL,
    catalog_id             VARCHAR2(32)        NOT NULL,
    item_id                VARCHAR2(255),
    item_description       VARCHAR2(500),
    unit_code              VARCHAR2(50)   NOT NULL,
    unit_price             NUMBER(19,6)   NOT NULL,
    minimum_order_qty      NUMBER(19,4),
    price_break_qty        NUMBER(19,4),
    price_break_unit_price NUMBER(19,6),
    CONSTRAINT pk_vendor_catalog_entries PRIMARY KEY (id),
    CONSTRAINT fk_vce_catalog FOREIGN KEY (catalog_id)
        REFERENCES "${schema}".vendor_catalogs (id) ON DELETE CASCADE
);

CREATE INDEX idx_vce_catalog_id ON "${schema}".vendor_catalog_entries (catalog_id);

-- ─── Purchase Permissions ───
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000A0000000000001'), 'PURCHASE_REQUEST', 'CREATE',  'Create a purchase request');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000A0000000000002'), 'PURCHASE_REQUEST', 'READ',    'View purchase request details');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000A0000000000003'), 'PURCHASE_REQUEST', 'UPDATE',  'Update purchase request');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000A0000000000004'), 'PURCHASE_REQUEST', 'APPROVE', 'Approve a purchase request');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000A0000000000005'), 'PURCHASE_REQUEST', 'REJECT',  'Reject a purchase request');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000A0000000000006'), 'PURCHASE_REQUEST', 'CANCEL',  'Cancel a purchase request');

INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000A1000000000001'), 'PURCHASE_ORDER', 'CREATE',  'Create a purchase order');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000A1000000000002'), 'PURCHASE_ORDER', 'READ',    'View purchase order details');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000A1000000000003'), 'PURCHASE_ORDER', 'UPDATE',  'Update purchase order');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000A1000000000004'), 'PURCHASE_ORDER', 'SEND',    'Send purchase order to vendor');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000A1000000000005'), 'PURCHASE_ORDER', 'CONFIRM', 'Confirm purchase order');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000A1000000000006'), 'PURCHASE_ORDER', 'CANCEL',  'Cancel a purchase order');

INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000A2000000000001'), 'GOODS_RECEIPT', 'CREATE', 'Create a goods receipt');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000A2000000000002'), 'GOODS_RECEIPT', 'READ',   'View goods receipt details');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000A2000000000003'), 'GOODS_RECEIPT', 'POST',   'Post a goods receipt');

INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000A3000000000001'), 'VENDOR_INVOICE', 'CREATE', 'Create a vendor invoice');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000A3000000000002'), 'VENDOR_INVOICE', 'READ',   'View vendor invoice details');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000A3000000000003'), 'VENDOR_INVOICE', 'POST',   'Post a vendor invoice');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000A3000000000004'), 'VENDOR_INVOICE', 'CANCEL', 'Cancel a vendor invoice');

INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000A4000000000001'), 'PURCHASE_RETURN', 'CREATE',   'Create a purchase return');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000A4000000000002'), 'PURCHASE_RETURN', 'READ',     'View purchase return details');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000A4000000000003'), 'PURCHASE_RETURN', 'POST',     'Post a purchase return');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000A4000000000004'), 'PURCHASE_RETURN', 'COMPLETE', 'Complete a purchase return');

INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000A5000000000001'), 'VENDOR_CATALOG', 'CREATE', 'Create a vendor catalog');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000A5000000000002'), 'VENDOR_CATALOG', 'READ',   'View vendor catalog details');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000A5000000000003'), 'VENDOR_CATALOG', 'UPDATE', 'Update vendor catalog');

-- ─── Role assignments ───
MERGE INTO "${schema}".role_permissions tgt
USING (SELECT STANDARD_HASH('ADMIN_${tenant_id}','MD5') AS role_id, p.id AS permission_id
       FROM "${schema}".permissions p
       WHERE p.module IN ('PURCHASE_REQUEST','PURCHASE_ORDER','GOODS_RECEIPT','VENDOR_INVOICE','PURCHASE_RETURN','VENDOR_CATALOG')) src
ON (tgt.role_id = src.role_id AND tgt.permission_id = src.permission_id)
WHEN NOT MATCHED THEN INSERT (role_id, permission_id) VALUES (src.role_id, src.permission_id);

MERGE INTO "${schema}".role_permissions tgt
USING (SELECT STANDARD_HASH('MANAGER_${tenant_id}','MD5') AS role_id, p.id AS permission_id
       FROM "${schema}".permissions p
       WHERE p.module IN ('PURCHASE_REQUEST','PURCHASE_ORDER','GOODS_RECEIPT','VENDOR_INVOICE','PURCHASE_RETURN','VENDOR_CATALOG')) src
ON (tgt.role_id = src.role_id AND tgt.permission_id = src.permission_id)
WHEN NOT MATCHED THEN INSERT (role_id, permission_id) VALUES (src.role_id, src.permission_id);

MERGE INTO "${schema}".role_permissions tgt
USING (SELECT STANDARD_HASH('USER_${tenant_id}','MD5') AS role_id, p.id AS permission_id
       FROM "${schema}".permissions p
       WHERE p.module IN ('PURCHASE_REQUEST','PURCHASE_ORDER','GOODS_RECEIPT','VENDOR_INVOICE','PURCHASE_RETURN','VENDOR_CATALOG')
         AND p.action IN ('CREATE','READ','UPDATE')) src
ON (tgt.role_id = src.role_id AND tgt.permission_id = src.permission_id)
WHEN NOT MATCHED THEN INSERT (role_id, permission_id) VALUES (src.role_id, src.permission_id);

MERGE INTO "${schema}".role_permissions tgt
USING (SELECT STANDARD_HASH('VIEWER_${tenant_id}','MD5') AS role_id, p.id AS permission_id
       FROM "${schema}".permissions p
       WHERE p.module IN ('PURCHASE_REQUEST','PURCHASE_ORDER','GOODS_RECEIPT','VENDOR_INVOICE','PURCHASE_RETURN','VENDOR_CATALOG')
         AND p.action = 'READ') src
ON (tgt.role_id = src.role_id AND tgt.permission_id = src.permission_id)
WHEN NOT MATCHED THEN INSERT (role_id, permission_id) VALUES (src.role_id, src.permission_id);

COMMIT;
