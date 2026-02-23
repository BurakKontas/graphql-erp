-- V1__init.sql (Tenant Service - MSSQL)
-- UUID -> UNIQUEIDENTIFIER, VARCHAR -> NVARCHAR

IF NOT EXISTS (SELECT 1 FROM sys.schemas WHERE name = '${schema}')
    EXEC('CREATE SCHEMA [${schema}]');
GO

CREATE TABLE [${schema}].[tenants] (
    [id]   UNIQUEIDENTIFIER NOT NULL,
    [code] NVARCHAR(255)    NOT NULL,
    [name] NVARCHAR(255)    NOT NULL,
    CONSTRAINT [PK_tenants]      PRIMARY KEY ([id]),
    CONSTRAINT [UQ_tenants_code] UNIQUE      ([code])
);
GO
