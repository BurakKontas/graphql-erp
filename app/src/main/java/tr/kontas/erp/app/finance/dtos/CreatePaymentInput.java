package tr.kontas.erp.app.finance.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.finance.validators.CreatePaymentInputValidator;

import java.math.BigDecimal;

@Data
@Validate(validator = CreatePaymentInputValidator.class)
public class CreatePaymentInput implements Validatable {
    private String companyId;
    private String paymentType;
    private String invoiceId;
    private String customerId;
    private String paymentDate;
    private BigDecimal amount;
    private String currencyCode;
    private BigDecimal exchangeRate;
    private String paymentMethod;
    private String bankAccountRef;
    private String referenceNumber;
}