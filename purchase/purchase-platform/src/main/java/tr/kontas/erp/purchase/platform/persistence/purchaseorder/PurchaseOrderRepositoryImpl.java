package tr.kontas.erp.purchase.platform.persistence.purchaseorder;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.purchase.domain.purchaseorder.PurchaseOrder;
import tr.kontas.erp.purchase.domain.purchaseorder.PurchaseOrderId;
import tr.kontas.erp.purchase.domain.purchaseorder.PurchaseOrderRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PurchaseOrderRepositoryImpl implements PurchaseOrderRepository {

    private final JpaPurchaseOrderRepository jpaRepository;

    @Override
    public void save(PurchaseOrder order) {
        jpaRepository.save(PurchaseOrderMapper.toEntity(order));
    }

    @Override
    public Optional<PurchaseOrder> findById(PurchaseOrderId id, TenantId tenantId) {
        return jpaRepository.findByIdAndTenantId(id.asUUID(), tenantId.asUUID())
                .map(PurchaseOrderMapper::toDomain);
    }

    @Override
    public List<PurchaseOrder> findByCompanyId(TenantId tenantId, CompanyId companyId) {
        return jpaRepository.findByTenantIdAndCompanyId(tenantId.asUUID(), companyId.asUUID())
                .stream()
                .map(PurchaseOrderMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<PurchaseOrder> findByIds(List<PurchaseOrderId> ids) {
        List<UUID> uuids = ids.stream().map(PurchaseOrderId::asUUID).toList();
        return jpaRepository.findByIdIn(uuids)
                .stream()
                .map(PurchaseOrderMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public int findMaxSequenceByTenantId(TenantId tenantId) {
        return jpaRepository.findMaxSequenceByTenantId(tenantId.asUUID());
    }
}

