package tr.kontas.erp.hr.application.performancereview;

import tr.kontas.erp.hr.domain.performancereview.PerformanceReviewId;

public interface CreatePerformanceReviewUseCase {
    PerformanceReviewId execute(CreatePerformanceReviewCommand command);
}
