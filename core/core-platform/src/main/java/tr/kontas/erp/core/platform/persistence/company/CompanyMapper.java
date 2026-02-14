package tr.kontas.erp.core.platform.persistence.company;

import tr.kontas.erp.core.domain.company.*;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

public final class CompanyMapper {

    private CompanyMapper() {}

    public static CompanyJpaEntity toEntity(Company company) {
        return new CompanyJpaEntity(
                company.getId().asUUID(),
                company.getTenantId().asUUID(),
                company.getName().getValue(),
                company.isActive()
        );
    }

    public static Company toDomain(CompanyJpaEntity entity) {
        return new Company(
                CompanyId.of(entity.getId()),
                TenantId.of(entity.getTenantId()),
                new CompanyName(entity.getName())
        );
    }
}