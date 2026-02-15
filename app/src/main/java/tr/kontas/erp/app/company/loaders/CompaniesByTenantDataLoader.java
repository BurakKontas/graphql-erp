package tr.kontas.erp.app.company.loaders;

import com.netflix.graphql.dgs.DgsDataLoader;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.dataloader.BatchLoader;
import tr.kontas.erp.app.company.dtos.CompanyPayload;
import tr.kontas.erp.app.company.graphql.CompanyGraphql;
import tr.kontas.erp.core.application.company.GetCompaniesByTenantIdsUseCase;
import tr.kontas.erp.core.domain.company.Company;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@DgsDataLoader(name = "companiesByTenantDataLoader")
@RequiredArgsConstructor
public class CompaniesByTenantDataLoader implements BatchLoader<String, List<CompanyPayload>> {

    private final GetCompaniesByTenantIdsUseCase getCompaniesByTenantIdsUseCase;

    @NonNull
    @Override
    public CompletionStage<List<List<CompanyPayload>>> load(@NonNull List<String> tenantIds) {
        List<TenantId> tenantIdList = tenantIds.stream()
                .map(TenantId::of)
                .toList();

        Map<TenantId, List<Company>> companiesByTenant = getCompaniesByTenantIdsUseCase.executeByTenantIds(tenantIdList);

        List<List<CompanyPayload>> result = tenantIdList.stream()
                .map(cid -> companiesByTenant.getOrDefault(cid, Collections.emptyList())
                        .stream()
                        .map(CompanyGraphql::toPayload)
                        .toList()
                )
                .toList();

        return CompletableFuture.completedFuture(result);
    }
}
