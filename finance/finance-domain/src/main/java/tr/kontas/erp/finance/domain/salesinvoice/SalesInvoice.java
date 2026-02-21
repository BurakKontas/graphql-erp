package tr.kontas.erp.finance.domain.salesinvoice;

import lombok.Getter;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.finance.domain.event.SalesInvoicePaidEvent;
import tr.kontas.erp.finance.domain.event.SalesInvoicePostedEvent;
import tr.kontas.erp.finance.domain.exception.InvalidInvoiceStateException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class SalesInvoice extends AggregateRoot<SalesInvoiceId> {
    private static final int AMOUNT_SCALE = 2;
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;

    private final TenantId tenantId;
    private final CompanyId companyId;
    private final InvoiceNumber invoiceNumber;
    private final InvoiceType invoiceType;
    private final String salesOrderId;
    private final String salesOrderNumber;
    private final String customerId;
    private final String customerName;
    private final LocalDate invoiceDate;
    private final LocalDate dueDate;
    private final String currencyCode;
    private final BigDecimal exchangeRate;
    private InvoiceStatus status;
    private BigDecimal subtotal;
    private BigDecimal taxTotal;
    private BigDecimal total;
    private BigDecimal paidAmount;
    private final List<SalesInvoiceLine> lines;

    public SalesInvoice(SalesInvoiceId id,
                        TenantId tenantId,
                        CompanyId companyId,
                        InvoiceNumber invoiceNumber,
                        InvoiceType invoiceType,
                        String salesOrderId,
                        String salesOrderNumber,
                        String customerId,
                        String customerName,
                        LocalDate invoiceDate,
                        LocalDate dueDate,
                        String currencyCode,
                        BigDecimal exchangeRate,
                        InvoiceStatus status,
                        BigDecimal paidAmount,
                        List<SalesInvoiceLine> lines) {
        super(id);

        if (tenantId == null) throw new IllegalArgumentException("tenantId cannot be null");
        if (companyId == null) throw new IllegalArgumentException("companyId cannot be null");
        if (invoiceNumber == null) throw new IllegalArgumentException("invoiceNumber cannot be null");
        if (invoiceDate == null) throw new IllegalArgumentException("invoiceDate cannot be null");

        this.tenantId = tenantId;
        this.companyId = companyId;
        this.invoiceNumber = invoiceNumber;
        this.invoiceType = invoiceType != null ? invoiceType : InvoiceType.STANDARD;
        this.salesOrderId = salesOrderId;
        this.salesOrderNumber = salesOrderNumber;
        this.customerId = customerId;
        this.customerName = customerName;
        this.invoiceDate = invoiceDate;
        this.dueDate = dueDate;
        this.currencyCode = currencyCode;
        this.exchangeRate = exchangeRate != null ? exchangeRate : BigDecimal.ONE;
        this.status = status != null ? status : InvoiceStatus.DRAFT;
        this.paidAmount = paidAmount != null ? paidAmount : BigDecimal.ZERO;
        this.lines = new ArrayList<>(lines != null ? lines : List.of());

        recalculateTotals();
    }

    public void post() {
        if (status != InvoiceStatus.DRAFT) {
            throw new InvalidInvoiceStateException(status.name(), "post");
        }
        if (lines.isEmpty()) {
            throw new IllegalStateException("Cannot post an invoice with no lines");
        }
        this.status = InvoiceStatus.POSTED;

        registerEvent(new SalesInvoicePostedEvent(
                getId().asUUID(), tenantId.asUUID(), companyId.asUUID(),
                invoiceNumber.getValue(), salesOrderId, customerId,
                subtotal, taxTotal, total, invoiceDate
        ));
    }

    public void recordPayment(BigDecimal amount) {
        if (status != InvoiceStatus.POSTED && status != InvoiceStatus.PARTIALLY_PAID) {
            throw new InvalidInvoiceStateException(status.name(), "recordPayment");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive");
        }

        this.paidAmount = this.paidAmount.add(amount).setScale(AMOUNT_SCALE, ROUNDING);

        if (this.paidAmount.compareTo(this.total) >= 0) {
            this.status = InvoiceStatus.PAID;
            registerEvent(new SalesInvoicePaidEvent(
                    getId().asUUID(), tenantId.asUUID(), companyId.asUUID(),
                    invoiceNumber.getValue(), salesOrderId, total
            ));
        } else {
            this.status = InvoiceStatus.PARTIALLY_PAID;
        }
    }

    public void cancel(String reason) {
        if (status == InvoiceStatus.PAID || status == InvoiceStatus.CANCELLED) {
            throw new InvalidInvoiceStateException(status.name(), "cancel");
        }
        if (paidAmount.compareTo(BigDecimal.ZERO) > 0) {
            throw new InvalidInvoiceStateException("PARTIALLY_PAID", "cancel (has payments)");
        }
        this.status = InvoiceStatus.CANCELLED;
    }

    public BigDecimal getRemainingAmount() {
        return total.subtract(paidAmount).setScale(AMOUNT_SCALE, ROUNDING);
    }

    public List<SalesInvoiceLine> getLines() {
        return Collections.unmodifiableList(lines);
    }

    private void recalculateTotals() {
        this.subtotal = lines.stream()
                .map(SalesInvoiceLine::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(AMOUNT_SCALE, ROUNDING);

        this.taxTotal = lines.stream()
                .map(SalesInvoiceLine::getTaxAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(AMOUNT_SCALE, ROUNDING);

        this.total = subtotal.add(taxTotal).setScale(AMOUNT_SCALE, ROUNDING);
    }
}

