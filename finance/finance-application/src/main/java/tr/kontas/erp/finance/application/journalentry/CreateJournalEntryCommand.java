package tr.kontas.erp.finance.application.journalentry;

import tr.kontas.erp.core.domain.company.CompanyId;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record CreateJournalEntryCommand(
        CompanyId companyId,
        String type,
        String periodId,
        LocalDate entryDate,
        String description,
        String referenceType,
        String referenceId,
        String currencyCode,
        BigDecimal exchangeRate,
        List<LineInput> lines
) {
    public record LineInput(
            String accountId,
            String accountCode,
            String accountName,
            BigDecimal debitAmount,
            BigDecimal creditAmount,
            String description
    ) {}
}

