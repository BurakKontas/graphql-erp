-- MySQL adapted version of public V1__init.sql
-- Please review indexes/constraints for MySQL specifics
CREATE TABLE IF NOT EXISTS tenants (
    id VARCHAR(36) NOT NULL,
    code VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

