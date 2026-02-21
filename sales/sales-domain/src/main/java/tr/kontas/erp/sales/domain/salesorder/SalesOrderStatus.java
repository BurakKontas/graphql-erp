package tr.kontas.erp.sales.domain.salesorder;

public enum SalesOrderStatus {
    DRAFT,
    NEEDS_ACTION,
    SENT,
    ACCEPTED,
    CONFIRMED,
    CLOSED,
    CANCELLED;

    public boolean isTerminal() {
        return this == CLOSED || this == CANCELLED;
    }

    public boolean allowsModification() {
        return this == DRAFT || this == NEEDS_ACTION;
    }

    public boolean canTransitionTo(SalesOrderStatus target) {
        return switch (this) {
            case DRAFT -> target == SENT || target == CONFIRMED || target == CANCELLED || target == NEEDS_ACTION;
            case NEEDS_ACTION -> target == DRAFT || target == CANCELLED;
            case SENT -> target == ACCEPTED || target == DRAFT || target == CANCELLED;
            case ACCEPTED -> target == CONFIRMED || target == CANCELLED;
            case CONFIRMED -> target == CLOSED || target == CANCELLED;
            case CLOSED, CANCELLED -> false;
        };
    }
}
