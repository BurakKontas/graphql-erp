package tr.kontas.erp.app.inventory.loaders;

import com.netflix.graphql.dgs.DgsDataLoader;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.dataloader.BatchLoader;
import tr.kontas.erp.app.inventory.dtos.WarehousePayload;
import tr.kontas.erp.app.inventory.graphql.WarehouseGraphql;
import tr.kontas.erp.inventory.application.warehouse.GetWarehousesByIdsUseCase;
import tr.kontas.erp.inventory.domain.warehouse.Warehouse;
import tr.kontas.erp.inventory.domain.warehouse.WarehouseId;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Collectors;

@DgsDataLoader(name = "warehouseLoader")
@RequiredArgsConstructor
public class WarehouseDataLoader implements BatchLoader<String, WarehousePayload> {

    private final GetWarehousesByIdsUseCase getWarehousesByIdsUseCase;

    @NonNull
    @Override
    public CompletionStage<List<WarehousePayload>> load(@NonNull List<String> ids) {
        List<WarehouseId> idList = ids.stream().map(WarehouseId::of).toList();

        Map<String, Warehouse> map = getWarehousesByIdsUseCase.execute(idList)
                .stream()
                .collect(Collectors.toMap(
                        w -> w.getId().asUUID().toString(),
                        Function.identity()
                ));

        List<WarehousePayload> result = ids.stream()
                .map(id -> {
                    Warehouse w = map.get(id);
                    return w != null ? WarehouseGraphql.toPayload(w) : null;
                })
                .toList();

        return CompletableFuture.completedFuture(result);
    }
}
