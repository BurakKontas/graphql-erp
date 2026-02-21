package tr.kontas.erp.crm.platform.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.crm.application.port.OpportunityNumberGeneratorPort;
import tr.kontas.erp.crm.domain.opportunity.OpportunityNumber;
import tr.kontas.erp.crm.platform.persistence.opportunity.JpaOpportunityRepository;

@Component
@RequiredArgsConstructor
public class OpportunityNumberGeneratorImpl implements OpportunityNumberGeneratorPort {

    private final JpaOpportunityRepository jpa;

    @Override
    public OpportunityNumber generate(TenantId tenantId, CompanyId companyId, int year) {
        int nextSeq = jpa.findMaxSequenceByTenantId(tenantId.asUUID()) + 1;
        String value = "OPP-%04d-%06d".formatted(year, nextSeq);
        return new OpportunityNumber(value);
    }
}

