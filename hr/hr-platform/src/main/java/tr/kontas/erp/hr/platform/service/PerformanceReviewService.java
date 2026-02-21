package tr.kontas.erp.hr.platform.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.application.performancereview.*;
import tr.kontas.erp.hr.domain.performancereview.*;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PerformanceReviewService implements CreatePerformanceReviewUseCase, GetPerformanceReviewByIdUseCase,
        GetPerformanceReviewsByCompanyUseCase, GetPerformanceReviewsByIdsUseCase {

    private final PerformanceReviewRepository performanceReviewRepository;

    @Override
    public PerformanceReviewId execute(CreatePerformanceReviewCommand cmd) {
        TenantId tenantId = TenantContext.get();
        PerformanceReviewId id = PerformanceReviewId.newId();
        PerformanceReview review = new PerformanceReview(id, tenantId, cmd.companyId(), cmd.cycleId(),
                cmd.employeeId(), cmd.reviewerId(), ReviewStatus.PENDING, 0, null, null, null, List.of());
        performanceReviewRepository.save(review);
        return id;
    }

    @Override
    @Transactional(readOnly = true)
    public PerformanceReview execute(PerformanceReviewId id) {
        return performanceReviewRepository.findById(id, TenantContext.get())
                .orElseThrow(() -> new IllegalArgumentException("PerformanceReview not found: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PerformanceReview> execute(CompanyId companyId) {
        return performanceReviewRepository.findByCompanyId(TenantContext.get(), companyId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PerformanceReview> execute(List<PerformanceReviewId> ids) {
        return performanceReviewRepository.findByIds(ids);
    }
}
