-- V2__auth.sql (Main Application)
-- Oracle conversion:
--   UUID    -> VARCHAR2(32)
--   BOOLEAN -> NUMBER(1,0)
--   BIGINT  -> NUMBER(19)
--   VARCHAR -> VARCHAR2

CREATE TABLE "${schema}".permissions (
    id          VARCHAR2(32)       NOT NULL,
    module      VARCHAR2(255) NOT NULL,
    action      VARCHAR2(255) NOT NULL,
    description VARCHAR2(500) NOT NULL,
    CONSTRAINT pk_permissions PRIMARY KEY (id),
    CONSTRAINT uk_permissions_module_action UNIQUE (module, action)
);

CREATE TABLE "${schema}".roles (
    id        VARCHAR2(32)       NOT NULL,
    tenant_id VARCHAR2(32)       NOT NULL,
    name      VARCHAR2(255) NOT NULL,
    CONSTRAINT pk_roles PRIMARY KEY (id),
    CONSTRAINT uk_roles_tenant_name UNIQUE (tenant_id, name)
);

CREATE TABLE "${schema}".role_permissions (
    role_id       VARCHAR2(32) NOT NULL,
    permission_id VARCHAR2(32) NOT NULL,
    CONSTRAINT pk_role_permissions PRIMARY KEY (role_id, permission_id),
    CONSTRAINT fk_rp_role FOREIGN KEY (role_id)
        REFERENCES "${schema}".roles (id) ON DELETE CASCADE,
    CONSTRAINT fk_rp_permission FOREIGN KEY (permission_id)
        REFERENCES "${schema}".permissions (id) ON DELETE CASCADE
);

CREATE INDEX idx_role_permissions_role_id       ON "${schema}".role_permissions (role_id);
CREATE INDEX idx_role_permissions_permission_id ON "${schema}".role_permissions (permission_id);

CREATE TABLE "${schema}".users (
    id            VARCHAR2(32)       NOT NULL,
    tenant_id     VARCHAR2(32)       NOT NULL,
    username      VARCHAR2(255),
    password_hash VARCHAR2(255),
    auth_provider VARCHAR2(100),
    external_id   VARCHAR2(255),
    auth_version  NUMBER(19)    NOT NULL,
    active        NUMBER(1,0)   NOT NULL,
    locked        NUMBER(1,0)   NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uk_users_tenant_username UNIQUE (tenant_id, username),
    CONSTRAINT uk_users_tenant_provider_external UNIQUE (tenant_id, auth_provider, external_id)
);

CREATE INDEX idx_users_tenant_id ON "${schema}".users (tenant_id);

CREATE TABLE "${schema}".user_roles (
    user_id VARCHAR2(32) NOT NULL,
    role_id VARCHAR2(32) NOT NULL,
    CONSTRAINT pk_user_roles PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_ur_user FOREIGN KEY (user_id)
        REFERENCES "${schema}".users (id) ON DELETE CASCADE,
    CONSTRAINT fk_ur_role FOREIGN KEY (role_id)
        REFERENCES "${schema}".roles (id) ON DELETE CASCADE
);

CREATE INDEX idx_user_roles_user_id ON "${schema}".user_roles (user_id);
CREATE INDEX idx_user_roles_role_id ON "${schema}".user_roles (role_id);
