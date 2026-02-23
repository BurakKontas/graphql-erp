-- V3__add_auth_settings.sql (Tenant Service)
-- Oracle: BIGINT -> NUMBER(19), INTEGER -> NUMBER(10), BOOLEAN -> NUMBER(1,0)

-- OIDC Settings
ALTER TABLE "${schema}".tenants ADD oidc_issuer            VARCHAR2(500);
ALTER TABLE "${schema}".tenants ADD oidc_audience           VARCHAR2(500);
ALTER TABLE "${schema}".tenants ADD oidc_jwk_set_uri        VARCHAR2(500);
ALTER TABLE "${schema}".tenants ADD oidc_clock_skew_seconds NUMBER(19) DEFAULT 60;

-- LDAP Settings
ALTER TABLE "${schema}".tenants ADD ldap_urls                     CLOB;
ALTER TABLE "${schema}".tenants ADD ldap_base_dn                  VARCHAR2(500);
ALTER TABLE "${schema}".tenants ADD ldap_user_search_filter       VARCHAR2(500);
ALTER TABLE "${schema}".tenants ADD ldap_bind_dn                  VARCHAR2(500);
ALTER TABLE "${schema}".tenants ADD ldap_bind_password            VARCHAR2(500);
ALTER TABLE "${schema}".tenants ADD ldap_connect_timeout_ms       NUMBER(10)  DEFAULT 5000;
ALTER TABLE "${schema}".tenants ADD ldap_read_timeout_ms          NUMBER(10)  DEFAULT 5000;
ALTER TABLE "${schema}".tenants ADD ldap_start_tls                NUMBER(1,0) DEFAULT 0;
ALTER TABLE "${schema}".tenants ADD ldap_max_retry                NUMBER(10)  DEFAULT 2;
ALTER TABLE "${schema}".tenants ADD ldap_circuit_breaker_timeout_ms NUMBER(19) DEFAULT 30000;
ALTER TABLE "${schema}".tenants ADD ldap_resolve_groups           NUMBER(1,0) DEFAULT 0;
ALTER TABLE "${schema}".tenants ADD ldap_group_search_filter      VARCHAR2(500) DEFAULT '(member={0})';
