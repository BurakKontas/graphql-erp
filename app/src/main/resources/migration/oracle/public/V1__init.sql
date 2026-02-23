-- V1__init.sql (Tenant Service)
-- Oracle conversion: UUID -> RAW(16), VARCHAR -> VARCHAR2

CREATE TABLE "${schema}".tenants (
    id   VARCHAR2(36)    NOT NULL,
    code VARCHAR2(255)  NOT NULL,
    name VARCHAR2(255)  NOT NULL,
    CONSTRAINT pk_tenants PRIMARY KEY (id),
    CONSTRAINT uq_tenants_code UNIQUE (code)
);
