package tr.kontas.erp.app.inventory.loaders;

import com.netflix.graphql.dgs.DgsDataLoader;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.dataloader.BatchLoader;
import tr.kontas.erp.app.inventory.dtos.CategoryPayload;
import tr.kontas.erp.app.inventory.graphql.CategoryGraphql;
import tr.kontas.erp.inventory.application.category.GetCategoriesByIdsUseCase;
import tr.kontas.erp.inventory.domain.category.Category;
import tr.kontas.erp.inventory.domain.category.CategoryId;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Collectors;

@DgsDataLoader(name = "categoryLoader")
@RequiredArgsConstructor
public class CategoryDataLoader implements BatchLoader<String, CategoryPayload> {

    private final GetCategoriesByIdsUseCase getCategoriesByIdsUseCase;

    @NonNull
    @Override
    public CompletionStage<List<CategoryPayload>> load(@NonNull List<String> ids) {
        List<CategoryId> idList = ids.stream().map(CategoryId::of).toList();

        Map<String, Category> map = getCategoriesByIdsUseCase.execute(idList)
                .stream()
                .collect(Collectors.toMap(
                        c -> c.getId().asUUID().toString(),
                        Function.identity()
                ));

        List<CategoryPayload> result = ids.stream()
                .map(id -> {
                    Category c = map.get(id);
                    return c != null ? CategoryGraphql.toPayload(c) : null;
                })
                .toList();

        return CompletableFuture.completedFuture(result);
    }
}
