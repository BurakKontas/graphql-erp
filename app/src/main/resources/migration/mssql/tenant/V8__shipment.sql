-- V8__shipment.sql (Main Application - MSSQL)
-- UUID -> UNIQUEIDENTIFIER, BOOLEAN -> BIT, TIMESTAMP -> DATETIME2, VARCHAR -> NVARCHAR

-- ─── Delivery Orders ───
CREATE TABLE [${schema}].[delivery_orders] (
    [id]                 UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]          UNIQUEIDENTIFIER NOT NULL,
    [company_id]         UNIQUEIDENTIFIER NOT NULL,
    [delivery_number]    NVARCHAR(20)     NOT NULL,
    [sales_order_id]     NVARCHAR(255)    NULL,
    [sales_order_number] NVARCHAR(20)     NULL,
    [customer_id]        NVARCHAR(255)    NULL,
    [address_line1]      NVARCHAR(255)    NULL,
    [address_line2]      NVARCHAR(255)    NULL,
    [city]               NVARCHAR(100)    NULL,
    [state_or_province]  NVARCHAR(100)    NULL,
    [postal_code]        NVARCHAR(20)     NULL,
    [country_code]       NVARCHAR(2)      NULL,
    [status]             NVARCHAR(50)     NOT NULL DEFAULT 'PENDING',
    CONSTRAINT [PK_delivery_orders] PRIMARY KEY ([id]),
    CONSTRAINT [FK_do_company]      FOREIGN KEY ([company_id]) REFERENCES [${schema}].[companies] ([id]),
    CONSTRAINT [UQ_do_number]       UNIQUE ([tenant_id], [delivery_number])
);
GO

CREATE INDEX [idx_delivery_order_tenant_id]      ON [${schema}].[delivery_orders] ([tenant_id]);
CREATE INDEX [idx_delivery_order_company_id]     ON [${schema}].[delivery_orders] ([company_id]);
CREATE INDEX [idx_delivery_order_sales_order_id] ON [${schema}].[delivery_orders] ([sales_order_id]);
CREATE INDEX [idx_delivery_order_status]         ON [${schema}].[delivery_orders] ([status]);
GO

-- ─── Delivery Order Lines ───
CREATE TABLE [${schema}].[delivery_order_lines] (
    [id]                  UNIQUEIDENTIFIER NOT NULL,
    [delivery_order_id]   UNIQUEIDENTIFIER NOT NULL,
    [sales_order_line_id] NVARCHAR(255)    NULL,
    [item_id]             NVARCHAR(255)    NOT NULL,
    [item_description]    NVARCHAR(500)    NULL,
    [unit_code]           NVARCHAR(50)     NOT NULL,
    [ordered_qty]         DECIMAL(19, 4)   NOT NULL,
    [shipped_qty]         DECIMAL(19, 4)   NOT NULL DEFAULT 0,
    CONSTRAINT [PK_delivery_order_lines] PRIMARY KEY ([id]),
    CONSTRAINT [FK_dol_delivery_order]   FOREIGN KEY ([delivery_order_id])
        REFERENCES [${schema}].[delivery_orders] ([id]) ON DELETE CASCADE
);
GO

CREATE INDEX [idx_do_line_delivery_order_id] ON [${schema}].[delivery_order_lines] ([delivery_order_id]);
GO

-- ─── Shipments ───
CREATE TABLE [${schema}].[shipments] (
    [id]                UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]         UNIQUEIDENTIFIER NOT NULL,
    [company_id]        UNIQUEIDENTIFIER NOT NULL,
    [shipment_number]   NVARCHAR(20)     NOT NULL,
    [delivery_order_id] NVARCHAR(255)    NULL,
    [sales_order_id]    NVARCHAR(255)    NULL,
    [warehouse_id]      NVARCHAR(255)    NULL,
    [address_line1]     NVARCHAR(255)    NULL,
    [address_line2]     NVARCHAR(255)    NULL,
    [city]              NVARCHAR(100)    NULL,
    [state_or_province] NVARCHAR(100)    NULL,
    [postal_code]       NVARCHAR(20)     NULL,
    [country_code]      NVARCHAR(2)      NULL,
    [tracking_number]   NVARCHAR(255)    NULL,
    [carrier_name]      NVARCHAR(255)    NULL,
    [status]            NVARCHAR(50)     NOT NULL DEFAULT 'PREPARING',
    [dispatched_at]     DATETIME2        NULL,
    [delivered_at]      DATETIME2        NULL,
    CONSTRAINT [PK_shipments]     PRIMARY KEY ([id]),
    CONSTRAINT [FK_sh_company]    FOREIGN KEY ([company_id]) REFERENCES [${schema}].[companies] ([id]),
    CONSTRAINT [UQ_sh_number]     UNIQUE ([tenant_id], [shipment_number])
);
GO

CREATE INDEX [idx_shipment_tenant_id]         ON [${schema}].[shipments] ([tenant_id]);
CREATE INDEX [idx_shipment_company_id]        ON [${schema}].[shipments] ([company_id]);
CREATE INDEX [idx_shipment_delivery_order_id] ON [${schema}].[shipments] ([delivery_order_id]);
CREATE INDEX [idx_shipment_sales_order_id]    ON [${schema}].[shipments] ([sales_order_id]);
CREATE INDEX [idx_shipment_status]            ON [${schema}].[shipments] ([status]);
GO

-- ─── Shipment Lines ───
CREATE TABLE [${schema}].[shipment_lines] (
    [id]                     UNIQUEIDENTIFIER NOT NULL,
    [shipment_id]            UNIQUEIDENTIFIER NOT NULL,
    [delivery_order_line_id] NVARCHAR(255)    NULL,
    [item_id]                NVARCHAR(255)    NOT NULL,
    [item_description]       NVARCHAR(500)    NULL,
    [unit_code]              NVARCHAR(50)     NOT NULL,
    [quantity]               DECIMAL(19, 4)   NOT NULL,
    CONSTRAINT [PK_shipment_lines] PRIMARY KEY ([id]),
    CONSTRAINT [FK_shl_shipment]   FOREIGN KEY ([shipment_id])
        REFERENCES [${schema}].[shipments] ([id]) ON DELETE CASCADE
);
GO

CREATE INDEX [idx_shipment_line_shipment_id] ON [${schema}].[shipment_lines] ([shipment_id]);
GO

-- ─── Shipment Returns ───
CREATE TABLE [${schema}].[shipment_returns] (
    [id]             UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]      UNIQUEIDENTIFIER NOT NULL,
    [company_id]     UNIQUEIDENTIFIER NOT NULL,
    [return_number]  NVARCHAR(20)     NOT NULL,
    [shipment_id]    NVARCHAR(255)    NULL,
    [sales_order_id] NVARCHAR(255)    NULL,
    [warehouse_id]   NVARCHAR(255)    NULL,
    [reason]         NVARCHAR(500)    NULL,
    [status]         NVARCHAR(50)     NOT NULL DEFAULT 'REQUESTED',
    [received_at]    DATETIME2        NULL,
    CONSTRAINT [PK_shipment_returns] PRIMARY KEY ([id]),
    CONSTRAINT [FK_sr_company]       FOREIGN KEY ([company_id]) REFERENCES [${schema}].[companies] ([id]),
    CONSTRAINT [UQ_sr_number]        UNIQUE ([tenant_id], [return_number])
);
GO

CREATE INDEX [idx_shipment_return_tenant_id]   ON [${schema}].[shipment_returns] ([tenant_id]);
CREATE INDEX [idx_shipment_return_company_id]  ON [${schema}].[shipment_returns] ([company_id]);
CREATE INDEX [idx_shipment_return_shipment_id] ON [${schema}].[shipment_returns] ([shipment_id]);
CREATE INDEX [idx_shipment_return_status]      ON [${schema}].[shipment_returns] ([status]);
GO

-- ─── Shipment Return Lines ───
CREATE TABLE [${schema}].[shipment_return_lines] (
    [id]                 UNIQUEIDENTIFIER NOT NULL,
    [shipment_return_id] UNIQUEIDENTIFIER NOT NULL,
    [shipment_line_id]   NVARCHAR(255)    NULL,
    [item_id]            NVARCHAR(255)    NOT NULL,
    [item_description]   NVARCHAR(500)    NULL,
    [unit_code]          NVARCHAR(50)     NOT NULL,
    [quantity]           DECIMAL(19, 4)   NOT NULL,
    [line_reason]        NVARCHAR(500)    NULL,
    CONSTRAINT [PK_shipment_return_lines] PRIMARY KEY ([id]),
    CONSTRAINT [FK_srl_return]            FOREIGN KEY ([shipment_return_id])
        REFERENCES [${schema}].[shipment_returns] ([id]) ON DELETE CASCADE
);
GO

CREATE INDEX [idx_sr_line_shipment_return_id] ON [${schema}].[shipment_return_lines] ([shipment_return_id]);
GO

-- ─── Shipment Permissions ───
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F0-0000-0000-000000000001','DELIVERY_ORDER','CREATE',  'Create a delivery order');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F0-0000-0000-000000000002','DELIVERY_ORDER','READ',    'View delivery order details');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F0-0000-0000-000000000003','DELIVERY_ORDER','UPDATE',  'Update delivery order');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F0-0000-0000-000000000004','DELIVERY_ORDER','CANCEL',  'Cancel a delivery order');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F1-0001-0000-000000000001','SHIPMENT','CREATE',   'Create a shipment');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F1-0001-0000-000000000002','SHIPMENT','READ',     'View shipment details');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F1-0001-0000-000000000003','SHIPMENT','UPDATE',   'Update shipment');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F1-0001-0000-000000000004','SHIPMENT','DISPATCH', 'Dispatch a shipment');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F1-0001-0000-000000000005','SHIPMENT','DELIVER',  'Mark shipment as delivered');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F2-0001-0000-000000000001','SHIPMENT_RETURN','CREATE',   'Create a shipment return');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F2-0001-0000-000000000002','SHIPMENT_RETURN','READ',     'View shipment return details');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F2-0001-0000-000000000003','SHIPMENT_RETURN','RECEIVE',  'Receive a shipment return');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F2-0001-0000-000000000004','SHIPMENT_RETURN','COMPLETE', 'Complete a shipment return');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F2-0001-0000-000000000005','SHIPMENT_RETURN','CANCEL',   'Cancel a shipment return');
GO
