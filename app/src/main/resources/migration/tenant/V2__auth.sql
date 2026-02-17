CREATE TABLE permissions
(
    id          UUID PRIMARY KEY,
    module      VARCHAR(255) NOT NULL,
    action      VARCHAR(255) NOT NULL,
    description VARCHAR(500) NOT NULL,
    CONSTRAINT uk_permissions_module_action UNIQUE (module, action)
);

CREATE TABLE roles
(
    id        UUID PRIMARY KEY,
    tenant_id UUID         NOT NULL,
    name      VARCHAR(255) NOT NULL,
    CONSTRAINT uk_roles_tenant_name UNIQUE (tenant_id, name)
);

CREATE TABLE role_permissions
(
    role_id       UUID NOT NULL,
    permission_id UUID NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    CONSTRAINT fk_role_permissions_role
        FOREIGN KEY (role_id)
            REFERENCES roles (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_role_permissions_permission
        FOREIGN KEY (permission_id)
            REFERENCES permissions (id)
            ON DELETE CASCADE
);

CREATE INDEX idx_role_permissions_role_id ON role_permissions (role_id);
CREATE INDEX idx_role_permissions_permission_id ON role_permissions (permission_id);

CREATE TABLE users
(
    id            UUID PRIMARY KEY,
    tenant_id     UUID    NOT NULL,
    username      VARCHAR(255),
    password_hash VARCHAR(255),
    auth_provider VARCHAR(100),
    external_id   VARCHAR(255),
    auth_version  BIGINT  NOT NULL,
    active        BOOLEAN NOT NULL,
    locked        BOOLEAN NOT NULL,
    CONSTRAINT uk_users_tenant_username UNIQUE (tenant_id, username),
    CONSTRAINT uk_users_tenant_provider_external UNIQUE (tenant_id, auth_provider, external_id)
);

CREATE INDEX idx_users_tenant_id ON users (tenant_id);

CREATE TABLE user_roles
(
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_user_roles_role
        FOREIGN KEY (role_id)
            REFERENCES roles (id)
            ON DELETE CASCADE
);

CREATE INDEX idx_user_roles_user_id ON user_roles (user_id);
CREATE INDEX idx_user_roles_role_id ON user_roles (role_id);