-- MySQL adapted version of V10__purchase.sql

CREATE TABLE IF NOT EXISTS purchase_requests (
    id VARCHAR(36) PRIMARY KEY,
    tenant_id VARCHAR(36) NOT NULL,
    company_id VARCHAR(36) NOT NULL,
    request_number VARCHAR(20) NOT NULL,
    requested_by VARCHAR(255),
    approved_by VARCHAR(255),
    request_date DATE NOT NULL,
    needed_by DATE,
    status VARCHAR(50) NOT NULL DEFAULT 'DRAFT',
    notes TEXT,
    CONSTRAINT fk_purchase_request_company FOREIGN KEY (company_id) REFERENCES companies (id),
    CONSTRAINT uq_purchase_request_number UNIQUE (tenant_id, request_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE INDEX idx_purchase_request_tenant_id ON purchase_requests (tenant_id);
CREATE INDEX idx_purchase_request_company_id ON purchase_requests (company_id);
CREATE INDEX idx_purchase_request_status ON purchase_requests (status);

CREATE TABLE IF NOT EXISTS purchase_request_lines (
    id VARCHAR(36) PRIMARY KEY,
    request_id VARCHAR(36) NOT NULL,
    item_id VARCHAR(255),
    item_description VARCHAR(500) NOT NULL,
    unit_code VARCHAR(50) NOT NULL,
    quantity DECIMAL(19,4) NOT NULL,
    preferred_vendor_id VARCHAR(255),
    notes TEXT,
    CONSTRAINT fk_pr_line_request FOREIGN KEY (request_id) REFERENCES purchase_requests (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE INDEX idx_pr_line_request_id ON purchase_request_lines (request_id);

CREATE TABLE IF NOT EXISTS purchase_orders (
    id VARCHAR(36) PRIMARY KEY,
    tenant_id VARCHAR(36) NOT NULL,
    company_id VARCHAR(36) NOT NULL,
    order_number VARCHAR(20) NOT NULL,
    request_id VARCHAR(255),
    vendor_id VARCHAR(255) NOT NULL,
    vendor_name VARCHAR(500),
    order_date DATE NOT NULL,
    expected_delivery_date DATE,
    currency_code VARCHAR(3),
    payment_term_code VARCHAR(50),
    address_line1 VARCHAR(500),
    address_line2 VARCHAR(500),
    city VARCHAR(255),
    state_or_province VARCHAR(255),
    postal_code VARCHAR(50),
    country_code VARCHAR(2),
    status VARCHAR(50) NOT NULL DEFAULT 'DRAFT',
    subtotal DECIMAL(19,2) NOT NULL DEFAULT 0,
    tax_total DECIMAL(19,2) NOT NULL DEFAULT 0,
    total DECIMAL(19,2) NOT NULL DEFAULT 0,
    CONSTRAINT fk_purchase_order_company FOREIGN KEY (company_id) REFERENCES companies (id),
    CONSTRAINT uq_purchase_order_number UNIQUE (tenant_id, order_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE INDEX idx_purchase_order_tenant_id ON purchase_orders (tenant_id);
CREATE INDEX idx_purchase_order_company_id ON purchase_orders (company_id);
CREATE INDEX idx_purchase_order_vendor_id ON purchase_orders (vendor_id);
CREATE INDEX idx_purchase_order_status ON purchase_orders (status);

CREATE TABLE IF NOT EXISTS purchase_order_lines (
    id VARCHAR(36) PRIMARY KEY,
    order_id VARCHAR(36) NOT NULL,
    request_line_id VARCHAR(255),
    item_id VARCHAR(255),
    item_description VARCHAR(500) NOT NULL,
    unit_code VARCHAR(50) NOT NULL,
    ordered_qty DECIMAL(19,4) NOT NULL,
    received_qty DECIMAL(19,4) NOT NULL DEFAULT 0,
    unit_price DECIMAL(19,6) NOT NULL,
    tax_code VARCHAR(50),
    tax_rate DECIMAL(10,4),
    line_total DECIMAL(19,2) NOT NULL,
    tax_amount DECIMAL(19,2) NOT NULL,
    expense_account_id VARCHAR(255),
    CONSTRAINT fk_po_line_order FOREIGN KEY (order_id) REFERENCES purchase_orders (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE INDEX idx_po_line_order_id ON purchase_order_lines (order_id);

CREATE TABLE IF NOT EXISTS goods_receipts (
    id VARCHAR(36) PRIMARY KEY,
    tenant_id VARCHAR(36) NOT NULL,
    company_id VARCHAR(36) NOT NULL,
    receipt_number VARCHAR(20) NOT NULL,
    purchase_order_id VARCHAR(255),
    vendor_id VARCHAR(255),
    warehouse_id VARCHAR(255),
    receipt_date DATE NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'DRAFT',
    vendor_delivery_note VARCHAR(500),
    CONSTRAINT fk_goods_receipt_company FOREIGN KEY (company_id) REFERENCES companies (id),
    CONSTRAINT uq_goods_receipt_number UNIQUE (tenant_id, receipt_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE INDEX idx_goods_receipt_tenant_id ON goods_receipts (tenant_id);
CREATE INDEX idx_goods_receipt_company_id ON goods_receipts (company_id);
CREATE INDEX idx_goods_receipt_po_id ON goods_receipts (purchase_order_id);
CREATE INDEX idx_goods_receipt_status ON goods_receipts (status);

CREATE TABLE IF NOT EXISTS goods_receipt_lines (
    id VARCHAR(36) PRIMARY KEY,
    receipt_id VARCHAR(36) NOT NULL,
    po_line_id VARCHAR(255),
    item_id VARCHAR(255),
    item_description VARCHAR(500),
    unit_code VARCHAR(50) NOT NULL,
    quantity DECIMAL(19,4) NOT NULL,
    batch_note VARCHAR(500),
    CONSTRAINT fk_gr_line_receipt FOREIGN KEY (receipt_id) REFERENCES goods_receipts (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE INDEX idx_gr_line_receipt_id ON goods_receipt_lines (receipt_id);

CREATE TABLE IF NOT EXISTS vendor_invoices (
    id VARCHAR(36) PRIMARY KEY,
    tenant_id VARCHAR(36) NOT NULL,
    company_id VARCHAR(36) NOT NULL,
    invoice_number VARCHAR(25) NOT NULL,
    vendor_invoice_ref VARCHAR(255),
    purchase_order_id VARCHAR(255),
    vendor_id VARCHAR(255),
    vendor_name VARCHAR(500),
    accounting_period_id VARCHAR(255),
    invoice_date DATE NOT NULL,
    due_date DATE,
    currency_code VARCHAR(3),
    exchange_rate DECIMAL(19,6),
    status VARCHAR(50) NOT NULL DEFAULT 'DRAFT',
    subtotal DECIMAL(19,2) NOT NULL DEFAULT 0,
    tax_total DECIMAL(19,2) NOT NULL DEFAULT 0,
    total DECIMAL(19,2) NOT NULL DEFAULT 0,
    paid_amount DECIMAL(19,2) NOT NULL DEFAULT 0,
    CONSTRAINT fk_vendor_invoice_company FOREIGN KEY (company_id) REFERENCES companies (id),
    CONSTRAINT uq_vendor_invoice_number UNIQUE (tenant_id, invoice_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE INDEX idx_vendor_invoice_tenant_id ON vendor_invoices (tenant_id);
CREATE INDEX idx_vendor_invoice_company_id ON vendor_invoices (company_id);
CREATE INDEX idx_vendor_invoice_vendor_id ON vendor_invoices (vendor_id);
CREATE INDEX idx_vendor_invoice_status ON vendor_invoices (status);

CREATE TABLE IF NOT EXISTS vendor_invoice_lines (
    id VARCHAR(36) PRIMARY KEY,
    invoice_id VARCHAR(36) NOT NULL,
    po_line_id VARCHAR(255),
    item_id VARCHAR(255),
    item_description VARCHAR(500),
    unit_code VARCHAR(50) NOT NULL,
    quantity DECIMAL(19,4) NOT NULL,
    unit_price DECIMAL(19,6) NOT NULL,
    tax_code VARCHAR(50),
    tax_rate DECIMAL(10,4),
    line_total DECIMAL(19,2) NOT NULL,
    tax_amount DECIMAL(19,2) NOT NULL,
    line_total_with_tax DECIMAL(19,2) NOT NULL,
    account_id VARCHAR(255),
    CONSTRAINT fk_vi_line_invoice FOREIGN KEY (invoice_id) REFERENCES vendor_invoices (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE INDEX idx_vi_line_invoice_id ON vendor_invoice_lines (invoice_id);

CREATE TABLE IF NOT EXISTS purchase_returns (
    id VARCHAR(36) PRIMARY KEY,
    tenant_id VARCHAR(36) NOT NULL,
    company_id VARCHAR(36) NOT NULL,
    return_number VARCHAR(25) NOT NULL,
    purchase_order_id VARCHAR(255),
    goods_receipt_id VARCHAR(255),
    vendor_id VARCHAR(255),
    warehouse_id VARCHAR(255),
    return_date DATE NOT NULL,
    reason VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'DRAFT',
    CONSTRAINT fk_purchase_return_company FOREIGN KEY (company_id) REFERENCES companies (id),
    CONSTRAINT uq_purchase_return_number UNIQUE (tenant_id, return_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE INDEX idx_purchase_return_tenant_id ON purchase_returns (tenant_id);
CREATE INDEX idx_purchase_return_company_id ON purchase_returns (company_id);
CREATE INDEX idx_purchase_return_po_id ON purchase_returns (purchase_order_id);
CREATE INDEX idx_purchase_return_status ON purchase_returns (status);

CREATE TABLE IF NOT EXISTS purchase_return_lines (
    id VARCHAR(36) PRIMARY KEY,
    return_id VARCHAR(36) NOT NULL,
    receipt_line_id VARCHAR(255),
    item_id VARCHAR(255),
    item_description VARCHAR(500),
    unit_code VARCHAR(50) NOT NULL,
    quantity DECIMAL(19,4) NOT NULL,
    line_reason VARCHAR(500),
    CONSTRAINT fk_prl_line_return FOREIGN KEY (return_id) REFERENCES purchase_returns (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE INDEX idx_prl_line_return_id ON purchase_return_lines (return_id);

CREATE TABLE IF NOT EXISTS vendor_catalogs (
    id VARCHAR(36) PRIMARY KEY,
    tenant_id VARCHAR(36) NOT NULL,
    company_id VARCHAR(36) NOT NULL,
    vendor_id VARCHAR(255) NOT NULL,
    vendor_name VARCHAR(500),
    currency_code VARCHAR(3),
    valid_from DATE NOT NULL,
    valid_to DATE,
    active TINYINT(1) NOT NULL DEFAULT 1,
    CONSTRAINT fk_vendor_catalog_company FOREIGN KEY (company_id) REFERENCES companies (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE INDEX idx_vendor_catalog_tenant_id ON vendor_catalogs (tenant_id);
CREATE INDEX idx_vendor_catalog_company_id ON vendor_catalogs (company_id);
CREATE INDEX idx_vendor_catalog_vendor_id ON vendor_catalogs (vendor_id);

CREATE TABLE IF NOT EXISTS vendor_catalog_entries (
    id VARCHAR(36) PRIMARY KEY,
    catalog_id VARCHAR(36) NOT NULL,
    item_id VARCHAR(255),
    item_description VARCHAR(500),
    unit_code VARCHAR(50) NOT NULL,
    unit_price DECIMAL(19,6) NOT NULL,
    minimum_order_qty DECIMAL(19,4),
    price_break_qty DECIMAL(19,4),
    price_break_unit_price DECIMAL(19,6),
    CONSTRAINT fk_vce_catalog FOREIGN KEY (catalog_id) REFERENCES vendor_catalogs (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE INDEX idx_vce_catalog_id ON vendor_catalog_entries (catalog_id);

-- PERMISSIONS
INSERT IGNORE INTO permissions (id, module, action, description) VALUES
    ('00000000-0000-0000-00A0-000000000001', 'PURCHASE_REQUEST', 'CREATE', 'Create a purchase request'),
    ('00000000-0000-0000-00A0-000000000002', 'PURCHASE_REQUEST', 'READ', 'View purchase request details'),
    ('00000000-0000-0000-00A0-000000000003', 'PURCHASE_REQUEST', 'UPDATE', 'Update purchase request'),
    ('00000000-0000-0000-00A0-000000000004', 'PURCHASE_REQUEST', 'APPROVE', 'Approve a purchase request'),
    ('00000000-0000-0000-00A0-000000000005', 'PURCHASE_REQUEST', 'REJECT', 'Reject a purchase request'),
    ('00000000-0000-0000-00A0-000000000006', 'PURCHASE_REQUEST', 'CANCEL', 'Cancel a purchase request'),

    ('00000000-0000-0000-00A1-000000000001', 'PURCHASE_ORDER', 'CREATE', 'Create a purchase order'),
    ('00000000-0000-0000-00A1-000000000002', 'PURCHASE_ORDER', 'READ', 'View purchase order details'),
    ('00000000-0000-0000-00A1-000000000003', 'PURCHASE_ORDER', 'UPDATE', 'Update purchase order'),
    ('00000000-0000-0000-00A1-000000000004', 'PURCHASE_ORDER', 'SEND', 'Send purchase order to vendor'),
    ('00000000-0000-0000-00A1-000000000005', 'PURCHASE_ORDER', 'CONFIRM', 'Confirm purchase order'),
    ('00000000-0000-0000-00A1-000000000006', 'PURCHASE_ORDER', 'CANCEL', 'Cancel a purchase order'),

    ('00000000-0000-0000-00A2-000000000001', 'GOODS_RECEIPT', 'CREATE', 'Create a goods receipt'),
    ('00000000-0000-0000-00A2-000000000002', 'GOODS_RECEIPT', 'READ', 'View goods receipt details'),
    ('00000000-0000-0000-00A2-000000000003', 'GOODS_RECEIPT', 'POST', 'Post a goods receipt'),

    ('00000000-0000-0000-00A3-000000000001', 'VENDOR_INVOICE', 'CREATE', 'Create a vendor invoice'),
    ('00000000-0000-0000-00A3-000000000002', 'VENDOR_INVOICE', 'READ', 'View vendor invoice details'),
    ('00000000-0000-0000-00A3-000000000003', 'VENDOR_INVOICE', 'POST', 'Post a vendor invoice'),
    ('00000000-0000-0000-00A3-000000000004', 'VENDOR_INVOICE', 'CANCEL', 'Cancel a vendor invoice'),

    ('00000000-0000-0000-00A4-000000000001', 'PURCHASE_RETURN', 'CREATE', 'Create a purchase return'),
    ('00000000-0000-0000-00A4-000000000002', 'PURCHASE_RETURN', 'READ', 'View purchase return details'),
    ('00000000-0000-0000-00A4-000000000003', 'PURCHASE_RETURN', 'POST', 'Post a purchase return'),
    ('00000000-0000-0000-00A4-000000000004', 'PURCHASE_RETURN', 'COMPLETE', 'Complete a purchase return'),

    ('00000000-0000-0000-00A5-000000000001', 'VENDOR_CATALOG', 'CREATE', 'Create a vendor catalog'),
    ('00000000-0000-0000-00A5-000000000002', 'VENDOR_CATALOG', 'READ', 'View vendor catalog details'),
    ('00000000-0000-0000-00A5-000000000003', 'VENDOR_CATALOG', 'UPDATE', 'Update vendor catalog')
;

-- Assign role_permissions
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM permissions p JOIN roles r ON r.tenant_id = '${tenant_id}' AND r.name = 'ADMIN'
LEFT JOIN role_permissions rp ON rp.role_id = r.id AND rp.permission_id = p.id
WHERE rp.role_id IS NULL
  AND p.module IN (
    'PURCHASE_REQUEST', 'PURCHASE_ORDER', 'GOODS_RECEIPT',
    'VENDOR_INVOICE', 'PURCHASE_RETURN', 'VENDOR_CATALOG'
);

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM permissions p JOIN roles r ON r.tenant_id = '${tenant_id}' AND r.name = 'MANAGER'
LEFT JOIN role_permissions rp ON rp.role_id = r.id AND rp.permission_id = p.id
WHERE rp.role_id IS NULL
  AND p.module IN (
    'PURCHASE_REQUEST', 'PURCHASE_ORDER', 'GOODS_RECEIPT',
    'VENDOR_INVOICE', 'PURCHASE_RETURN', 'VENDOR_CATALOG'
)
  AND p.action != 'DELETE';

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM permissions p JOIN roles r ON r.tenant_id = '${tenant_id}' AND r.name = 'USER'
LEFT JOIN role_permissions rp ON rp.role_id = r.id AND rp.permission_id = p.id
WHERE rp.role_id IS NULL
  AND p.module IN (
    'PURCHASE_REQUEST', 'PURCHASE_ORDER', 'GOODS_RECEIPT',
    'VENDOR_INVOICE', 'PURCHASE_RETURN', 'VENDOR_CATALOG'
)
  AND p.action IN ('CREATE', 'READ', 'UPDATE');

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM permissions p JOIN roles r ON r.tenant_id = '${tenant_id}' AND r.name = 'VIEWER'
LEFT JOIN role_permissions rp ON rp.role_id = r.id AND rp.permission_id = p.id
WHERE rp.role_id IS NULL
  AND p.module IN (
    'PURCHASE_REQUEST', 'PURCHASE_ORDER', 'GOODS_RECEIPT',
    'VENDOR_INVOICE', 'PURCHASE_RETURN', 'VENDOR_CATALOG'
)
  AND p.action = 'READ';

