-- V11__hr.sql (Main Application)
-- Oracle conversion: UUID->VARCHAR2(32), BOOLEAN->NUMBER(1,0), TEXT->CLOB,
--                    VARCHAR->VARCHAR2, INT->NUMBER(10), NUMERIC->NUMBER

CREATE TABLE "${schema}".positions (
    id             VARCHAR2(32)       NOT NULL,
    tenant_id      VARCHAR2(32)       NOT NULL,
    company_id     VARCHAR2(32)       NOT NULL,
    code           VARCHAR2(50)  NOT NULL,
    title          VARCHAR2(255) NOT NULL,
    department_id  VARCHAR2(36),
    position_level VARCHAR2(20),
    salary_grade   VARCHAR2(50),
    headcount      NUMBER(10)    DEFAULT 1 NOT NULL,
    filled_count   NUMBER(10)    DEFAULT 0 NOT NULL,
    status         VARCHAR2(20)  DEFAULT 'ACTIVE' NOT NULL,
    CONSTRAINT pk_positions   PRIMARY KEY (id),
    CONSTRAINT fk_pos_company FOREIGN KEY (company_id) REFERENCES "${schema}".companies (id),
    CONSTRAINT uq_pos_code    UNIQUE (tenant_id, company_id, code)
);

CREATE TABLE "${schema}".hr_employees (
    id                   VARCHAR2(32)       NOT NULL,
    tenant_id            VARCHAR2(32)       NOT NULL,
    company_id           VARCHAR2(32)       NOT NULL,
    employee_number      VARCHAR2(50)  NOT NULL,
    user_id              VARCHAR2(36),
    first_name           VARCHAR2(100) NOT NULL,
    last_name            VARCHAR2(100) NOT NULL,
    date_of_birth        DATE,
    national_id          VARCHAR2(50),
    gender               VARCHAR2(10),
    nationality          VARCHAR2(10),
    personal_email       VARCHAR2(255),
    work_email           VARCHAR2(255),
    phone                VARCHAR2(50),
    address_line         VARCHAR2(500),
    city                 VARCHAR2(100),
    contact_country_code VARCHAR2(10),
    position_id          VARCHAR2(36),
    department_id        VARCHAR2(36),
    manager_id           VARCHAR2(36),
    hire_date            DATE          NOT NULL,
    termination_date     DATE,
    employment_type      VARCHAR2(20)  NOT NULL,
    status               VARCHAR2(20)  DEFAULT 'ACTIVE' NOT NULL,
    country_code         VARCHAR2(10),
    CONSTRAINT pk_hr_employees   PRIMARY KEY (id),
    CONSTRAINT fk_hre_company    FOREIGN KEY (company_id) REFERENCES "${schema}".companies (id),
    CONSTRAINT uq_hre_emp_number UNIQUE (tenant_id, employee_number)
);

CREATE TABLE "${schema}".contracts (
    id                VARCHAR2(32)       NOT NULL,
    tenant_id         VARCHAR2(32)       NOT NULL,
    company_id        VARCHAR2(32)       NOT NULL,
    contract_number   VARCHAR2(50)  NOT NULL,
    employee_id       VARCHAR2(36)  NOT NULL,
    start_date        DATE          NOT NULL,
    end_date          DATE,
    contract_type     VARCHAR2(20)  NOT NULL,
    gross_salary      NUMBER(15,2)  NOT NULL,
    currency_code     VARCHAR2(10),
    payroll_config_id VARCHAR2(36),
    status            VARCHAR2(20)  DEFAULT 'DRAFT' NOT NULL,
    components_json   CLOB,
    CONSTRAINT pk_contracts   PRIMARY KEY (id),
    CONSTRAINT fk_con_company FOREIGN KEY (company_id) REFERENCES "${schema}".companies (id),
    CONSTRAINT uq_con_number  UNIQUE (tenant_id, contract_number)
);

CREATE TABLE "${schema}".leave_policies (
    id               VARCHAR2(32)       NOT NULL,
    tenant_id        VARCHAR2(32)       NOT NULL,
    company_id       VARCHAR2(32)       NOT NULL,
    name             VARCHAR2(255) NOT NULL,
    country_code     VARCHAR2(10),
    leave_types_json CLOB,
    active           NUMBER(1,0)   DEFAULT 1 NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (company_id) REFERENCES "${schema}".companies (id)
);

CREATE TABLE "${schema}".leave_requests (
    id               VARCHAR2(32)       NOT NULL,
    tenant_id        VARCHAR2(32)       NOT NULL,
    company_id       VARCHAR2(32)       NOT NULL,
    request_number   VARCHAR2(50)  NOT NULL,
    employee_id      VARCHAR2(36)  NOT NULL,
    approver_id      VARCHAR2(36),
    leave_type       VARCHAR2(20)  NOT NULL,
    start_date       DATE          NOT NULL,
    end_date         DATE          NOT NULL,
    requested_days   NUMBER(10)    NOT NULL,
    status           VARCHAR2(20)  DEFAULT 'DRAFT' NOT NULL,
    reason           CLOB,
    document_ref     VARCHAR2(255),
    rejection_reason CLOB,
    PRIMARY KEY (id),
    FOREIGN KEY (company_id) REFERENCES "${schema}".companies (id),
    UNIQUE (tenant_id, request_number)
);

CREATE TABLE "${schema}".leave_balances (
    id                VARCHAR2(32)      NOT NULL,
    tenant_id         VARCHAR2(32)      NOT NULL,
    company_id        VARCHAR2(32)      NOT NULL,
    employee_id       VARCHAR2(36) NOT NULL,
    leave_type        VARCHAR2(20) NOT NULL,
    year              NUMBER(10)   NOT NULL,
    entitlement_days  NUMBER(10)   DEFAULT 0 NOT NULL,
    used_days         NUMBER(10)   DEFAULT 0 NOT NULL,
    carryover_days    NUMBER(10)   DEFAULT 0 NOT NULL,
    pending_days      NUMBER(10)   DEFAULT 0 NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (company_id) REFERENCES "${schema}".companies (id),
    UNIQUE (tenant_id, employee_id, leave_type, year)
);

CREATE TABLE "${schema}".attendances (
    id               VARCHAR2(32)      NOT NULL,
    tenant_id        VARCHAR2(32)      NOT NULL,
    company_id       VARCHAR2(32)      NOT NULL,
    employee_id      VARCHAR2(36) NOT NULL,
    date_col         DATE         NOT NULL,  -- "date" is reserved in Oracle; renamed to date_col
    source           VARCHAR2(10) NOT NULL,
    check_in         VARCHAR2(8),            -- Oracle has no TIME type; store as HH24:MI:SS string
    check_out        VARCHAR2(8),
    status           VARCHAR2(20) DEFAULT 'PRESENT' NOT NULL,
    regular_minutes  NUMBER(10)   DEFAULT 0 NOT NULL,
    overtime_minutes NUMBER(10)   DEFAULT 0 NOT NULL,
    device_id        VARCHAR2(100),
    notes            CLOB,
    PRIMARY KEY (id),
    FOREIGN KEY (company_id) REFERENCES "${schema}".companies (id),
    UNIQUE (tenant_id, employee_id, date_col)
);

CREATE TABLE "${schema}".payroll_configs (
    id                 VARCHAR2(32)       NOT NULL,
    tenant_id          VARCHAR2(32)       NOT NULL,
    company_id         VARCHAR2(32)       NOT NULL,
    country_code       VARCHAR2(10),
    name               VARCHAR2(255) NOT NULL,
    valid_year         NUMBER(10)    NOT NULL,
    minimum_wage       NUMBER(15,2),
    currency_code      VARCHAR2(10),
    tax_brackets_json  CLOB,
    deductions_json    CLOB,
    active             NUMBER(1,0)   DEFAULT 1 NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (company_id) REFERENCES "${schema}".companies (id)
);

CREATE TABLE "${schema}".payroll_runs (
    id                VARCHAR2(32)       NOT NULL,
    tenant_id         VARCHAR2(32)       NOT NULL,
    company_id        VARCHAR2(32)       NOT NULL,
    run_number        VARCHAR2(50)  NOT NULL,
    year              NUMBER(10)    NOT NULL,
    month             NUMBER(10)    NOT NULL,
    status            VARCHAR2(20)  DEFAULT 'DRAFT' NOT NULL,
    payment_date      DATE,
    payroll_config_id VARCHAR2(36),
    entries_json      CLOB,
    PRIMARY KEY (id),
    FOREIGN KEY (company_id) REFERENCES "${schema}".companies (id),
    UNIQUE (tenant_id, run_number)
);

CREATE TABLE "${schema}".performance_cycles (
    id              VARCHAR2(32)       NOT NULL,
    tenant_id       VARCHAR2(32)       NOT NULL,
    company_id      VARCHAR2(32)       NOT NULL,
    name            VARCHAR2(255) NOT NULL,
    start_date      DATE          NOT NULL,
    end_date        DATE          NOT NULL,
    review_deadline DATE,
    status          VARCHAR2(20)  DEFAULT 'PLANNED' NOT NULL,
    goals_json      CLOB,
    CONSTRAINT pk_performance_cycles PRIMARY KEY (id),
    CONSTRAINT fk_pcy_company        FOREIGN KEY (company_id) REFERENCES "${schema}".companies (id)
);

CREATE TABLE "${schema}".performance_reviews (
    id              VARCHAR2(32)      NOT NULL,
    tenant_id       VARCHAR2(32)      NOT NULL,
    company_id      VARCHAR2(32)      NOT NULL,
    cycle_id        VARCHAR2(36) NOT NULL,
    employee_id     VARCHAR2(36) NOT NULL,
    reviewer_id     VARCHAR2(36),
    status          VARCHAR2(20) DEFAULT 'PENDING' NOT NULL,
    overall_rating  NUMBER(10)   DEFAULT 0,
    strengths       CLOB,
    improvements    CLOB,
    comments        CLOB,
    goal_reviews_json CLOB,
    CONSTRAINT pk_performance_reviews PRIMARY KEY (id),
    CONSTRAINT fk_prv_company         FOREIGN KEY (company_id) REFERENCES "${schema}".companies (id),
    CONSTRAINT uq_prv_cycle_employee  UNIQUE (tenant_id, cycle_id, employee_id)
);

CREATE TABLE "${schema}".job_postings (
    id                VARCHAR2(32)       NOT NULL,
    tenant_id         VARCHAR2(32)       NOT NULL,
    company_id        VARCHAR2(32)       NOT NULL,
    posting_number    VARCHAR2(50)  NOT NULL,
    position_id       VARCHAR2(36)  NOT NULL,
    title             VARCHAR2(255) NOT NULL,
    description       CLOB,
    requirements_json CLOB,
    employment_type   VARCHAR2(20)  NOT NULL,
    status            VARCHAR2(20)  DEFAULT 'DRAFT' NOT NULL,
    published_at      DATE,
    closing_date      DATE,
    CONSTRAINT pk_job_postings   PRIMARY KEY (id),
    CONSTRAINT fk_jp_company     FOREIGN KEY (company_id) REFERENCES "${schema}".companies (id),
    CONSTRAINT uq_jp_post_number UNIQUE (tenant_id, posting_number)
);

CREATE TABLE "${schema}".job_applications (
    id               VARCHAR2(32)       NOT NULL,
    tenant_id        VARCHAR2(32)       NOT NULL,
    company_id       VARCHAR2(32)       NOT NULL,
    job_posting_id   VARCHAR2(36)  NOT NULL,
    first_name       VARCHAR2(100) NOT NULL,
    last_name        VARCHAR2(100) NOT NULL,
    email            VARCHAR2(255) NOT NULL,
    phone            VARCHAR2(50),
    cv_ref           VARCHAR2(500),
    status           VARCHAR2(20)  DEFAULT 'APPLIED' NOT NULL,
    current_stage_note CLOB,
    interviews_json  CLOB,
    applied_at       DATE          NOT NULL,
    CONSTRAINT pk_job_applications PRIMARY KEY (id),
    CONSTRAINT fk_ja_company       FOREIGN KEY (company_id) REFERENCES "${schema}".companies (id)
);

-- ─── Indexes ───
CREATE INDEX idx_positions_tenant_company        ON "${schema}".positions (tenant_id, company_id);
CREATE INDEX idx_employees_tenant_company        ON "${schema}".hr_employees (tenant_id, company_id);
CREATE INDEX idx_contracts_tenant_company        ON "${schema}".contracts (tenant_id, company_id);
CREATE INDEX idx_leave_policies_tenant_company   ON "${schema}".leave_policies (tenant_id, company_id);
CREATE INDEX idx_leave_requests_tenant_company   ON "${schema}".leave_requests (tenant_id, company_id);
CREATE INDEX idx_leave_balances_tenant_company   ON "${schema}".leave_balances (tenant_id, company_id);
CREATE INDEX idx_attendances_tenant_company      ON "${schema}".attendances (tenant_id, company_id);
CREATE INDEX idx_payroll_configs_tenant_company  ON "${schema}".payroll_configs (tenant_id, company_id);
CREATE INDEX idx_payroll_runs_tenant_company     ON "${schema}".payroll_runs (tenant_id, company_id);
CREATE INDEX idx_performance_cycles_tenant_company ON "${schema}".performance_cycles (tenant_id, company_id);
CREATE INDEX idx_performance_reviews_tenant_company ON "${schema}".performance_reviews (tenant_id, company_id);
CREATE INDEX idx_job_postings_tenant_company     ON "${schema}".job_postings (tenant_id, company_id);
CREATE INDEX idx_job_applications_tenant_company ON "${schema}".job_applications (tenant_id, company_id);

-- ─── HR Permissions ───
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000E0000000000001'), 'POSITION', 'CREATE', 'Create a position');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000E0000000000002'), 'POSITION', 'READ',   'View positions');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000E0000000000003'), 'POSITION', 'UPDATE', 'Update positions');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000E0000000000004'), 'POSITION', 'DELETE', 'Delete positions');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000E1000000000001'), 'HR_EMPLOYEE', 'CREATE', 'Create an HR employee');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000E1000000000002'), 'HR_EMPLOYEE', 'READ',   'View HR employees');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000E1000000000003'), 'HR_EMPLOYEE', 'UPDATE', 'Update HR employees');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000E1000000000004'), 'HR_EMPLOYEE', 'DELETE', 'Delete HR employees');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000E2000000000001'), 'CONTRACT', 'CREATE', 'Create a contract');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000E2000000000002'), 'CONTRACT', 'READ',   'View contracts');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000E2000000000003'), 'CONTRACT', 'UPDATE', 'Update contracts');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000E2000000000004'), 'CONTRACT', 'DELETE', 'Delete contracts');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000E3000000000001'), 'LEAVE_POLICY', 'CREATE', 'Create a leave policy');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000E3000000000002'), 'LEAVE_POLICY', 'READ',   'View leave policies');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000E3000000000003'), 'LEAVE_POLICY', 'UPDATE', 'Update leave policies');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000E3000000000004'), 'LEAVE_POLICY', 'DELETE', 'Delete leave policies');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000E4000000000001'), 'LEAVE_REQUEST', 'CREATE', 'Create a leave request');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000E4000000000002'), 'LEAVE_REQUEST', 'READ',   'View leave requests');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000E4000000000003'), 'LEAVE_REQUEST', 'UPDATE', 'Update leave requests');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000E4000000000004'), 'LEAVE_REQUEST', 'DELETE', 'Delete leave requests');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000E5000000000001'), 'LEAVE_BALANCE', 'CREATE', 'Create a leave balance');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000E5000000000002'), 'LEAVE_BALANCE', 'READ',   'View leave balances');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000E5000000000003'), 'LEAVE_BALANCE', 'UPDATE', 'Update leave balances');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000E5000000000004'), 'LEAVE_BALANCE', 'DELETE', 'Delete leave balances');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000E6000000000001'), 'ATTENDANCE', 'CREATE', 'Create attendance record');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000E6000000000002'), 'ATTENDANCE', 'READ',   'View attendance records');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000E6000000000003'), 'ATTENDANCE', 'UPDATE', 'Update attendance records');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000E6000000000004'), 'ATTENDANCE', 'DELETE', 'Delete attendance records');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000E7000000000001'), 'PAYROLL_CONFIG', 'CREATE', 'Create payroll config');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000E7000000000002'), 'PAYROLL_CONFIG', 'READ',   'View payroll configs');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000E7000000000003'), 'PAYROLL_CONFIG', 'UPDATE', 'Update payroll configs');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000E7000000000004'), 'PAYROLL_CONFIG', 'DELETE', 'Delete payroll configs');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000E8000000000001'), 'PAYROLL_RUN', 'CREATE', 'Create a payroll run');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000E8000000000002'), 'PAYROLL_RUN', 'READ',   'View payroll runs');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000E8000000000003'), 'PAYROLL_RUN', 'UPDATE', 'Update payroll runs');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000E8000000000004'), 'PAYROLL_RUN', 'DELETE', 'Delete payroll runs');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000E9000000000001'), 'PERFORMANCE_CYCLE', 'CREATE', 'Create performance cycle');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000E9000000000002'), 'PERFORMANCE_CYCLE', 'READ',   'View performance cycles');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000E9000000000003'), 'PERFORMANCE_CYCLE', 'UPDATE', 'Update performance cycles');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000E9000000000004'), 'PERFORMANCE_CYCLE', 'DELETE', 'Delete performance cycles');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000EA000000000001'), 'PERFORMANCE_REVIEW', 'CREATE', 'Create performance review');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000EA000000000002'), 'PERFORMANCE_REVIEW', 'READ',   'View performance reviews');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000EA000000000003'), 'PERFORMANCE_REVIEW', 'UPDATE', 'Update performance reviews');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000EA000000000004'), 'PERFORMANCE_REVIEW', 'DELETE', 'Delete performance reviews');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000EB000000000001'), 'JOB_POSTING', 'CREATE', 'Create a job posting');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000EB000000000002'), 'JOB_POSTING', 'READ',   'View job postings');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000EB000000000003'), 'JOB_POSTING', 'UPDATE', 'Update job postings');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000EB000000000004'), 'JOB_POSTING', 'DELETE', 'Delete job postings');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000EC000000000001'), 'JOB_APPLICATION', 'CREATE', 'Create a job application');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000EC000000000002'), 'JOB_APPLICATION', 'READ',   'View job applications');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000EC000000000003'), 'JOB_APPLICATION', 'UPDATE', 'Update job applications');
INSERT INTO "${schema}".permissions (id, module, action, description) VALUES (HEXTORAW('000000000000000000EC000000000004'), 'JOB_APPLICATION', 'DELETE', 'Delete job applications');

-- ─── Role assignments ───
MERGE INTO "${schema}".role_permissions tgt
USING (SELECT STANDARD_HASH('ADMIN_${tenant_id}','MD5') AS role_id, p.id AS permission_id
       FROM "${schema}".permissions p
       WHERE p.module IN ('POSITION','HR_EMPLOYEE','CONTRACT','LEAVE_POLICY','LEAVE_REQUEST',
           'LEAVE_BALANCE','ATTENDANCE','PAYROLL_CONFIG','PAYROLL_RUN',
           'PERFORMANCE_CYCLE','PERFORMANCE_REVIEW','JOB_POSTING','JOB_APPLICATION')) src
ON (tgt.role_id = src.role_id AND tgt.permission_id = src.permission_id)
WHEN NOT MATCHED THEN INSERT (role_id, permission_id) VALUES (src.role_id, src.permission_id);

MERGE INTO "${schema}".role_permissions tgt
USING (SELECT STANDARD_HASH('MANAGER_${tenant_id}','MD5') AS role_id, p.id AS permission_id
       FROM "${schema}".permissions p
       WHERE p.module IN ('POSITION','HR_EMPLOYEE','CONTRACT','LEAVE_POLICY','LEAVE_REQUEST',
           'LEAVE_BALANCE','ATTENDANCE','PAYROLL_CONFIG','PAYROLL_RUN',
           'PERFORMANCE_CYCLE','PERFORMANCE_REVIEW','JOB_POSTING','JOB_APPLICATION')
         AND p.action != 'DELETE') src
ON (tgt.role_id = src.role_id AND tgt.permission_id = src.permission_id)
WHEN NOT MATCHED THEN INSERT (role_id, permission_id) VALUES (src.role_id, src.permission_id);

MERGE INTO "${schema}".role_permissions tgt
USING (SELECT STANDARD_HASH('USER_${tenant_id}','MD5') AS role_id, p.id AS permission_id
       FROM "${schema}".permissions p
       WHERE p.module IN ('POSITION','HR_EMPLOYEE','CONTRACT','LEAVE_POLICY','LEAVE_REQUEST',
           'LEAVE_BALANCE','ATTENDANCE','PAYROLL_CONFIG','PAYROLL_RUN',
           'PERFORMANCE_CYCLE','PERFORMANCE_REVIEW','JOB_POSTING','JOB_APPLICATION')
         AND p.action IN ('CREATE','READ','UPDATE')) src
ON (tgt.role_id = src.role_id AND tgt.permission_id = src.permission_id)
WHEN NOT MATCHED THEN INSERT (role_id, permission_id) VALUES (src.role_id, src.permission_id);

MERGE INTO "${schema}".role_permissions tgt
USING (SELECT STANDARD_HASH('VIEWER_${tenant_id}','MD5') AS role_id, p.id AS permission_id
       FROM "${schema}".permissions p
       WHERE p.module IN ('POSITION','HR_EMPLOYEE','CONTRACT','LEAVE_POLICY','LEAVE_REQUEST',
           'LEAVE_BALANCE','ATTENDANCE','PAYROLL_CONFIG','PAYROLL_RUN',
           'PERFORMANCE_CYCLE','PERFORMANCE_REVIEW','JOB_POSTING','JOB_APPLICATION')
         AND p.action = 'READ') src
ON (tgt.role_id = src.role_id AND tgt.permission_id = src.permission_id)
WHEN NOT MATCHED THEN INSERT (role_id, permission_id) VALUES (src.role_id, src.permission_id);

COMMIT;
