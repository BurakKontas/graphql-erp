package tr.kontas.erp.app.department.graphql;

import com.netflix.graphql.dgs.*;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import tr.kontas.erp.app.company.dtos.CompanyPayload;
import tr.kontas.erp.app.department.dtos.CreateDepartmentInput;
import tr.kontas.erp.app.department.dtos.DepartmentPayload;
import tr.kontas.erp.app.employee.dtos.EmployeePayload;
import tr.kontas.erp.core.application.department.CreateDepartmentCommand;
import tr.kontas.erp.core.application.department.CreateDepartmentUseCase;
import tr.kontas.erp.core.application.department.GetDepartmentByIdUseCase;
import tr.kontas.erp.core.application.department.GetDepartmentsByCompanyIdsUseCase;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.department.Department;
import tr.kontas.erp.core.domain.department.DepartmentId;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@DgsComponent
@RequiredArgsConstructor
public class DepartmentGraphql {

    private final CreateDepartmentUseCase createDepartmentUseCase;
    private final GetDepartmentByIdUseCase getDepartmentByIdUseCase;
    private final GetDepartmentsByCompanyIdsUseCase getDepartmentsByCompanyIdsUseCase;

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

    @DgsQuery
    public List<DepartmentPayload> departments(@InputArgument("companyId") String id) {
        CompanyId companyId = CompanyId.of(id);

        var departments = getDepartmentsByCompanyIdsUseCase.executeByCompanyIds(List.of(companyId))
                .get(companyId);

        if (departments == null) return List.of();

        return departments
                .stream()
                .map(DepartmentGraphql::toPayload)
                .toList();
    }

    @DgsQuery
    public DepartmentPayload department(@InputArgument("id") String id) {
        DepartmentId departmentId = DepartmentId.of(id);
        return toPayload(getDepartmentByIdUseCase.execute(departmentId));
    }

    @DgsEntityFetcher(name = "DepartmentPayload")
    public DepartmentPayload department(Map<String, Object> values) {
        String id = values.get("id").toString();
        return department(id);
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

        DataLoader<String, DepartmentPayload> dataLoader = dfe.getDataLoader("departmentLoader");

        if (dataLoader == null || department == null || department.getParentId() == null || department.getParentId().isBlank()) {
            return CompletableFuture.completedFuture(null);
        }

        return dataLoader.load(department.getParentId());
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