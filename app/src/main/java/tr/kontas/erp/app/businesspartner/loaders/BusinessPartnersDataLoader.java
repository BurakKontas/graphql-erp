package tr.kontas.erp.app.businesspartner.loaders;

import com.netflix.graphql.dgs.DgsDataLoader;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.dataloader.BatchLoader;
import tr.kontas.erp.app.businesspartner.dtos.BusinessPartnerPayload;
import tr.kontas.erp.app.businesspartner.graphql.BusinessPartnerGraphql;
import tr.kontas.erp.core.application.businesspartner.GetBusinessPartnersByCompanyIdsUseCase;
import tr.kontas.erp.core.domain.businesspartner.BusinessPartner;
import tr.kontas.erp.core.domain.company.CompanyId;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@DgsDataLoader(name = "businessPartnersLoader")
@RequiredArgsConstructor
public class BusinessPartnersDataLoader implements BatchLoader<String, List<BusinessPartnerPayload>> {

    private final GetBusinessPartnersByCompanyIdsUseCase getBusinessPartnersByCompanyIdsUseCase;

    @NonNull
    @Override
    public CompletionStage<List<List<BusinessPartnerPayload>>> load(@NonNull List<String> companyIds) {
        List<CompanyId> companyIdList = companyIds.stream()
                .map(CompanyId::of)
                .toList();

        Map<CompanyId, List<BusinessPartner>> businessPartnersByCompany = getBusinessPartnersByCompanyIdsUseCase.executeByCompanyIds(companyIdList);

        List<List<BusinessPartnerPayload>> result = companyIdList.stream()
                .map(cid -> businessPartnersByCompany.getOrDefault(cid, Collections.emptyList())
                        .stream()
                        .map(BusinessPartnerGraphql::toPayload)
                        .toList()
                )
                .toList();

        return CompletableFuture.completedFuture(result);
    }
}
