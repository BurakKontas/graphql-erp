package tr.kontas.erp.app.inventory.loaders;

import com.netflix.graphql.dgs.DgsDataLoader;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.dataloader.BatchLoader;
import tr.kontas.erp.app.inventory.dtos.ItemPayload;
import tr.kontas.erp.app.inventory.graphql.ItemGraphql;
import tr.kontas.erp.inventory.application.item.GetItemsByIdsUseCase;
import tr.kontas.erp.inventory.domain.item.Item;
import tr.kontas.erp.inventory.domain.item.ItemId;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Collectors;

@DgsDataLoader(name = "itemLoader")
@RequiredArgsConstructor
public class ItemDataLoader implements BatchLoader<String, ItemPayload> {

    private final GetItemsByIdsUseCase getItemsByIdsUseCase;

    @NonNull
    @Override
    public CompletionStage<List<ItemPayload>> load(@NonNull List<String> ids) {
        List<ItemId> idList = ids.stream().map(ItemId::of).toList();

        Map<String, Item> map = getItemsByIdsUseCase.execute(idList)
                .stream()
                .collect(Collectors.toMap(
                        i -> i.getId().asUUID().toString(),
                        Function.identity()
                ));

        List<ItemPayload> result = ids.stream()
                .map(id -> {
                    Item i = map.get(id);
                    return i != null ? ItemGraphql.toPayload(i) : null;
                })
                .toList();

        return CompletableFuture.completedFuture(result);
    }
}
