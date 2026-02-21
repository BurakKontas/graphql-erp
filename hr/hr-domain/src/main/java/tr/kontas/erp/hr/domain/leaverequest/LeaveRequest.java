package tr.kontas.erp.hr.domain.leaverequest;

import lombok.Getter;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.domain.leavepolicy.LeaveType;

import java.time.LocalDate;

@Getter
public class LeaveRequest extends AggregateRoot<LeaveRequestId> {

    private final TenantId tenantId;
    private final CompanyId companyId;
    private final LeaveRequestNumber requestNumber;
    private final String employeeId;
    private String approverId;
    private LeaveType leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private int requestedDays;
    private LeaveRequestStatus status;
    private String reason;
    private String documentRef;
    private String rejectionReason;

    public LeaveRequest(LeaveRequestId id, TenantId tenantId, CompanyId companyId,
                        LeaveRequestNumber requestNumber, String employeeId,
                        String approverId, LeaveType leaveType,
                        LocalDate startDate, LocalDate endDate, int requestedDays,
                        LeaveRequestStatus status, String reason,
                        String documentRef, String rejectionReason) {
        super(id);
        this.tenantId = tenantId;
        this.companyId = companyId;
        this.requestNumber = requestNumber;
        this.employeeId = employeeId;
        this.approverId = approverId;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.requestedDays = requestedDays;
        this.status = status;
        this.reason = reason;
        this.documentRef = documentRef;
        this.rejectionReason = rejectionReason;
    }


    public void submit() {
        if (status != LeaveRequestStatus.DRAFT) {
            throw new IllegalStateException("Can only submit DRAFT leave requests");
        }
        this.status = LeaveRequestStatus.SUBMITTED;
    }


    public void approve(String approverId) {
        if (status != LeaveRequestStatus.SUBMITTED) {
            throw new IllegalStateException("Can only approve SUBMITTED leave requests");
        }
        this.approverId = approverId;
        this.status = LeaveRequestStatus.APPROVED;
    }


    public void reject(String approverId, String reason) {
        if (status != LeaveRequestStatus.SUBMITTED) {
            throw new IllegalStateException("Can only reject SUBMITTED leave requests");
        }
        this.approverId = approverId;
        this.rejectionReason = reason;
        this.status = LeaveRequestStatus.REJECTED;
    }


    public void cancel(String reason) {
        if (status == LeaveRequestStatus.REJECTED || status == LeaveRequestStatus.CANCELLED) {
            throw new IllegalStateException("Cannot cancel leave request in status: " + status);
        }
        this.reason = reason;
        this.status = LeaveRequestStatus.CANCELLED;
    }
}

