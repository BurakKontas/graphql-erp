package tr.kontas.erp.crm.platform.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.crm.application.port.LeadNumberGeneratorPort;
import tr.kontas.erp.crm.domain.lead.LeadNumber;
import tr.kontas.erp.crm.platform.persistence.lead.JpaLeadRepository;

@Component
@RequiredArgsConstructor
public class LeadNumberGeneratorImpl implements LeadNumberGeneratorPort {

    private final JpaLeadRepository jpa;

    @Override
    public LeadNumber generate(TenantId tenantId, CompanyId companyId, int year) {
        int nextSeq = jpa.findMaxSequenceByTenantId(tenantId.asUUID()) + 1;
        String value = "LD-%04d-%06d".formatted(year, nextSeq);
        return new LeadNumber(value);
    }
}

