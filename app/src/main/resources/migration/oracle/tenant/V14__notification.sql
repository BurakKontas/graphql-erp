-- V14__notification.sql (Main Application)
-- Oracle conversion:
--   UUID      -> VARCHAR2(32)
--   BOOLEAN   -> NUMBER(1,0)
--   TEXT      -> CLOB
--   VARCHAR   -> VARCHAR2
--   TIMESTAMP -> TIMESTAMP
--   NOW()     -> SYSTIMESTAMP

CREATE TABLE "${schema}".tenant_notification_configs (
    id                          VARCHAR2(32)      NOT NULL,
    tenant_id                   VARCHAR2(32)      NOT NULL,
    all_notifications_enabled   NUMBER(1,0)  DEFAULT 1 NOT NULL,
    disabled_channels           CLOB,
    disabled_keys               CLOB,
    disabled_reason             CLOB,
    disabled_by                 VARCHAR2(255),
    disabled_at                 TIMESTAMP,
    disabled_until              TIMESTAMP,
    CONSTRAINT pk_tenant_notif_configs PRIMARY KEY (id)
);

CREATE TABLE "${schema}".notification_configs (
    id                  VARCHAR2(32)       NOT NULL,
    tenant_id           VARCHAR2(32)       NOT NULL,
    company_id          VARCHAR2(32)       NOT NULL,
    event_type          VARCHAR2(255) NOT NULL,
    notification_key    VARCHAR2(255) NOT NULL,
    description         CLOB,
    enabled_channels    CLOB,
    delivery_timing     VARCHAR2(20)  DEFAULT 'IMMEDIATE' NOT NULL,
    cron_expression     VARCHAR2(100),
    recipient_roles     CLOB,
    user_overridable    NUMBER(1,0)   DEFAULT 1 NOT NULL,
    active              NUMBER(1,0)   DEFAULT 1 NOT NULL,
    CONSTRAINT pk_notification_configs PRIMARY KEY (id)
);

CREATE TABLE "${schema}".notification_templates (
    id               VARCHAR2(32)       NOT NULL,
    tenant_id        VARCHAR2(32)       NOT NULL,
    notification_key VARCHAR2(255) NOT NULL,
    channel          VARCHAR2(20)  NOT NULL,
    locale           VARCHAR2(10)  DEFAULT 'TR' NOT NULL,
    subject          VARCHAR2(500),
    body_template    CLOB          NOT NULL,
    system_template  NUMBER(1,0)   DEFAULT 0 NOT NULL,
    active           NUMBER(1,0)   DEFAULT 1 NOT NULL,
    CONSTRAINT pk_notification_templates PRIMARY KEY (id)
);

CREATE TABLE "${schema}".notification_preferences (
    id               VARCHAR2(32)       NOT NULL,
    tenant_id        VARCHAR2(32)       NOT NULL,
    user_id          VARCHAR2(255) NOT NULL,
    notification_key VARCHAR2(255) NOT NULL,
    disabled_channels CLOB,
    preferred_locale VARCHAR2(10),
    CONSTRAINT pk_notification_preferences PRIMARY KEY (id)
);

CREATE TABLE "${schema}".webhook_configs (
    id                   VARCHAR2(32)        NOT NULL,
    tenant_id            VARCHAR2(32)        NOT NULL,
    company_id           VARCHAR2(32)        NOT NULL,
    target_url           VARCHAR2(1000) NOT NULL,
    secret_key           VARCHAR2(500),
    event_types          CLOB,
    status               VARCHAR2(20)   DEFAULT 'ACTIVE' NOT NULL,
    consecutive_failures NUMBER(10)     DEFAULT 0 NOT NULL,
    last_success_at      TIMESTAMP,
    CONSTRAINT pk_webhook_configs PRIMARY KEY (id)
);

CREATE TABLE "${schema}".notification_outbox (
    id                VARCHAR2(32)       NOT NULL,
    tenant_id         VARCHAR2(32)       NOT NULL,
    notification_key  VARCHAR2(255) NOT NULL,
    channel           VARCHAR2(20)  NOT NULL,
    recipient_user_id VARCHAR2(255),
    recipient_address VARCHAR2(500),
    subject           VARCHAR2(500),
    body              CLOB,
    status            VARCHAR2(20)  DEFAULT 'PENDING' NOT NULL,
    retry_count       NUMBER(10)    DEFAULT 0 NOT NULL,
    scheduled_at      TIMESTAMP,
    sent_at           TIMESTAMP,
    failure_reason    CLOB,
    correlation_id    VARCHAR2(255),
    created_at        TIMESTAMP     DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT pk_notification_outbox PRIMARY KEY (id)
);

CREATE TABLE "${schema}".in_app_notifications (
    id               VARCHAR2(32)        NOT NULL,
    tenant_id        VARCHAR2(32)        NOT NULL,
    user_id          VARCHAR2(255)  NOT NULL,
    notification_key VARCHAR2(255)  NOT NULL,
    title            VARCHAR2(500),
    body             CLOB,
    action_url       VARCHAR2(1000),
    read_flag        NUMBER(1,0)    DEFAULT 0 NOT NULL,  -- "read" is not reserved but safe to alias
    read_at          TIMESTAMP,
    created_at       TIMESTAMP      DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT pk_in_app_notifications PRIMARY KEY (id)
);

CREATE TABLE "${schema}".notification_suppress_log (
    id                VARCHAR2(32)       NOT NULL,
    tenant_id         VARCHAR2(32)       NOT NULL,
    correlation_id    VARCHAR2(255),
    notification_key  VARCHAR2(255) NOT NULL,
    channel           VARCHAR2(20)  NOT NULL,
    recipient_user_id VARCHAR2(255),
    suppress_reason   VARCHAR2(50)  NOT NULL,
    occurred_at       TIMESTAMP     DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT pk_notification_suppress_log PRIMARY KEY (id)
);

-- ─── Indexes ───
CREATE INDEX idx_tenant_notif_config_tenant  ON "${schema}".tenant_notification_configs (tenant_id);
CREATE INDEX idx_notif_config_tenant_company ON "${schema}".notification_configs (tenant_id, company_id);
CREATE INDEX idx_notif_config_event_type     ON "${schema}".notification_configs (event_type, tenant_id, company_id);
CREATE INDEX idx_notif_template_tenant       ON "${schema}".notification_templates (tenant_id);
CREATE INDEX idx_notif_template_key_channel  ON "${schema}".notification_templates (notification_key, channel, tenant_id);
CREATE INDEX idx_notif_pref_user             ON "${schema}".notification_preferences (user_id, tenant_id);
CREATE INDEX idx_webhook_config_tenant_co    ON "${schema}".webhook_configs (tenant_id, company_id);
CREATE INDEX idx_notif_outbox_status         ON "${schema}".notification_outbox (status, retry_count);
CREATE INDEX idx_notif_outbox_tenant         ON "${schema}".notification_outbox (tenant_id);
CREATE INDEX idx_in_app_notif_user           ON "${schema}".in_app_notifications (user_id, tenant_id);
CREATE INDEX idx_in_app_notif_created        ON "${schema}".in_app_notifications (created_at DESC);
CREATE INDEX idx_suppress_log_tenant         ON "${schema}".notification_suppress_log (tenant_id);

-- ─── Notification Permissions ───
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('00000000000000000100000000000001'), 'NOTIFICATION_CONFIG', 'CREATE', 'Create notification config');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('00000000000000000100000000000002'), 'NOTIFICATION_CONFIG', 'READ',   'View notification configs');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('00000000000000000100000000000003'), 'NOTIFICATION_CONFIG', 'UPDATE', 'Update notification configs');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('00000000000000000100000000000004'), 'NOTIFICATION_CONFIG', 'DELETE', 'Delete notification configs');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('00000000000000000101000000000001'), 'NOTIFICATION_TEMPLATE', 'CREATE', 'Create notification template');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('00000000000000000101000000000002'), 'NOTIFICATION_TEMPLATE', 'READ',   'View notification templates');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('00000000000000000101000000000003'), 'NOTIFICATION_TEMPLATE', 'UPDATE', 'Update notification templates');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('00000000000000000101000000000004'), 'NOTIFICATION_TEMPLATE', 'DELETE', 'Delete notification templates');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('00000000000000000102000000000001'), 'NOTIFICATION_PREFERENCE', 'CREATE', 'Create notification preference');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('00000000000000000102000000000002'), 'NOTIFICATION_PREFERENCE', 'READ',   'View notification preferences');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('00000000000000000102000000000003'), 'NOTIFICATION_PREFERENCE', 'UPDATE', 'Update notification preferences');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('00000000000000000102000000000004'), 'NOTIFICATION_PREFERENCE', 'DELETE', 'Delete notification preferences');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('00000000000000000103000000000001'), 'WEBHOOK_CONFIG', 'CREATE', 'Create webhook config');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('00000000000000000103000000000002'), 'WEBHOOK_CONFIG', 'READ',   'View webhook configs');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('00000000000000000103000000000003'), 'WEBHOOK_CONFIG', 'UPDATE', 'Update webhook configs');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('00000000000000000103000000000004'), 'WEBHOOK_CONFIG', 'DELETE', 'Delete webhook configs');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('00000000000000000104000000000001'), 'IN_APP_NOTIFICATION', 'CREATE', 'Create in-app notification');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('00000000000000000104000000000002'), 'IN_APP_NOTIFICATION', 'READ',   'View in-app notifications');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('00000000000000000104000000000003'), 'IN_APP_NOTIFICATION', 'UPDATE', 'Update in-app notifications');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('00000000000000000104000000000004'), 'IN_APP_NOTIFICATION', 'DELETE', 'Delete in-app notifications');

-- ─── Role assignments ───
MERGE INTO "${schema}".role_permissions tgt
USING (SELECT STANDARD_HASH('ADMIN_${tenant_id}','MD5') AS role_id, p.id AS permission_id
       FROM "${schema}".permissions p
       WHERE p.module IN ('NOTIFICATION_CONFIG','NOTIFICATION_TEMPLATE','NOTIFICATION_PREFERENCE','WEBHOOK_CONFIG','IN_APP_NOTIFICATION')) src
ON (tgt.role_id = src.role_id AND tgt.permission_id = src.permission_id)
WHEN NOT MATCHED THEN INSERT (role_id, permission_id) VALUES (src.role_id, src.permission_id);

MERGE INTO "${schema}".role_permissions tgt
USING (SELECT STANDARD_HASH('MANAGER_${tenant_id}','MD5') AS role_id, p.id AS permission_id
       FROM "${schema}".permissions p
       WHERE p.module IN ('NOTIFICATION_CONFIG','NOTIFICATION_TEMPLATE','NOTIFICATION_PREFERENCE','WEBHOOK_CONFIG','IN_APP_NOTIFICATION')
         AND p.action != 'DELETE') src
ON (tgt.role_id = src.role_id AND tgt.permission_id = src.permission_id)
WHEN NOT MATCHED THEN INSERT (role_id, permission_id) VALUES (src.role_id, src.permission_id);

MERGE INTO "${schema}".role_permissions tgt
USING (SELECT STANDARD_HASH('USER_${tenant_id}','MD5') AS role_id, p.id AS permission_id
       FROM "${schema}".permissions p
       WHERE p.module IN ('NOTIFICATION_PREFERENCE','IN_APP_NOTIFICATION')
         AND p.action IN ('CREATE','READ','UPDATE')) src
ON (tgt.role_id = src.role_id AND tgt.permission_id = src.permission_id)
WHEN NOT MATCHED THEN INSERT (role_id, permission_id) VALUES (src.role_id, src.permission_id);

MERGE INTO "${schema}".role_permissions tgt
USING (SELECT STANDARD_HASH('VIEWER_${tenant_id}','MD5') AS role_id, p.id AS permission_id
       FROM "${schema}".permissions p
       WHERE p.module = 'IN_APP_NOTIFICATION' AND p.action = 'READ') src
ON (tgt.role_id = src.role_id AND tgt.permission_id = src.permission_id)
WHEN NOT MATCHED THEN INSERT (role_id, permission_id) VALUES (src.role_id, src.permission_id);

COMMIT;
