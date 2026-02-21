package tr.kontas.erp.hr.domain.position;

import lombok.Getter;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

@Getter
public class Position extends AggregateRoot<PositionId> {

    private final TenantId tenantId;
    private final CompanyId companyId;
    private final PositionCode code;
    private PositionTitle title;
    private String departmentId;
    private PositionLevel level;
    private SalaryGrade salaryGrade;
    private int headcount;
    private int filledCount;
    private PositionStatus status;

    public Position(PositionId id, TenantId tenantId, CompanyId companyId,
                    PositionCode code, PositionTitle title, String departmentId,
                    PositionLevel level, SalaryGrade salaryGrade,
                    int headcount, int filledCount, PositionStatus status) {
        super(id);
        this.tenantId = tenantId;
        this.companyId = companyId;
        this.code = code;
        this.title = title;
        this.departmentId = departmentId;
        this.level = level;
        this.salaryGrade = salaryGrade;
        this.headcount = headcount;
        this.filledCount = filledCount;
        this.status = status;
    }


    public void freeze() {
        if (status != PositionStatus.ACTIVE) {
            throw new IllegalStateException("Can only freeze ACTIVE positions");
        }
        this.status = PositionStatus.FROZEN;
    }


    public void close() {
        if (status != PositionStatus.FROZEN) {
            throw new IllegalStateException("Can only close FROZEN positions");
        }
        this.status = PositionStatus.CLOSED;
    }


    public void activate() {
        if (status != PositionStatus.FROZEN) {
            throw new IllegalStateException("Can only activate FROZEN positions");
        }
        this.status = PositionStatus.ACTIVE;
    }


    public boolean isVacant() {
        return filledCount < headcount;
    }


    public void incrementFilled() {
        if (filledCount >= headcount) {
            throw new IllegalStateException("Position is fully filled");
        }
        this.filledCount++;
    }


    public void decrementFilled() {
        if (filledCount <= 0) {
            throw new IllegalStateException("No filled positions to decrement");
        }
        this.filledCount--;
    }


    public void updateTitle(PositionTitle title) {
        this.title = title;
    }


    public void updateLevel(PositionLevel level) {
        this.level = level;
    }
}
