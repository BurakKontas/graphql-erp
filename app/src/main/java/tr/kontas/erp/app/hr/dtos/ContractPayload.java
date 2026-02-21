package tr.kontas.erp.app.hr.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ContractPayload {
    private String id;
    private String companyId;
    private String contractNumber;
    private String employeeId;
    private String startDate;
    private String endDate;
    private String contractType;
    private String grossSalary;
    private String currencyCode;
    private String payrollConfigId;
    private String status;
}
