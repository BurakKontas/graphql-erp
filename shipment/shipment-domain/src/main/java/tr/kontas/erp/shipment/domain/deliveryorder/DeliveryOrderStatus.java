package tr.kontas.erp.shipment.domain.deliveryorder;

public enum DeliveryOrderStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED;

    public boolean isTerminal() {
        return this == COMPLETED || this == CANCELLED;
    }

    public boolean allowsModification() {
        return this == PENDING;
    }

    public boolean canTransitionTo(DeliveryOrderStatus target) {
        return switch (this) {
            case PENDING -> target == IN_PROGRESS || target == CANCELLED;
            case IN_PROGRESS -> target == COMPLETED || target == CANCELLED;
            case COMPLETED, CANCELLED -> false;
        };
    }
}

