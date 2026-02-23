-- V5__domain_outbox.sql (Main Application)
-- Oracle conversion:
--   BOOLEAN   -> NUMBER(1,0)
--   TIMESTAMP -> TIMESTAMP

CREATE TABLE "${schema}".domain_outbox (
    id             VARCHAR2(32)        NOT NULL,
    aggregate_type VARCHAR2(100)  NOT NULL,
    aggregate_id   VARCHAR2(32)        NOT NULL,
    event_type     VARCHAR2(500)  NOT NULL,
    payload        CLOB           NOT NULL,
    occurred_on    TIMESTAMP      NOT NULL,
    published      NUMBER(1,0)    DEFAULT 0 NOT NULL,
    published_at   TIMESTAMP,
    CONSTRAINT pk_domain_outbox PRIMARY KEY (id)
);

-- Oracle does not support partial indexes (WHERE clause).
-- Use a function-based index or a regular index on (published, occurred_on).
CREATE INDEX idx_domain_outbox_unpublished ON "${schema}".domain_outbox (published, occurred_on);
