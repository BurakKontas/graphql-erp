package tr.kontas.erp.hr.platform.persistence.leaverequest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.domain.leaverequest.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class LeaveRequestRepositoryImpl implements LeaveRequestRepository {

    private final JpaLeaveRequestRepository jpaRepository;

    @Override
    public void save(LeaveRequest entity) {
        jpaRepository.save(LeaveRequestMapper.toEntity(entity));
    }

    @Override
    public Optional<LeaveRequest> findById(LeaveRequestId id, TenantId tenantId) {
        return jpaRepository.findByTenantIdAndId(tenantId.asUUID(), id.asUUID())
                .stream().findFirst().map(LeaveRequestMapper::toDomain);
    }

    @Override
    public List<LeaveRequest> findByCompanyId(TenantId tenantId, CompanyId companyId) {
        return jpaRepository.findByTenantIdAndCompanyId(tenantId.asUUID(), companyId.asUUID())
                .stream().map(LeaveRequestMapper::toDomain).toList();
    }

    @Override
    public List<LeaveRequest> findByIds(List<LeaveRequestId> ids) {
        List<UUID> uuids = ids.stream().map(LeaveRequestId::asUUID).toList();
        return jpaRepository.findAllById(uuids).stream().map(LeaveRequestMapper::toDomain).toList();
    }
}
