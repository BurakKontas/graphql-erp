package tr.kontas.erp.shipment.domain.shipmentreturn;

public enum ReturnStatus {
    REQUESTED,
    RECEIVED,
    COMPLETED,
    CANCELLED;

    public boolean isTerminal() {
        return this == COMPLETED || this == CANCELLED;
    }

    public boolean canTransitionTo(ReturnStatus target) {
        return switch (this) {
            case REQUESTED -> target == RECEIVED || target == CANCELLED;
            case RECEIVED -> target == COMPLETED;
            case COMPLETED, CANCELLED -> false;
        };
    }
}

