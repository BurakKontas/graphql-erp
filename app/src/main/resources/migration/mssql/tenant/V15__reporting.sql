-- V15__reporting.sql (Main Application - MSSQL)
-- UUID -> UNIQUEIDENTIFIER, BOOLEAN -> BIT, TEXT -> NVARCHAR(MAX),
-- NUMERIC -> DECIMAL, TIMESTAMPTZ -> DATETIMEOFFSET,
-- gen_random_uuid() -> NEWID(), NOW() -> GETUTCDATE()

CREATE TABLE [${schema}].[report_definitions] (
    [id]                  UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID(),
    [tenant_id]           UNIQUEIDENTIFIER NOT NULL,
    [name]                NVARCHAR(255)    NOT NULL,
    [description]         NVARCHAR(MAX)    NULL,
    [module]              NVARCHAR(50)     NOT NULL,
    [type]                NVARCHAR(50)     NOT NULL,
    [data_source]         NVARCHAR(255)    NULL,
    [columns_json]        NVARCHAR(MAX)    NULL,
    [filters_json]        NVARCHAR(MAX)    NULL,
    [sql_query]           NVARCHAR(MAX)    NULL,
    [required_permission] NVARCHAR(100)    NULL,
    [system_report]       BIT              NOT NULL DEFAULT 0,
    [active]              BIT              NOT NULL DEFAULT 1,
    [created_by]          NVARCHAR(255)    NULL,
    CONSTRAINT [PK_report_definitions] PRIMARY KEY ([id])
);
GO

CREATE INDEX [idx_report_definitions_tenant] ON [${schema}].[report_definitions] ([tenant_id]);
CREATE INDEX [idx_report_definitions_module] ON [${schema}].[report_definitions] ([tenant_id],[module]);
GO

CREATE TABLE [${schema}].[saved_reports] (
    [id]                   UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID(),
    [tenant_id]            UNIQUEIDENTIFIER NOT NULL,
    [report_definition_id] UNIQUEIDENTIFIER NOT NULL,
    [name]                 NVARCHAR(255)    NOT NULL,
    [saved_filters_json]   NVARCHAR(MAX)    NULL,
    [saved_sorts_json]     NVARCHAR(MAX)    NULL,
    [shared]               BIT              NOT NULL DEFAULT 0,
    [created_by]           NVARCHAR(255)    NULL,
    [created_at]           DATETIMEOFFSET   NULL DEFAULT SYSDATETIMEOFFSET(),
    CONSTRAINT [PK_saved_reports]  PRIMARY KEY ([id]),
    CONSTRAINT [FK_sr_report_def]  FOREIGN KEY ([report_definition_id])
        REFERENCES [${schema}].[report_definitions] ([id])
);
GO

CREATE INDEX [idx_saved_reports_tenant] ON [${schema}].[saved_reports] ([tenant_id]);
CREATE INDEX [idx_saved_reports_user]   ON [${schema}].[saved_reports] ([created_by],[tenant_id]);
GO

CREATE TABLE [${schema}].[scheduled_reports] (
    [id]                   UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID(),
    [tenant_id]            UNIQUEIDENTIFIER NOT NULL,
    [report_definition_id] UNIQUEIDENTIFIER NOT NULL,
    [name]                 NVARCHAR(255)    NOT NULL,
    [cron_expression]      NVARCHAR(100)    NOT NULL,
    [format]               NVARCHAR(20)     NOT NULL,
    [recipient_emails]     NVARCHAR(MAX)    NULL,
    [active]               BIT              NOT NULL DEFAULT 1,
    [last_run_at]          DATETIMEOFFSET   NULL,
    [next_run_at]          DATETIMEOFFSET   NULL,
    [created_by]           NVARCHAR(255)    NULL,
    CONSTRAINT [PK_scheduled_reports] PRIMARY KEY ([id]),
    CONSTRAINT [FK_scr_report_def]    FOREIGN KEY ([report_definition_id])
        REFERENCES [${schema}].[report_definitions] ([id])
);
GO

CREATE INDEX [idx_scheduled_reports_tenant] ON [${schema}].[scheduled_reports] ([tenant_id]);
CREATE INDEX [idx_scheduled_reports_active] ON [${schema}].[scheduled_reports] ([active]);
GO

-- ─── Reporting Snapshot Tables ───
CREATE TABLE [${schema}].[rpt_sales_orders] (
    [order_id]           UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]          UNIQUEIDENTIFIER NOT NULL,
    [company_id]         UNIQUEIDENTIFIER NULL,
    [order_number]       NVARCHAR(50)     NULL,
    [order_date]         DATE             NULL,
    [customer_id]        UNIQUEIDENTIFIER NULL,
    [customer_name]      NVARCHAR(255)    NULL,
    [status]             NVARCHAR(30)     NULL,
    [fulfillment_status] NVARCHAR(30)     NULL,
    [invoicing_status]   NVARCHAR(30)     NULL,
    [subtotal]           DECIMAL(19, 4)   NULL,
    [tax_total]          DECIMAL(19, 4)   NULL,
    [total]              DECIMAL(19, 4)   NULL,
    [currency_code]      NVARCHAR(10)     NULL,
    CONSTRAINT [PK_rpt_sales_orders] PRIMARY KEY ([order_id])
);
GO
CREATE INDEX [idx_rpt_sales_orders_tenant] ON [${schema}].[rpt_sales_orders] ([tenant_id],[company_id]);
CREATE INDEX [idx_rpt_sales_orders_status] ON [${schema}].[rpt_sales_orders] ([tenant_id],[status]);
GO

CREATE TABLE [${schema}].[rpt_inventory_stock] (
    [stock_id]       UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]      UNIQUEIDENTIFIER NOT NULL,
    [company_id]     UNIQUEIDENTIFIER NULL,
    [item_id]        UNIQUEIDENTIFIER NULL,
    [item_code]      NVARCHAR(50)     NULL,
    [item_name]      NVARCHAR(255)    NULL,
    [warehouse_id]   UNIQUEIDENTIFIER NULL,
    [warehouse_name] NVARCHAR(255)    NULL,
    [qty_on_hand]    DECIMAL(19, 4)   NULL,
    [qty_reserved]   DECIMAL(19, 4)   NULL,
    [qty_available]  DECIMAL(19, 4)   NULL,
    [reorder_point]  DECIMAL(19, 4)   NULL,
    [below_reorder]  BIT              NULL DEFAULT 0,
    CONSTRAINT [PK_rpt_inventory_stock] PRIMARY KEY ([stock_id])
);
GO
CREATE INDEX [idx_rpt_inventory_stock_tenant] ON [${schema}].[rpt_inventory_stock] ([tenant_id],[company_id]);
CREATE INDEX [idx_rpt_inventory_stock_item]   ON [${schema}].[rpt_inventory_stock] ([tenant_id],[item_id]);
GO

CREATE TABLE [${schema}].[rpt_finance_invoices] (
    [invoice_id]       UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]        UNIQUEIDENTIFIER NOT NULL,
    [company_id]       UNIQUEIDENTIFIER NULL,
    [invoice_number]   NVARCHAR(50)     NULL,
    [invoice_date]     DATE             NULL,
    [due_date]         DATE             NULL,
    [customer_id]      UNIQUEIDENTIFIER NULL,
    [customer_name]    NVARCHAR(255)    NULL,
    [currency_code]    NVARCHAR(10)     NULL,
    [total]            DECIMAL(19, 4)   NULL,
    [paid_amount]      DECIMAL(19, 4)   NULL,
    [remaining_amount] DECIMAL(19, 4)   NULL,
    [payment_status]   NVARCHAR(30)     NULL,
    CONSTRAINT [PK_rpt_finance_invoices] PRIMARY KEY ([invoice_id])
);
GO
CREATE INDEX [idx_rpt_finance_invoices_tenant] ON [${schema}].[rpt_finance_invoices] ([tenant_id],[company_id]);
CREATE INDEX [idx_rpt_finance_invoices_status] ON [${schema}].[rpt_finance_invoices] ([tenant_id],[payment_status]);
GO

CREATE TABLE [${schema}].[rpt_purchase_orders] (
    [po_id]         UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]     UNIQUEIDENTIFIER NOT NULL,
    [company_id]    UNIQUEIDENTIFIER NULL,
    [po_number]     NVARCHAR(50)     NULL,
    [order_date]    DATE             NULL,
    [vendor_id]     UNIQUEIDENTIFIER NULL,
    [vendor_name]   NVARCHAR(255)    NULL,
    [status]        NVARCHAR(30)     NULL,
    [total]         DECIMAL(19, 4)   NULL,
    [currency_code] NVARCHAR(10)     NULL,
    CONSTRAINT [PK_rpt_purchase_orders] PRIMARY KEY ([po_id])
);
GO
CREATE INDEX [idx_rpt_purchase_orders_tenant] ON [${schema}].[rpt_purchase_orders] ([tenant_id],[company_id]);
CREATE INDEX [idx_rpt_purchase_orders_status] ON [${schema}].[rpt_purchase_orders] ([tenant_id],[status]);
GO

CREATE TABLE [${schema}].[rpt_hr_employees] (
    [employee_id]     UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]       UNIQUEIDENTIFIER NOT NULL,
    [company_id]      UNIQUEIDENTIFIER NULL,
    [employee_number] NVARCHAR(50)     NULL,
    [full_name]       NVARCHAR(255)    NULL,
    [position_title]  NVARCHAR(255)    NULL,
    [department_name] NVARCHAR(255)    NULL,
    [employment_type] NVARCHAR(30)     NULL,
    [status]          NVARCHAR(30)     NULL,
    [hire_date]       DATE             NULL,
    [country_code]    NVARCHAR(10)     NULL,
    CONSTRAINT [PK_rpt_hr_employees] PRIMARY KEY ([employee_id])
);
GO
CREATE INDEX [idx_rpt_hr_employees_tenant] ON [${schema}].[rpt_hr_employees] ([tenant_id],[company_id]);
CREATE INDEX [idx_rpt_hr_employees_status] ON [${schema}].[rpt_hr_employees] ([tenant_id],[status]);
GO

CREATE TABLE [${schema}].[rpt_crm_opportunities] (
    [opportunity_id]      UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]           UNIQUEIDENTIFIER NOT NULL,
    [company_id]          UNIQUEIDENTIFIER NULL,
    [opportunity_number]  NVARCHAR(50)     NULL,
    [title]               NVARCHAR(255)    NULL,
    [stage]               NVARCHAR(30)     NULL,
    [probability]         DECIMAL(5, 2)    NULL,
    [expected_value]      DECIMAL(19, 4)   NULL,
    [currency_code]       NVARCHAR(10)     NULL,
    [expected_close_date] DATE             NULL,
    [owner_id]            NVARCHAR(255)    NULL,
    CONSTRAINT [PK_rpt_crm_opportunities] PRIMARY KEY ([opportunity_id])
);
GO
CREATE INDEX [idx_rpt_crm_opportunities_tenant] ON [${schema}].[rpt_crm_opportunities] ([tenant_id],[company_id]);
CREATE INDEX [idx_rpt_crm_opportunities_stage]  ON [${schema}].[rpt_crm_opportunities] ([tenant_id],[stage]);
GO

CREATE TABLE [${schema}].[rpt_shipments] (
    [shipment_id]       UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]         UNIQUEIDENTIFIER NOT NULL,
    [company_id]        UNIQUEIDENTIFIER NULL,
    [shipment_number]   NVARCHAR(50)     NULL,
    [delivery_order_id] UNIQUEIDENTIFIER NULL,
    [warehouse_id]      UNIQUEIDENTIFIER NULL,
    [warehouse_name]    NVARCHAR(255)    NULL,
    [status]            NVARCHAR(30)     NULL,
    [carrier_name]      NVARCHAR(255)    NULL,
    [tracking_number]   NVARCHAR(255)    NULL,
    [dispatched_at]     DATETIMEOFFSET   NULL,
    [delivered_at]      DATETIMEOFFSET   NULL,
    CONSTRAINT [PK_rpt_shipments] PRIMARY KEY ([shipment_id])
);
GO
CREATE INDEX [idx_rpt_shipments_tenant] ON [${schema}].[rpt_shipments] ([tenant_id],[company_id]);
CREATE INDEX [idx_rpt_shipments_status] ON [${schema}].[rpt_shipments] ([tenant_id],[status]);
GO

-- ─── Report Permissions ───
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES (NEWID(),'REPORT','READ',     'View reports');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES (NEWID(),'REPORT','DESIGN',   'Design and manage report definitions');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES (NEWID(),'REPORT','SQL_ADMIN','Execute raw SQL reports');
GO

-- ─── Role assignments for REPORT ───
MERGE INTO [${schema}].[role_permissions] AS tgt
USING (SELECT CONVERT(UNIQUEIDENTIFIER,HASHBYTES('MD5','ADMIN_${tenant_id}')) AS role_id, p.[id] AS permission_id FROM [${schema}].[permissions] p WHERE p.[module] = 'REPORT') AS src
ON tgt.[role_id]=src.[role_id] AND tgt.[permission_id]=src.[permission_id]
WHEN NOT MATCHED THEN INSERT([role_id],[permission_id]) VALUES(src.[role_id],src.[permission_id]);

MERGE INTO [${schema}].[role_permissions] AS tgt
USING (SELECT CONVERT(UNIQUEIDENTIFIER,HASHBYTES('MD5','MANAGER_${tenant_id}')) AS role_id, p.[id] AS permission_id FROM [${schema}].[permissions] p WHERE p.[module] = 'REPORT' AND p.[action] IN ('READ','DESIGN')) AS src
ON tgt.[role_id]=src.[role_id] AND tgt.[permission_id]=src.[permission_id]
WHEN NOT MATCHED THEN INSERT([role_id],[permission_id]) VALUES(src.[role_id],src.[permission_id]);

MERGE INTO [${schema}].[role_permissions] AS tgt
USING (SELECT CONVERT(UNIQUEIDENTIFIER,HASHBYTES('MD5','USER_${tenant_id}')) AS role_id, p.[id] AS permission_id FROM [${schema}].[permissions] p WHERE p.[module] = 'REPORT' AND p.[action] = 'READ') AS src
ON tgt.[role_id]=src.[role_id] AND tgt.[permission_id]=src.[permission_id]
WHEN NOT MATCHED THEN INSERT([role_id],[permission_id]) VALUES(src.[role_id],src.[permission_id]);
GO

-- ─── System Report Definitions ───
DECLARE @global_tenant UNIQUEIDENTIFIER = '00000000-0000-0000-0000-000000000000';
INSERT INTO [${schema}].[report_definitions]([id],[tenant_id],[name],[description],[module],[type],[data_source],[required_permission],[system_report],[active])
VALUES
    (NEWID(), @global_tenant, 'Sales Order List',       'List of all sales orders',                  'SALES',     'PREDEFINED', 'rpt_sales_orders',      'REPORT:READ', 1, 1),
    (NEWID(), @global_tenant, 'Inventory Stock Levels', 'Current stock levels by item and warehouse', 'INVENTORY', 'PREDEFINED', 'rpt_inventory_stock',   'REPORT:READ', 1, 1),
    (NEWID(), @global_tenant, 'Sales Invoice Summary',  'Sales invoices with payment status',         'FINANCE',   'PREDEFINED', 'rpt_finance_invoices',  'REPORT:READ', 1, 1),
    (NEWID(), @global_tenant, 'Purchase Order List',    'List of all purchase orders',                'PURCHASE',  'PREDEFINED', 'rpt_purchase_orders',   'REPORT:READ', 1, 1),
    (NEWID(), @global_tenant, 'Employee Directory',     'All employees with their positions',         'HR',        'PREDEFINED', 'rpt_hr_employees',      'REPORT:READ', 1, 1),
    (NEWID(), @global_tenant, 'CRM Pipeline',           'Opportunities by stage',                     'CRM',       'PREDEFINED', 'rpt_crm_opportunities', 'REPORT:READ', 1, 1),
    (NEWID(), @global_tenant, 'Shipment Tracker',       'Shipments and delivery status',              'SHIPMENT',  'PREDEFINED', 'rpt_shipments',         'REPORT:READ', 1, 1);
GO
