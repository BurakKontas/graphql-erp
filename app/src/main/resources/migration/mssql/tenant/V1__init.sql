-- V1__init.sql (Main Application - MSSQL)
-- UUID -> UNIQUEIDENTIFIER, BOOLEAN -> BIT, VARCHAR -> NVARCHAR
-- CREATE SCHEMA / SET search_path -> not needed (use [schema].[table] prefix)

IF NOT EXISTS (SELECT 1 FROM sys.schemas WHERE name = '${schema}')
    EXEC('CREATE SCHEMA [${schema}]');
GO

-- ─── Companies ───
CREATE TABLE [${schema}].[companies] (
    [id]        UNIQUEIDENTIFIER NOT NULL,
    [active]    BIT              NOT NULL,
    [name]      NVARCHAR(255)    NOT NULL,
    [tenant_id] UNIQUEIDENTIFIER NOT NULL,
    CONSTRAINT [PK_companies] PRIMARY KEY ([id])
);
GO

-- ─── Departments ───
CREATE TABLE [${schema}].[departments] (
    [id]         UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]  UNIQUEIDENTIFIER NOT NULL,
    [company_id] UNIQUEIDENTIFIER NOT NULL,
    [name]       NVARCHAR(255)    NOT NULL,
    [parent_id]  UNIQUEIDENTIFIER NULL,
    [active]     BIT              NOT NULL DEFAULT 1,
    CONSTRAINT [PK_departments]       PRIMARY KEY ([id]),
    CONSTRAINT [FK_dept_parent]       FOREIGN KEY ([parent_id])
        REFERENCES [${schema}].[departments] ([id])
);
GO

CREATE INDEX [idx_department_tenant_id]  ON [${schema}].[departments] ([tenant_id]);
CREATE INDEX [idx_department_company_id] ON [${schema}].[departments] ([company_id]);
GO

-- Department sub-departments (ElementCollection)
CREATE TABLE [${schema}].[department_sub_departments] (
    [department_id]     UNIQUEIDENTIFIER NOT NULL,
    [sub_department_id] UNIQUEIDENTIFIER NOT NULL,
    CONSTRAINT [PK_dept_sub]        PRIMARY KEY ([department_id], [sub_department_id]),
    CONSTRAINT [FK_sub_dept_parent] FOREIGN KEY ([department_id])
        REFERENCES [${schema}].[departments] ([id]),
    CONSTRAINT [FK_sub_dept_child]  FOREIGN KEY ([sub_department_id])
        REFERENCES [${schema}].[departments] ([id])
);
GO

-- ─── Employees ───
CREATE TABLE [${schema}].[employees] (
    [id]            UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]     UNIQUEIDENTIFIER NOT NULL,
    [name]          NVARCHAR(255)    NOT NULL,
    [department_id] UNIQUEIDENTIFIER NOT NULL,
    [active]        BIT              NOT NULL DEFAULT 1,
    CONSTRAINT [PK_employees]        PRIMARY KEY ([id]),
    CONSTRAINT [FK_emp_department]   FOREIGN KEY ([department_id])
        REFERENCES [${schema}].[departments] ([id])
);
GO

CREATE INDEX [idx_employee_tenant_id]     ON [${schema}].[employees] ([tenant_id]);
CREATE INDEX [idx_employee_department_id] ON [${schema}].[employees] ([department_id]);
GO

-- ─── Business Partners ───
CREATE TABLE [${schema}].[business_partner] (
    [id]         UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]  UNIQUEIDENTIFIER NOT NULL,
    [company_id] UNIQUEIDENTIFIER NOT NULL,
    [code]       NVARCHAR(255)    NOT NULL,
    [name]       NVARCHAR(255)    NOT NULL,
    [tax_number] NVARCHAR(255)    NOT NULL,
    [active]     BIT              NOT NULL DEFAULT 1,
    CONSTRAINT [PK_business_partner]    PRIMARY KEY ([id]),
    CONSTRAINT [FK_bp_company]          FOREIGN KEY ([company_id])
        REFERENCES [${schema}].[companies] ([id])
);
GO

CREATE INDEX [idx_bp_tenant_id]  ON [${schema}].[business_partner] ([tenant_id]);
CREATE INDEX [idx_bp_company_id] ON [${schema}].[business_partner] ([company_id]);
CREATE INDEX [idx_bp_code]       ON [${schema}].[business_partner] ([code]);
GO

-- Business Partner Roles
CREATE TABLE [${schema}].[business_partner_roles] (
    [business_partner_id] UNIQUEIDENTIFIER NOT NULL,
    [role]                NVARCHAR(100)    NOT NULL,
    CONSTRAINT [PK_bp_roles]        PRIMARY KEY ([business_partner_id], [role]),
    CONSTRAINT [FK_bp_roles_partner] FOREIGN KEY ([business_partner_id])
        REFERENCES [${schema}].[business_partner] ([id])
        ON DELETE CASCADE
);
GO

CREATE INDEX [idx_bp_roles_partner_id] ON [${schema}].[business_partner_roles] ([business_partner_id]);
GO
