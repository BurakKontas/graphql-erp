package tr.kontas.erp.core.platform.multitenancy;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

@Component
public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver<String> {

    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

    public static void setTenantIdentifier(String tenantId) {
        CURRENT_TENANT.set(tenantId);
    }

    public static void clear() {
        CURRENT_TENANT.remove();
    }

    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenantId = CURRENT_TENANT.get();
        return tenantId != null ? "tenant_" + tenantId.replace("-", "") : "public";
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}