package tr.kontas.erp.shipment.domain.deliveryorder;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class DeliveryOrderId extends Identifier {

    private DeliveryOrderId(UUID value) {
        super(value);
    }

    public static DeliveryOrderId newId() {
        return new DeliveryOrderId(UUID.randomUUID());
    }

    public static DeliveryOrderId of(UUID value) {
        return new DeliveryOrderId(value);
    }

    public static DeliveryOrderId of(String value) {
        return new DeliveryOrderId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}

