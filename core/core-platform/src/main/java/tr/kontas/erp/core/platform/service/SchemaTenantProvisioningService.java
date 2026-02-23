package tr.kontas.erp.core.platform.service;

import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.domain.service.TenantProvisioningService;
import tr.kontas.erp.core.domain.tenant.Tenant;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SchemaTenantProvisioningService implements TenantProvisioningService {

    private final DataSource dataSource;

    @Value("${erp.datasource.type}")
    private String dbType;

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Override
    public void provision(Tenant tenant) {
        String schema = schemaName(tenant);

        if ("oracle".equalsIgnoreCase(dbType)) {
            provisionOracle(tenant, schema);
        } else {
            provisionStandard(tenant, schema);
        }
    }

    // PostgreSQL, MySQL, MSSQL — schema
    private void provisionStandard(Tenant tenant, String schema) {
        Flyway tenantFlyway = Flyway.configure()
                .dataSource(dataSource)
                .schemas(schema)
                .defaultSchema(schema)
                .locations("classpath:migration/" + dbType + "/tenant")
                .placeholders(buildPlaceholders(tenant, schema))
                .baselineOnMigrate(false)
                .load();

        tenantFlyway.migrate();
    }

    private void provisionOracle(Tenant tenant, String schema) {
        schema = schema.toUpperCase();

        try (Connection conn = dataSource.getConnection();
             CallableStatement stmt = conn.prepareCall(buildOracleProvisionSQL(schema))) {
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Oracle tenant provisioning failed for schema: " + schema, e);
        }

        Flyway tenantFlyway = Flyway.configure()
                .dataSource(datasourceUrl, schema, schema)
                .defaultSchema(schema)
                .locations("classpath:migration/" + dbType + "/tenant")
                .placeholders(buildPlaceholders(tenant, schema))
                .baselineOnMigrate(false)
                .load();

        tenantFlyway.migrate();
    }

    private String buildOracleProvisionSQL(String schema) {
        return """
        DECLARE
            v_count NUMBER;
        BEGIN
            SELECT COUNT(*) INTO v_count FROM all_users WHERE username = UPPER('%s');
            IF v_count = 0 THEN
                EXECUTE IMMEDIATE 'CREATE USER %s IDENTIFIED BY %s';
                EXECUTE IMMEDIATE 'GRANT CREATE SESSION, CREATE TABLE, CREATE VIEW, CREATE SEQUENCE, CREATE PROCEDURE TO %s';
                EXECUTE IMMEDIATE 'ALTER USER %s QUOTA UNLIMITED ON USERS';
                EXECUTE IMMEDIATE 'GRANT DBA TO %s';
            END IF;
        END;
        """.formatted(schema, schema, schema, schema, schema, schema);
    }

    private Map<String, String> buildPlaceholders(Tenant tenant, String schema) {
        return Map.of(
                "tenant_id", tenant.getId().asUUID().toString(),
                "schema", schema
        );
    }

    private String schemaName(Tenant tenant) {
        int hash = Math.abs(tenant.getId().asUUID().toString().replace("-", "").hashCode());
        return "tenant_" + hash;
    }
}