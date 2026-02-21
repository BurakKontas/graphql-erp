package tr.kontas.erp.app.hr.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LeaveBalancePayload {
    private String id;
    private String companyId;
    private String employeeId;
    private String leaveType;
    private int year;
    private int entitlementDays;
    private int usedDays;
    private int carryoverDays;
    private int pendingDays;
    private int remainingDays;
}
