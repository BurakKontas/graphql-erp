package tr.kontas.erp.shipment.domain.exception;

import tr.kontas.erp.shipment.domain.shipmentreturn.ReturnStatus;

public class InvalidShipmentReturnStateException extends ShipmentDomainException {

    public InvalidShipmentReturnStateException(ReturnStatus current, String operation) {
        super("Cannot perform '%s' on a ShipmentReturn in status '%s'".formatted(operation, current));
    }

    public InvalidShipmentReturnStateException(ReturnStatus from, ReturnStatus to) {
        super("Invalid ShipmentReturn status transition: %s → %s".formatted(from, to));
    }
}

