-- MySQL adapted version of V5__domain_outbox.sql
CREATE TABLE domain_outbox (
    id VARCHAR(36) PRIMARY KEY,
    aggregate_type VARCHAR(100) NOT NULL,
    aggregate_id VARCHAR(36) NOT NULL,
    event_type VARCHAR(500) NOT NULL,
    payload TEXT NOT NULL,
    occurred_on DATETIME(6) NOT NULL,
    published TINYINT(1) NOT NULL DEFAULT 0,
    published_at DATETIME(6)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_domain_outbox_unpublished ON domain_outbox (published, occurred_on);
-- Note: conditional index (WHERE published = FALSE) not supported in MySQL < 8.0.13; consider a partial index workaround.

