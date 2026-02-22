package tr.kontas.erp.hr.platform.adapter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.application.port.EmployeeNumberGeneratorPort;
import tr.kontas.erp.hr.domain.employee.EmployeeNumber;

import java.math.BigInteger;

@Component
@RequiredArgsConstructor
public class EmployeeNumberGeneratorImpl implements EmployeeNumberGeneratorPort {

    private final EntityManager entityManager;

    @Override
    public EmployeeNumber generate(TenantId tenantId, CompanyId companyId, int year) {
        Query query = entityManager.createNativeQuery(
    "SELECT COUNT(*) FROM employees WHERE tenant_id = :tid AND company_id = :cid AND employee_number LIKE :prefix FOR UPDATE");
        query.setParameter("tid", tenantId.asUUID());
        query.setParameter("cid", companyId.asUUID());
        query.setParameter("prefix", "EMP-" + year + "-%");
        long count = ((Number) query.getSingleResult()).longValue();
        return new EmployeeNumber(String.format("EMP-%d-%06d", year, count + 1));
    }
}
