-- V14__notification.sql (Main Application - MSSQL)
-- UUID -> UNIQUEIDENTIFIER, BOOLEAN -> BIT, TEXT -> NVARCHAR(MAX),
-- TIMESTAMP -> DATETIME2, NOW() -> GETUTCDATE()

CREATE TABLE [${schema}].[tenant_notification_configs] (
    [id]                        UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]                 UNIQUEIDENTIFIER NOT NULL,
    [all_notifications_enabled] BIT              NOT NULL DEFAULT 1,
    [disabled_channels]         NVARCHAR(MAX)    NULL,
    [disabled_keys]             NVARCHAR(MAX)    NULL,
    [disabled_reason]           NVARCHAR(MAX)    NULL,
    [disabled_by]               NVARCHAR(255)    NULL,
    [disabled_at]               DATETIME2        NULL,
    [disabled_until]            DATETIME2        NULL,
    CONSTRAINT [PK_tenant_notification_configs] PRIMARY KEY ([id])
);
GO

CREATE TABLE [${schema}].[notification_configs] (
    [id]               UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]        UNIQUEIDENTIFIER NOT NULL,
    [company_id]       UNIQUEIDENTIFIER NOT NULL,
    [event_type]       NVARCHAR(255)    NOT NULL,
    [notification_key] NVARCHAR(255)    NOT NULL,
    [description]      NVARCHAR(MAX)    NULL,
    [enabled_channels] NVARCHAR(MAX)    NULL,
    [delivery_timing]  NVARCHAR(20)     NOT NULL DEFAULT 'IMMEDIATE',
    [cron_expression]  NVARCHAR(100)    NULL,
    [recipient_roles]  NVARCHAR(MAX)    NULL,
    [user_overridable] BIT              NOT NULL DEFAULT 1,
    [active]           BIT              NOT NULL DEFAULT 1,
    CONSTRAINT [PK_notification_configs] PRIMARY KEY ([id])
);
GO

CREATE TABLE [${schema}].[notification_templates] (
    [id]               UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]        UNIQUEIDENTIFIER NOT NULL,
    [notification_key] NVARCHAR(255)    NOT NULL,
    [channel]          NVARCHAR(20)     NOT NULL,
    [locale]           NVARCHAR(10)     NOT NULL DEFAULT 'TR',
    [subject]          NVARCHAR(500)    NULL,
    [body_template]    NVARCHAR(MAX)    NOT NULL,
    [system_template]  BIT              NOT NULL DEFAULT 0,
    [active]           BIT              NOT NULL DEFAULT 1,
    CONSTRAINT [PK_notification_templates] PRIMARY KEY ([id])
);
GO

CREATE TABLE [${schema}].[notification_preferences] (
    [id]               UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]        UNIQUEIDENTIFIER NOT NULL,
    [user_id]          NVARCHAR(255)    NOT NULL,
    [notification_key] NVARCHAR(255)    NOT NULL,
    [disabled_channels] NVARCHAR(MAX)   NULL,
    [preferred_locale] NVARCHAR(10)     NULL,
    CONSTRAINT [PK_notification_preferences] PRIMARY KEY ([id])
);
GO

CREATE TABLE [${schema}].[webhook_configs] (
    [id]                   UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]            UNIQUEIDENTIFIER NOT NULL,
    [company_id]           UNIQUEIDENTIFIER NOT NULL,
    [target_url]           NVARCHAR(1000)   NOT NULL,
    [secret_key]           NVARCHAR(500)    NULL,
    [event_types]          NVARCHAR(MAX)    NULL,
    [status]               NVARCHAR(20)     NOT NULL DEFAULT 'ACTIVE',
    [consecutive_failures] INT              NOT NULL DEFAULT 0,
    [last_success_at]      DATETIME2        NULL,
    CONSTRAINT [PK_webhook_configs] PRIMARY KEY ([id])
);
GO

CREATE TABLE [${schema}].[notification_outbox] (
    [id]                UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]         UNIQUEIDENTIFIER NOT NULL,
    [notification_key]  NVARCHAR(255)    NOT NULL,
    [channel]           NVARCHAR(20)     NOT NULL,
    [recipient_user_id] NVARCHAR(255)    NULL,
    [recipient_address] NVARCHAR(500)    NULL,
    [subject]           NVARCHAR(500)    NULL,
    [body]              NVARCHAR(MAX)    NULL,
    [status]            NVARCHAR(20)     NOT NULL DEFAULT 'PENDING',
    [retry_count]       INT              NOT NULL DEFAULT 0,
    [scheduled_at]      DATETIME2        NULL,
    [sent_at]           DATETIME2        NULL,
    [failure_reason]    NVARCHAR(MAX)    NULL,
    [correlation_id]    NVARCHAR(255)    NULL,
    [created_at]        DATETIME2        NOT NULL DEFAULT GETUTCDATE(),
    CONSTRAINT [PK_notification_outbox] PRIMARY KEY ([id])
);
GO

CREATE TABLE [${schema}].[in_app_notifications] (
    [id]               UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]        UNIQUEIDENTIFIER NOT NULL,
    [user_id]          NVARCHAR(255)    NOT NULL,
    [notification_key] NVARCHAR(255)    NOT NULL,
    [title]            NVARCHAR(500)    NULL,
    [body]             NVARCHAR(MAX)    NULL,
    [action_url]       NVARCHAR(1000)   NULL,
    [read]             BIT              NOT NULL DEFAULT 0,
    [read_at]          DATETIME2        NULL,
    [created_at]       DATETIME2        NOT NULL DEFAULT GETUTCDATE(),
    CONSTRAINT [PK_in_app_notifications] PRIMARY KEY ([id])
);
GO

CREATE TABLE [${schema}].[notification_suppress_log] (
    [id]                UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]         UNIQUEIDENTIFIER NOT NULL,
    [correlation_id]    NVARCHAR(255)    NULL,
    [notification_key]  NVARCHAR(255)    NOT NULL,
    [channel]           NVARCHAR(20)     NOT NULL,
    [recipient_user_id] NVARCHAR(255)    NULL,
    [suppress_reason]   NVARCHAR(50)     NOT NULL,
    [occurred_at]       DATETIME2        NOT NULL DEFAULT GETUTCDATE(),
    CONSTRAINT [PK_notification_suppress_log] PRIMARY KEY ([id])
);
GO

-- ─── Indexes ───
CREATE INDEX [idx_tenant_notif_config_tenant]  ON [${schema}].[tenant_notification_configs] ([tenant_id]);
CREATE INDEX [idx_notif_config_tenant_company] ON [${schema}].[notification_configs] ([tenant_id],[company_id]);
CREATE INDEX [idx_notif_config_event_type]     ON [${schema}].[notification_configs] ([event_type],[tenant_id],[company_id]);
CREATE INDEX [idx_notif_template_tenant]       ON [${schema}].[notification_templates] ([tenant_id]);
CREATE INDEX [idx_notif_template_key_channel]  ON [${schema}].[notification_templates] ([notification_key],[channel],[tenant_id]);
CREATE INDEX [idx_notif_pref_user]             ON [${schema}].[notification_preferences] ([user_id],[tenant_id]);
CREATE INDEX [idx_webhook_config_tenant_co]    ON [${schema}].[webhook_configs] ([tenant_id],[company_id]);
CREATE INDEX [idx_notif_outbox_status]         ON [${schema}].[notification_outbox] ([status],[retry_count]);
CREATE INDEX [idx_notif_outbox_tenant]         ON [${schema}].[notification_outbox] ([tenant_id]);
CREATE INDEX [idx_in_app_notif_user]           ON [${schema}].[in_app_notifications] ([user_id],[tenant_id]);
CREATE INDEX [idx_in_app_notif_created]        ON [${schema}].[in_app_notifications] ([created_at] DESC);
CREATE INDEX [idx_suppress_log_tenant]         ON [${schema}].[notification_suppress_log] ([tenant_id]);
GO

-- ─── Notification Permissions ───
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-0100-0000-0000-000000000001','NOTIFICATION_CONFIG','CREATE','Create notification config');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-0100-0000-0000-000000000002','NOTIFICATION_CONFIG','READ',  'View notification configs');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-0100-0000-0000-000000000003','NOTIFICATION_CONFIG','UPDATE','Update notification configs');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-0100-0000-0000-000000000004','NOTIFICATION_CONFIG','DELETE','Delete notification configs');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-0101-0000-0000-000000000001','NOTIFICATION_TEMPLATE','CREATE','Create notification template');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-0101-0000-0000-000000000002','NOTIFICATION_TEMPLATE','READ',  'View notification templates');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-0101-0000-0000-000000000003','NOTIFICATION_TEMPLATE','UPDATE','Update notification templates');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-0101-0000-0000-000000000004','NOTIFICATION_TEMPLATE','DELETE','Delete notification templates');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-0102-0000-0000-000000000001','NOTIFICATION_PREFERENCE','CREATE','Create notification preference');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-0102-0000-0000-000000000002','NOTIFICATION_PREFERENCE','READ',  'View notification preferences');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-0102-0000-0000-000000000003','NOTIFICATION_PREFERENCE','UPDATE','Update notification preferences');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-0102-0000-0000-000000000004','NOTIFICATION_PREFERENCE','DELETE','Delete notification preferences');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-0103-0000-0000-000000000001','WEBHOOK_CONFIG','CREATE','Create webhook config');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-0103-0000-0000-000000000002','WEBHOOK_CONFIG','READ',  'View webhook configs');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-0103-0000-0000-000000000003','WEBHOOK_CONFIG','UPDATE','Update webhook configs');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-0103-0000-0000-000000000004','WEBHOOK_CONFIG','DELETE','Delete webhook configs');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-0104-0000-0000-000000000001','IN_APP_NOTIFICATION','CREATE','Create in-app notification');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-0104-0000-0000-000000000002','IN_APP_NOTIFICATION','READ',  'View in-app notifications');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-0104-0000-0000-000000000003','IN_APP_NOTIFICATION','UPDATE','Update in-app notifications');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-0104-0000-0000-000000000004','IN_APP_NOTIFICATION','DELETE','Delete in-app notifications');
GO

MERGE INTO [${schema}].[role_permissions] AS tgt
USING (SELECT CONVERT(UNIQUEIDENTIFIER,HASHBYTES('MD5','ADMIN_${tenant_id}')) AS role_id, p.[id] AS permission_id FROM [${schema}].[permissions] p WHERE p.[module] IN ('NOTIFICATION_CONFIG','NOTIFICATION_TEMPLATE','NOTIFICATION_PREFERENCE','WEBHOOK_CONFIG','IN_APP_NOTIFICATION')) AS src
ON tgt.[role_id]=src.[role_id] AND tgt.[permission_id]=src.[permission_id]
WHEN NOT MATCHED THEN INSERT([role_id],[permission_id]) VALUES(src.[role_id],src.[permission_id]);

MERGE INTO [${schema}].[role_permissions] AS tgt
USING (SELECT CONVERT(UNIQUEIDENTIFIER,HASHBYTES('MD5','MANAGER_${tenant_id}')) AS role_id, p.[id] AS permission_id FROM [${schema}].[permissions] p WHERE p.[module] IN ('NOTIFICATION_CONFIG','NOTIFICATION_TEMPLATE','NOTIFICATION_PREFERENCE','WEBHOOK_CONFIG','IN_APP_NOTIFICATION') AND p.[action] != 'DELETE') AS src
ON tgt.[role_id]=src.[role_id] AND tgt.[permission_id]=src.[permission_id]
WHEN NOT MATCHED THEN INSERT([role_id],[permission_id]) VALUES(src.[role_id],src.[permission_id]);

MERGE INTO [${schema}].[role_permissions] AS tgt
USING (SELECT CONVERT(UNIQUEIDENTIFIER,HASHBYTES('MD5','USER_${tenant_id}')) AS role_id, p.[id] AS permission_id FROM [${schema}].[permissions] p WHERE p.[module] IN ('NOTIFICATION_PREFERENCE','IN_APP_NOTIFICATION') AND p.[action] IN ('CREATE','READ','UPDATE')) AS src
ON tgt.[role_id]=src.[role_id] AND tgt.[permission_id]=src.[permission_id]
WHEN NOT MATCHED THEN INSERT([role_id],[permission_id]) VALUES(src.[role_id],src.[permission_id]);

MERGE INTO [${schema}].[role_permissions] AS tgt
USING (SELECT CONVERT(UNIQUEIDENTIFIER,HASHBYTES('MD5','VIEWER_${tenant_id}')) AS role_id, p.[id] AS permission_id FROM [${schema}].[permissions] p WHERE p.[module] = 'IN_APP_NOTIFICATION' AND p.[action] = 'READ') AS src
ON tgt.[role_id]=src.[role_id] AND tgt.[permission_id]=src.[permission_id]
WHEN NOT MATCHED THEN INSERT([role_id],[permission_id]) VALUES(src.[role_id],src.[permission_id]);
GO
