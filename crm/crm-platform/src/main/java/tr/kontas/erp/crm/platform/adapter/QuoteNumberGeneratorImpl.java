package tr.kontas.erp.crm.platform.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.crm.application.port.QuoteNumberGeneratorPort;
import tr.kontas.erp.crm.domain.quote.QuoteNumber;
import tr.kontas.erp.crm.platform.persistence.quote.JpaQuoteRepository;

@Component
@RequiredArgsConstructor
public class QuoteNumberGeneratorImpl implements QuoteNumberGeneratorPort {

    private final JpaQuoteRepository jpa;

    @Override
    public QuoteNumber generate(TenantId tenantId, CompanyId companyId, int year) {
        int nextSeq = jpa.findMaxSequenceByTenantId(tenantId.asUUID()) + 1;
        String value = "QT-%04d-%06d".formatted(year, nextSeq);
        return new QuoteNumber(value);
    }
}

