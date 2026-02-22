package tr.kontas.erp.app.finance.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreatePaymentInput {
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