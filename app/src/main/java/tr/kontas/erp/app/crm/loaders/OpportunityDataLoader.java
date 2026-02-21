package tr.kontas.erp.app.crm.loaders;

import com.netflix.graphql.dgs.DgsDataLoader;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.dataloader.BatchLoader;
import tr.kontas.erp.app.crm.dtos.OpportunityPayload;
import tr.kontas.erp.app.crm.graphql.CrmGraphql;
import tr.kontas.erp.crm.application.opportunity.GetOpportunitiesByIdsUseCase;
import tr.kontas.erp.crm.domain.opportunity.OpportunityId;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@DgsDataLoader(name = "opportunityLoader")
@RequiredArgsConstructor
public class OpportunityDataLoader implements BatchLoader<String, OpportunityPayload> {

    private final GetOpportunitiesByIdsUseCase getOpportunitiesByIdsUseCase;

    @NonNull
    @Override
    public CompletionStage<List<OpportunityPayload>> load(@NonNull List<String> ids) {
        List<OpportunityId> oppIds = ids.stream().map(OpportunityId::of).toList();
        List<OpportunityPayload> result = getOpportunitiesByIdsUseCase.execute(oppIds)
                .stream().map(CrmGraphql::toPayload).toList();
        return CompletableFuture.completedFuture(result);
    }
}

