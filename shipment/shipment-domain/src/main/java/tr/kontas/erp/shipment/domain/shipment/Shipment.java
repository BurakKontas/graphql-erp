package tr.kontas.erp.shipment.domain.shipment;

import lombok.Getter;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.shared.Address;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.shipment.domain.event.ShipmentCreatedEvent;
import tr.kontas.erp.shipment.domain.event.ShipmentDeliveredEvent;
import tr.kontas.erp.shipment.domain.event.ShipmentDispatchedEvent;
import tr.kontas.erp.shipment.domain.exception.InvalidShipmentStateException;
import tr.kontas.erp.shipment.domain.exception.ShipmentInvariantException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class Shipment extends AggregateRoot<ShipmentId> {

    private final TenantId tenantId;
    private final CompanyId companyId;
    private final ShipmentNumber number;
    private final String deliveryOrderId;
    private final String salesOrderId;
    private final String warehouseId;
    private Address shippingAddress;
    private String trackingNumber;
    private String carrierName;
    private ShipmentStatus status;
    private Instant dispatchedAt;
    private Instant deliveredAt;
    private final List<ShipmentLine> lines;

    /** Creation constructor */
    public Shipment(
            ShipmentId id,
            TenantId tenantId,
            CompanyId companyId,
            ShipmentNumber number,
            String deliveryOrderId,
            String salesOrderId,
            String warehouseId,
            Address shippingAddress,
            List<ShipmentLine> lines
    ) {
        super(id);

        if (tenantId == null) throw new IllegalArgumentException("tenantId cannot be null");
        if (companyId == null) throw new IllegalArgumentException("companyId cannot be null");
        if (number == null) throw new IllegalArgumentException("number cannot be null");
        if (deliveryOrderId == null || deliveryOrderId.isBlank())
            throw new IllegalArgumentException("deliveryOrderId cannot be blank");
        if (warehouseId == null || warehouseId.isBlank())
            throw new IllegalArgumentException("warehouseId cannot be blank");
        if (lines == null || lines.isEmpty())
            throw new ShipmentInvariantException("Shipment must have at least one line");

        this.tenantId = tenantId;
        this.companyId = companyId;
        this.number = number;
        this.deliveryOrderId = deliveryOrderId;
        this.salesOrderId = salesOrderId;
        this.warehouseId = warehouseId;
        this.shippingAddress = shippingAddress;
        this.status = ShipmentStatus.PREPARING;
        this.lines = new ArrayList<>(lines);

        registerEvent(new ShipmentCreatedEvent(
                id, tenantId, companyId, number,
                deliveryOrderId, salesOrderId, warehouseId
        ));
    }

    /** Reconstitution constructor */
    public Shipment(
            ShipmentId id,
            TenantId tenantId,
            CompanyId companyId,
            ShipmentNumber number,
            String deliveryOrderId,
            String salesOrderId,
            String warehouseId,
            Address shippingAddress,
            String trackingNumber,
            String carrierName,
            ShipmentStatus status,
            Instant dispatchedAt,
            Instant deliveredAt,
            List<ShipmentLine> lines
    ) {
        super(id);
        this.tenantId = tenantId;
        this.companyId = companyId;
        this.number = number;
        this.deliveryOrderId = deliveryOrderId;
        this.salesOrderId = salesOrderId;
        this.warehouseId = warehouseId;
        this.shippingAddress = shippingAddress;
        this.trackingNumber = trackingNumber;
        this.carrierName = carrierName;
        this.status = status;
        this.dispatchedAt = dispatchedAt;
        this.deliveredAt = deliveredAt;
        this.lines = new ArrayList<>(lines != null ? lines : List.of());
    }

    public void overrideShippingAddress(Address newAddress) {
        if (status != ShipmentStatus.PREPARING) {
            throw new InvalidShipmentStateException(status, "overrideShippingAddress");
        }
        this.shippingAddress = newAddress;
    }

    public void setTrackingInfo(String carrierName, String trackingNumber) {
        if (status == ShipmentStatus.DELIVERED) {
            throw new InvalidShipmentStateException(status, "setTrackingInfo");
        }
        this.carrierName = carrierName;
        this.trackingNumber = trackingNumber;
    }

    public void dispatch() {
        if (!status.canTransitionTo(ShipmentStatus.DISPATCHED)) {
            throw new InvalidShipmentStateException(status, ShipmentStatus.DISPATCHED);
        }
        this.status = ShipmentStatus.DISPATCHED;
        this.dispatchedAt = Instant.now();

        registerEvent(new ShipmentDispatchedEvent(
                getId(), tenantId, companyId,
                deliveryOrderId, salesOrderId, trackingNumber, carrierName
        ));
    }

    public void deliver() {
        if (!status.canTransitionTo(ShipmentStatus.DELIVERED)) {
            throw new InvalidShipmentStateException(status, ShipmentStatus.DELIVERED);
        }
        this.status = ShipmentStatus.DELIVERED;
        this.deliveredAt = Instant.now();

        // Build line data for event
        List<ShipmentDeliveredEvent.DeliveredLineData> lineData = lines.stream()
                .map(l -> new ShipmentDeliveredEvent.DeliveredLineData(
                        l.getItemId(), l.getQuantity(), l.getUnitCode()
                ))
                .toList();

        registerEvent(new ShipmentDeliveredEvent(
                getId(), tenantId, companyId,
                deliveryOrderId, salesOrderId, warehouseId, lineData
        ));
    }

    public List<ShipmentLine> getLines() {
        return Collections.unmodifiableList(lines);
    }
}

