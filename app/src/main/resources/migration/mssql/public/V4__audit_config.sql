-- V4__audit_config.sql (Tenant Service - MSSQL)
-- BOOLEAN -> BIT, IF NOT EXISTS check via system catalog

IF NOT EXISTS (
    SELECT 1 FROM sys.columns
    WHERE object_id = OBJECT_ID('[${schema}].[tenants]')
      AND name = 'audit_unauthenticated'
)
BEGIN
    ALTER TABLE [${schema}].[tenants]
        ADD [audit_unauthenticated] BIT NOT NULL DEFAULT 0;
END
GO
