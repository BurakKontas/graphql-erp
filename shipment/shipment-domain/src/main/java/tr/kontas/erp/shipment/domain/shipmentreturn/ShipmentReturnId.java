package tr.kontas.erp.shipment.domain.shipmentreturn;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class ShipmentReturnId extends Identifier {

    private ShipmentReturnId(UUID value) {
        super(value);
    }

    public static ShipmentReturnId newId() {
        return new ShipmentReturnId(UUID.randomUUID());
    }

    public static ShipmentReturnId of(UUID value) {
        return new ShipmentReturnId(value);
    }

    public static ShipmentReturnId of(String value) {
        return new ShipmentReturnId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}

