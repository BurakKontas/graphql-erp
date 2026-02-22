package tr.kontas.erp.hr.platform.persistence.performancereview;

import com.fasterxml.jackson.core.type.TypeReference;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.configuration.JacksonProvider;
import tr.kontas.erp.hr.domain.performancereview.*;

import java.util.List;

public class PerformanceReviewMapper {

    public static PerformanceReviewJpaEntity toEntity(PerformanceReview pr) {
        PerformanceReviewJpaEntity e = new PerformanceReviewJpaEntity();
        e.setId(pr.getId().asUUID());
        e.setTenantId(pr.getTenantId().asUUID());
        e.setCompanyId(pr.getCompanyId().asUUID());
        e.setCycleId(pr.getCycleId());
        e.setEmployeeId(pr.getEmployeeId());
        e.setReviewerId(pr.getReviewerId());
        e.setStatus(pr.getStatus().name());
        e.setOverallRating(pr.getOverallRating());
        e.setStrengths(pr.getStrengths());
        e.setImprovements(pr.getImprovements());
        e.setComments(pr.getComments());
        e.setGoalReviewsJson(JacksonProvider.serialize(pr.getGoalReviews()));
        return e;
    }

    public static PerformanceReview toDomain(PerformanceReviewJpaEntity e) {
        List<GoalReview> reviews = e.getGoalReviewsJson() != null
                ? JacksonProvider.deserialize(e.getGoalReviewsJson(), new TypeReference<>() {}) : List.of();
        return new PerformanceReview(
                PerformanceReviewId.of(e.getId()), TenantId.of(e.getTenantId()), CompanyId.of(e.getCompanyId()),
                e.getCycleId(), e.getEmployeeId(), e.getReviewerId(),
                ReviewStatus.valueOf(e.getStatus()), e.getOverallRating(),
                e.getStrengths(), e.getImprovements(), e.getComments(), reviews);
    }
}
