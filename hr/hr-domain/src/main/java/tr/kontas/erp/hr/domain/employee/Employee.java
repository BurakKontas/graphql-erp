package tr.kontas.erp.hr.domain.employee;

import lombok.Getter;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.domain.event.EmployeeCreatedEvent;
import tr.kontas.erp.hr.domain.event.EmployeeTerminatedEvent;
import tr.kontas.erp.hr.domain.position.PositionId;

import java.time.LocalDate;

@Getter
public class Employee extends AggregateRoot<EmployeeId> {

    private final TenantId tenantId;
    private final CompanyId companyId;
    private final EmployeeNumber employeeNumber;
    private String userId;
    private PersonalInfo personalInfo;
    private ContactInfo contactInfo;
    private String positionId;
    private String departmentId;
    private String managerId;
    private LocalDate hireDate;
    private LocalDate terminationDate;
    private EmploymentType employmentType;
    private EmployeeStatus status;
    private String countryCode;

    public Employee(EmployeeId id, TenantId tenantId, CompanyId companyId,
                    EmployeeNumber employeeNumber, String userId,
                    PersonalInfo personalInfo, ContactInfo contactInfo,
                    String positionId, String departmentId, String managerId,
                    LocalDate hireDate, LocalDate terminationDate,
                    EmploymentType employmentType, EmployeeStatus status,
                    String countryCode) {
        super(id);
        this.tenantId = tenantId;
        this.companyId = companyId;
        this.employeeNumber = employeeNumber;
        this.userId = userId;
        this.personalInfo = personalInfo;
        this.contactInfo = contactInfo;
        this.positionId = positionId;
        this.departmentId = departmentId;
        this.managerId = managerId;
        this.hireDate = hireDate;
        this.terminationDate = terminationDate;
        this.employmentType = employmentType;
        this.status = status;
        this.countryCode = countryCode;
    }


    public static Employee create(EmployeeId id, TenantId tenantId, CompanyId companyId,
                                  EmployeeNumber number, String userId,
                                  PersonalInfo personalInfo, ContactInfo contactInfo,
                                  String positionId, String departmentId, String managerId,
                                  LocalDate hireDate, EmploymentType type, String countryCode) {
        Employee emp = new Employee(id, tenantId, companyId, number, userId,
                personalInfo, contactInfo, positionId, departmentId, managerId,
                hireDate, null, type, EmployeeStatus.ACTIVE, countryCode);
        emp.registerEvent(new EmployeeCreatedEvent(
                id.asUUID(), tenantId.asUUID(), companyId.asUUID(),
                number.getValue(), personalInfo.getFirstName(), personalInfo.getLastName()));
        return emp;
    }


    public void updatePersonalInfo(PersonalInfo info) {
        this.personalInfo = info;
    }


    public void updateContactInfo(ContactInfo info) {
        this.contactInfo = info;
    }


    public void transfer(String newPositionId, String newDepartmentId) {
        this.positionId = newPositionId;
        this.departmentId = newDepartmentId;
    }


    public void changeManager(String newManagerId) {
        this.managerId = newManagerId;
    }


    public void suspend() {
        if (status != EmployeeStatus.ACTIVE) {
            throw new IllegalStateException("Can only suspend ACTIVE employees");
        }
        this.status = EmployeeStatus.SUSPENDED;
    }


    public void reinstate() {
        if (status != EmployeeStatus.SUSPENDED) {
            throw new IllegalStateException("Can only reinstate SUSPENDED employees");
        }
        this.status = EmployeeStatus.ACTIVE;
    }


    public void terminate(LocalDate terminationDate, String reason) {
        if (status != EmployeeStatus.ACTIVE && status != EmployeeStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot terminate employee in status: " + status);
        }
        this.terminationDate = terminationDate;
        this.status = EmployeeStatus.TERMINATED;
        registerEvent(new EmployeeTerminatedEvent(
                getId().asUUID(), tenantId.asUUID(), companyId.asUUID(),
                employeeNumber.getValue(), reason));
    }


    public void linkUser(String userId) {
        this.userId = userId;
    }
}

