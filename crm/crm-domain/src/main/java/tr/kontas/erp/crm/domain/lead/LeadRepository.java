package tr.kontas.erp.crm.domain.lead;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface LeadRepository {
    void save(Lead entity);
    Optional<Lead> findById(LeadId id, TenantId tenantId);
    List<Lead> findByCompanyId(TenantId tenantId, CompanyId companyId);
    List<Lead> findByIds(List<LeadId> ids);
}

