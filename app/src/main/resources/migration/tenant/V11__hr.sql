CREATE TABLE positions (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    company_id UUID NOT NULL REFERENCES companies(id),
    code VARCHAR(50) NOT NULL,
    title VARCHAR(255) NOT NULL,
    department_id VARCHAR(36),
    level VARCHAR(20),
    salary_grade VARCHAR(50),
    headcount INT NOT NULL DEFAULT 1,
    filled_count INT NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    UNIQUE (tenant_id, company_id, code)
);

CREATE TABLE employees (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    company_id UUID NOT NULL REFERENCES companies(id),
    employee_number VARCHAR(50) NOT NULL,
    user_id VARCHAR(36),
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    date_of_birth DATE,
    national_id VARCHAR(50),
    gender VARCHAR(10),
    nationality VARCHAR(10),
    personal_email VARCHAR(255),
    work_email VARCHAR(255),
    phone VARCHAR(50),
    address_line VARCHAR(500),
    city VARCHAR(100),
    contact_country_code VARCHAR(10),
    position_id VARCHAR(36),
    department_id VARCHAR(36),
    manager_id VARCHAR(36),
    hire_date DATE NOT NULL,
    termination_date DATE,
    employment_type VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    country_code VARCHAR(10),
    UNIQUE (tenant_id, employee_number)
);

CREATE TABLE contracts (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    company_id UUID NOT NULL REFERENCES companies(id),
    contract_number VARCHAR(50) NOT NULL,
    employee_id VARCHAR(36) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    contract_type VARCHAR(20) NOT NULL,
    gross_salary NUMERIC(15,2) NOT NULL,
    currency_code VARCHAR(10),
    payroll_config_id VARCHAR(36),
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    components_json TEXT,
    UNIQUE (tenant_id, contract_number)
);

CREATE TABLE leave_policies (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    company_id UUID NOT NULL REFERENCES companies(id),
    name VARCHAR(255) NOT NULL,
    country_code VARCHAR(10),
    leave_types_json TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE leave_requests (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    company_id UUID NOT NULL REFERENCES companies(id),
    request_number VARCHAR(50) NOT NULL,
    employee_id VARCHAR(36) NOT NULL,
    approver_id VARCHAR(36),
    leave_type VARCHAR(20) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    requested_days INT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    reason TEXT,
    document_ref VARCHAR(255),
    rejection_reason TEXT,
    UNIQUE (tenant_id, request_number)
);

CREATE TABLE leave_balances (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    company_id UUID NOT NULL REFERENCES companies(id),
    employee_id VARCHAR(36) NOT NULL,
    leave_type VARCHAR(20) NOT NULL,
    year INT NOT NULL,
    entitlement_days INT NOT NULL DEFAULT 0,
    used_days INT NOT NULL DEFAULT 0,
    carryover_days INT NOT NULL DEFAULT 0,
    pending_days INT NOT NULL DEFAULT 0,
    UNIQUE (tenant_id, employee_id, leave_type, year)
);

CREATE TABLE attendances (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    company_id UUID NOT NULL REFERENCES companies(id),
    employee_id VARCHAR(36) NOT NULL,
    date DATE NOT NULL,
    source VARCHAR(10) NOT NULL,
    check_in TIME,
    check_out TIME,
    status VARCHAR(20) NOT NULL DEFAULT 'PRESENT',
    regular_minutes INT NOT NULL DEFAULT 0,
    overtime_minutes INT NOT NULL DEFAULT 0,
    device_id VARCHAR(100),
    notes TEXT,
    UNIQUE (tenant_id, employee_id, date)
);

CREATE TABLE payroll_configs (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    company_id UUID NOT NULL REFERENCES companies(id),
    country_code VARCHAR(10),
    name VARCHAR(255) NOT NULL,
    valid_year INT NOT NULL,
    minimum_wage NUMERIC(15,2),
    currency_code VARCHAR(10),
    tax_brackets_json TEXT,
    deductions_json TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE payroll_runs (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    company_id UUID NOT NULL REFERENCES companies(id),
    run_number VARCHAR(50) NOT NULL,
    year INT NOT NULL,
    month INT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    payment_date DATE,
    payroll_config_id VARCHAR(36),
    entries_json TEXT,
    UNIQUE (tenant_id, run_number)
);

CREATE TABLE performance_cycles (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    company_id UUID NOT NULL REFERENCES companies(id),
    name VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    review_deadline DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'PLANNED',
    goals_json TEXT
);

CREATE TABLE performance_reviews (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    company_id UUID NOT NULL REFERENCES companies(id),
    cycle_id VARCHAR(36) NOT NULL,
    employee_id VARCHAR(36) NOT NULL,
    reviewer_id VARCHAR(36),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    overall_rating INT DEFAULT 0,
    strengths TEXT,
    improvements TEXT,
    comments TEXT,
    goal_reviews_json TEXT,
    UNIQUE (tenant_id, cycle_id, employee_id)
);

CREATE TABLE job_postings (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    company_id UUID NOT NULL REFERENCES companies(id),
    posting_number VARCHAR(50) NOT NULL,
    position_id VARCHAR(36) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    requirements_json TEXT,
    employment_type VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    published_at DATE,
    closing_date DATE,
    UNIQUE (tenant_id, posting_number)
);

CREATE TABLE job_applications (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    company_id UUID NOT NULL REFERENCES companies(id),
    job_posting_id VARCHAR(36) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(50),
    cv_ref VARCHAR(500),
    status VARCHAR(20) NOT NULL DEFAULT 'APPLIED',
    current_stage_note TEXT,
    interviews_json TEXT,
    applied_at DATE NOT NULL
);

CREATE INDEX idx_positions_tenant_company ON positions(tenant_id, company_id);
CREATE INDEX idx_employees_tenant_company ON employees(tenant_id, company_id);
CREATE INDEX idx_contracts_tenant_company ON contracts(tenant_id, company_id);
CREATE INDEX idx_leave_policies_tenant_company ON leave_policies(tenant_id, company_id);
CREATE INDEX idx_leave_requests_tenant_company ON leave_requests(tenant_id, company_id);
CREATE INDEX idx_leave_balances_tenant_company ON leave_balances(tenant_id, company_id);
CREATE INDEX idx_attendances_tenant_company ON attendances(tenant_id, company_id);
CREATE INDEX idx_payroll_configs_tenant_company ON payroll_configs(tenant_id, company_id);
CREATE INDEX idx_payroll_runs_tenant_company ON payroll_runs(tenant_id, company_id);
CREATE INDEX idx_performance_cycles_tenant_company ON performance_cycles(tenant_id, company_id);
CREATE INDEX idx_performance_reviews_tenant_company ON performance_reviews(tenant_id, company_id);
CREATE INDEX idx_job_postings_tenant_company ON job_postings(tenant_id, company_id);
CREATE INDEX idx_job_applications_tenant_company ON job_applications(tenant_id, company_id);

INSERT INTO permissions (id, module, action, description)
VALUES
    ('00000000-0000-0000-00E0-000000000001', 'POSITION', 'CREATE', 'Create a position'),
    ('00000000-0000-0000-00E0-000000000002', 'POSITION', 'READ', 'View positions'),
    ('00000000-0000-0000-00E0-000000000003', 'POSITION', 'UPDATE', 'Update positions'),
    ('00000000-0000-0000-00E0-000000000004', 'POSITION', 'DELETE', 'Delete positions'),
    ('00000000-0000-0000-00E1-000000000001', 'HR_EMPLOYEE', 'CREATE', 'Create an HR employee'),
    ('00000000-0000-0000-00E1-000000000002', 'HR_EMPLOYEE', 'READ', 'View HR employees'),
    ('00000000-0000-0000-00E1-000000000003', 'HR_EMPLOYEE', 'UPDATE', 'Update HR employees'),
    ('00000000-0000-0000-00E1-000000000004', 'HR_EMPLOYEE', 'DELETE', 'Delete HR employees'),
    ('00000000-0000-0000-00E2-000000000001', 'CONTRACT', 'CREATE', 'Create a contract'),
    ('00000000-0000-0000-00E2-000000000002', 'CONTRACT', 'READ', 'View contracts'),
    ('00000000-0000-0000-00E2-000000000003', 'CONTRACT', 'UPDATE', 'Update contracts'),
    ('00000000-0000-0000-00E2-000000000004', 'CONTRACT', 'DELETE', 'Delete contracts'),
    ('00000000-0000-0000-00E3-000000000001', 'LEAVE_POLICY', 'CREATE', 'Create a leave policy'),
    ('00000000-0000-0000-00E3-000000000002', 'LEAVE_POLICY', 'READ', 'View leave policies'),
    ('00000000-0000-0000-00E3-000000000003', 'LEAVE_POLICY', 'UPDATE', 'Update leave policies'),
    ('00000000-0000-0000-00E3-000000000004', 'LEAVE_POLICY', 'DELETE', 'Delete leave policies'),
    ('00000000-0000-0000-00E4-000000000001', 'LEAVE_REQUEST', 'CREATE', 'Create a leave request'),
    ('00000000-0000-0000-00E4-000000000002', 'LEAVE_REQUEST', 'READ', 'View leave requests'),
    ('00000000-0000-0000-00E4-000000000003', 'LEAVE_REQUEST', 'UPDATE', 'Update leave requests'),
    ('00000000-0000-0000-00E4-000000000004', 'LEAVE_REQUEST', 'DELETE', 'Delete leave requests'),
    ('00000000-0000-0000-00E5-000000000001', 'LEAVE_BALANCE', 'CREATE', 'Create a leave balance'),
    ('00000000-0000-0000-00E5-000000000002', 'LEAVE_BALANCE', 'READ', 'View leave balances'),
    ('00000000-0000-0000-00E5-000000000003', 'LEAVE_BALANCE', 'UPDATE', 'Update leave balances'),
    ('00000000-0000-0000-00E5-000000000004', 'LEAVE_BALANCE', 'DELETE', 'Delete leave balances'),
    ('00000000-0000-0000-00E6-000000000001', 'ATTENDANCE', 'CREATE', 'Create attendance record'),
    ('00000000-0000-0000-00E6-000000000002', 'ATTENDANCE', 'READ', 'View attendance records'),
    ('00000000-0000-0000-00E6-000000000003', 'ATTENDANCE', 'UPDATE', 'Update attendance records'),
    ('00000000-0000-0000-00E6-000000000004', 'ATTENDANCE', 'DELETE', 'Delete attendance records'),
    ('00000000-0000-0000-00E7-000000000001', 'PAYROLL_CONFIG', 'CREATE', 'Create payroll config'),
    ('00000000-0000-0000-00E7-000000000002', 'PAYROLL_CONFIG', 'READ', 'View payroll configs'),
    ('00000000-0000-0000-00E7-000000000003', 'PAYROLL_CONFIG', 'UPDATE', 'Update payroll configs'),
    ('00000000-0000-0000-00E7-000000000004', 'PAYROLL_CONFIG', 'DELETE', 'Delete payroll configs'),
    ('00000000-0000-0000-00E8-000000000001', 'PAYROLL_RUN', 'CREATE', 'Create a payroll run'),
    ('00000000-0000-0000-00E8-000000000002', 'PAYROLL_RUN', 'READ', 'View payroll runs'),
    ('00000000-0000-0000-00E8-000000000003', 'PAYROLL_RUN', 'UPDATE', 'Update payroll runs'),
    ('00000000-0000-0000-00E8-000000000004', 'PAYROLL_RUN', 'DELETE', 'Delete payroll runs'),
    ('00000000-0000-0000-00E9-000000000001', 'PERFORMANCE_CYCLE', 'CREATE', 'Create performance cycle'),
    ('00000000-0000-0000-00E9-000000000002', 'PERFORMANCE_CYCLE', 'READ', 'View performance cycles'),
    ('00000000-0000-0000-00E9-000000000003', 'PERFORMANCE_CYCLE', 'UPDATE', 'Update performance cycles'),
    ('00000000-0000-0000-00E9-000000000004', 'PERFORMANCE_CYCLE', 'DELETE', 'Delete performance cycles'),
    ('00000000-0000-0000-00EA-000000000001', 'PERFORMANCE_REVIEW', 'CREATE', 'Create performance review'),
    ('00000000-0000-0000-00EA-000000000002', 'PERFORMANCE_REVIEW', 'READ', 'View performance reviews'),
    ('00000000-0000-0000-00EA-000000000003', 'PERFORMANCE_REVIEW', 'UPDATE', 'Update performance reviews'),
    ('00000000-0000-0000-00EA-000000000004', 'PERFORMANCE_REVIEW', 'DELETE', 'Delete performance reviews'),
    ('00000000-0000-0000-00EB-000000000001', 'JOB_POSTING', 'CREATE', 'Create a job posting'),
    ('00000000-0000-0000-00EB-000000000002', 'JOB_POSTING', 'READ', 'View job postings'),
    ('00000000-0000-0000-00EB-000000000003', 'JOB_POSTING', 'UPDATE', 'Update job postings'),
    ('00000000-0000-0000-00EB-000000000004', 'JOB_POSTING', 'DELETE', 'Delete job postings'),
    ('00000000-0000-0000-00EC-000000000001', 'JOB_APPLICATION', 'CREATE', 'Create a job application'),
    ('00000000-0000-0000-00EC-000000000002', 'JOB_APPLICATION', 'READ', 'View job applications'),
    ('00000000-0000-0000-00EC-000000000003', 'JOB_APPLICATION', 'UPDATE', 'Update job applications'),
    ('00000000-0000-0000-00EC-000000000004', 'JOB_APPLICATION', 'DELETE', 'Delete job applications')
ON CONFLICT DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT md5('ADMIN_${tenant_id}')::uuid, p.id
FROM permissions p
WHERE p.module IN (
    'POSITION', 'HR_EMPLOYEE', 'CONTRACT', 'LEAVE_POLICY', 'LEAVE_REQUEST',
    'LEAVE_BALANCE', 'ATTENDANCE', 'PAYROLL_CONFIG', 'PAYROLL_RUN',
    'PERFORMANCE_CYCLE', 'PERFORMANCE_REVIEW', 'JOB_POSTING', 'JOB_APPLICATION'
)
ON CONFLICT DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT md5('MANAGER_${tenant_id}')::uuid, p.id
FROM permissions p
WHERE p.module IN (
    'POSITION', 'HR_EMPLOYEE', 'CONTRACT', 'LEAVE_POLICY', 'LEAVE_REQUEST',
    'LEAVE_BALANCE', 'ATTENDANCE', 'PAYROLL_CONFIG', 'PAYROLL_RUN',
    'PERFORMANCE_CYCLE', 'PERFORMANCE_REVIEW', 'JOB_POSTING', 'JOB_APPLICATION'
)
  AND p.action != 'DELETE'
ON CONFLICT DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT md5('USER_${tenant_id}')::uuid, p.id
FROM permissions p
WHERE p.module IN (
    'POSITION', 'HR_EMPLOYEE', 'CONTRACT', 'LEAVE_POLICY', 'LEAVE_REQUEST',
    'LEAVE_BALANCE', 'ATTENDANCE', 'PAYROLL_CONFIG', 'PAYROLL_RUN',
    'PERFORMANCE_CYCLE', 'PERFORMANCE_REVIEW', 'JOB_POSTING', 'JOB_APPLICATION'
)
  AND p.action IN ('CREATE', 'READ', 'UPDATE')
ON CONFLICT DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT md5('VIEWER_${tenant_id}')::uuid, p.id
FROM permissions p
WHERE p.module IN (
    'POSITION', 'HR_EMPLOYEE', 'CONTRACT', 'LEAVE_POLICY', 'LEAVE_REQUEST',
    'LEAVE_BALANCE', 'ATTENDANCE', 'PAYROLL_CONFIG', 'PAYROLL_RUN',
    'PERFORMANCE_CYCLE', 'PERFORMANCE_REVIEW', 'JOB_POSTING', 'JOB_APPLICATION'
)
  AND p.action = 'READ'
ON CONFLICT DO NOTHING;

