package tr.kontas.erp.app.employee.graphql;

import com.netflix.graphql.dgs.*;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import tr.kontas.erp.app.department.dtos.DepartmentPayload;
import tr.kontas.erp.app.employee.dtos.CreateEmployeeInput;
import tr.kontas.erp.app.employee.dtos.EmployeePayload;
import tr.kontas.erp.core.application.employee.CreateEmployeeCommand;
import tr.kontas.erp.core.application.employee.CreateEmployeeUseCase;
import tr.kontas.erp.core.application.employee.GetEmployeeByIdUseCase;
import tr.kontas.erp.core.application.employee.GetEmployeesByDepartmentIdsUseCase;
import tr.kontas.erp.core.domain.department.DepartmentId;
import tr.kontas.erp.core.domain.employee.Employee;
import tr.kontas.erp.core.domain.employee.EmployeeId;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@DgsComponent
@RequiredArgsConstructor
public class EmployeeGraphql {

    private final CreateEmployeeUseCase createEmployeeUseCase;
    private final GetEmployeeByIdUseCase getEmployeeByIdUseCase;
    private final GetEmployeesByDepartmentIdsUseCase getEmployeesByDepartmentIdsUseCase;

    public static EmployeePayload toPayload(Employee domain) {
        return new EmployeePayload(
                domain.getId().asUUID().toString(),
                domain.getName().getValue(),
                domain.getDepartmentId().asUUID().toString()
        );
    }

    @DgsMutation
    public EmployeePayload createEmployee(@InputArgument("input") CreateEmployeeInput input) {
        CreateEmployeeCommand command = new CreateEmployeeCommand(DepartmentId.of(input.getDepartmentId()), input.getName());

        EmployeeId id = createEmployeeUseCase.execute(command);

        return new EmployeePayload(id.asUUID().toString(), input.getName(), input.getDepartmentId());
    }

    @DgsQuery
    public List<EmployeePayload> employees(@InputArgument("departmentId") String id) {
        DepartmentId departmentId = DepartmentId.of(id);

        var employees = getEmployeesByDepartmentIdsUseCase.execute(List.of(departmentId))
                .get(departmentId);

        if (employees == null) return List.of();

        return employees
                .stream()
                .map(EmployeeGraphql::toPayload)
                .toList();
    }

    @DgsQuery
    public EmployeePayload employee(@InputArgument("id") String id) {
        EmployeeId employeeId = EmployeeId.of(id);
        return toPayload(getEmployeeByIdUseCase.execute(employeeId));
    }

    @DgsEntityFetcher(name = "EmployeePayload")
    public EmployeePayload employee(Map<String, Object> values) {
        Object id = values.get("id");
        if (id == null) {
            throw new IllegalArgumentException("EmployeePayload entity fetcher requires 'id'");
        }
        return employee(id.toString());
    }

    @DgsData(parentType = "EmployeePayload")
    public CompletableFuture<DepartmentPayload> department(DgsDataFetchingEnvironment dfe) {
        EmployeePayload employee = dfe.getSource();

        DataLoader<String, DepartmentPayload> dataLoader = dfe.getDataLoader("departmentLoader");

        if (dataLoader == null || employee == null) {
            return CompletableFuture.completedFuture(null);
        }

        return dataLoader.load(employee.getDepartmentId());
    }
}