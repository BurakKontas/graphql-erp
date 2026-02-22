package tr.kontas.erp.reporting.domain.saved;

import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface SavedReportRepository {
    void save(SavedReport report);
    Optional<SavedReport> findById(SavedReportId id, TenantId tenantId);
    List<SavedReport> findByCreatedBy(String userId, TenantId tenantId);
    List<SavedReport> findShared(TenantId tenantId);
    void deleteById(SavedReportId id);
}

