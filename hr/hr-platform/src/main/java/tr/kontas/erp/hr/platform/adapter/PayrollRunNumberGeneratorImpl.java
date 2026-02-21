package tr.kontas.erp.hr.platform.adapter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.application.port.PayrollRunNumberGeneratorPort;
import tr.kontas.erp.hr.domain.payrollrun.PayrollRunNumber;

import java.math.BigInteger;

@Component
@RequiredArgsConstructor
public class PayrollRunNumberGeneratorImpl implements PayrollRunNumberGeneratorPort {

    private final EntityManager entityManager;

    @Override
    public PayrollRunNumber generate(TenantId tenantId, CompanyId companyId, int year) {
        Query query = entityManager.createNativeQuery(
                "SELECT COUNT(*) FROM payroll_runs WHERE tenant_id = :tid AND company_id = :cid AND run_number LIKE :prefix");
        query.setParameter("tid", tenantId.asUUID());
        query.setParameter("cid", companyId.asUUID());
        query.setParameter("prefix", "PRUN-" + year + "-%");
        long count = ((Number) query.getSingleResult()).longValue();
        return new PayrollRunNumber(String.format("PRUN-%d-%06d", year, count + 1));
    }
}
