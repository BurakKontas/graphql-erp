package tr.kontas.erp.hr.application.performancereview;

import tr.kontas.erp.core.domain.company.CompanyId;

public record CreatePerformanceReviewCommand(CompanyId companyId, String cycleId, String employeeId, String reviewerId) {}
