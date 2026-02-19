package tr.kontas.erp.core.platform.persistence.businesspartner;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.businesspartner.*;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class BusinessPartnerRepositoryImpl implements BusinessPartnerRepository {

    private final JpaBusinessPartnerRepository jpaRepository;

    @Override
    public Optional<BusinessPartner> findById(BusinessPartnerId id) {
        return jpaRepository.findById(id.asUUID())
                .map(BusinessPartnerMapper::toDomain);
    }

    @Override
    public Optional<BusinessPartner> findByCode(TenantId tenantId, CompanyId companyId, BusinessPartnerCode code) {
        return jpaRepository.findByTenantIdAndCompanyIdAndCode(tenantId.asUUID(), companyId.asUUID(), code.getValue())
                .map(BusinessPartnerMapper::toDomain);
    }

    @Override
    public List<BusinessPartner> findByCompany(TenantId tenantId, CompanyId companyId) {
        return jpaRepository.findByTenantIdAndCompanyId(tenantId.asUUID(), companyId.asUUID())
                .stream()
                .map(BusinessPartnerMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<BusinessPartner> findByTenant(TenantId tenantId) {
        return jpaRepository.findByTenantId(tenantId.asUUID())
                .stream()
                .map(BusinessPartnerMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<BusinessPartner> findByCompanyIds(TenantId tenantId, List<CompanyId> companyIds) {
        List<UUID> ids = companyIds.stream()
                .map(CompanyId::asUUID)
                .collect(Collectors.toList());

        return jpaRepository.findByTenantIdAndCompanyIdIn(tenantId.asUUID(), ids)
                .stream()
                .map(BusinessPartnerMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<BusinessPartner> findByRole(TenantId tenantId, CompanyId companyId, BusinessPartnerRole role) {
        return jpaRepository.findByRole(tenantId.asUUID(), companyId.asUUID(), role)
                .stream()
                .map(BusinessPartnerMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByCode(TenantId tenantId, CompanyId companyId, BusinessPartnerCode code) {
        return jpaRepository.existsByTenantIdAndCompanyIdAndCode(tenantId.asUUID(), companyId.asUUID(), code.getValue());
    }

    @Override
    public void save(BusinessPartner businessPartner) {
        BusinessPartnerJpaEntity entity = BusinessPartnerMapper.toEntity(businessPartner);
        jpaRepository.save(entity);
    }
}
