package tr.kontas.erp.finance.domain.creditnote;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface CreditNoteRepository {
    void save(CreditNote creditNote);
    Optional<CreditNote> findById(CreditNoteId id, TenantId tenantId);
    List<CreditNote> findByCompanyId(TenantId tenantId, CompanyId companyId);
    List<CreditNote> findByIds(List<CreditNoteId> ids);
    int findMaxSequenceByTenantId(TenantId tenantId);
}

