package tr.kontas.erp.core.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.application.employee.CreateEmployeeCommand;
import tr.kontas.erp.core.application.employee.CreateEmployeeUseCase;
import tr.kontas.erp.core.application.employee.GetEmployeeByIdUseCase;
import tr.kontas.erp.core.application.employee.GetEmployeesByDepartmentIdsUseCase;
import tr.kontas.erp.core.domain.department.DepartmentId;
import tr.kontas.erp.core.domain.department.DepartmentRepository;
import tr.kontas.erp.core.domain.employee.Employee;
import tr.kontas.erp.core.domain.employee.EmployeeId;
import tr.kontas.erp.core.domain.employee.EmployeeName;
import tr.kontas.erp.core.domain.employee.EmployeeRepository;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;

import java.util.*;

@Service
@RequiredArgsConstructor
public class EmployeeService implements
        CreateEmployeeUseCase,
        GetEmployeesByDepartmentIdsUseCase,
        GetEmployeeByIdUseCase {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    @SneakyThrows
    @Override
    @Transactional
    public EmployeeId execute(CreateEmployeeCommand command) {
        TenantId tenantId = TenantContext.get();

        var departmentExists = departmentRepository.existsById(command.departmentId());

        if (!departmentExists) {
            throw new Exception("Department doesn't exists");
        }

        EmployeeId id = EmployeeId.newId();
        var employee = new Employee(id, tenantId, new EmployeeName(command.name()), command.departmentId());

        employeeRepository.save(employee);

        return id;
    }

    @Override
    @Transactional
    public Map<DepartmentId, List<Employee>> execute(List<DepartmentId> ids) {
        Map<DepartmentId, List<Employee>> resultMap = new HashMap<>();

        Set<Employee> employees = employeeRepository.findByDepartmentIds(ids);

        employees.forEach(employee -> {
            resultMap.computeIfAbsent(employee.getDepartmentId(), _ -> new ArrayList<>())
                    .add(employee);
        });

        return resultMap;
    }

    @Override
    public Employee execute(EmployeeId id) {
        return employeeRepository.findById(id)
                .orElseThrow();
    }
}
