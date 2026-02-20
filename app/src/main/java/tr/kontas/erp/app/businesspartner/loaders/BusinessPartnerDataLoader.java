package tr.kontas.erp.app.businesspartner.loaders;

import com.netflix.graphql.dgs.DgsDataLoader;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.dataloader.BatchLoader;
import tr.kontas.erp.app.businesspartner.dtos.BusinessPartnerPayload;
import tr.kontas.erp.app.businesspartner.graphql.BusinessPartnerGraphql;
import tr.kontas.erp.core.application.businesspartner.GetBusinessPartnersByIdsUseCase;
import tr.kontas.erp.core.domain.businesspartner.BusinessPartner;
import tr.kontas.erp.core.domain.businesspartner.BusinessPartnerId;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@DgsDataLoader(name = "businessPartnerLoader")
@RequiredArgsConstructor
public class BusinessPartnerDataLoader implements BatchLoader<String, BusinessPartnerPayload> {

    private final GetBusinessPartnersByIdsUseCase getBusinessPartnersByIdsUseCase;

    @NonNull
    @Override
    public CompletionStage<List<BusinessPartnerPayload>> load(@NonNull List<String> businessPartnerIds) {
        List<BusinessPartnerId> idList = businessPartnerIds.stream()
                .map(BusinessPartnerId::of)
                .toList();

        List<BusinessPartner> businessPartners = getBusinessPartnersByIdsUseCase.execute(idList);

        List<BusinessPartnerPayload> result = businessPartners
                .stream()
                .map(BusinessPartnerGraphql::toPayload)
                .toList();

        return CompletableFuture.completedFuture(result);
    }
}
