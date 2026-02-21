package tr.kontas.erp.app.crm.loaders;

import com.netflix.graphql.dgs.DgsDataLoader;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.dataloader.BatchLoader;
import tr.kontas.erp.app.crm.dtos.ContactPayload;
import tr.kontas.erp.app.crm.graphql.CrmGraphql;
import tr.kontas.erp.crm.application.contact.GetContactsByIdsUseCase;
import tr.kontas.erp.crm.domain.contact.ContactId;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@DgsDataLoader(name = "crmContactLoader")
@RequiredArgsConstructor
public class CrmContactDataLoader implements BatchLoader<String, ContactPayload> {

    private final GetContactsByIdsUseCase getContactsByIdsUseCase;

    @NonNull
    @Override
    public CompletionStage<List<ContactPayload>> load(@NonNull List<String> ids) {
        List<ContactId> contactIds = ids.stream().map(ContactId::of).toList();
        List<ContactPayload> result = getContactsByIdsUseCase.execute(contactIds)
                .stream().map(CrmGraphql::toPayload).toList();
        return CompletableFuture.completedFuture(result);
    }
}

