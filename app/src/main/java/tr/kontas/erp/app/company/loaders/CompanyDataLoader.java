package tr.kontas.erp.app.company.loaders;

import com.netflix.graphql.dgs.DgsDataLoader;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.dataloader.BatchLoader;
import tr.kontas.erp.app.company.dtos.CompanyPayload;
import tr.kontas.erp.app.company.graphql.CompanyGraphql;
import tr.kontas.erp.core.application.company.GetCompaniesByIdsUseCase;
import tr.kontas.erp.core.domain.company.CompanyId;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@DgsDataLoader(name = "companyLoader")
@RequiredArgsConstructor
public class CompanyDataLoader implements BatchLoader<String, CompanyPayload> {

    private final GetCompaniesByIdsUseCase getCompaniesByIdsUseCase;

    @NonNull
    @Override
    public CompletionStage<List<CompanyPayload>> load(@NonNull List<String> companyIds) {
        List<CompanyId> companyIdList = companyIds.stream()
                .map(CompanyId::of)
                .toList();

        List<CompanyPayload> result = getCompaniesByIdsUseCase.execute(companyIdList)
                .stream()
                .map(CompanyGraphql::toPayload)
                .toList();

        return CompletableFuture.completedFuture(result);
    }
}
