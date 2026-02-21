package tr.kontas.erp.hr.application.performancecycle;

import tr.kontas.erp.core.domain.company.CompanyId;

import java.time.LocalDate;
import java.util.List;

public record CreatePerformanceCycleCommand(CompanyId companyId, String name, LocalDate startDate, LocalDate endDate, LocalDate reviewDeadline, List<GoalInput> goals) {
    public record GoalInput(String description, int targetScore) {}
}
