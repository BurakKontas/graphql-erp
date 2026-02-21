package tr.kontas.erp.shipment.domain.shipmentreturn;

import lombok.Getter;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.shipment.domain.event.*;
import tr.kontas.erp.shipment.domain.exception.InvalidShipmentReturnStateException;
import tr.kontas.erp.shipment.domain.exception.ShipmentInvariantException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class ShipmentReturn extends AggregateRoot<ShipmentReturnId> {

    private final TenantId tenantId;
    private final CompanyId companyId;
    private final ShipmentReturnNumber number;
    private final String shipmentId;
    private final String salesOrderId;
    private final String warehouseId;
    private final ReturnReason reason;
    private ReturnStatus status;
    private Instant receivedAt;
    private final List<ShipmentReturnLine> lines;

    /** Creation constructor */
    public ShipmentReturn(
            ShipmentReturnId id,
            TenantId tenantId,
            CompanyId companyId,
            ShipmentReturnNumber number,
            String shipmentId,
            String salesOrderId,
            String warehouseId,
            ReturnReason reason,
            List<ShipmentReturnLine> lines
    ) {
        super(id);

        if (tenantId == null) throw new IllegalArgumentException("tenantId cannot be null");
        if (companyId == null) throw new IllegalArgumentException("companyId cannot be null");
        if (number == null) throw new IllegalArgumentException("number cannot be null");
        if (shipmentId == null || shipmentId.isBlank())
            throw new IllegalArgumentException("shipmentId cannot be blank");
        if (reason == null) throw new IllegalArgumentException("reason cannot be null");
        if (lines == null || lines.isEmpty())
            throw new ShipmentInvariantException("ShipmentReturn must have at least one line");

        this.tenantId = tenantId;
        this.companyId = companyId;
        this.number = number;
        this.shipmentId = shipmentId;
        this.salesOrderId = salesOrderId;
        this.warehouseId = warehouseId;
        this.reason = reason;
        this.status = ReturnStatus.REQUESTED;
        this.lines = new ArrayList<>(lines);

        registerEvent(new ShipmentReturnCreatedEvent(
                id, tenantId, companyId, number,
                shipmentId, salesOrderId, warehouseId
        ));
    }

    /** Reconstitution constructor */
    public ShipmentReturn(
            ShipmentReturnId id,
            TenantId tenantId,
            CompanyId companyId,
            ShipmentReturnNumber number,
            String shipmentId,
            String salesOrderId,
            String warehouseId,
            ReturnReason reason,
            ReturnStatus status,
            Instant receivedAt,
            List<ShipmentReturnLine> lines
    ) {
        super(id);
        this.tenantId = tenantId;
        this.companyId = companyId;
        this.number = number;
        this.shipmentId = shipmentId;
        this.salesOrderId = salesOrderId;
        this.warehouseId = warehouseId;
        this.reason = reason;
        this.status = status;
        this.receivedAt = receivedAt;
        this.lines = new ArrayList<>(lines != null ? lines : List.of());
    }

    public void receive() {
        if (!status.canTransitionTo(ReturnStatus.RECEIVED)) {
            throw new InvalidShipmentReturnStateException(status, ReturnStatus.RECEIVED);
        }
        this.status = ReturnStatus.RECEIVED;
        this.receivedAt = Instant.now();

        // Build line data for event
        List<ShipmentReturnReceivedEvent.ReturnLineData> lineData = lines.stream()
                .map(l -> new ShipmentReturnReceivedEvent.ReturnLineData(
                        l.getItemId(), l.getQuantity(), l.getUnitCode()
                ))
                .toList();

        registerEvent(new ShipmentReturnReceivedEvent(
                getId(), tenantId, companyId,
                shipmentId, salesOrderId, warehouseId, lineData
        ));
    }

    public void complete() {
        if (!status.canTransitionTo(ReturnStatus.COMPLETED)) {
            throw new InvalidShipmentReturnStateException(status, ReturnStatus.COMPLETED);
        }
        this.status = ReturnStatus.COMPLETED;

        registerEvent(new ShipmentReturnCompletedEvent(
                getId(), tenantId, companyId, shipmentId, salesOrderId
        ));
    }

    public void cancel() {
        if (!status.canTransitionTo(ReturnStatus.CANCELLED)) {
            throw new InvalidShipmentReturnStateException(status, ReturnStatus.CANCELLED);
        }
        this.status = ReturnStatus.CANCELLED;

        registerEvent(new ShipmentReturnCancelledEvent(
                getId(), tenantId, companyId, shipmentId, salesOrderId
        ));
    }

    public BigDecimal getTotalReturnQty() {
        return lines.stream()
                .map(ShipmentReturnLine::getQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<ShipmentReturnLine> getLines() {
        return Collections.unmodifiableList(lines);
    }
}

