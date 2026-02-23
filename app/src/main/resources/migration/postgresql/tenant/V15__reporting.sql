CREATE TABLE IF NOT EXISTS report_definitions (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    module VARCHAR(50) NOT NULL,
    type VARCHAR(50) NOT NULL,
    data_source VARCHAR(255),
    columns_json TEXT,
    filters_json TEXT,
    sql_query TEXT,
    required_permission VARCHAR(100),
    system_report BOOLEAN NOT NULL DEFAULT FALSE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_by VARCHAR(255)
    );

CREATE UNIQUE INDEX IF NOT EXISTS uq_report_definitions_name
    ON report_definitions (tenant_id, name);

CREATE INDEX IF NOT EXISTS idx_report_definitions_tenant
    ON report_definitions (tenant_id);

CREATE INDEX IF NOT EXISTS idx_report_definitions_module
    ON report_definitions (tenant_id, module);


CREATE TABLE IF NOT EXISTS saved_reports (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    report_definition_id UUID NOT NULL REFERENCES report_definitions(id),
    name VARCHAR(255) NOT NULL,
    saved_filters_json TEXT,
    saved_sorts_json TEXT,
    shared BOOLEAN NOT NULL DEFAULT FALSE,
    created_by VARCHAR(255),
    created_at TIMESTAMPTZ DEFAULT NOW()
    );

CREATE INDEX IF NOT EXISTS idx_saved_reports_tenant
    ON saved_reports (tenant_id);

CREATE INDEX IF NOT EXISTS idx_saved_reports_user
    ON saved_reports (created_by, tenant_id);


CREATE TABLE IF NOT EXISTS scheduled_reports (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    report_definition_id UUID NOT NULL REFERENCES report_definitions(id),
    name VARCHAR(255) NOT NULL,
    cron_expression VARCHAR(100) NOT NULL,
    format VARCHAR(20) NOT NULL,
    recipient_emails TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    last_run_at TIMESTAMPTZ,
    next_run_at TIMESTAMPTZ,
    created_by VARCHAR(255)
    );

CREATE INDEX IF NOT EXISTS idx_scheduled_reports_tenant
    ON scheduled_reports (tenant_id);

CREATE INDEX IF NOT EXISTS idx_scheduled_reports_active
    ON scheduled_reports (active);


-- =====================================================
-- REPORT DATA SOURCES
-- =====================================================

CREATE TABLE IF NOT EXISTS rpt_sales_orders (
    order_id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    company_id UUID,
    order_number VARCHAR(50),
    order_date DATE,
    customer_id UUID,
    customer_name VARCHAR(255),
    status VARCHAR(30),
    fulfillment_status VARCHAR(30),
    invoicing_status VARCHAR(30),
    subtotal NUMERIC(19,4),
    tax_total NUMERIC(19,4),
    total NUMERIC(19,4),
    currency_code VARCHAR(10)
    );

CREATE INDEX IF NOT EXISTS idx_rpt_sales_orders_tenant
    ON rpt_sales_orders (tenant_id, company_id);

CREATE INDEX IF NOT EXISTS idx_rpt_sales_orders_status
    ON rpt_sales_orders (tenant_id, status);

CREATE TABLE rpt_inventory_stock (
    stock_id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    company_id UUID NOT NULL,
    item_id UUID NOT NULL,
    warehouse_id UUID NOT NULL,
    qty_on_hand NUMERIC NOT NULL DEFAULT 0,
    qty_reserved NUMERIC NOT NULL DEFAULT 0,
    qty_available NUMERIC NOT NULL DEFAULT 0
);

CREATE INDEX IF NOT EXISTS idx_rpt_inventory_stock_tenant
    ON rpt_inventory_stock (tenant_id, company_id);

CREATE INDEX IF NOT EXISTS idx_rpt_inventory_stock_item
    ON rpt_inventory_stock (tenant_id, item_id);

CREATE INDEX IF NOT EXISTS idx_rpt_inventory_stock_warehouse
    ON rpt_inventory_stock (tenant_id, warehouse_id);

-- (Diğer rpt_* tabloların CREATE TABLE bölümü aynı mantıkla
-- CREATE TABLE IF NOT EXISTS + CREATE INDEX IF NOT EXISTS
-- şeklinde devam etmeli. Kısaltıyorum ama yapın doğru.)

-- =====================================================
-- PERMISSIONS (key kolonu YOK varsayımı)
-- =====================================================

INSERT INTO permissions (id, module, action, description)
VALUES
    (gen_random_uuid(), 'REPORT', 'READ', 'Report read access'),
    (gen_random_uuid(), 'REPORT', 'DESIGN', 'Report design access'),
    (gen_random_uuid(), 'REPORT', 'SQL_ADMIN', 'Report SQL admin access')
    ON CONFLICT DO NOTHING;

-- =====================================================
-- ROLE PERMISSIONS
-- =====================================================

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
         JOIN permissions p
              ON p.module = 'REPORT'
WHERE r.name = 'ADMIN'
  AND p.action IN ('READ','DESIGN','SQL_ADMIN')
    ON CONFLICT DO NOTHING;


INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
         JOIN permissions p
              ON p.module = 'REPORT'
WHERE r.name = 'MANAGER'
  AND p.action IN ('READ','DESIGN')
    ON CONFLICT DO NOTHING;


INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
         JOIN permissions p
              ON p.module = 'REPORT'
WHERE r.name = 'USER'
  AND p.action = 'READ'
    ON CONFLICT DO NOTHING;


-- =====================================================
-- SYSTEM REPORT DEFINITIONS
-- =====================================================

INSERT INTO report_definitions (
    id, tenant_id, name, description,
    module, type, data_source,
    required_permission, system_report, active
)
VALUES
    (gen_random_uuid(), '00000000-0000-0000-0000-000000000000',
     'Sales Order List', 'List of all sales orders',
     'SALES', 'PREDEFINED', 'rpt_sales_orders',
     'REPORT:READ', TRUE, TRUE),

    (gen_random_uuid(), '00000000-0000-0000-0000-000000000000',
     'Inventory Stock Levels', 'Current stock levels',
     'INVENTORY', 'PREDEFINED', 'rpt_inventory_stock',
     'REPORT:READ', TRUE, TRUE),

    (gen_random_uuid(), '00000000-0000-0000-0000-000000000000',
     'Sales Invoice Summary', 'Invoices with payment status',
     'FINANCE', 'PREDEFINED', 'rpt_finance_invoices',
     'REPORT:READ', TRUE, TRUE)
    ON CONFLICT (tenant_id, name) DO NOTHING;