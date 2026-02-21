package tr.kontas.erp.app.hr.dtos;

import lombok.Data;

@Data
public class CreateLeaveRequestInput {
    private String companyId;
    private String employeeId;
    private String leaveType;
    private String startDate;
    private String endDate;
    private int requestedDays;
    private String reason;
    private String documentRef;
}
