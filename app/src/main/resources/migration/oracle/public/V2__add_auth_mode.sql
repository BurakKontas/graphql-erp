-- V2__add_auth_mode.sql (Tenant Service)
ALTER TABLE "${schema}".tenants
    ADD auth_mode VARCHAR2(20) DEFAULT 'LOCAL' NOT NULL;
