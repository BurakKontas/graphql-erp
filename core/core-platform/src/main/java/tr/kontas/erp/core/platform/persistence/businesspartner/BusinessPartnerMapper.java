package tr.kontas.erp.core.platform.persistence.businesspartner;

import tr.kontas.erp.core.domain.businesspartner.*;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

public class BusinessPartnerMapper {
    public static BusinessPartnerJpaEntity toEntity(BusinessPartner domain) {
        BusinessPartnerJpaEntity entity = new BusinessPartnerJpaEntity();
        entity.setId(domain.getId().asUUID());
        entity.setTenantId(domain.getTenantId().asUUID());
        entity.setCompanyId(domain.getCompanyId().asUUID());
        entity.setCode(domain.getCode().getValue());
        entity.setName(domain.getName().getValue());
        entity.setTaxNumber(domain.getTaxNumber().getValue());
        entity.setActive(domain.isActive());
        entity.setRoles(domain.getRoles());
        return entity;
    }

    public static BusinessPartner toDomain(BusinessPartnerJpaEntity entity) {
        return new BusinessPartner(
                BusinessPartnerId.of(entity.getId()),
                TenantId.of(entity.getTenantId()),
                CompanyId.of(entity.getCompanyId()),
                new BusinessPartnerCode(entity.getCode()),
                new BusinessPartnerName(entity.getName()),
                entity.getRoles(),
                new BusinessPartnerTaxNumber(entity.getTaxNumber()),
                entity.isActive()
        );
    }
}
