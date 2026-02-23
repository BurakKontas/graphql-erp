-- V3__add_auth_settings.sql (Tenant Service - MSSQL)
-- BIGINT kept, BOOLEAN -> BIT, INTEGER -> INT

-- OIDC Settings
ALTER TABLE [${schema}].[tenants] ADD [oidc_issuer]            NVARCHAR(500) NULL;
ALTER TABLE [${schema}].[tenants] ADD [oidc_audience]          NVARCHAR(500) NULL;
ALTER TABLE [${schema}].[tenants] ADD [oidc_jwk_set_uri]       NVARCHAR(500) NULL;
ALTER TABLE [${schema}].[tenants] ADD [oidc_clock_skew_seconds] BIGINT        NULL DEFAULT 60;
GO

-- LDAP Settings
ALTER TABLE [${schema}].[tenants] ADD [ldap_urls]                       NVARCHAR(MAX) NULL;
ALTER TABLE [${schema}].[tenants] ADD [ldap_base_dn]                    NVARCHAR(500) NULL;
ALTER TABLE [${schema}].[tenants] ADD [ldap_user_search_filter]         NVARCHAR(500) NULL;
ALTER TABLE [${schema}].[tenants] ADD [ldap_bind_dn]                    NVARCHAR(500) NULL;
ALTER TABLE [${schema}].[tenants] ADD [ldap_bind_password]              NVARCHAR(500) NULL;
ALTER TABLE [${schema}].[tenants] ADD [ldap_connect_timeout_ms]         INT           NULL DEFAULT 5000;
ALTER TABLE [${schema}].[tenants] ADD [ldap_read_timeout_ms]            INT           NULL DEFAULT 5000;
ALTER TABLE [${schema}].[tenants] ADD [ldap_start_tls]                  BIT           NULL DEFAULT 0;
ALTER TABLE [${schema}].[tenants] ADD [ldap_max_retry]                  INT           NULL DEFAULT 2;
ALTER TABLE [${schema}].[tenants] ADD [ldap_circuit_breaker_timeout_ms] BIGINT        NULL DEFAULT 30000;
ALTER TABLE [${schema}].[tenants] ADD [ldap_resolve_groups]             BIT           NULL DEFAULT 0;
ALTER TABLE [${schema}].[tenants] ADD [ldap_group_search_filter]        NVARCHAR(500) NULL DEFAULT '(member={0})';
GO
