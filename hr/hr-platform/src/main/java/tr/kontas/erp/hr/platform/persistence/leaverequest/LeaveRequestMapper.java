package tr.kontas.erp.hr.platform.persistence.leaverequest;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.domain.leavepolicy.LeaveType;
import tr.kontas.erp.hr.domain.leaverequest.*;

public class LeaveRequestMapper {
    public static LeaveRequestJpaEntity toEntity(LeaveRequest lr) {
        LeaveRequestJpaEntity e = new LeaveRequestJpaEntity();
        e.setId(lr.getId().asUUID());
        e.setTenantId(lr.getTenantId().asUUID());
        e.setCompanyId(lr.getCompanyId().asUUID());
        e.setRequestNumber(lr.getRequestNumber().getValue());
        e.setEmployeeId(lr.getEmployeeId());
        e.setApproverId(lr.getApproverId());
        e.setLeaveType(lr.getLeaveType().name());
        e.setStartDate(lr.getStartDate());
        e.setEndDate(lr.getEndDate());
        e.setRequestedDays(lr.getRequestedDays());
        e.setStatus(lr.getStatus().name());
        e.setReason(lr.getReason());
        e.setDocumentRef(lr.getDocumentRef());
        e.setRejectionReason(lr.getRejectionReason());
        return e;
    }
    public static LeaveRequest toDomain(LeaveRequestJpaEntity e) {
        return new LeaveRequest(
                LeaveRequestId.of(e.getId()), TenantId.of(e.getTenantId()), CompanyId.of(e.getCompanyId()),
                new LeaveRequestNumber(e.getRequestNumber()), e.getEmployeeId(), e.getApproverId(),
                LeaveType.valueOf(e.getLeaveType()), e.getStartDate(), e.getEndDate(), e.getRequestedDays(),
                LeaveRequestStatus.valueOf(e.getStatus()), e.getReason(), e.getDocumentRef(), e.getRejectionReason());
    }
}
