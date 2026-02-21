package tr.kontas.erp.finance.application.creditnote;

import java.math.BigDecimal;

public interface ApplyCreditNoteUseCase {
    void execute(String creditNoteId, BigDecimal amount);
}

