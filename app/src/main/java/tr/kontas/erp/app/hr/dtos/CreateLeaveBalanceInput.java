package tr.kontas.erp.app.hr.dtos;

import lombok.Data;

@Data
public class CreateLeaveBalanceInput {
    private String companyId;
    private String employeeId;
    private String leaveType;
    private int year;
    private int entitlementDays;
    private int carryoverDays;
}
