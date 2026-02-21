package tr.kontas.erp.hr.domain.performancereview;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GoalReview {
    private final String goalDescription;
    private final int targetScore;
    private int achievedScore;
    private String comment;
}

