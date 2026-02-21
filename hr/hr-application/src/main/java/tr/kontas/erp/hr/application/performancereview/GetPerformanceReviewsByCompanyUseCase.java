package tr.kontas.erp.hr.application.performancereview;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.hr.domain.performancereview.PerformanceReview;

import java.util.List;

public interface GetPerformanceReviewsByCompanyUseCase {
    List<PerformanceReview> execute(CompanyId companyId);
}
