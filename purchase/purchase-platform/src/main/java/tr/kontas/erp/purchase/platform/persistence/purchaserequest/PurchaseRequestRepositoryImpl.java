package tr.kontas.erp.purchase.platform.persistence.purchaserequest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.purchase.domain.purchaserequest.PurchaseRequest;
import tr.kontas.erp.purchase.domain.purchaserequest.PurchaseRequestId;
import tr.kontas.erp.purchase.domain.purchaserequest.PurchaseRequestRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PurchaseRequestRepositoryImpl implements PurchaseRequestRepository {

    private final JpaPurchaseRequestRepository jpaRepository;

    @Override
    public void save(PurchaseRequest request) {
        jpaRepository.save(PurchaseRequestMapper.toEntity(request));
    }

    @Override
    public Optional<PurchaseRequest> findById(PurchaseRequestId id, TenantId tenantId) {
        return jpaRepository.findByIdAndTenantId(id.asUUID(), tenantId.asUUID())
                .map(PurchaseRequestMapper::toDomain);
    }

    @Override
    public List<PurchaseRequest> findByCompanyId(TenantId tenantId, CompanyId companyId) {
        return jpaRepository.findByTenantIdAndCompanyId(tenantId.asUUID(), companyId.asUUID())
                .stream()
                .map(PurchaseRequestMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<PurchaseRequest> findByIds(List<PurchaseRequestId> ids) {
        List<UUID> uuids = ids.stream().map(PurchaseRequestId::asUUID).toList();
        return jpaRepository.findByIdIn(uuids)
                .stream()
                .map(PurchaseRequestMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public int findMaxSequenceByTenantId(TenantId tenantId) {
        return jpaRepository.findMaxSequenceByTenantId(tenantId.asUUID());
    }
}

