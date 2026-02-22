package tr.kontas.erp.core.platform.service;

import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.domain.service.TenantProvisioningService;
import tr.kontas.erp.core.domain.tenant.Tenant;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SchemaTenantProvisioningService implements TenantProvisioningService {

    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    @Override
    public void provision(Tenant tenant) {
        String schema = schemaName(tenant);

        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM information_schema.schemata WHERE schema_name = ?",
                new Object[]{schema},
                Integer.class
        );

        if (count == 0) {
            try (Connection conn = dataSource.getConnection()) {
                conn.setAutoCommit(true);
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute("CREATE SCHEMA " + schema);
                }
            } catch (SQLException e) {
                throw new IllegalStateException("Error occurred while creating Schema: " + schema, e);
            }
        }

        boolean schemaNew = Boolean.TRUE.equals(
                jdbcTemplate.queryForObject(
                        "SELECT COUNT(*) = 0 FROM information_schema.tables WHERE table_schema = ?",
                        Boolean.class, schema));

        // Flyway migration
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .schemas(schema)
                .defaultSchema(schema)
                .locations("classpath:migration/tenant")
                .placeholders(Map.of("tenant_id", tenant.getId().asUUID().toString()))
                .baselineOnMigrate(schemaNew)
                .load();

        flyway.migrate();
    }

    private String schemaName(Tenant tenant) {
        return "tenant_" + tenant.getId().asUUID().toString().replace("-", "");
    }
}
