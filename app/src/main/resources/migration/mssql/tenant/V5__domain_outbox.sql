-- V5__domain_outbox.sql (Main Application - MSSQL)
-- BOOLEAN -> BIT, TIMESTAMP -> DATETIME2
-- MSSQL supports filtered indexes (WHERE clause) natively

CREATE TABLE [${schema}].[domain_outbox] (
    [id]             UNIQUEIDENTIFIER NOT NULL,
    [aggregate_type] NVARCHAR(100)    NOT NULL,
    [aggregate_id]   UNIQUEIDENTIFIER NOT NULL,
    [event_type]     NVARCHAR(500)    NOT NULL,
    [payload]        NVARCHAR(MAX)    NOT NULL,
    [occurred_on]    DATETIME2        NOT NULL,
    [published]      BIT              NOT NULL DEFAULT 0,
    [published_at]   DATETIME2        NULL,
    CONSTRAINT [PK_domain_outbox] PRIMARY KEY ([id])
);
GO

-- Filtered index: only unpublished rows (equivalent to PostgreSQL partial index)
CREATE INDEX [idx_domain_outbox_unpublished]
    ON [${schema}].[domain_outbox] ([published], [occurred_on])
    WHERE [published] = 0;
GO
