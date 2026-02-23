-- MySQL adapted version of V14__notification.sql
-- Types and functions adjusted from PostgreSQL

CREATE TABLE IF NOT EXISTS tenant_notification_configs (
    id VARCHAR(36) PRIMARY KEY,
    tenant_id VARCHAR(36) NOT NULL,
    all_notifications_enabled TINYINT(1) NOT NULL DEFAULT 1,
    disabled_channels TEXT,
    disabled_keys TEXT,
    disabled_reason TEXT,
    disabled_by VARCHAR(255),
    disabled_at DATETIME(6),
    disabled_until DATETIME(6)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS notification_configs (
    id VARCHAR(36) PRIMARY KEY,
    tenant_id VARCHAR(36) NOT NULL,
    company_id VARCHAR(36) NOT NULL,
    event_type VARCHAR(255) NOT NULL,
    notification_key VARCHAR(255) NOT NULL,
    description TEXT,
    enabled_channels TEXT,
    delivery_timing VARCHAR(20) NOT NULL DEFAULT 'IMMEDIATE',
    cron_expression VARCHAR(100),
    recipient_roles TEXT,
    user_overridable TINYINT(1) NOT NULL DEFAULT 1,
    active TINYINT(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS notification_templates (
    id VARCHAR(36) PRIMARY KEY,
    tenant_id VARCHAR(36) NOT NULL,
    notification_key VARCHAR(255) NOT NULL,
    channel VARCHAR(20) NOT NULL,
    locale VARCHAR(10) NOT NULL DEFAULT 'TR',
    subject VARCHAR(500),
    body_template TEXT NOT NULL,
    system_template TINYINT(1) NOT NULL DEFAULT 0,
    active TINYINT(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS notification_preferences (
    id VARCHAR(36) PRIMARY KEY,
    tenant_id VARCHAR(36) NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    notification_key VARCHAR(255) NOT NULL,
    disabled_channels TEXT,
    preferred_locale VARCHAR(10)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS webhook_configs (
    id VARCHAR(36) PRIMARY KEY,
    tenant_id VARCHAR(36) NOT NULL,
    company_id VARCHAR(36) NOT NULL,
    target_url VARCHAR(1000) NOT NULL,
    secret_key VARCHAR(500),
    event_types TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    consecutive_failures INT NOT NULL DEFAULT 0,
    last_success_at DATETIME(6)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS notification_outbox (
    id VARCHAR(36) PRIMARY KEY,
    tenant_id VARCHAR(36) NOT NULL,
    notification_key VARCHAR(255) NOT NULL,
    channel VARCHAR(20) NOT NULL,
    recipient_user_id VARCHAR(255),
    recipient_address VARCHAR(500),
    subject VARCHAR(500),
    body TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    retry_count INT NOT NULL DEFAULT 0,
    scheduled_at DATETIME(6),
    sent_at DATETIME(6),
    failure_reason TEXT,
    correlation_id VARCHAR(255),
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS in_app_notifications (
    id VARCHAR(36) PRIMARY KEY,
    tenant_id VARCHAR(36) NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    notification_key VARCHAR(255) NOT NULL,
    title VARCHAR(500),
    body TEXT,
    action_url VARCHAR(1000),
    `read` TINYINT(1) NOT NULL DEFAULT 0,
    read_at DATETIME(6),
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS notification_suppress_log (
    id VARCHAR(36) PRIMARY KEY,
    tenant_id VARCHAR(36) NOT NULL,
    correlation_id VARCHAR(255),
    notification_key VARCHAR(255) NOT NULL,
    channel VARCHAR(20) NOT NULL,
    recipient_user_id VARCHAR(255),
    suppress_reason VARCHAR(50) NOT NULL,
    occurred_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_tenant_notif_config_tenant ON tenant_notification_configs(tenant_id);
CREATE INDEX idx_notif_config_tenant_company ON notification_configs(tenant_id, company_id);
CREATE INDEX idx_notif_config_event_type ON notification_configs(event_type, tenant_id, company_id);
CREATE INDEX idx_notif_template_tenant ON notification_templates(tenant_id);
CREATE INDEX idx_notif_template_key_channel ON notification_templates(notification_key, channel, tenant_id);
CREATE INDEX idx_notif_pref_user ON notification_preferences(user_id, tenant_id);
CREATE INDEX idx_webhook_config_tenant_company ON webhook_configs(tenant_id, company_id);
CREATE INDEX idx_notif_outbox_status ON notification_outbox(status, retry_count);
CREATE INDEX idx_notif_outbox_tenant ON notification_outbox(tenant_id);
CREATE INDEX idx_in_app_notif_user ON in_app_notifications(user_id, tenant_id);
CREATE INDEX idx_in_app_notif_created ON in_app_notifications(created_at);
CREATE INDEX idx_suppress_log_tenant ON notification_suppress_log(tenant_id);

-- INSERT permissions (use INSERT IGNORE to avoid duplicates)
INSERT IGNORE INTO permissions (id, module, action, description) VALUES
    ('00000000-0000-0000-0100-000000000001', 'NOTIFICATION_CONFIG', 'CREATE', 'Create notification config'),
    ('00000000-0000-0000-0100-000000000002', 'NOTIFICATION_CONFIG', 'READ', 'View notification configs'),
    ('00000000-0000-0000-0100-000000000003', 'NOTIFICATION_CONFIG', 'UPDATE', 'Update notification configs'),
    ('00000000-0000-0000-0100-000000000004', 'NOTIFICATION_CONFIG', 'DELETE', 'Delete notification configs'),
    ('00000000-0000-0000-0101-000000000001', 'NOTIFICATION_TEMPLATE', 'CREATE', 'Create notification template'),
    ('00000000-0000-0000-0101-000000000002', 'NOTIFICATION_TEMPLATE', 'READ', 'View notification templates'),
    ('00000000-0000-0000-0101-000000000003', 'NOTIFICATION_TEMPLATE', 'UPDATE', 'Update notification templates'),
    ('00000000-0000-0000-0101-000000000004', 'NOTIFICATION_TEMPLATE', 'DELETE', 'Delete notification templates'),
    ('00000000-0000-0000-0102-000000000001', 'NOTIFICATION_PREFERENCE', 'CREATE', 'Create notification preference'),
    ('00000000-0000-0000-0102-000000000002', 'NOTIFICATION_PREFERENCE', 'READ', 'View notification preferences'),
    ('00000000-0000-0000-0102-000000000003', 'NOTIFICATION_PREFERENCE', 'UPDATE', 'Update notification preferences'),
    ('00000000-0000-0000-0102-000000000004', 'NOTIFICATION_PREFERENCE', 'DELETE', 'Delete notification preferences'),
    ('00000000-0000-0000-0103-000000000001', 'WEBHOOK_CONFIG', 'CREATE', 'Create webhook config'),
    ('00000000-0000-0000-0103-000000000002', 'WEBHOOK_CONFIG', 'READ', 'View webhook configs'),
    ('00000000-0000-0000-0103-000000000003', 'WEBHOOK_CONFIG', 'UPDATE', 'Update webhook configs'),
    ('00000000-0000-0000-0103-000000000004', 'WEBHOOK_CONFIG', 'DELETE', 'Delete webhook configs'),
    ('00000000-0000-0000-0104-000000000001', 'IN_APP_NOTIFICATION', 'CREATE', 'Create in-app notification'),
    ('00000000-0000-0000-0104-000000000002', 'IN_APP_NOTIFICATION', 'READ', 'View in-app notifications'),
    ('00000000-0000-0000-0104-000000000003', 'IN_APP_NOTIFICATION', 'UPDATE', 'Update in-app notifications'),
    ('00000000-0000-0000-0104-000000000004', 'IN_APP_NOTIFICATION', 'DELETE', 'Delete in-app notifications');

-- Assign notification permissions to roles (by matching role name + tenant)
-- ADMIN: full set
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM permissions p
JOIN roles r ON r.tenant_id = '${tenant_id}' AND r.name = 'ADMIN'
LEFT JOIN role_permissions rp ON rp.role_id = r.id AND rp.permission_id = p.id
WHERE rp.role_id IS NULL
  AND p.module IN ('NOTIFICATION_CONFIG','NOTIFICATION_TEMPLATE','NOTIFICATION_PREFERENCE','WEBHOOK_CONFIG','IN_APP_NOTIFICATION');

-- MANAGER: same but no DELETE
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM permissions p
JOIN roles r ON r.tenant_id = '${tenant_id}' AND r.name = 'MANAGER'
LEFT JOIN role_permissions rp ON rp.role_id = r.id AND rp.permission_id = p.id
WHERE rp.role_id IS NULL
  AND p.module IN ('NOTIFICATION_CONFIG','NOTIFICATION_TEMPLATE','NOTIFICATION_PREFERENCE','WEBHOOK_CONFIG','IN_APP_NOTIFICATION')
  AND p.action != 'DELETE';

-- USER: preference & in_app create/read/update
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM permissions p
JOIN roles r ON r.tenant_id = '${tenant_id}' AND r.name = 'USER'
LEFT JOIN role_permissions rp ON rp.role_id = r.id AND rp.permission_id = p.id
WHERE rp.role_id IS NULL
  AND p.module IN ('NOTIFICATION_PREFERENCE','IN_APP_NOTIFICATION')
  AND p.action IN ('CREATE','READ','UPDATE');

-- VIEWER: in-app read only
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM permissions p
JOIN roles r ON r.tenant_id = '${tenant_id}' AND r.name = 'VIEWER'
LEFT JOIN role_permissions rp ON rp.role_id = r.id AND rp.permission_id = p.id
WHERE rp.role_id IS NULL
  AND p.module = 'IN_APP_NOTIFICATION'
  AND p.action = 'READ';
