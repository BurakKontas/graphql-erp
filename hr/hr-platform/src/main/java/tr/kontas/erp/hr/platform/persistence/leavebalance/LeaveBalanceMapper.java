package tr.kontas.erp.hr.platform.persistence.leavebalance;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.domain.leavebalance.*;
import tr.kontas.erp.hr.domain.leavepolicy.LeaveType;

public class LeaveBalanceMapper {
    public static LeaveBalanceJpaEntity toEntity(LeaveBalance lb) {
        LeaveBalanceJpaEntity e = new LeaveBalanceJpaEntity();
        e.setId(lb.getId().asUUID());
        e.setTenantId(lb.getTenantId().asUUID());
        e.setCompanyId(lb.getCompanyId().asUUID());
        e.setEmployeeId(lb.getEmployeeId());
        e.setLeaveType(lb.getLeaveType().name());
        e.setYear(lb.getYear());
        e.setEntitlementDays(lb.getEntitlementDays());
        e.setUsedDays(lb.getUsedDays());
        e.setCarryoverDays(lb.getCarryoverDays());
        e.setPendingDays(lb.getPendingDays());
        return e;
    }
    public static LeaveBalance toDomain(LeaveBalanceJpaEntity e) {
        return new LeaveBalance(
                LeaveBalanceId.of(e.getId()), TenantId.of(e.getTenantId()), CompanyId.of(e.getCompanyId()),
                e.getEmployeeId(), LeaveType.valueOf(e.getLeaveType()), e.getYear(),
                e.getEntitlementDays(), e.getUsedDays(), e.getCarryoverDays(), e.getPendingDays());
    }
}
