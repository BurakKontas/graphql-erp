package tr.kontas.erp.core.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.application.department.*;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.department.Department;
import tr.kontas.erp.core.domain.department.DepartmentId;
import tr.kontas.erp.core.domain.department.DepartmentName;
import tr.kontas.erp.core.domain.department.DepartmentRepository;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DepartmentService implements
        CreateDepartmentUseCase,
        GetDepartmentsUseCase,
        GetDepartmentByIdUseCase,
        GetDepartmentsByCompanyIdsUseCase,
        GetDepartmentsByIdsUseCase {

    private final DepartmentRepository departmentRepository;

    @SneakyThrows
    @Override
    @Transactional
    public DepartmentId execute(CreateDepartmentCommand command) {
        TenantId tenantId = TenantContext.get();

        if (command.parentId() != null) {
            var departmentExists = departmentRepository.existsById(command.parentId());

            if (!departmentExists) {
                throw new Exception("Parent department doesn't exists");
            }
        }

        DepartmentId id = DepartmentId.newId();
        Department department = new Department(id, tenantId, new DepartmentName(command.name()), command.companyId(), command.parentId());

        departmentRepository.save(department);

        return id;
    }

    @Override
    public Department execute(DepartmentId id) {
        return departmentRepository.findById(id)
                .orElseThrow();
    }

    @Override
    public Set<Department> execute(CompanyId id) {
        return departmentRepository.findByCompanyId(id);
    }

    @Override
    @Transactional
    public Map<CompanyId, List<Department>> executeByCompanyIds(List<CompanyId> ids) {
        Map<CompanyId, List<Department>> resultMap = new HashMap<>();

        Set<Department> departments = departmentRepository.findDepartmentsByCompanyIds(ids);

        departments.forEach(department -> {
            resultMap.computeIfAbsent(department.getCompanyId(), _ -> new ArrayList<>())
                    .add(department);
        });

        return resultMap;
    }

    @Override
    public List<Department> execute(List<DepartmentId> ids) {
        return departmentRepository.findDepartmentsByIds(ids);
    }
}
