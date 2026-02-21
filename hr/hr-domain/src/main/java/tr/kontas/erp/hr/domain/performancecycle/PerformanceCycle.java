package tr.kontas.erp.hr.domain.performancecycle;

import lombok.Getter;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class PerformanceCycle extends AggregateRoot<PerformanceCycleId> {

    private final TenantId tenantId;
    private final CompanyId companyId;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate reviewDeadline;
    private CycleStatus status;
    private final List<PerformanceGoal> goals;

    public PerformanceCycle(PerformanceCycleId id, TenantId tenantId, CompanyId companyId,
                            String name, LocalDate startDate, LocalDate endDate,
                            LocalDate reviewDeadline, CycleStatus status,
                            List<PerformanceGoal> goals) {
        super(id);
        this.tenantId = tenantId;
        this.companyId = companyId;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reviewDeadline = reviewDeadline;
        this.status = status;
        this.goals = new ArrayList<>(goals != null ? goals : List.of());
    }


    public List<PerformanceGoal> getGoals() {
        return Collections.unmodifiableList(goals);
    }


    public void activate() {
        if (status != CycleStatus.PLANNED) {
            throw new IllegalStateException("Can only activate PLANNED cycles");
        }
        this.status = CycleStatus.ACTIVE;
    }


    public void complete() {
        if (status != CycleStatus.ACTIVE) {
            throw new IllegalStateException("Can only complete ACTIVE cycles");
        }
        this.status = CycleStatus.COMPLETED;
    }


    public void cancel() {
        if (status == CycleStatus.COMPLETED || status == CycleStatus.CANCELLED) {
            throw new IllegalStateException("Cannot cancel cycle in status: " + status);
        }
        this.status = CycleStatus.CANCELLED;
    }


    public void addGoal(PerformanceGoal goal) {
        this.goals.add(goal);
    }
}

