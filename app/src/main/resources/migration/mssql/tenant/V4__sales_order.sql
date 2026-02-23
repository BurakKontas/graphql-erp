-- V4__sales_order.sql (Main Application - MSSQL)
-- UUID -> UNIQUEIDENTIFIER, NUMERIC -> DECIMAL, VARCHAR -> NVARCHAR

CREATE TABLE [${schema}].[sales_orders] (
    [id]                         UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]                  UNIQUEIDENTIFIER NOT NULL,
    [company_id]                 UNIQUEIDENTIFIER NOT NULL,
    [order_number]               NVARCHAR(20)     NOT NULL,
    [order_date]                 DATE             NOT NULL,
    [expiry_date]                DATE             NULL,
    [customer_id]                UNIQUEIDENTIFIER NULL,
    [currency_code]              NVARCHAR(3)      NULL,
    [payment_term_code]          NVARCHAR(50)     NULL,
    [shipping_address_line1]     NVARCHAR(500)    NULL,
    [shipping_address_line2]     NVARCHAR(500)    NULL,
    [shipping_city]              NVARCHAR(255)    NULL,
    [shipping_state_or_province] NVARCHAR(255)    NULL,
    [shipping_postal_code]       NVARCHAR(50)     NULL,
    [shipping_country_code]      NVARCHAR(2)      NULL,
    [status]                     NVARCHAR(50)     NOT NULL DEFAULT 'DRAFT',
    [fulfillment_status]         NVARCHAR(50)     NOT NULL DEFAULT 'NOT_STARTED',
    [invoiced_amount]            DECIMAL(19, 2)   NOT NULL DEFAULT 0,
    [subtotal]                   DECIMAL(19, 2)   NOT NULL DEFAULT 0,
    [tax_total]                  DECIMAL(19, 2)   NOT NULL DEFAULT 0,
    [total]                      DECIMAL(19, 2)   NOT NULL DEFAULT 0,
    CONSTRAINT [PK_sales_orders]       PRIMARY KEY ([id]),
    CONSTRAINT [FK_so_company]         FOREIGN KEY ([company_id])        REFERENCES [${schema}].[companies] ([id]),
    CONSTRAINT [FK_so_customer]        FOREIGN KEY ([customer_id])       REFERENCES [${schema}].[business_partner] ([id]),
    CONSTRAINT [FK_so_currency]        FOREIGN KEY ([currency_code])     REFERENCES [${schema}].[currencies] ([code]),
    CONSTRAINT [FK_so_payment_term]    FOREIGN KEY ([payment_term_code]) REFERENCES [${schema}].[payment_terms] ([code]),
    CONSTRAINT [UQ_so_number]          UNIQUE ([tenant_id], [order_number])
);
GO

CREATE INDEX [idx_sales_order_tenant_id]   ON [${schema}].[sales_orders] ([tenant_id]);
CREATE INDEX [idx_sales_order_company_id]  ON [${schema}].[sales_orders] ([company_id]);
CREATE INDEX [idx_sales_order_customer_id] ON [${schema}].[sales_orders] ([customer_id]);
CREATE INDEX [idx_sales_order_status]      ON [${schema}].[sales_orders] ([status]);
CREATE INDEX [idx_sales_order_order_date]  ON [${schema}].[sales_orders] ([order_date]);
GO

-- ─── Sales Order Lines ───
CREATE TABLE [${schema}].[sales_order_lines] (
    [id]                  UNIQUEIDENTIFIER NOT NULL,
    [order_id]            UNIQUEIDENTIFIER NOT NULL,
    [sequence]            INT              NOT NULL,
    [item_id]             NVARCHAR(255)    NULL,
    [item_description]    NVARCHAR(500)    NOT NULL,
    [unit_code]           NVARCHAR(50)     NOT NULL,
    [quantity]            DECIMAL(19, 4)   NOT NULL,
    [unit_price]          DECIMAL(19, 6)   NOT NULL,
    [tax_code]            NVARCHAR(50)     NULL,
    [tax_rate]            DECIMAL(10, 4)   NULL,
    [line_total]          DECIMAL(19, 2)   NOT NULL,
    [tax_amount]          DECIMAL(19, 2)   NOT NULL,
    [line_total_with_tax] DECIMAL(19, 2)   NOT NULL,
    CONSTRAINT [PK_sales_order_lines] PRIMARY KEY ([id]),
    CONSTRAINT [FK_sol_order]         FOREIGN KEY ([order_id])
        REFERENCES [${schema}].[sales_orders] ([id]) ON DELETE CASCADE
);
GO

CREATE INDEX [idx_sales_order_line_order_id] ON [${schema}].[sales_order_lines] ([order_id]);
GO

-- ─── SALES_ORDER Permissions ───
INSERT INTO [${schema}].[permissions] ([id], [module], [action], [description]) VALUES ('00000000-0000-0009-0000-000000000001', 'SALES_ORDER', 'CREATE',  'Create a new sales order');
INSERT INTO [${schema}].[permissions] ([id], [module], [action], [description]) VALUES ('00000000-0000-0009-0000-000000000002', 'SALES_ORDER', 'READ',    'View sales order details');
INSERT INTO [${schema}].[permissions] ([id], [module], [action], [description]) VALUES ('00000000-0000-0009-0000-000000000003', 'SALES_ORDER', 'UPDATE',  'Update sales order information');
INSERT INTO [${schema}].[permissions] ([id], [module], [action], [description]) VALUES ('00000000-0000-0009-0000-000000000004', 'SALES_ORDER', 'DELETE',  'Delete a sales order');
INSERT INTO [${schema}].[permissions] ([id], [module], [action], [description]) VALUES ('00000000-0000-0009-0000-000000000005', 'SALES_ORDER', 'SEND',    'Send a sales order to customer');
INSERT INTO [${schema}].[permissions] ([id], [module], [action], [description]) VALUES ('00000000-0000-0009-0000-000000000006', 'SALES_ORDER', 'ACCEPT',  'Accept a sales order');
INSERT INTO [${schema}].[permissions] ([id], [module], [action], [description]) VALUES ('00000000-0000-0009-0000-000000000007', 'SALES_ORDER', 'CONFIRM', 'Confirm a sales order');
INSERT INTO [${schema}].[permissions] ([id], [module], [action], [description]) VALUES ('00000000-0000-0009-0000-000000000008', 'SALES_ORDER', 'CANCEL',  'Cancel a sales order');
GO
