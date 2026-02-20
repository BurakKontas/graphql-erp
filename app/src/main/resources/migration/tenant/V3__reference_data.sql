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

-- ─── Default Permissions ───

-- GENERAL module
INSERT INTO permissions (id, module, action, description)
VALUES ('00000000-0000-0000-0000-000000000001', 'GENERAL', 'ADMIN',
        'Super admin — full access to all modules and actions');

-- COMPANY module
INSERT INTO permissions (id, module, action, description)
VALUES ('00000000-0000-0000-0001-000000000001', 'COMPANY', 'CREATE', 'Create a new company'),
       ('00000000-0000-0000-0001-000000000002', 'COMPANY', 'READ', 'View company details'),
       ('00000000-0000-0000-0001-000000000003', 'COMPANY', 'UPDATE', 'Update company information'),
       ('00000000-0000-0000-0001-000000000004', 'COMPANY', 'DELETE', 'Delete a company');

-- DEPARTMENT module
INSERT INTO permissions (id, module, action, description)
VALUES ('00000000-0000-0000-0002-000000000001', 'DEPARTMENT', 'CREATE', 'Create a new department'),
       ('00000000-0000-0000-0002-000000000002', 'DEPARTMENT', 'READ', 'View department details'),
       ('00000000-0000-0000-0002-000000000003', 'DEPARTMENT', 'UPDATE', 'Update department information'),
       ('00000000-0000-0000-0002-000000000004', 'DEPARTMENT', 'DELETE', 'Delete a department');

-- EMPLOYEE module
INSERT INTO permissions (id, module, action, description)
VALUES ('00000000-0000-0000-0003-000000000001', 'EMPLOYEE', 'CREATE', 'Create a new employee'),
       ('00000000-0000-0000-0003-000000000002', 'EMPLOYEE', 'READ', 'View employee details'),
       ('00000000-0000-0000-0003-000000000003', 'EMPLOYEE', 'UPDATE', 'Update employee information'),
       ('00000000-0000-0000-0003-000000000004', 'EMPLOYEE', 'DELETE', 'Delete an employee');

-- BUSINESS_PARTNER module
INSERT INTO permissions (id, module, action, description)
VALUES ('00000000-0000-0000-0004-000000000001', 'BUSINESS_PARTNER', 'CREATE', 'Create a new business partner'),
       ('00000000-0000-0000-0004-000000000002', 'BUSINESS_PARTNER', 'READ', 'View business partner details'),
       ('00000000-0000-0000-0004-000000000003', 'BUSINESS_PARTNER', 'UPDATE', 'Update business partner information'),
       ('00000000-0000-0000-0004-000000000004', 'BUSINESS_PARTNER', 'DELETE', 'Delete a business partner');

-- CURRENCY module
INSERT INTO permissions (id, module, action, description)
VALUES ('00000000-0000-0000-0005-000000000001', 'CURRENCY', 'CREATE', 'Create a new currency'),
       ('00000000-0000-0000-0005-000000000002', 'CURRENCY', 'READ', 'View currency details'),
       ('00000000-0000-0000-0005-000000000003', 'CURRENCY', 'UPDATE', 'Update currency information'),
       ('00000000-0000-0000-0005-000000000004', 'CURRENCY', 'DELETE', 'Delete a currency');

-- PAYMENT_TERM module
INSERT INTO permissions (id, module, action, description)
VALUES ('00000000-0000-0000-0006-000000000001', 'PAYMENT_TERM', 'CREATE', 'Create a new payment term'),
       ('00000000-0000-0000-0006-000000000002', 'PAYMENT_TERM', 'READ', 'View payment term details'),
       ('00000000-0000-0000-0006-000000000003', 'PAYMENT_TERM', 'UPDATE', 'Update payment term information'),
       ('00000000-0000-0000-0006-000000000004', 'PAYMENT_TERM', 'DELETE', 'Delete a payment term');

-- TAX module
INSERT INTO permissions (id, module, action, description)
VALUES ('00000000-0000-0000-0007-000000000001', 'TAX', 'CREATE', 'Create a new tax definition'),
       ('00000000-0000-0000-0007-000000000002', 'TAX', 'READ', 'View tax details'),
       ('00000000-0000-0000-0007-000000000003', 'TAX', 'UPDATE', 'Update tax information'),
       ('00000000-0000-0000-0007-000000000004', 'TAX', 'DELETE', 'Delete a tax definition');

-- UNIT module
INSERT INTO permissions (id, module, action, description)
VALUES ('00000000-0000-0000-0008-000000000001', 'UNIT', 'CREATE', 'Create a new unit of measure'),
       ('00000000-0000-0000-0008-000000000002', 'UNIT', 'READ', 'View unit details'),
       ('00000000-0000-0000-0008-000000000003', 'UNIT', 'UPDATE', 'Update unit information'),
       ('00000000-0000-0000-0008-000000000004', 'UNIT', 'DELETE', 'Delete a unit of measure');

