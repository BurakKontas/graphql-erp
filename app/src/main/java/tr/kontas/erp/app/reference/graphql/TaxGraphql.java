package tr.kontas.erp.app.reference.graphql;

import com.netflix.graphql.dgs.*;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import tr.kontas.erp.app.company.dtos.CompanyPayload;
import tr.kontas.erp.app.reference.dtos.CreateTaxInput;
import tr.kontas.erp.app.reference.dtos.TaxPayload;
import tr.kontas.erp.app.reference.dtos.UpdateTaxRateInput;
import tr.kontas.erp.core.application.reference.tax.CreateTaxCommand;
import tr.kontas.erp.core.application.reference.tax.CreateTaxUseCase;
import tr.kontas.erp.core.application.reference.tax.GetTaxesByCompanyUseCase;
import tr.kontas.erp.core.application.reference.tax.UpdateTaxRateCommand;
import tr.kontas.erp.core.application.reference.tax.UpdateTaxRateUseCase;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.reference.tax.Tax;
import tr.kontas.erp.core.domain.reference.tax.TaxCode;
import tr.kontas.erp.core.domain.reference.tax.TaxRepository;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@DgsComponent
@RequiredArgsConstructor
public class TaxGraphql {

    private final CreateTaxUseCase createTaxUseCase;
    private final GetTaxesByCompanyUseCase getTaxesByCompanyUseCase;
    private final UpdateTaxRateUseCase updateTaxRateUseCase;
    private final TaxRepository taxRepository;

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

    @DgsMutation
    public TaxPayload updateTaxRate(@InputArgument("input") UpdateTaxRateInput input) {
        // Core service updates tax + publishes TaxRateChangedEvent
        // Sales module listens to the event and recalculates DRAFT orders
        updateTaxRateUseCase.execute(
                new UpdateTaxRateCommand(input.getCompanyId(), input.getTaxCode(), input.getNewRate())
        );

        TenantId tenantId = TenantContext.get();
        CompanyId companyId = CompanyId.of(input.getCompanyId());
        Tax tax = taxRepository.findByCode(tenantId, companyId, new TaxCode(input.getTaxCode()))
                .orElseThrow(() -> new IllegalArgumentException("Tax not found after update: " + input.getTaxCode()));

        return toPayload(tax);
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
