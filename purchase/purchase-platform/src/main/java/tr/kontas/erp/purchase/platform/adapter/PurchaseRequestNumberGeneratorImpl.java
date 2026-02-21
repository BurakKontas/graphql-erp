package tr.kontas.erp.purchase.platform.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.purchase.application.port.PurchaseRequestNumberGeneratorPort;
import tr.kontas.erp.purchase.domain.purchaserequest.PurchaseRequestNumber;
import tr.kontas.erp.purchase.platform.persistence.purchaserequest.JpaPurchaseRequestRepository;

@Component
@RequiredArgsConstructor
public class PurchaseRequestNumberGeneratorImpl implements PurchaseRequestNumberGeneratorPort {

    private final JpaPurchaseRequestRepository jpaRepository;

    @Override
    public PurchaseRequestNumber generate(TenantId tenantId, CompanyId companyId, int year) {
        int nextSeq = jpaRepository.findMaxSequenceByTenantId(tenantId.asUUID()) + 1;
        String value = "PR-%04d-%06d".formatted(year, nextSeq);
        return new PurchaseRequestNumber(value);
    }
}

