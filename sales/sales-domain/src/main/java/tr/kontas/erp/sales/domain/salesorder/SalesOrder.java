package tr.kontas.erp.sales.domain.salesorder;

import lombok.Getter;
import tr.kontas.erp.core.domain.businesspartner.BusinessPartnerId;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.reference.currency.Currency;
import tr.kontas.erp.core.domain.reference.payment.PaymentTerm;
import tr.kontas.erp.core.domain.reference.tax.Tax;
import tr.kontas.erp.core.domain.shared.Address;
import tr.kontas.erp.core.domain.shared.Quantity;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.sales.domain.event.*;
import tr.kontas.erp.sales.domain.exception.CannotCancelInvoicedOrderException;
import tr.kontas.erp.sales.domain.exception.InvalidSalesOrderStateException;
import tr.kontas.erp.sales.domain.exception.SalesOrderInvariantException;
import tr.kontas.erp.sales.domain.exception.SalesOrderLineNotFoundException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class SalesOrder extends AggregateRoot<SalesOrderId> {
    private static final int AMOUNT_SCALE = 2;
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;

    private final TenantId tenantId;
    private final CompanyId companyId;

    private final SalesOrderNumber orderNumber;
    private final List<SalesOrderLine> lines;
    private LocalDate orderDate;
    private LocalDate expiryDate;
    private final BusinessPartnerId customerId;
    private final Currency currency;
    private PaymentTerm paymentTerm;
    private Address shippingAddress;
    private SalesOrderStatus status;
    private FulfillmentStatus fulfillmentStatus;
    private BigDecimal invoicedAmount;
    private BigDecimal subtotal;
    private BigDecimal taxTotal;
    private BigDecimal total;

    public SalesOrder(
            SalesOrderId id,
            TenantId tenantId,
            CompanyId companyId,
            SalesOrderNumber orderNumber,
            BusinessPartnerId customerId,
            Currency currency,
            PaymentTerm paymentTerm,
            LocalDate orderDate) {
        super(id);

        if (tenantId == null)
            throw new IllegalArgumentException("tenantId cannot be null");
        if (companyId == null)
            throw new IllegalArgumentException("companyId cannot be null");
        if (orderNumber == null)
            throw new IllegalArgumentException("orderNumber cannot be null");
        if (orderDate == null)
            throw new IllegalArgumentException("orderDate cannot be null");

        this.tenantId = tenantId;
        this.companyId = companyId;
        this.orderNumber = orderNumber;
        this.customerId = customerId;
        this.currency = currency;
        this.paymentTerm = paymentTerm;
        this.orderDate = orderDate;

        this.status = SalesOrderStatus.DRAFT;
        this.fulfillmentStatus = FulfillmentStatus.NOT_STARTED;
        this.invoicedAmount = BigDecimal.ZERO;
        this.lines = new ArrayList<>();

        recalculateTotals();

        registerEvent(new SalesOrderCreatedEvent(
                        id, tenantId, companyId, orderNumber,
                        customerId, currency, orderDate
                )
        );
    }

    public SalesOrder(
            SalesOrderId id,
            TenantId tenantId,
            CompanyId companyId,
            SalesOrderNumber orderNumber,
            BusinessPartnerId customerId,
            Currency currency,
            PaymentTerm paymentTerm,
            LocalDate orderDate,
            LocalDate expiryDate,
            Address shippingAddress,
            SalesOrderStatus status,
            FulfillmentStatus fulfillmentStatus,
            BigDecimal invoicedAmount,
            List<SalesOrderLine> lines) {
        super(id);

        this.tenantId = tenantId;
        this.companyId = companyId;
        this.orderNumber = orderNumber;
        this.customerId = customerId;
        this.currency = currency;
        this.paymentTerm = paymentTerm;
        this.orderDate = orderDate;
        this.expiryDate = expiryDate;
        this.shippingAddress = shippingAddress;
        this.status = status;
        this.fulfillmentStatus = fulfillmentStatus;
        this.invoicedAmount = invoicedAmount != null ? invoicedAmount : BigDecimal.ZERO;
        this.lines = new ArrayList<>(lines != null ? lines : List.of());

        recalculateTotals();
    }

    public void addLine(
            SalesOrderLineId lineId,
            String itemId,
            String itemDescription,
            String unitCode,
            Quantity quantity,
            BigDecimal unitPrice,
            Tax tax) {

        ensureDraft("addLine");

        SalesOrderLine line = new SalesOrderLine(
                lineId,
                getId(),
                nextSequence(),
                itemId,
                itemDescription,
                unitCode,
                quantity,
                unitPrice,
                tax
        );

        lines.add(line);
        recalculateTotals();

        registerEvent(new SalesOrderLineAddedEvent(
                        getId(), lineId, tenantId,
                        itemId, itemDescription, quantity.getValue(),
                        unitPrice, currency, tax
                )
        );
    }

    public void removeLine(SalesOrderLineId lineId) {
        ensureDraft("removeLine");

        boolean removed = lines.removeIf(l -> l.getId().equals(lineId));
        if (!removed) {
            throw new SalesOrderLineNotFoundException(lineId);
        }

        recalculateTotals();

        registerEvent(new SalesOrderLineRemovedEvent(getId(), lineId, tenantId));
    }

    public boolean updateTaxOnLines(Tax updatedTax) {
        if (status != SalesOrderStatus.DRAFT && status != SalesOrderStatus.NEEDS_ACTION) return false;

        String taxCode = updatedTax.getId().getValue();
        boolean anyUpdated = false;
        for (SalesOrderLine line : lines) {
            if (line.getTax() != null && taxCode.equals(line.getTax().getId().getValue())) {
                line.updateTax(updatedTax);
                anyUpdated = true;
            }
        }
        if (anyUpdated) {
            recalculateTotals();
        }
        return anyUpdated;
    }

    public void markNeedsAction() {
        if (status == SalesOrderStatus.DRAFT) {
            this.status = SalesOrderStatus.NEEDS_ACTION;
        }
    }

    public void updateLine(
            SalesOrderLineId lineId,
            Quantity newQuantity,
            BigDecimal newUnitPrice) {

        ensureDraft("updateLine");

        SalesOrderLine line = findLine(lineId);
        line.update(newQuantity, newUnitPrice);
        recalculateTotals();

        registerEvent(new SalesOrderLineUpdatedEvent(
                        getId(), lineId, tenantId,
                        newQuantity.getValue(), newUnitPrice, currency
                )
        );
    }

    public void updateHeader(
            LocalDate orderDate,
            LocalDate expiryDate,
            PaymentTerm paymentTerm,
            Address shippingAddress) {

        ensureDraft("updateHeader");

        if (orderDate != null) this.orderDate = orderDate;
        if (expiryDate != null) this.expiryDate = expiryDate;
        if (paymentTerm != null) this.paymentTerm = paymentTerm;
        this.shippingAddress = shippingAddress;
    }

    public void send() {
        transitionTo(SalesOrderStatus.SENT, "send");

        registerEvent(new SalesOrderSentEvent(getId(), tenantId, companyId, expiryDate));
    }

    public void accept() {
        transitionTo(SalesOrderStatus.ACCEPTED, "accept");

        registerEvent(new SalesOrderAcceptedEvent(getId(), tenantId, companyId));
    }

    public void confirm() {
        if (!status.canTransitionTo(SalesOrderStatus.CONFIRMED)) {
            throw new InvalidSalesOrderStateException(status, SalesOrderStatus.CONFIRMED);
        }
        if (lines.isEmpty()) {
            throw new SalesOrderInvariantException(
                    "Cannot confirm SalesOrder '%s' — no lines".formatted(orderNumber));
        }
        if (total.compareTo(BigDecimal.ZERO) < 0) {
            throw new SalesOrderInvariantException(
                    "Cannot confirm SalesOrder '%s' — total is negative".formatted(orderNumber));
        }

        this.status = SalesOrderStatus.CONFIRMED;

        registerEvent(new SalesOrderConfirmedEvent(
                        getId(), tenantId, companyId, orderNumber,
                        customerId, currency, paymentTerm,
                        subtotal, taxTotal, total, orderDate
                )
        );
    }

    public void cancel(CancellationReason reason) {
        if (reason == null) {
            throw new IllegalArgumentException("CancellationReason cannot be null");
        }
        if (!status.canTransitionTo(SalesOrderStatus.CANCELLED)) {
            throw new InvalidSalesOrderStateException(status, SalesOrderStatus.CANCELLED);
        }

        InvoicingStatus invoicingStatus = getInvoicingStatus();
        if (invoicingStatus != InvoicingStatus.NOT_INVOICED) {
            throw new CannotCancelInvoicedOrderException(invoicingStatus);
        }

        SalesOrderStatus previousStatus = this.status;
        this.status = SalesOrderStatus.CANCELLED;

        registerEvent(new SalesOrderCancelledEvent(
                        getId(), tenantId, companyId, reason, previousStatus
                )
        );
    }

    public void recordInvoice(BigDecimal invoicedAmountDelta) {
        if (invoicedAmountDelta == null || invoicedAmountDelta.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("invoicedAmountDelta must be positive");
        }
        if (status != SalesOrderStatus.CONFIRMED && status != SalesOrderStatus.CLOSED) {
            throw new InvalidSalesOrderStateException(status, "recordInvoice");
        }

        this.invoicedAmount = this.invoicedAmount
                .add(invoicedAmountDelta)
                .setScale(AMOUNT_SCALE, ROUNDING);

        registerEvent(new SalesOrderInvoiceRecordedEvent(
                        getId(), tenantId,
                        invoicedAmountDelta, this.invoicedAmount, getInvoicingStatus()
                )
        );

        tryClose();
    }

    public void updateFulfillmentStatus(FulfillmentStatus newFulfillmentStatus) {
        if (newFulfillmentStatus == null) {
            throw new IllegalArgumentException("fulfillmentStatus cannot be null");
        }
        if (this.fulfillmentStatus == newFulfillmentStatus) {
            return; // idempotent
        }

        this.fulfillmentStatus = newFulfillmentStatus;

        registerEvent(new SalesOrderFulfillmentStatusChangedEvent(
                        getId(), tenantId, newFulfillmentStatus
                )
        );

        tryClose();
    }

    public InvoicingStatus getInvoicingStatus() {
        if (invoicedAmount.compareTo(BigDecimal.ZERO) == 0) {
            return InvoicingStatus.NOT_INVOICED;
        }
        if (invoicedAmount.compareTo(total) < 0) {
            return InvoicingStatus.PARTIALLY_INVOICED;
        }
        return InvoicingStatus.FULLY_INVOICED;
    }

    public List<SalesOrderLine> getLines() {
        return Collections.unmodifiableList(lines);
    }

    private void tryClose() {
        if (status == SalesOrderStatus.CONFIRMED
                && fulfillmentStatus == FulfillmentStatus.FULFILLED
                && getInvoicingStatus() == InvoicingStatus.FULLY_INVOICED) {

            this.status = SalesOrderStatus.CLOSED;

            registerEvent(new SalesOrderClosedEvent(getId(), tenantId, companyId));
        }
    }

    private void ensureDraft(String operation) {
        if (!status.allowsModification()) {
            throw new InvalidSalesOrderStateException(status, operation);
        }
    }

    private void transitionTo(SalesOrderStatus target, String operation) {
        if (!status.canTransitionTo(target)) {
            throw new InvalidSalesOrderStateException(status, target);
        }
        this.status = target;
    }

    private void recalculateTotals() {
        this.subtotal = lines.stream()
                .map(SalesOrderLine::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(AMOUNT_SCALE, ROUNDING);

        this.taxTotal = lines.stream()
                .map(SalesOrderLine::getTaxAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(AMOUNT_SCALE, ROUNDING);

        this.total = subtotal.add(taxTotal)
                .setScale(AMOUNT_SCALE, ROUNDING);
    }

    private int nextSequence() {
        return lines.stream()
                .mapToInt(SalesOrderLine::getSequence)
                .max()
                .orElse(0) + 1;
    }

    private SalesOrderLine findLine(SalesOrderLineId lineId) {
        return lines.stream()
                .filter(l -> l.getId().equals(lineId))
                .findFirst()
                .orElseThrow(() -> new SalesOrderLineNotFoundException(lineId));
    }
}