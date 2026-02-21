package tr.kontas.erp.hr.platform.persistence.performancereview;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "performance_reviews")
@Getter
@Setter
@NoArgsConstructor
public class PerformanceReviewJpaEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "cycle_id", nullable = false)
    private String cycleId;

    @Column(name = "employee_id", nullable = false)
    private String employeeId;

    @Column(name = "reviewer_id")
    private String reviewerId;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "overall_rating")
    private int overallRating;

    @Column(name = "strengths")
    private String strengths;

    @Column(name = "improvements")
    private String improvements;

    @Column(name = "comments")
    private String comments;

    @Column(name = "goal_reviews_json", columnDefinition = "TEXT")
    private String goalReviewsJson;
}
