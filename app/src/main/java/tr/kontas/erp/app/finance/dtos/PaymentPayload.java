package tr.kontas.erp.app.finance.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PaymentPayload {
    private String id;
    private String companyId;
    private String paymentNumber;
    private String paymentType;
    private String invoiceId;
    private String customerId;
    private String paymentDate;
    private BigDecimal amount;
    private String currencyCode;
    private BigDecimal exchangeRate;
    private String paymentMethod;
    private String status;
    private String bankAccountRef;
    private String referenceNumber;
}

