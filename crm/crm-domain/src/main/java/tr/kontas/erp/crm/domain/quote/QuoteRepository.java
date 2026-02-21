package tr.kontas.erp.crm.domain.quote;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface QuoteRepository {
    void save(Quote entity);
    Optional<Quote> findById(QuoteId id, TenantId tenantId);
    List<Quote> findByCompanyId(TenantId tenantId, CompanyId companyId);
    List<Quote> findByIds(List<QuoteId> ids);
}

