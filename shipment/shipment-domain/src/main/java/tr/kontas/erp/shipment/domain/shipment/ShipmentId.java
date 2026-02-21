package tr.kontas.erp.shipment.domain.shipment;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class ShipmentId extends Identifier {

    private ShipmentId(UUID value) {
        super(value);
    }

    public static ShipmentId newId() {
        return new ShipmentId(UUID.randomUUID());
    }

    public static ShipmentId of(UUID value) {
        return new ShipmentId(value);
    }

    public static ShipmentId of(String value) {
        return new ShipmentId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}

