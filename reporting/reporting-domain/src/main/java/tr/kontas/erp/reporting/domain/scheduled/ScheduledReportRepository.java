package tr.kontas.erp.reporting.domain.scheduled;

import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface ScheduledReportRepository {
    void save(ScheduledReport report);
    Optional<ScheduledReport> findById(ScheduledReportId id, TenantId tenantId);
    List<ScheduledReport> findByTenantId(TenantId tenantId);
    List<ScheduledReport> findActive();
    void deleteById(ScheduledReportId id);
}

