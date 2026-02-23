-- V13__audit.sql (Main Application - MSSQL)
-- UUID -> UNIQUEIDENTIFIER, TEXT -> NVARCHAR(MAX),
-- TIMESTAMP WITH TIME ZONE -> DATETIMEOFFSET, INT -> INT

CREATE TABLE [${schema}].[audit_entries] (
    [id]              UNIQUEIDENTIFIER NOT NULL,
    [source]          NVARCHAR(30)     NOT NULL,
    [tenant_id]       UNIQUEIDENTIFIER NOT NULL,
    [company_id]      NVARCHAR(255)    NULL,
    [module_name]     NVARCHAR(50)     NOT NULL,
    [aggregate_type]  NVARCHAR(100)    NOT NULL,
    [aggregate_id]    NVARCHAR(255)    NOT NULL,
    [action]          NVARCHAR(30)     NOT NULL,
    [event_type]      NVARCHAR(255)    NULL,
    [user_id]         NVARCHAR(255)    NULL,
    [user_email]      NVARCHAR(255)    NULL,
    [user_ip]         NVARCHAR(50)     NULL,
    [user_agent]      NVARCHAR(500)    NULL,
    [occurred_at]     DATETIMEOFFSET   NOT NULL,
    [before_snapshot] NVARCHAR(MAX)    NULL,
    [after_snapshot]  NVARCHAR(MAX)    NULL,
    [changes_json]    NVARCHAR(MAX)    NULL,
    [correlation_id]  NVARCHAR(255)    NULL,
    [session_id]      NVARCHAR(255)    NULL,
    CONSTRAINT [PK_audit_entries] PRIMARY KEY ([id])
);
GO

CREATE INDEX [idx_audit_entries_tenant]    ON [${schema}].[audit_entries] ([tenant_id]);
CREATE INDEX [idx_audit_entries_aggregate] ON [${schema}].[audit_entries] ([aggregate_type],[aggregate_id],[tenant_id]);
CREATE INDEX [idx_audit_entries_user]      ON [${schema}].[audit_entries] ([user_id],[tenant_id],[occurred_at]);
CREATE INDEX [idx_audit_entries_module]    ON [${schema}].[audit_entries] ([module_name],[tenant_id],[occurred_at]);
CREATE INDEX [idx_audit_entries_occurred]  ON [${schema}].[audit_entries] ([occurred_at]);
GO

CREATE TABLE [${schema}].[audit_retention_policies] (
    [id]             UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]      UNIQUEIDENTIFIER NOT NULL,
    [module_name]    NVARCHAR(50)     NULL,
    [retention_days] INT              NOT NULL DEFAULT 365,
    CONSTRAINT [PK_audit_retention_policies] PRIMARY KEY ([id])
);
GO

-- ─── Audit Permissions ───
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00FF-0000-0000-000000000001','AUDIT','READ', 'View audit logs');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00FF-0000-0000-000000000002','AUDIT','ADMIN','Administer audit logs');
GO

MERGE INTO [${schema}].[role_permissions] AS tgt
USING (SELECT CONVERT(UNIQUEIDENTIFIER,HASHBYTES('MD5','ADMIN_${tenant_id}')) AS role_id, p.[id] AS permission_id FROM [${schema}].[permissions] p WHERE p.[module] = 'AUDIT') AS src
ON tgt.[role_id]=src.[role_id] AND tgt.[permission_id]=src.[permission_id]
WHEN NOT MATCHED THEN INSERT([role_id],[permission_id]) VALUES(src.[role_id],src.[permission_id]);

MERGE INTO [${schema}].[role_permissions] AS tgt
USING (SELECT CONVERT(UNIQUEIDENTIFIER,HASHBYTES('MD5','MANAGER_${tenant_id}')) AS role_id, p.[id] AS permission_id FROM [${schema}].[permissions] p WHERE p.[module] = 'AUDIT' AND p.[action] = 'READ') AS src
ON tgt.[role_id]=src.[role_id] AND tgt.[permission_id]=src.[permission_id]
WHEN NOT MATCHED THEN INSERT([role_id],[permission_id]) VALUES(src.[role_id],src.[permission_id]);
GO
