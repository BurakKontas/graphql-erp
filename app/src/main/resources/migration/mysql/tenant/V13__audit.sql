-- MySQL adapted version of V13__audit.sql

CREATE TABLE IF NOT EXISTS audit_entries (
    id VARCHAR(36) PRIMARY KEY,
    tenant_id VARCHAR(36) NOT NULL,
    module_name VARCHAR(100),
    aggregate_type VARCHAR(100),
    aggregate_id VARCHAR(255),
    action VARCHAR(50),
    event_type VARCHAR(255),
    user_id VARCHAR(255),
    user_email VARCHAR(255),
    user_ip VARCHAR(100),
    user_agent VARCHAR(1000),
    occurred_at DATETIME(6) NOT NULL,
    before_snapshot LONGTEXT,
    after_snapshot LONGTEXT,
    correlation_id VARCHAR(255),
    company_id VARCHAR(36),
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS audit_field_changes (
    id VARCHAR(36) PRIMARY KEY,
    audit_entry_id VARCHAR(36) NOT NULL,
    field_name VARCHAR(255) NOT NULL,
    old_value TEXT,
    new_value TEXT,
    mask_level VARCHAR(20),
    CONSTRAINT fk_afc_entry FOREIGN KEY (audit_entry_id) REFERENCES audit_entries(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS audit_retention_policies (
    id VARCHAR(36) PRIMARY KEY,
    tenant_id VARCHAR(36),
    module_name VARCHAR(100),
    retention_days INT NOT NULL,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_audit_entries_tenant ON audit_entries(tenant_id);
CREATE INDEX idx_audit_entries_module_agg ON audit_entries(module_name, aggregate_type, aggregate_id);
CREATE INDEX idx_audit_entries_occurred ON audit_entries(occurred_at);
CREATE INDEX idx_audit_field_changes_entry ON audit_field_changes(audit_entry_id);

-- Permissions for audit viewing
INSERT IGNORE INTO permissions (id, module, action, description) VALUES
    ('00000000-0000-0000-0200-000000000001', 'AUDIT', 'VIEW', 'View audit entries'),
    ('00000000-0000-0000-0200-000000000002', 'AUDIT', 'RETENTION_MANAGE', 'Manage audit retention policies');

-- Assign AUDIT_VIEWER to VIEW permission and AUDIT_ADMIN to both
INSERT IGNORE INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM permissions p JOIN roles r ON r.tenant_id = '${tenant_id}' AND r.name = 'AUDIT_VIEWER'
LEFT JOIN role_permissions rp ON rp.role_id = r.id AND rp.permission_id = p.id
WHERE rp.role_id IS NULL AND p.module = 'AUDIT' AND p.action = 'VIEW';

INSERT IGNORE INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM permissions p JOIN roles r ON r.tenant_id = '${tenant_id}' AND r.name = 'AUDIT_ADMIN'
LEFT JOIN role_permissions rp ON rp.role_id = r.id AND rp.permission_id = p.id
WHERE rp.role_id IS NULL AND p.module = 'AUDIT';
