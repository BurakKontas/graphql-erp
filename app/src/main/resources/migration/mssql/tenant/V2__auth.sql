-- V2__auth.sql (Main Application - MSSQL)
-- UUID -> UNIQUEIDENTIFIER, BOOLEAN -> BIT, BIGINT kept

CREATE TABLE [${schema}].[permissions] (
    [id]          UNIQUEIDENTIFIER NOT NULL,
    [module]      NVARCHAR(255)    NOT NULL,
    [action]      NVARCHAR(255)    NOT NULL,
    [description] NVARCHAR(500)    NOT NULL,
    CONSTRAINT [PK_permissions]                   PRIMARY KEY ([id]),
    CONSTRAINT [UQ_permissions_module_action]      UNIQUE ([module], [action])
);
GO

CREATE TABLE [${schema}].[roles] (
    [id]        UNIQUEIDENTIFIER NOT NULL,
    [tenant_id] UNIQUEIDENTIFIER NOT NULL,
    [name]      NVARCHAR(255)    NOT NULL,
    CONSTRAINT [PK_roles]               PRIMARY KEY ([id]),
    CONSTRAINT [UQ_roles_tenant_name]   UNIQUE ([tenant_id], [name])
);
GO

CREATE TABLE [${schema}].[role_permissions] (
    [role_id]       UNIQUEIDENTIFIER NOT NULL,
    [permission_id] UNIQUEIDENTIFIER NOT NULL,
    CONSTRAINT [PK_role_permissions]       PRIMARY KEY ([role_id], [permission_id]),
    CONSTRAINT [FK_rp_role]                FOREIGN KEY ([role_id])
        REFERENCES [${schema}].[roles] ([id]) ON DELETE CASCADE,
    CONSTRAINT [FK_rp_permission]          FOREIGN KEY ([permission_id])
        REFERENCES [${schema}].[permissions] ([id]) ON DELETE CASCADE
);
GO

CREATE INDEX [idx_role_permissions_role_id]       ON [${schema}].[role_permissions] ([role_id]);
CREATE INDEX [idx_role_permissions_permission_id] ON [${schema}].[role_permissions] ([permission_id]);
GO

CREATE TABLE [${schema}].[users] (
    [id]            UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]     UNIQUEIDENTIFIER NOT NULL,
    [username]      NVARCHAR(255)    NULL,
    [password_hash] NVARCHAR(255)    NULL,
    [auth_provider] NVARCHAR(100)    NULL,
    [external_id]   NVARCHAR(255)    NULL,
    [auth_version]  BIGINT           NOT NULL,
    [active]        BIT              NOT NULL,
    [locked]        BIT              NOT NULL,
    CONSTRAINT [PK_users]                          PRIMARY KEY ([id]),
    CONSTRAINT [UQ_users_tenant_username]          UNIQUE ([tenant_id], [username]),
    CONSTRAINT [UQ_users_tenant_provider_external] UNIQUE ([tenant_id], [auth_provider], [external_id])
);
GO

CREATE INDEX [idx_users_tenant_id] ON [${schema}].[users] ([tenant_id]);
GO

CREATE TABLE [${schema}].[user_roles] (
    [user_id] UNIQUEIDENTIFIER NOT NULL,
    [role_id] UNIQUEIDENTIFIER NOT NULL,
    CONSTRAINT [PK_user_roles]  PRIMARY KEY ([user_id], [role_id]),
    CONSTRAINT [FK_ur_user]     FOREIGN KEY ([user_id])
        REFERENCES [${schema}].[users] ([id]) ON DELETE CASCADE,
    CONSTRAINT [FK_ur_role]     FOREIGN KEY ([role_id])
        REFERENCES [${schema}].[roles] ([id]) ON DELETE CASCADE
);
GO

CREATE INDEX [idx_user_roles_user_id] ON [${schema}].[user_roles] ([user_id]);
CREATE INDEX [idx_user_roles_role_id] ON [${schema}].[user_roles] ([role_id]);
GO
