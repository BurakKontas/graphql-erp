package tr.kontas.erp.app.inventory.graphql;

import com.netflix.graphql.dgs.*;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import tr.kontas.erp.app.company.dtos.CompanyPayload;
import tr.kontas.erp.app.inventory.dtos.*;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.inventory.application.warehouse.*;
import tr.kontas.erp.inventory.domain.warehouse.Warehouse;
import tr.kontas.erp.inventory.domain.warehouse.WarehouseId;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@DgsComponent
@RequiredArgsConstructor
public class WarehouseGraphql {

    private final CreateWarehouseUseCase createWarehouseUseCase;
    private final GetWarehouseByIdUseCase getWarehouseByIdUseCase;
    private final GetWarehousesByCompanyUseCase getWarehousesByCompanyUseCase;
    private final RenameWarehouseUseCase renameWarehouseUseCase;
    private final DeactivateWarehouseUseCase deactivateWarehouseUseCase;
    private final ActivateWarehouseUseCase activateWarehouseUseCase;

    public static WarehousePayload toPayload(Warehouse w) {
        return new WarehousePayload(
                w.getId().asUUID().toString(),
                w.getCompanyId().asUUID().toString(),
                w.getCode().getValue(),
                w.getName(),
                w.isActive()
        );
    }

    // ─── Queries ───

    @DgsQuery
    public WarehousePayload warehouse(@InputArgument("id") String id) {
        Warehouse w = getWarehouseByIdUseCase.execute(WarehouseId.of(id));
        return toPayload(w);
    }

    @DgsQuery
    public List<WarehousePayload> warehouses(@InputArgument("companyId") String companyId) {
        return getWarehousesByCompanyUseCase.execute(CompanyId.of(companyId))
                .stream().map(WarehouseGraphql::toPayload).toList();
    }

    // ─── Mutations ───

    @DgsMutation
    public WarehousePayload createWarehouse(@InputArgument("input") CreateWarehouseInput input) {
        CreateWarehouseCommand cmd = new CreateWarehouseCommand(
                CompanyId.of(input.getCompanyId()),
                input.getCode(),
                input.getName()
        );
        WarehouseId id = createWarehouseUseCase.execute(cmd);
        return toPayload(getWarehouseByIdUseCase.execute(id));
    }

    @DgsMutation
    public WarehousePayload renameWarehouse(@InputArgument("input") RenameWarehouseInput input) {
        renameWarehouseUseCase.execute(input.getWarehouseId(), input.getName());
        return toPayload(getWarehouseByIdUseCase.execute(WarehouseId.of(input.getWarehouseId())));
    }

    @DgsMutation
    public WarehousePayload deactivateWarehouse(@InputArgument("warehouseId") String warehouseId) {
        deactivateWarehouseUseCase.deactivate(warehouseId);
        return toPayload(getWarehouseByIdUseCase.execute(WarehouseId.of(warehouseId)));
    }

    @DgsMutation
    public WarehousePayload activateWarehouse(@InputArgument("warehouseId") String warehouseId) {
        activateWarehouseUseCase.activate(warehouseId);
        return toPayload(getWarehouseByIdUseCase.execute(WarehouseId.of(warehouseId)));
    }

    // ─── Nested resolvers ───

    @DgsData(parentType = "WarehousePayload")
    public CompletableFuture<CompanyPayload> company(DgsDataFetchingEnvironment dfe) {
        WarehousePayload payload = dfe.getSource();
        DataLoader<String, CompanyPayload> dataLoader = dfe.getDataLoader("companyLoader");
        if (dataLoader == null || payload.getCompanyId() == null) {
            return CompletableFuture.completedFuture(null);
        }
        return dataLoader.load(payload.getCompanyId());
    }
}
