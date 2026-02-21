package tr.kontas.erp.finance.application.journalentry;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.finance.domain.journalentry.JournalEntry;
import java.util.List;

public interface GetJournalEntriesByCompanyUseCase {
    List<JournalEntry> execute(CompanyId companyId);
}

