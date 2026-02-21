package tr.kontas.erp.purchase.platform.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.purchase.application.port.GoodsReceiptNumberGeneratorPort;
import tr.kontas.erp.purchase.domain.goodsreceipt.GoodsReceiptNumber;
import tr.kontas.erp.purchase.platform.persistence.goodsreceipt.JpaGoodsReceiptRepository;

@Component
@RequiredArgsConstructor
public class GoodsReceiptNumberGeneratorImpl implements GoodsReceiptNumberGeneratorPort {

    private final JpaGoodsReceiptRepository jpaRepository;

    @Override
    public GoodsReceiptNumber generate(TenantId tenantId, CompanyId companyId, int year) {
        int nextSeq = jpaRepository.findMaxSequenceByTenantId(tenantId.asUUID()) + 1;
        String value = "GR-%04d-%06d".formatted(year, nextSeq);
        return new GoodsReceiptNumber(value);
    }
}

