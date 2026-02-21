package tr.kontas.erp.hr.domain.leavebalance;

import lombok.Getter;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.domain.leavepolicy.LeaveType;

@Getter
public class LeaveBalance extends AggregateRoot<LeaveBalanceId> {

    private final TenantId tenantId;
    private final CompanyId companyId;
    private final String employeeId;
    private final LeaveType leaveType;
    private final int year;
    private int entitlementDays;
    private int usedDays;
    private int carryoverDays;
    private int pendingDays;

    public LeaveBalance(LeaveBalanceId id, TenantId tenantId, CompanyId companyId,
                        String employeeId, LeaveType leaveType, int year,
                        int entitlementDays, int usedDays, int carryoverDays,
                        int pendingDays) {
        super(id);
        this.tenantId = tenantId;
        this.companyId = companyId;
        this.employeeId = employeeId;
        this.leaveType = leaveType;
        this.year = year;
        this.entitlementDays = entitlementDays;
        this.usedDays = usedDays;
        this.carryoverDays = carryoverDays;
        this.pendingDays = pendingDays;
    }


    public int getRemainingDays() {
        return entitlementDays + carryoverDays - usedDays - pendingDays;
    }


    public void debit(int days) {
        if (days > getRemainingDays()) {
            throw new IllegalStateException("Insufficient leave balance");
        }
        this.pendingDays += days;
    }


    public void confirm(int days) {
        this.pendingDays -= days;
        this.usedDays += days;
    }


    public void credit(int days) {
        this.pendingDays -= days;
    }


    public void carryover(int days) {
        this.carryoverDays += days;
    }


    public void setEntitlementDays(int days) {
        this.entitlementDays = days;
    }
}

