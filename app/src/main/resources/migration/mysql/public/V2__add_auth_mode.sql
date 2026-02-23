-- MySQL adapted version of V2__add_auth_mode.sql
ALTER TABLE tenants
    ADD COLUMN auth_mode VARCHAR(20) NOT NULL DEFAULT 'LOCAL';

