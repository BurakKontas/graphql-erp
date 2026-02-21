package tr.kontas.erp.purchase.platform.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.purchase.application.port.PurchaseOrderNumberGeneratorPort;
import tr.kontas.erp.purchase.domain.purchaseorder.PurchaseOrderNumber;
import tr.kontas.erp.purchase.platform.persistence.purchaseorder.JpaPurchaseOrderRepository;

@Component
@RequiredArgsConstructor
public class PurchaseOrderNumberGeneratorImpl implements PurchaseOrderNumberGeneratorPort {

    private final JpaPurchaseOrderRepository jpaRepository;

    @Override
    public PurchaseOrderNumber generate(TenantId tenantId, CompanyId companyId, int year) {
        int nextSeq = jpaRepository.findMaxSequenceByTenantId(tenantId.asUUID()) + 1;
        String value = "PO-%04d-%06d".formatted(year, nextSeq);
        return new PurchaseOrderNumber(value);
    }
}

