package tr.kontas.erp.core.platform.multitenancy;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

@Component
public class TenantContext implements CurrentTenantIdentifierResolver<String> {

    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

    public static void setTenantIdentifier(String tenantId) {
        CURRENT_TENANT.set(tenantId);
    }

    public static void clear() {
        CURRENT_TENANT.remove();
    }

    public static TenantId get() {
        String value = CURRENT_TENANT.get();
        if (value == null) {
            throw new IllegalStateException("TenantId not set in current context");
        }
        return TenantId.of(value);
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