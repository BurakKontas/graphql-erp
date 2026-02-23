-- V13__audit.sql (Main Application)
-- Oracle conversion:
--   UUID                     -> VARCHAR2(32)
--   TEXT                     -> CLOB
--   VARCHAR                  -> VARCHAR2
--   TIMESTAMP WITH TIME ZONE -> TIMESTAMP WITH TIME ZONE
--   INT                      -> NUMBER(10)

CREATE TABLE "${schema}".audit_entries (
    id              VARCHAR2(32)                    NOT NULL,
    source          VARCHAR2(30)               NOT NULL,
    tenant_id       VARCHAR2(32)                    NOT NULL,
    company_id      VARCHAR2(255),
    module_name     VARCHAR2(50)               NOT NULL,
    aggregate_type  VARCHAR2(100)              NOT NULL,
    aggregate_id    VARCHAR2(255)              NOT NULL,
    action          VARCHAR2(30)               NOT NULL,
    event_type      VARCHAR2(255),
    user_id         VARCHAR2(255),
    user_email      VARCHAR2(255),
    user_ip         VARCHAR2(50),
    user_agent      VARCHAR2(500),
    occurred_at     TIMESTAMP WITH TIME ZONE   NOT NULL,
    before_snapshot CLOB,
    after_snapshot  CLOB,
    changes_json    CLOB,
    correlation_id  VARCHAR2(255),
    session_id      VARCHAR2(255),
    CONSTRAINT pk_audit_entries PRIMARY KEY (id)
);

CREATE INDEX idx_audit_entries_tenant    ON "${schema}".audit_entries (tenant_id);
CREATE INDEX idx_audit_entries_aggregate ON "${schema}".audit_entries (aggregate_type, aggregate_id, tenant_id);
CREATE INDEX idx_audit_entries_user      ON "${schema}".audit_entries (user_id, tenant_id, occurred_at);
CREATE INDEX idx_audit_entries_module    ON "${schema}".audit_entries (module_name, tenant_id, occurred_at);
CREATE INDEX idx_audit_entries_occurred  ON "${schema}".audit_entries (occurred_at);

CREATE TABLE "${schema}".audit_retention_policies (
    id             VARCHAR2(32)      NOT NULL,
    tenant_id      VARCHAR2(32)      NOT NULL,
    module_name    VARCHAR2(50),
    retention_days NUMBER(10)   DEFAULT 365 NOT NULL,
    CONSTRAINT pk_audit_retention_policies PRIMARY KEY (id)
);

-- ─── Audit Permissions ───
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (HEXTORAW('000000000000000000FF000000000001'), 'AUDIT', 'READ',  'View audit logs');
INSERT INTO "${schema}".permissions (id, module, action, description)
VALUES (HEXTORAW('000000000000000000FF000000000002'), 'AUDIT', 'ADMIN', 'Administer audit logs');

MERGE INTO "${schema}".role_permissions tgt
USING (SELECT STANDARD_HASH('ADMIN_${tenant_id}','MD5') AS role_id, p.id AS permission_id
       FROM "${schema}".permissions p WHERE p.module = 'AUDIT') src
ON (tgt.role_id = src.role_id AND tgt.permission_id = src.permission_id)
WHEN NOT MATCHED THEN INSERT (role_id, permission_id) VALUES (src.role_id, src.permission_id);

MERGE INTO "${schema}".role_permissions tgt
USING (SELECT STANDARD_HASH('MANAGER_${tenant_id}','MD5') AS role_id, p.id AS permission_id
       FROM "${schema}".permissions p WHERE p.module = 'AUDIT' AND p.action = 'READ') src
ON (tgt.role_id = src.role_id AND tgt.permission_id = src.permission_id)
WHEN NOT MATCHED THEN INSERT (role_id, permission_id) VALUES (src.role_id, src.permission_id);

COMMIT;
