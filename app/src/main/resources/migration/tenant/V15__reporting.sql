CREATE TABLE report_definitions (
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

CREATE INDEX idx_report_definitions_tenant ON report_definitions (tenant_id);
CREATE INDEX idx_report_definitions_module ON report_definitions (tenant_id, module);

CREATE TABLE saved_reports (
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

CREATE INDEX idx_saved_reports_tenant ON saved_reports (tenant_id);
CREATE INDEX idx_saved_reports_user ON saved_reports (created_by, tenant_id);

CREATE TABLE scheduled_reports (
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

CREATE INDEX idx_scheduled_reports_tenant ON scheduled_reports (tenant_id);
CREATE INDEX idx_scheduled_reports_active ON scheduled_reports (active);

CREATE TABLE rpt_sales_orders (
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
    subtotal NUMERIC(19, 4),
    tax_total NUMERIC(19, 4),
    total NUMERIC(19, 4),
    currency_code VARCHAR(10)
);

CREATE INDEX idx_rpt_sales_orders_tenant ON rpt_sales_orders (tenant_id, company_id);
CREATE INDEX idx_rpt_sales_orders_status ON rpt_sales_orders (tenant_id, status);

CREATE TABLE rpt_inventory_stock (
    stock_id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    company_id UUID,
    item_id UUID,
    item_code VARCHAR(50),
    item_name VARCHAR(255),
    warehouse_id UUID,
    warehouse_name VARCHAR(255),
    qty_on_hand NUMERIC(19, 4),
    qty_reserved NUMERIC(19, 4),
    qty_available NUMERIC(19, 4),
    reorder_point NUMERIC(19, 4),
    below_reorder BOOLEAN DEFAULT FALSE
);

CREATE INDEX idx_rpt_inventory_stock_tenant ON rpt_inventory_stock (tenant_id, company_id);
CREATE INDEX idx_rpt_inventory_stock_item ON rpt_inventory_stock (tenant_id, item_id);

CREATE TABLE rpt_finance_invoices (
    invoice_id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    company_id UUID,
    invoice_number VARCHAR(50),
    invoice_date DATE,
    due_date DATE,
    customer_id UUID,
    customer_name VARCHAR(255),
    currency_code VARCHAR(10),
    total NUMERIC(19, 4),
    paid_amount NUMERIC(19, 4),
    remaining_amount NUMERIC(19, 4),
    payment_status VARCHAR(30)
);

CREATE INDEX idx_rpt_finance_invoices_tenant ON rpt_finance_invoices (tenant_id, company_id);
CREATE INDEX idx_rpt_finance_invoices_status ON rpt_finance_invoices (tenant_id, payment_status);

CREATE TABLE rpt_purchase_orders (
    po_id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    company_id UUID,
    po_number VARCHAR(50),
    order_date DATE,
    vendor_id UUID,
    vendor_name VARCHAR(255),
    status VARCHAR(30),
    total NUMERIC(19, 4),
    currency_code VARCHAR(10)
);

CREATE INDEX idx_rpt_purchase_orders_tenant ON rpt_purchase_orders (tenant_id, company_id);
CREATE INDEX idx_rpt_purchase_orders_status ON rpt_purchase_orders (tenant_id, status);

CREATE TABLE rpt_hr_employees (
    employee_id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    company_id UUID,
    employee_number VARCHAR(50),
    full_name VARCHAR(255),
    position_title VARCHAR(255),
    department_name VARCHAR(255),
    employment_type VARCHAR(30),
    status VARCHAR(30),
    hire_date DATE,
    country_code VARCHAR(10)
);

CREATE INDEX idx_rpt_hr_employees_tenant ON rpt_hr_employees (tenant_id, company_id);
CREATE INDEX idx_rpt_hr_employees_status ON rpt_hr_employees (tenant_id, status);

CREATE TABLE rpt_crm_opportunities (
    opportunity_id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    company_id UUID,
    opportunity_number VARCHAR(50),
    title VARCHAR(255),
    stage VARCHAR(30),
    probability NUMERIC(5, 2),
    expected_value NUMERIC(19, 4),
    currency_code VARCHAR(10),
    expected_close_date DATE,
    owner_id VARCHAR(255)
);

CREATE INDEX idx_rpt_crm_opportunities_tenant ON rpt_crm_opportunities (tenant_id, company_id);
CREATE INDEX idx_rpt_crm_opportunities_stage ON rpt_crm_opportunities (tenant_id, stage);

CREATE TABLE rpt_shipments (
    shipment_id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    company_id UUID,
    shipment_number VARCHAR(50),
    delivery_order_id UUID,
    warehouse_id UUID,
    warehouse_name VARCHAR(255),
    status VARCHAR(30),
    carrier_name VARCHAR(255),
    tracking_number VARCHAR(255),
    dispatched_at TIMESTAMPTZ,
    delivered_at TIMESTAMPTZ
);

CREATE INDEX idx_rpt_shipments_tenant ON rpt_shipments (tenant_id, company_id);
CREATE INDEX idx_rpt_shipments_status ON rpt_shipments (tenant_id, status);

INSERT INTO permissions (id, module, action, key) VALUES
    (gen_random_uuid(), 'REPORT', 'READ', 'REPORT:READ'),
    (gen_random_uuid(), 'REPORT', 'DESIGN', 'REPORT:DESIGN'),
    (gen_random_uuid(), 'REPORT', 'SQL_ADMIN', 'REPORT:SQL_ADMIN');

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
WHERE r.name = 'ADMIN' AND p.key IN ('REPORT:READ', 'REPORT:DESIGN', 'REPORT:SQL_ADMIN');

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
WHERE r.name = 'MANAGER' AND p.key IN ('REPORT:READ', 'REPORT:DESIGN');

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
WHERE r.name = 'USER' AND p.key = 'REPORT:READ';

INSERT INTO report_definitions (id, tenant_id, name, description, module, type, data_source, required_permission, system_report, active)
VALUES
    (gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Sales Order List', 'List of all sales orders', 'SALES', 'PREDEFINED', 'rpt_sales_orders', 'REPORT:READ', TRUE, TRUE),
    (gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Inventory Stock Levels', 'Current stock levels by item and warehouse', 'INVENTORY', 'PREDEFINED', 'rpt_inventory_stock', 'REPORT:READ', TRUE, TRUE),
    (gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Sales Invoice Summary', 'Sales invoices with payment status', 'FINANCE', 'PREDEFINED', 'rpt_finance_invoices', 'REPORT:READ', TRUE, TRUE),
    (gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Purchase Order List', 'List of all purchase orders', 'PURCHASE', 'PREDEFINED', 'rpt_purchase_orders', 'REPORT:READ', TRUE, TRUE),
    (gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Employee Directory', 'All employees with their positions', 'HR', 'PREDEFINED', 'rpt_hr_employees', 'REPORT:READ', TRUE, TRUE),
    (gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'CRM Pipeline', 'Opportunities by stage', 'CRM', 'PREDEFINED', 'rpt_crm_opportunities', 'REPORT:READ', TRUE, TRUE),
    (gen_random_uuid(), '00000000-0000-0000-0000-000000000000', 'Shipment Tracker', 'Shipments and delivery status', 'SHIPMENT', 'PREDEFINED', 'rpt_shipments', 'REPORT:READ', TRUE, TRUE);

