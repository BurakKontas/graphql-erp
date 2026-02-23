-- MySQL adapted version of V7__default_roles.sql
-- Note: deterministic UUID generation from md5 is PostgreSQL specific; using UUID() fallback. If deterministic ids are required, replace with application-side generation.

INSERT INTO roles (id, tenant_id, name)
SELECT UUID(), '${tenant_id}', 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE tenant_id = '${tenant_id}' AND name = 'ADMIN');

INSERT INTO roles (id, tenant_id, name)
SELECT UUID(), '${tenant_id}', 'MANAGER'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE tenant_id = '${tenant_id}' AND name = 'MANAGER');

INSERT INTO roles (id, tenant_id, name)
SELECT UUID(), '${tenant_id}', 'USER'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE tenant_id = '${tenant_id}' AND name = 'USER');

INSERT INTO roles (id, tenant_id, name)
SELECT UUID(), '${tenant_id}', 'VIEWER'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE tenant_id = '${tenant_id}' AND name = 'VIEWER');

-- Assign permissions
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM permissions p
JOIN roles r ON r.tenant_id = '${tenant_id}' AND r.name = 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM role_permissions rp WHERE rp.role_id = r.id AND rp.permission_id = p.id);

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM permissions p
JOIN roles r ON r.tenant_id = '${tenant_id}' AND r.name = 'MANAGER'
WHERE NOT EXISTS (SELECT 1 FROM role_permissions rp WHERE rp.role_id = r.id AND rp.permission_id = p.id)
  AND NOT (p.module = 'GENERAL' AND p.action = 'ADMIN')
  AND p.action != 'DELETE';

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM permissions p
JOIN roles r ON r.tenant_id = '${tenant_id}' AND r.name = 'USER'
WHERE NOT EXISTS (SELECT 1 FROM role_permissions rp WHERE rp.role_id = r.id AND rp.permission_id = p.id)
  AND p.action IN ('CREATE','READ','UPDATE')
  AND p.module != 'GENERAL';

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM permissions p
JOIN roles r ON r.tenant_id = '${tenant_id}' AND r.name = 'VIEWER'
WHERE NOT EXISTS (SELECT 1 FROM role_permissions rp WHERE rp.role_id = r.id AND rp.permission_id = p.id)
  AND p.action = 'READ'
  AND p.module != 'GENERAL';

