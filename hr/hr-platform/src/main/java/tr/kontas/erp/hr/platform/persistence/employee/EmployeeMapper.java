package tr.kontas.erp.hr.platform.persistence.employee;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.domain.employee.*;

public class EmployeeMapper {

    public static EmployeeJpaEntity toEntity(Employee emp) {
        EmployeeJpaEntity e = new EmployeeJpaEntity();
        e.setId(emp.getId().asUUID());
        e.setTenantId(emp.getTenantId().asUUID());
        e.setCompanyId(emp.getCompanyId().asUUID());
        e.setEmployeeNumber(emp.getEmployeeNumber().getValue());
        e.setUserId(emp.getUserId());
        if (emp.getPersonalInfo() != null) {
            e.setFirstName(emp.getPersonalInfo().getFirstName());
            e.setLastName(emp.getPersonalInfo().getLastName());
            e.setDateOfBirth(emp.getPersonalInfo().getDateOfBirth());
            e.setNationalId(emp.getPersonalInfo().getNationalId());
            e.setGender(emp.getPersonalInfo().getGender() != null ? emp.getPersonalInfo().getGender().name() : null);
            e.setNationality(emp.getPersonalInfo().getNationality());
        }
        if (emp.getContactInfo() != null) {
            e.setPersonalEmail(emp.getContactInfo().getPersonalEmail());
            e.setWorkEmail(emp.getContactInfo().getWorkEmail());
            e.setPhone(emp.getContactInfo().getPhone());
            e.setAddressLine(emp.getContactInfo().getAddressLine());
            e.setCity(emp.getContactInfo().getCity());
            e.setContactCountryCode(emp.getContactInfo().getCountryCode());
        }
        e.setPositionId(emp.getPositionId());
        e.setDepartmentId(emp.getDepartmentId());
        e.setManagerId(emp.getManagerId());
        e.setHireDate(emp.getHireDate());
        e.setTerminationDate(emp.getTerminationDate());
        e.setEmploymentType(emp.getEmploymentType().name());
        e.setStatus(emp.getStatus().name());
        e.setCountryCode(emp.getCountryCode());
        return e;
    }

    public static Employee toDomain(EmployeeJpaEntity e) {
        PersonalInfo pi = new PersonalInfo(
                e.getFirstName(), e.getLastName(), e.getDateOfBirth(),
                e.getNationalId(),
                e.getGender() != null ? Gender.valueOf(e.getGender()) : null,
                e.getNationality()
        );
        ContactInfo ci = new ContactInfo(
                e.getPersonalEmail(), e.getWorkEmail(), e.getPhone(),
                e.getAddressLine(), e.getCity(), e.getContactCountryCode()
        );
        return new Employee(
                EmployeeId.of(e.getId()),
                TenantId.of(e.getTenantId()),
                CompanyId.of(e.getCompanyId()),
                new EmployeeNumber(e.getEmployeeNumber()),
                e.getUserId(), pi, ci,
                e.getPositionId(), e.getDepartmentId(), e.getManagerId(),
                e.getHireDate(), e.getTerminationDate(),
                EmploymentType.valueOf(e.getEmploymentType()),
                EmployeeStatus.valueOf(e.getStatus()),
                e.getCountryCode()
        );
    }
}

