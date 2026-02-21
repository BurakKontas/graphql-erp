package tr.kontas.erp.app.inventory.graphql;

import com.netflix.graphql.dgs.*;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import tr.kontas.erp.app.company.dtos.CompanyPayload;
import tr.kontas.erp.app.inventory.dtos.*;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.inventory.application.item.*;
import tr.kontas.erp.inventory.domain.item.Item;
import tr.kontas.erp.inventory.domain.item.ItemId;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@DgsComponent
@RequiredArgsConstructor
public class ItemGraphql {

    private final CreateItemUseCase createItemUseCase;
    private final GetItemByIdUseCase getItemByIdUseCase;
    private final GetItemsByCompanyUseCase getItemsByCompanyUseCase;
    private final UpdateItemUseCase updateItemUseCase;
    private final DeactivateItemUseCase deactivateItemUseCase;
    private final ActivateItemUseCase activateItemUseCase;

    public static ItemPayload toPayload(Item i) {
        return new ItemPayload(
                i.getId().asUUID().toString(),
                i.getCompanyId().asUUID().toString(),
                i.getCode().getValue(),
                i.getName().getValue(),
                i.getType().name(),
                i.getUnit() != null ? i.getUnit().getId().getValue() : null,
                i.getCategoryId() != null ? i.getCategoryId().asUUID().toString() : null,
                i.isAllowNegativeStock(),
                i.isActive()
        );
    }

    // ─── Queries ───

    @DgsQuery
    public ItemPayload item(@InputArgument("id") String id) {
        Item i = getItemByIdUseCase.execute(ItemId.of(id));
        return toPayload(i);
    }

    @DgsQuery
    public List<ItemPayload> items(@InputArgument("companyId") String companyId) {
        return getItemsByCompanyUseCase.execute(CompanyId.of(companyId))
                .stream().map(ItemGraphql::toPayload).toList();
    }

    // ─── Mutations ───

    @DgsMutation
    public ItemPayload createItem(@InputArgument("input") CreateItemInput input) {
        CreateItemCommand cmd = new CreateItemCommand(
                CompanyId.of(input.getCompanyId()),
                input.getCode(),
                input.getName(),
                input.getType(),
                input.getUnitCode(),
                input.getCategoryId(),
                input.getAllowNegativeStock() != null && input.getAllowNegativeStock()
        );
        ItemId id = createItemUseCase.execute(cmd);
        return toPayload(getItemByIdUseCase.execute(id));
    }

    @DgsMutation
    public ItemPayload updateItem(@InputArgument("input") UpdateItemInput input) {
        UpdateItemCommand cmd = new UpdateItemCommand(
                input.getItemId(),
                input.getName(),
                input.getUnitCode(),
                input.getCategoryId()
        );
        updateItemUseCase.execute(cmd);
        return toPayload(getItemByIdUseCase.execute(ItemId.of(input.getItemId())));
    }

    @DgsMutation
    public ItemPayload deactivateItem(@InputArgument("itemId") String itemId) {
        deactivateItemUseCase.deactivate(itemId);
        return toPayload(getItemByIdUseCase.execute(ItemId.of(itemId)));
    }

    @DgsMutation
    public ItemPayload activateItem(@InputArgument("itemId") String itemId) {
        activateItemUseCase.activate(itemId);
        return toPayload(getItemByIdUseCase.execute(ItemId.of(itemId)));
    }

    // ─── Nested resolvers ───

    @DgsData(parentType = "ItemPayload")
    public CompletableFuture<CompanyPayload> company(DgsDataFetchingEnvironment dfe) {
        ItemPayload payload = dfe.getSource();
        DataLoader<String, CompanyPayload> dataLoader = dfe.getDataLoader("companyLoader");
        if (dataLoader == null || payload.getCompanyId() == null) {
            return CompletableFuture.completedFuture(null);
        }
        return dataLoader.load(payload.getCompanyId());
    }

    @DgsData(parentType = "ItemPayload")
    public CompletableFuture<CategoryPayload> category(DgsDataFetchingEnvironment dfe) {
        ItemPayload payload = dfe.getSource();
        DataLoader<String, CategoryPayload> dataLoader = dfe.getDataLoader("categoryLoader");
        if (dataLoader == null || payload.getCategoryId() == null) {
            return CompletableFuture.completedFuture(null);
        }
        return dataLoader.load(payload.getCategoryId());
    }
}
