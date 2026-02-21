package tr.kontas.erp.shipment.domain.shipment;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class ShipmentLineId extends Identifier {

    private ShipmentLineId(UUID value) {
        super(value);
    }

    public static ShipmentLineId newId() {
        return new ShipmentLineId(UUID.randomUUID());
    }

    public static ShipmentLineId of(UUID value) {
        return new ShipmentLineId(value);
    }

    public static ShipmentLineId of(String value) {
        return new ShipmentLineId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}

