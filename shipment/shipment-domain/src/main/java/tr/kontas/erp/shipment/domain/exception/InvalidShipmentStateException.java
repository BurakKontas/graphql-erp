package tr.kontas.erp.shipment.domain.exception;

import tr.kontas.erp.shipment.domain.shipment.ShipmentStatus;

public class InvalidShipmentStateException extends ShipmentDomainException {

    public InvalidShipmentStateException(ShipmentStatus current, String operation) {
        super("Cannot perform '%s' on a Shipment in status '%s'".formatted(operation, current));
    }

    public InvalidShipmentStateException(ShipmentStatus from, ShipmentStatus to) {
        super("Invalid Shipment status transition: %s → %s".formatted(from, to));
    }
}

