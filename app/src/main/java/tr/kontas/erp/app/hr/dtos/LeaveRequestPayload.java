package tr.kontas.erp.app.hr.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LeaveRequestPayload {
    private String id;
    private String companyId;
    private String requestNumber;
    private String employeeId;
    private String approverId;
    private String leaveType;
    private String startDate;
    private String endDate;
    private int requestedDays;
    private String status;
    private String reason;
    private String documentRef;
    private String rejectionReason;
}
