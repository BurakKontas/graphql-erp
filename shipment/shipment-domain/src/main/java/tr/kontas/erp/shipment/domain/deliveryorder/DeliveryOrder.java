package tr.kontas.erp.shipment.domain.deliveryorder;

import lombok.Getter;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.shared.Address;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.shipment.domain.event.DeliveryOrderCancelledEvent;
import tr.kontas.erp.shipment.domain.event.DeliveryOrderCompletedEvent;
import tr.kontas.erp.shipment.domain.event.DeliveryOrderCreatedEvent;
import tr.kontas.erp.shipment.domain.exception.InvalidDeliveryOrderStateException;
import tr.kontas.erp.shipment.domain.exception.ShipmentInvariantException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class DeliveryOrder extends AggregateRoot<DeliveryOrderId> {

    private final TenantId tenantId;
    private final CompanyId companyId;
    private final DeliveryOrderNumber number;
    private final String salesOrderId;
    private final String salesOrderNumber;
    private final String customerId;
    private Address shippingAddress;
    private DeliveryOrderStatus status;
    private final List<DeliveryOrderLine> lines;

    /** Creation constructor */
    public DeliveryOrder(
            DeliveryOrderId id,
            TenantId tenantId,
            CompanyId companyId,
            DeliveryOrderNumber number,
            String salesOrderId,
            String salesOrderNumber,
            String customerId,
            Address shippingAddress,
            List<DeliveryOrderLine> lines
    ) {
        super(id);

        if (tenantId == null) throw new IllegalArgumentException("tenantId cannot be null");
        if (companyId == null) throw new IllegalArgumentException("companyId cannot be null");
        if (number == null) throw new IllegalArgumentException("number cannot be null");
        if (salesOrderId == null || salesOrderId.isBlank())
            throw new IllegalArgumentException("salesOrderId cannot be blank");
        if (lines == null || lines.isEmpty())
            throw new ShipmentInvariantException("DeliveryOrder must have at least one line");

        this.tenantId = tenantId;
        this.companyId = companyId;
        this.number = number;
        this.salesOrderId = salesOrderId;
        this.salesOrderNumber = salesOrderNumber;
        this.customerId = customerId;
        this.shippingAddress = shippingAddress;
        this.status = DeliveryOrderStatus.PENDING;
        this.lines = new ArrayList<>(lines);

        registerEvent(new DeliveryOrderCreatedEvent(
                id, tenantId, companyId, number,
                salesOrderId, salesOrderNumber, customerId
        ));
    }

    /** Reconstitution constructor */
    public DeliveryOrder(
            DeliveryOrderId id,
            TenantId tenantId,
            CompanyId companyId,
            DeliveryOrderNumber number,
            String salesOrderId,
            String salesOrderNumber,
            String customerId,
            Address shippingAddress,
            DeliveryOrderStatus status,
            List<DeliveryOrderLine> lines
    ) {
        super(id);
        this.tenantId = tenantId;
        this.companyId = companyId;
        this.number = number;
        this.salesOrderId = salesOrderId;
        this.salesOrderNumber = salesOrderNumber;
        this.customerId = customerId;
        this.shippingAddress = shippingAddress;
        this.status = status;
        this.lines = new ArrayList<>(lines != null ? lines : List.of());
    }

    public void overrideShippingAddress(Address newAddress) {
        if (!status.allowsModification()) {
            throw new InvalidDeliveryOrderStateException(status, "overrideShippingAddress");
        }
        this.shippingAddress = newAddress;
    }

    public void recordShipment(DeliveryOrderLineId lineId, BigDecimal quantity) {
        if (status.isTerminal()) {
            throw new InvalidDeliveryOrderStateException(status, "recordShipment");
        }

        DeliveryOrderLine line = findLine(lineId);
        line.addShipped(quantity);

        // auto-transition PENDING → IN_PROGRESS
        if (status == DeliveryOrderStatus.PENDING) {
            this.status = DeliveryOrderStatus.IN_PROGRESS;
        }

        // check if fully shipped
        if (isFullyShipped()) {
            this.status = DeliveryOrderStatus.COMPLETED;
            registerEvent(new DeliveryOrderCompletedEvent(
                    getId(), tenantId, companyId, salesOrderId
            ));
        }
    }

    public void cancel(CancellationReason reason) {
        if (reason == null) {
            throw new IllegalArgumentException("CancellationReason cannot be null");
        }
        if (!status.canTransitionTo(DeliveryOrderStatus.CANCELLED)) {
            throw new InvalidDeliveryOrderStateException(status, DeliveryOrderStatus.CANCELLED);
        }

        DeliveryOrderStatus previousStatus = this.status;
        this.status = DeliveryOrderStatus.CANCELLED;

        registerEvent(new DeliveryOrderCancelledEvent(
                getId(), tenantId, companyId, salesOrderId, reason, previousStatus
        ));
    }

    public boolean isFullyShipped() {
        return lines.stream().allMatch(DeliveryOrderLine::isFullyShipped);
    }

    public List<DeliveryOrderLine> getLines() {
        return Collections.unmodifiableList(lines);
    }

    private DeliveryOrderLine findLine(DeliveryOrderLineId lineId) {
        return lines.stream()
                .filter(l -> l.getId().equals(lineId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "DeliveryOrderLine not found: " + lineId));
    }
}

