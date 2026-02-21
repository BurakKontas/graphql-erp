-- ─── Default Roles ───

-- ADMIN: full access to everything
INSERT INTO roles (id, tenant_id, name)
VALUES (md5('ADMIN_${tenant_id}')::uuid, '${tenant_id}'::uuid, 'ADMIN')
ON CONFLICT DO NOTHING;

-- MANAGER: all CRUD except GENERAL:ADMIN and DELETE
INSERT INTO roles (id, tenant_id, name)
VALUES (md5('MANAGER_${tenant_id}')::uuid, '${tenant_id}'::uuid, 'MANAGER')
ON CONFLICT DO NOTHING;

-- USER: CREATE, READ, UPDATE (no GENERAL, no DELETE)
INSERT INTO roles (id, tenant_id, name)
VALUES (md5('USER_${tenant_id}')::uuid, '${tenant_id}'::uuid, 'USER')
ON CONFLICT DO NOTHING;

-- VIEWER: READ only
INSERT INTO roles (id, tenant_id, name)
VALUES (md5('VIEWER_${tenant_id}')::uuid, '${tenant_id}'::uuid, 'VIEWER')
ON CONFLICT DO NOTHING;


-- ─── ADMIN → ALL permissions ───
INSERT INTO role_permissions (role_id, permission_id)
SELECT md5('ADMIN_${tenant_id}')::uuid, p.id
FROM permissions p
ON CONFLICT DO NOTHING;

-- ─── MANAGER → everything except GENERAL:ADMIN and DELETE ───
INSERT INTO role_permissions (role_id, permission_id)
SELECT md5('MANAGER_${tenant_id}')::uuid, p.id
FROM permissions p
WHERE NOT (p.module = 'GENERAL' AND p.action = 'ADMIN')
  AND p.action != 'DELETE'
ON CONFLICT DO NOTHING;

-- ─── USER → CREATE, READ, UPDATE (no GENERAL module) ───
INSERT INTO role_permissions (role_id, permission_id)
SELECT md5('USER_${tenant_id}')::uuid, p.id
FROM permissions p
WHERE p.action IN ('CREATE', 'READ', 'UPDATE')
  AND p.module != 'GENERAL'
ON CONFLICT DO NOTHING;

-- ─── VIEWER → READ only (no GENERAL module) ───
INSERT INTO role_permissions (role_id, permission_id)
SELECT md5('VIEWER_${tenant_id}')::uuid, p.id
FROM permissions p
WHERE p.action = 'READ'
  AND p.module != 'GENERAL'
ON CONFLICT DO NOTHING;
