-- V3__reference_data.sql (Main Application - MSSQL)
-- UUID -> UNIQUEIDENTIFIER, BOOLEAN -> BIT, INT kept, NUMERIC -> DECIMAL

-- ─── Currencies ───
CREATE TABLE [${schema}].[currencies] (
    [code]            NVARCHAR(3)  NOT NULL,
    [name]            NVARCHAR(255) NOT NULL,
    [symbol]          NVARCHAR(10)  NOT NULL,
    [fraction_digits] INT           NOT NULL DEFAULT 2,
    [active]          BIT           NOT NULL DEFAULT 1,
    CONSTRAINT [PK_currencies] PRIMARY KEY ([code])
);
GO

-- ─── Units ───
CREATE TABLE [${schema}].[units] (
    [code]   NVARCHAR(50)  NOT NULL,
    [name]   NVARCHAR(255) NOT NULL,
    [type]   NVARCHAR(50)  NOT NULL,
    [active] BIT           NOT NULL DEFAULT 1,
    CONSTRAINT [PK_units] PRIMARY KEY ([code])
);
GO

-- ─── Payment Terms ───
CREATE TABLE [${schema}].[payment_terms] (
    [code]       NVARCHAR(50)  NOT NULL,
    [tenant_id]  UNIQUEIDENTIFIER NOT NULL,
    [company_id] UNIQUEIDENTIFIER NOT NULL,
    [name]       NVARCHAR(255) NOT NULL,
    [due_days]   INT           NOT NULL DEFAULT 0,
    [active]     BIT           NOT NULL DEFAULT 1,
    CONSTRAINT [PK_payment_terms]  PRIMARY KEY ([code]),
    CONSTRAINT [FK_pt_company]     FOREIGN KEY ([company_id])
        REFERENCES [${schema}].[companies] ([id])
);
GO

CREATE INDEX [idx_payment_term_tenant_company] ON [${schema}].[payment_terms] ([tenant_id], [company_id]);
GO

-- ─── Taxes ───
CREATE TABLE [${schema}].[taxes] (
    [code]       NVARCHAR(50)    NOT NULL,
    [tenant_id]  UNIQUEIDENTIFIER NOT NULL,
    [company_id] UNIQUEIDENTIFIER NOT NULL,
    [name]       NVARCHAR(255)   NOT NULL,
    [type]       NVARCHAR(50)    NOT NULL,
    [rate]       DECIMAL(10, 4)  NOT NULL,
    [active]     BIT             NOT NULL DEFAULT 1,
    CONSTRAINT [PK_taxes]     PRIMARY KEY ([code]),
    CONSTRAINT [FK_tax_company] FOREIGN KEY ([company_id])
        REFERENCES [${schema}].[companies] ([id])
);
GO

CREATE INDEX [idx_tax_tenant_company] ON [${schema}].[taxes] ([tenant_id], [company_id]);
GO

-- ─── Default Permissions ───
-- GENERAL
INSERT INTO [${schema}].[permissions] ([id], [module], [action], [description])
VALUES ('00000000-0000-0000-0000-000000000001', 'GENERAL', 'ADMIN', 'Super admin — full access to all modules and actions');

-- COMPANY
INSERT INTO [${schema}].[permissions] ([id], [module], [action], [description]) VALUES ('00000000-0000-0001-0000-000000000001', 'COMPANY', 'CREATE', 'Create a new company');
INSERT INTO [${schema}].[permissions] ([id], [module], [action], [description]) VALUES ('00000000-0000-0001-0000-000000000002', 'COMPANY', 'READ',   'View company details');
INSERT INTO [${schema}].[permissions] ([id], [module], [action], [description]) VALUES ('00000000-0000-0001-0000-000000000003', 'COMPANY', 'UPDATE', 'Update company information');
INSERT INTO [${schema}].[permissions] ([id], [module], [action], [description]) VALUES ('00000000-0000-0001-0000-000000000004', 'COMPANY', 'DELETE', 'Delete a company');

-- DEPARTMENT
INSERT INTO [${schema}].[permissions] ([id], [module], [action], [description]) VALUES ('00000000-0000-0002-0000-000000000001', 'DEPARTMENT', 'CREATE', 'Create a new department');
INSERT INTO [${schema}].[permissions] ([id], [module], [action], [description]) VALUES ('00000000-0000-0002-0000-000000000002', 'DEPARTMENT', 'READ',   'View department details');
INSERT INTO [${schema}].[permissions] ([id], [module], [action], [description]) VALUES ('00000000-0000-0002-0000-000000000003', 'DEPARTMENT', 'UPDATE', 'Update department information');
INSERT INTO [${schema}].[permissions] ([id], [module], [action], [description]) VALUES ('00000000-0000-0002-0000-000000000004', 'DEPARTMENT', 'DELETE', 'Delete a department');

-- EMPLOYEE
INSERT INTO [${schema}].[permissions] ([id], [module], [action], [description]) VALUES ('00000000-0000-0003-0000-000000000001', 'EMPLOYEE', 'CREATE', 'Create a new employee');
INSERT INTO [${schema}].[permissions] ([id], [module], [action], [description]) VALUES ('00000000-0000-0003-0000-000000000002', 'EMPLOYEE', 'READ',   'View employee details');
INSERT INTO [${schema}].[permissions] ([id], [module], [action], [description]) VALUES ('00000000-0000-0003-0000-000000000003', 'EMPLOYEE', 'UPDATE', 'Update employee information');
INSERT INTO [${schema}].[permissions] ([id], [module], [action], [description]) VALUES ('00000000-0000-0003-0000-000000000004', 'EMPLOYEE', 'DELETE', 'Delete an employee');

-- BUSINESS_PARTNER
INSERT INTO [${schema}].[permissions] ([id], [module], [action], [description]) VALUES ('00000000-0000-0004-0000-000000000001', 'BUSINESS_PARTNER', 'CREATE', 'Create a new business partner');
INSERT INTO [${schema}].[permissions] ([id], [module], [action], [description]) VALUES ('00000000-0000-0004-0000-000000000002', 'BUSINESS_PARTNER', 'READ',   'View business partner details');
INSERT INTO [${schema}].[permissions] ([id], [module], [action], [description]) VALUES ('00000000-0000-0004-0000-000000000003', 'BUSINESS_PARTNER', 'UPDATE', 'Update business partner information');
INSERT INTO [${schema}].[permissions] ([id], [module], [action], [description]) VALUES ('00000000-0000-0004-0000-000000000004', 'BUSINESS_PARTNER', 'DELETE', 'Delete a business partner');

-- CURRENCY
INSERT INTO [${schema}].[permissions] ([id], [module], [action], [description]) VALUES ('00000000-0000-0005-0000-000000000001', 'CURRENCY', 'CREATE', 'Create a new currency');
INSERT INTO [${schema}].[permissions] ([id], [module], [action], [description]) VALUES ('00000000-0000-0005-0000-000000000002', 'CURRENCY', 'READ',   'View currency details');
INSERT INTO [${schema}].[permissions] ([id], [module], [action], [description]) VALUES ('00000000-0000-0005-0000-000000000003', 'CURRENCY', 'UPDATE', 'Update currency information');
INSERT INTO [${schema}].[permissions] ([id], [module], [action], [description]) VALUES ('00000000-0000-0005-0000-000000000004', 'CURRENCY', 'DELETE', 'Delete a currency');

-- PAYMENT_TERM
INSERT INTO [${schema}].[permissions] ([id], [module], [action], [description]) VALUES ('00000000-0000-0006-0000-000000000001', 'PAYMENT_TERM', 'CREATE', 'Create a new payment term');
INSERT INTO [${schema}].[permissions] ([id], [module], [action], [description]) VALUES ('00000000-0000-0006-0000-000000000002', 'PAYMENT_TERM', 'READ',   'View payment term details');
INSERT INTO [${schema}].[permissions] ([id], [module], [action], [description]) VALUES ('00000000-0000-0006-0000-000000000003', 'PAYMENT_TERM', 'UPDATE', 'Update payment term information');
INSERT INTO [${schema}].[permissions] ([id], [module], [action], [description]) VALUES ('00000000-0000-0006-0000-000000000004', 'PAYMENT_TERM', 'DELETE', 'Delete a payment term');

-- TAX
INSERT INTO [${schema}].[permissions] ([id], [module], [action], [description]) VALUES ('00000000-0000-0007-0000-000000000001', 'TAX', 'CREATE', 'Create a new tax definition');
INSERT INTO [${schema}].[permissions] ([id], [module], [action], [description]) VALUES ('00000000-0000-0007-0000-000000000002', 'TAX', 'READ',   'View tax details');
INSERT INTO [${schema}].[permissions] ([id], [module], [action], [description]) VALUES ('00000000-0000-0007-0000-000000000003', 'TAX', 'UPDATE', 'Update tax information');
INSERT INTO [${schema}].[permissions] ([id], [module], [action], [description]) VALUES ('00000000-0000-0007-0000-000000000004', 'TAX', 'DELETE', 'Delete a tax definition');

-- UNIT
INSERT INTO [${schema}].[permissions] ([id], [module], [action], [description]) VALUES ('00000000-0000-0008-0000-000000000001', 'UNIT', 'CREATE', 'Create a new unit of measure');
INSERT INTO [${schema}].[permissions] ([id], [module], [action], [description]) VALUES ('00000000-0000-0008-0000-000000000002', 'UNIT', 'READ',   'View unit details');
INSERT INTO [${schema}].[permissions] ([id], [module], [action], [description]) VALUES ('00000000-0000-0008-0000-000000000003', 'UNIT', 'UPDATE', 'Update unit information');
INSERT INTO [${schema}].[permissions] ([id], [module], [action], [description]) VALUES ('00000000-0000-0008-0000-000000000004', 'UNIT', 'DELETE', 'Delete a unit of measure');
GO
