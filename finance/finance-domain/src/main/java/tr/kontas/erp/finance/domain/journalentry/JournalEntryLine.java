package tr.kontas.erp.finance.domain.journalentry;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.Entity;

import java.math.BigDecimal;

@Getter
public class JournalEntryLine extends Entity<JournalEntryLineId> {

    private final JournalEntryId entryId;
    private final String accountId;
    private final String accountCode;
    private final String accountName;
    private final BigDecimal debitAmount;
    private final BigDecimal creditAmount;
    private final String description;

    public JournalEntryLine(JournalEntryLineId id,
                            JournalEntryId entryId,
                            String accountId,
                            String accountCode,
                            String accountName,
                            BigDecimal debitAmount,
                            BigDecimal creditAmount,
                            String description) {
        super(id);

        if (entryId == null) throw new IllegalArgumentException("entryId cannot be null");
        if (accountId == null) throw new IllegalArgumentException("accountId cannot be null");

        boolean hasDebit = debitAmount != null && debitAmount.compareTo(BigDecimal.ZERO) > 0;
        boolean hasCredit = creditAmount != null && creditAmount.compareTo(BigDecimal.ZERO) > 0;

        if (hasDebit && hasCredit) {
            throw new IllegalArgumentException("A journal entry line cannot have both debit and credit amounts");
        }
        if (!hasDebit && !hasCredit) {
            throw new IllegalArgumentException("A journal entry line must have either a debit or credit amount");
        }

        this.entryId = entryId;
        this.accountId = accountId;
        this.accountCode = accountCode;
        this.accountName = accountName;
        this.debitAmount = hasDebit ? debitAmount : BigDecimal.ZERO;
        this.creditAmount = hasCredit ? creditAmount : BigDecimal.ZERO;
        this.description = description;
    }
}

