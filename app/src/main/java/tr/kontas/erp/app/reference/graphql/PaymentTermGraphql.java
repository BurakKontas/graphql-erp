package tr.kontas.erp.app.reference.graphql;

import com.netflix.graphql.dgs.*;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import tr.kontas.erp.app.company.dtos.CompanyPayload;
import tr.kontas.erp.app.reference.dtos.CreatePaymentTermInput;
import tr.kontas.erp.app.reference.dtos.PaymentTermPayload;
import tr.kontas.erp.core.application.reference.payment.CreatePaymentTermCommand;
import tr.kontas.erp.core.application.reference.payment.CreatePaymentTermUseCase;
import tr.kontas.erp.core.application.reference.payment.GetPaymentTermsByCompanyUseCase;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.reference.payment.PaymentTerm;
import tr.kontas.erp.core.domain.reference.payment.PaymentTermCode;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@DgsComponent
@RequiredArgsConstructor
public class PaymentTermGraphql {

    private final CreatePaymentTermUseCase createPaymentTermUseCase;
    private final GetPaymentTermsByCompanyUseCase getPaymentTermsByCompanyUseCase;

    public static PaymentTermPayload toPayload(PaymentTerm pt) {
        return new PaymentTermPayload(
                pt.getId().getValue(),
                pt.getName(),
                pt.getDueDays(),
                pt.isActive(),
                pt.getCompanyId().asUUID().toString()
        );
    }

    @DgsMutation
    public PaymentTermPayload createPaymentTerm(@InputArgument("input") CreatePaymentTermInput input) {
        CompanyId companyId = CompanyId.of(input.getCompanyId());

        CreatePaymentTermCommand command = new CreatePaymentTermCommand(
                companyId,
                input.getCode(),
                input.getName(),
                input.getDueDays()
        );

        PaymentTermCode code = createPaymentTermUseCase.execute(command);

        return new PaymentTermPayload(
                code.getValue(),
                input.getName(),
                input.getDueDays(),
                true,
                input.getCompanyId()
        );
    }

    @DgsQuery
    public List<PaymentTermPayload> paymentTerms(@InputArgument("companyId") String id) {
        CompanyId companyId = CompanyId.of(id);

        return getPaymentTermsByCompanyUseCase.execute(companyId)
                .stream()
                .map(PaymentTermGraphql::toPayload)
                .toList();
    }

    @DgsData(parentType = "PaymentTermPayload")
    public CompletableFuture<CompanyPayload> company(DgsDataFetchingEnvironment dfe) {
        PaymentTermPayload paymentTerm = dfe.getSource();

        DataLoader<String, CompanyPayload> dataLoader = dfe.getDataLoader("companyLoader");

        if (dataLoader == null || paymentTerm == null) {
            return CompletableFuture.completedFuture(null);
        }

        return dataLoader.load(paymentTerm.getCompanyId());
    }
}
