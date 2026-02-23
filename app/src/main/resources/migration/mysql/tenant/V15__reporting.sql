-- MySQL adapted version of V15__reporting.sql

-- Example read models for Sales and Inventory; extend as needed
CREATE TABLE IF NOT EXISTS sales_order_summary (
    order_id VARCHAR(36) PRIMARY KEY,
    tenant_id VARCHAR(36) NOT NULL,
    company_id VARCHAR(36) NOT NULL,
    order_number VARCHAR(50),
    order_date DATE,
    customer_id VARCHAR(36),
    customer_name VARCHAR(255),
    status VARCHAR(50),
    fulfillment_status VARCHAR(50),
    invoicing_status VARCHAR(50),
    subtotal DECIMAL(19,2),
    tax_total DECIMAL(19,2),
    total DECIMAL(19,2),
    currency_code VARCHAR(10)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS stock_level_summary (
    item_id VARCHAR(36),
    item_code VARCHAR(100),
    item_name VARCHAR(255),
    warehouse_id VARCHAR(36),
    warehouse_name VARCHAR(255),
    qty_on_hand DECIMAL(19,4),
    qty_reserved DECIMAL(19,4),
    qty_available DECIMAL(19,4),
    reorder_point DECIMAL(19,4),
    below_reorder TINYINT(1),
    tenant_id VARCHAR(36),
    company_id VARCHAR(36),
    PRIMARY KEY (item_id, warehouse_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Report Definition metadata
CREATE TABLE IF NOT EXISTS report_definitions (
    id VARCHAR(36) PRIMARY KEY,
    tenant_id VARCHAR(36),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(50),
    report_type VARCHAR(50) NOT NULL,
    data_source VARCHAR(255),
    columns_json TEXT,
    default_filters_json TEXT,
    sql_query TEXT,
    permission_run_roles TEXT,
    permission_edit_roles TEXT,
    system_report TINYINT(1) DEFAULT 0,
    created_by VARCHAR(255),
    active TINYINT(1) DEFAULT 1,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_report_def_tenant ON report_definitions(tenant_id);

-- Permissions
INSERT IGNORE INTO permissions (id, module, action, description) VALUES
    ('00000000-0000-0000-0300-000000000001', 'REPORT', 'RUN', 'Run reports'),
    ('00000000-0000-0000-0300-000000000002', 'REPORT', 'DESIGN', 'Design reports'),
    ('00000000-0000-0000-0300-000000000003', 'REPORT', 'SQL', 'Custom SQL reports');

-- Assign basic report permissions
INSERT IGNORE INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM permissions p JOIN roles r ON r.tenant_id = '${tenant_id}' AND r.name = 'GENERAL:ADMIN'
LEFT JOIN role_permissions rp ON rp.role_id = r.id AND rp.permission_id = p.id
WHERE rp.role_id IS NULL AND p.module = 'REPORT';
