-- V0__create_schema.sql (MSSQL)
-- SQL Server supports schemas natively via CREATE SCHEMA.
-- The ${schema} placeholder is resolved by Flyway at runtime.

IF NOT EXISTS (SELECT 1 FROM sys.schemas WHERE name = '${schema}')
BEGIN
    EXEC('CREATE SCHEMA [${schema}]');
END
GO
