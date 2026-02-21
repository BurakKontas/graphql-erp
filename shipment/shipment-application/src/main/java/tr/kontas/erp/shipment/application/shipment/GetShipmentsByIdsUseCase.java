package tr.kontas.erp.shipment.application.shipment;

import tr.kontas.erp.shipment.domain.shipment.Shipment;
import tr.kontas.erp.shipment.domain.shipment.ShipmentId;

import java.util.List;

public interface GetShipmentsByIdsUseCase {
    List<Shipment> execute(List<ShipmentId> ids);
}

