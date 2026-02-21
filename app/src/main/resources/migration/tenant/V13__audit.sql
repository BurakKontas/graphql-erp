CREATE TABLE audit_entries (
    id UUID PRIMARY KEY,
    source VARCHAR(30) NOT NULL,
    tenant_id UUID NOT NULL,
    company_id VARCHAR(255),
    module_name VARCHAR(50) NOT NULL,
    aggregate_type VARCHAR(100) NOT NULL,
    aggregate_id VARCHAR(255) NOT NULL,
    action VARCHAR(30) NOT NULL,
    event_type VARCHAR(255),
    user_id VARCHAR(255),
    user_email VARCHAR(255),
    user_ip VARCHAR(50),
    user_agent VARCHAR(500),
    occurred_at TIMESTAMP WITH TIME ZONE NOT NULL,
    before_snapshot TEXT,
    after_snapshot TEXT,
    changes_json TEXT,
    correlation_id VARCHAR(255),
    session_id VARCHAR(255)
);

CREATE INDEX idx_audit_entries_tenant ON audit_entries(tenant_id);
CREATE INDEX idx_audit_entries_aggregate ON audit_entries(aggregate_type, aggregate_id, tenant_id);
CREATE INDEX idx_audit_entries_user ON audit_entries(user_id, tenant_id, occurred_at);
CREATE INDEX idx_audit_entries_module ON audit_entries(module_name, tenant_id, occurred_at);
CREATE INDEX idx_audit_entries_occurred ON audit_entries(occurred_at);

CREATE TABLE audit_retention_policies (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    module_name VARCHAR(50),
    retention_days INT NOT NULL DEFAULT 365
);

INSERT INTO permissions (id, module, action, description)
VALUES
    ('00000000-0000-0000-00FF-000000000001', 'AUDIT', 'READ', 'View audit logs'),
    ('00000000-0000-0000-00FF-000000000002', 'AUDIT', 'ADMIN', 'Administer audit logs')
ON CONFLICT DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT md5('ADMIN_${tenant_id}')::uuid, p.id
FROM permissions p
WHERE p.module = 'AUDIT'
ON CONFLICT DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT md5('MANAGER_${tenant_id}')::uuid, p.id
FROM permissions p
WHERE p.module = 'AUDIT' AND p.action = 'READ'
ON CONFLICT DO NOTHING;

