package tr.kontas.erp.shipment.domain.deliveryorder;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class DeliveryOrderLineId extends Identifier {

    private DeliveryOrderLineId(UUID value) {
        super(value);
    }

    public static DeliveryOrderLineId newId() {
        return new DeliveryOrderLineId(UUID.randomUUID());
    }

    public static DeliveryOrderLineId of(UUID value) {
        return new DeliveryOrderLineId(value);
    }

    public static DeliveryOrderLineId of(String value) {
        return new DeliveryOrderLineId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}

