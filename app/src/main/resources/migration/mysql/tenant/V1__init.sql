-- Ensure tenant schema/database exists (placeholder ${schema})
CREATE DATABASE IF NOT EXISTS `${schema}`;
USE `${schema}`;

-- Companies table
CREATE TABLE IF NOT EXISTS `${schema}`.companies (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  active TINYINT(1) NOT NULL,
  name VARCHAR(255) NOT NULL,
  tenant_id VARCHAR(36) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Departments table
CREATE TABLE IF NOT EXISTS `${schema}`.departments (
  id VARCHAR(36) PRIMARY KEY,
  tenant_id VARCHAR(36) NOT NULL,
  company_id VARCHAR(36) NOT NULL,
  name VARCHAR(255) NOT NULL,
  parent_id VARCHAR(36),
  active TINYINT(1) NOT NULL DEFAULT 1,
  CONSTRAINT fk_parent_department FOREIGN KEY (parent_id) REFERENCES `${schema}`.departments (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Indexes for performance
CREATE INDEX idx_department_tenant_id ON `${schema}`.departments (tenant_id);
CREATE INDEX idx_department_company_id ON `${schema}`.departments (company_id);

-- Department sub-departments
CREATE TABLE IF NOT EXISTS `${schema}`.department_sub_departments (
  department_id VARCHAR(36) NOT NULL,
  sub_department_id VARCHAR(36) NOT NULL,
  PRIMARY KEY (department_id, sub_department_id),
  CONSTRAINT fk_sub_department_parent FOREIGN KEY (department_id) REFERENCES `${schema}`.departments (id),
  CONSTRAINT fk_sub_department FOREIGN KEY (sub_department_id) REFERENCES `${schema}`.departments (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Employees table
CREATE TABLE IF NOT EXISTS `${schema}`.employees (
  id VARCHAR(36) PRIMARY KEY,
  tenant_id VARCHAR(36) NOT NULL,
  name VARCHAR(255) NOT NULL,
  department_id VARCHAR(36) NOT NULL,
  active TINYINT(1) NOT NULL DEFAULT 1,
  CONSTRAINT fk_employee_department FOREIGN KEY (department_id) REFERENCES `${schema}`.departments (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Indexes for performance
CREATE INDEX idx_employee_tenant_id ON `${schema}`.employees (tenant_id);
CREATE INDEX idx_employee_department_id ON `${schema}`.employees (department_id);

-- Business Partners table
CREATE TABLE IF NOT EXISTS `${schema}`.business_partner (
  id VARCHAR(36) PRIMARY KEY,
  tenant_id VARCHAR(36) NOT NULL,
  company_id VARCHAR(36) NOT NULL,
  code VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  tax_number VARCHAR(255) NOT NULL,
  active TINYINT(1) NOT NULL DEFAULT 1,
  CONSTRAINT fk_business_partner_company FOREIGN KEY (company_id) REFERENCES `${schema}`.companies (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Indexes for performance
CREATE INDEX idx_bp_tenant_id ON `${schema}`.business_partner (tenant_id);
CREATE INDEX idx_bp_company_id ON `${schema}`.business_partner (company_id);
CREATE INDEX idx_bp_code ON `${schema}`.business_partner (code);

CREATE TABLE IF NOT EXISTS `${schema}`.business_partner_roles (
  business_partner_id VARCHAR(36) NOT NULL,
  role VARCHAR(100) NOT NULL,
  PRIMARY KEY (business_partner_id, role),
  CONSTRAINT fk_bp_roles_partner FOREIGN KEY (business_partner_id) REFERENCES `${schema}`.business_partner (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_bp_roles_partner_id ON `${schema}`.business_partner_roles (business_partner_id);
