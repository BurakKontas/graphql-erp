package tr.kontas.erp.shipment.domain.shipment;

public enum ShipmentStatus {
    PREPARING,
    DISPATCHED,
    DELIVERED;

    public boolean isTerminal() {
        return this == DELIVERED;
    }

    public boolean canTransitionTo(ShipmentStatus target) {
        return switch (this) {
            case PREPARING -> target == DISPATCHED;
            case DISPATCHED -> target == DELIVERED;
            case DELIVERED -> false;
        };
    }
}

