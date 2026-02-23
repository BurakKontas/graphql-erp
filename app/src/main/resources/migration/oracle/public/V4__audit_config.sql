-- V4__audit_config.sql (Tenant Service)
-- Oracle: BOOLEAN -> NUMBER(1,0), IF NOT EXISTS not supported -> use exception handler

DECLARE
    col_count NUMBER;
BEGIN
    SELECT COUNT(*)
    INTO   col_count
    FROM   user_tab_columns
    WHERE  table_name  = 'TENANTS'
      AND  column_name = 'AUDIT_UNAUTHENTICATED';

    IF col_count = 0 THEN
        EXECUTE IMMEDIATE
            'ALTER TABLE "${schema}".tenants ADD audit_unauthenticated NUMBER(1,0) DEFAULT 0 NOT NULL';
    END IF;
END;
/
