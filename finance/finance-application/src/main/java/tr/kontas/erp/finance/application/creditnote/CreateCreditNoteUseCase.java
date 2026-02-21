package tr.kontas.erp.finance.application.creditnote;

import tr.kontas.erp.finance.domain.creditnote.CreditNoteId;

public interface CreateCreditNoteUseCase {
    CreditNoteId execute(CreateCreditNoteCommand command);
}

