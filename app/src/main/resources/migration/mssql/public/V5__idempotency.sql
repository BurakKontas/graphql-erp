-- V5__idempotency.sql (Idempotency table - MSSQL)
-- Stores responses for idempotency keys as NVARCHAR(MAX) or JSON (depending on version)

CREATE TABLE [${schema}].[idempotencies] (
    [id] UNIQUEIDENTIFIER NOT NULL,
    [response] NVARCHAR(MAX) NOT NULL,
    [created_at] DATETIME2 NOT NULL DEFAULT SYSUTCDATETIME(),
    CONSTRAINT [PK_idempotencies] PRIMARY KEY ([id])
);
GO

CREATE INDEX [idx_idempotencies_created_at] ON [${schema}].[idempotencies] ([created_at]);
GO
