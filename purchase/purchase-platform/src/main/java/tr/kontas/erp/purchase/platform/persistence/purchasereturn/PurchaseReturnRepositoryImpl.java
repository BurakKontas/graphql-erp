package tr.kontas.erp.purchase.platform.persistence.purchasereturn;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.purchase.domain.purchasereturn.PurchaseReturn;
import tr.kontas.erp.purchase.domain.purchasereturn.PurchaseReturnId;
import tr.kontas.erp.purchase.domain.purchasereturn.PurchaseReturnRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PurchaseReturnRepositoryImpl implements PurchaseReturnRepository {

    private final JpaPurchaseReturnRepository jpaRepository;

    @Override
    public void save(PurchaseReturn ret) {
        jpaRepository.save(PurchaseReturnMapper.toEntity(ret));
    }

    @Override
    public Optional<PurchaseReturn> findById(PurchaseReturnId id, TenantId tenantId) {
        return jpaRepository.findByIdAndTenantId(id.asUUID(), tenantId.asUUID())
                .map(PurchaseReturnMapper::toDomain);
    }

    @Override
    public List<PurchaseReturn> findByCompanyId(TenantId tenantId, CompanyId companyId) {
        return jpaRepository.findByTenantIdAndCompanyId(tenantId.asUUID(), companyId.asUUID())
                .stream()
                .map(PurchaseReturnMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<PurchaseReturn> findByIds(List<PurchaseReturnId> ids) {
        List<UUID> uuids = ids.stream().map(PurchaseReturnId::asUUID).toList();
        return jpaRepository.findByIdIn(uuids)
                .stream()
                .map(PurchaseReturnMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public int findMaxSequenceByTenantId(TenantId tenantId) {
        return jpaRepository.findMaxSequenceByTenantId(tenantId.asUUID());
    }
}

