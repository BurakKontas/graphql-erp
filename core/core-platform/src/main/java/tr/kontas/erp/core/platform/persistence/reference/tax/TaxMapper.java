package tr.kontas.erp.core.platform.persistence.reference.tax;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.reference.tax.Tax;
import tr.kontas.erp.core.domain.reference.tax.TaxCode;
import tr.kontas.erp.core.domain.reference.tax.TaxRate;
import tr.kontas.erp.core.domain.reference.tax.TaxType;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

public class TaxMapper {

    public static Tax toDomain(TaxJpaEntity entity) {
        Tax tax = new Tax(
                new TaxCode(entity.getCode()),
                TenantId.of(entity.getTenantId()),
                CompanyId.of(entity.getCompanyId()),
                entity.getName(),
                TaxType.valueOf(entity.getType()),
                new TaxRate(entity.getRate())
        );
        if (!entity.isActive()) {
            tax.deactivate();
        }
        return tax;
    }

    public static TaxJpaEntity toEntity(Tax domain) {
        return TaxJpaEntity.builder()
                .code(domain.getId().getValue())
                .tenantId(domain.getTenantId().asUUID())
                .companyId(domain.getCompanyId().asUUID())
                .name(domain.getName())
                .type(domain.getType().name())
                .rate(domain.getRate().getValue())
                .active(domain.isActive())
                .build();
    }
}
