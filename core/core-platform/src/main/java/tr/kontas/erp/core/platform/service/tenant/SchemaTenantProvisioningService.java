package tr.kontas.erp.core.platform.service.tenant;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import tr.kontas.erp.core.domain.service.TenantProvisioningService;
import tr.kontas.erp.core.domain.tenant.Tenant;

import org.flywaydb.core.Flyway;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
@RequiredArgsConstructor
public class SchemaTenantProvisioningService implements TenantProvisioningService {

    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    @Override
    public void provision(Tenant tenant) {

        String schema = schemaName(tenant);

        jdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS " + schema);

        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .schemas(schema)
                .defaultSchema(schema)
                .locations("classpath:migration/tenant")
                .baselineOnMigrate(true)
                .load();

        flyway.migrate();
    }

    private String schemaName(Tenant tenant) {
        return "tenant_" + tenant.getId().asUUID().toString().replace("-", "");
    }
}
