package tr.kontas.erp.hr.application.employee;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.hr.domain.employee.Employee;

import java.util.List;

public interface GetEmployeesByCompanyUseCase {
    List<Employee> execute(CompanyId companyId);
}
