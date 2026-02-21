package tr.kontas.erp.hr.domain.performancecycle;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PerformanceGoal {
    private final String description;
    private final int targetScore;
}

