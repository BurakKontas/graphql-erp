package tr.kontas.erp.shipment.application.shipmentreturn;

import tr.kontas.erp.shipment.domain.shipmentreturn.ShipmentReturn;
import tr.kontas.erp.shipment.domain.shipmentreturn.ShipmentReturnId;

public interface GetShipmentReturnByIdUseCase {
    ShipmentReturn execute(ShipmentReturnId id);
}

