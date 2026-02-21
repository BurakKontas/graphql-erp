package tr.kontas.erp.shipment.domain.exception;

import tr.kontas.erp.shipment.domain.deliveryorder.DeliveryOrderStatus;

public class InvalidDeliveryOrderStateException extends ShipmentDomainException {

    public InvalidDeliveryOrderStateException(DeliveryOrderStatus current, String operation) {
        super("Cannot perform '%s' on a DeliveryOrder in status '%s'".formatted(operation, current));
    }

    public InvalidDeliveryOrderStateException(DeliveryOrderStatus from, DeliveryOrderStatus to) {
        super("Invalid DeliveryOrder status transition: %s → %s".formatted(from, to));
    }
}

