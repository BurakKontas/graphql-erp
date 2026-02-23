-- V9__finance.sql (Main Application)
-- Oracle conversion:
--   UUID                    -> VARCHAR2(32)
--   BOOLEAN                 -> NUMBER(1,0)
--   TEXT                    -> CLOB
--   NUMERIC / BIGINT        -> NUMBER
--   TIMESTAMP WITH TIME ZONE-> TIMESTAMP WITH TIME ZONE
--   UNIQUE (per table)      -> CONSTRAINT
--   md5(...)::uuid          -> STANDARD_HASH(...,'MD5')

-- ─── Accounts (Chart of Accounts) ───
CREATE TABLE "${schema}".accounts (
    id                VARCHAR2(32)       NOT NULL,
    tenant_id         VARCHAR2(32)       NOT NULL,
    company_id        VARCHAR2(32)       NOT NULL,
    code              VARCHAR2(50)  NOT NULL,
    name              VARCHAR2(200) NOT NULL,
    type              VARCHAR2(50)  NOT NULL,
    nature            VARCHAR2(50)  NOT NULL,
    parent_account_id VARCHAR2(32),
    system_account    NUMBER(1,0)   DEFAULT 0 NOT NULL,
    active            NUMBER(1,0)   DEFAULT 1 NOT NULL,
    CONSTRAINT pk_accounts PRIMARY KEY (id),
    CONSTRAINT fk_account_company FOREIGN KEY (company_id)        REFERENCES "${schema}".companies (id),
    CONSTRAINT fk_account_parent  FOREIGN KEY (parent_account_id) REFERENCES "${schema}".accounts (id),
    CONSTRAINT uq_account_code    UNIQUE (tenant_id, company_id, code)
);

CREATE INDEX idx_account_tenant  ON "${schema}".accounts (tenant_id);
CREATE INDEX idx_account_company ON "${schema}".accounts (company_id);

-- ─── Accounting Periods ───
CREATE TABLE "${schema}".accounting_periods (
    id          VARCHAR2(32)      NOT NULL,
    tenant_id   VARCHAR2(32)      NOT NULL,
    company_id  VARCHAR2(32)      NOT NULL,
    period_type VARCHAR2(20) NOT NULL,
    start_date  DATE         NOT NULL,
    end_date    DATE         NOT NULL,
    status      VARCHAR2(20) DEFAULT 'OPEN' NOT NULL,
    CONSTRAINT pk_accounting_periods PRIMARY KEY (id),
    CONSTRAINT fk_period_company FOREIGN KEY (company_id) REFERENCES "${schema}".companies (id)
);

CREATE INDEX idx_period_tenant  ON "${schema}".accounting_periods (tenant_id);
CREATE INDEX idx_period_company ON "${schema}".accounting_periods (company_id);

-- ─── Journal Entries ───
CREATE TABLE "${schema}".journal_entries (
    id             VARCHAR2(32)        NOT NULL,
    tenant_id      VARCHAR2(32)        NOT NULL,
    company_id     VARCHAR2(32)        NOT NULL,
    entry_number   VARCHAR2(20)   NOT NULL,
    type           VARCHAR2(50)   NOT NULL,
    period_id      VARCHAR2(32),
    entry_date     DATE           NOT NULL,
    description    CLOB,
    reference_type VARCHAR2(100),
    reference_id   VARCHAR2(100),
    currency_code  VARCHAR2(10),
    exchange_rate  NUMBER(19,6)   DEFAULT 1,
    status         VARCHAR2(20)   DEFAULT 'DRAFT' NOT NULL,
    CONSTRAINT pk_journal_entries PRIMARY KEY (id),
    CONSTRAINT uq_je_entry_number UNIQUE (entry_number),
    CONSTRAINT fk_je_company FOREIGN KEY (company_id) REFERENCES "${schema}".companies (id),
    CONSTRAINT fk_je_period  FOREIGN KEY (period_id)  REFERENCES "${schema}".accounting_periods (id)
);

CREATE INDEX idx_je_tenant    ON "${schema}".journal_entries (tenant_id);
CREATE INDEX idx_je_company   ON "${schema}".journal_entries (company_id);
CREATE INDEX idx_je_reference ON "${schema}".journal_entries (reference_type, reference_id);

-- ─── Journal Entry Lines ───
CREATE TABLE "${schema}".journal_entry_lines (
    id            VARCHAR2(32)        NOT NULL,
    entry_id      VARCHAR2(32)        NOT NULL,
    account_id    VARCHAR2(100)  NOT NULL,
    account_code  VARCHAR2(50),
    account_name  VARCHAR2(200),
    debit_amount  NUMBER(19,2)   DEFAULT 0,
    credit_amount NUMBER(19,2)   DEFAULT 0,
    description   CLOB,
    CONSTRAINT pk_journal_entry_lines PRIMARY KEY (id),
    CONSTRAINT fk_jel_entry FOREIGN KEY (entry_id)
        REFERENCES "${schema}".journal_entries (id) ON DELETE CASCADE
);

CREATE INDEX idx_jel_entry ON "${schema}".journal_entry_lines (entry_id);

-- ─── Sales Invoices ───
CREATE TABLE "${schema}".sales_invoices (
    id                 VARCHAR2(32)        NOT NULL,
    tenant_id          VARCHAR2(32)        NOT NULL,
    company_id         VARCHAR2(32)        NOT NULL,
    invoice_number     VARCHAR2(20)   NOT NULL,
    invoice_type       VARCHAR2(20)   DEFAULT 'STANDARD' NOT NULL,
    sales_order_id     VARCHAR2(100),
    sales_order_number VARCHAR2(50),
    customer_id        VARCHAR2(100),
    customer_name      VARCHAR2(200),
    invoice_date       DATE           NOT NULL,
    due_date           DATE,
    currency_code      VARCHAR2(10),
    exchange_rate      NUMBER(19,6)   DEFAULT 1,
    status             VARCHAR2(20)   DEFAULT 'DRAFT' NOT NULL,
    subtotal           NUMBER(19,2)   DEFAULT 0,
    tax_total          NUMBER(19,2)   DEFAULT 0,
    total              NUMBER(19,2)   DEFAULT 0,
    paid_amount        NUMBER(19,2)   DEFAULT 0,
    CONSTRAINT pk_sales_invoices    PRIMARY KEY (id),
    CONSTRAINT uq_sinv_number       UNIQUE (invoice_number),
    CONSTRAINT fk_sinv_company      FOREIGN KEY (company_id) REFERENCES "${schema}".companies (id)
);

CREATE INDEX idx_sinv_tenant  ON "${schema}".sales_invoices (tenant_id);
CREATE INDEX idx_sinv_company ON "${schema}".sales_invoices (company_id);

-- ─── Sales Invoice Lines ───
CREATE TABLE "${schema}".sales_invoice_lines (
    id                  VARCHAR2(32)        NOT NULL,
    invoice_id          VARCHAR2(32)        NOT NULL,
    sales_order_line_id VARCHAR2(100),
    item_id             VARCHAR2(100),
    item_description    VARCHAR2(500)  NOT NULL,
    unit_code           VARCHAR2(50),
    quantity            NUMBER(19,4),
    unit_price          NUMBER(19,6),
    tax_code            VARCHAR2(50),
    tax_rate            NUMBER(10,4)   DEFAULT 0,
    line_total          NUMBER(19,2)   DEFAULT 0,
    tax_amount          NUMBER(19,2)   DEFAULT 0,
    line_total_with_tax NUMBER(19,2)   DEFAULT 0,
    revenue_account_id  VARCHAR2(100),
    CONSTRAINT pk_sales_invoice_lines PRIMARY KEY (id),
    CONSTRAINT fk_sinvl_invoice FOREIGN KEY (invoice_id)
        REFERENCES "${schema}".sales_invoices (id) ON DELETE CASCADE
);

CREATE INDEX idx_sinvl_invoice ON "${schema}".sales_invoice_lines (invoice_id);

-- ─── Credit Notes ───
CREATE TABLE "${schema}".credit_notes (
    id                 VARCHAR2(32)        NOT NULL,
    tenant_id          VARCHAR2(32)        NOT NULL,
    company_id         VARCHAR2(32)        NOT NULL,
    credit_note_number VARCHAR2(20)   NOT NULL,
    invoice_id         VARCHAR2(100),
    customer_id        VARCHAR2(100),
    credit_note_date   DATE           NOT NULL,
    currency_code      VARCHAR2(10),
    status             VARCHAR2(20)   DEFAULT 'DRAFT' NOT NULL,
    total              NUMBER(19,2)   DEFAULT 0,
    applied_amount     NUMBER(19,2)   DEFAULT 0,
    reason             CLOB,
    CONSTRAINT pk_credit_notes  PRIMARY KEY (id),
    CONSTRAINT uq_cn_number     UNIQUE (credit_note_number),
    CONSTRAINT fk_cn_company    FOREIGN KEY (company_id) REFERENCES "${schema}".companies (id)
);

CREATE INDEX idx_cn_tenant ON "${schema}".credit_notes (tenant_id);

-- ─── Credit Note Lines ───
CREATE TABLE "${schema}".credit_note_lines (
    id                  VARCHAR2(32)       NOT NULL,
    credit_note_id      VARCHAR2(32)       NOT NULL,
    item_id             VARCHAR2(100),
    item_description    VARCHAR2(500),
    unit_code           VARCHAR2(50),
    quantity            NUMBER(19,4),
    unit_price          NUMBER(19,6),
    tax_code            VARCHAR2(50),
    tax_rate            NUMBER(10,4)  DEFAULT 0,
    line_total          NUMBER(19,2)  DEFAULT 0,
    tax_amount          NUMBER(19,2)  DEFAULT 0,
    line_total_with_tax NUMBER(19,2)  DEFAULT 0,
    CONSTRAINT pk_credit_note_lines PRIMARY KEY (id),
    CONSTRAINT fk_cnl_cn FOREIGN KEY (credit_note_id)
        REFERENCES "${schema}".credit_notes (id) ON DELETE CASCADE
);

CREATE INDEX idx_cnl_cn ON "${schema}".credit_note_lines (credit_note_id);

-- ─── Payments ───
CREATE TABLE "${schema}".payments (
    id               VARCHAR2(32)        NOT NULL,
    tenant_id        VARCHAR2(32)        NOT NULL,
    company_id       VARCHAR2(32)        NOT NULL,
    payment_number   VARCHAR2(20)   NOT NULL,
    payment_type     VARCHAR2(20)   NOT NULL,
    invoice_id       VARCHAR2(100),
    customer_id      VARCHAR2(100),
    payment_date     DATE           NOT NULL,
    amount           NUMBER(19,2)   NOT NULL,
    currency_code    VARCHAR2(10),
    exchange_rate    NUMBER(19,6)   DEFAULT 1,
    payment_method   VARCHAR2(30)   NOT NULL,
    status           VARCHAR2(20)   DEFAULT 'DRAFT' NOT NULL,
    bank_account_ref VARCHAR2(100),
    reference_number VARCHAR2(100),
    CONSTRAINT pk_payments     PRIMARY KEY (id),
    CONSTRAINT uq_pay_number   UNIQUE (payment_number),
    CONSTRAINT fk_pay_company  FOREIGN KEY (company_id) REFERENCES "${schema}".companies (id)
);

CREATE INDEX idx_pay_tenant  ON "${schema}".payments (tenant_id);
CREATE INDEX idx_pay_invoice ON "${schema}".payments (invoice_id);

-- ─── Expenses ───
CREATE TABLE "${schema}".expenses (
    id                 VARCHAR2(32)        NOT NULL,
    tenant_id          VARCHAR2(32)        NOT NULL,
    company_id         VARCHAR2(32)        NOT NULL,
    expense_number     VARCHAR2(20)   NOT NULL,
    description        CLOB           NOT NULL,
    category           VARCHAR2(100),
    submitted_by       VARCHAR2(100),
    expense_date       DATE           NOT NULL,
    amount             NUMBER(19,2)   NOT NULL,
    currency_code      VARCHAR2(10),
    status             VARCHAR2(20)   DEFAULT 'DRAFT' NOT NULL,
    approved_by        VARCHAR2(100),
    receipt_reference  VARCHAR2(100),
    expense_account_id VARCHAR2(100),
    CONSTRAINT pk_expenses    PRIMARY KEY (id),
    CONSTRAINT uq_exp_number  UNIQUE (expense_number),
    CONSTRAINT fk_exp_company FOREIGN KEY (company_id) REFERENCES "${schema}".companies (id)
);

CREATE INDEX idx_exp_tenant  ON "${schema}".expenses (tenant_id);
CREATE INDEX idx_exp_company ON "${schema}".expenses (company_id);

-- ─── Finance Permissions ───
-- ACCOUNT
MERGE INTO "${schema}".permissions tgt
    USING (
        SELECT HEXTORAW('000000000000000000F1000000000001') id, 'ACCOUNT' module, 'CREATE' action, 'Create a new account' description FROM dual UNION ALL
        SELECT HEXTORAW('000000000000000000F1000000000002'), 'ACCOUNT','READ','View account details' FROM dual UNION ALL
        SELECT HEXTORAW('000000000000000000F1000000000003'), 'ACCOUNT','UPDATE','Update account information' FROM dual UNION ALL
        SELECT HEXTORAW('000000000000000000F1000000000004'), 'ACCOUNT','DELETE','Delete an account' FROM dual UNION ALL

        SELECT HEXTORAW('000000000000000000F2000000000001'), 'ACCOUNTING_PERIOD','CREATE','Create a new accounting period' FROM dual UNION ALL
        SELECT HEXTORAW('000000000000000000F2000000000002'), 'ACCOUNTING_PERIOD','READ','View accounting period details' FROM dual UNION ALL
        SELECT HEXTORAW('000000000000000000F2000000000003'), 'ACCOUNTING_PERIOD','UPDATE','Update accounting period' FROM dual UNION ALL
        SELECT HEXTORAW('000000000000000000F2000000000004'), 'ACCOUNTING_PERIOD','DELETE','Delete an accounting period' FROM dual UNION ALL

        SELECT HEXTORAW('000000000000000000F3000000000001'), 'JOURNAL_ENTRY','CREATE','Create a new journal entry' FROM dual UNION ALL
        SELECT HEXTORAW('000000000000000000F3000000000002'), 'JOURNAL_ENTRY','READ','View journal entry details' FROM dual UNION ALL
        SELECT HEXTORAW('000000000000000000F3000000000003'), 'JOURNAL_ENTRY','UPDATE','Update journal entry' FROM dual UNION ALL
        SELECT HEXTORAW('000000000000000000F3000000000004'), 'JOURNAL_ENTRY','DELETE','Delete a journal entry' FROM dual UNION ALL

        SELECT HEXTORAW('000000000000000000F4000000000001'), 'SALES_INVOICE','CREATE','Create a new sales invoice' FROM dual UNION ALL
        SELECT HEXTORAW('000000000000000000F4000000000002'), 'SALES_INVOICE','READ','View sales invoice details' FROM dual UNION ALL
        SELECT HEXTORAW('000000000000000000F4000000000003'), 'SALES_INVOICE','UPDATE','Update sales invoice' FROM dual UNION ALL
        SELECT HEXTORAW('000000000000000000F4000000000004'), 'SALES_INVOICE','DELETE','Delete a sales invoice' FROM dual UNION ALL

        SELECT HEXTORAW('000000000000000000F5000000000001'), 'PAYMENT','CREATE','Create a new payment' FROM dual UNION ALL
        SELECT HEXTORAW('000000000000000000F5000000000002'), 'PAYMENT','READ','View payment details' FROM dual UNION ALL
        SELECT HEXTORAW('000000000000000000F5000000000003'), 'PAYMENT','UPDATE','Update payment' FROM dual UNION ALL
        SELECT HEXTORAW('000000000000000000F5000000000004'), 'PAYMENT','DELETE','Delete a payment' FROM dual UNION ALL

        SELECT HEXTORAW('000000000000000000F6000000000001'), 'CREDIT_NOTE','CREATE','Create a new credit note' FROM dual UNION ALL
        SELECT HEXTORAW('000000000000000000F6000000000002'), 'CREDIT_NOTE','READ','View credit note details' FROM dual UNION ALL
        SELECT HEXTORAW('000000000000000000F6000000000003'), 'CREDIT_NOTE','UPDATE','Update credit note' FROM dual UNION ALL
        SELECT HEXTORAW('000000000000000000F6000000000004'), 'CREDIT_NOTE','DELETE','Delete a credit note' FROM dual UNION ALL

        SELECT HEXTORAW('000000000000000000F7000000000001'), 'EXPENSE','CREATE','Create a new expense' FROM dual UNION ALL
        SELECT HEXTORAW('000000000000000000F7000000000002'), 'EXPENSE','READ','View expense details' FROM dual UNION ALL
        SELECT HEXTORAW('000000000000000000F7000000000003'), 'EXPENSE','UPDATE','Update expense' FROM dual UNION ALL
        SELECT HEXTORAW('000000000000000000F7000000000004'), 'EXPENSE','DELETE','Delete an expense' FROM dual
    ) src
    ON (tgt.id = src.id)
    WHEN NOT MATCHED THEN
        INSERT (id, module, action, description)
            VALUES (src.id, src.module, src.action, src.description);

-- ─── Update default role permissions for Finance ───
MERGE INTO "${schema}".role_permissions tgt
USING (SELECT STANDARD_HASH('ADMIN_${tenant_id}', 'MD5') AS role_id, p.id AS permission_id
       FROM "${schema}".permissions p
       WHERE p.module IN ('ACCOUNT','ACCOUNTING_PERIOD','JOURNAL_ENTRY','SALES_INVOICE','PAYMENT','CREDIT_NOTE','EXPENSE')) src
ON (tgt.role_id = src.role_id AND tgt.permission_id = src.permission_id)
WHEN NOT MATCHED THEN INSERT (role_id, permission_id) VALUES (src.role_id, src.permission_id);

MERGE INTO "${schema}".role_permissions tgt
USING (SELECT STANDARD_HASH('MANAGER_${tenant_id}', 'MD5') AS role_id, p.id AS permission_id
       FROM "${schema}".permissions p
       WHERE p.module IN ('ACCOUNT','ACCOUNTING_PERIOD','JOURNAL_ENTRY','SALES_INVOICE','PAYMENT','CREDIT_NOTE','EXPENSE')
         AND p.action != 'DELETE') src
ON (tgt.role_id = src.role_id AND tgt.permission_id = src.permission_id)
WHEN NOT MATCHED THEN INSERT (role_id, permission_id) VALUES (src.role_id, src.permission_id);

MERGE INTO "${schema}".role_permissions tgt
USING (SELECT STANDARD_HASH('USER_${tenant_id}', 'MD5') AS role_id, p.id AS permission_id
       FROM "${schema}".permissions p
       WHERE p.module IN ('ACCOUNT','ACCOUNTING_PERIOD','JOURNAL_ENTRY','SALES_INVOICE','PAYMENT','CREDIT_NOTE','EXPENSE')
         AND p.action IN ('CREATE','READ','UPDATE')) src
ON (tgt.role_id = src.role_id AND tgt.permission_id = src.permission_id)
WHEN NOT MATCHED THEN INSERT (role_id, permission_id) VALUES (src.role_id, src.permission_id);

MERGE INTO "${schema}".role_permissions tgt
USING (SELECT STANDARD_HASH('VIEWER_${tenant_id}', 'MD5') AS role_id, p.id AS permission_id
       FROM "${schema}".permissions p
       WHERE p.module IN ('ACCOUNT','ACCOUNTING_PERIOD','JOURNAL_ENTRY','SALES_INVOICE','PAYMENT','CREDIT_NOTE','EXPENSE')
         AND p.action = 'READ') src
ON (tgt.role_id = src.role_id AND tgt.permission_id = src.permission_id)
WHEN NOT MATCHED THEN INSERT (role_id, permission_id) VALUES (src.role_id, src.permission_id);

COMMIT;
