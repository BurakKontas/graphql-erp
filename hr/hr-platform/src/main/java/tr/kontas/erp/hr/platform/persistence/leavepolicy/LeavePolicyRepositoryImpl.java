package tr.kontas.erp.hr.platform.persistence.leavepolicy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.domain.leavepolicy.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class LeavePolicyRepositoryImpl implements LeavePolicyRepository {

    private final JpaLeavePolicyRepository jpaRepository;

    @Override
    public void save(LeavePolicy entity) {
        jpaRepository.save(LeavePolicyMapper.toEntity(entity));
    }

    @Override
    public Optional<LeavePolicy> findById(LeavePolicyId id, TenantId tenantId) {
        return jpaRepository.findByTenantIdAndId(tenantId.asUUID(), id.asUUID())
                .stream().findFirst().map(LeavePolicyMapper::toDomain);
    }

    @Override
    public List<LeavePolicy> findByCompanyId(TenantId tenantId, CompanyId companyId) {
        return jpaRepository.findByTenantIdAndCompanyId(tenantId.asUUID(), companyId.asUUID())
                .stream().map(LeavePolicyMapper::toDomain).toList();
    }

    @Override
    public List<LeavePolicy> findByIds(List<LeavePolicyId> ids) {
        List<UUID> uuids = ids.stream().map(LeavePolicyId::asUUID).toList();
        return jpaRepository.findAllById(uuids).stream().map(LeavePolicyMapper::toDomain).toList();
    }
}
