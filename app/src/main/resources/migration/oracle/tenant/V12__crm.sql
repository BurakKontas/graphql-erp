-- V12__crm.sql (Main Application)
-- Oracle conversion: UUID->VARCHAR2(32), TEXT->CLOB, VARCHAR->VARCHAR2,
--                    NUMERIC->NUMBER, TIMESTAMP->TIMESTAMP

CREATE TABLE "${schema}".crm_contacts (
    id                  VARCHAR2(32)       NOT NULL,
    tenant_id           VARCHAR2(32)       NOT NULL,
    company_id          VARCHAR2(32)       NOT NULL,
    contact_number      VARCHAR2(50)  NOT NULL,
    contact_type        VARCHAR2(20)  NOT NULL,
    first_name          VARCHAR2(255),
    last_name           VARCHAR2(255),
    company_name        VARCHAR2(255),
    job_title           VARCHAR2(255),
    email               VARCHAR2(255),
    phone               VARCHAR2(100),
    website             VARCHAR2(255),
    address             CLOB,
    business_partner_id VARCHAR2(255),
    owner_id            VARCHAR2(255),
    status              VARCHAR2(20)  NOT NULL,
    source              VARCHAR2(30),
    notes               CLOB,
    CONSTRAINT pk_crm_contacts PRIMARY KEY (id)
);

CREATE TABLE "${schema}".crm_leads (
    id                       VARCHAR2(32)        NOT NULL,
    tenant_id                VARCHAR2(32)        NOT NULL,
    company_id               VARCHAR2(32)        NOT NULL,
    lead_number              VARCHAR2(50)   NOT NULL,
    title                    VARCHAR2(500)  NOT NULL,
    contact_id               VARCHAR2(255),
    owner_id                 VARCHAR2(255),
    status                   VARCHAR2(30)   NOT NULL,
    source                   VARCHAR2(30),
    estimated_value          NUMBER(19,2),
    disqualification_reason  CLOB,
    notes                    CLOB,
    expected_close_date      DATE,
    opportunity_id           VARCHAR2(255),
    CONSTRAINT pk_crm_leads PRIMARY KEY (id)
);

CREATE TABLE "${schema}".crm_opportunities (
    id                  VARCHAR2(32)        NOT NULL,
    tenant_id           VARCHAR2(32)        NOT NULL,
    company_id          VARCHAR2(32)        NOT NULL,
    opportunity_number  VARCHAR2(50)   NOT NULL,
    title               VARCHAR2(500)  NOT NULL,
    contact_id          VARCHAR2(255),
    lead_id             VARCHAR2(255),
    owner_id            VARCHAR2(255),
    stage               VARCHAR2(30)   NOT NULL,
    probability         NUMBER(5,2),
    expected_value      NUMBER(19,2),
    currency_code       VARCHAR2(10),
    expected_close_date DATE,
    sales_order_id      VARCHAR2(255),
    lost_reason         CLOB,
    notes               CLOB,
    CONSTRAINT pk_crm_opportunities PRIMARY KEY (id)
);

CREATE TABLE "${schema}".crm_quotes (
    id                VARCHAR2(32)        NOT NULL,
    tenant_id         VARCHAR2(32)        NOT NULL,
    company_id        VARCHAR2(32)        NOT NULL,
    quote_number      VARCHAR2(50)   NOT NULL,
    opportunity_id    VARCHAR2(255),
    contact_id        VARCHAR2(255),
    owner_id          VARCHAR2(255),
    quote_date        DATE,
    expiry_date       DATE,
    currency_code     VARCHAR2(10),
    payment_term_code VARCHAR2(50),
    status            VARCHAR2(20)   NOT NULL,
    version           VARCHAR2(10),
    previous_quote_id VARCHAR2(255),
    subtotal          NUMBER(19,2),
    tax_total         NUMBER(19,2),
    total             NUMBER(19,2),
    discount_rate     NUMBER(5,2),
    notes             CLOB,
    lines_json        CLOB,
    CONSTRAINT pk_crm_quotes PRIMARY KEY (id)
);

CREATE TABLE "${schema}".crm_activities (
    id                      VARCHAR2(32)       NOT NULL,
    tenant_id               VARCHAR2(32)       NOT NULL,
    company_id              VARCHAR2(32)       NOT NULL,
    activity_type           VARCHAR2(20)  NOT NULL,
    subject                 VARCHAR2(500) NOT NULL,
    owner_id                VARCHAR2(255),
    status                  VARCHAR2(20)  NOT NULL,
    related_entity_type     VARCHAR2(30),
    related_entity_id       VARCHAR2(255),
    scheduled_at            TIMESTAMP,
    completed_at            TIMESTAMP,
    duration_minutes        NUMBER(10)    DEFAULT 0,
    description             CLOB,
    outcome                 CLOB,
    follow_up_type          VARCHAR2(20),
    follow_up_scheduled_at  TIMESTAMP,
    follow_up_note          CLOB,
    CONSTRAINT pk_crm_activities PRIMARY KEY (id)
);

CREATE INDEX idx_crm_contacts_tenant_company      ON "${schema}".crm_contacts (tenant_id, company_id);
CREATE INDEX idx_crm_leads_tenant_company         ON "${schema}".crm_leads (tenant_id, company_id);
CREATE INDEX idx_crm_opportunities_tenant_company ON "${schema}".crm_opportunities (tenant_id, company_id);
CREATE INDEX idx_crm_quotes_tenant_company        ON "${schema}".crm_quotes (tenant_id, company_id);
CREATE INDEX idx_crm_activities_tenant_company    ON "${schema}".crm_activities (tenant_id, company_id);
CREATE INDEX idx_crm_activities_related           ON "${schema}".crm_activities (related_entity_type, related_entity_id);

-- ─── CRM Permissions ───
MERGE INTO "${schema}".permissions tgt
    USING (

        SELECT HEXTORAW('0000000000000000000C000000000001') id,'CONTACT' module,'CREATE' action,'Create a CRM contact' description FROM dual UNION ALL
        SELECT HEXTORAW('0000000000000000000C000000000002'),'CONTACT','READ','View CRM contacts' FROM dual UNION ALL
        SELECT HEXTORAW('0000000000000000000C000000000003'),'CONTACT','UPDATE','Update CRM contacts' FROM dual UNION ALL
        SELECT HEXTORAW('0000000000000000000C000000000004'),'CONTACT','DELETE','Delete CRM contacts' FROM dual UNION ALL

        SELECT HEXTORAW('0000000000000000000C010000000001'),'LEAD','CREATE','Create a lead' FROM dual UNION ALL
        SELECT HEXTORAW('0000000000000000000C010000000002'),'LEAD','READ','View leads' FROM dual UNION ALL
        SELECT HEXTORAW('0000000000000000000C010000000003'),'LEAD','UPDATE','Update leads' FROM dual UNION ALL
        SELECT HEXTORAW('0000000000000000000C010000000004'),'LEAD','DELETE','Delete leads' FROM dual UNION ALL

        SELECT HEXTORAW('0000000000000000000C020000000001'),'OPPORTUNITY','CREATE','Create an opportunity' FROM dual UNION ALL
        SELECT HEXTORAW('0000000000000000000C020000000002'),'OPPORTUNITY','READ','View opportunities' FROM dual UNION ALL
        SELECT HEXTORAW('0000000000000000000C020000000003'),'OPPORTUNITY','UPDATE','Update opportunities' FROM dual UNION ALL
        SELECT HEXTORAW('0000000000000000000C020000000004'),'OPPORTUNITY','DELETE','Delete opportunities' FROM dual UNION ALL

        SELECT HEXTORAW('0000000000000000000C030000000001'),'QUOTE','CREATE','Create a quote' FROM dual UNION ALL
        SELECT HEXTORAW('0000000000000000000C030000000002'),'QUOTE','READ','View quotes' FROM dual UNION ALL
        SELECT HEXTORAW('0000000000000000000C030000000003'),'QUOTE','UPDATE','Update quotes' FROM dual UNION ALL
        SELECT HEXTORAW('0000000000000000000C030000000004'),'QUOTE','DELETE','Delete quotes' FROM dual UNION ALL

        SELECT HEXTORAW('0000000000000000000C040000000001'),'ACTIVITY','CREATE','Create an activity' FROM dual UNION ALL
        SELECT HEXTORAW('0000000000000000000C040000000002'),'ACTIVITY','READ','View activities' FROM dual UNION ALL
        SELECT HEXTORAW('0000000000000000000C040000000003'),'ACTIVITY','UPDATE','Update activities' FROM dual UNION ALL
        SELECT HEXTORAW('0000000000000000000C040000000004'),'ACTIVITY','DELETE','Delete activities' FROM dual

    ) src
    ON (tgt.id = src.id)
    WHEN NOT MATCHED THEN
        INSERT (id,module,action,description)
            VALUES (src.id,src.module,src.action,src.description);

-- ─── Role assignments ───
MERGE INTO "${schema}".role_permissions tgt
USING (SELECT STANDARD_HASH('ADMIN_${tenant_id}','MD5') AS role_id, p.id AS permission_id
       FROM "${schema}".permissions p
       WHERE p.module IN ('CONTACT','LEAD','OPPORTUNITY','QUOTE','ACTIVITY')) src
ON (tgt.role_id = src.role_id AND tgt.permission_id = src.permission_id)
WHEN NOT MATCHED THEN INSERT (role_id, permission_id) VALUES (src.role_id, src.permission_id);

MERGE INTO "${schema}".role_permissions tgt
USING (SELECT STANDARD_HASH('MANAGER_${tenant_id}','MD5') AS role_id, p.id AS permission_id
       FROM "${schema}".permissions p
       WHERE p.module IN ('CONTACT','LEAD','OPPORTUNITY','QUOTE','ACTIVITY')
         AND p.action != 'DELETE') src
ON (tgt.role_id = src.role_id AND tgt.permission_id = src.permission_id)
WHEN NOT MATCHED THEN INSERT (role_id, permission_id) VALUES (src.role_id, src.permission_id);

MERGE INTO "${schema}".role_permissions tgt
USING (SELECT STANDARD_HASH('USER_${tenant_id}','MD5') AS role_id, p.id AS permission_id
       FROM "${schema}".permissions p
       WHERE p.module IN ('CONTACT','LEAD','OPPORTUNITY','QUOTE','ACTIVITY')
         AND p.action IN ('CREATE','READ','UPDATE')) src
ON (tgt.role_id = src.role_id AND tgt.permission_id = src.permission_id)
WHEN NOT MATCHED THEN INSERT (role_id, permission_id) VALUES (src.role_id, src.permission_id);

MERGE INTO "${schema}".role_permissions tgt
USING (SELECT STANDARD_HASH('VIEWER_${tenant_id}','MD5') AS role_id, p.id AS permission_id
       FROM "${schema}".permissions p
       WHERE p.module IN ('CONTACT','LEAD','OPPORTUNITY','QUOTE','ACTIVITY')
         AND p.action = 'READ') src
ON (tgt.role_id = src.role_id AND tgt.permission_id = src.permission_id)
WHEN NOT MATCHED THEN INSERT (role_id, permission_id) VALUES (src.role_id, src.permission_id);

COMMIT;
