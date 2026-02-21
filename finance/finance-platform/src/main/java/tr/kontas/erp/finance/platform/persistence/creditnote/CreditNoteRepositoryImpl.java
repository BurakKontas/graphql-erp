package tr.kontas.erp.finance.platform.persistence.creditnote;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.finance.domain.creditnote.*;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CreditNoteRepositoryImpl implements CreditNoteRepository {

    private final JpaCreditNoteRepository jpa;

    @Override
    public void save(CreditNote cn) {
        jpa.save(CreditNoteMapper.toEntity(cn));
    }

    @Override
    public Optional<CreditNote> findById(CreditNoteId id, TenantId tenantId) {
        return jpa.findByIdAndTenantId(id.asUUID(), tenantId.asUUID())
                .map(CreditNoteMapper::toDomain);
    }

    @Override
    public List<CreditNote> findByCompanyId(TenantId tenantId, CompanyId companyId) {
        return jpa.findByTenantIdAndCompanyId(tenantId.asUUID(), companyId.asUUID())
                .stream().map(CreditNoteMapper::toDomain).toList();
    }

    @Override
    public List<CreditNote> findByIds(List<CreditNoteId> ids) {
        return jpa.findByIdIn(ids.stream().map(CreditNoteId::asUUID).toList())
                .stream().map(CreditNoteMapper::toDomain).toList();
    }

    @Override
    public int findMaxSequenceByTenantId(TenantId tenantId) {
        return jpa.findMaxSequenceByTenantId(tenantId.asUUID());
    }
}
