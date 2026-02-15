package tr.kontas.erp.app.graphql;

import com.netflix.graphql.dgs.*;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import tr.kontas.erp.app.dtos.CompanyPayload;
import tr.kontas.erp.app.dtos.CreateDepartmentInput;
import tr.kontas.erp.app.dtos.DepartmentPayload;
import tr.kontas.erp.app.dtos.EmployeePayload;
import tr.kontas.erp.core.application.department.CreateDepartmentCommand;
import tr.kontas.erp.core.application.department.CreateDepartmentUseCase;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.department.Department;
import tr.kontas.erp.core.domain.department.DepartmentId;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@DgsComponent
@RequiredArgsConstructor
public class DepartmentGraphql {

    private final CreateDepartmentUseCase createDepartmentUseCase;

    public static DepartmentPayload toPayload(Department department) {
        return new DepartmentPayload(
                department.getId().asUUID().toString(),
                department.getName().getValue(),
                department.getParentId() != null ? department.getParentId().asUUID().toString() : null,
                department.getCompanyId().asUUID().toString()
        );
    }

    @DgsMutation
    public DepartmentPayload createDepartment(@InputArgument("input") CreateDepartmentInput input) {

        DepartmentId parentId = input.getParentId() != null ? DepartmentId.of(input.getParentId()) : null;
        CompanyId companyId = CompanyId.of(input.getCompanyId());

        CreateDepartmentCommand command = new CreateDepartmentCommand(parentId, companyId, input.getName());

        DepartmentId id = createDepartmentUseCase.execute(command);

        return new DepartmentPayload(id.asUUID().toString(), input.getName(), input.getParentId(), input.getCompanyId());
    }

    @DgsData(parentType = "DepartmentPayload")
    public CompletableFuture<List<EmployeePayload>> employees(DgsDataFetchingEnvironment dfe) {
        DepartmentPayload department = dfe.getSource();

        DataLoader<String, List<EmployeePayload>> dataLoader = dfe.getDataLoader("employeeLoader");

        if (dataLoader == null || department == null) {
            return CompletableFuture.supplyAsync(List::of);
        }

        return dataLoader.load(department.getId());
    }

    @DgsData(parentType = "DepartmentPayload")
    public CompletableFuture<DepartmentPayload> parent(DgsDataFetchingEnvironment dfe) {
        DepartmentPayload department = dfe.getSource();

        DataLoader<String, List<DepartmentPayload>> dataLoader = dfe.getDataLoader("departmentsLoader");

        if (dataLoader == null || department == null || department.getParentId() == null || department.getParentId().isBlank()) {
            return CompletableFuture.completedFuture(null);
        }

        return dataLoader.load(department.getParentId())
                .thenApply(list -> list.isEmpty() ? null : list.getFirst());
    }

    @DgsData(parentType = "DepartmentPayload")
    public CompletableFuture<CompanyPayload> company(DgsDataFetchingEnvironment dfe) {
        DepartmentPayload department = dfe.getSource();

        DataLoader<String, CompanyPayload> dataLoader = dfe.getDataLoader("companyLoader");

        if (dataLoader == null || department == null) {
            return CompletableFuture.completedFuture(null);
        }

        return dataLoader.load(department.getCompanyId());
    }
}