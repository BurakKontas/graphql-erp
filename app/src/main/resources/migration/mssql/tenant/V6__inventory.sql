-- V6__inventory.sql (Main Application - MSSQL)
-- UUID -> UNIQUEIDENTIFIER, BOOLEAN -> BIT, NUMERIC -> DECIMAL
-- NOW() -> GETUTCDATE(), VARCHAR -> NVARCHAR

-- ─── Warehouses ───
CREATE TABLE [${schema}].[warehouses] (
    [id]         UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]  UNIQUEIDENTIFIER NOT NULL,
    [company_id] UNIQUEIDENTIFIER NOT NULL,
    [code]       NVARCHAR(20)     NOT NULL,
    [name]       NVARCHAR(200)    NOT NULL,
    [active]     BIT              NOT NULL DEFAULT 1,
    CONSTRAINT [PK_warehouses]    PRIMARY KEY ([id]),
    CONSTRAINT [FK_wh_company]    FOREIGN KEY ([company_id]) REFERENCES [${schema}].[companies] ([id]),
    CONSTRAINT [UQ_wh_code]       UNIQUE ([tenant_id], [company_id], [code])
);
GO

CREATE INDEX [idx_warehouse_tenant_id]  ON [${schema}].[warehouses] ([tenant_id]);
CREATE INDEX [idx_warehouse_company_id] ON [${schema}].[warehouses] ([company_id]);
GO

-- ─── Categories ───
CREATE TABLE [${schema}].[categories] (
    [id]                 UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]          UNIQUEIDENTIFIER NOT NULL,
    [company_id]         UNIQUEIDENTIFIER NOT NULL,
    [name]               NVARCHAR(200)    NOT NULL,
    [parent_category_id] UNIQUEIDENTIFIER NULL,
    [active]             BIT              NOT NULL DEFAULT 1,
    CONSTRAINT [PK_categories]   PRIMARY KEY ([id]),
    CONSTRAINT [FK_cat_company]  FOREIGN KEY ([company_id])         REFERENCES [${schema}].[companies] ([id]),
    CONSTRAINT [FK_cat_parent]   FOREIGN KEY ([parent_category_id]) REFERENCES [${schema}].[categories] ([id])
);
GO

CREATE INDEX [idx_category_tenant_id]  ON [${schema}].[categories] ([tenant_id]);
CREATE INDEX [idx_category_company_id] ON [${schema}].[categories] ([company_id]);
CREATE INDEX [idx_category_parent_id]  ON [${schema}].[categories] ([parent_category_id]);
GO

-- ─── Items ───
CREATE TABLE [${schema}].[items] (
    [id]                   UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]            UNIQUEIDENTIFIER NOT NULL,
    [company_id]           UNIQUEIDENTIFIER NOT NULL,
    [code]                 NVARCHAR(50)     NOT NULL,
    [name]                 NVARCHAR(200)    NOT NULL,
    [type]                 NVARCHAR(50)     NOT NULL,
    [unit_code]            NVARCHAR(50)     NULL,
    [category_id]          UNIQUEIDENTIFIER NULL,
    [allow_negative_stock] BIT              NOT NULL DEFAULT 0,
    [active]               BIT              NOT NULL DEFAULT 1,
    CONSTRAINT [PK_items]       PRIMARY KEY ([id]),
    CONSTRAINT [FK_item_company]  FOREIGN KEY ([company_id])  REFERENCES [${schema}].[companies] ([id]),
    CONSTRAINT [FK_item_category] FOREIGN KEY ([category_id]) REFERENCES [${schema}].[categories] ([id]),
    CONSTRAINT [FK_item_unit]     FOREIGN KEY ([unit_code])   REFERENCES [${schema}].[units] ([code]),
    CONSTRAINT [UQ_item_code]     UNIQUE ([tenant_id], [company_id], [code])
);
GO

CREATE INDEX [idx_item_tenant_id]   ON [${schema}].[items] ([tenant_id]);
CREATE INDEX [idx_item_company_id]  ON [${schema}].[items] ([company_id]);
CREATE INDEX [idx_item_category_id] ON [${schema}].[items] ([category_id]);
CREATE INDEX [idx_item_type]        ON [${schema}].[items] ([type]);
GO

-- ─── Stock Levels ───
CREATE TABLE [${schema}].[stock_levels] (
    [id]                   UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]            UNIQUEIDENTIFIER NOT NULL,
    [company_id]           UNIQUEIDENTIFIER NOT NULL,
    [item_id]              UNIQUEIDENTIFIER NOT NULL,
    [warehouse_id]         UNIQUEIDENTIFIER NOT NULL,
    [quantity_on_hand]     DECIMAL(19, 4)   NOT NULL DEFAULT 0,
    [quantity_reserved]    DECIMAL(19, 4)   NOT NULL DEFAULT 0,
    [reorder_point]        DECIMAL(19, 4)   NULL,
    [allow_negative_stock] BIT              NOT NULL DEFAULT 0,
    CONSTRAINT [PK_stock_levels]       PRIMARY KEY ([id]),
    CONSTRAINT [FK_sl_company]         FOREIGN KEY ([company_id])  REFERENCES [${schema}].[companies] ([id]),
    CONSTRAINT [FK_sl_item]            FOREIGN KEY ([item_id])     REFERENCES [${schema}].[items] ([id]),
    CONSTRAINT [FK_sl_warehouse]       FOREIGN KEY ([warehouse_id]) REFERENCES [${schema}].[warehouses] ([id]),
    CONSTRAINT [UQ_sl_item_warehouse]  UNIQUE ([tenant_id], [item_id], [warehouse_id])
);
GO

CREATE INDEX [idx_stock_level_tenant_id]    ON [${schema}].[stock_levels] ([tenant_id]);
CREATE INDEX [idx_stock_level_item_id]      ON [${schema}].[stock_levels] ([item_id]);
CREATE INDEX [idx_stock_level_warehouse_id] ON [${schema}].[stock_levels] ([warehouse_id]);
GO

-- ─── Stock Movements ───
CREATE TABLE [${schema}].[stock_movements] (
    [id]             UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]      UNIQUEIDENTIFIER NOT NULL,
    [company_id]     UNIQUEIDENTIFIER NOT NULL,
    [item_id]        UNIQUEIDENTIFIER NOT NULL,
    [warehouse_id]   UNIQUEIDENTIFIER NOT NULL,
    [movement_type]  NVARCHAR(50)     NOT NULL,
    [quantity]       DECIMAL(19, 4)   NOT NULL,
    [reference_type] NVARCHAR(50)     NOT NULL,
    [reference_id]   NVARCHAR(255)    NULL,
    [note]           NVARCHAR(1000)   NULL,
    [movement_date]  DATE             NOT NULL,
    [created_at]     DATETIME2        NOT NULL DEFAULT GETUTCDATE(),
    CONSTRAINT [PK_stock_movements] PRIMARY KEY ([id]),
    CONSTRAINT [FK_sm_company]      FOREIGN KEY ([company_id])   REFERENCES [${schema}].[companies] ([id]),
    CONSTRAINT [FK_sm_item]         FOREIGN KEY ([item_id])      REFERENCES [${schema}].[items] ([id]),
    CONSTRAINT [FK_sm_warehouse]    FOREIGN KEY ([warehouse_id]) REFERENCES [${schema}].[warehouses] ([id])
);
GO

CREATE INDEX [idx_stock_movement_tenant_id]    ON [${schema}].[stock_movements] ([tenant_id]);
CREATE INDEX [idx_stock_movement_item_id]      ON [${schema}].[stock_movements] ([item_id]);
CREATE INDEX [idx_stock_movement_warehouse_id] ON [${schema}].[stock_movements] ([warehouse_id]);
CREATE INDEX [idx_stock_movement_date]         ON [${schema}].[stock_movements] ([movement_date]);
CREATE INDEX [idx_stock_movement_type]         ON [${schema}].[stock_movements] ([movement_type]);
GO

-- ─── Inventory Permissions ───
INSERT INTO [${schema}].[permissions] ([id],[module],[action],[description]) VALUES ('00000000-000A-0000-0000-000000000001','WAREHOUSE','CREATE','Create a new warehouse');
INSERT INTO [${schema}].[permissions] ([id],[module],[action],[description]) VALUES ('00000000-000A-0000-0000-000000000002','WAREHOUSE','READ',  'View warehouse details');
INSERT INTO [${schema}].[permissions] ([id],[module],[action],[description]) VALUES ('00000000-000A-0000-0000-000000000003','WAREHOUSE','UPDATE','Update warehouse information');
INSERT INTO [${schema}].[permissions] ([id],[module],[action],[description]) VALUES ('00000000-000A-0000-0000-000000000004','WAREHOUSE','DELETE','Delete a warehouse');
INSERT INTO [${schema}].[permissions] ([id],[module],[action],[description]) VALUES ('00000000-000B-0000-0000-000000000001','CATEGORY','CREATE','Create a new category');
INSERT INTO [${schema}].[permissions] ([id],[module],[action],[description]) VALUES ('00000000-000B-0000-0000-000000000002','CATEGORY','READ',  'View category details');
INSERT INTO [${schema}].[permissions] ([id],[module],[action],[description]) VALUES ('00000000-000B-0000-0000-000000000003','CATEGORY','UPDATE','Update category information');
INSERT INTO [${schema}].[permissions] ([id],[module],[action],[description]) VALUES ('00000000-000B-0000-0000-000000000004','CATEGORY','DELETE','Delete a category');
INSERT INTO [${schema}].[permissions] ([id],[module],[action],[description]) VALUES ('00000000-000C-0000-0000-000000000001','ITEM','CREATE','Create a new item');
INSERT INTO [${schema}].[permissions] ([id],[module],[action],[description]) VALUES ('00000000-000C-0000-0000-000000000002','ITEM','READ',  'View item details');
INSERT INTO [${schema}].[permissions] ([id],[module],[action],[description]) VALUES ('00000000-000C-0000-0000-000000000003','ITEM','UPDATE','Update item information');
INSERT INTO [${schema}].[permissions] ([id],[module],[action],[description]) VALUES ('00000000-000C-0000-0000-000000000004','ITEM','DELETE','Delete an item');
INSERT INTO [${schema}].[permissions] ([id],[module],[action],[description]) VALUES ('00000000-000D-0000-0000-000000000001','STOCK_LEVEL','CREATE','Create a stock level entry');
INSERT INTO [${schema}].[permissions] ([id],[module],[action],[description]) VALUES ('00000000-000D-0000-0000-000000000002','STOCK_LEVEL','READ',  'View stock level details');
INSERT INTO [${schema}].[permissions] ([id],[module],[action],[description]) VALUES ('00000000-000D-0000-0000-000000000003','STOCK_LEVEL','UPDATE','Update stock level');
INSERT INTO [${schema}].[permissions] ([id],[module],[action],[description]) VALUES ('00000000-000D-0000-0000-000000000004','STOCK_LEVEL','DELETE','Delete a stock level entry');
INSERT INTO [${schema}].[permissions] ([id],[module],[action],[description]) VALUES ('00000000-000E-0000-0000-000000000001','STOCK_MOVEMENT','CREATE','Record a stock movement');
INSERT INTO [${schema}].[permissions] ([id],[module],[action],[description]) VALUES ('00000000-000E-0000-0000-000000000002','STOCK_MOVEMENT','READ',  'View stock movement details');
INSERT INTO [${schema}].[permissions] ([id],[module],[action],[description]) VALUES ('00000000-000E-0000-0000-000000000003','STOCK_MOVEMENT','UPDATE','Update stock movement');
INSERT INTO [${schema}].[permissions] ([id],[module],[action],[description]) VALUES ('00000000-000E-0000-0000-000000000004','STOCK_MOVEMENT','DELETE','Delete a stock movement');
GO
