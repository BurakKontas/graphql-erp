package tr.kontas.erp.app.finance.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateJournalEntryInput {
    private String companyId;
    private String type;
    private String periodId;
    private String entryDate;
    private String description;
    private String referenceType;
    private String referenceId;
    private String currencyCode;
    private BigDecimal exchangeRate;
    private List<JournalEntryLineInput> lines;

    @Data
    public static class JournalEntryLineInput {
        private String accountId;
        private String accountCode;
        private String accountName;
        private BigDecimal debitAmount;
        private BigDecimal creditAmount;
        private String description;
    }
}

