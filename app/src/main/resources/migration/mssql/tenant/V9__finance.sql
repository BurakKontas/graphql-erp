-- V9__finance.sql (Main Application - MSSQL)
-- UUID -> UNIQUEIDENTIFIER, BOOLEAN -> BIT, TEXT -> NVARCHAR(MAX),
-- NUMERIC -> DECIMAL, TIMESTAMP -> DATETIME2, UNIQUE -> named CONSTRAINT

-- ─── Accounts (Chart of Accounts) ───
CREATE TABLE [${schema}].[accounts] (
    [id]                UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]         UNIQUEIDENTIFIER NOT NULL,
    [company_id]        UNIQUEIDENTIFIER NOT NULL,
    [code]              NVARCHAR(50)     NOT NULL,
    [name]              NVARCHAR(200)    NOT NULL,
    [type]              NVARCHAR(50)     NOT NULL,
    [nature]            NVARCHAR(50)     NOT NULL,
    [parent_account_id] UNIQUEIDENTIFIER NULL,
    [system_account]    BIT              NOT NULL DEFAULT 0,
    [active]            BIT              NOT NULL DEFAULT 1,
    CONSTRAINT [PK_accounts]       PRIMARY KEY ([id]),
    CONSTRAINT [FK_acc_company]    FOREIGN KEY ([company_id])        REFERENCES [${schema}].[companies] ([id]),
    CONSTRAINT [FK_acc_parent]     FOREIGN KEY ([parent_account_id]) REFERENCES [${schema}].[accounts] ([id]),
    CONSTRAINT [UQ_acc_code]       UNIQUE ([tenant_id], [company_id], [code])
);
GO
CREATE INDEX [idx_account_tenant]  ON [${schema}].[accounts] ([tenant_id]);
CREATE INDEX [idx_account_company] ON [${schema}].[accounts] ([company_id]);
GO

-- ─── Accounting Periods ───
CREATE TABLE [${schema}].[accounting_periods] (
    [id]          UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]   UNIQUEIDENTIFIER NOT NULL,
    [company_id]  UNIQUEIDENTIFIER NOT NULL,
    [period_type] NVARCHAR(20)     NOT NULL,
    [start_date]  DATE             NOT NULL,
    [end_date]    DATE             NOT NULL,
    [status]      NVARCHAR(20)     NOT NULL DEFAULT 'OPEN',
    CONSTRAINT [PK_accounting_periods] PRIMARY KEY ([id]),
    CONSTRAINT [FK_period_company]     FOREIGN KEY ([company_id]) REFERENCES [${schema}].[companies] ([id])
);
GO
CREATE INDEX [idx_period_tenant]  ON [${schema}].[accounting_periods] ([tenant_id]);
CREATE INDEX [idx_period_company] ON [${schema}].[accounting_periods] ([company_id]);
GO

-- ─── Journal Entries ───
CREATE TABLE [${schema}].[journal_entries] (
    [id]             UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]      UNIQUEIDENTIFIER NOT NULL,
    [company_id]     UNIQUEIDENTIFIER NOT NULL,
    [entry_number]   NVARCHAR(20)     NOT NULL,
    [type]           NVARCHAR(50)     NOT NULL,
    [period_id]      UNIQUEIDENTIFIER NULL,
    [entry_date]     DATE             NOT NULL,
    [description]    NVARCHAR(MAX)    NULL,
    [reference_type] NVARCHAR(100)    NULL,
    [reference_id]   NVARCHAR(100)    NULL,
    [currency_code]  NVARCHAR(10)     NULL,
    [exchange_rate]  DECIMAL(19, 6)   NULL DEFAULT 1,
    [status]         NVARCHAR(20)     NOT NULL DEFAULT 'DRAFT',
    CONSTRAINT [PK_journal_entries]   PRIMARY KEY ([id]),
    CONSTRAINT [UQ_je_entry_number]   UNIQUE ([entry_number]),
    CONSTRAINT [FK_je_company]        FOREIGN KEY ([company_id]) REFERENCES [${schema}].[companies] ([id]),
    CONSTRAINT [FK_je_period]         FOREIGN KEY ([period_id])  REFERENCES [${schema}].[accounting_periods] ([id])
);
GO
CREATE INDEX [idx_je_tenant]    ON [${schema}].[journal_entries] ([tenant_id]);
CREATE INDEX [idx_je_company]   ON [${schema}].[journal_entries] ([company_id]);
CREATE INDEX [idx_je_reference] ON [${schema}].[journal_entries] ([reference_type], [reference_id]);
GO

-- ─── Journal Entry Lines ───
CREATE TABLE [${schema}].[journal_entry_lines] (
    [id]            UNIQUEIDENTIFIER NOT NULL,
    [entry_id]      UNIQUEIDENTIFIER NOT NULL,
    [account_id]    NVARCHAR(100)    NOT NULL,
    [account_code]  NVARCHAR(50)     NULL,
    [account_name]  NVARCHAR(200)    NULL,
    [debit_amount]  DECIMAL(19, 2)   NULL DEFAULT 0,
    [credit_amount] DECIMAL(19, 2)   NULL DEFAULT 0,
    [description]   NVARCHAR(MAX)    NULL,
    CONSTRAINT [PK_journal_entry_lines] PRIMARY KEY ([id]),
    CONSTRAINT [FK_jel_entry]           FOREIGN KEY ([entry_id])
        REFERENCES [${schema}].[journal_entries] ([id]) ON DELETE CASCADE
);
GO
CREATE INDEX [idx_jel_entry] ON [${schema}].[journal_entry_lines] ([entry_id]);
GO

-- ─── Sales Invoices ───
CREATE TABLE [${schema}].[sales_invoices] (
    [id]                 UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]          UNIQUEIDENTIFIER NOT NULL,
    [company_id]         UNIQUEIDENTIFIER NOT NULL,
    [invoice_number]     NVARCHAR(20)     NOT NULL,
    [invoice_type]       NVARCHAR(20)     NOT NULL DEFAULT 'STANDARD',
    [sales_order_id]     NVARCHAR(100)    NULL,
    [sales_order_number] NVARCHAR(50)     NULL,
    [customer_id]        NVARCHAR(100)    NULL,
    [customer_name]      NVARCHAR(200)    NULL,
    [invoice_date]       DATE             NOT NULL,
    [due_date]           DATE             NULL,
    [currency_code]      NVARCHAR(10)     NULL,
    [exchange_rate]      DECIMAL(19, 6)   NULL DEFAULT 1,
    [status]             NVARCHAR(20)     NOT NULL DEFAULT 'DRAFT',
    [subtotal]           DECIMAL(19, 2)   NULL DEFAULT 0,
    [tax_total]          DECIMAL(19, 2)   NULL DEFAULT 0,
    [total]              DECIMAL(19, 2)   NULL DEFAULT 0,
    [paid_amount]        DECIMAL(19, 2)   NULL DEFAULT 0,
    CONSTRAINT [PK_sales_invoices] PRIMARY KEY ([id]),
    CONSTRAINT [UQ_sinv_number]    UNIQUE ([invoice_number]),
    CONSTRAINT [FK_sinv_company]   FOREIGN KEY ([company_id]) REFERENCES [${schema}].[companies] ([id])
);
GO
CREATE INDEX [idx_sinv_tenant]  ON [${schema}].[sales_invoices] ([tenant_id]);
CREATE INDEX [idx_sinv_company] ON [${schema}].[sales_invoices] ([company_id]);
GO

-- ─── Sales Invoice Lines ───
CREATE TABLE [${schema}].[sales_invoice_lines] (
    [id]                  UNIQUEIDENTIFIER NOT NULL,
    [invoice_id]          UNIQUEIDENTIFIER NOT NULL,
    [sales_order_line_id] NVARCHAR(100)    NULL,
    [item_id]             NVARCHAR(100)    NULL,
    [item_description]    NVARCHAR(500)    NOT NULL,
    [unit_code]           NVARCHAR(50)     NULL,
    [quantity]            DECIMAL(19, 4)   NULL,
    [unit_price]          DECIMAL(19, 6)   NULL,
    [tax_code]            NVARCHAR(50)     NULL,
    [tax_rate]            DECIMAL(10, 4)   NULL DEFAULT 0,
    [line_total]          DECIMAL(19, 2)   NULL DEFAULT 0,
    [tax_amount]          DECIMAL(19, 2)   NULL DEFAULT 0,
    [line_total_with_tax] DECIMAL(19, 2)   NULL DEFAULT 0,
    [revenue_account_id]  NVARCHAR(100)    NULL,
    CONSTRAINT [PK_sales_invoice_lines] PRIMARY KEY ([id]),
    CONSTRAINT [FK_sinvl_invoice]       FOREIGN KEY ([invoice_id])
        REFERENCES [${schema}].[sales_invoices] ([id]) ON DELETE CASCADE
);
GO
CREATE INDEX [idx_sinvl_invoice] ON [${schema}].[sales_invoice_lines] ([invoice_id]);
GO

-- ─── Credit Notes ───
CREATE TABLE [${schema}].[credit_notes] (
    [id]                 UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]          UNIQUEIDENTIFIER NOT NULL,
    [company_id]         UNIQUEIDENTIFIER NOT NULL,
    [credit_note_number] NVARCHAR(20)     NOT NULL,
    [invoice_id]         NVARCHAR(100)    NULL,
    [customer_id]        NVARCHAR(100)    NULL,
    [credit_note_date]   DATE             NOT NULL,
    [currency_code]      NVARCHAR(10)     NULL,
    [status]             NVARCHAR(20)     NOT NULL DEFAULT 'DRAFT',
    [total]              DECIMAL(19, 2)   NULL DEFAULT 0,
    [applied_amount]     DECIMAL(19, 2)   NULL DEFAULT 0,
    [reason]             NVARCHAR(MAX)    NULL,
    CONSTRAINT [PK_credit_notes] PRIMARY KEY ([id]),
    CONSTRAINT [UQ_cn_number]    UNIQUE ([credit_note_number]),
    CONSTRAINT [FK_cn_company]   FOREIGN KEY ([company_id]) REFERENCES [${schema}].[companies] ([id])
);
GO
CREATE INDEX [idx_cn_tenant] ON [${schema}].[credit_notes] ([tenant_id]);
GO

-- ─── Credit Note Lines ───
CREATE TABLE [${schema}].[credit_note_lines] (
    [id]                  UNIQUEIDENTIFIER NOT NULL,
    [credit_note_id]      UNIQUEIDENTIFIER NOT NULL,
    [item_id]             NVARCHAR(100)    NULL,
    [item_description]    NVARCHAR(500)    NULL,
    [unit_code]           NVARCHAR(50)     NULL,
    [quantity]            DECIMAL(19, 4)   NULL,
    [unit_price]          DECIMAL(19, 6)   NULL,
    [tax_code]            NVARCHAR(50)     NULL,
    [tax_rate]            DECIMAL(10, 4)   NULL DEFAULT 0,
    [line_total]          DECIMAL(19, 2)   NULL DEFAULT 0,
    [tax_amount]          DECIMAL(19, 2)   NULL DEFAULT 0,
    [line_total_with_tax] DECIMAL(19, 2)   NULL DEFAULT 0,
    CONSTRAINT [PK_credit_note_lines] PRIMARY KEY ([id]),
    CONSTRAINT [FK_cnl_cn]            FOREIGN KEY ([credit_note_id])
        REFERENCES [${schema}].[credit_notes] ([id]) ON DELETE CASCADE
);
GO
CREATE INDEX [idx_cnl_cn] ON [${schema}].[credit_note_lines] ([credit_note_id]);
GO

-- ─── Payments ───
CREATE TABLE [${schema}].[payments] (
    [id]               UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]        UNIQUEIDENTIFIER NOT NULL,
    [company_id]       UNIQUEIDENTIFIER NOT NULL,
    [payment_number]   NVARCHAR(20)     NOT NULL,
    [payment_type]     NVARCHAR(20)     NOT NULL,
    [invoice_id]       NVARCHAR(100)    NULL,
    [customer_id]      NVARCHAR(100)    NULL,
    [payment_date]     DATE             NOT NULL,
    [amount]           DECIMAL(19, 2)   NOT NULL,
    [currency_code]    NVARCHAR(10)     NULL,
    [exchange_rate]    DECIMAL(19, 6)   NULL DEFAULT 1,
    [payment_method]   NVARCHAR(30)     NOT NULL,
    [status]           NVARCHAR(20)     NOT NULL DEFAULT 'DRAFT',
    [bank_account_ref] NVARCHAR(100)    NULL,
    [reference_number] NVARCHAR(100)    NULL,
    CONSTRAINT [PK_payments]    PRIMARY KEY ([id]),
    CONSTRAINT [UQ_pay_number]  UNIQUE ([payment_number]),
    CONSTRAINT [FK_pay_company] FOREIGN KEY ([company_id]) REFERENCES [${schema}].[companies] ([id])
);
GO
CREATE INDEX [idx_pay_tenant]  ON [${schema}].[payments] ([tenant_id]);
CREATE INDEX [idx_pay_invoice] ON [${schema}].[payments] ([invoice_id]);
GO

-- ─── Expenses ───
CREATE TABLE [${schema}].[expenses] (
    [id]                 UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]          UNIQUEIDENTIFIER NOT NULL,
    [company_id]         UNIQUEIDENTIFIER NOT NULL,
    [expense_number]     NVARCHAR(20)     NOT NULL,
    [description]        NVARCHAR(MAX)    NOT NULL,
    [category]           NVARCHAR(100)    NULL,
    [submitted_by]       NVARCHAR(100)    NULL,
    [expense_date]       DATE             NOT NULL,
    [amount]             DECIMAL(19, 2)   NOT NULL,
    [currency_code]      NVARCHAR(10)     NULL,
    [status]             NVARCHAR(20)     NOT NULL DEFAULT 'DRAFT',
    [approved_by]        NVARCHAR(100)    NULL,
    [receipt_reference]  NVARCHAR(100)    NULL,
    [expense_account_id] NVARCHAR(100)    NULL,
    CONSTRAINT [PK_expenses]    PRIMARY KEY ([id]),
    CONSTRAINT [UQ_exp_number]  UNIQUE ([expense_number]),
    CONSTRAINT [FK_exp_company] FOREIGN KEY ([company_id]) REFERENCES [${schema}].[companies] ([id])
);
GO
CREATE INDEX [idx_exp_tenant]  ON [${schema}].[expenses] ([tenant_id]);
CREATE INDEX [idx_exp_company] ON [${schema}].[expenses] ([company_id]);
GO

-- ─── Finance Permissions ───
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F1-0002-0000-000000000001','ACCOUNT','CREATE','Create a new account');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F1-0002-0000-000000000002','ACCOUNT','READ',  'View account details');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F1-0002-0000-000000000003','ACCOUNT','UPDATE','Update account information');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F1-0002-0000-000000000004','ACCOUNT','DELETE','Delete an account');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F2-0002-0000-000000000001','ACCOUNTING_PERIOD','CREATE','Create a new accounting period');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F2-0002-0000-000000000002','ACCOUNTING_PERIOD','READ',  'View accounting period details');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F2-0002-0000-000000000003','ACCOUNTING_PERIOD','UPDATE','Update accounting period');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F2-0002-0000-000000000004','ACCOUNTING_PERIOD','DELETE','Delete an accounting period');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F3-0002-0000-000000000001','JOURNAL_ENTRY','CREATE','Create a new journal entry');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F3-0002-0000-000000000002','JOURNAL_ENTRY','READ',  'View journal entry details');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F3-0002-0000-000000000003','JOURNAL_ENTRY','UPDATE','Update journal entry');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F3-0002-0000-000000000004','JOURNAL_ENTRY','DELETE','Delete a journal entry');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F4-0002-0000-000000000001','SALES_INVOICE','CREATE','Create a new sales invoice');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F4-0002-0000-000000000002','SALES_INVOICE','READ',  'View sales invoice details');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F4-0002-0000-000000000003','SALES_INVOICE','UPDATE','Update sales invoice');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F4-0002-0000-000000000004','SALES_INVOICE','DELETE','Delete a sales invoice');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F5-0002-0000-000000000001','PAYMENT','CREATE','Create a new payment');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F5-0002-0000-000000000002','PAYMENT','READ',  'View payment details');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F5-0002-0000-000000000003','PAYMENT','UPDATE','Update payment');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F5-0002-0000-000000000004','PAYMENT','DELETE','Delete a payment');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F6-0002-0000-000000000001','CREDIT_NOTE','CREATE','Create a new credit note');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F6-0002-0000-000000000002','CREDIT_NOTE','READ',  'View credit note details');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F6-0002-0000-000000000003','CREDIT_NOTE','UPDATE','Update credit note');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F6-0002-0000-000000000004','CREDIT_NOTE','DELETE','Delete a credit note');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F7-0002-0000-000000000001','EXPENSE','CREATE','Create a new expense');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F7-0002-0000-000000000002','EXPENSE','READ',  'View expense details');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F7-0002-0000-000000000003','EXPENSE','UPDATE','Update expense');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F7-0002-0000-000000000004','EXPENSE','DELETE','Delete an expense');
GO

-- ─── Update default role permissions for Finance ───
MERGE INTO [${schema}].[role_permissions] AS tgt
USING (SELECT CONVERT(UNIQUEIDENTIFIER, HASHBYTES('MD5','ADMIN_${tenant_id}')) AS role_id, p.[id] AS permission_id
       FROM [${schema}].[permissions] p
       WHERE p.[module] IN ('ACCOUNT','ACCOUNTING_PERIOD','JOURNAL_ENTRY','SALES_INVOICE','PAYMENT','CREDIT_NOTE','EXPENSE')) AS src
ON tgt.[role_id]=src.[role_id] AND tgt.[permission_id]=src.[permission_id]
WHEN NOT MATCHED THEN INSERT([role_id],[permission_id]) VALUES(src.[role_id],src.[permission_id]);

MERGE INTO [${schema}].[role_permissions] AS tgt
USING (SELECT CONVERT(UNIQUEIDENTIFIER, HASHBYTES('MD5','MANAGER_${tenant_id}')) AS role_id, p.[id] AS permission_id
       FROM [${schema}].[permissions] p
       WHERE p.[module] IN ('ACCOUNT','ACCOUNTING_PERIOD','JOURNAL_ENTRY','SALES_INVOICE','PAYMENT','CREDIT_NOTE','EXPENSE')
         AND p.[action] != 'DELETE') AS src
ON tgt.[role_id]=src.[role_id] AND tgt.[permission_id]=src.[permission_id]
WHEN NOT MATCHED THEN INSERT([role_id],[permission_id]) VALUES(src.[role_id],src.[permission_id]);

MERGE INTO [${schema}].[role_permissions] AS tgt
USING (SELECT CONVERT(UNIQUEIDENTIFIER, HASHBYTES('MD5','USER_${tenant_id}')) AS role_id, p.[id] AS permission_id
       FROM [${schema}].[permissions] p
       WHERE p.[module] IN ('ACCOUNT','ACCOUNTING_PERIOD','JOURNAL_ENTRY','SALES_INVOICE','PAYMENT','CREDIT_NOTE','EXPENSE')
         AND p.[action] IN ('CREATE','READ','UPDATE')) AS src
ON tgt.[role_id]=src.[role_id] AND tgt.[permission_id]=src.[permission_id]
WHEN NOT MATCHED THEN INSERT([role_id],[permission_id]) VALUES(src.[role_id],src.[permission_id]);

MERGE INTO [${schema}].[role_permissions] AS tgt
USING (SELECT CONVERT(UNIQUEIDENTIFIER, HASHBYTES('MD5','VIEWER_${tenant_id}')) AS role_id, p.[id] AS permission_id
       FROM [${schema}].[permissions] p
       WHERE p.[module] IN ('ACCOUNT','ACCOUNTING_PERIOD','JOURNAL_ENTRY','SALES_INVOICE','PAYMENT','CREDIT_NOTE','EXPENSE')
         AND p.[action] = 'READ') AS src
ON tgt.[role_id]=src.[role_id] AND tgt.[permission_id]=src.[permission_id]
WHEN NOT MATCHED THEN INSERT([role_id],[permission_id]) VALUES(src.[role_id],src.[permission_id]);
GO
