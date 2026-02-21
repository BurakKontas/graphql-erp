package tr.kontas.erp.shipment.application.shipmentreturn;

import tr.kontas.erp.shipment.domain.shipmentreturn.ShipmentReturnId;

public interface CreateShipmentReturnUseCase {
    ShipmentReturnId execute(CreateShipmentReturnCommand command);
}

