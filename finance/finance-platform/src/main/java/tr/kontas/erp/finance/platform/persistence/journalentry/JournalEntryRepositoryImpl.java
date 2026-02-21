package tr.kontas.erp.finance.platform.persistence.journalentry;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.finance.domain.journalentry.*;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JournalEntryRepositoryImpl implements JournalEntryRepository {

    private final JpaJournalEntryRepository jpa;

    @Override
    public void save(JournalEntry entry) {
        jpa.save(JournalEntryMapper.toEntity(entry));
    }

    @Override
    public Optional<JournalEntry> findById(JournalEntryId id, TenantId tenantId) {
        return jpa.findByIdAndTenantId(id.asUUID(), tenantId.asUUID())
                .map(JournalEntryMapper::toDomain);
    }

    @Override
    public List<JournalEntry> findByCompanyId(TenantId tenantId, CompanyId companyId) {
        return jpa.findByTenantIdAndCompanyId(tenantId.asUUID(), companyId.asUUID())
                .stream().map(JournalEntryMapper::toDomain).toList();
    }

    @Override
    public List<JournalEntry> findByReference(TenantId tenantId, String referenceType, String referenceId) {
        return jpa.findByTenantIdAndReferenceTypeAndReferenceId(tenantId.asUUID(), referenceType, referenceId)
                .stream().map(JournalEntryMapper::toDomain).toList();
    }

    @Override
    public List<JournalEntry> findByIds(List<JournalEntryId> ids) {
        return jpa.findByIdIn(ids.stream().map(JournalEntryId::asUUID).toList())
                .stream().map(JournalEntryMapper::toDomain).toList();
    }

    @Override
    public int findMaxSequenceByTenantId(TenantId tenantId) {
        return jpa.findMaxSequenceByTenantId(tenantId.asUUID());
    }
}
