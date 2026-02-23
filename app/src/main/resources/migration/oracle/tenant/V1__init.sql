-- V1__init.sql (Main Application)
-- Oracle conversion notes:
--   UUID        -> VARCHAR2(32)
--   BOOLEAN     -> NUMBER(1,0)  [0=false, 1=true]
--   VARCHAR(n)  -> VARCHAR2(n)
--   CREATE SCHEMA / SET search_path -> not applicable in Oracle (use schema prefix)

-- ─── Companies ───
CREATE TABLE "${schema}".companies (
    id        VARCHAR2(32)       NOT NULL,
    active    NUMBER(1,0)   NOT NULL,
    name      VARCHAR2(255) NOT NULL,
    tenant_id VARCHAR2(32)       NOT NULL,
    CONSTRAINT pk_companies PRIMARY KEY (id)
);

-- ─── Departments ───
CREATE TABLE "${schema}".departments (
    id         VARCHAR2(32)       NOT NULL,
    tenant_id  VARCHAR2(32)       NOT NULL,
    company_id VARCHAR2(32)       NOT NULL,
    name       VARCHAR2(255) NOT NULL,
    parent_id  VARCHAR2(32),
    active     NUMBER(1,0)   DEFAULT 1 NOT NULL,
    CONSTRAINT pk_departments PRIMARY KEY (id),
    CONSTRAINT fk_dept_parent FOREIGN KEY (parent_id)
        REFERENCES "${schema}".departments (id)
);

CREATE INDEX idx_department_tenant_id  ON "${schema}".departments (tenant_id);
CREATE INDEX idx_department_company_id ON "${schema}".departments (company_id);

-- Department sub-departments (ElementCollection)
CREATE TABLE "${schema}".department_sub_departments (
    department_id     VARCHAR2(32) NOT NULL,
    sub_department_id VARCHAR2(32) NOT NULL,
    CONSTRAINT pk_dept_sub PRIMARY KEY (department_id, sub_department_id),
    CONSTRAINT fk_sub_dept_parent FOREIGN KEY (department_id)
        REFERENCES "${schema}".departments (id),
    CONSTRAINT fk_sub_dept_child  FOREIGN KEY (sub_department_id)
        REFERENCES "${schema}".departments (id)
);

-- ─── Employees ───
CREATE TABLE "${schema}".employees (
    id            VARCHAR2(32)       NOT NULL,
    tenant_id     VARCHAR2(32)       NOT NULL,
    name          VARCHAR2(255) NOT NULL,
    department_id VARCHAR2(32)       NOT NULL,
    active        NUMBER(1,0)   DEFAULT 1 NOT NULL,
    CONSTRAINT pk_employees PRIMARY KEY (id),
    CONSTRAINT fk_emp_department FOREIGN KEY (department_id)
        REFERENCES "${schema}".departments (id)
);

CREATE INDEX idx_employee_tenant_id     ON "${schema}".employees (tenant_id);
CREATE INDEX idx_employee_department_id ON "${schema}".employees (department_id);

-- ─── Business Partners ───
CREATE TABLE "${schema}".business_partner (
    id         VARCHAR2(32)       NOT NULL,
    tenant_id  VARCHAR2(32)       NOT NULL,
    company_id VARCHAR2(32)       NOT NULL,
    code       VARCHAR2(255) NOT NULL,
    name       VARCHAR2(255) NOT NULL,
    tax_number VARCHAR2(255) NOT NULL,
    active     NUMBER(1,0)   DEFAULT 1 NOT NULL,
    CONSTRAINT pk_business_partner PRIMARY KEY (id),
    CONSTRAINT fk_bp_company FOREIGN KEY (company_id)
        REFERENCES "${schema}".companies (id)
);

CREATE INDEX idx_bp_tenant_id  ON "${schema}".business_partner (tenant_id);
CREATE INDEX idx_bp_company_id ON "${schema}".business_partner (company_id);
CREATE INDEX idx_bp_code       ON "${schema}".business_partner (code);

-- Business Partner Roles
CREATE TABLE "${schema}".business_partner_roles (
    business_partner_id VARCHAR2(32)       NOT NULL,
    role                VARCHAR2(100) NOT NULL,
    CONSTRAINT pk_bp_roles PRIMARY KEY (business_partner_id, role),
    CONSTRAINT fk_bp_roles_partner FOREIGN KEY (business_partner_id)
        REFERENCES "${schema}".business_partner (id)
        ON DELETE CASCADE
);

CREATE INDEX idx_bp_roles_partner_id ON "${schema}".business_partner_roles (business_partner_id);
