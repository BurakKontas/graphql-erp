package tr.kontas.erp.shipment.application.shipment;

import tr.kontas.erp.shipment.domain.shipment.Shipment;
import tr.kontas.erp.shipment.domain.shipment.ShipmentId;

public interface GetShipmentByIdUseCase {
    Shipment execute(ShipmentId id);
}

