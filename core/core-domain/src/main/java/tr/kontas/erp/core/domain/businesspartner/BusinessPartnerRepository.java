package tr.kontas.erp.core.domain.businesspartner;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.repository.Repository;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface BusinessPartnerRepository extends Repository<BusinessPartner, BusinessPartnerId> {
    Optional<BusinessPartner> findById(BusinessPartnerId id);

    Optional<BusinessPartner> findByCode(TenantId tenantId, CompanyId companyId, BusinessPartnerCode code);

    List<BusinessPartner> findByCompany(TenantId tenantId, CompanyId companyId);

    List<BusinessPartner> findByTenant(TenantId tenantId);

    List<BusinessPartner> findByCompanyIds(TenantId tenantId, List<CompanyId> companyIds);

    List<BusinessPartner> findByRole(TenantId tenantId, CompanyId companyId, BusinessPartnerRole role);

    List<BusinessPartner> findByIds(List<BusinessPartnerId> ids);

    boolean existsByCode(TenantId tenantId, CompanyId companyId, BusinessPartnerCode code);

    void save(BusinessPartner businessPartner);
}
