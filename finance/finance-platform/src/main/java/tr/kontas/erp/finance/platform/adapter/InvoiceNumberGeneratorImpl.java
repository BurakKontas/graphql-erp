package tr.kontas.erp.finance.platform.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.finance.application.port.InvoiceNumberGeneratorPort;
import tr.kontas.erp.finance.platform.persistence.salesinvoice.JpaSalesInvoiceRepository;

@Component
@RequiredArgsConstructor
public class InvoiceNumberGeneratorImpl implements InvoiceNumberGeneratorPort {
    private final JpaSalesInvoiceRepository jpa;

    @Override
    public String generate(TenantId tenantId, CompanyId companyId, int year) {
        int nextSeq = jpa.findMaxSequenceByTenantId(tenantId.asUUID()) + 1;
        return "INV-%04d-%06d".formatted(year, nextSeq);
    }
}

