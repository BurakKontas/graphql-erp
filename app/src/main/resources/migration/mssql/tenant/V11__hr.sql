-- V11__hr.sql (Main Application - MSSQL)
-- UUID -> UNIQUEIDENTIFIER, BOOLEAN -> BIT, TEXT -> NVARCHAR(MAX),
-- VARCHAR -> NVARCHAR, INT -> INT, NUMERIC -> DECIMAL
-- TIME type does not exist in MSSQL the same way; use TIME(0) or store as NVARCHAR(8)

CREATE TABLE [${schema}].[positions] (
    [id]             UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]      UNIQUEIDENTIFIER NOT NULL,
    [company_id]     UNIQUEIDENTIFIER NOT NULL,
    [code]           NVARCHAR(50)     NOT NULL,
    [title]          NVARCHAR(255)    NOT NULL,
    [department_id]  NVARCHAR(36)     NULL,
    [position_level] NVARCHAR(20)     NULL,
    [salary_grade]   NVARCHAR(50)     NULL,
    [headcount]      INT              NOT NULL DEFAULT 1,
    [filled_count]   INT              NOT NULL DEFAULT 0,
    [status]         NVARCHAR(20)     NOT NULL DEFAULT 'ACTIVE',
    CONSTRAINT [PK_positions]   PRIMARY KEY ([id]),
    CONSTRAINT [FK_pos_company] FOREIGN KEY ([company_id]) REFERENCES [${schema}].[companies] ([id]),
    CONSTRAINT [UQ_pos_code]    UNIQUE ([tenant_id], [company_id], [code])
);
GO

CREATE TABLE [${schema}].[hr_employees] (
    [id]                   UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]            UNIQUEIDENTIFIER NOT NULL,
    [company_id]           UNIQUEIDENTIFIER NOT NULL,
    [employee_number]      NVARCHAR(50)     NOT NULL,
    [user_id]              NVARCHAR(36)     NULL,
    [first_name]           NVARCHAR(100)    NOT NULL,
    [last_name]            NVARCHAR(100)    NOT NULL,
    [date_of_birth]        DATE             NULL,
    [national_id]          NVARCHAR(50)     NULL,
    [gender]               NVARCHAR(10)     NULL,
    [nationality]          NVARCHAR(10)     NULL,
    [personal_email]       NVARCHAR(255)    NULL,
    [work_email]           NVARCHAR(255)    NULL,
    [phone]                NVARCHAR(50)     NULL,
    [address_line]         NVARCHAR(500)    NULL,
    [city]                 NVARCHAR(100)    NULL,
    [contact_country_code] NVARCHAR(10)     NULL,
    [position_id]          NVARCHAR(36)     NULL,
    [department_id]        NVARCHAR(36)     NULL,
    [manager_id]           NVARCHAR(36)     NULL,
    [hire_date]            DATE             NOT NULL,
    [termination_date]     DATE             NULL,
    [employment_type]      NVARCHAR(20)     NOT NULL,
    [status]               NVARCHAR(20)     NOT NULL DEFAULT 'ACTIVE',
    [country_code]         NVARCHAR(10)     NULL,
    CONSTRAINT [PK_hr_employees]   PRIMARY KEY ([id]),
    CONSTRAINT [FK_hre_company]    FOREIGN KEY ([company_id]) REFERENCES [${schema}].[companies] ([id]),
    CONSTRAINT [UQ_hre_emp_number] UNIQUE ([tenant_id], [employee_number])
);
GO

CREATE TABLE [${schema}].[contracts] (
    [id]                UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]         UNIQUEIDENTIFIER NOT NULL,
    [company_id]        UNIQUEIDENTIFIER NOT NULL,
    [contract_number]   NVARCHAR(50)     NOT NULL,
    [employee_id]       NVARCHAR(36)     NOT NULL,
    [start_date]        DATE             NOT NULL,
    [end_date]          DATE             NULL,
    [contract_type]     NVARCHAR(20)     NOT NULL,
    [gross_salary]      DECIMAL(15, 2)   NOT NULL,
    [currency_code]     NVARCHAR(10)     NULL,
    [payroll_config_id] NVARCHAR(36)     NULL,
    [status]            NVARCHAR(20)     NOT NULL DEFAULT 'DRAFT',
    [components_json]   NVARCHAR(MAX)    NULL,
    CONSTRAINT [PK_contracts]   PRIMARY KEY ([id]),
    CONSTRAINT [FK_con_company] FOREIGN KEY ([company_id]) REFERENCES [${schema}].[companies] ([id]),
    CONSTRAINT [UQ_con_number]  UNIQUE ([tenant_id], [contract_number])
);
GO

CREATE TABLE [${schema}].[leave_policies] (
    [id]               UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]        UNIQUEIDENTIFIER NOT NULL,
    [company_id]       UNIQUEIDENTIFIER NOT NULL,
    [name]             NVARCHAR(255)    NOT NULL,
    [country_code]     NVARCHAR(10)     NULL,
    [leave_types_json] NVARCHAR(MAX)    NULL,
    [active]           BIT              NOT NULL DEFAULT 1,
    CONSTRAINT [PK_leave_policies] PRIMARY KEY ([id]),
    CONSTRAINT [FK_lp_company]     FOREIGN KEY ([company_id]) REFERENCES [${schema}].[companies] ([id])
);
GO

CREATE TABLE [${schema}].[leave_requests] (
    [id]               UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]        UNIQUEIDENTIFIER NOT NULL,
    [company_id]       UNIQUEIDENTIFIER NOT NULL,
    [request_number]   NVARCHAR(50)     NOT NULL,
    [employee_id]      NVARCHAR(36)     NOT NULL,
    [approver_id]      NVARCHAR(36)     NULL,
    [leave_type]       NVARCHAR(20)     NOT NULL,
    [start_date]       DATE             NOT NULL,
    [end_date]         DATE             NOT NULL,
    [requested_days]   INT              NOT NULL,
    [status]           NVARCHAR(20)     NOT NULL DEFAULT 'DRAFT',
    [reason]           NVARCHAR(MAX)    NULL,
    [document_ref]     NVARCHAR(255)    NULL,
    [rejection_reason] NVARCHAR(MAX)    NULL,
    CONSTRAINT [PK_leave_requests] PRIMARY KEY ([id]),
    CONSTRAINT [FK_lr_company]     FOREIGN KEY ([company_id]) REFERENCES [${schema}].[companies] ([id]),
    CONSTRAINT [UQ_lr_number]      UNIQUE ([tenant_id], [request_number])
);
GO

CREATE TABLE [${schema}].[leave_balances] (
    [id]               UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]        UNIQUEIDENTIFIER NOT NULL,
    [company_id]       UNIQUEIDENTIFIER NOT NULL,
    [employee_id]      NVARCHAR(36)     NOT NULL,
    [leave_type]       NVARCHAR(20)     NOT NULL,
    [year]             INT              NOT NULL,
    [entitlement_days] INT              NOT NULL DEFAULT 0,
    [used_days]        INT              NOT NULL DEFAULT 0,
    [carryover_days]   INT              NOT NULL DEFAULT 0,
    [pending_days]     INT              NOT NULL DEFAULT 0,
    CONSTRAINT [PK_leave_balances]    PRIMARY KEY ([id]),
    CONSTRAINT [FK_lb_company]        FOREIGN KEY ([company_id]) REFERENCES [${schema}].[companies] ([id]),
    CONSTRAINT [UQ_lb_emp_type_year]  UNIQUE ([tenant_id], [employee_id], [leave_type], [year])
);
GO

CREATE TABLE [${schema}].[attendances] (
    [id]               UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]        UNIQUEIDENTIFIER NOT NULL,
    [company_id]       UNIQUEIDENTIFIER NOT NULL,
    [employee_id]      NVARCHAR(36)     NOT NULL,
    [date]             DATE             NOT NULL,
    [source]           NVARCHAR(10)     NOT NULL,
    [check_in]         TIME(0)          NULL,
    [check_out]        TIME(0)          NULL,
    [status]           NVARCHAR(20)     NOT NULL DEFAULT 'PRESENT',
    [regular_minutes]  INT              NOT NULL DEFAULT 0,
    [overtime_minutes] INT              NOT NULL DEFAULT 0,
    [device_id]        NVARCHAR(100)    NULL,
    [notes]            NVARCHAR(MAX)    NULL,
    CONSTRAINT [PK_attendances]      PRIMARY KEY ([id]),
    CONSTRAINT [FK_att_company]      FOREIGN KEY ([company_id]) REFERENCES [${schema}].[companies] ([id]),
    CONSTRAINT [UQ_att_emp_date]     UNIQUE ([tenant_id], [employee_id], [date])
);
GO

CREATE TABLE [${schema}].[payroll_configs] (
    [id]                 UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]          UNIQUEIDENTIFIER NOT NULL,
    [company_id]         UNIQUEIDENTIFIER NOT NULL,
    [country_code]       NVARCHAR(10)     NULL,
    [name]               NVARCHAR(255)    NOT NULL,
    [valid_year]         INT              NOT NULL,
    [minimum_wage]       DECIMAL(15, 2)   NULL,
    [currency_code]      NVARCHAR(10)     NULL,
    [tax_brackets_json]  NVARCHAR(MAX)    NULL,
    [deductions_json]    NVARCHAR(MAX)    NULL,
    [active]             BIT              NOT NULL DEFAULT 1,
    CONSTRAINT [PK_payroll_configs] PRIMARY KEY ([id]),
    CONSTRAINT [FK_pc_company]      FOREIGN KEY ([company_id]) REFERENCES [${schema}].[companies] ([id])
);
GO

CREATE TABLE [${schema}].[payroll_runs] (
    [id]                UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]         UNIQUEIDENTIFIER NOT NULL,
    [company_id]        UNIQUEIDENTIFIER NOT NULL,
    [run_number]        NVARCHAR(50)     NOT NULL,
    [year]              INT              NOT NULL,
    [month]             INT              NOT NULL,
    [status]            NVARCHAR(20)     NOT NULL DEFAULT 'DRAFT',
    [payment_date]      DATE             NULL,
    [payroll_config_id] NVARCHAR(36)     NULL,
    [entries_json]      NVARCHAR(MAX)    NULL,
    CONSTRAINT [PK_payroll_runs]  PRIMARY KEY ([id]),
    CONSTRAINT [FK_prn_company]   FOREIGN KEY ([company_id]) REFERENCES [${schema}].[companies] ([id]),
    CONSTRAINT [UQ_prn_number]    UNIQUE ([tenant_id], [run_number])
);
GO

CREATE TABLE [${schema}].[performance_cycles] (
    [id]              UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]       UNIQUEIDENTIFIER NOT NULL,
    [company_id]      UNIQUEIDENTIFIER NOT NULL,
    [name]            NVARCHAR(255)    NOT NULL,
    [start_date]      DATE             NOT NULL,
    [end_date]        DATE             NOT NULL,
    [review_deadline] DATE             NULL,
    [status]          NVARCHAR(20)     NOT NULL DEFAULT 'PLANNED',
    [goals_json]      NVARCHAR(MAX)    NULL,
    CONSTRAINT [PK_performance_cycles] PRIMARY KEY ([id]),
    CONSTRAINT [FK_pcy_company]        FOREIGN KEY ([company_id]) REFERENCES [${schema}].[companies] ([id])
);
GO

CREATE TABLE [${schema}].[performance_reviews] (
    [id]                UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]         UNIQUEIDENTIFIER NOT NULL,
    [company_id]        UNIQUEIDENTIFIER NOT NULL,
    [cycle_id]          NVARCHAR(36)     NOT NULL,
    [employee_id]       NVARCHAR(36)     NOT NULL,
    [reviewer_id]       NVARCHAR(36)     NULL,
    [status]            NVARCHAR(20)     NOT NULL DEFAULT 'PENDING',
    [overall_rating]    INT              NULL DEFAULT 0,
    [strengths]         NVARCHAR(MAX)    NULL,
    [improvements]      NVARCHAR(MAX)    NULL,
    [comments]          NVARCHAR(MAX)    NULL,
    [goal_reviews_json] NVARCHAR(MAX)    NULL,
    CONSTRAINT [PK_performance_reviews]   PRIMARY KEY ([id]),
    CONSTRAINT [FK_prv_company]           FOREIGN KEY ([company_id]) REFERENCES [${schema}].[companies] ([id]),
    CONSTRAINT [UQ_prv_cycle_employee]    UNIQUE ([tenant_id], [cycle_id], [employee_id])
);
GO

CREATE TABLE [${schema}].[job_postings] (
    [id]                UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]         UNIQUEIDENTIFIER NOT NULL,
    [company_id]        UNIQUEIDENTIFIER NOT NULL,
    [posting_number]    NVARCHAR(50)     NOT NULL,
    [position_id]       NVARCHAR(36)     NOT NULL,
    [title]             NVARCHAR(255)    NOT NULL,
    [description]       NVARCHAR(MAX)    NULL,
    [requirements_json] NVARCHAR(MAX)    NULL,
    [employment_type]   NVARCHAR(20)     NOT NULL,
    [status]            NVARCHAR(20)     NOT NULL DEFAULT 'DRAFT',
    [published_at]      DATE             NULL,
    [closing_date]      DATE             NULL,
    CONSTRAINT [PK_job_postings]    PRIMARY KEY ([id]),
    CONSTRAINT [FK_jp_company]      FOREIGN KEY ([company_id]) REFERENCES [${schema}].[companies] ([id]),
    CONSTRAINT [UQ_jp_post_number]  UNIQUE ([tenant_id], [posting_number])
);
GO

CREATE TABLE [${schema}].[job_applications] (
    [id]                 UNIQUEIDENTIFIER NOT NULL,
    [tenant_id]          UNIQUEIDENTIFIER NOT NULL,
    [company_id]         UNIQUEIDENTIFIER NOT NULL,
    [job_posting_id]     NVARCHAR(36)     NOT NULL,
    [first_name]         NVARCHAR(100)    NOT NULL,
    [last_name]          NVARCHAR(100)    NOT NULL,
    [email]              NVARCHAR(255)    NOT NULL,
    [phone]              NVARCHAR(50)     NULL,
    [cv_ref]             NVARCHAR(500)    NULL,
    [status]             NVARCHAR(20)     NOT NULL DEFAULT 'APPLIED',
    [current_stage_note] NVARCHAR(MAX)    NULL,
    [interviews_json]    NVARCHAR(MAX)    NULL,
    [applied_at]         DATE             NOT NULL,
    CONSTRAINT [PK_job_applications] PRIMARY KEY ([id]),
    CONSTRAINT [FK_ja_company]       FOREIGN KEY ([company_id]) REFERENCES [${schema}].[companies] ([id])
);
GO

-- ─── Indexes ───
CREATE INDEX [idx_positions_tenant_company]         ON [${schema}].[positions] ([tenant_id],[company_id]);
CREATE INDEX [idx_employees_tenant_company]         ON [${schema}].[hr_employees] ([tenant_id],[company_id]);
CREATE INDEX [idx_contracts_tenant_company]         ON [${schema}].[contracts] ([tenant_id],[company_id]);
CREATE INDEX [idx_leave_policies_tenant_company]    ON [${schema}].[leave_policies] ([tenant_id],[company_id]);
CREATE INDEX [idx_leave_requests_tenant_company]    ON [${schema}].[leave_requests] ([tenant_id],[company_id]);
CREATE INDEX [idx_leave_balances_tenant_company]    ON [${schema}].[leave_balances] ([tenant_id],[company_id]);
CREATE INDEX [idx_attendances_tenant_company]       ON [${schema}].[attendances] ([tenant_id],[company_id]);
CREATE INDEX [idx_payroll_configs_tenant_company]   ON [${schema}].[payroll_configs] ([tenant_id],[company_id]);
CREATE INDEX [idx_payroll_runs_tenant_company]      ON [${schema}].[payroll_runs] ([tenant_id],[company_id]);
CREATE INDEX [idx_perf_cycles_tenant_company]       ON [${schema}].[performance_cycles] ([tenant_id],[company_id]);
CREATE INDEX [idx_perf_reviews_tenant_company]      ON [${schema}].[performance_reviews] ([tenant_id],[company_id]);
CREATE INDEX [idx_job_postings_tenant_company]      ON [${schema}].[job_postings] ([tenant_id],[company_id]);
CREATE INDEX [idx_job_applications_tenant_company]  ON [${schema}].[job_applications] ([tenant_id],[company_id]);
GO

-- ─── HR Permissions ───
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00E0-0000-0000-000000000001','POSITION','CREATE','Create a position');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00E0-0000-0000-000000000002','POSITION','READ',  'View positions');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00E0-0000-0000-000000000003','POSITION','UPDATE','Update positions');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00E0-0000-0000-000000000004','POSITION','DELETE','Delete positions');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00E1-0000-0000-000000000001','HR_EMPLOYEE','CREATE','Create an HR employee');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00E1-0000-0000-000000000002','HR_EMPLOYEE','READ',  'View HR employees');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00E1-0000-0000-000000000003','HR_EMPLOYEE','UPDATE','Update HR employees');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00E1-0000-0000-000000000004','HR_EMPLOYEE','DELETE','Delete HR employees');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00E2-0000-0000-000000000001','CONTRACT','CREATE','Create a contract');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00E2-0000-0000-000000000002','CONTRACT','READ',  'View contracts');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00E2-0000-0000-000000000003','CONTRACT','UPDATE','Update contracts');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00E2-0000-0000-000000000004','CONTRACT','DELETE','Delete contracts');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00E3-0000-0000-000000000001','LEAVE_POLICY','CREATE','Create a leave policy');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00E3-0000-0000-000000000002','LEAVE_POLICY','READ',  'View leave policies');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00E3-0000-0000-000000000003','LEAVE_POLICY','UPDATE','Update leave policies');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00E3-0000-0000-000000000004','LEAVE_POLICY','DELETE','Delete leave policies');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00E4-0000-0000-000000000001','LEAVE_REQUEST','CREATE','Create a leave request');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00E4-0000-0000-000000000002','LEAVE_REQUEST','READ',  'View leave requests');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00E4-0000-0000-000000000003','LEAVE_REQUEST','UPDATE','Update leave requests');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00E4-0000-0000-000000000004','LEAVE_REQUEST','DELETE','Delete leave requests');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00E5-0000-0000-000000000001','LEAVE_BALANCE','CREATE','Create a leave balance');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00E5-0000-0000-000000000002','LEAVE_BALANCE','READ',  'View leave balances');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00E5-0000-0000-000000000003','LEAVE_BALANCE','UPDATE','Update leave balances');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00E5-0000-0000-000000000004','LEAVE_BALANCE','DELETE','Delete leave balances');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00E6-0000-0000-000000000001','ATTENDANCE','CREATE','Create attendance record');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00E6-0000-0000-000000000002','ATTENDANCE','READ',  'View attendance records');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00E6-0000-0000-000000000003','ATTENDANCE','UPDATE','Update attendance records');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00E6-0000-0000-000000000004','ATTENDANCE','DELETE','Delete attendance records');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00E7-0000-0000-000000000001','PAYROLL_CONFIG','CREATE','Create payroll config');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00E7-0000-0000-000000000002','PAYROLL_CONFIG','READ',  'View payroll configs');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00E7-0000-0000-000000000003','PAYROLL_CONFIG','UPDATE','Update payroll configs');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00E7-0000-0000-000000000004','PAYROLL_CONFIG','DELETE','Delete payroll configs');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00E8-0000-0000-000000000001','PAYROLL_RUN','CREATE','Create a payroll run');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00E8-0000-0000-000000000002','PAYROLL_RUN','READ',  'View payroll runs');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00E8-0000-0000-000000000003','PAYROLL_RUN','UPDATE','Update payroll runs');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00E8-0000-0000-000000000004','PAYROLL_RUN','DELETE','Delete payroll runs');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00E9-0000-0000-000000000001','PERFORMANCE_CYCLE','CREATE','Create performance cycle');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00E9-0000-0000-000000000002','PERFORMANCE_CYCLE','READ',  'View performance cycles');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00E9-0000-0000-000000000003','PERFORMANCE_CYCLE','UPDATE','Update performance cycles');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00E9-0000-0000-000000000004','PERFORMANCE_CYCLE','DELETE','Delete performance cycles');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00EA-0000-0000-000000000001','PERFORMANCE_REVIEW','CREATE','Create performance review');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00EA-0000-0000-000000000002','PERFORMANCE_REVIEW','READ',  'View performance reviews');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00EA-0000-0000-000000000003','PERFORMANCE_REVIEW','UPDATE','Update performance reviews');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00EA-0000-0000-000000000004','PERFORMANCE_REVIEW','DELETE','Delete performance reviews');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00EB-0000-0000-000000000001','JOB_POSTING','CREATE','Create a job posting');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00EB-0000-0000-000000000002','JOB_POSTING','READ',  'View job postings');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00EB-0000-0000-000000000003','JOB_POSTING','UPDATE','Update job postings');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00EB-0000-0000-000000000004','JOB_POSTING','DELETE','Delete job postings');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00EC-0000-0000-000000000001','JOB_APPLICATION','CREATE','Create a job application');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00EC-0000-0000-000000000002','JOB_APPLICATION','READ',  'View job applications');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00EC-0000-0000-000000000003','JOB_APPLICATION','UPDATE','Update job applications');
INSERT INTO [${schema}].[permissions]([id],[module],[action],[description]) VALUES ('00000000-00EC-0000-0000-000000000004','JOB_APPLICATION','DELETE','Delete job applications');
GO

-- ─── Role assignments ───
MERGE INTO [${schema}].[role_permissions] AS tgt
USING (SELECT CONVERT(UNIQUEIDENTIFIER,HASHBYTES('MD5','ADMIN_${tenant_id}')) AS role_id, p.[id] AS permission_id
       FROM [${schema}].[permissions] p WHERE p.[module] IN ('POSITION','HR_EMPLOYEE','CONTRACT','LEAVE_POLICY','LEAVE_REQUEST','LEAVE_BALANCE','ATTENDANCE','PAYROLL_CONFIG','PAYROLL_RUN','PERFORMANCE_CYCLE','PERFORMANCE_REVIEW','JOB_POSTING','JOB_APPLICATION')) AS src
ON tgt.[role_id]=src.[role_id] AND tgt.[permission_id]=src.[permission_id]
WHEN NOT MATCHED THEN INSERT([role_id],[permission_id]) VALUES(src.[role_id],src.[permission_id]);

MERGE INTO [${schema}].[role_permissions] AS tgt
USING (SELECT CONVERT(UNIQUEIDENTIFIER,HASHBYTES('MD5','MANAGER_${tenant_id}')) AS role_id, p.[id] AS permission_id
       FROM [${schema}].[permissions] p WHERE p.[module] IN ('POSITION','HR_EMPLOYEE','CONTRACT','LEAVE_POLICY','LEAVE_REQUEST','LEAVE_BALANCE','ATTENDANCE','PAYROLL_CONFIG','PAYROLL_RUN','PERFORMANCE_CYCLE','PERFORMANCE_REVIEW','JOB_POSTING','JOB_APPLICATION') AND p.[action] != 'DELETE') AS src
ON tgt.[role_id]=src.[role_id] AND tgt.[permission_id]=src.[permission_id]
WHEN NOT MATCHED THEN INSERT([role_id],[permission_id]) VALUES(src.[role_id],src.[permission_id]);

MERGE INTO [${schema}].[role_permissions] AS tgt
USING (SELECT CONVERT(UNIQUEIDENTIFIER,HASHBYTES('MD5','USER_${tenant_id}')) AS role_id, p.[id] AS permission_id
       FROM [${schema}].[permissions] p WHERE p.[module] IN ('POSITION','HR_EMPLOYEE','CONTRACT','LEAVE_POLICY','LEAVE_REQUEST','LEAVE_BALANCE','ATTENDANCE','PAYROLL_CONFIG','PAYROLL_RUN','PERFORMANCE_CYCLE','PERFORMANCE_REVIEW','JOB_POSTING','JOB_APPLICATION') AND p.[action] IN ('CREATE','READ','UPDATE')) AS src
ON tgt.[role_id]=src.[role_id] AND tgt.[permission_id]=src.[permission_id]
WHEN NOT MATCHED THEN INSERT([role_id],[permission_id]) VALUES(src.[role_id],src.[permission_id]);

MERGE INTO [${schema}].[role_permissions] AS tgt
USING (SELECT CONVERT(UNIQUEIDENTIFIER,HASHBYTES('MD5','VIEWER_${tenant_id}')) AS role_id, p.[id] AS permission_id
       FROM [${schema}].[permissions] p WHERE p.[module] IN ('POSITION','HR_EMPLOYEE','CONTRACT','LEAVE_POLICY','LEAVE_REQUEST','LEAVE_BALANCE','ATTENDANCE','PAYROLL_CONFIG','PAYROLL_RUN','PERFORMANCE_CYCLE','PERFORMANCE_REVIEW','JOB_POSTING','JOB_APPLICATION') AND p.[action] = 'READ') AS src
ON tgt.[role_id]=src.[role_id] AND tgt.[permission_id]=src.[permission_id]
WHEN NOT MATCHED THEN INSERT([role_id],[permission_id]) VALUES(src.[role_id],src.[permission_id]);
GO
