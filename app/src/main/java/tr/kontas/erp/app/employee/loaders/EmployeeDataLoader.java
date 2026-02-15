package tr.kontas.erp.app.employee.loaders;

import com.netflix.graphql.dgs.DgsDataLoader;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.dataloader.BatchLoader;
import tr.kontas.erp.app.employee.dtos.EmployeePayload;
import tr.kontas.erp.app.employee.graphql.EmployeeGraphql;
import tr.kontas.erp.core.application.employee.GetEmployeesByDepartmentIdsUseCase;
import tr.kontas.erp.core.domain.department.DepartmentId;
import tr.kontas.erp.core.domain.employee.Employee;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@DgsDataLoader(name = "employeeLoader")
@RequiredArgsConstructor
public class EmployeeDataLoader implements BatchLoader<String, List<EmployeePayload>> {

    private final GetEmployeesByDepartmentIdsUseCase getEmployeesByDepartmentIdsUseCase;

    @Override
    @org.jspecify.annotations.NonNull
    public CompletionStage<List<List<EmployeePayload>>> load(@NonNull List<String> ids) {

        List<DepartmentId> departmentIds = ids.stream()
                .map(DepartmentId::of)
                .toList();

        Map<DepartmentId, List<Employee>> employeesByDepartment = getEmployeesByDepartmentIdsUseCase.execute(departmentIds);

        List<List<EmployeePayload>> result = departmentIds.stream()
                .map(cid -> employeesByDepartment.getOrDefault(cid, Collections.emptyList())
                        .stream()
                        .map(EmployeeGraphql::toPayload)
                        .toList()
                )
                .toList();

        return CompletableFuture.completedFuture(result);
    }
}
