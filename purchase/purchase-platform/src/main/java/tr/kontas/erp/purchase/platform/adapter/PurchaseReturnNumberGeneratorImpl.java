package tr.kontas.erp.purchase.platform.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.purchase.application.port.PurchaseReturnNumberGeneratorPort;
import tr.kontas.erp.purchase.domain.purchasereturn.PurchaseReturnNumber;
import tr.kontas.erp.purchase.platform.persistence.purchasereturn.JpaPurchaseReturnRepository;

@Component
@RequiredArgsConstructor
public class PurchaseReturnNumberGeneratorImpl implements PurchaseReturnNumberGeneratorPort {

    private final JpaPurchaseReturnRepository jpaRepository;

    @Override
    public PurchaseReturnNumber generate(TenantId tenantId, CompanyId companyId, int year) {
        int nextSeq = jpaRepository.findMaxSequenceByTenantId(tenantId.asUUID()) + 1;
        String value = "PR-RET-%04d-%06d".formatted(year, nextSeq);
        return new PurchaseReturnNumber(value);
    }
}

