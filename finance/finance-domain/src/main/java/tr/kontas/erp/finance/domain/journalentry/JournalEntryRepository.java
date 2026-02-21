package tr.kontas.erp.finance.domain.journalentry;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface JournalEntryRepository {
    void save(JournalEntry entry);
    Optional<JournalEntry> findById(JournalEntryId id, TenantId tenantId);
    List<JournalEntry> findByCompanyId(TenantId tenantId, CompanyId companyId);
    List<JournalEntry> findByReference(TenantId tenantId, String referenceType, String referenceId);
    List<JournalEntry> findByIds(List<JournalEntryId> ids);
    int findMaxSequenceByTenantId(TenantId tenantId);
}

