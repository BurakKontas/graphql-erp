package tr.kontas.erp.app.tenant.graphql;

import com.netflix.graphql.dgs.*;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import tr.kontas.erp.app.company.dtos.CompanyPayload;
import tr.kontas.erp.app.tenant.dtos.CreateTenantInput;
import tr.kontas.erp.app.tenant.dtos.TenantPayload;
import tr.kontas.erp.core.application.tenant.CreateTenantCommand;
import tr.kontas.erp.core.application.tenant.CreateTenantUseCase;
import tr.kontas.erp.core.application.tenant.GetTenantByIdUseCase;
import tr.kontas.erp.core.application.tenant.GetTenantsUseCase;
import tr.kontas.erp.core.domain.tenant.Tenant;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@DgsComponent
@RequiredArgsConstructor
public class TenantGraphql {

    private final CreateTenantUseCase createTenantUseCase;
    private final GetTenantsUseCase getTenantsUseCase;
    private final GetTenantByIdUseCase getTenantByIdUseCase;

    @DgsMutation
    public TenantPayload createTenant(@InputArgument("input") CreateTenantInput input) {

        CreateTenantCommand command = new CreateTenantCommand(input.getName(), input.getCode());

        TenantId tenantId = createTenantUseCase.execute(command);

        return new TenantPayload(
                tenantId.asUUID().toString(),
                input.getName(),
                input.getCode()
        );
    }

    public static TenantPayload toPayload(Tenant domain) {
        return new TenantPayload(
                domain.getId().asUUID().toString(),
                domain.getName().getValue(),
                domain.getCode().getValue()
        );
    }

    @DgsQuery
    public List<TenantPayload> tenants() {
        return getTenantsUseCase.execute()
                .stream()
                .map(TenantGraphql::toPayload)
                .toList();
    }

    @DgsQuery
    public TenantPayload tenant(@InputArgument("id") String id) {
        TenantId tenantId = TenantId.of(id);
        return toPayload(getTenantByIdUseCase.execute(tenantId).orElseThrow());
    }

    @DgsEntityFetcher(name = "TenantPayload")
    public TenantPayload tenant(Map<String, Object> values) {
        String id = values.get("id").toString();
        return tenant(id);
    }

    @DgsData(parentType = "TenantPayload")
    public CompletableFuture<List<CompanyPayload>> companies(DgsDataFetchingEnvironment dfe) {
        TenantPayload employee = dfe.getSource();

        DataLoader<String, List<CompanyPayload>> dataLoader = dfe.getDataLoader("companiesByTenantDataLoader");

        if (dataLoader == null || employee == null) {
            return CompletableFuture.completedFuture(List.of());
        }

        return dataLoader.load(employee.getId());
    }
}
