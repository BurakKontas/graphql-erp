package tr.kontas.erp.app.reference.loaders;

import com.netflix.graphql.dgs.DgsDataLoader;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.dataloader.BatchLoader;
import tr.kontas.erp.app.reference.dtos.PaymentTermPayload;
import tr.kontas.erp.app.reference.graphql.PaymentTermGraphql;
import tr.kontas.erp.core.application.reference.payment.GetPaymentTermsByCompanyIdsUseCase;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.reference.payment.PaymentTerm;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@DgsDataLoader(name = "paymentTermsLoader")
@RequiredArgsConstructor
public class PaymentTermsDataLoader implements BatchLoader<String, List<PaymentTermPayload>> {

    private final GetPaymentTermsByCompanyIdsUseCase getPaymentTermsByCompanyIdsUseCase;

    @NonNull
    @Override
    public CompletionStage<List<List<PaymentTermPayload>>> load(@NonNull List<String> companyIds) {
        List<CompanyId> companyIdList = companyIds.stream()
                .map(CompanyId::of)
                .toList();

        Map<CompanyId, List<PaymentTerm>> paymentTermsByCompany =
                getPaymentTermsByCompanyIdsUseCase.executeByCompanyIds(companyIdList);

        List<List<PaymentTermPayload>> result = companyIdList.stream()
                .map(cid -> paymentTermsByCompany.getOrDefault(cid, Collections.emptyList())
                        .stream()
                        .map(PaymentTermGraphql::toPayload)
                        .toList()
                )
                .toList();

        return CompletableFuture.completedFuture(result);
    }
}
