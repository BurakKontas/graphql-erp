package tr.kontas.erp.core.platform.persistence.tenant;

import tr.kontas.erp.core.domain.tenant.*;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

public final class TenantMapper {

    private TenantMapper() {}

    public static TenantJpaEntity toEntity(Tenant tenant) {
        return new TenantJpaEntity(
                tenant.getId().asUUID(),
                tenant.getName().getValue(),
                tenant.getCode().getValue()
        );
    }

    public static Tenant toDomain(TenantJpaEntity entity) {
        return new Tenant(
                TenantId.of(entity.getId()),
                new TenantName(entity.getName()),
                new TenantCode(entity.getCode())
        );
    }
}