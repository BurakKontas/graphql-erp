-- V12__crm.sql (Main Application - MSSQL)
-- UUID -> UNIQUEIDENTIFIER, TEXT -> NVARCHAR(MAX), TIMESTAMP -> DATETIME2,
-- NUMERIC -> DECIMAL, VARCHAR -> NVARCHAR

CREATE TABLE [${schema}].[crm_contacts] (
    [id]                  UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]           UNIQUEIDENTIFIER NOT NULL,
    [company_id]          UNIQUEIDENTIFIER NOT NULL,
    [contact_number]      NVARCHAR(50)     NOT NULL,
    [contact_type]        NVARCHAR(20)     NOT NULL,
    [first_name]          NVARCHAR(255)    NULL,
    [last_name]           NVARCHAR(255)    NULL,
    [company_name]        NVARCHAR(255)    NULL,
    [job_title]           NVARCHAR(255)    NULL,
    [email]               NVARCHAR(255)    NULL,
    [phone]               NVARCHAR(100)    NULL,
    [website]             NVARCHAR(255)    NULL,
    [address]             NVARCHAR(MAX)    NULL,
    [business_partner_id] NVARCHAR(255)    NULL,
    [owner_id]            NVARCHAR(255)    NULL,
    [status]              NVARCHAR(20)     NOT NULL,
    [source]              NVARCHAR(30)     NULL,
    [notes]               NVARCHAR(MAX)    NULL,
    CONSTRAINT [PK_crm_contacts] PRIMARY KEY ([id])
);
GO

CREATE TABLE [${schema}].[crm_leads] (
    [id]                      UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]               UNIQUEIDENTIFIER NOT NULL,
    [company_id]              UNIQUEIDENTIFIER NOT NULL,
    [lead_number]             NVARCHAR(50)     NOT NULL,
    [title]                   NVARCHAR(500)    NOT NULL,
    [contact_id]              NVARCHAR(255)    NULL,
    [owner_id]                NVARCHAR(255)    NULL,
    [status]                  NVARCHAR(30)     NOT NULL,
    [source]                  NVARCHAR(30)     NULL,
    [estimated_value]         DECIMAL(19, 2)   NULL,
    [disqualification_reason] NVARCHAR(MAX)    NULL,
    [notes]                   NVARCHAR(MAX)    NULL,
    [expected_close_date]     DATE             NULL,
    [opportunity_id]          NVARCHAR(255)    NULL,
    CONSTRAINT [PK_crm_leads] PRIMARY KEY ([id])
);
GO

CREATE TABLE [${schema}].[crm_opportunities] (
    [id]                  UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]           UNIQUEIDENTIFIER NOT NULL,
    [company_id]          UNIQUEIDENTIFIER NOT NULL,
    [opportunity_number]  NVARCHAR(50)     NOT NULL,
    [title]               NVARCHAR(500)    NOT NULL,
    [contact_id]          NVARCHAR(255)    NULL,
    [lead_id]             NVARCHAR(255)    NULL,
    [owner_id]            NVARCHAR(255)    NULL,
    [stage]               NVARCHAR(30)     NOT NULL,
    [probability]         DECIMAL(5, 2)    NULL,
    [expected_value]      DECIMAL(19, 2)   NULL,
    [currency_code]       NVARCHAR(10)     NULL,
    [expected_close_date] DATE             NULL,
    [sales_order_id]      NVARCHAR(255)    NULL,
    [lost_reason]         NVARCHAR(MAX)    NULL,
    [notes]               NVARCHAR(MAX)    NULL,
    CONSTRAINT [PK_crm_opportunities] PRIMARY KEY ([id])
);
GO

CREATE TABLE [${schema}].[crm_quotes] (
    [id]                UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]         UNIQUEIDENTIFIER NOT NULL,
    [company_id]        UNIQUEIDENTIFIER NOT NULL,
    [quote_number]      NVARCHAR(50)     NOT NULL,
    [opportunity_id]    NVARCHAR(255)    NULL,
    [contact_id]        NVARCHAR(255)    NULL,
    [owner_id]          NVARCHAR(255)    NULL,
    [quote_date]        DATE             NULL,
    [expiry_date]       DATE             NULL,
    [currency_code]     NVARCHAR(10)     NULL,
    [payment_term_code] NVARCHAR(50)     NULL,
    [status]            NVARCHAR(20)     NOT NULL,
    [version]           NVARCHAR(10)     NULL,
    [previous_quote_id] NVARCHAR(255)    NULL,
    [subtotal]          DECIMAL(19, 2)   NULL,
    [tax_total]         DECIMAL(19, 2)   NULL,
    [total]             DECIMAL(19, 2)   NULL,
    [discount_rate]     DECIMAL(5, 2)    NULL,
    [notes]             NVARCHAR(MAX)    NULL,
    [lines_json]        NVARCHAR(MAX)    NULL,
    CONSTRAINT [PK_crm_quotes] PRIMARY KEY ([id])
);
GO

CREATE TABLE [${schema}].[crm_activities] (
    [id]                     UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]              UNIQUEIDENTIFIER NOT NULL,
    [company_id]             UNIQUEIDENTIFIER NOT NULL,
    [activity_type]          NVARCHAR(20)     NOT NULL,
    [subject]                NVARCHAR(500)    NOT NULL,
    [owner_id]               NVARCHAR(255)    NULL,
    [status]                 NVARCHAR(20)     NOT NULL,
    [related_entity_type]    NVARCHAR(30)     NULL,
    [related_entity_id]      NVARCHAR(255)    NULL,
    [scheduled_at]           DATETIME2        NULL,
    [completed_at]           DATETIME2        NULL,
    [duration_minutes]       INT              NULL DEFAULT 0,
    [description]            NVARCHAR(MAX)    NULL,
    [outcome]                NVARCHAR(MAX)    NULL,
    [follow_up_type]         NVARCHAR(20)     NULL,
    [follow_up_scheduled_at] DATETIME2        NULL,
    [follow_up_note]         NVARCHAR(MAX)    NULL,
    CONSTRAINT [PK_crm_activities] PRIMARY KEY ([id])
);
GO

CREATE INDEX [idx_crm_contacts_tenant_company]      ON [${schema}].[crm_contacts] ([tenant_id],[company_id]);
CREATE INDEX [idx_crm_leads_tenant_company]         ON [${schema}].[crm_leads] ([tenant_id],[company_id]);
CREATE INDEX [idx_crm_opportunities_tenant_company] ON [${schema}].[crm_opportunities] ([tenant_id],[company_id]);
CREATE INDEX [idx_crm_quotes_tenant_company]        ON [${schema}].[crm_quotes] ([tenant_id],[company_id]);
CREATE INDEX [idx_crm_activities_tenant_company]    ON [${schema}].[crm_activities] ([tenant_id],[company_id]);
CREATE INDEX [idx_crm_activities_related]           ON [${schema}].[crm_activities] ([related_entity_type],[related_entity_id]);
GO

-- ─── CRM Permissions ───
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F0-0001-0000-000000000001','CONTACT','CREATE','Create a CRM contact');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F0-0001-0000-000000000002','CONTACT','READ',  'View CRM contacts');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F0-0001-0000-000000000003','CONTACT','UPDATE','Update CRM contacts');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F0-0001-0000-000000000004','CONTACT','DELETE','Delete CRM contacts');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F1-0002-0000-000000000001','LEAD','CREATE','Create a lead');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F1-0002-0000-000000000002','LEAD','READ',  'View leads');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F1-0002-0000-000000000003','LEAD','UPDATE','Update leads');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F1-0002-0000-000000000004','LEAD','DELETE','Delete leads');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F2-0002-0000-000000000001','OPPORTUNITY','CREATE','Create an opportunity');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F2-0002-0000-000000000002','OPPORTUNITY','READ',  'View opportunities');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F2-0002-0000-000000000003','OPPORTUNITY','UPDATE','Update opportunities');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F2-0002-0000-000000000004','OPPORTUNITY','DELETE','Delete opportunities');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F3-0002-0000-000000000001','QUOTE','CREATE','Create a quote');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F3-0002-0000-000000000002','QUOTE','READ',  'View quotes');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F3-0002-0000-000000000003','QUOTE','UPDATE','Update quotes');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F3-0002-0000-000000000004','QUOTE','DELETE','Delete quotes');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F4-0002-0000-000000000001','ACTIVITY','CREATE','Create an activity');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F4-0002-0000-000000000002','ACTIVITY','READ',  'View activities');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F4-0002-0000-000000000003','ACTIVITY','UPDATE','Update activities');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00F4-0002-0000-000000000004','ACTIVITY','DELETE','Delete activities');
GO

MERGE INTO [${schema}].[role_permissions] AS tgt
USING (SELECT CONVERT(UNIQUEIDENTIFIER,HASHBYTES('MD5','ADMIN_${tenant_id}')) AS role_id, p.[id] AS permission_id FROM [${schema}].[permissions] p WHERE p.[module] IN ('CONTACT','LEAD','OPPORTUNITY','QUOTE','ACTIVITY')) AS src
ON tgt.[role_id]=src.[role_id] AND tgt.[permission_id]=src.[permission_id]
WHEN NOT MATCHED THEN INSERT([role_id],[permission_id]) VALUES(src.[role_id],src.[permission_id]);

MERGE INTO [${schema}].[role_permissions] AS tgt
USING (SELECT CONVERT(UNIQUEIDENTIFIER,HASHBYTES('MD5','MANAGER_${tenant_id}')) AS role_id, p.[id] AS permission_id FROM [${schema}].[permissions] p WHERE p.[module] IN ('CONTACT','LEAD','OPPORTUNITY','QUOTE','ACTIVITY') AND p.[action] != 'DELETE') AS src
ON tgt.[role_id]=src.[role_id] AND tgt.[permission_id]=src.[permission_id]
WHEN NOT MATCHED THEN INSERT([role_id],[permission_id]) VALUES(src.[role_id],src.[permission_id]);

MERGE INTO [${schema}].[role_permissions] AS tgt
USING (SELECT CONVERT(UNIQUEIDENTIFIER,HASHBYTES('MD5','USER_${tenant_id}')) AS role_id, p.[id] AS permission_id FROM [${schema}].[permissions] p WHERE p.[module] IN ('CONTACT','LEAD','OPPORTUNITY','QUOTE','ACTIVITY') AND p.[action] IN ('CREATE','READ','UPDATE')) AS src
ON tgt.[role_id]=src.[role_id] AND tgt.[permission_id]=src.[permission_id]
WHEN NOT MATCHED THEN INSERT([role_id],[permission_id]) VALUES(src.[role_id],src.[permission_id]);

MERGE INTO [${schema}].[role_permissions] AS tgt
USING (SELECT CONVERT(UNIQUEIDENTIFIER,HASHBYTES('MD5','VIEWER_${tenant_id}')) AS role_id, p.[id] AS permission_id FROM [${schema}].[permissions] p WHERE p.[module] IN ('CONTACT','LEAD','OPPORTUNITY','QUOTE','ACTIVITY') AND p.[action] = 'READ') AS src
ON tgt.[role_id]=src.[role_id] AND tgt.[permission_id]=src.[permission_id]
WHEN NOT MATCHED THEN INSERT([role_id],[permission_id]) VALUES(src.[role_id],src.[permission_id]);
GO
