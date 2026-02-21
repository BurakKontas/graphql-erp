package tr.kontas.erp.app.inventory.graphql;

import com.netflix.graphql.dgs.*;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import tr.kontas.erp.app.company.dtos.CompanyPayload;
import tr.kontas.erp.app.inventory.dtos.*;
import tr.kontas.erp.inventory.application.stocklevel.*;
import tr.kontas.erp.inventory.domain.item.ItemId;
import tr.kontas.erp.inventory.domain.stocklevel.StockLevel;
import tr.kontas.erp.inventory.domain.stocklevel.StockLevelId;
import tr.kontas.erp.inventory.domain.warehouse.WarehouseId;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@DgsComponent
@RequiredArgsConstructor
public class StockLevelGraphql {

    private final GetStockLevelByIdUseCase getStockLevelByIdUseCase;
    private final GetStockLevelsByWarehouseUseCase getStockLevelsByWarehouseUseCase;
    private final GetStockLevelsByItemUseCase getStockLevelsByItemUseCase;
    private final ReserveStockUseCase reserveStockUseCase;
    private final ReleaseReservationUseCase releaseReservationUseCase;
    private final AdjustStockUseCase adjustStockUseCase;
    private final SetReorderPointUseCase setReorderPointUseCase;

    public static StockLevelPayload toPayload(StockLevel sl) {
        return new StockLevelPayload(
                sl.getId().asUUID().toString(),
                sl.getCompanyId().asUUID().toString(),
                sl.getItemId().asUUID().toString(),
                sl.getWarehouseId().asUUID().toString(),
                sl.getQuantityOnHand(),
                sl.getQuantityReserved(),
                sl.getQuantityAvailable(),
                sl.getReorderPoint(),
                sl.isAllowNegativeStock()
        );
    }

    // ─── Queries ───

    @DgsQuery
    public StockLevelPayload stockLevel(@InputArgument("id") String id) {
        return toPayload(getStockLevelByIdUseCase.execute(StockLevelId.of(id)));
    }

    @DgsQuery
    public List<StockLevelPayload> stockLevelsByWarehouse(@InputArgument("warehouseId") String warehouseId) {
        return getStockLevelsByWarehouseUseCase.execute(WarehouseId.of(warehouseId))
                .stream().map(StockLevelGraphql::toPayload).toList();
    }

    @DgsQuery
    public List<StockLevelPayload> stockLevelsByItem(@InputArgument("itemId") String itemId) {
        return getStockLevelsByItemUseCase.execute(ItemId.of(itemId))
                .stream().map(StockLevelGraphql::toPayload).toList();
    }

    // ─── Mutations ───

    @DgsMutation
    public StockLevelPayload reserveStock(@InputArgument("input") ReserveStockInput input) {
        ReserveStockCommand cmd = new ReserveStockCommand(
                input.getItemId(),
                input.getWarehouseId(),
                input.getQuantity(),
                input.getReferenceId()
        );
        reserveStockUseCase.execute(cmd);
        return findStockLevelPayload(input.getItemId(), input.getWarehouseId());
    }

    @DgsMutation
    public StockLevelPayload releaseReservation(@InputArgument("input") ReleaseReservationInput input) {
        ReleaseReservationCommand cmd = new ReleaseReservationCommand(
                input.getItemId(),
                input.getWarehouseId(),
                input.getQuantity(),
                input.getReferenceId()
        );
        releaseReservationUseCase.execute(cmd);
        return findStockLevelPayload(input.getItemId(), input.getWarehouseId());
    }

    @DgsMutation
    public StockLevelPayload adjustStock(@InputArgument("input") AdjustStockInput input) {
        AdjustStockCommand cmd = new AdjustStockCommand(
                input.getItemId(),
                input.getWarehouseId(),
                input.getAdjustment()
        );
        adjustStockUseCase.execute(cmd);
        return findStockLevelPayload(input.getItemId(), input.getWarehouseId());
    }

    @DgsMutation
    public StockLevelPayload setReorderPoint(@InputArgument("input") SetReorderPointInput input) {
        setReorderPointUseCase.execute(input.getStockLevelId(), input.getReorderPoint());
        return toPayload(getStockLevelByIdUseCase.execute(StockLevelId.of(input.getStockLevelId())));
    }

    private StockLevelPayload findStockLevelPayload(String itemId, String warehouseId) {
        // After mutation, retrieve the stock levels for this item and find the matching one
        List<StockLevel> levels = getStockLevelsByItemUseCase.execute(ItemId.of(itemId));
        return levels.stream()
                .filter(sl -> sl.getWarehouseId().asUUID().toString().equals(warehouseId))
                .findFirst()
                .map(StockLevelGraphql::toPayload)
                .orElseThrow(() -> new IllegalStateException("StockLevel not found after mutation"));
    }

    // ─── Nested resolvers via DataLoaders ───

    @DgsData(parentType = "StockLevelPayload")
    public CompletableFuture<ItemPayload> item(DgsDataFetchingEnvironment dfe) {
        StockLevelPayload payload = dfe.getSource();
        DataLoader<String, ItemPayload> dataLoader = dfe.getDataLoader("itemLoader");
        if (dataLoader == null || payload.getItemId() == null) {
            return CompletableFuture.completedFuture(null);
        }
        return dataLoader.load(payload.getItemId());
    }

    @DgsData(parentType = "StockLevelPayload")
    public CompletableFuture<WarehousePayload> warehouse(DgsDataFetchingEnvironment dfe) {
        StockLevelPayload payload = dfe.getSource();
        DataLoader<String, WarehousePayload> dataLoader = dfe.getDataLoader("warehouseLoader");
        if (dataLoader == null || payload.getWarehouseId() == null) {
            return CompletableFuture.completedFuture(null);
        }
        return dataLoader.load(payload.getWarehouseId());
    }

    @DgsData(parentType = "StockLevelPayload")
    public CompletableFuture<CompanyPayload> company(DgsDataFetchingEnvironment dfe) {
        StockLevelPayload payload = dfe.getSource();
        DataLoader<String, CompanyPayload> dataLoader = dfe.getDataLoader("companyLoader");
        if (dataLoader == null || payload.getCompanyId() == null) {
            return CompletableFuture.completedFuture(null);
        }
        return dataLoader.load(payload.getCompanyId());
    }
}
