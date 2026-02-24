-- V5__idempotency.sql (Idempotency table - PostgreSQL)
-- Stores responses for idempotency keys as JSONB

CREATE TABLE IF NOT EXISTS ${schema}.idempotencies (
    id uuid NOT NULL,
    response TEXT NOT NULL,
    created_at timestamptz NOT NULL DEFAULT now(),
    CONSTRAINT pk_idempotencies PRIMARY KEY (id)
);

-- index to speed up lookups by created_at if needed
CREATE INDEX IF NOT EXISTS idx_idempotencies_created_at ON ${schema}.idempotencies (created_at);
