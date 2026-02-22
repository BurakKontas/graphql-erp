package tr.kontas.erp.hr.platform.adapter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.application.port.ContractNumberGeneratorPort;
import tr.kontas.erp.hr.domain.contract.ContractNumber;

import java.math.BigInteger;

@Component
@RequiredArgsConstructor
public class ContractNumberGeneratorImpl implements ContractNumberGeneratorPort {

    private final EntityManager entityManager;

    @Override
    public ContractNumber generate(TenantId tenantId, CompanyId companyId, int year) {
        Query query = entityManager.createNativeQuery(
    "SELECT COUNT(*) FROM contracts WHERE tenant_id = :tid AND company_id = :cid AND contract_number LIKE :prefix FOR UPDATE");
        query.setParameter("tid", tenantId.asUUID());
        query.setParameter("cid", companyId.asUUID());
        query.setParameter("prefix", "CTR-" + year + "-%");
        long count = ((Number) query.getSingleResult()).longValue();
        return new ContractNumber(String.format("CTR-%d-%06d", year, count + 1));
    }
}
