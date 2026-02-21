package tr.kontas.erp.purchase.platform.persistence.goodsreceipt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.purchase.domain.goodsreceipt.GoodsReceipt;
import tr.kontas.erp.purchase.domain.goodsreceipt.GoodsReceiptId;
import tr.kontas.erp.purchase.domain.goodsreceipt.GoodsReceiptRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class GoodsReceiptRepositoryImpl implements GoodsReceiptRepository {

    private final JpaGoodsReceiptRepository jpaRepository;

    @Override
    public void save(GoodsReceipt receipt) {
        jpaRepository.save(GoodsReceiptMapper.toEntity(receipt));
    }

    @Override
    public Optional<GoodsReceipt> findById(GoodsReceiptId id, TenantId tenantId) {
        return jpaRepository.findByIdAndTenantId(id.asUUID(), tenantId.asUUID())
                .map(GoodsReceiptMapper::toDomain);
    }

    @Override
    public List<GoodsReceipt> findByCompanyId(TenantId tenantId, CompanyId companyId) {
        return jpaRepository.findByTenantIdAndCompanyId(tenantId.asUUID(), companyId.asUUID())
                .stream()
                .map(GoodsReceiptMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<GoodsReceipt> findByIds(List<GoodsReceiptId> ids) {
        List<UUID> uuids = ids.stream().map(GoodsReceiptId::asUUID).toList();
        return jpaRepository.findByIdIn(uuids)
                .stream()
                .map(GoodsReceiptMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public int findMaxSequenceByTenantId(TenantId tenantId) {
        return jpaRepository.findMaxSequenceByTenantId(tenantId.asUUID());
    }
}

