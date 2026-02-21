package tr.kontas.erp.hr.platform.adapter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.application.port.LeaveRequestNumberGeneratorPort;
import tr.kontas.erp.hr.domain.leaverequest.LeaveRequestNumber;

import java.math.BigInteger;

@Component
@RequiredArgsConstructor
public class LeaveRequestNumberGeneratorImpl implements LeaveRequestNumberGeneratorPort {

    private final EntityManager entityManager;

    @Override
    public LeaveRequestNumber generate(TenantId tenantId, CompanyId companyId, int year) {
        Query query = entityManager.createNativeQuery(
                "SELECT COUNT(*) FROM leave_requests WHERE tenant_id = :tid AND company_id = :cid AND request_number LIKE :prefix");
        query.setParameter("tid", tenantId.asUUID());
        query.setParameter("cid", companyId.asUUID());
        query.setParameter("prefix", "LR-" + year + "-%");
        long count = ((Number) query.getSingleResult()).longValue();
        return new LeaveRequestNumber(String.format("LR-%d-%06d", year, count + 1));
    }
}
