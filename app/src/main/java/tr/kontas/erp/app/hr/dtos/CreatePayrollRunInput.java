package tr.kontas.erp.app.hr.dtos;

import lombok.Data;

@Data
public class CreatePayrollRunInput {
    private String companyId;
    private int year;
    private int month;
    private String paymentDate;
    private String payrollConfigId;
}
