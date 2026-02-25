package tr.kontas.erp.core.platform.multitenancy;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tr.kontas.erp.core.domain.tenant.Tenant;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

@Component
public class TenantContext implements CurrentTenantIdentifierResolver<String> {

    @Value("${erp.public.schema}")
    public String publicSchema;

    private static final ThreadLocal<Tenant> CURRENT_TENANT = new ThreadLocal<>();

    public static void setTenantIdentifier(Tenant tenant) {
        CURRENT_TENANT.set(tenant);
    }

    public static void clear() {
        CURRENT_TENANT.remove();
    }

    public static Tenant get() {
        Tenant value = CURRENT_TENANT.get();
        if (value == null) {
            throw new IllegalStateException("Tenant not set in current context");
        }
        return value;
    }

    @Override
    public String resolveCurrentTenantIdentifier() {
        Tenant tenant = CURRENT_TENANT.get();
        if (tenant == null) return publicSchema;
        int hash = Math.abs(tenant.getId().asUUID().toString().replace("-", "").hashCode());
        return ("tenant_" + hash).toUpperCase();
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}