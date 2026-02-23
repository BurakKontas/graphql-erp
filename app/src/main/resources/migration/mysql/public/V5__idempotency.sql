-- V5__idempotency.sql (Idempotency table - MySQL)
-- Stores responses for idempotency keys as JSON

CREATE TABLE IF NOT EXISTS idempotencies (
    id VARCHAR(36) NOT NULL,
    response TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- index for created_at
CREATE INDEX idx_idempotencies_created_at ON idempotencies (created_at);
