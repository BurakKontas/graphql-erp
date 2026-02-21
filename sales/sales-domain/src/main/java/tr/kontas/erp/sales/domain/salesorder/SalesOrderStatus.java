package tr.kontas.erp.sales.domain.salesorder;

public enum SalesOrderStatus {
    DRAFT,
    SENT,
    ACCEPTED,
    CONFIRMED,
    CLOSED,
    CANCELLED;

    public boolean isTerminal() {
        return this == CLOSED || this == CANCELLED;
    }

    public boolean allowsModification() {
        return this == DRAFT;
    }

    public boolean canTransitionTo(SalesOrderStatus target) {
        return switch (this) {
            case DRAFT -> target == SENT || target == CONFIRMED || target == CANCELLED;
            case SENT -> target == ACCEPTED || target == DRAFT || target == CANCELLED;
            case ACCEPTED -> target == CONFIRMED || target == CANCELLED;
            case CONFIRMED -> target == CLOSED || target == CANCELLED;
            case CLOSED, CANCELLED -> false;
        };
    }
}
