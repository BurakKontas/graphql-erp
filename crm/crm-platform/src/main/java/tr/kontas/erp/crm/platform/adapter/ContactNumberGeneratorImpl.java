package tr.kontas.erp.crm.platform.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.crm.application.port.ContactNumberGeneratorPort;
import tr.kontas.erp.crm.domain.contact.ContactNumber;
import tr.kontas.erp.crm.platform.persistence.contact.JpaContactRepository;

@Component
@RequiredArgsConstructor
public class ContactNumberGeneratorImpl implements ContactNumberGeneratorPort {

    private final JpaContactRepository jpa;

    @Override
    public ContactNumber generate(TenantId tenantId, CompanyId companyId, int year) {
        int nextSeq = jpa.findMaxSequenceByTenantId(tenantId.asUUID()) + 1;
        String value = "CT-%04d-%06d".formatted(year, nextSeq);
        return new ContactNumber(value);
    }
}

