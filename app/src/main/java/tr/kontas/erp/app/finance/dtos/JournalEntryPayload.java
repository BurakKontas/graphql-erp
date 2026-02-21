package tr.kontas.erp.app.finance.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class JournalEntryPayload {
    private String id;
    private String companyId;
    private String entryNumber;
    private String type;
    private String periodId;
    private String entryDate;
    private String description;
    private String referenceType;
    private String referenceId;
    private String currencyCode;
    private BigDecimal exchangeRate;
    private String status;
    private BigDecimal totalDebit;
    private BigDecimal totalCredit;
    private List<JournalEntryLinePayload> lines;
}

