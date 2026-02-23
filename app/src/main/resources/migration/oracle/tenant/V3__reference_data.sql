-- V3__reference_data.sql (Main Application)
-- Oracle conversion:
--   UUID    -> VARCHAR2(32)
--   BOOLEAN -> NUMBER(1,0)
--   INT     -> NUMBER(10)
--   NUMERIC -> NUMBER

-- ─── Currencies ───
CREATE TABLE "${schema}".currencies (
    code            VARCHAR2(3)   NOT NULL,
    name            VARCHAR2(255) NOT NULL,
    symbol          VARCHAR2(10)  NOT NULL,
    fraction_digits NUMBER(10)    DEFAULT 2 NOT NULL,
    active          NUMBER(1,0)   DEFAULT 1 NOT NULL,
    CONSTRAINT pk_currencies PRIMARY KEY (code)
);

-- ─── Units ───
CREATE TABLE "${schema}".units (
    code   VARCHAR2(50)  NOT NULL,
    name   VARCHAR2(255) NOT NULL,
    type   VARCHAR2(50)  NOT NULL,
    active NUMBER(1,0)   DEFAULT 1 NOT NULL,
    CONSTRAINT pk_units PRIMARY KEY (code)
);

-- ─── Payment Terms ───
CREATE TABLE "${schema}".payment_terms (
    code       VARCHAR2(50)  NOT NULL,
    tenant_id  VARCHAR2(32)       NOT NULL,
    company_id VARCHAR2(32)       NOT NULL,
    name       VARCHAR2(255) NOT NULL,
    due_days   NUMBER(10)    DEFAULT 0 NOT NULL,
    active     NUMBER(1,0)   DEFAULT 1 NOT NULL,
    CONSTRAINT pk_payment_terms PRIMARY KEY (code),
    CONSTRAINT fk_pt_company FOREIGN KEY (company_id)
        REFERENCES "${schema}".companies (id)
);

CREATE INDEX idx_payment_term_tenant_company ON "${schema}".payment_terms (tenant_id, company_id);

-- ─── Taxes ───
CREATE TABLE "${schema}".taxes (
    code       VARCHAR2(50)   NOT NULL,
    tenant_id  VARCHAR2(32)        NOT NULL,
    company_id VARCHAR2(32)        NOT NULL,
    name       VARCHAR2(255)  NOT NULL,
    type       VARCHAR2(50)   NOT NULL,
    rate       NUMBER(10,4)   NOT NULL,
    active     NUMBER(1,0)    DEFAULT 1 NOT NULL,
    CONSTRAINT pk_taxes PRIMARY KEY (code),
    CONSTRAINT fk_tax_company FOREIGN KEY (company_id)
        REFERENCES "${schema}".companies (id)
);

CREATE INDEX idx_tax_tenant_company ON "${schema}".taxes (tenant_id, company_id);

-- ─── Default Permissions ───

-- GENERAL
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (HEXTORAW('00000000000000000000000000000001'), 'GENERAL', 'ADMIN',
        'Super admin — full access to all modules and actions');

-- COMPANY
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (HEXTORAW('00000000000000010000000000000001'), 'COMPANY', 'CREATE', 'Create a new company');
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (HEXTORAW('00000000000000010000000000000002'), 'COMPANY', 'READ', 'View company details');
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (HEXTORAW('00000000000000010000000000000003'), 'COMPANY', 'UPDATE', 'Update company information');
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (HEXTORAW('00000000000000010000000000000004'), 'COMPANY', 'DELETE', 'Delete a company');

-- DEPARTMENT
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (HEXTORAW('00000000000000020000000000000001'), 'DEPARTMENT', 'CREATE', 'Create a new department');
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (HEXTORAW('00000000000000020000000000000002'), 'DEPARTMENT', 'READ', 'View department details');
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (HEXTORAW('00000000000000020000000000000003'), 'DEPARTMENT', 'UPDATE', 'Update department information');
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (HEXTORAW('00000000000000020000000000000004'), 'DEPARTMENT', 'DELETE', 'Delete a department');

-- EMPLOYEE
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (HEXTORAW('00000000000000030000000000000001'), 'EMPLOYEE', 'CREATE', 'Create a new employee');
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (HEXTORAW('00000000000000030000000000000002'), 'EMPLOYEE', 'READ', 'View employee details');
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (HEXTORAW('00000000000000030000000000000003'), 'EMPLOYEE', 'UPDATE', 'Update employee information');
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (HEXTORAW('00000000000000030000000000000004'), 'EMPLOYEE', 'DELETE', 'Delete an employee');

-- BUSINESS_PARTNER
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (HEXTORAW('00000000000000040000000000000001'), 'BUSINESS_PARTNER', 'CREATE', 'Create a new business partner');
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (HEXTORAW('00000000000000040000000000000002'), 'BUSINESS_PARTNER', 'READ', 'View business partner details');
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (HEXTORAW('00000000000000040000000000000003'), 'BUSINESS_PARTNER', 'UPDATE', 'Update business partner information');
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (HEXTORAW('00000000000000040000000000000004'), 'BUSINESS_PARTNER', 'DELETE', 'Delete a business partner');

-- CURRENCY
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (HEXTORAW('00000000000000050000000000000001'), 'CURRENCY', 'CREATE', 'Create a new currency');
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (HEXTORAW('00000000000000050000000000000002'), 'CURRENCY', 'READ', 'View currency details');
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (HEXTORAW('00000000000000050000000000000003'), 'CURRENCY', 'UPDATE', 'Update currency information');
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (HEXTORAW('00000000000000050000000000000004'), 'CURRENCY', 'DELETE', 'Delete a currency');

-- PAYMENT_TERM
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (HEXTORAW('00000000000000060000000000000001'), 'PAYMENT_TERM', 'CREATE', 'Create a new payment term');
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (HEXTORAW('00000000000000060000000000000002'), 'PAYMENT_TERM', 'READ', 'View payment term details');
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (HEXTORAW('00000000000000060000000000000003'), 'PAYMENT_TERM', 'UPDATE', 'Update payment term information');
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (HEXTORAW('00000000000000060000000000000004'), 'PAYMENT_TERM', 'DELETE', 'Delete a payment term');

-- TAX
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (HEXTORAW('00000000000000070000000000000001'), 'TAX', 'CREATE', 'Create a new tax definition');
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (HEXTORAW('00000000000000070000000000000002'), 'TAX', 'READ', 'View tax details');
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (HEXTORAW('00000000000000070000000000000003'), 'TAX', 'UPDATE', 'Update tax information');
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (HEXTORAW('00000000000000070000000000000004'), 'TAX', 'DELETE', 'Delete a tax definition');

-- UNIT
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (HEXTORAW('00000000000000080000000000000001'), 'UNIT', 'CREATE', 'Create a new unit of measure');
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (HEXTORAW('00000000000000080000000000000002'), 'UNIT', 'READ', 'View unit details');
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (HEXTORAW('00000000000000080000000000000003'), 'UNIT', 'UPDATE', 'Update unit information');
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (HEXTORAW('00000000000000080000000000000004'), 'UNIT', 'DELETE', 'Delete a unit of measure');

COMMIT;
