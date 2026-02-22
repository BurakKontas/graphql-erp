package tr.kontas.erp.reporting.platform.persistence.saved;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.reporting.domain.saved.SavedReport;
import tr.kontas.erp.reporting.domain.saved.SavedReportId;
import tr.kontas.erp.reporting.domain.saved.SavedReportRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SavedReportRepositoryImpl implements SavedReportRepository {

    private final JpaSavedReportRepository jpaRepository;

    @Override
    public void save(SavedReport report) {
        jpaRepository.save(SavedReportMapper.toJpa(report));
    }

    @Override
    public Optional<SavedReport> findById(SavedReportId id, TenantId tenantId) {
        return jpaRepository.findById(id.asUUID()).map(SavedReportMapper::toDomain);
    }

    @Override
    public List<SavedReport> findByCreatedBy(String userId, TenantId tenantId) {
        return jpaRepository.findByCreatedByAndTenantId(userId, tenantId.asUUID()).stream()
                .map(SavedReportMapper::toDomain)
                .toList();
    }

    @Override
    public List<SavedReport> findShared(TenantId tenantId) {
        return jpaRepository.findBySharedTrueAndTenantId(tenantId.asUUID()).stream()
                .map(SavedReportMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(SavedReportId id) {
        jpaRepository.deleteById(id.asUUID());
    }
}

