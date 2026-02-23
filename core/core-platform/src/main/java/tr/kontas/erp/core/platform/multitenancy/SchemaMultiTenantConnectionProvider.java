package tr.kontas.erp.core.platform.multitenancy;

import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class SchemaMultiTenantConnectionProvider implements MultiTenantConnectionProvider<String> {

    @Value("${erp.datasource.type}")
    private String dbType;

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
            statement.execute(buildSetSchemaSQL(schema));
        }

        return connection;
    }

    private String buildSetSchemaSQL(String schema) {
        return switch (dbType) {
            case "postgresql" -> "SET search_path TO " + schema;
            case "mssql"      -> "USE " + schema;
            case "mysql"      -> "USE " + schema;
            case "oracle"     -> "ALTER SESSION SET CURRENT_SCHEMA = " + schema;
            default -> throw new IllegalStateException(
                    "Unsupported database type for multi-tenancy: " + dbType
            );
        };
    }

    private String buildResetSchemaSQL() {
        return switch (dbType) {
            case "postgresql" -> "SET search_path TO " + "ERP_USR";
            case "mssql"      -> "USE " + "ERP_USR";
            case "mysql"      -> "USE " + "ERP_USR";
            case "oracle"     -> "ALTER SESSION SET CURRENT_SCHEMA = " + "ERP_USR";
            default -> throw new IllegalStateException(
                    "Unsupported database type for multi-tenancy: " + dbType
            );
        };
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection)
            throws SQLException {

        try (Statement statement = connection.createStatement()) {
            statement.execute(buildResetSchemaSQL());
        }

        releaseAnyConnection(connection);
    }

    private String resolveSchema(String tenantIdentifier) {

        if (tenantIdentifier == null || tenantIdentifier.isBlank()) {
            return "ERP_USR";
        }

        String tenant = tenantIdentifier.trim();

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