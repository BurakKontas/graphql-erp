package tr.kontas.erp.purchase.domain.purchaseorder;

import lombok.Getter;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.shared.Address;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class PurchaseOrder extends AggregateRoot<PurchaseOrderId> {

    private final TenantId tenantId;
    private final CompanyId companyId;
    private final PurchaseOrderNumber orderNumber;
    private final String requestId;
    private final String vendorId;
    private final String vendorName;
    private LocalDate orderDate;
    private LocalDate expectedDeliveryDate;
    private final String currencyCode;
    private final String paymentTermCode;
    private Address deliveryAddress;
    private PurchaseOrderStatus status;
    private BigDecimal subtotal;
    private BigDecimal taxTotal;
    private BigDecimal total;
    private final List<PurchaseOrderLine> lines;

    public PurchaseOrder(PurchaseOrderId id, TenantId tenantId, CompanyId companyId,
                         PurchaseOrderNumber orderNumber, String requestId,
                         String vendorId, String vendorName,
                         LocalDate orderDate, LocalDate expectedDeliveryDate,
                         String currencyCode, String paymentTermCode,
                         Address deliveryAddress, PurchaseOrderStatus status,
                         List<PurchaseOrderLine> lines) {
        super(id);
        this.tenantId = tenantId;
        this.companyId = companyId;
        this.orderNumber = orderNumber;
        this.requestId = requestId;
        this.vendorId = vendorId;
        this.vendorName = vendorName;
        this.orderDate = orderDate;
        this.expectedDeliveryDate = expectedDeliveryDate;
        this.currencyCode = currencyCode;
        this.paymentTermCode = paymentTermCode;
        this.deliveryAddress = deliveryAddress;
        this.status = status;
        this.lines = new ArrayList<>(lines != null ? lines : List.of());
        recalculate();
    }


    public List<PurchaseOrderLine> getLines() {
        return Collections.unmodifiableList(lines);
    }


    public void send() {
        if (status != PurchaseOrderStatus.DRAFT) {
            throw new IllegalStateException("Can only send DRAFT purchase orders");
        }
        if (lines.isEmpty()) {
            throw new IllegalStateException("Cannot send a purchase order with no lines");
        }
        this.status = PurchaseOrderStatus.SENT;
    }


    public void confirm() {
        if (status != PurchaseOrderStatus.SENT) {
            throw new IllegalStateException("Can only confirm SENT purchase orders");
        }
        this.status = PurchaseOrderStatus.CONFIRMED;
    }


    public void recordReceipt(PurchaseOrderLineId lineId, BigDecimal quantity) {
        if (status != PurchaseOrderStatus.CONFIRMED && status != PurchaseOrderStatus.PARTIALLY_RECEIVED) {
            throw new IllegalStateException("Can only record receipt on CONFIRMED or PARTIALLY_RECEIVED orders");
        }
        PurchaseOrderLine line = lines.stream()
                .filter(l -> l.getId().equals(lineId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Line not found: " + lineId));
        line.addReceived(quantity);

        if (isFullyReceived()) {
            this.status = PurchaseOrderStatus.RECEIVED;
        } else {
            this.status = PurchaseOrderStatus.PARTIALLY_RECEIVED;
        }
    }


    public void markAsInvoiced() {
        if (status != PurchaseOrderStatus.RECEIVED) {
            throw new IllegalStateException("Can only mark RECEIVED purchase orders as invoiced");
        }
        this.status = PurchaseOrderStatus.INVOICED;
    }


    public void cancel(String reason) {
        if (status != PurchaseOrderStatus.DRAFT && status != PurchaseOrderStatus.SENT && status != PurchaseOrderStatus.CONFIRMED) {
            throw new IllegalStateException("Cannot cancel purchase order in status: " + status);
        }
        this.status = PurchaseOrderStatus.CANCELLED;
    }


    public boolean isFullyReceived() {
        return lines.stream().allMatch(PurchaseOrderLine::isFullyReceived);
    }


    public void addLine(PurchaseOrderLine line) {
        if (status != PurchaseOrderStatus.DRAFT) {
            throw new IllegalStateException("Can only add lines to DRAFT purchase orders");
        }
        this.lines.add(line);
        recalculate();
    }


    public void removeLine(PurchaseOrderLineId lineId) {
        if (status != PurchaseOrderStatus.DRAFT) {
            throw new IllegalStateException("Can only remove lines from DRAFT purchase orders");
        }
        boolean removed = this.lines.removeIf(l -> l.getId().equals(lineId));
        if (!removed) {
            throw new IllegalArgumentException("Line not found: " + lineId);
        }
        recalculate();
    }


    private void recalculate() {
        this.subtotal = lines.stream()
                .map(PurchaseOrderLine::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
        this.taxTotal = lines.stream()
                .map(PurchaseOrderLine::getTaxAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
        this.total = subtotal.add(taxTotal);
    }
}

