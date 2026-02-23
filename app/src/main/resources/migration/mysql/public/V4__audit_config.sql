-- MySQL adapted version of V4__audit_config.sql
ALTER TABLE tenants ADD COLUMN IF NOT EXISTS audit_unauthenticated TINYINT(1) NOT NULL DEFAULT 0;

