package tr.kontas.erp.app.finance.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.finance.validators.CreateCreditNoteInputValidator;

import java.math.BigDecimal;
import java.util.List;

@Data
@Validate(validator = CreateCreditNoteInputValidator.class)
public class CreateCreditNoteInput implements Validatable {
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
