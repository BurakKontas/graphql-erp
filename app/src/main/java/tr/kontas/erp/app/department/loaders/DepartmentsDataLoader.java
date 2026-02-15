package tr.kontas.erp.app.department.loaders;

import com.netflix.graphql.dgs.DgsDataLoader;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.dataloader.BatchLoader;
import tr.kontas.erp.app.department.dtos.DepartmentPayload;
import tr.kontas.erp.app.department.graphql.DepartmentGraphql;
import tr.kontas.erp.core.application.department.GetDepartmentsByCompanyIdsUseCase;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.department.Department;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@DgsDataLoader(name = "departmentsLoader")
@RequiredArgsConstructor
public class DepartmentsDataLoader implements BatchLoader<String, List<DepartmentPayload>> {

    private final GetDepartmentsByCompanyIdsUseCase getDepartmentsByCompanyIdsUseCase;

    @NonNull
    @Override
    public CompletionStage<List<List<DepartmentPayload>>> load(@NonNull List<String> companyIds) {
        List<CompanyId> companyIdList = companyIds.stream()
                .map(CompanyId::of)
                .toList();

        Map<CompanyId, List<Department>> departmentsByCompany = getDepartmentsByCompanyIdsUseCase.executeByCompanyIds(companyIdList);

        List<List<DepartmentPayload>> result = companyIdList.stream()
                .map(cid -> departmentsByCompany.getOrDefault(cid, Collections.emptyList())
                        .stream()
                        .map(DepartmentGraphql::toPayload)
                        .toList()
                )
                .toList();

        return CompletableFuture.completedFuture(result);
    }
}
