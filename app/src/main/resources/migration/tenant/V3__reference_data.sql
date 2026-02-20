-- Currencies table (global, not tenant-scoped)
CREATE TABLE currencies
(
    code            VARCHAR(3)   NOT NULL PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    symbol          VARCHAR(10)  NOT NULL,
    fraction_digits INT          NOT NULL DEFAULT 2,
    active          BOOLEAN      NOT NULL DEFAULT TRUE
);

-- Units table (global, not tenant-scoped)
CREATE TABLE units
(
    code   VARCHAR(50)  NOT NULL PRIMARY KEY,
    name   VARCHAR(255) NOT NULL,
    type   VARCHAR(50)  NOT NULL,
    active BOOLEAN      NOT NULL DEFAULT TRUE
);

-- Payment Terms table (tenant + company scoped)
CREATE TABLE payment_terms
(
    code       VARCHAR(50)  NOT NULL PRIMARY KEY,
    tenant_id  UUID         NOT NULL,
    company_id UUID         NOT NULL,
    name       VARCHAR(255) NOT NULL,
    due_days   INT          NOT NULL DEFAULT 0,
    active     BOOLEAN      NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_payment_term_company
        FOREIGN KEY (company_id) REFERENCES companies (id)
);

CREATE INDEX idx_payment_term_tenant_company ON payment_terms (tenant_id, company_id);

-- Taxes table (tenant + company scoped)
CREATE TABLE taxes
(
    code       VARCHAR(50)    NOT NULL PRIMARY KEY,
    tenant_id  UUID           NOT NULL,
    company_id UUID           NOT NULL,
    name       VARCHAR(255)   NOT NULL,
    type       VARCHAR(50)    NOT NULL,
    rate       NUMERIC(10, 4) NOT NULL,
    active     BOOLEAN        NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_tax_company
        FOREIGN KEY (company_id) REFERENCES companies (id)
);

CREATE INDEX idx_tax_tenant_company ON taxes (tenant_id, company_id);
