package tr.kontas.erp.shipment.domain.shipmentreturn;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class ShipmentReturnLineId extends Identifier {

    private ShipmentReturnLineId(UUID value) {
        super(value);
    }

    public static ShipmentReturnLineId newId() {
        return new ShipmentReturnLineId(UUID.randomUUID());
    }

    public static ShipmentReturnLineId of(UUID value) {
        return new ShipmentReturnLineId(value);
    }

    public static ShipmentReturnLineId of(String value) {
        return new ShipmentReturnLineId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}

