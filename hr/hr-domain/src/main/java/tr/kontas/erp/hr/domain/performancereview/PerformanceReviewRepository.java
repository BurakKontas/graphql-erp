package tr.kontas.erp.hr.domain.performancereview;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface PerformanceReviewRepository {
    void save(PerformanceReview entity);
    Optional<PerformanceReview> findById(PerformanceReviewId id, TenantId tenantId);
    List<PerformanceReview> findByCompanyId(TenantId tenantId, CompanyId companyId);
    List<PerformanceReview> findByIds(List<PerformanceReviewId> ids);
}
