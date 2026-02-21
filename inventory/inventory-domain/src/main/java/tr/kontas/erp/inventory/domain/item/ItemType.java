package tr.kontas.erp.inventory.domain.item;

public enum ItemType {
    PHYSICAL,
    SERVICE;

    public boolean isStockTracked() {
        return this == PHYSICAL;
    }
}