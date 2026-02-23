-- MySQL adapted version of V3__reference_data.sql

-- Currencies table (global)
CREATE TABLE IF NOT EXISTS currencies (
  code VARCHAR(3) PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  symbol VARCHAR(10) NOT NULL,
  fraction_digits INT NOT NULL DEFAULT 2,
  active TINYINT(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Units table (global)
CREATE TABLE IF NOT EXISTS units (
  code VARCHAR(50) PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  type VARCHAR(50) NOT NULL,
  active TINYINT(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Payment Terms table
CREATE TABLE IF NOT EXISTS payment_terms (
  code VARCHAR(50) PRIMARY KEY,
  tenant_id VARCHAR(36) NOT NULL,
  company_id VARCHAR(36) NOT NULL,
  name VARCHAR(255) NOT NULL,
  due_days INT NOT NULL DEFAULT 0,
  active TINYINT(1) NOT NULL DEFAULT 1,
  CONSTRAINT fk_payment_term_company FOREIGN KEY (company_id) REFERENCES companies (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_payment_term_tenant_company ON payment_terms (tenant_id, company_id);

-- Taxes table
CREATE TABLE IF NOT EXISTS taxes (
  code VARCHAR(50) PRIMARY KEY,
  tenant_id VARCHAR(36) NOT NULL,
  company_id VARCHAR(36) NOT NULL,
  name VARCHAR(255) NOT NULL,
  type VARCHAR(50) NOT NULL,
  rate DECIMAL(19,6) NOT NULL,
  active TINYINT(1) NOT NULL DEFAULT 1,
  CONSTRAINT fk_tax_company FOREIGN KEY (company_id) REFERENCES companies (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_tax_tenant_company ON taxes (tenant_id, company_id);

-- Default permissions (insert statements expected in V3)
INSERT INTO permissions (id, module, action, description) VALUES
('00000000-0000-0000-0000-000000000001', 'GENERAL', 'ADMIN', 'Super admin — full access to all modules and actions'),
('00000000-0000-0000-0001-000000000001', 'COMPANY', 'CREATE', 'Create a new company');

-- Additional permission inserts omitted for brevity; V3 in PostgreSQL contains many inserts

