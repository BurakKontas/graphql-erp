-- ─── Accounts (Chart of Accounts) ───

CREATE TABLE accounts
(
    id                UUID PRIMARY KEY,
    tenant_id         UUID         NOT NULL,
    company_id        UUID         NOT NULL,
    code              VARCHAR(50)  NOT NULL,
    name              VARCHAR(200) NOT NULL,
    type              VARCHAR(50)  NOT NULL,
    nature            VARCHAR(50)  NOT NULL,
    parent_account_id UUID,
    system_account    BOOLEAN      NOT NULL DEFAULT FALSE,
    active            BOOLEAN      NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_account_company FOREIGN KEY (company_id) REFERENCES companies (id),
    CONSTRAINT fk_account_parent FOREIGN KEY (parent_account_id) REFERENCES accounts (id),
    CONSTRAINT uq_account_code UNIQUE (tenant_id, company_id, code)
);

CREATE INDEX idx_account_tenant ON accounts (tenant_id);
CREATE INDEX idx_account_company ON accounts (company_id);

-- ─── Accounting Periods ───

CREATE TABLE accounting_periods
(
    id          UUID PRIMARY KEY,
    tenant_id   UUID        NOT NULL,
    company_id  UUID        NOT NULL,
    period_type VARCHAR(20) NOT NULL,
    start_date  DATE        NOT NULL,
    end_date    DATE        NOT NULL,
    status      VARCHAR(20) NOT NULL DEFAULT 'OPEN',

    CONSTRAINT fk_period_company FOREIGN KEY (company_id) REFERENCES companies (id)
);

CREATE INDEX idx_period_tenant ON accounting_periods (tenant_id);
CREATE INDEX idx_period_company ON accounting_periods (company_id);

-- ─── Journal Entries ───

CREATE TABLE journal_entries
(
    id              UUID PRIMARY KEY,
    tenant_id       UUID         NOT NULL,
    company_id      UUID         NOT NULL,
    entry_number    VARCHAR(20)  NOT NULL UNIQUE,
    type            VARCHAR(50)  NOT NULL,
    period_id       UUID,
    entry_date      DATE         NOT NULL,
    description     TEXT,
    reference_type  VARCHAR(100),
    reference_id    VARCHAR(100),
    currency_code   VARCHAR(10),
    exchange_rate   NUMERIC(19, 6) DEFAULT 1,
    status          VARCHAR(20)  NOT NULL DEFAULT 'DRAFT',

    CONSTRAINT fk_je_company FOREIGN KEY (company_id) REFERENCES companies (id),
    CONSTRAINT fk_je_period FOREIGN KEY (period_id) REFERENCES accounting_periods (id)
);

CREATE INDEX idx_je_tenant ON journal_entries (tenant_id);
CREATE INDEX idx_je_company ON journal_entries (company_id);
CREATE INDEX idx_je_reference ON journal_entries (reference_type, reference_id);

-- ─── Journal Entry Lines ───

CREATE TABLE journal_entry_lines
(
    id            UUID PRIMARY KEY,
    entry_id      UUID        NOT NULL,
    account_id    VARCHAR(100) NOT NULL,
    account_code  VARCHAR(50),
    account_name  VARCHAR(200),
    debit_amount  NUMERIC(19, 2) DEFAULT 0,
    credit_amount NUMERIC(19, 2) DEFAULT 0,
    description   TEXT,

    CONSTRAINT fk_jel_entry FOREIGN KEY (entry_id) REFERENCES journal_entries (id) ON DELETE CASCADE
);

CREATE INDEX idx_jel_entry ON journal_entry_lines (entry_id);

-- ─── Sales Invoices ───

CREATE TABLE sales_invoices
(
    id                 UUID PRIMARY KEY,
    tenant_id          UUID        NOT NULL,
    company_id         UUID        NOT NULL,
    invoice_number     VARCHAR(20) NOT NULL UNIQUE,
    invoice_type       VARCHAR(20) NOT NULL DEFAULT 'STANDARD',
    sales_order_id     VARCHAR(100),
    sales_order_number VARCHAR(50),
    customer_id        VARCHAR(100),
    customer_name      VARCHAR(200),
    invoice_date       DATE        NOT NULL,
    due_date           DATE,
    currency_code      VARCHAR(10),
    exchange_rate      NUMERIC(19, 6) DEFAULT 1,
    status             VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    subtotal           NUMERIC(19, 2) DEFAULT 0,
    tax_total          NUMERIC(19, 2) DEFAULT 0,
    total              NUMERIC(19, 2) DEFAULT 0,
    paid_amount        NUMERIC(19, 2) DEFAULT 0,

    CONSTRAINT fk_sinv_company FOREIGN KEY (company_id) REFERENCES companies (id)
);

CREATE INDEX idx_sinv_tenant ON sales_invoices (tenant_id);
CREATE INDEX idx_sinv_company ON sales_invoices (company_id);

-- ─── Sales Invoice Lines ───

CREATE TABLE sales_invoice_lines
(
    id                  UUID PRIMARY KEY,
    invoice_id          UUID NOT NULL,
    sales_order_line_id VARCHAR(100),
    item_id             VARCHAR(100),
    item_description    VARCHAR(500) NOT NULL,
    unit_code           VARCHAR(50),
    quantity            NUMERIC(19, 4),
    unit_price          NUMERIC(19, 6),
    tax_code            VARCHAR(50),
    tax_rate            NUMERIC(10, 4) DEFAULT 0,
    line_total          NUMERIC(19, 2) DEFAULT 0,
    tax_amount          NUMERIC(19, 2) DEFAULT 0,
    line_total_with_tax NUMERIC(19, 2) DEFAULT 0,
    revenue_account_id  VARCHAR(100),

    CONSTRAINT fk_sinvl_invoice FOREIGN KEY (invoice_id) REFERENCES sales_invoices (id) ON DELETE CASCADE
);

CREATE INDEX idx_sinvl_invoice ON sales_invoice_lines (invoice_id);

-- ─── Credit Notes ───

CREATE TABLE credit_notes
(
    id                 UUID PRIMARY KEY,
    tenant_id          UUID        NOT NULL,
    company_id         UUID        NOT NULL,
    credit_note_number VARCHAR(20) NOT NULL UNIQUE,
    invoice_id         VARCHAR(100),
    customer_id        VARCHAR(100),
    credit_note_date   DATE        NOT NULL,
    currency_code      VARCHAR(10),
    status             VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    total              NUMERIC(19, 2) DEFAULT 0,
    applied_amount     NUMERIC(19, 2) DEFAULT 0,
    reason             TEXT,

    CONSTRAINT fk_cn_company FOREIGN KEY (company_id) REFERENCES companies (id)
);

CREATE INDEX idx_cn_tenant ON credit_notes (tenant_id);

-- ─── Credit Note Lines ───

CREATE TABLE credit_note_lines
(
    id                  UUID PRIMARY KEY,
    credit_note_id      UUID NOT NULL,
    item_id             VARCHAR(100),
    item_description    VARCHAR(500),
    unit_code           VARCHAR(50),
    quantity            NUMERIC(19, 4),
    unit_price          NUMERIC(19, 6),
    tax_code            VARCHAR(50),
    tax_rate            NUMERIC(10, 4) DEFAULT 0,
    line_total          NUMERIC(19, 2) DEFAULT 0,
    tax_amount          NUMERIC(19, 2) DEFAULT 0,
    line_total_with_tax NUMERIC(19, 2) DEFAULT 0,

    CONSTRAINT fk_cnl_cn FOREIGN KEY (credit_note_id) REFERENCES credit_notes (id) ON DELETE CASCADE
);

CREATE INDEX idx_cnl_cn ON credit_note_lines (credit_note_id);

-- ─── Payments ───

CREATE TABLE payments
(
    id               UUID PRIMARY KEY,
    tenant_id        UUID           NOT NULL,
    company_id       UUID           NOT NULL,
    payment_number   VARCHAR(20)    NOT NULL UNIQUE,
    payment_type     VARCHAR(20)    NOT NULL,
    invoice_id       VARCHAR(100),
    customer_id      VARCHAR(100),
    payment_date     DATE           NOT NULL,
    amount           NUMERIC(19, 2) NOT NULL,
    currency_code    VARCHAR(10),
    exchange_rate    NUMERIC(19, 6) DEFAULT 1,
    payment_method   VARCHAR(30)    NOT NULL,
    status           VARCHAR(20)    NOT NULL DEFAULT 'DRAFT',
    bank_account_ref VARCHAR(100),
    reference_number VARCHAR(100),

    CONSTRAINT fk_pay_company FOREIGN KEY (company_id) REFERENCES companies (id)
);

CREATE INDEX idx_pay_tenant ON payments (tenant_id);
CREATE INDEX idx_pay_invoice ON payments (invoice_id);

-- ─── Expenses ───

CREATE TABLE expenses
(
    id                 UUID PRIMARY KEY,
    tenant_id          UUID           NOT NULL,
    company_id         UUID           NOT NULL,
    expense_number     VARCHAR(20)    NOT NULL UNIQUE,
    description        TEXT           NOT NULL,
    category           VARCHAR(100),
    submitted_by       VARCHAR(100),
    expense_date       DATE           NOT NULL,
    amount             NUMERIC(19, 2) NOT NULL,
    currency_code      VARCHAR(10),
    status             VARCHAR(20)    NOT NULL DEFAULT 'DRAFT',
    approved_by        VARCHAR(100),
    receipt_reference  VARCHAR(100),
    expense_account_id VARCHAR(100),

    CONSTRAINT fk_exp_company FOREIGN KEY (company_id) REFERENCES companies (id)
);

CREATE INDEX idx_exp_tenant ON expenses (tenant_id);
CREATE INDEX idx_exp_company ON expenses (company_id);

-- ─── Finance Permissions ───

INSERT INTO permissions (id, module, action, description)
VALUES
    -- ACCOUNT
    ('00000000-0000-0000-00F1-000000000001', 'ACCOUNT', 'CREATE', 'Create a new account'),
    ('00000000-0000-0000-00F1-000000000002', 'ACCOUNT', 'READ', 'View account details'),
    ('00000000-0000-0000-00F1-000000000003', 'ACCOUNT', 'UPDATE', 'Update account information'),
    ('00000000-0000-0000-00F1-000000000004', 'ACCOUNT', 'DELETE', 'Delete an account'),
    -- ACCOUNTING_PERIOD
    ('00000000-0000-0000-00F2-000000000001', 'ACCOUNTING_PERIOD', 'CREATE', 'Create a new accounting period'),
    ('00000000-0000-0000-00F2-000000000002', 'ACCOUNTING_PERIOD', 'READ', 'View accounting period details'),
    ('00000000-0000-0000-00F2-000000000003', 'ACCOUNTING_PERIOD', 'UPDATE', 'Update accounting period'),
    ('00000000-0000-0000-00F2-000000000004', 'ACCOUNTING_PERIOD', 'DELETE', 'Delete an accounting period'),
    -- JOURNAL_ENTRY
    ('00000000-0000-0000-00F3-000000000001', 'JOURNAL_ENTRY', 'CREATE', 'Create a new journal entry'),
    ('00000000-0000-0000-00F3-000000000002', 'JOURNAL_ENTRY', 'READ', 'View journal entry details'),
    ('00000000-0000-0000-00F3-000000000003', 'JOURNAL_ENTRY', 'UPDATE', 'Update journal entry'),
    ('00000000-0000-0000-00F3-000000000004', 'JOURNAL_ENTRY', 'DELETE', 'Delete a journal entry'),
    -- SALES_INVOICE
    ('00000000-0000-0000-00F4-000000000001', 'SALES_INVOICE', 'CREATE', 'Create a new sales invoice'),
    ('00000000-0000-0000-00F4-000000000002', 'SALES_INVOICE', 'READ', 'View sales invoice details'),
    ('00000000-0000-0000-00F4-000000000003', 'SALES_INVOICE', 'UPDATE', 'Update sales invoice'),
    ('00000000-0000-0000-00F4-000000000004', 'SALES_INVOICE', 'DELETE', 'Delete a sales invoice'),
    -- PAYMENT
    ('00000000-0000-0000-00F5-000000000001', 'PAYMENT', 'CREATE', 'Create a new payment'),
    ('00000000-0000-0000-00F5-000000000002', 'PAYMENT', 'READ', 'View payment details'),
    ('00000000-0000-0000-00F5-000000000003', 'PAYMENT', 'UPDATE', 'Update payment'),
    ('00000000-0000-0000-00F5-000000000004', 'PAYMENT', 'DELETE', 'Delete a payment'),
    -- CREDIT_NOTE
    ('00000000-0000-0000-00F6-000000000001', 'CREDIT_NOTE', 'CREATE', 'Create a new credit note'),
    ('00000000-0000-0000-00F6-000000000002', 'CREDIT_NOTE', 'READ', 'View credit note details'),
    ('00000000-0000-0000-00F6-000000000003', 'CREDIT_NOTE', 'UPDATE', 'Update credit note'),
    ('00000000-0000-0000-00F6-000000000004', 'CREDIT_NOTE', 'DELETE', 'Delete a credit note'),
    -- EXPENSE
    ('00000000-0000-0000-00F7-000000000001', 'EXPENSE', 'CREATE', 'Create a new expense'),
    ('00000000-0000-0000-00F7-000000000002', 'EXPENSE', 'READ', 'View expense details'),
    ('00000000-0000-0000-00F7-000000000003', 'EXPENSE', 'UPDATE', 'Update expense'),
    ('00000000-0000-0000-00F7-000000000004', 'EXPENSE', 'DELETE', 'Delete an expense');

-- ─── Update default role permissions ───
INSERT INTO role_permissions (role_id, permission_id)
SELECT md5('ADMIN_${tenant_id}')::uuid, p.id
FROM permissions p
WHERE p.module IN ('ACCOUNT', 'ACCOUNTING_PERIOD', 'JOURNAL_ENTRY', 'SALES_INVOICE', 'PAYMENT', 'CREDIT_NOTE', 'EXPENSE')
ON CONFLICT DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT md5('MANAGER_${tenant_id}')::uuid, p.id
FROM permissions p
WHERE p.module IN ('ACCOUNT', 'ACCOUNTING_PERIOD', 'JOURNAL_ENTRY', 'SALES_INVOICE', 'PAYMENT', 'CREDIT_NOTE', 'EXPENSE')
  AND p.action != 'DELETE'
ON CONFLICT DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT md5('USER_${tenant_id}')::uuid, p.id
FROM permissions p
WHERE p.module IN ('ACCOUNT', 'ACCOUNTING_PERIOD', 'JOURNAL_ENTRY', 'SALES_INVOICE', 'PAYMENT', 'CREDIT_NOTE', 'EXPENSE')
  AND p.action IN ('CREATE', 'READ', 'UPDATE')
ON CONFLICT DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT md5('VIEWER_${tenant_id}')::uuid, p.id
FROM permissions p
WHERE p.module IN ('ACCOUNT', 'ACCOUNTING_PERIOD', 'JOURNAL_ENTRY', 'SALES_INVOICE', 'PAYMENT', 'CREDIT_NOTE', 'EXPENSE')
  AND p.action = 'READ'
ON CONFLICT DO NOTHING;
