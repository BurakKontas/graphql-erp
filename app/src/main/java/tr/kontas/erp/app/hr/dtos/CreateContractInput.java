package tr.kontas.erp.app.hr.dtos;

import lombok.Data;

@Data
public class CreateContractInput {
    private String companyId;
    private String employeeId;
    private String startDate;
    private String endDate;
    private String contractType;
    private String grossSalary;
    private String currencyCode;
    private String payrollConfigId;
}
