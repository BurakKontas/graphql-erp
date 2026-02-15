package tr.kontas.erp.app.graphql;

import com.netflix.graphql.dgs.*;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import tr.kontas.erp.app.dtos.CreateEmployeeInput;
import tr.kontas.erp.app.dtos.DepartmentPayload;
import tr.kontas.erp.app.dtos.EmployeePayload;
import tr.kontas.erp.core.application.employee.CreateEmployeeCommand;
import tr.kontas.erp.core.application.employee.CreateEmployeeUseCase;
import tr.kontas.erp.core.domain.department.DepartmentId;
import tr.kontas.erp.core.domain.employee.Employee;
import tr.kontas.erp.core.domain.employee.EmployeeId;

import java.util.concurrent.CompletableFuture;

@DgsComponent
@RequiredArgsConstructor
public class EmployeeGraphql {

    private final CreateEmployeeUseCase createEmployeeUseCase;

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