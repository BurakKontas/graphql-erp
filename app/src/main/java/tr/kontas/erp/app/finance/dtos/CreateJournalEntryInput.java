package tr.kontas.erp.app.finance.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.finance.validators.CreateJournalEntryInputValidator;

import java.math.BigDecimal;
import java.util.List;

@Data
@Validate(validator = CreateJournalEntryInputValidator.class)
public class CreateJournalEntryInput implements Validatable {
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
