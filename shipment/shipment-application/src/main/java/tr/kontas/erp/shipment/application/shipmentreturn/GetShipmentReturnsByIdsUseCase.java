package tr.kontas.erp.shipment.application.shipmentreturn;

import tr.kontas.erp.shipment.domain.shipmentreturn.ShipmentReturn;
import tr.kontas.erp.shipment.domain.shipmentreturn.ShipmentReturnId;

import java.util.List;

public interface GetShipmentReturnsByIdsUseCase {
    List<ShipmentReturn> execute(List<ShipmentReturnId> ids);
}

