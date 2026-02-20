package tr.kontas.erp.app.company.graphql;

import com.netflix.graphql.dgs.*;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import tr.kontas.erp.app.businesspartner.dtos.BusinessPartnerPayload;
import tr.kontas.erp.app.company.dtos.CompanyPayload;
import tr.kontas.erp.app.company.dtos.CreateCompanyInput;
import tr.kontas.erp.app.department.dtos.DepartmentPayload;
import tr.kontas.erp.app.reference.dtos.PaymentTermPayload;
import tr.kontas.erp.app.reference.dtos.TaxPayload;
import tr.kontas.erp.core.application.company.CreateCompanyCommand;
import tr.kontas.erp.core.application.company.CreateCompanyUseCase;
import tr.kontas.erp.core.application.company.GetCompaniesByTenantIdsUseCase;
import tr.kontas.erp.core.application.company.GetCompanyByIdUseCase;
import tr.kontas.erp.core.domain.company.Company;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@DgsComponent
@RequiredArgsConstructor
public class CompanyGraphql {
    private final GetCompanyByIdUseCase getCompanyByIdUseCase;
    private final CreateCompanyUseCase createCompanyUseCase;
    private final GetCompaniesByTenantIdsUseCase getCompaniesByTenantIdsUseCase;

    public static CompanyPayload toPayload(Company company) {
        return new CompanyPayload(
                company.getId().asUUID().toString(),
                company.getName().getValue()
        );
    }

    @DgsMutation
    public CompanyPayload createCompany(@InputArgument("input") CreateCompanyInput input) {
        CreateCompanyCommand command = new CreateCompanyCommand(input.getName());

        CompanyId companyId = createCompanyUseCase.execute(command);

        return new CompanyPayload(
                companyId.asUUID().toString(),
                input.getName()
        );
    }

    @DgsQuery
    public List<CompanyPayload> companies(@InputArgument("tenantId") UUID id) {
        TenantId tenantId = TenantId.of(id);

        List<Company> companies = getCompaniesByTenantIdsUseCase.executeByTenantIds(List.of(tenantId))
                .get(tenantId);

        if (companies == null) return List.of();

        return companies
                .stream()
                .map(CompanyGraphql::toPayload)
                .toList();
    }

    @DgsQuery
    public CompanyPayload company(@InputArgument("id") UUID id) {
        CompanyId companyId = CompanyId.of(id);
        return toPayload(getCompanyByIdUseCase.execute(companyId));
    }

    @DgsData(parentType = "CompanyPayload")
    public CompletableFuture<List<DepartmentPayload>> departments(DgsDataFetchingEnvironment dfe) {
        CompanyPayload company = dfe.getSource();

        DataLoader<String, List<DepartmentPayload>> dataLoader = dfe.getDataLoader("departmentsLoader");

        if (dataLoader == null || company == null) {
            return CompletableFuture.supplyAsync(List::of);
        }

        return dataLoader.load(company.getId());
    }

    @DgsData(parentType = "CompanyPayload")
    public CompletableFuture<List<BusinessPartnerPayload>> businessPartners(DgsDataFetchingEnvironment dfe) {
        CompanyPayload company = dfe.getSource();

        DataLoader<String, List<BusinessPartnerPayload>> dataLoader = dfe.getDataLoader("businessPartnersLoader");

        if (dataLoader == null || company == null) {
            return CompletableFuture.supplyAsync(List::of);
        }

        return dataLoader.load(company.getId());
    }

    @DgsData(parentType = "CompanyPayload")
    public CompletableFuture<List<PaymentTermPayload>> paymentTerms(DgsDataFetchingEnvironment dfe) {
        CompanyPayload company = dfe.getSource();

        DataLoader<String, List<PaymentTermPayload>> dataLoader = dfe.getDataLoader("paymentTermsLoader");

        if (dataLoader == null || company == null) {
            return CompletableFuture.supplyAsync(List::of);
        }

        return dataLoader.load(company.getId());
    }

    @DgsData(parentType = "CompanyPayload")
    public CompletableFuture<List<TaxPayload>> taxes(DgsDataFetchingEnvironment dfe) {
        CompanyPayload company = dfe.getSource();

        DataLoader<String, List<TaxPayload>> dataLoader = dfe.getDataLoader("taxesLoader");

        if (dataLoader == null || company == null) {
            return CompletableFuture.supplyAsync(List::of);
        }

        return dataLoader.load(company.getId());
    }
}
