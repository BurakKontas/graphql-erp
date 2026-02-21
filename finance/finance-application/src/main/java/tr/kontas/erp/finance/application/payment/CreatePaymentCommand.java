package tr.kontas.erp.finance.application.payment;

import tr.kontas.erp.core.domain.company.CompanyId;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CreatePaymentCommand(
        CompanyId companyId,
        String paymentType,
        String invoiceId,
        String customerId,
        LocalDate paymentDate,
        BigDecimal amount,
        String currencyCode,
        BigDecimal exchangeRate,
        String paymentMethod,
        String bankAccountRef,
        String referenceNumber
) {}

