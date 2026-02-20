-- Companies table
CREATE TABLE companies
(
    id        uuid         NOT NULL PRIMARY KEY,
    active    BOOLEAN      NOT NULL,
    name      varchar(255) NOT NULL,
    tenant_id UUID         NOT NULL
);

-- Departments table
CREATE TABLE departments
(
    id         UUID PRIMARY KEY,
    tenant_id  UUID         NOT NULL,
    company_id UUID         NOT NULL,
    name       VARCHAR(255) NOT NULL,
    parent_id  UUID,
    active     BOOLEAN      NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_parent_department FOREIGN KEY (parent_id) REFERENCES departments (id)
);

-- Indexes for performance
CREATE INDEX idx_department_tenant_id ON departments (tenant_id);
CREATE INDEX idx_department_company_id ON departments (company_id);

-- Department sub-departments (ElementCollection)
CREATE TABLE department_sub_departments
(
    department_id     UUID NOT NULL,
    sub_department_id UUID NOT NULL,
    PRIMARY KEY (department_id, sub_department_id),
    CONSTRAINT fk_sub_department_parent FOREIGN KEY (department_id) REFERENCES departments (id),
    CONSTRAINT fk_sub_department FOREIGN KEY (sub_department_id) REFERENCES departments (id)
);

-- Employees table
CREATE TABLE employees
(
    id            UUID PRIMARY KEY,
    tenant_id     UUID         NOT NULL,
    name          VARCHAR(255) NOT NULL,
    department_id UUID         NOT NULL,
    active        BOOLEAN      NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_employee_department FOREIGN KEY (department_id) REFERENCES departments (id)
);

-- Indexes for performance
CREATE INDEX idx_employee_tenant_id ON employees (tenant_id);
CREATE INDEX idx_employee_department_id ON employees (department_id);

-- Business Partners table
CREATE TABLE business_partner
(
    id         UUID PRIMARY KEY,
    tenant_id  UUID         NOT NULL,
    company_id UUID         NOT NULL,
    code       VARCHAR(255) NOT NULL,
    name       VARCHAR(255) NOT NULL,
    tax_number VARCHAR(255) NOT NULL,
    active     BOOLEAN      NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_business_partner_company
        FOREIGN KEY (company_id) REFERENCES companies (id)
);

-- Indexes for performance
CREATE INDEX idx_bp_tenant_id ON business_partner (tenant_id);
CREATE INDEX idx_bp_company_id ON business_partner (company_id);
CREATE INDEX idx_bp_code ON business_partner (code);


CREATE TABLE business_partner_roles
(
    business_partner_id UUID         NOT NULL,
    role                VARCHAR(100) NOT NULL,

    PRIMARY KEY (business_partner_id, role),

    CONSTRAINT fk_bp_roles_partner
        FOREIGN KEY (business_partner_id)
            REFERENCES business_partner (id)
            ON DELETE CASCADE
);

CREATE INDEX idx_bp_roles_partner_id ON business_partner_roles (business_partner_id);