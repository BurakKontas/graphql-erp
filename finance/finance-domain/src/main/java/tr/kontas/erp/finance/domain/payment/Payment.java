package tr.kontas.erp.finance.domain.payment;

import lombok.Getter;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.finance.domain.event.PaymentPostedEvent;
import tr.kontas.erp.finance.domain.exception.InvalidPaymentStateException;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public class Payment extends AggregateRoot<PaymentId> {

    private final TenantId tenantId;
    private final CompanyId companyId;
    private final String paymentNumber;
    private final PaymentType paymentType;
    private final String invoiceId;
    private final String customerId;
    private final LocalDate paymentDate;
    private final BigDecimal amount;
    private final String currencyCode;
    private final BigDecimal exchangeRate;
    private final PaymentMethod paymentMethod;
    private PaymentStatus status;
    private final String bankAccountRef;
    private final String referenceNumber;

    public Payment(PaymentId id,
                   TenantId tenantId,
                   CompanyId companyId,
                   String paymentNumber,
                   PaymentType paymentType,
                   String invoiceId,
                   String customerId,
                   LocalDate paymentDate,
                   BigDecimal amount,
                   String currencyCode,
                   BigDecimal exchangeRate,
                   PaymentMethod paymentMethod,
                   PaymentStatus status,
                   String bankAccountRef,
                   String referenceNumber) {
        super(id);

        if (tenantId == null) throw new IllegalArgumentException("tenantId cannot be null");
        if (companyId == null) throw new IllegalArgumentException("companyId cannot be null");
        if (paymentType == null) throw new IllegalArgumentException("paymentType cannot be null");
        if (paymentDate == null) throw new IllegalArgumentException("paymentDate cannot be null");
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("amount must be positive");
        if (paymentMethod == null) throw new IllegalArgumentException("paymentMethod cannot be null");

        this.tenantId = tenantId;
        this.companyId = companyId;
        this.paymentNumber = paymentNumber;
        this.paymentType = paymentType;
        this.invoiceId = invoiceId;
        this.customerId = customerId;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.currencyCode = currencyCode;
        this.exchangeRate = exchangeRate != null ? exchangeRate : BigDecimal.ONE;
        this.paymentMethod = paymentMethod;
        this.status = status != null ? status : PaymentStatus.DRAFT;
        this.bankAccountRef = bankAccountRef;
        this.referenceNumber = referenceNumber;
    }

    public void post() {
        if (status != PaymentStatus.DRAFT) {
            throw new InvalidPaymentStateException(status.name(), "post");
        }
        this.status = PaymentStatus.POSTED;

        registerEvent(new PaymentPostedEvent(
                getId().asUUID(), tenantId.asUUID(), companyId.asUUID(),
                paymentNumber, paymentType.name(), invoiceId, customerId,
                amount, currencyCode, paymentDate
        ));
    }

    public void reconcile() {
        if (status != PaymentStatus.POSTED) {
            throw new InvalidPaymentStateException(status.name(), "reconcile");
        }
        this.status = PaymentStatus.RECONCILED;
    }

    public void cancel(String reason) {
        if (status == PaymentStatus.RECONCILED || status == PaymentStatus.CANCELLED) {
            throw new InvalidPaymentStateException(status.name(), "cancel");
        }
        this.status = PaymentStatus.CANCELLED;
    }
}

