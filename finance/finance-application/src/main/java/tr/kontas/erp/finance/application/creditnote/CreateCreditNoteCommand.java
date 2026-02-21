package tr.kontas.erp.finance.application.creditnote;

import tr.kontas.erp.core.domain.company.CompanyId;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record CreateCreditNoteCommand(
        CompanyId companyId,
        String invoiceId,
        String customerId,
        LocalDate creditNoteDate,
        String currencyCode,
        String reason,
        List<LineInput> lines
) {
    public record LineInput(
            String itemId,
            String itemDescription,
            String unitCode,
            BigDecimal quantity,
            BigDecimal unitPrice,
            String taxCode,
            BigDecimal taxRate
    ) {}
}

