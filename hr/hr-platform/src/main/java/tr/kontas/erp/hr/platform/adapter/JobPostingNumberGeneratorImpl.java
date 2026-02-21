package tr.kontas.erp.hr.platform.adapter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.application.port.JobPostingNumberGeneratorPort;
import tr.kontas.erp.hr.domain.jobposting.JobPostingNumber;

import java.math.BigInteger;

@Component
@RequiredArgsConstructor
public class JobPostingNumberGeneratorImpl implements JobPostingNumberGeneratorPort {

    private final EntityManager entityManager;

    @Override
    public JobPostingNumber generate(TenantId tenantId, CompanyId companyId, int year) {
        Query query = entityManager.createNativeQuery(
                "SELECT COUNT(*) FROM job_postings WHERE tenant_id = :tid AND company_id = :cid AND posting_number LIKE :prefix");
        query.setParameter("tid", tenantId.asUUID());
        query.setParameter("cid", companyId.asUUID());
        query.setParameter("prefix", "JP-" + year + "-%");
        long count = ((Number) query.getSingleResult()).longValue();
        return new JobPostingNumber(String.format("JP-%d-%06d", year, count + 1));
    }
}
