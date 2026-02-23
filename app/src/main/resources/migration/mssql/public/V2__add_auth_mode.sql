-- V2__add_auth_mode.sql (Tenant Service - MSSQL)

ALTER TABLE [${schema}].[tenants]
    ADD [auth_mode] NVARCHAR(20) NOT NULL DEFAULT 'LOCAL';
GO
