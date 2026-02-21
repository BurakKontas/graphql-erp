package tr.kontas.erp.finance.application.creditnote;

import tr.kontas.erp.finance.domain.creditnote.CreditNote;
import tr.kontas.erp.finance.domain.creditnote.CreditNoteId;

public interface GetCreditNoteByIdUseCase {
    CreditNote execute(CreditNoteId id);
}

