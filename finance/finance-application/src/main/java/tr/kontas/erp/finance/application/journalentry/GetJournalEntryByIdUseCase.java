package tr.kontas.erp.finance.application.journalentry;

import tr.kontas.erp.finance.domain.journalentry.JournalEntry;
import tr.kontas.erp.finance.domain.journalentry.JournalEntryId;

public interface GetJournalEntryByIdUseCase {
    JournalEntry execute(JournalEntryId id);
}

