package tr.kontas.erp.hr.platform.adapter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.application.port.LeaveRequestNumberGeneratorPort;
import tr.kontas.erp.hr.domain.leaverequest.LeaveRequestNumber;

@Component
@RequiredArgsConstructor
public class LeaveRequestNumberGeneratorImpl implements LeaveRequestNumberGeneratorPort {

    private final EntityManager entityManager;

    @Override
    public LeaveRequestNumber generate(TenantId tenantId, CompanyId companyId, int year) {
        Query query = entityManager.createNativeQuery(
        "SELECT COUNT(*) FROM leave_requests WHERE tenant_id = ?1 AND company_id = ?2 AND request_number LIKE ?3 FOR UPDATE");
        query.setParameter(1, tenantId.asUUID().toString()); // Ensure UUID is converted to String
        query.setParameter(2, companyId.asUUID().toString()); // Ensure UUID is converted to String
        query.setParameter(3, "LR-" + year + "-%");
        long count = ((Number) query.getSingleResult()).longValue();
        return new LeaveRequestNumber(String.format("LR-%d-%06d", year, count + 1));
    }
}
