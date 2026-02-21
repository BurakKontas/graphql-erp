package tr.kontas.erp.inventory.domain.stockmovement;

public enum MovementType {
    RECEIPT,
    ISSUE,
    TRANSFER,
    ADJUSTMENT;

    public boolean increasesStock() {
        return this == RECEIPT || this == ADJUSTMENT;
    }
}