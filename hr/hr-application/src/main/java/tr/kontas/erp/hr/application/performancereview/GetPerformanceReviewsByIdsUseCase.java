package tr.kontas.erp.hr.application.performancereview;

import tr.kontas.erp.hr.domain.performancereview.PerformanceReview;
import tr.kontas.erp.hr.domain.performancereview.PerformanceReviewId;

import java.util.List;

public interface GetPerformanceReviewsByIdsUseCase {
    List<PerformanceReview> execute(List<PerformanceReviewId> ids);
}
