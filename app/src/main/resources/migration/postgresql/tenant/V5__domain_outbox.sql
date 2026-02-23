-- Domain Outbox table (transactional outbox pattern — shared across all modules)
CREATE TABLE domain_outbox
(
    id             UUID PRIMARY KEY,
    aggregate_type VARCHAR(100) NOT NULL,
    aggregate_id   UUID         NOT NULL,
    event_type     VARCHAR(500) NOT NULL,
    payload        TEXT         NOT NULL,
    occurred_on    TIMESTAMP    NOT NULL,
    published      BOOLEAN      NOT NULL DEFAULT FALSE,
    published_at   TIMESTAMP
);

CREATE INDEX idx_domain_outbox_unpublished ON domain_outbox (published, occurred_on) WHERE published = FALSE;
