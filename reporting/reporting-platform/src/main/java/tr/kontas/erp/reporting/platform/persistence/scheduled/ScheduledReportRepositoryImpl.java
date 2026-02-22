package tr.kontas.erp.reporting.platform.persistence.scheduled;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.reporting.domain.scheduled.ScheduledReport;
import tr.kontas.erp.reporting.domain.scheduled.ScheduledReportId;
import tr.kontas.erp.reporting.domain.scheduled.ScheduledReportRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ScheduledReportRepositoryImpl implements ScheduledReportRepository {

    private final JpaScheduledReportRepository jpaRepository;

    @Override
    public void save(ScheduledReport report) {
        jpaRepository.save(ScheduledReportMapper.toJpa(report));
    }

    @Override
    public Optional<ScheduledReport> findById(ScheduledReportId id, TenantId tenantId) {
        return jpaRepository.findById(id.asUUID()).map(ScheduledReportMapper::toDomain);
    }

    @Override
    public List<ScheduledReport> findByTenantId(TenantId tenantId) {
        return jpaRepository.findByTenantId(tenantId.asUUID()).stream()
                .map(ScheduledReportMapper::toDomain)
                .toList();
    }

    @Override
    public List<ScheduledReport> findActive() {
        return jpaRepository.findByActiveTrue().stream()
                .map(ScheduledReportMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(ScheduledReportId id) {
        jpaRepository.deleteById(id.asUUID());
    }
}

