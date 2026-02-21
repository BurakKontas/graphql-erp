package tr.kontas.erp.inventory.application.category;

public interface RenameCategoryUseCase {
    void execute(String categoryId, String newName);
}
