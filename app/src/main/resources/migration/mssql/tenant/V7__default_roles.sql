-- V7__default_roles.sql (Main Application - MSSQL)
-- md5(...)::uuid -> CONVERT(UNIQUEIDENTIFIER, HASHBYTES('MD5', ...)) not standard;
-- MSSQL does not have a built-in md5-to-uuid function.
-- We use HASHBYTES('MD5', ...) cast to a deterministic BINARY(16) then to UNIQUEIDENTIFIER.
-- Flyway placeholder ${tenant_id} must be configured.
-- ON CONFLICT DO NOTHING -> MERGE ... WHEN NOT MATCHED

-- ─── Helper: deterministic ID from role+tenant string ───
-- Pattern: CONVERT(UNIQUEIDENTIFIER, HASHBYTES('MD5', 'ROLE_<tenant_id>'))
-- Note: MSSQL UNIQUEIDENTIFIER is stored as 16 bytes.

-- ─── Default Roles ───
MERGE INTO [${schema}].[roles] AS tgt
USING (VALUES (CONVERT(UNIQUEIDENTIFIER, HASHBYTES('MD5', 'ADMIN_${tenant_id}')),
               CONVERT(UNIQUEIDENTIFIER, '${tenant_id}'), 'ADMIN')) AS src([id],[tenant_id],[name])
ON tgt.[id] = src.[id]
WHEN NOT MATCHED THEN INSERT ([id],[tenant_id],[name]) VALUES (src.[id],src.[tenant_id],src.[name]);

MERGE INTO [${schema}].[roles] AS tgt
USING (VALUES (CONVERT(UNIQUEIDENTIFIER, HASHBYTES('MD5', 'MANAGER_${tenant_id}')),
               CONVERT(UNIQUEIDENTIFIER, '${tenant_id}'), 'MANAGER')) AS src([id],[tenant_id],[name])
ON tgt.[id] = src.[id]
WHEN NOT MATCHED THEN INSERT ([id],[tenant_id],[name]) VALUES (src.[id],src.[tenant_id],src.[name]);

MERGE INTO [${schema}].[roles] AS tgt
USING (VALUES (CONVERT(UNIQUEIDENTIFIER, HASHBYTES('MD5', 'USER_${tenant_id}')),
               CONVERT(UNIQUEIDENTIFIER, '${tenant_id}'), 'USER')) AS src([id],[tenant_id],[name])
ON tgt.[id] = src.[id]
WHEN NOT MATCHED THEN INSERT ([id],[tenant_id],[name]) VALUES (src.[id],src.[tenant_id],src.[name]);

MERGE INTO [${schema}].[roles] AS tgt
USING (VALUES (CONVERT(UNIQUEIDENTIFIER, HASHBYTES('MD5', 'VIEWER_${tenant_id}')),
               CONVERT(UNIQUEIDENTIFIER, '${tenant_id}'), 'VIEWER')) AS src([id],[tenant_id],[name])
ON tgt.[id] = src.[id]
WHEN NOT MATCHED THEN INSERT ([id],[tenant_id],[name]) VALUES (src.[id],src.[tenant_id],src.[name]);
GO

-- ─── ADMIN → ALL permissions ───
MERGE INTO [${schema}].[role_permissions] AS tgt
USING (
    SELECT CONVERT(UNIQUEIDENTIFIER, HASHBYTES('MD5', 'ADMIN_${tenant_id}')) AS role_id, p.[id] AS permission_id
    FROM [${schema}].[permissions] p
) AS src ON tgt.[role_id] = src.[role_id] AND tgt.[permission_id] = src.[permission_id]
WHEN NOT MATCHED THEN INSERT ([role_id],[permission_id]) VALUES (src.[role_id], src.[permission_id]);
GO

-- ─── MANAGER → everything except GENERAL:ADMIN and DELETE ───
MERGE INTO [${schema}].[role_permissions] AS tgt
USING (
    SELECT CONVERT(UNIQUEIDENTIFIER, HASHBYTES('MD5', 'MANAGER_${tenant_id}')) AS role_id, p.[id] AS permission_id
    FROM [${schema}].[permissions] p
    WHERE NOT ([module] = 'GENERAL' AND [action] = 'ADMIN')
      AND [action] != 'DELETE'
) AS src ON tgt.[role_id] = src.[role_id] AND tgt.[permission_id] = src.[permission_id]
WHEN NOT MATCHED THEN INSERT ([role_id],[permission_id]) VALUES (src.[role_id], src.[permission_id]);
GO

-- ─── USER → CREATE, READ, UPDATE (no GENERAL module) ───
MERGE INTO [${schema}].[role_permissions] AS tgt
USING (
    SELECT CONVERT(UNIQUEIDENTIFIER, HASHBYTES('MD5', 'USER_${tenant_id}')) AS role_id, p.[id] AS permission_id
    FROM [${schema}].[permissions] p
    WHERE [action] IN ('CREATE', 'READ', 'UPDATE')
      AND [module] != 'GENERAL'
) AS src ON tgt.[role_id] = src.[role_id] AND tgt.[permission_id] = src.[permission_id]
WHEN NOT MATCHED THEN INSERT ([role_id],[permission_id]) VALUES (src.[role_id], src.[permission_id]);
GO

-- ─── VIEWER → READ only (no GENERAL module) ───
MERGE INTO [${schema}].[role_permissions] AS tgt
USING (
    SELECT CONVERT(UNIQUEIDENTIFIER, HASHBYTES('MD5', 'VIEWER_${tenant_id}')) AS role_id, p.[id] AS permission_id
    FROM [${schema}].[permissions] p
    WHERE [action] = 'READ'
      AND [module] != 'GENERAL'
) AS src ON tgt.[role_id] = src.[role_id] AND tgt.[permission_id] = src.[permission_id]
WHEN NOT MATCHED THEN INSERT ([role_id],[permission_id]) VALUES (src.[role_id], src.[permission_id]);
GO
