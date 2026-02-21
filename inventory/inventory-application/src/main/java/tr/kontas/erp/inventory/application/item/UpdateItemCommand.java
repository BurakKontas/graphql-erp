package tr.kontas.erp.inventory.application.item;

public record UpdateItemCommand(
        String itemId,
        String name,
        String unitCode,
        String categoryId
) {
}
