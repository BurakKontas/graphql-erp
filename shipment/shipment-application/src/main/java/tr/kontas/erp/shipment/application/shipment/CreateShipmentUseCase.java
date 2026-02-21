package tr.kontas.erp.shipment.application.shipment;

import tr.kontas.erp.shipment.domain.shipment.ShipmentId;

public interface CreateShipmentUseCase {
    ShipmentId execute(CreateShipmentCommand command);
}

