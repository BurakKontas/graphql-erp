package tr.kontas.erp.hr.domain.performancereview;

import lombok.Getter;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class PerformanceReview extends AggregateRoot<PerformanceReviewId> {

    private final TenantId tenantId;
    private final CompanyId companyId;
    private final String cycleId;
    private final String employeeId;
    private String reviewerId;
    private ReviewStatus status;
    private int overallRating;
    private String strengths;
    private String improvements;
    private String comments;
    private final List<GoalReview> goalReviews;

    public PerformanceReview(PerformanceReviewId id, TenantId tenantId, CompanyId companyId,
                             String cycleId, String employeeId, String reviewerId,
                             ReviewStatus status, int overallRating,
                             String strengths, String improvements, String comments,
                             List<GoalReview> goalReviews) {
        super(id);
        this.tenantId = tenantId;
        this.companyId = companyId;
        this.cycleId = cycleId;
        this.employeeId = employeeId;
        this.reviewerId = reviewerId;
        this.status = status;
        this.overallRating = overallRating;
        this.strengths = strengths;
        this.improvements = improvements;
        this.comments = comments;
        this.goalReviews = new ArrayList<>(goalReviews != null ? goalReviews : List.of());
    }


    public List<GoalReview> getGoalReviews() {
        return Collections.unmodifiableList(goalReviews);
    }


    public void submitSelfReview(String comments) {
        if (status != ReviewStatus.PENDING) {
            throw new IllegalStateException("Can only submit self-review from PENDING");
        }
        this.comments = comments;
        this.status = ReviewStatus.SELF_REVIEW;
    }


    public void submitManagerReview(int overallRating, List<GoalReview> reviews, String strengths, String improvements) {
        if (status != ReviewStatus.SELF_REVIEW) {
            throw new IllegalStateException("Can only submit manager review from SELF_REVIEW");
        }
        this.overallRating = overallRating;
        this.goalReviews.clear();
        this.goalReviews.addAll(reviews);
        this.strengths = strengths;
        this.improvements = improvements;
        this.status = ReviewStatus.MANAGER_REVIEW;
    }


    public void complete() {
        if (status != ReviewStatus.MANAGER_REVIEW) {
            throw new IllegalStateException("Can only complete from MANAGER_REVIEW");
        }
        this.status = ReviewStatus.COMPLETED;
    }


    public void acknowledge() {
        if (status != ReviewStatus.COMPLETED) {
            throw new IllegalStateException("Can only acknowledge COMPLETED reviews");
        }
        this.status = ReviewStatus.ACKNOWLEDGED;
    }
}

