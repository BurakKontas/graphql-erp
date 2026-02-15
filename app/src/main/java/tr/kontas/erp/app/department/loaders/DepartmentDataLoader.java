package tr.kontas.erp.app.department.loaders;

import com.netflix.graphql.dgs.DgsDataLoader;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.dataloader.BatchLoader;
import tr.kontas.erp.app.department.dtos.DepartmentPayload;
import tr.kontas.erp.app.department.graphql.DepartmentGraphql;
import tr.kontas.erp.core.application.department.GetDepartmentsByIdsUseCase;
import tr.kontas.erp.core.domain.department.Department;
import tr.kontas.erp.core.domain.department.DepartmentId;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@DgsDataLoader(name = "departmentLoader")
@RequiredArgsConstructor
public class DepartmentDataLoader implements BatchLoader<String, DepartmentPayload> {

    private final GetDepartmentsByIdsUseCase getDepartmentsByIdsUseCase;

    @NonNull
    @Override
    public CompletionStage<List<DepartmentPayload>> load(@NonNull List<String> departmentIds) {
        List<DepartmentId> departmentIdList = departmentIds.stream()
                .map(DepartmentId::of)
                .toList();

        List<Department> departments = getDepartmentsByIdsUseCase.execute(departmentIdList);

        List<DepartmentPayload> result = departments
                .stream()
                .map(DepartmentGraphql::toPayload)
                .toList();

        return CompletableFuture.completedFuture(result);
    }
}
