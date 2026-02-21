package tr.kontas.erp.app.finance.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateCreditNoteInput {
    private String companyId;
    private String invoiceId;
    private String customerId;
    private String creditNoteDate;
    private String currencyCode;
    private String reason;
    private List<CreditNoteLineInput> lines;

    @Data
    public static class CreditNoteLineInput {
        private String itemId;
        private String itemDescription;
        private String unitCode;
        private BigDecimal quantity;
        private BigDecimal unitPrice;
        private String taxCode;
        private BigDecimal taxRate;
    }
}

