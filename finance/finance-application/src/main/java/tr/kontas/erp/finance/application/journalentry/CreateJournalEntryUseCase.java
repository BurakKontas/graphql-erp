package tr.kontas.erp.finance.application.journalentry;

import tr.kontas.erp.finance.domain.journalentry.JournalEntryId;

public interface CreateJournalEntryUseCase {
    JournalEntryId execute(CreateJournalEntryCommand command);
}

