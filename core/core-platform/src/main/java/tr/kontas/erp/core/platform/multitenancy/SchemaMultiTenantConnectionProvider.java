package tr.kontas.erp.core.platform.multitenancy;

import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class SchemaMultiTenantConnectionProvider implements MultiTenantConnectionProvider<String> {

    private static final String DEFAULT_SCHEMA = "public";

    private final DataSource dataSource;

    public SchemaMultiTenantConnectionProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        Connection connection = getAnyConnection();

        String schema = resolveSchema(tenantIdentifier);

        try (Statement statement = connection.createStatement()) {
            statement.execute("SET search_path TO " + schema);
        }

        return connection;
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection)
            throws SQLException {

        try (Statement statement = connection.createStatement()) {
            statement.execute("SET search_path TO " + DEFAULT_SCHEMA);
        }

        releaseAnyConnection(connection);
    }

    private String resolveSchema(Object tenantIdentifier) {
        if (tenantIdentifier == null) {
            return DEFAULT_SCHEMA;
        }

        String tenant = tenantIdentifier.toString().trim();

        if (tenant.isBlank()) {
            return DEFAULT_SCHEMA;
        }

        if (!tenant.matches("[a-zA-Z0-9_]+")) {
            throw new IllegalArgumentException("Invalid tenant identifier: " + tenant);
        }

        return tenant;
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public boolean isUnwrappableAs(Class<?> unwrapType) {
        return unwrapType.isAssignableFrom(getClass());
    }

    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        if (unwrapType.isAssignableFrom(getClass())) {
            return unwrapType.cast(this);
        }
        throw new IllegalArgumentException("Unknown unwrap type: " + unwrapType);
    }
}
