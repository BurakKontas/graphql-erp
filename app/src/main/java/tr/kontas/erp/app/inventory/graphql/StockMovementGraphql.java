package tr.kontas.erp.app.inventory.graphql;

import com.netflix.graphql.dgs.*;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import tr.kontas.erp.app.company.dtos.CompanyPayload;
import tr.kontas.erp.app.inventory.dtos.*;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.inventory.application.stockmovement.*;
import tr.kontas.erp.inventory.domain.item.ItemId;
import tr.kontas.erp.inventory.domain.stockmovement.StockMovement;
import tr.kontas.erp.inventory.domain.stockmovement.StockMovementId;
import tr.kontas.erp.inventory.domain.warehouse.WarehouseId;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@DgsComponent
@RequiredArgsConstructor
public class StockMovementGraphql {

    private final RecordStockMovementUseCase recordStockMovementUseCase;
    private final GetStockMovementsByWarehouseUseCase getStockMovementsByWarehouseUseCase;
    private final GetStockMovementsByItemUseCase getStockMovementsByItemUseCase;

    public static StockMovementPayload toPayload(StockMovement sm) {
        return new StockMovementPayload(
                sm.getId().asUUID().toString(),
                sm.getCompanyId().asUUID().toString(),
                sm.getItemId().asUUID().toString(),
                sm.getWarehouseId().asUUID().toString(),
                sm.getMovementType().name(),
                sm.getQuantity(),
                sm.getReferenceType().name(),
                sm.getReferenceId(),
                sm.getNote(),
                sm.getMovementDate().toString(),
                sm.getCreatedAt().toString()
        );
    }

    // ─── Queries ───

    @DgsQuery
    public List<StockMovementPayload> stockMovementsByWarehouse(@InputArgument("warehouseId") String warehouseId) {
        return getStockMovementsByWarehouseUseCase.execute(WarehouseId.of(warehouseId))
                .stream().map(StockMovementGraphql::toPayload).toList();
    }

    @DgsQuery
    public List<StockMovementPayload> stockMovementsByItem(@InputArgument("itemId") String itemId) {
        return getStockMovementsByItemUseCase.execute(ItemId.of(itemId))
                .stream().map(StockMovementGraphql::toPayload).toList();
    }

    // ─── Mutations ───

    @DgsMutation
    public StockMovementPayload recordStockMovement(@InputArgument("input") RecordStockMovementInput input) {
        RecordStockMovementCommand cmd = new RecordStockMovementCommand(
                CompanyId.of(input.getCompanyId()),
                input.getItemId(),
                input.getWarehouseId(),
                input.getMovementType(),
                input.getQuantity(),
                input.getReferenceType(),
                input.getReferenceId(),
                input.getNote(),
                input.getMovementDate() != null ? LocalDate.parse(input.getMovementDate()) : null
        );
        StockMovementId id = recordStockMovementUseCase.execute(cmd);

        // Find the created movement from the item's movements
        return getStockMovementsByItemUseCase.execute(ItemId.of(input.getItemId()))
                .stream()
                .filter(sm -> sm.getId().asUUID().toString().equals(id.asUUID().toString()))
                .findFirst()
                .map(StockMovementGraphql::toPayload)
                .orElseThrow(() -> new IllegalStateException("StockMovement not found after creation"));
    }

    // ─── Nested resolvers via DataLoaders ───

    @DgsData(parentType = "StockMovementPayload")
    public CompletableFuture<ItemPayload> item(DgsDataFetchingEnvironment dfe) {
        StockMovementPayload payload = dfe.getSource();
        DataLoader<String, ItemPayload> dataLoader = dfe.getDataLoader("itemLoader");
        if (dataLoader == null || payload.getItemId() == null) {
            return CompletableFuture.completedFuture(null);
        }
        return dataLoader.load(payload.getItemId());
    }

    @DgsData(parentType = "StockMovementPayload")
    public CompletableFuture<WarehousePayload> warehouse(DgsDataFetchingEnvironment dfe) {
        StockMovementPayload payload = dfe.getSource();
        DataLoader<String, WarehousePayload> dataLoader = dfe.getDataLoader("warehouseLoader");
        if (dataLoader == null || payload.getWarehouseId() == null) {
            return CompletableFuture.completedFuture(null);
        }
        return dataLoader.load(payload.getWarehouseId());
    }

    @DgsData(parentType = "StockMovementPayload")
    public CompletableFuture<CompanyPayload> company(DgsDataFetchingEnvironment dfe) {
        StockMovementPayload payload = dfe.getSource();
        DataLoader<String, CompanyPayload> dataLoader = dfe.getDataLoader("companyLoader");
        if (dataLoader == null || payload.getCompanyId() == null) {
            return CompletableFuture.completedFuture(null);
        }
        return dataLoader.load(payload.getCompanyId());
    }
}
