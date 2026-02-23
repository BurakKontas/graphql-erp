-- V10__purchase.sql (Main Application - MSSQL)

-- ─── Purchase Requests ───
CREATE TABLE [${schema}].[purchase_requests] (
    [id]             UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]      UNIQUEIDENTIFIER NOT NULL,
    [company_id]     UNIQUEIDENTIFIER NOT NULL,
    [request_number] NVARCHAR(20)     NOT NULL,
    [requested_by]   NVARCHAR(255)    NULL,
    [approved_by]    NVARCHAR(255)    NULL,
    [request_date]   DATE             NOT NULL,
    [needed_by]      DATE             NULL,
    [status]         NVARCHAR(50)     NOT NULL DEFAULT 'DRAFT',
    [notes]          NVARCHAR(MAX)    NULL,
    CONSTRAINT [PK_purchase_requests] PRIMARY KEY ([id]),
    CONSTRAINT [FK_pr_company]        FOREIGN KEY ([company_id]) REFERENCES [${schema}].[companies] ([id]),
    CONSTRAINT [UQ_pr_number]         UNIQUE ([tenant_id], [request_number])
);
GO
CREATE INDEX [idx_purchase_request_tenant_id]  ON [${schema}].[purchase_requests] ([tenant_id]);
CREATE INDEX [idx_purchase_request_company_id] ON [${schema}].[purchase_requests] ([company_id]);
CREATE INDEX [idx_purchase_request_status]     ON [${schema}].[purchase_requests] ([status]);
GO

CREATE TABLE [${schema}].[purchase_request_lines] (
    [id]                  UNIQUEIDENTIFIER NOT NULL,
    [request_id]          UNIQUEIDENTIFIER NOT NULL,
    [item_id]             NVARCHAR(255)    NULL,
    [item_description]    NVARCHAR(500)    NOT NULL,
    [unit_code]           NVARCHAR(50)     NOT NULL,
    [quantity]            DECIMAL(19, 4)   NOT NULL,
    [preferred_vendor_id] NVARCHAR(255)    NULL,
    [notes]               NVARCHAR(MAX)    NULL,
    CONSTRAINT [PK_purchase_request_lines] PRIMARY KEY ([id]),
    CONSTRAINT [FK_prl_request]            FOREIGN KEY ([request_id])
        REFERENCES [${schema}].[purchase_requests] ([id]) ON DELETE CASCADE
);
GO
CREATE INDEX [idx_pr_line_request_id] ON [${schema}].[purchase_request_lines] ([request_id]);
GO

-- ─── Purchase Orders ───
CREATE TABLE [${schema}].[purchase_orders] (
    [id]                     UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]              UNIQUEIDENTIFIER NOT NULL,
    [company_id]             UNIQUEIDENTIFIER NOT NULL,
    [order_number]           NVARCHAR(20)     NOT NULL,
    [request_id]             NVARCHAR(255)    NULL,
    [vendor_id]              NVARCHAR(255)    NOT NULL,
    [vendor_name]            NVARCHAR(500)    NULL,
    [order_date]             DATE             NOT NULL,
    [expected_delivery_date] DATE             NULL,
    [currency_code]          NVARCHAR(3)      NULL,
    [payment_term_code]      NVARCHAR(50)     NULL,
    [address_line1]          NVARCHAR(500)    NULL,
    [address_line2]          NVARCHAR(500)    NULL,
    [city]                   NVARCHAR(255)    NULL,
    [state_or_province]      NVARCHAR(255)    NULL,
    [postal_code]            NVARCHAR(50)     NULL,
    [country_code]           NVARCHAR(2)      NULL,
    [status]                 NVARCHAR(50)     NOT NULL DEFAULT 'DRAFT',
    [subtotal]               DECIMAL(19, 2)   NOT NULL DEFAULT 0,
    [tax_total]              DECIMAL(19, 2)   NOT NULL DEFAULT 0,
    [total]                  DECIMAL(19, 2)   NOT NULL DEFAULT 0,
    CONSTRAINT [PK_purchase_orders] PRIMARY KEY ([id]),
    CONSTRAINT [FK_po_company]      FOREIGN KEY ([company_id]) REFERENCES [${schema}].[companies] ([id]),
    CONSTRAINT [UQ_po_number]       UNIQUE ([tenant_id], [order_number])
);
GO
CREATE INDEX [idx_purchase_order_tenant_id]  ON [${schema}].[purchase_orders] ([tenant_id]);
CREATE INDEX [idx_purchase_order_company_id] ON [${schema}].[purchase_orders] ([company_id]);
CREATE INDEX [idx_purchase_order_vendor_id]  ON [${schema}].[purchase_orders] ([vendor_id]);
CREATE INDEX [idx_purchase_order_status]     ON [${schema}].[purchase_orders] ([status]);
GO

CREATE TABLE [${schema}].[purchase_order_lines] (
    [id]                 UNIQUEIDENTIFIER NOT NULL,
    [order_id]           UNIQUEIDENTIFIER NOT NULL,
    [request_line_id]    NVARCHAR(255)    NULL,
    [item_id]            NVARCHAR(255)    NULL,
    [item_description]   NVARCHAR(500)    NOT NULL,
    [unit_code]          NVARCHAR(50)     NOT NULL,
    [ordered_qty]        DECIMAL(19, 4)   NOT NULL,
    [received_qty]       DECIMAL(19, 4)   NOT NULL DEFAULT 0,
    [unit_price]         DECIMAL(19, 6)   NOT NULL,
    [tax_code]           NVARCHAR(50)     NULL,
    [tax_rate]           DECIMAL(10, 4)   NULL,
    [line_total]         DECIMAL(19, 2)   NOT NULL,
    [tax_amount]         DECIMAL(19, 2)   NOT NULL,
    [expense_account_id] NVARCHAR(255)    NULL,
    CONSTRAINT [PK_purchase_order_lines] PRIMARY KEY ([id]),
    CONSTRAINT [FK_pol_order]            FOREIGN KEY ([order_id])
        REFERENCES [${schema}].[purchase_orders] ([id]) ON DELETE CASCADE
);
GO
CREATE INDEX [idx_po_line_order_id] ON [${schema}].[purchase_order_lines] ([order_id]);
GO

-- ─── Goods Receipts ───
CREATE TABLE [${schema}].[goods_receipts] (
    [id]                   UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]            UNIQUEIDENTIFIER NOT NULL,
    [company_id]           UNIQUEIDENTIFIER NOT NULL,
    [receipt_number]       NVARCHAR(20)     NOT NULL,
    [purchase_order_id]    NVARCHAR(255)    NULL,
    [vendor_id]            NVARCHAR(255)    NULL,
    [warehouse_id]         NVARCHAR(255)    NULL,
    [receipt_date]         DATE             NOT NULL,
    [status]               NVARCHAR(50)     NOT NULL DEFAULT 'DRAFT',
    [vendor_delivery_note] NVARCHAR(500)    NULL,
    CONSTRAINT [PK_goods_receipts] PRIMARY KEY ([id]),
    CONSTRAINT [FK_gr_company]     FOREIGN KEY ([company_id]) REFERENCES [${schema}].[companies] ([id]),
    CONSTRAINT [UQ_gr_number]      UNIQUE ([tenant_id], [receipt_number])
);
GO
CREATE INDEX [idx_goods_receipt_tenant_id]  ON [${schema}].[goods_receipts] ([tenant_id]);
CREATE INDEX [idx_goods_receipt_company_id] ON [${schema}].[goods_receipts] ([company_id]);
CREATE INDEX [idx_goods_receipt_po_id]      ON [${schema}].[goods_receipts] ([purchase_order_id]);
CREATE INDEX [idx_goods_receipt_status]     ON [${schema}].[goods_receipts] ([status]);
GO

CREATE TABLE [${schema}].[goods_receipt_lines] (
    [id]               UNIQUEIDENTIFIER NOT NULL,
    [receipt_id]       UNIQUEIDENTIFIER NOT NULL,
    [po_line_id]       NVARCHAR(255)    NULL,
    [item_id]          NVARCHAR(255)    NULL,
    [item_description] NVARCHAR(500)    NULL,
    [unit_code]        NVARCHAR(50)     NOT NULL,
    [quantity]         DECIMAL(19, 4)   NOT NULL,
    [batch_note]       NVARCHAR(500)    NULL,
    CONSTRAINT [PK_goods_receipt_lines] PRIMARY KEY ([id]),
    CONSTRAINT [FK_grl_receipt]         FOREIGN KEY ([receipt_id])
        REFERENCES [${schema}].[goods_receipts] ([id]) ON DELETE CASCADE
);
GO
CREATE INDEX [idx_gr_line_receipt_id] ON [${schema}].[goods_receipt_lines] ([receipt_id]);
GO

-- ─── Vendor Invoices ───
CREATE TABLE [${schema}].[vendor_invoices] (
    [id]                   UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]            UNIQUEIDENTIFIER NOT NULL,
    [company_id]           UNIQUEIDENTIFIER NOT NULL,
    [invoice_number]       NVARCHAR(25)     NOT NULL,
    [vendor_invoice_ref]   NVARCHAR(255)    NULL,
    [purchase_order_id]    NVARCHAR(255)    NULL,
    [vendor_id]            NVARCHAR(255)    NULL,
    [vendor_name]          NVARCHAR(500)    NULL,
    [accounting_period_id] NVARCHAR(255)    NULL,
    [invoice_date]         DATE             NOT NULL,
    [due_date]             DATE             NULL,
    [currency_code]        NVARCHAR(3)      NULL,
    [exchange_rate]        DECIMAL(19, 6)   NULL,
    [status]               NVARCHAR(50)     NOT NULL DEFAULT 'DRAFT',
    [subtotal]             DECIMAL(19, 2)   NOT NULL DEFAULT 0,
    [tax_total]            DECIMAL(19, 2)   NOT NULL DEFAULT 0,
    [total]                DECIMAL(19, 2)   NOT NULL DEFAULT 0,
    [paid_amount]          DECIMAL(19, 2)   NOT NULL DEFAULT 0,
    CONSTRAINT [PK_vendor_invoices] PRIMARY KEY ([id]),
    CONSTRAINT [FK_vi_company]      FOREIGN KEY ([company_id]) REFERENCES [${schema}].[companies] ([id]),
    CONSTRAINT [UQ_vi_number]       UNIQUE ([tenant_id], [invoice_number])
);
GO
CREATE INDEX [idx_vendor_invoice_tenant_id]  ON [${schema}].[vendor_invoices] ([tenant_id]);
CREATE INDEX [idx_vendor_invoice_company_id] ON [${schema}].[vendor_invoices] ([company_id]);
CREATE INDEX [idx_vendor_invoice_vendor_id]  ON [${schema}].[vendor_invoices] ([vendor_id]);
CREATE INDEX [idx_vendor_invoice_status]     ON [${schema}].[vendor_invoices] ([status]);
GO

CREATE TABLE [${schema}].[vendor_invoice_lines] (
    [id]                  UNIQUEIDENTIFIER NOT NULL,
    [invoice_id]          UNIQUEIDENTIFIER NOT NULL,
    [po_line_id]          NVARCHAR(255)    NULL,
    [item_id]             NVARCHAR(255)    NULL,
    [item_description]    NVARCHAR(500)    NULL,
    [unit_code]           NVARCHAR(50)     NOT NULL,
    [quantity]            DECIMAL(19, 4)   NOT NULL,
    [unit_price]          DECIMAL(19, 6)   NOT NULL,
    [tax_code]            NVARCHAR(50)     NULL,
    [tax_rate]            DECIMAL(10, 4)   NULL,
    [line_total]          DECIMAL(19, 2)   NOT NULL,
    [tax_amount]          DECIMAL(19, 2)   NOT NULL,
    [line_total_with_tax] DECIMAL(19, 2)   NOT NULL,
    [account_id]          NVARCHAR(255)    NULL,
    CONSTRAINT [PK_vendor_invoice_lines] PRIMARY KEY ([id]),
    CONSTRAINT [FK_vil_invoice]          FOREIGN KEY ([invoice_id])
        REFERENCES [${schema}].[vendor_invoices] ([id]) ON DELETE CASCADE
);
GO
CREATE INDEX [idx_vi_line_invoice_id] ON [${schema}].[vendor_invoice_lines] ([invoice_id]);
GO

-- ─── Purchase Returns ───
CREATE TABLE [${schema}].[purchase_returns] (
    [id]                UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]         UNIQUEIDENTIFIER NOT NULL,
    [company_id]        UNIQUEIDENTIFIER NOT NULL,
    [return_number]     NVARCHAR(25)     NOT NULL,
    [purchase_order_id] NVARCHAR(255)    NULL,
    [goods_receipt_id]  NVARCHAR(255)    NULL,
    [vendor_id]         NVARCHAR(255)    NULL,
    [warehouse_id]      NVARCHAR(255)    NULL,
    [return_date]       DATE             NOT NULL,
    [reason]            NVARCHAR(50)     NOT NULL,
    [status]            NVARCHAR(50)     NOT NULL DEFAULT 'DRAFT',
    CONSTRAINT [PK_purchase_returns] PRIMARY KEY ([id]),
    CONSTRAINT [FK_pret_company]     FOREIGN KEY ([company_id]) REFERENCES [${schema}].[companies] ([id]),
    CONSTRAINT [UQ_pret_number]      UNIQUE ([tenant_id], [return_number])
);
GO
CREATE INDEX [idx_purchase_return_tenant_id]  ON [${schema}].[purchase_returns] ([tenant_id]);
CREATE INDEX [idx_purchase_return_company_id] ON [${schema}].[purchase_returns] ([company_id]);
CREATE INDEX [idx_purchase_return_po_id]      ON [${schema}].[purchase_returns] ([purchase_order_id]);
CREATE INDEX [idx_purchase_return_status]     ON [${schema}].[purchase_returns] ([status]);
GO

CREATE TABLE [${schema}].[purchase_return_lines] (
    [id]               UNIQUEIDENTIFIER NOT NULL,
    [return_id]        UNIQUEIDENTIFIER NOT NULL,
    [receipt_line_id]  NVARCHAR(255)    NULL,
    [item_id]          NVARCHAR(255)    NULL,
    [item_description] NVARCHAR(500)    NULL,
    [unit_code]        NVARCHAR(50)     NOT NULL,
    [quantity]         DECIMAL(19, 4)   NOT NULL,
    [line_reason]      NVARCHAR(500)    NULL,
    CONSTRAINT [PK_purchase_return_lines] PRIMARY KEY ([id]),
    CONSTRAINT [FK_prl_return]            FOREIGN KEY ([return_id])
        REFERENCES [${schema}].[purchase_returns] ([id]) ON DELETE CASCADE
);
GO
CREATE INDEX [idx_prl_line_return_id] ON [${schema}].[purchase_return_lines] ([return_id]);
GO

-- ─── Vendor Catalogs ───
CREATE TABLE [${schema}].[vendor_catalogs] (
    [id]            UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]     UNIQUEIDENTIFIER NOT NULL,
    [company_id]    UNIQUEIDENTIFIER NOT NULL,
    [vendor_id]     NVARCHAR(255)    NOT NULL,
    [vendor_name]   NVARCHAR(500)    NULL,
    [currency_code] NVARCHAR(3)      NULL,
    [valid_from]    DATE             NOT NULL,
    [valid_to]      DATE             NULL,
    [active]        BIT              NOT NULL DEFAULT 1,
    CONSTRAINT [PK_vendor_catalogs] PRIMARY KEY ([id]),
    CONSTRAINT [FK_vc_company]      FOREIGN KEY ([company_id]) REFERENCES [${schema}].[companies] ([id])
);
GO
CREATE INDEX [idx_vendor_catalog_tenant_id]  ON [${schema}].[vendor_catalogs] ([tenant_id]);
CREATE INDEX [idx_vendor_catalog_company_id] ON [${schema}].[vendor_catalogs] ([company_id]);
CREATE INDEX [idx_vendor_catalog_vendor_id]  ON [${schema}].[vendor_catalogs] ([vendor_id]);
GO

CREATE TABLE [${schema}].[vendor_catalog_entries] (
    [id]                     UNIQUEIDENTIFIER NOT NULL,
    [catalog_id]             UNIQUEIDENTIFIER NOT NULL,
    [item_id]                NVARCHAR(255)    NULL,
    [item_description]       NVARCHAR(500)    NULL,
    [unit_code]              NVARCHAR(50)     NOT NULL,
    [unit_price]             DECIMAL(19, 6)   NOT NULL,
    [minimum_order_qty]      DECIMAL(19, 4)   NULL,
    [price_break_qty]        DECIMAL(19, 4)   NULL,
    [price_break_unit_price] DECIMAL(19, 6)   NULL,
    CONSTRAINT [PK_vendor_catalog_entries] PRIMARY KEY ([id]),
    CONSTRAINT [FK_vce_catalog]            FOREIGN KEY ([catalog_id])
        REFERENCES [${schema}].[vendor_catalogs] ([id]) ON DELETE CASCADE
);
GO
CREATE INDEX [idx_vce_catalog_id] ON [${schema}].[vendor_catalog_entries] ([catalog_id]);
GO

-- ─── Purchase Permissions ───
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00A0-0000-0000-000000000001','PURCHASE_REQUEST','CREATE', 'Create a purchase request');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00A0-0000-0000-000000000002','PURCHASE_REQUEST','READ',   'View purchase request details');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00A0-0000-0000-000000000003','PURCHASE_REQUEST','UPDATE', 'Update purchase request');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00A0-0000-0000-000000000004','PURCHASE_REQUEST','APPROVE','Approve a purchase request');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00A0-0000-0000-000000000005','PURCHASE_REQUEST','REJECT', 'Reject a purchase request');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00A0-0000-0000-000000000006','PURCHASE_REQUEST','CANCEL', 'Cancel a purchase request');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00A1-0000-0000-000000000001','PURCHASE_ORDER','CREATE', 'Create a purchase order');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00A1-0000-0000-000000000002','PURCHASE_ORDER','READ',   'View purchase order details');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00A1-0000-0000-000000000003','PURCHASE_ORDER','UPDATE', 'Update purchase order');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00A1-0000-0000-000000000004','PURCHASE_ORDER','SEND',   'Send purchase order to vendor');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00A1-0000-0000-000000000005','PURCHASE_ORDER','CONFIRM','Confirm purchase order');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00A1-0000-0000-000000000006','PURCHASE_ORDER','CANCEL', 'Cancel a purchase order');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00A2-0000-0000-000000000001','GOODS_RECEIPT','CREATE','Create a goods receipt');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00A2-0000-0000-000000000002','GOODS_RECEIPT','READ',  'View goods receipt details');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00A2-0000-0000-000000000003','GOODS_RECEIPT','POST',  'Post a goods receipt');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00A3-0000-0000-000000000001','VENDOR_INVOICE','CREATE','Create a vendor invoice');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00A3-0000-0000-000000000002','VENDOR_INVOICE','READ',  'View vendor invoice details');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00A3-0000-0000-000000000003','VENDOR_INVOICE','POST',  'Post a vendor invoice');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00A3-0000-0000-000000000004','VENDOR_INVOICE','CANCEL','Cancel a vendor invoice');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00A4-0000-0000-000000000001','PURCHASE_RETURN','CREATE',  'Create a purchase return');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00A4-0000-0000-000000000002','PURCHASE_RETURN','READ',    'View purchase return details');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00A4-0000-0000-000000000003','PURCHASE_RETURN','POST',    'Post a purchase return');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00A4-0000-0000-000000000004','PURCHASE_RETURN','COMPLETE','Complete a purchase return');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00A5-0000-0000-000000000001','VENDOR_CATALOG','CREATE','Create a vendor catalog');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00A5-0000-0000-000000000002','VENDOR_CATALOG','READ',  'View vendor catalog details');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00A5-0000-0000-000000000003','VENDOR_CATALOG','UPDATE','Update vendor catalog');
GO

-- ─── Role assignments ───
DECLARE @modules NVARCHAR(MAX) = 'PURCHASE_REQUEST,PURCHASE_ORDER,GOODS_RECEIPT,VENDOR_INVOICE,PURCHASE_RETURN,VENDOR_CATALOG';

MERGE INTO [${schema}].[role_permissions] AS tgt
USING (SELECT CONVERT(UNIQUEIDENTIFIER,HASHBYTES('MD5','ADMIN_${tenant_id}')) AS role_id, p.[id] AS permission_id
       FROM [${schema}].[permissions] p WHERE p.[module] IN ('PURCHASE_REQUEST','PURCHASE_ORDER','GOODS_RECEIPT','VENDOR_INVOICE','PURCHASE_RETURN','VENDOR_CATALOG')) AS src
ON tgt.[role_id]=src.[role_id] AND tgt.[permission_id]=src.[permission_id]
WHEN NOT MATCHED THEN INSERT([role_id],[permission_id]) VALUES(src.[role_id],src.[permission_id]);

MERGE INTO [${schema}].[role_permissions] AS tgt
USING (SELECT CONVERT(UNIQUEIDENTIFIER,HASHBYTES('MD5','MANAGER_${tenant_id}')) AS role_id, p.[id] AS permission_id
       FROM [${schema}].[permissions] p WHERE p.[module] IN ('PURCHASE_REQUEST','PURCHASE_ORDER','GOODS_RECEIPT','VENDOR_INVOICE','PURCHASE_RETURN','VENDOR_CATALOG')) AS src
ON tgt.[role_id]=src.[role_id] AND tgt.[permission_id]=src.[permission_id]
WHEN NOT MATCHED THEN INSERT([role_id],[permission_id]) VALUES(src.[role_id],src.[permission_id]);

MERGE INTO [${schema}].[role_permissions] AS tgt
USING (SELECT CONVERT(UNIQUEIDENTIFIER,HASHBYTES('MD5','USER_${tenant_id}')) AS role_id, p.[id] AS permission_id
       FROM [${schema}].[permissions] p WHERE p.[module] IN ('PURCHASE_REQUEST','PURCHASE_ORDER','GOODS_RECEIPT','VENDOR_INVOICE','PURCHASE_RETURN','VENDOR_CATALOG')
         AND p.[action] IN ('CREATE','READ','UPDATE')) AS src
ON tgt.[role_id]=src.[role_id] AND tgt.[permission_id]=src.[permission_id]
WHEN NOT MATCHED THEN INSERT([role_id],[permission_id]) VALUES(src.[role_id],src.[permission_id]);

MERGE INTO [${schema}].[role_permissions] AS tgt
USING (SELECT CONVERT(UNIQUEIDENTIFIER,HASHBYTES('MD5','VIEWER_${tenant_id}')) AS role_id, p.[id] AS permission_id
       FROM [${schema}].[permissions] p WHERE p.[module] IN ('PURCHASE_REQUEST','PURCHASE_ORDER','GOODS_RECEIPT','VENDOR_INVOICE','PURCHASE_RETURN','VENDOR_CATALOG')
         AND p.[action] = 'READ') AS src
ON tgt.[role_id]=src.[role_id] AND tgt.[permission_id]=src.[permission_id]
WHEN NOT MATCHED THEN INSERT([role_id],[permission_id]) VALUES(src.[role_id],src.[permission_id]);
GO
