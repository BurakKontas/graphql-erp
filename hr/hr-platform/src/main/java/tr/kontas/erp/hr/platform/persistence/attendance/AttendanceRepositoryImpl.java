package tr.kontas.erp.hr.platform.persistence.attendance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.domain.attendance.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AttendanceRepositoryImpl implements AttendanceRepository {

    private final JpaAttendanceRepository jpaRepository;

    @Override
    public void save(Attendance entity) {
        jpaRepository.save(AttendanceMapper.toEntity(entity));
    }

    @Override
    public Optional<Attendance> findById(AttendanceId id, TenantId tenantId) {
        return jpaRepository.findByTenantIdAndId(tenantId.asUUID(), id.asUUID())
                .stream().findFirst().map(AttendanceMapper::toDomain);
    }

    @Override
    public List<Attendance> findByCompanyId(TenantId tenantId, CompanyId companyId) {
        return jpaRepository.findByTenantIdAndCompanyId(tenantId.asUUID(), companyId.asUUID())
                .stream().map(AttendanceMapper::toDomain).toList();
    }

    @Override
    public List<Attendance> findByIds(List<AttendanceId> ids) {
        List<UUID> uuids = ids.stream().map(AttendanceId::asUUID).toList();
        return jpaRepository.findAllById(uuids).stream().map(AttendanceMapper::toDomain).toList();
    }
}
