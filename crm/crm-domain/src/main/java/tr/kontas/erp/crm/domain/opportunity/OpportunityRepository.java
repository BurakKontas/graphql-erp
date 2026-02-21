package tr.kontas.erp.crm.domain.opportunity;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface OpportunityRepository {
    void save(Opportunity entity);
    Optional<Opportunity> findById(OpportunityId id, TenantId tenantId);
    List<Opportunity> findByCompanyId(TenantId tenantId, CompanyId companyId);
    List<Opportunity> findByIds(List<OpportunityId> ids);
}

