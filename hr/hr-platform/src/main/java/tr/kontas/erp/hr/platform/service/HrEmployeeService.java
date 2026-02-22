package tr.kontas.erp.hr.platform.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.event.DomainEventPublisher;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.hr.application.employee.*;
import tr.kontas.erp.hr.application.port.EmployeeNumberGeneratorPort;
import tr.kontas.erp.hr.domain.employee.*;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class HrEmployeeService implements CreateHrEmployeeUseCase, GetHrEmployeeByIdUseCase,
        GetHrEmployeesByCompanyUseCase, GetHrEmployeesByIdsUseCase {

    private final HrEmployeeRepository employeeRepository;
    private final EmployeeNumberGeneratorPort numberGenerator;
    private final DomainEventPublisher eventPublisher;

    @Override
    public EmployeeId execute(CreateEmployeeCommand cmd) {
        TenantId tenantId = TenantContext.get();
        EmployeeId id = EmployeeId.newId();
        EmployeeNumber number = numberGenerator.generate(tenantId, cmd.companyId(), LocalDate.now().getYear());
        PersonalInfo pi = new PersonalInfo(
                cmd.firstName(), cmd.lastName(), cmd.dateOfBirth(), cmd.nationalId(),
                cmd.gender() != null ? Gender.valueOf(cmd.gender()) : null, cmd.nationality()
        );
        ContactInfo ci = new ContactInfo(
                cmd.personalEmail(), cmd.workEmail(), cmd.phone(),
                cmd.addressLine(), cmd.city(), cmd.contactCountryCode()
        );
        Employee employee = Employee.create(
                id, tenantId, cmd.companyId(), number, cmd.userId(),
                pi, ci, cmd.positionId(), cmd.departmentId(), cmd.managerId(),
                cmd.hireDate(), EmploymentType.valueOf(cmd.employmentType()), cmd.countryCode()
        );
        saveAndPublish(employee);
        return id;
    }

    @Override
    @Transactional(readOnly = true)
    public Employee execute(EmployeeId id) {
        return employeeRepository.findById(id, TenantContext.get())
                .orElseThrow(() -> new IllegalArgumentException("Employee not found: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> execute(CompanyId companyId) {
        return employeeRepository.findByCompanyId(TenantContext.get(), companyId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> execute(List<EmployeeId> ids) {
        return employeeRepository.findByIds(ids);
    }

    private void saveAndPublish(Employee employee) {
        employeeRepository.save(employee);
        employee.getDomainEvents().forEach(eventPublisher::publish);
        employee.clearDomainEvents();
    }
}

