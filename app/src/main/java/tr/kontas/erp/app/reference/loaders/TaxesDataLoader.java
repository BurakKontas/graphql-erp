package tr.kontas.erp.app.reference.loaders;

import com.netflix.graphql.dgs.DgsDataLoader;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.dataloader.BatchLoader;
import tr.kontas.erp.app.reference.dtos.TaxPayload;
import tr.kontas.erp.app.reference.graphql.TaxGraphql;
import tr.kontas.erp.core.application.reference.tax.GetTaxesByCompanyIdsUseCase;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.reference.tax.Tax;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@DgsDataLoader(name = "taxesLoader")
@RequiredArgsConstructor
public class TaxesDataLoader implements BatchLoader<String, List<TaxPayload>> {

    private final GetTaxesByCompanyIdsUseCase getTaxesByCompanyIdsUseCase;

    @NonNull
    @Override
    public CompletionStage<List<List<TaxPayload>>> load(@NonNull List<String> companyIds) {
        List<CompanyId> companyIdList = companyIds.stream()
                .map(CompanyId::of)
                .toList();

        Map<CompanyId, List<Tax>> taxesByCompany =
                getTaxesByCompanyIdsUseCase.executeByCompanyIds(companyIdList);

        List<List<TaxPayload>> result = companyIdList.stream()
                .map(cid -> taxesByCompany.getOrDefault(cid, Collections.emptyList())
                        .stream()
                        .map(TaxGraphql::toPayload)
                        .toList()
                )
                .toList();

        return CompletableFuture.completedFuture(result);
    }
}
