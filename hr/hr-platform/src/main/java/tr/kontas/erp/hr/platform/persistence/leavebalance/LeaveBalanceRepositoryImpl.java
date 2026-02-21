package tr.kontas.erp.hr.platform.persistence.leavebalance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.domain.leavebalance.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class LeaveBalanceRepositoryImpl implements LeaveBalanceRepository {

    private final JpaLeaveBalanceRepository jpaRepository;

    @Override
    public void save(LeaveBalance entity) {
        jpaRepository.save(LeaveBalanceMapper.toEntity(entity));
    }

    @Override
    public Optional<LeaveBalance> findById(LeaveBalanceId id, TenantId tenantId) {
        return jpaRepository.findByTenantIdAndId(tenantId.asUUID(), id.asUUID())
                .stream().findFirst().map(LeaveBalanceMapper::toDomain);
    }

    @Override
    public List<LeaveBalance> findByCompanyId(TenantId tenantId, CompanyId companyId) {
        return jpaRepository.findByTenantIdAndCompanyId(tenantId.asUUID(), companyId.asUUID())
                .stream().map(LeaveBalanceMapper::toDomain).toList();
    }

    @Override
    public List<LeaveBalance> findByIds(List<LeaveBalanceId> ids) {
        List<UUID> uuids = ids.stream().map(LeaveBalanceId::asUUID).toList();
        return jpaRepository.findAllById(uuids).stream().map(LeaveBalanceMapper::toDomain).toList();
    }
}
