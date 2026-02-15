package tr.kontas.erp.app.company.graphql;

import com.netflix.graphql.dgs.*;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import tr.kontas.erp.app.company.dtos.CompanyPayload;
import tr.kontas.erp.app.company.dtos.CreateCompanyInput;
import tr.kontas.erp.app.department.dtos.DepartmentPayload;
import tr.kontas.erp.core.application.company.CreateCompanyCommand;
import tr.kontas.erp.core.application.company.CreateCompanyUseCase;
import tr.kontas.erp.core.application.company.GetCompaniesUseCase;
import tr.kontas.erp.core.application.company.GetCompanyByIdUseCase;
import tr.kontas.erp.core.domain.company.Company;
import tr.kontas.erp.core.domain.company.CompanyId;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@DgsComponent
@RequiredArgsConstructor
public class CompanyGraphql {
    private final GetCompanyByIdUseCase getCompanyByIdUseCase;
    private final GetCompaniesUseCase getCompaniesUseCase;
    private final CreateCompanyUseCase createCompanyUseCase;

    public static CompanyPayload fromDomain(Company company) {
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
    public List<CompanyPayload> companies() {
        return getCompaniesUseCase.execute()
                .stream()
                .map(CompanyGraphql::fromDomain)
                .toList();
    }

    @DgsQuery
    public CompanyPayload company(@InputArgument("id") UUID id) {
        CompanyId companyId = CompanyId.of(id);
        return fromDomain(getCompanyByIdUseCase.execute(companyId));
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
}
