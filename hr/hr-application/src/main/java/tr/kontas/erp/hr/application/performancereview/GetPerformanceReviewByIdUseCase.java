package tr.kontas.erp.hr.application.performancereview;

import tr.kontas.erp.hr.domain.performancereview.PerformanceReview;
import tr.kontas.erp.hr.domain.performancereview.PerformanceReviewId;

public interface GetPerformanceReviewByIdUseCase {
    PerformanceReview execute(PerformanceReviewId id);
}
