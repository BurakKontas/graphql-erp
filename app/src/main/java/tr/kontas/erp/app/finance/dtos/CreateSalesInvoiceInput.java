package tr.kontas.erp.app.finance.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.finance.validators.CreateSalesInvoiceInputValidator;

import java.math.BigDecimal;
import java.util.List;

@Data
@Validate(validator = CreateSalesInvoiceInputValidator.class)
public class CreateSalesInvoiceInput implements Validatable {
    private String companyId;
    private String invoiceType;
    private String salesOrderId;
    private String salesOrderNumber;
    private String customerId;
    private String customerName;
    private String invoiceDate;
    private String dueDate;
    private String currencyCode;
    private BigDecimal exchangeRate;
    private List<SalesInvoiceLineInput> lines;

    @Data
    public static class SalesInvoiceLineInput {
        private String salesOrderLineId;
        private String itemId;
        private String itemDescription;
        private String unitCode;
        private BigDecimal quantity;
        private BigDecimal unitPrice;
        private String taxCode;
        private BigDecimal taxRate;
        private String revenueAccountId;
    }
}
