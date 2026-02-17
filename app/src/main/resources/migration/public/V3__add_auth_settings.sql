-- OIDC Settings
ALTER TABLE public.tenants
    ADD COLUMN oidc_issuer VARCHAR(500);
ALTER TABLE public.tenants
    ADD COLUMN oidc_audience VARCHAR(500);
ALTER TABLE public.tenants
    ADD COLUMN oidc_jwk_set_uri VARCHAR(500);
ALTER TABLE public.tenants
    ADD COLUMN oidc_clock_skew_seconds BIGINT DEFAULT 60;

-- LDAP Settings
ALTER TABLE public.tenants
    ADD COLUMN ldap_urls TEXT;
ALTER TABLE public.tenants
    ADD COLUMN ldap_base_dn VARCHAR(500);
ALTER TABLE public.tenants
    ADD COLUMN ldap_user_search_filter VARCHAR(500);
ALTER TABLE public.tenants
    ADD COLUMN ldap_bind_dn VARCHAR(500);
ALTER TABLE public.tenants
    ADD COLUMN ldap_bind_password VARCHAR(500);
ALTER TABLE public.tenants
    ADD COLUMN ldap_connect_timeout_ms INTEGER DEFAULT 5000;
ALTER TABLE public.tenants
    ADD COLUMN ldap_read_timeout_ms INTEGER DEFAULT 5000;
ALTER TABLE public.tenants
    ADD COLUMN ldap_start_tls BOOLEAN DEFAULT FALSE;
ALTER TABLE public.tenants
    ADD COLUMN ldap_max_retry INTEGER DEFAULT 2;
ALTER TABLE public.tenants
    ADD COLUMN ldap_circuit_breaker_timeout_ms BIGINT DEFAULT 30000;
ALTER TABLE public.tenants
    ADD COLUMN ldap_resolve_groups BOOLEAN DEFAULT FALSE;
ALTER TABLE public.tenants
    ADD COLUMN ldap_group_search_filter VARCHAR(500) DEFAULT '(member={0})';
