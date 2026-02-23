-- MySQL adapted version of V12__crm.sql

CREATE TABLE IF NOT EXISTS crm_contacts (
    id VARCHAR(36) PRIMARY KEY,
    tenant_id VARCHAR(36) NOT NULL,
    company_id VARCHAR(36) NOT NULL,
    contact_number VARCHAR(50) NOT NULL,
    contact_type VARCHAR(20) NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    company_name VARCHAR(255),
    job_title VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(100),
    website VARCHAR(255),
    address TEXT,
    business_partner_id VARCHAR(255),
    owner_id VARCHAR(255),
    status VARCHAR(20) NOT NULL,
    source VARCHAR(30),
    notes TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS crm_leads (
    id VARCHAR(36) PRIMARY KEY,
    tenant_id VARCHAR(36) NOT NULL,
    company_id VARCHAR(36) NOT NULL,
    lead_number VARCHAR(50) NOT NULL,
    title VARCHAR(500) NOT NULL,
    contact_id VARCHAR(255),
    owner_id VARCHAR(255),
    status VARCHAR(30) NOT NULL,
    source VARCHAR(30),
    estimated_value DECIMAL(19,2),
    disqualification_reason TEXT,
    notes TEXT,
    expected_close_date DATE,
    opportunity_id VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS crm_opportunities (
    id VARCHAR(36) PRIMARY KEY,
    tenant_id VARCHAR(36) NOT NULL,
    company_id VARCHAR(36) NOT NULL,
    opportunity_number VARCHAR(50) NOT NULL,
    title VARCHAR(500) NOT NULL,
    contact_id VARCHAR(255),
    lead_id VARCHAR(255),
    owner_id VARCHAR(255),
    stage VARCHAR(30) NOT NULL,
    probability DECIMAL(5,2),
    expected_value DECIMAL(19,2),
    currency_code VARCHAR(10),
    expected_close_date DATE,
    sales_order_id VARCHAR(255),
    lost_reason TEXT,
    notes TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS crm_quotes (
    id VARCHAR(36) PRIMARY KEY,
    tenant_id VARCHAR(36) NOT NULL,
    company_id VARCHAR(36) NOT NULL,
    quote_number VARCHAR(50) NOT NULL,
    opportunity_id VARCHAR(255),
    contact_id VARCHAR(255),
    owner_id VARCHAR(255),
    quote_date DATE,
    expiry_date DATE,
    currency_code VARCHAR(10),
    payment_term_code VARCHAR(50),
    status VARCHAR(20) NOT NULL,
    version VARCHAR(10),
    previous_quote_id VARCHAR(255),
    subtotal DECIMAL(19,2),
    tax_total DECIMAL(19,2),
    total DECIMAL(19,2),
    discount_rate DECIMAL(5,2),
    notes TEXT,
    lines_json TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS crm_activities (
    id VARCHAR(36) PRIMARY KEY,
    tenant_id VARCHAR(36) NOT NULL,
    company_id VARCHAR(36) NOT NULL,
    activity_type VARCHAR(20) NOT NULL,
    subject VARCHAR(500) NOT NULL,
    owner_id VARCHAR(255),
    status VARCHAR(20) NOT NULL,
    related_entity_type VARCHAR(30),
    related_entity_id VARCHAR(255),
    scheduled_at DATETIME(6),
    completed_at DATETIME(6),
    duration_minutes INT DEFAULT 0,
    description TEXT,
    outcome TEXT,
    follow_up_type VARCHAR(20),
    follow_up_scheduled_at DATETIME(6),
    follow_up_note TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_crm_contacts_tenant_company ON crm_contacts(tenant_id, company_id);
CREATE INDEX idx_crm_leads_tenant_company ON crm_leads(tenant_id, company_id);
CREATE INDEX idx_crm_opportunities_tenant_company ON crm_opportunities(tenant_id, company_id);
CREATE INDEX idx_crm_quotes_tenant_company ON crm_quotes(tenant_id, company_id);
CREATE INDEX idx_crm_activities_tenant_company ON crm_activities(tenant_id, company_id);
CREATE INDEX idx_crm_activities_related ON crm_activities(related_entity_type, related_entity_id);

-- Permissions
INSERT IGNORE INTO permissions (id, module, action, description) VALUES
    ('00000000-0000-0000-00F0-000000000001', 'CONTACT', 'CREATE', 'Create a CRM contact'),
    ('00000000-0000-0000-00F0-000000000002', 'CONTACT', 'READ', 'View CRM contacts'),
    ('00000000-0000-0000-00F0-000000000003', 'CONTACT', 'UPDATE', 'Update CRM contacts'),
    ('00000000-0000-0000-00F0-000000000004', 'CONTACT', 'DELETE', 'Delete CRM contacts'),
    ('00000000-0000-0000-00F1-000000000001', 'LEAD', 'CREATE', 'Create a lead'),
    ('00000000-0000-0000-00F1-000000000002', 'LEAD', 'READ', 'View leads'),
    ('00000000-0000-0000-00F1-000000000003', 'LEAD', 'UPDATE', 'Update leads'),
    ('00000000-0000-0000-00F1-000000000004', 'LEAD', 'DELETE', 'Delete leads'),
    ('00000000-0000-0000-00F2-000000000001', 'OPPORTUNITY', 'CREATE', 'Create an opportunity'),
    ('00000000-0000-0000-00F2-000000000002', 'OPPORTUNITY', 'READ', 'View opportunities'),
    ('00000000-0000-0000-00F2-000000000003', 'OPPORTUNITY', 'UPDATE', 'Update opportunities'),
    ('00000000-0000-0000-00F2-000000000004', 'OPPORTUNITY', 'DELETE', 'Delete opportunities'),
    ('00000000-0000-0000-00F3-000000000001', 'QUOTE', 'CREATE', 'Create a quote'),
    ('00000000-0000-0000-00F3-000000000002', 'QUOTE', 'READ', 'View quotes'),
    ('00000000-0000-0000-00F3-000000000003', 'QUOTE', 'UPDATE', 'Update quotes'),
    ('00000000-0000-0000-00F3-000000000004', 'QUOTE', 'DELETE', 'Delete quotes'),
    ('00000000-0000-0000-00F4-000000000001', 'ACTIVITY', 'CREATE', 'Create an activity'),
    ('00000000-0000-0000-00F4-000000000002', 'ACTIVITY', 'READ', 'View activities'),
    ('00000000-0000-0000-00F4-000000000003', 'ACTIVITY', 'UPDATE', 'Update activities'),
    ('00000000-0000-0000-00F4-000000000004', 'ACTIVITY', 'DELETE', 'Delete activities');

-- Assign role permissions
INSERT IGNORE INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM permissions p JOIN roles r ON r.tenant_id = '${tenant_id}' AND r.name = 'ADMIN'
LEFT JOIN role_permissions rp ON rp.role_id = r.id AND rp.permission_id = p.id
WHERE rp.role_id IS NULL
  AND p.module IN ('CONTACT', 'LEAD', 'OPPORTUNITY', 'QUOTE', 'ACTIVITY');

INSERT IGNORE INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM permissions p JOIN roles r ON r.tenant_id = '${tenant_id}' AND r.name = 'MANAGER'
LEFT JOIN role_permissions rp ON rp.role_id = r.id AND rp.permission_id = p.id
WHERE rp.role_id IS NULL
  AND p.module IN ('CONTACT', 'LEAD', 'OPPORTUNITY', 'QUOTE', 'ACTIVITY')
  AND p.action != 'DELETE';

INSERT IGNORE INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM permissions p JOIN roles r ON r.tenant_id = '${tenant_id}' AND r.name = 'USER'
LEFT JOIN role_permissions rp ON rp.role_id = r.id AND rp.permission_id = p.id
WHERE rp.role_id IS NULL
  AND p.module IN ('CONTACT', 'LEAD', 'OPPORTUNITY', 'QUOTE', 'ACTIVITY')
  AND p.action IN ('CREATE', 'READ', 'UPDATE');

INSERT IGNORE INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM permissions p JOIN roles r ON r.tenant_id = '${tenant_id}' AND r.name = 'VIEWER'
LEFT JOIN role_permissions rp ON rp.role_id = r.id AND rp.permission_id = p.id
WHERE rp.role_id IS NULL
  AND p.module IN ('CONTACT', 'LEAD', 'OPPORTUNITY', 'QUOTE', 'ACTIVITY')
  AND p.action = 'READ';
