-- V5__idempotency.sql (Idempotency table - Oracle)
-- Uses CLOB for large JSON payloads

CREATE TABLE "${schema}".idempotencies (
    id VARCHAR2(32) NOT NULL,
    response TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT pk_idempotencies PRIMARY KEY (id)
);

CREATE INDEX idx_idempotencies_created_at ON "${schema}".idempotencies (created_at);
