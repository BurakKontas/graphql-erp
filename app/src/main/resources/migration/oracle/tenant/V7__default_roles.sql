-- V7__default_roles.sql (Main Application)
-- Oracle conversion:
--   md5(...)::uuid -> DBMS_CRYPTO.HASH based deterministic UUID
--   '${tenant_id}'::uuid -> HEXTORAW with placeholder
--   ON CONFLICT DO NOTHING -> MERGE INTO
--
-- NOTE: ${tenant_id} is a Flyway placeholder that must be configured.
--       In Oracle, VARCHAR2(32) UUIDs are stored as 16-byte hex.
--       We use STANDARD_HASH (available Oracle 12c+) to derive deterministic IDs.
--
-- Helper: deterministic role ID from role name + tenant_id string
-- STANDARD_HASH('ADMIN_<tenant_id>', 'MD5') returns 16-byte RAW

-- ─── Default Roles ───
MERGE INTO "${schema}".roles tgt
USING (SELECT STANDARD_HASH('ADMIN_${tenant_id}', 'MD5')   AS id,
              HEXTORAW(REPLACE('${tenant_id}', '-', ''))    AS tenant_id,
              'ADMIN'   AS name FROM DUAL) src
ON (tgt.id = src.id)
WHEN NOT MATCHED THEN INSERT (id, tenant_id, name) VALUES (src.id, src.tenant_id, src.name);

MERGE INTO "${schema}".roles tgt
USING (SELECT STANDARD_HASH('MANAGER_${tenant_id}', 'MD5') AS id,
              HEXTORAW(REPLACE('${tenant_id}', '-', ''))    AS tenant_id,
              'MANAGER' AS name FROM DUAL) src
ON (tgt.id = src.id)
WHEN NOT MATCHED THEN INSERT (id, tenant_id, name) VALUES (src.id, src.tenant_id, src.name);

MERGE INTO "${schema}".roles tgt
USING (SELECT STANDARD_HASH('USER_${tenant_id}', 'MD5')    AS id,
              HEXTORAW(REPLACE('${tenant_id}', '-', ''))    AS tenant_id,
              'USER'    AS name FROM DUAL) src
ON (tgt.id = src.id)
WHEN NOT MATCHED THEN INSERT (id, tenant_id, name) VALUES (src.id, src.tenant_id, src.name);

MERGE INTO "${schema}".roles tgt
USING (SELECT STANDARD_HASH('VIEWER_${tenant_id}', 'MD5')  AS id,
              HEXTORAW(REPLACE('${tenant_id}', '-', ''))    AS tenant_id,
              'VIEWER'  AS name FROM DUAL) src
ON (tgt.id = src.id)
WHEN NOT MATCHED THEN INSERT (id, tenant_id, name) VALUES (src.id, src.tenant_id, src.name);

-- ─── ADMIN → ALL permissions ───
MERGE INTO "${schema}".role_permissions tgt
USING (SELECT STANDARD_HASH('ADMIN_${tenant_id}', 'MD5') AS role_id, p.id AS permission_id
       FROM "${schema}".permissions p) src
ON (tgt.role_id = src.role_id AND tgt.permission_id = src.permission_id)
WHEN NOT MATCHED THEN INSERT (role_id, permission_id) VALUES (src.role_id, src.permission_id);

-- ─── MANAGER → everything except GENERAL:ADMIN and DELETE ───
MERGE INTO "${schema}".role_permissions tgt
USING (SELECT STANDARD_HASH('MANAGER_${tenant_id}', 'MD5') AS role_id, p.id AS permission_id
       FROM "${schema}".permissions p
       WHERE NOT (p.module = 'GENERAL' AND p.action = 'ADMIN')
         AND p.action != 'DELETE') src
ON (tgt.role_id = src.role_id AND tgt.permission_id = src.permission_id)
WHEN NOT MATCHED THEN INSERT (role_id, permission_id) VALUES (src.role_id, src.permission_id);

-- ─── USER → CREATE, READ, UPDATE (no GENERAL module) ───
MERGE INTO "${schema}".role_permissions tgt
USING (SELECT STANDARD_HASH('USER_${tenant_id}', 'MD5') AS role_id, p.id AS permission_id
       FROM "${schema}".permissions p
       WHERE p.action IN ('CREATE', 'READ', 'UPDATE')
         AND p.module != 'GENERAL') src
ON (tgt.role_id = src.role_id AND tgt.permission_id = src.permission_id)
WHEN NOT MATCHED THEN INSERT (role_id, permission_id) VALUES (src.role_id, src.permission_id);

-- ─── VIEWER → READ only (no GENERAL module) ───
MERGE INTO "${schema}".role_permissions tgt
USING (SELECT STANDARD_HASH('VIEWER_${tenant_id}', 'MD5') AS role_id, p.id AS permission_id
       FROM "${schema}".permissions p
       WHERE p.action = 'READ'
         AND p.module != 'GENERAL') src
ON (tgt.role_id = src.role_id AND tgt.permission_id = src.permission_id)
WHEN NOT MATCHED THEN INSERT (role_id, permission_id) VALUES (src.role_id, src.permission_id);

COMMIT;
