-- V5__idempotency.sql (Idempotency table - MSSQL)
-- Stores responses for idempotency keys

IF NOT EXISTS (SELECT 1 FROM sys.objects o WHERE o.object_id = OBJECT_ID('[${schema}].idempotencies') AND o.type IN ('U'))
BEGIN
    CREATE TABLE [${schema}].[idempotencies] (
        [id] NVARCHAR(255) NOT NULL PRIMARY KEY,
        [response] NVARCHAR(MAX) NOT NULL,
        [created_at] DATETIME2 NOT NULL DEFAULT SYSUTCDATETIME()
    );
END

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE object_id = OBJECT_ID('[${schema}].idempotencies') AND name = 'idx_idempotencies_created_at')
BEGIN
    CREATE INDEX idx_idempotencies_created_at ON [${schema}].[idempotencies] ([created_at]);
END
