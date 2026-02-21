package tr.kontas.erp.purchase.platform.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.purchase.application.port.VendorInvoiceNumberGeneratorPort;
import tr.kontas.erp.purchase.domain.vendorinvoice.VendorInvoiceNumber;
import tr.kontas.erp.purchase.platform.persistence.vendorinvoice.JpaVendorInvoiceRepository;

@Component
@RequiredArgsConstructor
public class VendorInvoiceNumberGeneratorImpl implements VendorInvoiceNumberGeneratorPort {

    private final JpaVendorInvoiceRepository jpaRepository;

    @Override
    public VendorInvoiceNumber generate(TenantId tenantId, CompanyId companyId, int year) {
        int nextSeq = jpaRepository.findMaxSequenceByTenantId(tenantId.asUUID()) + 1;
        String value = "VINV-%04d-%06d".formatted(year, nextSeq);
        return new VendorInvoiceNumber(value);
    }
}

