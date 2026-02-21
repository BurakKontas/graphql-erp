package tr.kontas.erp.finance.platform.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.finance.application.port.JournalEntryNumberGeneratorPort;
import tr.kontas.erp.finance.platform.persistence.journalentry.JpaJournalEntryRepository;

@Component
@RequiredArgsConstructor
public class JournalEntryNumberGeneratorImpl implements JournalEntryNumberGeneratorPort {
    private final JpaJournalEntryRepository jpa;

    @Override
    public String generate(TenantId tenantId, CompanyId companyId, int year) {
        int nextSeq = jpa.findMaxSequenceByTenantId(tenantId.asUUID()) + 1;
        return "JE-%04d-%06d".formatted(year, nextSeq);
    }
}

