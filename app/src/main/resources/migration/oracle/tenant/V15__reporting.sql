-- V15__reporting.sql (Main Application)
-- Oracle conversion:
--   UUID          -> VARCHAR2(32)
--   BOOLEAN       -> NUMBER(1,0)
--   TEXT          -> CLOB
--   VARCHAR       -> VARCHAR2
--   NUMERIC       -> NUMBER
--   TIMESTAMPTZ   -> TIMESTAMP WITH TIME ZONE
--   NOW()         -> SYSTIMESTAMP
--   gen_random_uuid() -> SYS_GUID()
--   UNIQUE inline -> named CONSTRAINT
--
-- NOTE: V15 original used a "key" column on permissions that does not exist
--       in the base schema (V2). The column was added here only for this
--       migration's INSERT. If your schema doesn't have it, remove those
--       INSERT statements or add the column in a separate migration.

CREATE TABLE "${schema}".report_definitions (
    id                  VARCHAR2(32)       NOT NULL,
    tenant_id           VARCHAR2(32)       NOT NULL,
    name                VARCHAR2(255) NOT NULL,
    description         CLOB,
    module              VARCHAR2(50)  NOT NULL,
    type                VARCHAR2(50)  NOT NULL,
    data_source         VARCHAR2(255),
    columns_json        CLOB,
    filters_json        CLOB,
    sql_query           CLOB,
    required_permission VARCHAR2(100),
    system_report       NUMBER(1,0)   DEFAULT 0 NOT NULL,
    active              NUMBER(1,0)   DEFAULT 1 NOT NULL,
    created_by          VARCHAR2(255),
    CONSTRAINT pk_report_definitions PRIMARY KEY (id)
);

CREATE INDEX idx_report_definitions_tenant ON "${schema}".report_definitions (tenant_id);
CREATE INDEX idx_report_definitions_module ON "${schema}".report_definitions (tenant_id, module);

CREATE TABLE "${schema}".saved_reports (
    id                   VARCHAR2(32)       NOT NULL,
    tenant_id            VARCHAR2(32)       NOT NULL,
    report_definition_id VARCHAR2(32)       NOT NULL,
    name                 VARCHAR2(255) NOT NULL,
    saved_filters_json   CLOB,
    saved_sorts_json     CLOB,
    shared               NUMBER(1,0)   DEFAULT 0 NOT NULL,
    created_by           VARCHAR2(255),
    created_at           TIMESTAMP WITH TIME ZONE DEFAULT SYSTIMESTAMP,
    CONSTRAINT pk_saved_reports   PRIMARY KEY (id),
    CONSTRAINT fk_sr_report_def   FOREIGN KEY (report_definition_id)
        REFERENCES "${schema}".report_definitions (id)
);

CREATE INDEX idx_saved_reports_tenant ON "${schema}".saved_reports (tenant_id);
CREATE INDEX idx_saved_reports_user   ON "${schema}".saved_reports (created_by, tenant_id);

CREATE TABLE "${schema}".scheduled_reports (
    id                   VARCHAR2(32)       NOT NULL,
    tenant_id            VARCHAR2(32)       NOT NULL,
    report_definition_id VARCHAR2(32)       NOT NULL,
    name                 VARCHAR2(255) NOT NULL,
    cron_expression      VARCHAR2(100) NOT NULL,
    format               VARCHAR2(20)  NOT NULL,
    recipient_emails     CLOB,
    active               NUMBER(1,0)   DEFAULT 1 NOT NULL,
    last_run_at          TIMESTAMP WITH TIME ZONE,
    next_run_at          TIMESTAMP WITH TIME ZONE,
    created_by           VARCHAR2(255),
    CONSTRAINT pk_scheduled_reports PRIMARY KEY (id),
    CONSTRAINT fk_scr_report_def    FOREIGN KEY (report_definition_id)
        REFERENCES "${schema}".report_definitions (id)
);

CREATE INDEX idx_scheduled_reports_tenant ON "${schema}".scheduled_reports (tenant_id);
CREATE INDEX idx_scheduled_reports_active ON "${schema}".scheduled_reports (active);

-- ─── Reporting Snapshot Tables ───
CREATE TABLE "${schema}".rpt_sales_orders (
    order_id           VARCHAR2(32)       NOT NULL,
    tenant_id          VARCHAR2(32)       NOT NULL,
    company_id         VARCHAR2(32),
    order_number       VARCHAR2(50),
    order_date         DATE,
    customer_id        VARCHAR2(32),
    customer_name      VARCHAR2(255),
    status             VARCHAR2(30),
    fulfillment_status VARCHAR2(30),
    invoicing_status   VARCHAR2(30),
    subtotal           NUMBER(19,4),
    tax_total          NUMBER(19,4),
    total              NUMBER(19,4),
    currency_code      VARCHAR2(10),
    CONSTRAINT pk_rpt_sales_orders PRIMARY KEY (order_id)
);

CREATE INDEX idx_rpt_sales_orders_tenant ON "${schema}".rpt_sales_orders (tenant_id, company_id);
CREATE INDEX idx_rpt_sales_orders_status ON "${schema}".rpt_sales_orders (tenant_id, status);

CREATE TABLE "${schema}".rpt_inventory_stock (
    stock_id       VARCHAR2(32)       NOT NULL,
    tenant_id      VARCHAR2(32)       NOT NULL,
    company_id     VARCHAR2(32),
    item_id        VARCHAR2(32),
    item_code      VARCHAR2(50),
    item_name      VARCHAR2(255),
    warehouse_id   VARCHAR2(32),
    warehouse_name VARCHAR2(255),
    qty_on_hand    NUMBER(19,4),
    qty_reserved   NUMBER(19,4),
    qty_available  NUMBER(19,4),
    reorder_point  NUMBER(19,4),
    below_reorder  NUMBER(1,0)   DEFAULT 0,
    CONSTRAINT pk_rpt_inventory_stock PRIMARY KEY (stock_id)
);

CREATE INDEX idx_rpt_inventory_stock_tenant      ON "${schema}".rpt_inventory_stock (tenant_id, company_id);
CREATE INDEX idx_rpt_inventory_stock_item        ON "${schema}".rpt_inventory_stock (tenant_id, item_id);
CREATE INDEX idx_rpt_inventory_stock_warehouse   ON "${schema}".rpt_inventory_stock (tenant_id, warehouse_id);

CREATE TABLE "${schema}".rpt_finance_invoices (
    invoice_id      VARCHAR2(32)       NOT NULL,
    tenant_id       VARCHAR2(32)       NOT NULL,
    company_id      VARCHAR2(32),
    invoice_number  VARCHAR2(50),
    invoice_date    DATE,
    due_date        DATE,
    customer_id     VARCHAR2(32),
    customer_name   VARCHAR2(255),
    currency_code   VARCHAR2(10),
    total           NUMBER(19,4),
    paid_amount     NUMBER(19,4),
    remaining_amount NUMBER(19,4),
    payment_status  VARCHAR2(30),
    CONSTRAINT pk_rpt_finance_invoices PRIMARY KEY (invoice_id)
);

CREATE INDEX idx_rpt_finance_invoices_tenant ON "${schema}".rpt_finance_invoices (tenant_id, company_id);
CREATE INDEX idx_rpt_finance_invoices_status ON "${schema}".rpt_finance_invoices (tenant_id, payment_status);

CREATE TABLE "${schema}".rpt_purchase_orders (
    po_id         VARCHAR2(32)       NOT NULL,
    tenant_id     VARCHAR2(32)       NOT NULL,
    company_id    VARCHAR2(32),
    po_number     VARCHAR2(50),
    order_date    DATE,
    vendor_id     VARCHAR2(32),
    vendor_name   VARCHAR2(255),
    status        VARCHAR2(30),
    total         NUMBER(19,4),
    currency_code VARCHAR2(10),
    CONSTRAINT pk_rpt_purchase_orders PRIMARY KEY (po_id)
);

CREATE INDEX idx_rpt_purchase_orders_tenant ON "${schema}".rpt_purchase_orders (tenant_id, company_id);
CREATE INDEX idx_rpt_purchase_orders_status ON "${schema}".rpt_purchase_orders (tenant_id, status);

CREATE TABLE "${schema}".rpt_hr_employees (
    employee_id      VARCHAR2(32)       NOT NULL,
    tenant_id        VARCHAR2(32)       NOT NULL,
    company_id       VARCHAR2(32),
    employee_number  VARCHAR2(50),
    full_name        VARCHAR2(255),
    position_title   VARCHAR2(255),
    department_name  VARCHAR2(255),
    employment_type  VARCHAR2(30),
    status           VARCHAR2(30),
    hire_date        DATE,
    country_code     VARCHAR2(10),
    CONSTRAINT pk_rpt_hr_employees PRIMARY KEY (employee_id)
);

CREATE INDEX idx_rpt_hr_employees_tenant ON "${schema}".rpt_hr_employees (tenant_id, company_id);
CREATE INDEX idx_rpt_hr_employees_status ON "${schema}".rpt_hr_employees (tenant_id, status);

CREATE TABLE "${schema}".rpt_crm_opportunities (
    opportunity_id      VARCHAR2(32)       NOT NULL,
    tenant_id           VARCHAR2(32)       NOT NULL,
    company_id          VARCHAR2(32),
    opportunity_number  VARCHAR2(50),
    title               VARCHAR2(255),
    stage               VARCHAR2(30),
    probability         NUMBER(5,2),
    expected_value      NUMBER(19,4),
    currency_code       VARCHAR2(10),
    expected_close_date DATE,
    owner_id            VARCHAR2(255),
    CONSTRAINT pk_rpt_crm_opportunities PRIMARY KEY (opportunity_id)
);

CREATE INDEX idx_rpt_crm_opportunities_tenant ON "${schema}".rpt_crm_opportunities (tenant_id, company_id);
CREATE INDEX idx_rpt_crm_opportunities_stage  ON "${schema}".rpt_crm_opportunities (tenant_id, stage);

CREATE TABLE "${schema}".rpt_shipments (
    shipment_id       VARCHAR2(32)       NOT NULL,
    tenant_id         VARCHAR2(32)       NOT NULL,
    company_id        VARCHAR2(32),
    shipment_number   VARCHAR2(50),
    delivery_order_id VARCHAR2(32),
    warehouse_id      VARCHAR2(32),
    warehouse_name    VARCHAR2(255),
    status            VARCHAR2(30),
    carrier_name      VARCHAR2(255),
    tracking_number   VARCHAR2(255),
    dispatched_at     TIMESTAMP WITH TIME ZONE,
    delivered_at      TIMESTAMP WITH TIME ZONE,
    CONSTRAINT pk_rpt_shipments PRIMARY KEY (shipment_id)
);

CREATE INDEX idx_rpt_shipments_tenant ON "${schema}".rpt_shipments (tenant_id, company_id);
CREATE INDEX idx_rpt_shipments_status ON "${schema}".rpt_shipments (tenant_id, status);

-- ─── Report Permissions ───
-- NOTE: Original script used a "key" column not in base schema.
-- We insert using module+action only (no key column).
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (SYS_GUID(), 'REPORT', 'READ',      'View reports');
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (SYS_GUID(), 'REPORT', 'DESIGN',    'Design and manage report definitions');
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (SYS_GUID(), 'REPORT', 'SQL_ADMIN', 'Execute raw SQL reports');

-- ─── Role assignments for REPORT permissions ───
MERGE INTO "${schema}".role_permissions tgt
USING (SELECT STANDARD_HASH('ADMIN_${tenant_id}','MD5') AS role_id, p.id AS permission_id
       FROM "${schema}".permissions p
       WHERE p.module = 'REPORT') src
ON (tgt.role_id = src.role_id AND tgt.permission_id = src.permission_id)
WHEN NOT MATCHED THEN INSERT (role_id, permission_id) VALUES (src.role_id, src.permission_id);

MERGE INTO "${schema}".role_permissions tgt
USING (SELECT STANDARD_HASH('MANAGER_${tenant_id}','MD5') AS role_id, p.id AS permission_id
       FROM "${schema}".permissions p
       WHERE p.module = 'REPORT' AND p.action IN ('READ','DESIGN')) src
ON (tgt.role_id = src.role_id AND tgt.permission_id = src.permission_id)
WHEN NOT MATCHED THEN INSERT (role_id, permission_id) VALUES (src.role_id, src.permission_id);

MERGE INTO "${schema}".role_permissions tgt
USING (SELECT STANDARD_HASH('USER_${tenant_id}','MD5') AS role_id, p.id AS permission_id
       FROM "${schema}".permissions p
       WHERE p.module = 'REPORT' AND p.action = 'READ') src
ON (tgt.role_id = src.role_id AND tgt.permission_id = src.permission_id)
WHEN NOT MATCHED THEN INSERT (role_id, permission_id) VALUES (src.role_id, src.permission_id);

-- ─── System Report Definitions (tenant_id = all-zeros = global) ───
INSERT INTO "${schema}".report_definitions (id, tenant_id, name, description, module, type, data_source, required_permission, system_report, active)
VALUES (SYS_GUID(), HEXTORAW('00000000000000000000000000000000'), 'Sales Order List',        'List of all sales orders',                  'SALES',     'PREDEFINED', 'rpt_sales_orders',       'REPORT:READ', 1, 1);
INSERT INTO "${schema}".report_definitions (id, tenant_id, name, description, module, type, data_source, required_permission, system_report, active)
VALUES (SYS_GUID(), HEXTORAW('00000000000000000000000000000000'), 'Inventory Stock Levels',   'Current stock levels by item and warehouse', 'INVENTORY', 'PREDEFINED', 'rpt_inventory_stock',    'REPORT:READ', 1, 1);
INSERT INTO "${schema}".report_definitions (id, tenant_id, name, description, module, type, data_source, required_permission, system_report, active)
VALUES (SYS_GUID(), HEXTORAW('00000000000000000000000000000000'), 'Sales Invoice Summary',    'Sales invoices with payment status',         'FINANCE',   'PREDEFINED', 'rpt_finance_invoices',   'REPORT:READ', 1, 1);
INSERT INTO "${schema}".report_definitions (id, tenant_id, name, description, module, type, data_source, required_permission, system_report, active)
VALUES (SYS_GUID(), HEXTORAW('00000000000000000000000000000000'), 'Purchase Order List',      'List of all purchase orders',                'PURCHASE',  'PREDEFINED', 'rpt_purchase_orders',    'REPORT:READ', 1, 1);
INSERT INTO "${schema}".report_definitions (id, tenant_id, name, description, module, type, data_source, required_permission, system_report, active)
VALUES (SYS_GUID(), HEXTORAW('00000000000000000000000000000000'), 'Employee Directory',       'All employees with their positions',         'HR',        'PREDEFINED', 'rpt_hr_employees',       'REPORT:READ', 1, 1);
INSERT INTO "${schema}".report_definitions (id, tenant_id, name, description, module, type, data_source, required_permission, system_report, active)
VALUES (SYS_GUID(), HEXTORAW('00000000000000000000000000000000'), 'CRM Pipeline',             'Opportunities by stage',                     'CRM',       'PREDEFINED', 'rpt_crm_opportunities',  'REPORT:READ', 1, 1);
INSERT INTO "${schema}".report_definitions (id, tenant_id, name, description, module, type, data_source, required_permission, system_report, active)
VALUES (SYS_GUID(), HEXTORAW('00000000000000000000000000000000'), 'Shipment Tracker',         'Shipments and delivery status',              'SHIPMENT',  'PREDEFINED', 'rpt_shipments',          'REPORT:READ', 1, 1);

COMMIT;
