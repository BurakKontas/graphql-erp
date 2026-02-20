package tr.kontas.erp.app.reference.graphql;

import com.netflix.graphql.dgs.*;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import tr.kontas.erp.app.company.dtos.CompanyPayload;
import tr.kontas.erp.app.reference.dtos.CreateTaxInput;
import tr.kontas.erp.app.reference.dtos.TaxPayload;
import tr.kontas.erp.core.application.reference.tax.CreateTaxCommand;
import tr.kontas.erp.core.application.reference.tax.CreateTaxUseCase;
import tr.kontas.erp.core.application.reference.tax.GetTaxesByCompanyUseCase;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.reference.tax.Tax;
import tr.kontas.erp.core.domain.reference.tax.TaxCode;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@DgsComponent
@RequiredArgsConstructor
public class TaxGraphql {

    private final CreateTaxUseCase createTaxUseCase;
    private final GetTaxesByCompanyUseCase getTaxesByCompanyUseCase;

    public static TaxPayload toPayload(Tax tax) {
        return new TaxPayload(
                tax.getId().getValue(),
                tax.getName(),
                tax.getType().name(),
                tax.getRate().getValue(),
                tax.isActive(),
                tax.getCompanyId().asUUID().toString()
        );
    }

    @DgsMutation
    public TaxPayload createTax(@InputArgument("input") CreateTaxInput input) {
        CompanyId companyId = CompanyId.of(input.getCompanyId());

        CreateTaxCommand command = new CreateTaxCommand(
                companyId,
                input.getCode(),
                input.getName(),
                input.getType(),
                input.getRate()
        );

        TaxCode code = createTaxUseCase.execute(command);

        return new TaxPayload(
                code.getValue(),
                input.getName(),
                input.getType(),
                input.getRate(),
                true,
                input.getCompanyId()
        );
    }

    @DgsQuery
    public List<TaxPayload> taxes(@InputArgument("companyId") String id) {
        CompanyId companyId = CompanyId.of(id);

        return getTaxesByCompanyUseCase.execute(companyId)
                .stream()
                .map(TaxGraphql::toPayload)
                .toList();
    }

    @DgsData(parentType = "TaxPayload")
    public CompletableFuture<CompanyPayload> company(DgsDataFetchingEnvironment dfe) {
        TaxPayload tax = dfe.getSource();

        DataLoader<String, CompanyPayload> dataLoader = dfe.getDataLoader("companyLoader");

        if (dataLoader == null || tax == null) {
            return CompletableFuture.completedFuture(null);
        }

        return dataLoader.load(tax.getCompanyId());
    }
}
